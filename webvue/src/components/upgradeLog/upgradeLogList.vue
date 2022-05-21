<template>
  <div class='maincontent listcontent'>
    <div class='list-header-but-group'>
      <button type="button inline-block" class="btn btn-outline-purple" @click="add()" v-authcode='"userList_add"'>
        <i class='fa fa-plus fa-plus-myrotate mr5'></i>添加
      </button>
      <button type="button inline-block" class="btn btn-outline-purple" @click="preview()" v-authcode='"userList_add"'>
        <i class='fa fa-picture-o mr5'></i>预览
      </button>
    </div>
    <div class='searchbar'>
      <form class='myform form-inline form-group-w280 form-label-w80'>
        <div class="form-group">
          <label class="form-label colon">键</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="键" autofocus v-model='searchKey'>
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
          <th class='w200'>标题</th>
          <th class='w200'>更新时间</th>
          <th>内容</th>
          <th class='w155'>创建时间</th>
          <th class='w100'>操作</th>
        </tr>
        </thead>
        <tbody v-if='!pager.loading'>
        <tr v-for="(item, index) in list">
          <td class="text-center" v-text='((pager.pageNum - 1) * pager.pageSize) + index + 1'></td>
          <td>
            <a class='block w100p h100p' href='javascript:void(0)' v-text='item.title' @click='edit(item)'></a>
          </td>
          <td v-text='$options.filters.formatDate(item.upgradeTime)'></td>
          <td v-text='item.content'></td>
          <td v-text='$options.filters.formatDate(item.createTime)'></td>
          <td class='operate'>
            <a class='inline-block mybtn' href='javascript:void(0)' @click='deleteItem(item)' title='删除'>
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
            me.$router.push({path: '/upgradeLog/edit', query: {id: resp.data.value}});
          }
        });

      },
      preview() {
        this.$router.push({path: '/upgradeLog/preview'});
      },
      edit(entity) {
        this.$router.push({path: '/upgradeLog/edit', query: {id: entity.id}});
      },
      search() {
        this.goPage(1);
      },
      queryList() {
        var me = this;
        me.allDisabled = true;
        me.pager.loading = true;
        this.$axios.get('/upgradeLog/queryPageList', {
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
        this.$confirm.confirm('确定要删除键值对吗？', function () {
          me.$axios.get('/upgradeLog/delete', {id: entity.id}).then(function (resp) {
            if(resp.data.status == ResultStatus.OK.key) {
              me.$toaster.success('删除成功！');
              me.queryList();
            }
          });
        });
      }
    },
    mounted: function () {
      this.search();
    },
    components: {commonModal}
  }
</script>
<style scoped>
  .fa-female{color:#f3c;}
  .fa-male{color:#09c;}
</style>
