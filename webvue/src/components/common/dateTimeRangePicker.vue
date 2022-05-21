<template>
  <div class="date-time date-time-range-box">
    <label class="input-group mb0" style="width:100%;height:100%;background-color: transparent;cursor:pointer;">
      <input class="form-control border-right-0" size="16" type="text" readonly :value='getText()' :disabled="disabled"/>
      <div class="input-group-addon input-group-append">
        <span :class='{"input-group-text":true}'>
          <i :class='{"fa mr0 fa-calendar":true}'></i>
        </span>
      </div>
    </label>
  </div>
</template>

<script>
  import "../../../static/plugins/dateTimeRangePicker/js/daterangepicker.js";

  export default {
    name: "dateTimeRangePicker",
    // type:1日期，2日期时间，3日期范围，4日期时间范围
    props: ['from', 'to', 'disabled', 'value', 'options', 'type', 'format'],
    computed: {},
    data: function() {
      return {
        isInit: false,
        isValueChange: false,
        defaultFormat: 'yyyy-MM-dd HH:mm:ss'
      }
    },
    methods: {
      init: function () {
        var me = this;
        if(me.isInit) {
          me.destoryCom();
        }
        var obj = {
          singleDatePicker: !me.isRange(),
          timePicker: me.showTime(),
          autoUpdateInput: false,
          // startDate: me.from || me.value,
          // endDate: me.to || me.value,
          applyButtonClasses: 'btn-dmall',
          showWeekNumbers:true,
          timePicker24Hour: true,
          timePickerSeconds: true,
          showDropdowns:true,
          linkedCalendars: false,
          opens:me.options && me.options.opens || 'left',
          locale : {
            applyLabel : '确定',
            cancelLabel : '清空',
            fromLabel : '起始时间',
            toLabel : '结束时间',
            customRangeLabel : '自定义',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1,
            weekLabel: '周',
            format: me.getMonmentFormat()
          },
          // parentEl:$(me.$el)
        };
        if(this.options && this.options.maxSpanDays) {
          obj.maxSpan= {
            days: me.options.maxSpanDays
          }
        }
        if(me.from || me.value) {
          obj.startDate = me.from || me.value;
        }
        if(me.to || me.value) {
          obj.endDate = me.to || me.value;
        }
        // var drp = $(this.$el).find('input.form-control').daterangepicker(obj, function(start, end, label) {
        //   me.change(start.toDate(), end.toDate());
        // });
        var drp = $(this.$el).find('input.form-control').daterangepicker(obj);
        drp.on('apply.daterangepicker', function(ev, picker) {
          me.change(picker.startDate.toDate(), picker.endDate.toDate());
        }).on('cancel.daterangepicker', function (ev, picker) {
          me.change(null, null);
        });
        if(!obj.startDate) {
          // $(this.$el).find('input.form-control').data('daterangepicker').setStartDate(null);
          // drp.setStartDate(null);
        }
        me.isInit = true;
      },
      getText: function() {
        if(this.isRange()) {
          var arr = [];
          if(this.from) {
            arr.push(this.$options.filters.formatDate(this.from, this.getFormat()));
          }
          if(this.to){
            arr.push(this.$options.filters.formatDate(this.to, this.getFormat()));
          }
          return arr.join('  -  ');
        } else {
          return this.$options.filters.formatDate(this.value, this.getFormat());
        }
      },
      isRange: function() {
        return this.type == 3 || this.type == 4;
      },
      showTime: function() {
        return this.type == 2 || this.type == 4;
      },
      getFormat: function() {
        return this.format || this.defaultFormat;
      },
      getMonmentFormat: function() {
        return this.getFormat().replace('yyyy', 'YYYY').replace('dd', 'DD');
      },
      destoryCom: function() {
        this.isInit = false;
      },
      change: function(from, to) {
        this.$emit('update:from', from);
        this.$emit('update:to', to);
        this.$emit('input', from);
      }
    },
    mounted: function () {
      this.init();
    },
    watch: {
      from: function() {
        this.init();
      },
      to: function() {
        this.init();
      },
      value: function() {
        this.init();
      },
    },
    beforeDestroy: function () {
      this.destoryCom();
    }
  }
</script>

