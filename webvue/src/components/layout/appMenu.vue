<template>
  <div class="menu" :class='{"hidemenu":!showMenu}'>
    <div class="slide-menu">
      <ul class="first-menu">
        <li v-for="item in list" :class="{'selected': item.isSelected}" :title="item.name"
            @click="clickFirstMenu(item)" :style='{"height": item.height + "px"}'>
          <div>
            <i :class="'fa ' + item.data.icon + ' fw'"></i>
            <span v-text="item.name"></span>
            <i class='fa fa-angle-down subMenuTog mr10' :class="{'open':item.isSelected}"></i>
          </div>
          <ul class='sub-menu' :class="{'show':item.isSelected}">
            <li v-for="sub in item.children" :class="{'selected': sub.isSelected}"
                @click="clickSecondMenu(item, sub, $event)">
              <a href="javascript:void(0)"><i :class="'fa ' + sub.data.icon + ' fw'"></i>
                <span v-text="sub.name"></span>
              </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <span class='tog' @click='toggleMenu()'>
      <i class='fa fa-angle-left'></i>
    </span>
  </div>
</template>

<script>
  import commonSrv from '@/common/commonService';

  export default {
    name: 'appMenu',
    data () {
      return {
        list: []
      }
    },
    mounted: function () {
      this.getMenuData();
    },
    methods: {
      getMenuData: function () {
        var list = this.$root.getMenuList();
        if(list && list.length > 0) {
          this.list = list;
        } else {
          var me = this;
          me.$axios.get('/menu/queryMenu').then(function (resp) {
            if (resp.data.status == ResultStatus.OK.key) {
              var menuInfo = {first: {}, second: {}};
              for (var i = 0; i < resp.data.value.length; i++) {
                var menu = resp.data.value[i];
                menu.isSelected = false;
                if (menuInfo.first.selected && menuInfo.first.selected.id == menu.id) {
                  menuInfo.first.selected = menu;
                }
                if (menuInfo.second.selected && menuInfo.second.selected.id == menu.id) {
                  menuInfo.second.selected = menu;
                }
              }
              var parents = resp.data.value.filter(function (item) {
                return !item.pId;
              });
              for (var i = 0; i < parents.length; i++) {
                parents[i].children = resp.data.value.filter(function (item) {
                  return item.pId == parents[i].id;
                });
                if(i == 0) {
                  parents[i].isSelected = true;
                  parents[i].height = (parents[i].children.length + 1) * 35;
                }
              }
              if (menuInfo.first.selected) {
                menuInfo.first.selected.isSelected = true;
              }
              if (menuInfo.second.selected) {
                menuInfo.second.selected.isSelected = true;
              }
              me.list = parents;
              if(!me.list || me.list.length == 0) {
                me.setMenuShowOrHide(false);
              }
              me.$root.setMenuList(me.list);
            }
          });
        }
      },
      clickFirstMenu: function (item) {
        if (item.isSelected) {
          item.isSelected = false;
          item.height = 35;
          this.$root.setMenuList(this.list);
          return;
        }
        for (var i = 0; i < this.list.length; i++) {
          var obj = this.list[i];
          if (item == obj) {
            obj.isSelected = !obj.isSelected;
            obj.height = (obj.children.length + 1) * 35;
          } else {
            obj.isSelected = false;
            obj.height = 35;
          }
        }
        this.$root.setMenuList(this.list);
      },
      clickSecondMenu: function (item, sub, $event) {
        $event.stopPropagation();
        if (!sub.isSelected) {
          this.$root.clearBreadrumb();
        }
        for (var i = 0; i < this.list.length; i++) {
          var obj = this.list[i];
          for (var j = 0; j < obj.children.length; j++) {
            var subObj = obj.children[j];
            if (item == subObj) continue;
            subObj.isSelected = false;
          }
        }
        sub.isSelected = true;
        this.$root.setMenuList(this.list);
        commonSrv.clearPagerModel();
        this.$router.push({path: sub.data.url});
      },
      toggleMenu() {
        this.setMenuShowOrHide(!this.$root.getShowMenu());
      },
      setMenuShowOrHide(isShow) {
        this.$root.setShowMenu(isShow);
      }
    },
    computed: {
      showMenu() {
        return this.$root.getShowMenu();
      }
    }
  }
</script>

<style scoped>
  .menu {
    position: relative;
  }

  .tog {
    position: absolute;
    top: 50%;
    right: -15px;
    margin-top: -15px;
    width: 15px;
    border-radius: 0px 15px 15px 0px;
    height: 30px;
    line-height: 30px;
    cursor: pointer;
    background-color: transparent;
    transition: 0.3s;
    z-index: 1;
  }

  .tog i {
    color: transparent;
    font-size: 18px;
    margin-left: 3px;
    transition: 0.3s;
  }

  .menu:hover .tog,.menu.hidemenu .tog {background-color: #71a;}
  .menu:hover .tog i,.menu.hidemenu .tog i {color: #fff;}

  .hidemenu {width: 0px;border: 0;}
  .hidemenu i.fa-angle-left:before {content: "\f105";}
</style>
