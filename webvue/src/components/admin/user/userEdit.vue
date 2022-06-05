<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">用户信息</div>
      <div class="panel-body">
        <form class='myform infotip form-label-w110 block-form-group'>
          <div class="form-group" v-if="user.id">
            <label class="form-label req colon">id</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="id"
                     v-model='user.id' :disabled="true"/>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label req colon">编号</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="编号" autofocus
                     v-model='user.code' v-focus :disabled='editSelf'/>
            </div>
            <div class='form-info'>
              <i class='fa' title='编号重复！'></i>
            </div>
            <span class='error-msg'></span>
          </div>
          <div class="form-group">
            <label class="form-label colon">姓名</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="姓名"
                     v-model='user.name' maxlength="20">
            </div>
            <div class='form-info'>
              <i class='fa' title='姓名不能为空！'></i>
            </div>
            <span class='error-msg'>名称不能为空！</span>
          </div>
          <div class="form-group" v-if='!user.isSave'>
            <label class="form-label colon">密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="密码"
                     v-model='user.password'>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group" v-if='!user.isSave'>
            <label class="form-label colon">确认密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="确认密码"
                     v-model='user.passwordAgain'>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">邮箱</label>
            <div class="form-content">
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">&yen;</span>
                </div>
                <input type="text" class="form-control border-right-0" maxlength="50">
                <div class="input-group-append">
                  <span class="input-group-text">@qq.com</span>
                </div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">创建时间</label>
            <div class="form-content">
              <date-time-picker v-model='user.createdOn' :option='dateOpt'></date-time-picker>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">生日</label>
            <div class="form-content">
              <date-time-picker v-model='user.birthday'></date-time-picker>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">性别</label>
            <div class="form-content">
              <label class="radio_checkbox mt2" v-for="item in sexList">
                <input type='radio' name="sex" :value="item.value" v-model="user.sex"/>
                <i></i>
                <span v-text="item.name"></span>
              </label>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">状态</label>
            <div class="form-content">
              <label class="radio_checkbox mt2" v-for="item in statusList">
                <input type='radio' name="status" :value="item.value" v-model="user.status"/>
                <i></i>
                <span v-text="item.name"></span>
              </label>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">类型</label>
            <div class="form-content">
              <label class="radio_checkbox mt2" v-for="item in typeList">
                <input type='radio' name="type" :value="item.value" v-model="user.type"/>
                <i></i>
                <span v-text="item.name"></span>
              </label>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label colon">兴趣爱好</label>
            <div class="form-content">
              <label class="radio_checkbox mt2" v-for="item in interestList">
                <input type='checkbox' name="interst" :value="item.value" v-model="intertCheck"/>
                <i></i>
                <span v-text="item.name"></span>
              </label>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
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
        user: {
          id: null,
          name: null,
          password: null,
          passwordAgain: null,
          sex: null,
          birthday: null,
          type: null,
          status: null,
          isSave: true,
          interests: []
        },
        mydate: new Date(),
        dateOpt:{format: 'yyyy-mm-dd hh:ii:ss', minView: 0,disabled:true},
        editSelf: false,
        sexList: [],
        typeList: [],
        statusList: [],
        interestList: [],
        intertCheck:[]
      }
    },
    methods: {
      getDetail: function (id) {
        var me = this;
        this.allDisabled = true;
        this.$axios.get('/user/getDetail', {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.value) {
            me.user = resp.data.value;
            me.intertCheck = me.user.interestList.concat()
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        this.allDisabled = true;
        me.user.interestList = this.intertCheck;
        this.$axios.post('/user/save', me.user).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.value) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      },
      getEnumList() {
        var list = [];
        for (var item in window.enumMap.Sex) {
          list.push(window.enumMap.Sex[item]);
        }
        this.sexList = list;
        list = [];
        for (var item in window.enumMap.UserTypeEnum) {
          list.push(window.enumMap.UserTypeEnum[item]);
        }
        this.typeList = list;
        list = [];
        for (var item in window.enumMap.UserStatus) {
          list.push(window.enumMap.UserStatus[item]);
        }
        this.statusList = list;
      }
    },
    mounted: function () {
      if (this.$route.query.type == 'editSelf') {
        this.editSelf = true;
      }
      this.getDetail(this.$route.query.id);
      this.getEnumList();
      var list = [];
      list.push({value: 1, name: '看书'});
      list.push({value: 2, name: '看电影'});
      list.push({value: 3, name: '运动'});
      this.interestList = list;
    }
  }
</script>
