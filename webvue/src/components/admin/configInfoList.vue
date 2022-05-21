<template>
  <div class='maincontent listcontent'>
    <div class='list-header-but-group'>
      <button type="button inline-block" class="btn btn-outline-purple" @click="add()" v-authcode='"configInfoList_add"'>
        <i class='fa fa-plus mr5'></i>添加
      </button>
    </div>
    <div class='searchbar'>
      <form class='myform form-inline form-group-w250 form-label-w80'>
        <div class="form-group">
          <label class="form-label">名称：</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="名称" autofocus v-model='searchKey'>
          </div>
        </div>
        <div class="form-group">
          <button type="button" class="btn btn-purple ml20" @click='search()' :disabled='allDisabled'>
            <i class='fa fa-search mr5'></i>搜索
          </button>
        </div>
      </form>
    </div>
    <div class='fixtable-container'>
      <table class='table fixtable-header-top'>
        <thead>
          <tr>
            <th class='w50'>#</th>
            <th>名称</th>
            <th class='w100'>类型</th>
            <th class='w100'>账户</th>
            <th class='w100'>密码</th>
            <th class='w100'>联系人</th>
            <th class='w100'>联系方式</th>
            <th class='w100'>相关站点</th>
            <th class='w100'>备注</th>
            <th class='w100'>操作</th>
          </tr>
        </thead>
      </table>
      <table class='table fixtable-header-left' v-if='!pager.loading'>
        <thead>
          <tr>
            <th class='w50'>#</th>
          </tr>
        </thead>
         <tbody>
          <tr v-for="(item, index) in list">
            <td class="text-center" v-text='index + 1'></td>
          </tr>
        </tbody>
      </table>
      <table class='table fixtable-header-left-top' v-if='!pager.loading'>
        <thead>
          <tr>
            <th class='w50'>#</th>
          </tr>
        </thead>
      </table>
      <table class="table table-hover fixtable-con">
        <thead>
        <tr>
          <th class='w50'>#</th>
          <th>名称</th>
          <th class='w100'>类型</th>
          <th class='w100'>账户</th>
          <th class='w100'>密码</th>
          <th class='w100'>联系人</th>
          <th class='w100'>联系方式</th>
          <th class='w100'>相关站点</th>
          <th class='w100'>备注</th>
          <th class='w100'>操作</th>
        </tr>
        </thead>
        <tbody v-if='!pager.loading'>
        <tr v-for="(item, index) in list">
          <td class="text-center" v-text='index + 1'></td>
          <td>
            <a class='block w100p h100p' href='javascript:void(0)' v-text='item.name' @click='edit(item)'></a>
          </td>
          <td v-text='$options.filters.enumNameFilter(item.type, "ConfigType")'></td>
          <td v-text='item.account'></td>
          <td v-text='item.password'></td>
          <td v-text='item.contactPerson'></td>
          <td v-text='item.contacts'></td>
          <td v-text='item.relateWebsite'></td>
          <td v-text='item.memo' v-tooltip='item.memo'></td>
          <td class="operate">
            <a class='inline-block mybtn' href='javascript:void(0)' @click='deleteItem(item)' v-authcode='"configInfoList_delete"' title='删除'><i
            class='fa fa-trash cf05'></i></a>
          </td>
        </tr>
        </tbody>
      </table>
      <table-list-loading :list='list' :loading='pager.loading'></table-list-loading>
    </div>
    <div class='footer-pager'>
      <pagination :pager-info='pager'></pagination>
    </div>
  </div>
</template>

<script>
  import commonSrv from '@/common/commonService';
  export default {
    name: 'configInfoList',
    data () {
      return {
        allDisabled: true,
        searchKey: null,
        list: [],
        pager: {pageNum: 1, pageSize: 10, loading: true}
      }
    },
    methods: {
      add() {
        var me = this;
        this.$axios.get('/comm/getNewId').then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.$router.push({path: '/admin/configInfoEdit', query: {id: resp.data.value}});
          }
        });

      },
      edit(entity) {
        this.$router.push({path: '/admin/configInfoEdit', query: {id: entity.id}});

      },
      search() {
        var me = this;
        me.allDisabled = true;
        me.pager.loading = true;
        this.$axios.get('/configInfo/queryPageList', {
          name: this.searchKey,
          pageNum: this.pager.pageNum,
          pageSize: this.pager.pageSize
        }).then(function (resp) {
          if(resp.data.status == ResultStatus.OK.key) {
            me.list = resp.data.value.list;
            me.pager = commonSrv.getPagerInfo(resp.data.value, me.goPage);
          }
          me.allDisabled = false;
        });
      },
      goPage(page) {
        this.pager.pageNum = page;
        this.search();
      },
      deleteItem: function (entity) {
        var me = this;
        this.$confirm.confirm('确定要删除配置吗？', function () {
          me.$axios.get('/configInfo/delete', {id: entity.id}).then(function (resp) {
            if(resp.data.status == ResultStatus.OK.key) {
              me.$toaster.success('删除成功！');
              me.search();
            }
          });
        });
      }
    },
    mounted: function () {
      this.search();
    }
  }
</script>
<style scoped>
  .fixtable-header-top,.fixtable-con{min-width:990px;}
</style>