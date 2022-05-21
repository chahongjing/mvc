<template>
  <div class="text-left">
    <select ref='myselect' style='width:100%;'>
    </select>
  </div>
</template>

<script>
  import "../../../static/plugins/select2/js/select2.js";
  import "../../../static/plugins/select2/js/i18n/zh-CN.js";

  export default {
    name: "select2",
    props: ['list', 'multiple', 'disabled', 'value', 'idField', 'textField'],
    data: function () {
      return {
        isInit: false,
        outerChange: false
      }
    },
    methods: {
      // 初始化
      init: function () {
        // 处理id和text
        this.preprocessData();
        // 绑定数据和dom
        this.handleDataAndDom();
        // 初始化完毕
        this.isInit = true;
      },
      // 预处理数据
      preprocessData: function () {
        if (this.list && this.list.length > 0 && (this.idField || this.textField)) {
          for (var i = 0; i < this.list.length; i++) {
            var obj = this.list[i];
            obj.id = obj.id || obj[this.idField];
            obj.text = obj.text || obj[this.textField];
            if (obj.id === null || obj.id === undefined) {
              console.log('select2绑定id值错误!' + JSON.stringify(obj));
            }
          }
        }
      },
      // 绑定数据和dom
      handleDataAndDom: function () {
        var me = this;
        var $obj = $(this.$refs.myselect);
        $obj.select2({
          language: "zh-CN",
          multiple: !!me.multiple,
          closeOnSelect: !me.multiple,
          disabled: !!me.disabled,
          placeholder: '--请选择--',
          allowClear: true,
          data: this.list
        }).val(this.value).trigger('change').on('select2:select', function (e) {
          me.innerChange(e);
        }).on('select2:unselect', function(e) {
          me.innerChange(e);
        }).on('select2:clear', function(e) {
          me.innerChange(e);
        });
        $obj.closest('div').find('.select2-search__field').css({width: '100%'});
      },
      innerChange: function(e) {
        // var data = e.params.data;
        // console.log(data);
        this.setCstValue(this.getCstValue(), true);
      },
      // 取值
      getCstValue: function () {
        return $(this.$refs.myselect).select2('val');
      },
      // 设置值
      setCstValue: function (value, innerChange) {
        var $obj = $(this.$refs.myselect);
        // 内部发生变化
        if (innerChange) {
          // dom值发生变化时同步js对象的值
          this.$emit('input', value);
          this.$emit('change', value);
        } else {
          $obj.val(value).trigger('change');
        }
        this.outerChange = false;
      },
      // 判断是否联动引起的变化
      isCycleChange: function (value) {
        var oldValue = this.getCstValue();
        if (value == oldValue) {
          return true;
        }
        if ($.isArray(value) && $.isArray(oldValue) && Array.intersect(value, oldValue).length == 0) {
          return true;
        }
        return false;
      },
      // 销毁控件
      destoryCom: function () {
        var $obj = $(this.$refs.myselect);
        $obj.off('change').empty();
        if (this.isInit) {
          $obj.select2("destroy");
        }
        this.isInit = false;
      }
    },
    mounted: function () {
      this.init();
    },
    watch: {
      list: {
        handler: function (curVal, oldVal) {
          this.destoryCom();
          this.init();
        },
        deep: true
        // immediate: true
      },
      disabled: function () {
        $(this.$refs.myselect).prop('disabled', !!this.disabled);
      },
      // 监听值是否发生变化
      value: function (curVal, oldVal) {
        if (this.isCycleChange(curVal)) {
          return;
        }
        this.outerChange = true;
        this.setCstValue(curVal);
      }
    },
    beforeDestroy: function () {
      this.destoryCom();
    }
  }
</script>

<style src="../../../static/plugins/select2/css/select2.css"></style>
<style>
  .myform .form-group.info-error .select2-container .select2-selection{border-color:#dc3545;}

  .select2-container .select2-selection{border-radius: 0;min-height: 31px;border-radius: 0!important;}
  .select2-container .select2-selection:focus{outline: none;}
  .select2-container.select2-container--open.select2-container--below .selection .select2-selection--single,.select2-container.select2-container--focus .selection .select2-selection{border-color:#f59942;}
  .select2-container .selection .select2-selection .select2-selection__choice{border-radius: 2px;margin-right: 3px;background-color: #f4f4f4;}
  .select2-container .selection .select2-selection .select2-selection__choice__remove{float:right;margin-right:-2px;margin-left:3px;}
  .select2-container .select2-results__option[aria-selected=true]{background-color:transparent;color:#aaa;}
  .select2-container .select2-results__option--highlighted[aria-selected]{background-color:#5897fb;}
  .select2-search:after{content:''}
  .select2-container .selection .select2-selection{border-color:#d5d5d5;}
  .select2-container.select2-container--disabled .selection .select2-selection{background-color: #e9ecef;border-color:#d5d5d5;}
  .select2-search__field{outline: none;}
</style>
