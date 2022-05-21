<template>
  <div class='maincontent'>
    <div class='w100p h100p oxh oya'>
      <div class="mypanel" v-for='firstMenu in list'>
        <div class="panel-heading font-bold"
             :class='{"noboderbottom":firstMenu.subList.length == 0 || !firstMenu.showDetail}'>
          <label class="radio_checkbox">
            <input type='checkbox' v-model='firstMenu.singleCheck' @change='save(firstMenu)'/>
            <i></i>
            <span v-text='firstMenu.name + " 菜单"'></span>
          </label>
          <div class='inline-block fr'>
            <label class="radio_checkbox checkall">
              <input type='checkbox' v-model='firstMenu.isCheck' @change='saveGroup(firstMenu)'/>
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
                <input type='checkbox' v-model='secondMenu.singleCheck' @change='save(secondMenu)'/>
                <i></i>
                <span v-text='secondMenu.name + " 菜单"'></span>
              </label>
              <div class='inline-block fr'>
                <label class="radio_checkbox checkall">
                  <input type='checkbox' v-model='secondMenu.isCheck' @change='saveGroup(secondMenu)'/>
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
                    <input type='checkbox' v-model='functionItem.singleCheck' @change='save(functionItem)' disabled/>
                    <i style="display:none"></i>
                    <span v-text='functionItem.name' style="margin-left:0;"></span>
                  </label>
                  <div class='inline-block fr'>
                    <label class="radio_checkbox checkall">
                      <input type='checkbox' v-model='functionItem.isCheck' @change='saveGroup(functionItem)'/>
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
                  <label class="radio_checkbox" v-for="permission in functionItem.subList">
                    <input type='checkbox' v-model="permission.isCheck" @change='save(permission)'/>
                    <i></i>
                    <span v-text="permission.name"></span>
                  </label>
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
    name: 'roleGrantPermission',
    data () {
      return {
        list: [{
          id: null, name: null, relativeId: null, isCheck: false, singleCheck: false, type: null,
          subList: [{
            id: null,
            name: null,
            relativeId: null,
            isCheck: false,
            singleCheck: false,
            type: null,
            subList: [{
              id: null,
              name: null,
              relativeId: null,
              isCheck: false,
              singleCheck: false,
              type: null,
              subList: [{
                id: null,
                name: null,
                relativeId: null,
                isCheck: false,
                singleCheck: false,
                type: null,
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
      getRolePermission: function (id) {
        var me = this;
        this.$axios.get('/rolePermission/getRolePermission', {id: id}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            me.list = resp.data.value;
            me.refreshCheckbox();
          }
        });
      },
      save: function (entity) {
          var me = this;
        this.refreshCheckbox();
        var changed = [entity];
        // 处理联动
        this.$axios.post('/rolePermission/savePermission', {listStr: JSON.stringify(changed)}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
          }
        });
      },
      saveGroup: function (entity) {
          var me = this;
        var changed = [entity];
        this.checkChildren(entity, entity.subList, changed);
        this.refreshCheckbox();
        // 处理联动
        this.$axios.post('/rolePermission/savePermission', {listStr: JSON.stringify(changed)}).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {

          }
        });
      },
      refreshCheckbox() {
        if (!this.list) return;
        for (var i = 0; i < this.list.length; i++) {
          var temp = this.list[i];
          if (this.getChildrenStatus(temp.subList) == 1) {
            temp.isCheck = true;
          } else {
            temp.isCheck = false;
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
              temp.isCheck = true;
            } else {
              temp.isCheck = false;
            }
          }
        }
        if (childrenStatus.length == 0) return 0;
        if (childrenStatus.some(function (item) {
            return item == -1
          })) return -1;
        return 1;
      },
      checkChildren(item, children, changed) {
        if (!children || children.length == 0) return;
        for (var i = 0; i < children.length; i++) {
          if (children[i].type == enumMap.PermissionType.Permission.key) {
            if (children[i].isCheck !== item.isCheck) {
              changed.push(children[i]);
              children[i].isCheck = item.isCheck;
              this.checkChildren(children[i], children[i].subList, changed);
            }
          } else {
            if (children[i].isCheck != enumMap.PermissionType.Permission.key) {
              children[i].isCheck = item.isCheck;
              this.checkChildren(children[i], children[i].subList, changed);
            }
          }
        }
      }
    },
    mounted: function () {
      this.getRolePermission(this.$route.query.id);
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
</style>
