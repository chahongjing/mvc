<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">权限信息</div>
      <div class="panel-body">
        <form class='myform form-label-w120 block-form-group'>
          <div class="form-group">
            <label class="form-label">名称：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="名称" autofocus
                     v-model='permission.name'>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">功能：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="功能"
                     v-model='permission.functionName' readonly>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">编码：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="编码"
                     v-model='permission.code'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">序号：</label>
            <div class="form-content">
              <input type="num" class="form-control" step='1' placeholder="序号"
                     v-model='permission.seq'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group text-right mb0">
            <button type="button" class="btn btn-outline-purple" @click='$root.goBack()'>
              <i class='fa fa-arrow-circle-o-left'></i><span>返回</span>
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
    name: 'permissionEdit',
    data () {
      return {
        allDisabled:true,
        permission: {permissionId: null, name: null, code: null, seq: null}
      }
    },
    methods: {
      getDetail: function (id, functionId) {
        var me = this;
        me.allDisabled = true;
        this.$axios.get('/permission/getDetail', {id: id, functionId: functionId}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.permission = resp.data.value;
          }
        me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        me.allDisabled = true;
        this.$axios.post('/permission/save', me.permission).then(function (resp) {
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
      this.getDetail(this.$route.query.id, this.$route.query.functionId || '');
    }
  }
</script>
