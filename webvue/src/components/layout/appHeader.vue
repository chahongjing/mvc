<template>
  <div class="head">
    <div class="logo" title="首页">
      <a class='fl' href='javascript:void(0)' @click='$root.goHome()'>
        <i class="fa fa-android fa-2x white"></i>
      </a>
      <a class='fl title pl10' href='javascript:void(0)' @click='$root.goHome()'>
        首页
      </a>
    </div>
    <div class="info">
      <ul class='rightmenu' @mouseenter='enterMenu' @mouseleave='leaveMenu'>
        <li>
          <a class="licontent relative submenu pointer" href='javascript:void(0)'>
            <i class='fa fa-caret-down more text-center' title='更多操作' @click='showMenu = !showMenu'>
            </i>
            <ul class="dropdown-menu dropdown-menu-right" :style='{"height": getSubMenuHeight}'>
              <li class='dropdown-item' @click='editInfo()'>
                <i class="fa fa-id-card-o fa-fw c06f"></i>修改个人信息
              </li>
              <li class='dropdown-item' @click='openChangePasswordDialog()'>
                <i class="fa fa-key fa-fw text-danger"></i>修改密码
              </li>
              <li class='dropdown-item bg-danger text-white' @click="logout()">
                <i class="fa fa-power-off fa-fw"></i>注销
              </li>
            </ul>
          </a>
        </li>
        <li>
          <a class="licontent pointer" title='消息' href='javascript:void(0)' @click='building()'>
            <i class='fa fa-commenting-o icon-animated-vertical c5dd255 mr0'></i>
            <span class="badge badge-success">2</span>
          </a>
        </li>
        <li>
          <a class="licontent pointer" title='公告' href='javascript:void(0)' @click='building()'>
            <i class='fa fa-bullhorn icon-animated-horn cf93939 mr0'></i>
            <span class="badge badge-danger">4</span>
          </a>
        </li>
        <li>
          <a class="licontent pointer" title='提醒' href='javascript:void(0)' @click='building()'>
            <i class='fa fa-bell-o icon-animated-bell text-warning mr0'></i>
            <span class="badge badge-warning">5</span>
          </a>
        </li>
        <li class='userInfo'>
          <a class="licontent" :title='user.userName + "\r\n" + user.userCode'>
            <i class="fa fa-user-o mr0 caf0"></i>
            <span class='blank' v-html='user.userName + "<br>" + user.userCode'></span>
            <span class='userName ellipsis' v-text='user.userName'></span>
            <span class='userCode ellipsis' v-text='user.userCode'></span>
          </a>
        </li>
      </ul>
    </div>
    <common-modal :show-modal='showchangePasswordDialog' :options="modalOpt">
      <div class="modal-header" slot='headerSlot'>
        <h5 class="modal-title">修改密码</h5>
        <button type="button" class="close" @click='showchangePasswordDialog = false'>
          <span class='closeicon' title="关闭">&times;</span>
        </button>
      </div>
      <div class="modal-body pr30" slot="bodySlot">
        <form class='myform form-label-w100 block-form-group'>
          <div class="form-group" :class='getErrorClass("oldPasswordStatus")'>
            <label class="form-label req colon">原密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="原密码" autofocus
                     v-model='user.oldPassword' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa' :title='oldPasswordStatus.t'></i>
            </div>
          <span class='error-msg' v-text='oldPasswordStatus.t'></span>
          </div>
          <div class="form-group" :class='getErrorClass("newPasswordStatus")'>
            <label class="form-label req colon">新密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="新密码" autofocus
                     v-model='user.newPassword' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa' :title='newPasswordStatus.t'></i>
            </div>
          <span class='error-msg' v-text='newPasswordStatus.t'></span>
          </div>
          <div class="form-group" :class='getErrorClass("passwordAgainStatus")'>
            <label class="form-label req colon">确认密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="确认密码" autofocus
                     v-model='user.passwordAgain' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa' :title='passwordAgainStatus.t'></i>
            </div>
          <span class='error-msg' v-text='passwordAgainStatus.t'></span>
          </div>
        </form>
      </div>
      <div class="modal-footer" slot="footerSlot">
        <button type="button" class="btn btn-outline-purple" @click='showchangePasswordDialog = false'>
          <i class='fa fa-times fa-plus-myrotate fa-back-myrotate'></i><span>取消</span>
        </button>
        <button type="button" class="btn btn-purple mr5" @click="changePassword()">
          <i class='fa fa-check'></i>确定
        </button>
      </div>
    </common-modal>
  </div>
</template>

