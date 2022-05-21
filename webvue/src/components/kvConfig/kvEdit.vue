<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">健值对信息</div>
      <div class="panel-body">
        <form class='myform infotip form-label-w110 block-form-group'>
          <div class="form-group">
            <label class="form-label req colon">键</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="键" autofocus
                     v-model='kvConfig.code' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa' title='编号重复！'></i>
            </div>
            <span class='error-msg'></span>
          </div>
          <div class="form-group">
            <label class="form-label colon">值</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="值"
                     v-model='kvConfig.value'>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">说明</label>
            <div class="form-content">
              <textarea type="text" class="form-control" placeholder="说明"
                        v-model='kvConfig.memo'></textarea>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">创建时间</label>
            <div class="form-content">
              <date-time-picker v-model='kvConfig.createTime' :option='dateOpt'></date-time-picker>
            </div>
          </div>

          <div class="form-group text-right mb0">
            <button type="button" class="btn btn-outline-purple" @click='$root.goBack()'>
              <i class='fa fa-arrow-circle-o-left fa-plus-myrotate fa-back-myrotate'></i><span>返回</span>
            </button>
            <button type="button" class="btn btn-purple mr5" @click="save" :disabled='allDisabled'>
              <i class='fa fa-save'></i>保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'userEdit',
    data () {
      return {
        allDisabled: true,
        kvConfig: {
          id: null,
          code: null,
          value: null,
          memo: null,
          createTime: null
        },
        mydate: new Date(),
        dateOpt:{format: 'yyyy-mm-dd hh:ii:ss', minView: 0,disabled:true},
        editSelf: false,
        YesNoList: [],
        interstList: [],
        intertCheck: []
      }
    },
    methods: {
      getDetail: function (id) {
        var me = this;
        this.allDisabled = true;
        this.$axios.get('/kvConfig/getDetail', {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.kvConfig = resp.data.value || {id:id};
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        this.allDisabled = true;
        this.$axios.post('/kvConfig/save', me.kvConfig).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      }
    },
    mounted: function () {
      if (this.$route.query.type == 'editSelf') {
        this.editSelf = true;
      }
      this.getDetail(this.$route.query.id);
      var list = [];
      list.push({key: 1, name: '看书'});
      list.push({key: 2, name: '看电影'});
      list.push({key: 3, name: '运动'});
      this.interstList = list;
    }
  }
</script>
