<template>
  <div class='maincontent listcontent'>
    <div class='list-header-but-group'>
      <button type="button inline-block" class="btn btn-outline-purple" @click="add()" v-authcode='"userList_add"'>
        <i class='fa fa-plus fa-plus-myrotate mr5'></i>添加
      </button>
    </div>
    <div class='searchbar'>
      <form class='myform form-inline form-group-w280 form-label-w80'>
        <div class="form-group">
          <label class="form-label colon">名称</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="名称" autofocus v-model='searchKey'>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">性别</label>
          <div class="form-content">
            <select class='form-control' v-model="sexValue">
              <option value="">-- 全部 --</option>
              <option v-for="item in sexList" :value="item.key" v-text="item.name"></option>
            </select>
          </div>
        </div>
        <div class="form-group btn100">
          <button type="button" class="btn btn-purple ml20" @click='search()' :disabled='allDisabled'>
            <i class='fa fa-search mr5'></i>搜索
          </button>
        </div>
      </form>
    </div>
    <div class='table-list'>
      <table class="table table-hover">
        <thead>
        <tr>
          <th class='w50'>#</th>
          <th class='sortheader' :class='getOrderByClass(nameOrderBy)' @click='setOrderBy(nameOrderBy)'>名称</th>
          <th class='w100 sortheader' :class='getOrderByClass(codeOrderBy)' @click='setOrderBy(codeOrderBy)'>编码</th>
          <th class='w155 sortheader' :class='getOrderByClass(createdOnOrderBy)' @click='setOrderBy(createdOnOrderBy)'>创建时间</th>
          <th class='w70'>性别</th>
          <th class='w70'>系统用户</th>
          <th class='w70 text-center'>是否禁用</th>
          <th class='w100'>操作</th>
        </tr>
        </thead>
        <tbody v-if='!pager.loading'>
        <tr v-for="(item, index) in list">
          <td class="text-center" v-text='((pager.pageNum - 1) * pager.pageSize) + index + 1'></td>
          <td>
            <a class='block w100p h100p' href='javascript:void(0)' v-text='item.userName' @click='edit(item)'></a>
          </td>
          <td v-text='item.userCode'></td>
          <td class='text-center' v-text='$options.filters.formatDate(item.createdOn)'></td>
          <td class='text-center'>
            <i class='fa mr0' :class='{"fa-female": item.sex == Sex.Female.key,"fa-male": item.sex == Sex.Male.key}'></i>
            <span v-text='$options.filters.enumNameFilter(item.sex, "Sex")'></span>
          </td>
          <td class='text-center' v-text='$options.filters.enumNameFilter(item.isSystem, "YesNo")'></td>
          <td class='text-center' v-text='$options.filters.enumNameFilter(item.isDisabled, "YesNo")'></td>
          <td class='operate'>
            <a class='inline-block mybtn' v-authcode='"userList_grant"' href='javascript:void(0)' @click='grant(item)' title='授权'>
              <i class='fa fa-id-badge c66c'></i>
            </a>
            <a class='inline-block mybtn' v-authcode='"userList_resetPassword"' href='javascript:void(0)' @click='setPassword(item)' title='修改密码'>
              <i class='fa fa-key c393'></i>
            </a>
            <a class='inline-block mybtn' v-authcode='"userList_delete"' href='javascript:void(0)' @click='deleteItem(item)' title='删除' v-if='item.isSystem != YesNo.YES.key'>
              <i class='fa fa-trash cf05'></i>
            </a>
          </td>
        </tr>
        </tbody>
      </table>
      <table-list-loading :list='list' :loading='pager.loading'></table-list-loading>
    </div>
    <div class='footer-pager'>
      <pagination :pager-info='pager'></pagination>
    </div>
    <common-modal :show-modal='showChangePasswordDialog' :options="modalOpt">
      <div class="modal-header" slot='headerSlot'>
        <h5 class="modal-title">修改密码</h5>
        <button type="button" class="close" @click='showChangePasswordDialog = false'>
          <span class='closeicon' title="关闭">&times;</span>
        </button>
      </div>
      <div class="modal-body pr20" slot="bodySlot">
        <form class='myform form-label-w100 block-form-group'>
          <div class="form-group">
            <label class="form-label req colon">新密码</label>
            <div class="form-content">
              <input type="password" class="form-control" placeholder="新密码" autofocus
                     v-model='password' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa'></i>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer" slot="footerSlot">
        <button type="button" class="btn btn-secondary" @click='showChangePasswordDialog = false'>
          <i class='fa fa-times'></i><span>取消</span>
        </button>
        <button type="button" class="btn btn-purple" @click='changePassword()'>
          <i class='fa fa-check'></i><span>确定</span>
        </button>
      </div>
    </common-modal>
  </div>
