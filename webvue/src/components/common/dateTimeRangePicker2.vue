<template>
  <div class="input-group flatpickr">
    <input type="text" class="form-control border-right-0" data-input :disabled="disabled"/>
    <div class="input-group-append pointer" data-clear v-show="!disabled">
      <span class="input-group-text" style="border-right:none;"><i class='fa mr0 fa-times'></i></span>
    </div>
    <div class="input-group-append pointer" data-toggle>
      <span class="input-group-text"><i class='fa mr0 fa-calendar'></i></span>
    </div>
  </div>
</template>

<script>
  import flatpickr from "../../../static/plugins/flatpickr/js/flatpickr";
  import confirmDatePlugin from "../../../static/plugins/flatpickr/js/confirmDate";
  import monthSelectPlugin from "../../../static/plugins/flatpickr/js/monthSelect";
  import zh_CN from "../../../static/plugins/flatpickr/js/i18n/zh_CN";

  export default {
    name: "dateTimeRangePicker2",
    props: ['value', 'from', 'to', 'type', 'disabled'],
    data: function () {
      var defaultCfg = {
        enableSeconds: true,
        time_24hr: true,
        weekNumbers: true,
        shorthandCurrentMonth: true,
        wrap: true,
        locale: zh_CN,
        plugins: [],
        // onChange: onChange,
        // onConfirm: onConfirm,
        // onClear: onClear,
        // onClose: onClose,
        // onOpen: onOpen
        // Y-m-d H:i:S
      };

      return {
        defaultCfg: defaultCfg,
        cfg: null,
        instance: null,
      }
    },
    methods: {
      // 初始化
      init: function () {
        // 处理id和text
        this.preprocessData();
        // 绑定数据和dom
        this.handleDataAndDom();
      },
      // 预处理数据
      preprocessData: function () {
        this.defaultCfg.onChange = this.innerChange;
        this.defaultCfg.onOpen = this.onOpen;
        this.cfg = {};
        $.extend(true, this.cfg, this.defaultCfg);
        this.setConfig();
        var defValue = [];
        if (this.from || this.value) {
          defValue.push(this.from || this.value);
        }
        if (this.to) {
          defValue.push(this.to);
        }
        this.cfg.defaultDate = defValue;
      },
      // 绑定数据和dom
      handleDataAndDom: function () {
        this.instance = flatpickr(this.$el, this.cfg);
      },
      innerChange: function (selectedDates, dateStr, instance) {
        this.setCstValue(selectedDates, true);
      },
      // 设置值
      setCstValue: function (value, innerChange) {
        // var $obj = $(this.$refs.myselect);
        // // 内部发生变化
        // if (innerChange) {
        // dom值发生变化时同步js对象的值
        if (value[0]) {
          if (this.type == 5) {
            this.$emit('input', value[0].format('HH:mm'));
            this.$emit('update:from', value[0].format('HH:mm'));
          } else if (this.type == 6) {
            this.$emit('input', value[0].format('HH:mm:ss'));
            this.$emit('update:from', value[0].format('HH:mm:ss'));
          } else {
            this.$emit('input', new Date(value[0].getTime()));
            this.$emit('update:from', new Date(value[0].getTime()));
          }
        } else {
          this.$emit('input', null);
          this.$emit('update:from', null);
        }
        if (value[1]) {
          this.$emit('update:to', new Date(value[1].getTime()));
        } else {
          this.$emit('update:to', null);
        }

        // this.$emit('change', value);
        // } else {
        //   $obj.val(value).trigger('change');
        // }
      },
      onOpen: function () {
        if (!this.instance) return;
        var arr = [];
        if (this.instance.selectedDates && !this.instance.selectedDates[0]) {
          arr.push(new Date());
        } else {
          arr.push(this.instance.selectedDates[0]);
        }
        if (this.type == 2 || this.type == 4) {
          if (this.instance.selectedDates && !this.instance.selectedDates[1]) {
            arr.push(new Date());
          } else {
            arr.push(this.instance.selectedDates[1]);
          }
        }
        this.instance.setDate(arr, true);
      },
      setConfig: function () {
        switch (this.type) {
          // 日期
          case 1:
            break;
          // 日期range
          case 2:
            this.cfg.mode = 'range';
            break;
          // 日期时间
          case 3:
            this.cfg.enableTime = true;
            break;
          // 日期时间range
          case 4:
            this.cfg.enableTime = true;
            this.cfg.mode = 'range';
            // this.cfg.defaultDate = [new Date(), new Date(2020, 10, 1, 20, 35, 23)];
            // this.cfg.plugins.push(new confirmDatePlugin({}));
            break;
          // 时分
          case 5:
            this.cfg.enableTime = true;
            this.cfg.noCalendar = true;
            this.cfg.enableSeconds = false;
            break;
          // 时分秒
          case 6:
            this.cfg.enableTime = true;
            this.cfg.noCalendar = true;
            break;
          // 月
          case 7:
            // this.cfg.enableTime = false;
            // this.cfg.noCalendar = true;
            this.cfg.weekNumbers = false;
            this.cfg.plugins.push(
              new monthSelectPlugin({
                shorthand: true, //defaults to false
                dateFormat: "m.y", //defaults to "F Y"
                altFormat: "F Y", //defaults to "F Y"
                // theme: "dark" // defaults to "light"
              }));
            break;
          default:
            break;
        }
      },
      // 销毁控件
      destoryCom: function () {
        if (this.instance) {
          try {
            this.instance.destory();
          } catch (e) {
          }
        }
      }
    },
    mounted: function () {
      this.init();
    },
    watch: {
      // 监听值是否发生变化
      value: function (curVal, oldVal) {
        if (!this.instance) return;
        var oldValue = this.instance.selectedDates && this.instance.selectedDates.length > 0 && this.instance.selectedDates[0] || undefined;
        if (this.value === oldValue) return;
        if (this.value instanceof Date && oldValue instanceof Date && this.value.getTime() === oldValue.getTime()) return;
        var arr = [this.value || this.from];
        if (this.type == 2 || this.type == 4) {
          arr.push(this.to);
        }
        this.instance.setDate(arr, true);
      },
      'from': function (curVal, oldVal) {
        if (!this.instance) return;
        var oldValue = this.instance.selectedDates && this.instance.selectedDates.length > 0 && this.instance.selectedDates[0] || undefined;
        if (this.from === oldValue) return;
        if (this.from instanceof Date && oldValue instanceof Date && this.from.getTime() === oldValue.getTime()) return;
        var arr = [this.from || this.value];
        if (this.type == 2 || this.type == 4) {
          arr.push(this.to);
        }
        this.instance.setDate(arr, true);
      },
      to: function (curVal, oldVal) {
        if (!this.instance) return;
        var oldValue = this.instance.selectedDates && this.instance.selectedDates.length > 0 && this.instance.selectedDates[1] || undefined;
        if (this.to === oldValue) return;
        if (this.to instanceof Date && oldValue instanceof Date && this.to.getTime() === oldValue.getTime()) return;
        var arr = [this.value || this.from, this.to];
        this.instance.setDate(arr, true);
      },
      type: function (curVal, oldVal) {
        this.setConfig();
      }
    },
    beforeDestroy: function () {
      this.destoryCom();
    }
  }
