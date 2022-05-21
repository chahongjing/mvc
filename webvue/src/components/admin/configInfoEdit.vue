<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">配置信息</div>
      <div class="panel-body">
        <form class='myform form-label-w120 block-form-group'>
          <div class="form-group">
            <label class="form-label">名称：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="名称" autofocus
                     v-model='configInfo.name'>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">类型：</label>
            <div class="form-content">
              <select class="form-control" v-model='configInfo.type'
                      placeholder='请选择类型'>
                <option>--请选择类型--</option>
                <option v-for='item in configTypeList' :value="item.key" v-text="item.name"></option>
              </select>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">账户：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="账户"
                     v-model='configInfo.account'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">密码：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="密码"
                     v-model='configInfo.password'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">联系人：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="联系人"
                     v-model='configInfo.contactPerson'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">联系方式：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="联系方式"
                     v-model='configInfo.contacts'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">相关站点：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="相关站点"
                     v-model='configInfo.relateWebsite'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">备注：</label>
            <div class="form-content">
            <textarea class="form-control" placeholder="备注"
                      v-model='configInfo.memo'></textarea>
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
    name: 'configInfoEdit',
    data () {
      return {
        allDisabled:true,
        configInfo: {
          id: null, name: null, type: null, account: null, password: null, contactPersion: null,
          contacts: null, relateWebsite: null, memo: null
        },
        configTypeList: []
      }
    },
    methods: {
      getDetail: function (id) {
        var me = this;
        me.allDisabled = true;
        this.$axios.get('/configInfo/getDetail', {id: id}).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.configInfo = resp.data.value;
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        me.allDisabled = true;
        this.$axios.post('/configInfo/save', me.configInfo).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      },
      getConfigTypeList() {
        var list = [];
        for (var item in ConfigType) {
          list.push(ConfigType[item]);
        }
        return list;
      }
    },
    mounted: function () {
      this.getDetail(this.$route.query.id);
      this.configTypeList = this.getConfigTypeList();
    }
  }
</script>
