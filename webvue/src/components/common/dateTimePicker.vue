<template>
  <div class="input-group date">
    <input class="form-control border-right-0" size="16" type="text" readonly v-model='dataText' :disabled="disabled"/>
    <div class="input-group-addon input-group-append">
      <span :class='{"input-group-text":true,"btn-outline-purple":!option || !option.disabled}'>
        <i :class='{"fa mr0":true,"fa-calendar": type == 1,"fa-clock-o": type == 2}'></i>
      </span>
    </div>
  </div>
</template>

<script>
  import "../../../static/bootstrap/js/bootstrap-datetimepicker.js";
  import "../../../static/bootstrap/js/bootstrap-datetimepicker.zh-CN.js";

  var defaultOption = {
    todayBtn: true,
    clearBtn: true,
    autoclose: true,
    todayHighlight: true,
    // showMeridian: 1,
    fontAwesome: true,
    language: 'zh-CN',
    pickerPosition: "bottom-left",
    weekStart: 1,
    //format: 'yyyy-mm-dd hh:ii:ss',
    minView: 2, //如果是到时分秒，则去掉这一项，或值改成0
    format: 'yyyy-mm-dd',
    disabled: false
  };
  var mergeDefaultOpt = {};
  $.extend(true, mergeDefaultOpt, defaultOption);

  export default {
    name: 'dateTimePicker',
    props: {option: {disabled:null}, mydate: null, disabled: null, formart:''},
    model: {
      prop: 'mydate',
      event: 'returnBack'
    },
    data(){
      return {
        dataValue: null,
        dataText: null,
        mergeOption: mergeDefaultOpt,
        // 1日历，2时间
        type: 1
      }
    },
    methods: {
      returnBackFn() {
        this.$emit('returnBack', this.dataValue);
      },
      initDate() {
        var me = this;
        var opt = {};
        $.extend(true, opt, defaultOption, this.option);
        this.mergeOption = opt;
        if (this.dataValue) {
          opt.initialDate = this.formatDate(this.dataValue, opt.format);
          this.dataText = opt.initialDate;
        }
        if (opt.format && (opt.format.indexOf('hh') > -1 || opt.format.indexOf('HH') > -1)) {
          this.type = 2;
        }
        var $el = $(this.$el);
        this.destoryDatetimePicker();
        if(!this.option || !this.option.disabled) {
          $el.datetimepicker(this.mergeOption).off('changeDate')
          .on('changeDate', function (e) {
            me.dataValue = e.date;
            me.returnBackFn();
          });
        }
      },
      destoryDatetimePicker() {
        var $el = $(this.$el);
        $el.datetimepicker('remove');
      },
      formatDate: function (date, format) {
        if (!date || !format) return date;
        var format = format.replace('mm', 'MM').replace('hh', 'HH').replace('ii', 'mm');
        return date.format(format);
      }
    },
    watch: {
      mydate: {
        handler: function (curVal, oldVal) {
          this.dataValue = curVal;
          this.dataText = this.formatDate(this.dataValue, this.mergeOption.format);
          this.initDate();
        },
        deep: true
      }
    },
    mounted: function () {
      this.dataValue = this.mydate;
      this.initDate();
    },
    beforeDestroy:function() {
        this.destoryDatetimePicker();
    }
  }
</script>

<style src="../../../static/bootstrap/css/bootstrap-datetimepicker.css"></style>
<style scoped>
  .input-group.date .input-group-addon span {
    width: auto;
    height: auto;
  }
  .input-group .form-control{cursor:pointer;}
  .input-group-text{transition:0.3s;}
  .input-group-text.btn-outline-purple{border-color:#fb9271;}
  .input-group-text.btn-outline-purple:hover{border-color:#ed724d;}
</style>
