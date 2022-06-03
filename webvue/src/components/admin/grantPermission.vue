<template>
  <div class='maincontent'>
    <div class='w100p h100p oxh oya'>
      <div class="mypanel" v-for='firstMenu in list'>
        <div class="panel-heading font-bold"
             :class='{"noboderbottom":firstMenu.subList.length == 0 || !firstMenu.showDetail}'>
          <label class="radio_checkbox">
            <input type='checkbox' v-model='firstMenu.isCheck' @change='save(firstMenu)'/>
            <i></i>
            <span class="c39c" v-text='firstMenu.name'></span>
          </label>
          <span class="text-other" v-text='"【" + firstMenu.code + "】" + "【菜单】"'></span>
          <div class='inline-block fr'>
            <label class="radio_checkbox checkall">
              <input type='checkbox' v-model='firstMenu.isGroupCheck' @change='saveGroup(firstMenu)'/>
              <i></i>
            <span>全选</span>
          </label>
          <i class="fa showdetailarray"
             :class='{"fa-chevron-up":firstMenu.showDetail,"fa-chevron-down":!firstMenu.showDetail}'
             @click='firstMenu.showDetail = !firstMenu.showDetail'></i>
          </div>
        </div>
        <div class="panel-body" :class='{"hidedetail":!firstMenu.showDetail}' v-if='firstMenu.subList.length > 0'>
          <div class="mypanel subpanel" v-for='secondMenu in firstMenu.subList'>
            <div class="panel-heading font-bold"
                 :class='{"noboderbottom":secondMenu.subList.length == 0 || !secondMenu.showDetail}'>
              <label class="radio_checkbox">
                <input type='checkbox' v-model='secondMenu.isCheck' @change='save(secondMenu)'/>
                <i></i>
                <span class="c06f" v-text='secondMenu.name'></span>
              </label>
              <span class="text-other" v-text='"【" + secondMenu.code + "】" + "【菜单】"'></span>
              <div class='inline-block fr'>
                <label class="radio_checkbox checkall">
                  <input type='checkbox' v-model='secondMenu.isGroupCheck' @change='saveGroup(secondMenu)'/>
                  <i></i>
                  <span>全选</span>
                </label>
                <i class="fa showdetailarray"
                   :class='{"fa-chevron-up":secondMenu.showDetail,"fa-chevron-down":!secondMenu.showDetail}'
                   @click='secondMenu.showDetail = !secondMenu.showDetail'></i>
              </div>
            </div>
            <div class="panel-body" :class='{"hidedetail":!secondMenu.showDetail}' v-if='secondMenu.subList.length > 0'>
              <div class="mypanel subpanel" v-for='functionItem in secondMenu.subList'>
                <div class="panel-heading font-bold"
                     :class='{"noboderbottom":functionItem.subList.length == 0 || !functionItem.showDetail}'>
                  <label class="radio_checkbox">
                    <input type='checkbox' v-model='functionItem.isCheck' @change='save(functionItem)'/>
                    <i></i>
                    <span class="c393" v-text='functionItem.name'></span>
                  </label>
                  <span class="text-other" v-text='"【" + functionItem.code + "】" + "【页面】"'></span>
                  <div class='inline-block fr'>
                    <label class="radio_checkbox checkall">
                      <input type='checkbox' v-model='functionItem.isGroupCheck' @change='saveGroup(functionItem)'/>
                      <i></i>
                      <span>全选</span>
                    </label>
                    <i class="fa showdetailarray"
                       :class='{"fa-chevron-up":functionItem.showDetail,"fa-chevron-down":!functionItem.showDetail}'
                       @click='functionItem.showDetail = !functionItem.showDetail'></i>
                  </div>
                </div>
                <div class="panel-body" :class='{"hidedetail":!functionItem.showDetail}'
                     v-if='functionItem.subList.length > 0'>
                  <span class="per-item" v-for="permission in functionItem.subList">
                    <label class="radio_checkbox">
                      <input type='checkbox' v-model="permission.isCheck" @change='save(permission)'/>
                      <i></i>
                      <span v-text='permission.name'></span>
                    </label>
                    <span class="text-other permission" v-text='"【" + permission.code + "】"'></span>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'grantPermission',
    data () {
      return {
        id: null,
        type: null,
        getUrl: {'role':'/role/getRolePermission','user':'/user/getUserPermission'},
        saveUrl: {'role':'/role/saveRolePermission','user':'/user/saveUserPermission'},
        list: [{
          id: null, name: null, relatedId: null, isCheck: false, showDetail: false, code: null,
          subList: [{
            id: null, name: null, relatedId: null, isCheck: false, showDetail: false, code: null,
            subList: [{
              id: null, name: null, relatedId: null, isCheck: false, showDetail: false, code: null,
              subList: [{
                id: null, name: null, relatedId: null, isCheck: false, showDetail: false, code: null,
                subList: []
              }]
            }]
          }]
        }]
      }
    },
    methods: {
      goBack() {
        this.$root.goBack();
      },
      getRolePermission: function (id, type) {
        var me = this;
        this.$axios.get(this.getUrl[this.type], {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.value) {
            me.list = resp.data.value;
            me.refreshCheckbox();
          }
        });
      },
      save: function (entity) {
        this.refreshCheckbox();
        var changed = [entity];
        // 处理联动
        this.saveCore(changed);
      },
      saveGroup: function (entity) {
        var changed = [entity];
        this.checkChildren(entity.isGroupCheck, entity, entity.subList, changed);
        this.refreshCheckbox();
        // 处理联动
        this.saveCore(changed);
      },
      saveCore: function(changed) {
        // 处理联动
        this.$axios.post(this.saveUrl[this.type], {listStr: JSON.stringify(changed), targetId: this.id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.value) {

          }
        });
      },
      refreshCheckbox() {
        if (!this.list) return;
        for (var i = 0; i < this.list.length; i++) {
          var temp = this.list[i];
          if (this.getChildrenStatus(temp.subList) == 1) {
            temp.isGroupCheck = true;
          } else {
            temp.isGroupCheck = false;
          }
        }
      },
      // 1全选中；0没有子节点；-1不是全选中
      getChildrenStatus(list) {
        var me = this;
        if (!list || list.length == 0) return 0;
        var childrenStatus = [];
        for (var i = 0; i < list.length; i++) {
          var temp = list[i];
          if (!temp.subList || temp.subList.length == 0) {
            childrenStatus.push(temp.isCheck ? 1 : -1);
          } else {
            childrenStatus.push(me.getChildrenStatus(temp.subList));
            if (childrenStatus[childrenStatus.length - 1] == 1) {
              temp.isGroupCheck = true;
            } else {
              temp.isGroupCheck = false;
            }
          }
        }
        if (childrenStatus.length == 0) return 0;
        if (childrenStatus.some(function (item) {
            return item == -1
          })) return -1;
        return 1;
      },
      checkChildren(isGroupCheck, item, children, changed) {
        if (!children || children.length == 0) return;
        item.isCheck = isGroupCheck
        item.isGroupCheck = isGroupCheck
        for (var i = 0; i < children.length; i++) {
          if (children[i].isCheck !== isGroupCheck) {
            children[i].isCheck = isGroupCheck;
            changed.push(children[i]);
          }
          this.checkChildren(isGroupCheck, children[i], children[i].subList, changed);
        }
      }
    },
    mounted: function () {
      this.id = this.$route.query.id
      this.type = this.$route.query.type
      if(!this.getUrl[this.type]) return
      this.getRolePermission(this.id, this.type);
    }
  }
</script>

<style scoped>
  .mypanel {
    margin: auto;
    width: calc(100% - 30px);
    margin-top: 15px;
    border-color: #393;
  }

  .panel-body {
    overflow: hidden;
    transition: 0.3s;
    padding: 4px 8px;
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
    border-color: #39c;
  }

  .subpanel .mypanel.subpanel {
    border-color: #96c;
  }

  .radio_checkbox {
    margin-bottom: 0;
    margin-right:0;
  }

  .panel-heading {
    padding: 4px 8px;
  }

  .showdetailarray {
    display: inline-block;
    width: 30px;
    height: 29px;
    line-height: 28px;
    margin-right: 0;
    text-align: center;
    cursor: pointer;
    color: #999;
    transition: 0.2s;
  }

  .showdetailarray:hover {
    color: #333;
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
  .text-other{line-height: 30px;cursor: default;color:#888;}
  .per-item{margin-right:10px;}
  .permission{}
</style>
