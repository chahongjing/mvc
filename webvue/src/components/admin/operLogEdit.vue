<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">日志信息</div>
      <div class="panel-body">
        <form class='myform form-label-w120 block-form-group'>
          <div class="form-group">
            <label class="form-label">日志级别：</label>
            <div class="form-content">
              <select class="form-control" v-model='operLog.logLevel'
                      placeholder='请选择日志级别' disabled>
                <option>--请选择类型--</option>
                <option v-for='item in logLevelList' :value="item.key" v-text="item.name"></option>
              </select>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">控制器：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="控制器"
                     v-model='operLog.controller' readonly>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">方法：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="方法"
                     v-model='operLog.method' readonly>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">创建时间：</label>
            <div class="form-content">
              <date-time-picker v-model='operLog.createdOn' :option='dateOpt'></date-time-picker>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">用户：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="用户" autofocus
                     v-model='operLog.userName' readonly>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">内容：</label>
            <div class="form-content">
              <textarea type="text" class="form-control" placeholder="内容"
                        v-model='operLog.content' readonly style="height:200px;"></textarea>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group text-right mb0">
            <button type="button" class="btn btn-outline-purple" @click='$root.goBack()'>
              <i class='fa fa-arrow-circle-o-left'></i><span>返回</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'operLogEdit',
    data () {
      return {
        allDisabled:true,
        operLog: {
          logID: null, userID: null, userName: null, content: null, controller: null,
          method: null, logLevel: null, createdOn: null
        },
        logLevelList: [],
        dateOpt: {format: 'yyyy-mm-dd hh:ii:ss', minView: 0, disabled:true}
      }
    },
    methods: {
      getDetail: function (id) {
        var me = this;
        me.allDisabled = true;
        this.$axios.get('/operlog/getDetail', {id: id}).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.operLog = resp.data.value;
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        me.allDisabled = true;
        this.$axios.post('/operlog/save', me.operLog).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      },
      getLogLevelList() {
        var list = [];
        for (var item in LogLevel) {
          list.push(LogLevel[item]);
        }
        return list;
      }
    },
    mounted: function () {
      this.getDetail(this.$route.query.id);
      this.logLevelList = this.getLogLevelList();
    }
  }
</script>