</template>

<script>
  import commonSrv from '@/common/commonService';
  import commonModal from '@/components/common/commonModal';

  export default {
    name: 'userList',
    inject: ['reload'],
    data () {
      return {
        allDisabled: true,
        searchKey: null,
        list: [],
        sexValue: '',
        sexList: [],
        nameOrderBy: {value: enumMap.OrderByType.ASC.key},
        codeOrderBy: {value: null},
        createdOnOrderBy: {value: null},
        pager: {pageNum: 1, pageSize: 5, loading: true},
        showChangePasswordDialog: false,
        userCode: null,
        password: null,
        YesNo: enumMap.YesNo,
        Sex: enumMap.Sex,
        modalOpt: {width: '350px'}
      }
    },
    methods: {
      reloadPage() {
        this.reload();
      },
      add() {
        var me = this;
        this.$axios.get('/comm/getNewId').then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.$router.push({path: '/user/userEdit', query: {id: resp.data.value}});
          }
        });

      },
      edit(entity) {
        this.$router.push({path: '/user/userEdit', query: {id: entity.userId}});
      },
      search() {
        this.goPage(1);
      },
      queryList() {
        var me = this;
        me.allDisabled = true;
        me.pager.loading = true;
        this.$axios.get('/user/queryPageList', {
          userName: this.searchKey,
          sex: this.sexValue,
          pageNum: this.pager.pageNum,
          pageSize: this.pager.pageSize,
          nameOrderBy: this.nameOrderBy.value,
          codeOrderBy: this.codeOrderBy.value,
          createdOnOrderBy: this.createdOnOrderBy.value
        }).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.list = resp.data.value.list;
            me.pager = commonSrv.getPagerInfo(resp.data.value, me.goPage);
          } else {
              me.pager.loading = false;
          }
          me.allDisabled = false;
        });
      },
      goPage(page) {
        this.pager.pageNum = page;
        this.queryList();
      },
      deleteItem: function (entity) {
        var me = this;
        this.$confirm.confirm('确定要删除用户吗？', function () {
          me.$axios.get('/user/delete', {id: entity.userId}).then(function (resp) {
            if(resp.data.status == ResultStatus.OK.key) {
              me.$toaster.success('删除成功！');
              me.queryList();
            }
          });
        });
      },
      grant(entity) {
        this.$router.push({path: '/user/userRole', query: {id: entity.userId}});
      },
      getEnumList() {
        var list = [];
        for (var item in enumMap.Sex) {
          list.push(enumMap.Sex[item]);
        }
        this.sexList = list;
      },
      setOrderBy(field) {
        if(this.nameOrderBy != field) {
          this.nameOrderBy.value = null;
        }
        if(this.codeOrderBy != field) {
          this.codeOrderBy.value = null;
        }
        if(this.createdOnOrderBy != field) {
          this.createdOnOrderBy.value = null;
        }
        field.value = (field.value == OrderByType.ASC.key ? OrderByType.DESC.key : OrderByType.ASC.key);
        this.queryList();
      },
      setPassword(entity) {
        this.userCode = entity.userCode;
        this.password = '';
        this.showChangePasswordDialog = true;
      },
      changePassword() {
        var me = this;
        if (this.userCode === null || this.userCode === undefined
          || this.userCode === '' || this.userCode.trim() === '') {
          me.$toaster.warning('当前用户编码不能为空！');
          return;
        }
        if (this.password === null || this.password === undefined
          || this.password === '' || this.password.trim() === '') {
          me.$toaster.warning('新密码不能为空！');
          return;
        }
        me.$axios.get('/user/resetPassword', {
          userCode: this.userCode,
          password: this.password
        }).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('修改密码成功！');
            me.showChangePasswordDialog = false;
          }
        });
      }
    },
    computed: {
      getOrderByClass() {
        return function(field) {
          var res = {};
          if(field.value) {
            res[field.value.toLowerCase()] = true;
          }
          return res;
        }
      }
    },
    mounted: function () {
      this.search();
      this.getEnumList();
    },
    components: {commonModal}
  }
</script>
<style scoped>
  .fa-female{color:#f3c;}
  .fa-male{color:#09c;}
</style>
