<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">健值对信息</div>
      <div class="panel-body">
        <form class='myform infotip form-label-w110 block-form-group'>
          <div class="form-group">
            <label class="form-label req colon">标题</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="标题" autofocus
                     v-model='upgradeLog.title' v-focus/>
            </div>
            <div class='form-info'>
              <i class='fa' title='编号重复！'></i>
            </div>
            <span class='error-msg'></span>
          </div>
          <div class="form-group">
            <label class="form-label colon">系统更新时间</label>
            <div class="form-content">
              <date-time-picker v-model='upgradeLog.upgradeTime' :option='dateOpt'></date-time-picker>
            </div>
          </div>
<!--          <div class="form-group">-->
<!--            <label class="form-label colon">内容</label>-->
<!--            <div class="form-content">-->
<!--              <input type="text" class="form-control" placeholder="内容"-->
<!--                     v-model='upgradeLog.content'/>-->
<!--            </div>-->
<!--          </div>-->
          <div class="form-group">
            <label class="form-label colon">创建时间</label>
            <div class="form-content">
              <date-time-picker v-model='upgradeLog.createTime' :option='dateOpt'></date-time-picker>
            </div>
          </div>
          <table class="table table-hover" style="font-size:14px;min-width: 100px;">
            <thead>
            <tr>
              <th class="w100">类型</th>
              <th class="w150">标题</th>
              <th>链接</th>
              <th class="w30">
                <a class='inline-block mybtn' href='javascript:void(0)' @click='addItem()' title='新增'>
                <i class='fa fa-plus-circle cf05'></i>
              </a>
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in upgradeLog.contentList">
              <td>
                <select class="form-control w100p" v-model="item.type">
                  <option value="" disabled>-- 请选择 --</option>
                  <option v-for="item in typeList" :value="item.key" v-text="item.name"></option>
                </select>
              </td>
              <td>
                <input class="form-control w100p" placeholder="标题" v-model="item.text" />
              </td>
              <td>
                <input class="form-control w100p" placeholder="内容" v-model="item.link" />
              </td>
              <td>
                <a class='inline-block mybtn' style="margin-top:5px;margin-left:9px;" href='javascript:void(0)' @click='deleteItem(item)' title='删除'>
                  <i class='fa fa-trash cf05'></i>
                </a>
              </td>
            </tr>
            </tbody>
          </table>
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
  import Index from "../index";
  export default {
    name: 'userEdit',
    components: {Index},
    data () {
      return {
        allDisabled: true,
        upgradeLog: {
          id: null,
          upgradeTime: null,
          title: null,
          content: null,
          createTime: null
        },
        typeList: [{key: "1",name:"功能"},{key: "2",name:"bug"},{key: "3",name:"文档"}],
        mydate: new Date(),
        dateOpt:{format: 'yyyy-mm-dd hh:ii:ss', minView: 0,disabled:false},
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
        this.$axios.get('/upgradeLog/getDetail', {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.upgradeLog = resp.data.value || {id:id,contentList:[]};
          }
          me.allDisabled = false;
        });
      },
      save: function () {
        var me = this;
        this.allDisabled = true;
        if(me.upgradeLog.contentList && me.upgradeLog.contentList.length > 0) {
          me.upgradeLog.content = JSON.stringify(me.upgradeLog.contentList);
        }
        var param = {};
        $.extend(true, param, me.upgradeLog);
        delete param.contentList;
        this.$axios.post('/upgradeLog/save', param).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.$toaster.success('保存成功！');
            me.$root.goBack();
          } else {
            me.allDisabled = false;
          }
        });
      },
      deleteItem: function(item) {
        for(var i = 0; i < this.upgradeLog.contentList.length; i++) {
          if(this.upgradeLog.contentList[i] == item) {
            this.upgradeLog.contentList.splice(i, 1);
            return;
          }
        }
      },
      addItem: function() {
        this.upgradeLog.contentList.push({});
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

<style scoped>
  .table tbody tr td{padding:2px 2px;}
</style>
