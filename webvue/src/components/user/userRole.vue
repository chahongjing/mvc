<template>
  <div class='w100p h100p oxh oya'>
    <div class="mypanel" v-for='root in list'>
      <div class="panel-heading font-bold" :class='{"noboderbottom":root.subList.length == 0 || !root.showDetail}'>
        <label class="radio_checkbox">
          <input type='checkbox'/>
          <i style='display:none;'></i>
          <span style='margin-left:5px;' v-text="root.name"></span>
        </label>
        <div class='inline-block fr'>
          <label class="radio_checkbox checkall">
            <input type='checkbox' v-model='root.isCheck' @change='saveGroup(root)'/>
            <i></i>
            <span>全选</span>
          </label>
          <i class="fa showdetailarray" :class='{"fa-chevron-up":root.showDetail,"fa-chevron-down":!root.showDetail}'
             @click='root.showDetail = !root.showDetail'></i>
        </div>
      </div>
      <div class="panel-body" :class='{"hidedetail":!root.showDetail}' v-if='root.subList.length > 0'>
        <label class="radio_checkbox" v-for="role in root.subList">
          <input type='checkbox' v-model="role.isCheck" @change='save(root, role)'/>
          <i></i>
          <span v-text="role.name"></span>
        </label>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'userRole',
    data () {
      return {
        list: [{id: null, name: null, relativeId: null, isCheck: false, subList: []}]
      }
    },
    methods: {
      goBack() {
        this.$root.goBack();
      },
      getUserRole: function (id) {
        var me = this;
        this.$axios.get('/userRole/queryUserRole', {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.list = resp.data.value;
          }
        });
      },
      save: function (parent, entity) {
          var me = this;
        if (!entity.isCheck) {
          parent.isCheck = false;
        } else {
          var flag = true;
          for (var i = 0; i < parent.subList.length; i++) {
            if (!parent.subList[i].isCheck) {
              flag = false;
              break;
            }
          }
          parent.isCheck = flag;
        }
        var changed = [entity];
        // 处理联动
        this.$axios.post('/userRole/saveUserRole', {listStr: JSON.stringify(changed)}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
          }
        });
      },
      saveGroup: function (parent) {
          var me = this;
        if (!parent.subList || parent.subList.length == 0) return;
        var changed = [];
        for (var i = 0; i < parent.subList.length; i++) {
          if (parent.subList[i].isCheck !== parent.isCheck) {
            parent.subList[i].isCheck = parent.isCheck;
            changed.push(parent.subList[i]);
          }
        }
        if (changed.length == 0) return;
        // 处理联动
        this.$axios.post('/userRole/saveUserRole', {listStr: JSON.stringify(changed)}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
          }
        });
      }
    },
    mounted: function () {
      this.getUserRole(this.$route.query.id);
    }
  }
</script>

<style scoped>
  .mypanel {
    margin: auto;
    width: calc(100% - 30px);
    margin-top: 15px;
    border-color: #5ba66c;
  }

  .panel-body {
    overflow: hidden;
    transition: 0.3s;
    padding: 8px;
  }

  .subpanel {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 0;
  }

  .subpanel:last-child {
    margin-bottom: 0;
  }

  .mypanel.subpanel {
    border-color: #a0ccfb;
  }

  .subpanel .mypanel.subpanel {
    border-color: #ac80f3;
  }

  .radio_checkbox {
    margin-bottom: 0;
  }

  .panel-heading {
    padding: 8px;
  }

  .showdetailarray {
    display: inline-block;
    width: 30px;
    height: 29px;
    line-height: 28px;
    margin-right: 0;
    text-align: center;
    cursor: pointer
  }

  .checkall {
    margin-right: 0;
  }

  .hidedetail {
    height: 0;
    padding: 0;
  }

  .noboderbottom {
    border-bottom-width: 0;
  }
</style>