<script>
  import commonModal from '@/components/common/commonModal';
  export default {
    name: 'appHeader',
    data () {
      return {
        user: {userName: '', oldPassword: null, newPassword: null, passwordAgain: null},
        showMenu: false,
        showchangePasswordDialog: false,
        hoverMenu: false,
        oldPasswordStatus: {v:null,t:''},
        newPasswordStatus: {v:null,t:''},
        passwordAgainStatus:{v:null,t:''},
        modalOpt: {width: '350px'}
      }
    },
    methods: {
      building() {
        this.$toaster.warning('功能建设中...');
      },
      logout: function () {
        var me = this;
        //this.$confirm.confirm('确定要退出系统吗？', function () {
          me.$axios.get('/user/logout').then(function (resp) {
            if (resp.data.status == ResultStatus.OK.key) {
              me.$root.clearUser();
              me.$router.push({path: '/login'});
            }
          });
        //});
      },
      openChangePasswordDialog: function () {
        this.user.oldPassword = null;
        this.user.newPassword = null;
        this.user.passwordAgain = null;
        this.oldPasswordStatus.v = null;
        this.newPasswordStatus.v = null;
        this.passwordAgainStatus.v = null;
        this.showchangePasswordDialog = true;
      },
      changePassword: function () {
        var me = this;
        if (this.user.oldPassword === null || this.user.oldPassword === undefined
          || this.user.oldPassword === '' || this.user.oldPassword.trim() === '') {
          me.oldPasswordStatus.v = 2;
          me.oldPasswordStatus.t = '原密码不能为空！';
          return;
        }
        me.oldPasswordStatus.v = 1;
        me.oldPasswordStatus.t = '';
        if (this.user.newPassword === null || this.user.newPassword === undefined
          || this.user.newPassword === '' || this.user.newPassword.trim() === '') {
          me.newPasswordStatus.v = 2;
          me.newPasswordStatus.t = '新密码不能为空！';
          return;
        }
        me.newPasswordStatus.v = 1;
        me.newPasswordStatus.t = '';
        if (this.user.passwordAgain === null || this.user.passwordAgain === undefined
          || this.user.passwordAgain === '' || this.user.passwordAgain.trim() === '') {
          me.passwordAgainStatus.v = 2;
          me.passwordAgainStatus.t = '确认密码不能为空！';
          return;
        }
        if (this.user.newPassword !== this.user.passwordAgain) {
          me.passwordAgainStatus.v = 2;
          me.passwordAgainStatus.t = '两次密码不一致！';
          return;
        }
        me.passwordAgainStatus.v = 1;
        me.passwordAgainStatus.t = '';
        me.$axios.get('/user/changePassword', {
          userCode: this.user.userCode,
          oldPassword: this.user.oldPassword,
          newPassword: this.user.newPassword
        }).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('修改密码成功！');
            me.showchangePasswordDialog = false;
            me.user.oldPassword = '';
            me.user.newPassword = '';
            me.user.passwordAgain = '';
          }
        });
      },
      getErrorClass(type) {
        var obj = {};
        if(this[type].v == 1) {
          obj["info-success"] = true;
        } else if(this[type].v == 2) {
          obj["info-error"] = true;
        }
        return obj;
      },
      enterMenu: function () {
        this.hoverMenu = true;
      },
      leaveMenu: function () {
        var me = this;
        me.hoverMenu = false;
        setTimeout(function () {
          if (!me.hoverMenu) {
            me.showMenu = false;
          }
        }, 400);
      },
      editInfo() {
        var user = this.$root.getUser();
        this.$router.push({path: '/user/userEdit', query: {id: user.userId, type: 'editSelf'}});
        this.showMenu = false;
      }
    },
    computed: {
      getSubMenuHeight() {
        var length = $('.dropdown-menu-right').children().length;
        return (this.showMenu ? length * 35 : 0) + 'px';
      }
    },
    mounted: function () {
      this.user = this.$root.getUser();
    },
    components: {commonModal}
  }
</script>

<style scoped>
  .rightmenu a:hover{text-decoration: none;}
  .logo a i {
    width: 45px;
    height: 50px;
    display: inline-block;
    transition:0.3s;
  }
  .logo a:hover i{color:#ffc107;}

  .head .info .licontent.submenu {
    padding: 0px;
  }

  .head .info .licontent.submenu > i {
    padding-left: 5px;
    padding-right: 7px;
    transition: 0.3s;
  }

  .form-group:last-child {
    margin-bottom: 0;
  }
  .icon-animated-bell{display:inline-block;animation:ringing 3s infinite ease 1s;transform-origin:50% 0}
  @keyframes ringing{0%{transform:rotate(-15deg)}2%{transform:rotate(15deg)}4%{transform:rotate(-18deg)}6%{transform:rotate(18deg)}8%{transform:rotate(-22deg)}10%{transform:rotate(22deg)}12%{transform:rotate(-18deg)}14%{transform:rotate(18deg)}16%{transform:rotate(-12deg)}18%{transform:rotate(12deg)}20%{transform:rotate(0deg)}}

  .icon-animated-horn{display:inline-block;animation:horning 3s infinite ease 2s;transform-origin:0 50%}
  @keyframes horning{0%{transform:rotate(-15deg)}2%{transform:rotate(15deg)}4%{transform:rotate(-18deg)}6%{transform:rotate(18deg)}8%{transform:rotate(-22deg)}10%{transform:rotate(22deg)}12%{transform:rotate(-18deg)}14%{transform:rotate(18deg)}16%{transform:rotate(-12deg)}18%{transform:rotate(12deg)}20%{transform:rotate(0deg)}}

  .icon-animated-vertical{display:inline-block;animation:vertical 3s infinite ease 3s;}
  @keyframes vertical{0%{transform:translate(0,-3px)}3%{transform:translate(0,3px)}6%{transform:translate(0,-3px)}9%{transform:translate(0,3px)}12%{transform:translate(0,-3px)}15%{transform:translate(0,3px)}18%{transform:translate(0,0)}}

  .blank{opacity:0;display:inline-block;line-height:1;}
  .userInfo{position:relative;overflow: hidden;}
  .userName,.userCode{position:absolute;left:25px;top:0;transition:0.2s;width:70px;}
  .userCode{top:50px;}
  .userInfo:hover .userName{top:-34px;}
  .userInfo:hover .userCode{top:0;}

  .badge-info{color: #212529;background-color: #0df;}
  .badge-danger{background-color: #f93939;}
  /*.badge-success{background-color:#5dd255;}*/
</style>