</script>

<style src="../../../static/plugins/flatpickr/css/material_orange.css"></style>
<style src="../../../static/plugins/flatpickr/css/confirmDate.css"></style>
<style src="../../../static/plugins/flatpickr/css/monthSelect.css"></style>
<style scoped>
  /* 默认 */
  .form-control {
    border: 1px solid transparent;
    outline: none;
  }

  /* 正常 */
  .form-control:not(:read-only),
  .form-control:not(:disabled) {
    border-color: #ced4da;
  }

  /* 正常hover */
  .input-group:hover .form-control:not(:read-only),
  .input-group:focus .form-control:not(:read-only),
  .input-group:active .form-control:not(:read-only),
  .input-group:hover .form-control:not(:disabled),
  .input-group:focus .form-control:not(:disabled),
  .input-group:active .form-control:not(:disabled) {
    border-color: #f59942;
  }

  /* 禁用，只读 */
  .form-control:read-only {
    border-color: #ced4da;
  }

  .form-control:disabled {
    border-color: #ced4da;
  }

  /* 只读hover */
  .input-group:hover .form-control:read-only,
  .input-group:focus .form-control:read-only,
  .input-group:active .form-control:read-only {
    border-color: #f59942;
  }

  /* 禁用hover */
  .input-group:hover .form-control:disabled,
  .input-group:focus .form-control:disabled,
  .input-group:active .form-control:disabled {
    border-color: #ced4da;
  }


  /* 默认 */
  .form-control ~ .input-group-append .input-group-text {
    background-color: transparent;
    transition: 0.3s;
    border-color: transparent;
    color: #fff;
    outline: none;
  }

  /* 正常 */
  .form-control:not(:read-only) ~ .input-group-append .input-group-text,
  .form-control:not(:disabled) ~ .input-group-append .input-group-text {
    background-color: #fb9271;
    border-color: #fb9271;
  }

  /* 正常hover */
  .input-group:hover .form-control:not(:read-only) ~ .input-group-append .input-group-text,
  .input-group:focus .form-control:not(:read-only) ~ .input-group-append .input-group-text,
  .input-group:active .form-control:not(:read-only) ~ .input-group-append .input-group-text,
  .input-group:hover .form-control:not(:disabled) ~ .input-group-append .input-group-text,
  .input-group:focus .form-control:not(:disabled) ~ .input-group-append .input-group-text,
  .input-group:active .form-control:not(:disabled) ~ .input-group-append .input-group-text {
    background-color: #ed724d;
  }

  /* 禁用，只读 */
  .form-control:read-only ~ .input-group-append .input-group-text {
    border-color: #fb9271;
  }

  .form-control:read-only ~ .input-group-append:last-child .input-group-text {
    border-left-color: #e6b280;
  }

  .form-control:disabled ~ .input-group-append .input-group-text {
    border-color: #ced4da;
    background-color: #d5d5d5;
  }

  .form-control:disabled ~ .input-group-append:last-child .input-group-text {
    border-left-color: #eee;
  }

  /* 只读hover */
  .input-group:hover .form-control:read-only ~ .input-group-append .input-group-text,
  .input-group:focus .form-control:read-only ~ .input-group-append .input-group-text,
  .input-group:active .form-control:read-only ~ .input-group-append .input-group-text {
    border-color: #f59942;
  }

  /* 禁用hover */
  .input-group:hover .form-control:disabled ~ .input-group-append .input-group-text,
  .input-group:focus .form-control:disabled ~ .input-group-append .input-group-text,
  .input-group:active .form-control:disabled ~ .input-group-append .input-group-text {
    border-color: #ced4da;
  }
</style>
