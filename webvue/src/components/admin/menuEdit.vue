<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">菜单信息</div>
      <div class="panel-body">
        <form class='myform form-label-w120 block-form-group'>
          <div class="form-group">
            <label class="form-label">名称：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="名称" autofocus
                     v-model='menu.name'>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">父级：</label>
            <div class="form-content">
              <select class="form-control" v-model='menu.pId'
                      placeholder='请选择类型'>
                <option>--请选择类型--</option>
                <option v-for='item in menuList' :value="item.menuId" v-text="item.name"></option>
              </select>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">编码：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="编码"
                     v-model='menu.code'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">路径：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="路径"
                     v-model='menu.url'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">序号：</label>
            <div class="form-content">
              <input type="num" class="form-control" step='1' placeholder="序号"
                     v-model='menu.seq'>
            </div>
            <div class='form-info'>
              <i class='fa fa-question-circle-o'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">图标：</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="图标"
                     v-model='menu.icon'>
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
    name: 'menuEdit',
    data () {
      return {
        allDisabled:true,
        menu: {menuId: null, name: null, code: null, url: null, seq: null, icon: null},
        menuList: []
      }
    },
    methods: {
      getDetail: function (id) {
        var me = this;
        me.allDisabled = true;
        this.$axios.get('/menu/getDetail', {id: id}).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.menu = resp.data.value;
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        me.allDisabled = true;
        this.$axios.post('/menu/save', me.menu).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      },
      getMenuList() {
        var me = this;
        this.$axios.post('/menu/queryParentList', me.menu).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.menuList = resp.data.value;
          }
        });
      }
    },
    mounted: function () {
      this.getDetail(this.$route.query.id);
      this.getMenuList();
    }
  }
</script>