<style src="../../../static/plugins/dateTimeRangePicker/css/daterangepicker.css"></style>
<style>
  .date-time-range-box .date-time-range{font-size:0!important;padding:0;height:31px;}
  .date-time-range-box .date-time-range:focus{border-color:#f59942;}
  .date-time-range-box .date-time-range.date{min-width: 160px;}
  .date-time-range-box .date-time-range.date-time{min-width: 270px;}
  .date-time-range-box .date-time-range .date-from,.date-time-range .date-limiter,.date-time-range .date-to{font-size:13px;display:inline-block;height:30px;line-height:30px;vertical-align: top;}
  .date-time-range-box .date-time-range .date-limiter{width:10px;line-height: 30px;}
  .date-time-range-box .date-time-range .date-from,.date-time-range .date-to{width:calc(50% - 5px);}
  .date-time-range-box .daterangepicker{width:545px!important;}
  .date-time-range-box .daterangepicker.single{width:auto!important;min-width: 272px;}
  .date-time-range-box .daterangepicker .calendar-time{margin-top:0;}
  .date-time-range-box .daterangepicker .calendar-time select{height:22px;}
  .date-time-range-box .daterangepicker .drp-calendar{padding-bottom:0;}
  .date-time-range-box .daterangepicker .drp-buttons{padding:4px 8px;}
  .date-time-range-box .daterangepicker .drp-buttons button{border-width:1px;text-shadow: none;font-weight: normal;}


  .date-time-range-box .input-group .input-group-addon{padding:0;}
  .date-time-range-box .input-group .input-group-addon span {
    width: auto;
    height: auto;
    height:33px;
  }
  .date-time-range-box .input-group-text{transition:0.3s;background-color:#ed724d;color:#fff; }
  .date-time-range-box .input-group-text.btn-outline-purple{border-color:#ced4da;}
  .date-time-range-box .input-group-text.btn-outline-purple:hover{border-color:#5a4daa;}
  .date-time-range-box .input-group-append span{border:1px solid #ed724d;border-left:none;}
  .date-time-range-box .input-group.date:hover .input-group-text{background-color: #eb6238;}
  .date-time-range-box .input-group.date input:disabled + div .input-group-text{
    background-color: #d5d5d5;border-color:#d5d5d5;}
  .date-time-range-box .input-group.date:hover input:disabled + div .input-group-text{background-color: #d5d5d5;border-color:#d5d5d5;}

  .date-time-range-box .month .monthselect,.date-time-range-box  .month .yearselect{vertical-align: middle;}
  .date-time-range-box .form-control:not(:disabled){cursor:pointer}
  .date-time-range-box .input-group:hover input:not(:disabled),
  .date-time-range-box input:active:not(:disabled) ,
  .date-time-range-box input:focus:not(:disabled){border-color:#f59942;}
  .date-time-range-box .input-group:hover input:not(:disabled) + div span,
  .date-time-range-box input:active:not(:disabled) + div span,
  .date-time-range-box input:focus:not(:disabled) + div span{background-color:#eb6238;}
  .date-time-range-box input:disabled + div .input-group-text{background-color: #d5d5d5;border-color:#d5d5d5;}

  .daterangepicker:after{border-bottom-color: #ff8a65;}
  .daterangepicker .drp-calendar.left,.daterangepicker .drp-calendar.right{padding:0;}
  .daterangepicker .drp-calendar.left .calendar-table{padding-right:0!important;}
  .daterangepicker .drp-calendar.single.left .calendar-table{border-right:none;}
  .daterangepicker select.monthselect, .daterangepicker select.yearselect{height:24px;vertical-align: middle;}
  .daterangepicker select.hourselect, .daterangepicker select.minuteselect, .daterangepicker select.secondselect, .daterangepicker select.ampmselect
  {border:1px solid rgb(169, 169, 169);height:24px;width:60px;}
  .daterangepicker td.off, .daterangepicker td.off.in-range, .daterangepicker td.off.start-date, .daterangepicker td.off.end-date
  {color:#888;}
  /*.daterangepicker .calendar-table td{border-radius: 50%;}*/
  .daterangepicker .calendar-table th, .daterangepicker .calendar-table td{height:28px;width:26px!important;line-height: 28px;}
  .daterangepicker td.week, .daterangepicker th.week{color:#999;}
  .daterangepicker .drp-buttons .btn{font-weight:normal;}
  .daterangepicker td.in-range{background-color:#ccc;}
  .daterangepicker td.active, .daterangepicker td.active:hover{background-color:#ed724d!important;color:#fff!important;}
  .daterangepicker td.available:hover, .daterangepicker th.available:hover{background-color: #eb6238!important;color:#fff!important;}
    /*border-top-right-radius: 50%;border-bottom-right-radius: 50%;}*/
  .daterangepicker .calendar-table{border:none;}
  .daterangepicker .calendar-table thead{background-color:#ff8a65;}
  .daterangepicker .calendar-table thead th{background-color: transparent;border-radius: 0;}
  .daterangepicker th.week{color:#222;}
  .daterangepicker .table-condensed td:first-child{border-right:1px solid #ccc;}
  .daterangepicker .drp-calendar .table-condensed{margin-right:-2px;}

  .daterangepicker .drp-calendar.right .table-condensed th:first-child{border-left:1px solid #fff;}
  .daterangepicker .drp-calendar.right .table-condensed td:first-child{border-left:1px solid #ffa689;}
  .daterangepicker .drp-buttons{padding:3px;}
  .daterangepicker .drp-calendar.right .calendar-time{border-left:1px solid #ffa689;}
  .daterangepicker .calendar-time{margin-top:0px;}
  .daterangepicker .table-condensed td.start-date{border-top-left-radius: 50%;border-bottom-left-radius: 50%;}
  .daterangepicker .table-condensed td.end-date{border-top-right-radius: 50%;border-bottom-right-radius: 50%;}
</style>
