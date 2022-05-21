<template>
  <li :class='{"tree-item":true,"folder":isFolder}'>
    <div class='tree-item-content'>
      <span class='tree-item-icon' @click.stop="toggleItem(model)">
        <i v-if="isFolder" :class='getFolderIcon'></i>
      </span>
      <span :class='getTextClass()' @click.stop='option.clickItem(model)'>
       <label class="radio_checkbox" @click.stop='stopEvent()' style="display:none">
          <input type='radio' :name='"treeitem_" + option.id' v-model="option.checkedResult" :value='model'
                 @click.stop='stopEvent()' @change.stop='option.checkedItem(model)' v-if='option.checktype == "radio"'/>
          <input type='checkbox' :name='"treeitem_" + option.id' v-model="option.checkedResult" :value='model'
                 @click.stop='stopEvent()' @change.stop='option.checkedItem(model)'
                 v-if='option.checktype == "checkbox"'/>
          <i></i>
        </label>
       <span class='tree-item-text-icon' style="display:none;">
         <i :class='getItemIcon'></i>
       </span>
       <span class="tree-item-text-content" v-tooltip="model.name" v-html='model.name'></span>
     </span>
      <span class="tree-btn-group fr">
        <i class="fa fa-plus text-primary" @click="option.addNode(model)" v-if="showAddIcon()"></i>
        <i class="fa fa-edit text-primary" @click="option.editNode(model)" v-if="showEditIcon()"></i>
        <i class="fa fa-trash text-primary" @click="option.deleteNode(model)" v-if="showDeleteIcon()"></i>
      </span>
    </div>
    <ul v-show="model.isOpen" v-if="isFolder">
      <tree-item v-for="model in model.children" :model="model" :option='option' :key="model.id">
      </tree-item>
    </ul>
  </li>
</template>

<script>
  export default {
    name: 'treeItem',
    props: {
      model: Object,
      option: Object
    },
    data: function () {
      return {
        open: false
      }
    },
    computed: {
      isFolder: function () {
        return this.model.children && this.model.children.length
      },
      getFolderIcon: function () {
        var obj = {'fa fw mr0': true,};
        if (this.model.isOpen && this.option.openIcon) {
          obj[this.option.openIcon] = true;
        } else if (this.option.closeIcon) {
          obj[this.option.closeIcon] = true;
        }
        return obj;
      },
      getItemIcon: function (item) {
        var obj = {'fa fw mr0': true};
        if (item.model.icon) {
          obj[item.model.icon] = true;
        } else if (this.isFolder && this.option.folderIcon) {
          obj[this.option.folderIcon] = true;
        } else if (this.option.leafIcon) {
          obj[this.option.leafIcon] = true;
        }
        return obj;
      }
    },
    methods: {
      toggleItem: function (item) {
        if (this.isFolder) {
          if (this.option.beforeOpenClose && this.option.beforeOpenClose(item) === false) {
            return;
          }
          this.model.isOpen = !this.model.isOpen;
          this.option.afterOpenClose && this.option.afterOpenClose(item);
        }
      },
      stopEvent: function () {
      },
      getTextClass: function () {
        var obj = {"tree-item-text": true};
        if (this.model.selected) {
          obj["selected"] = true;
        }
        // width
        var arr = ['one-icon', 'two-icon', 'three-icon'];
        var i = -1;
        if(this.showAddIcon()) {
          i++;
        }
        if(this.showEditIcon()) {
          i++;
        }
        if(this.showDeleteIcon()) {
          i++;
        }
        if(i > -1) {
          obj[arr[i]] = true;
        }
        return obj;
      },
      showAddIcon: function() {
        return true;
      },
      showEditIcon: function() {
        return true;
      },
      showDeleteIcon: function() {
        return false;
      }
    }
  };
</script>

<style scoped>
  .tree-item {
    cursor: pointer;
  }

  .tree-item-content {
    font-size: 0;
  }

  .tree-item-content span {
    font-size: 13px;
    display: inline-block;
    padding-top: 1px;
    padding-bottom: 1px;
  }

  /* 71 33 */
  span.tree-item-text {
    font-size: 0;
    padding: 0 3px 0 1px;
    height: 21px;
    line-height: 21px;
    vertical-align: middle;
  }
  span.tree-item-text{width:calc(100% - 16px);}

  span.tree-item-text .tree-item-text-content {
    width: 100%;
    border-radius: 1px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  span.tree-item-text.selected .tree-item-text-content,span.tree-item-text:hover .tree-item-text-content {
    background-color: #dbe7f5;
  }

  .tree-item-icon {
    width: 15px;
    text-align: center;
    height: 21px;
    line-height: 21px;
    vertical-align: middle;
  }

  .tree-item-text-content {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
  }

  .tree-item-text-icon {
    width: 15px;
    text-align: center;
  }

  ul {
    padding-left: 12px;
  }

  /** 水平方向连线 */
  li {
    position: relative;
  }

  li:before, li:after {
    content: ' ';
    position: absolute;
    border: 0 dotted #999;
  }

  ul ul > li:after {
    top: 11px;
    left: -5px;
    width: 20px;
    border-top-width: 1px;
  }

  ul ul > li.folder:after {
    width: 5px;
  }

  /** 垂直方向连线 */
  ul > li:before {
    content: ' ';
    position: absolute;
    top: 0;
    left: -5px;
    height: 100%;
    border-left-width: 1px;
  }

  .mytree > li:before {
    border-left-width: 0;
  }

  ul > li:last-child:before {
    content: ' ';
    position: absolute;
    top: 0;
    left: -5px;
    height: 12px;
    border-left-width: 1px;
  }

  .mytree > li:last-child:before {
    border-left-width: 0;
  }

  .radio_checkbox span {
    padding: 0;
    margin-left: 0;
    height: 0;
    line-height: inherit;
  }

  .radio_checkbox input + i {
    left: 0;
    top: 3px;
  }

  .radio_checkbox input + i:after {
    left: 2px;
  }

  .radio_checkbox, .togglecheckbox {
    margin-right: 18px;
  }

  .tree-btn-group i {
    display: none;
  }

  .tree-item-content:hover .tree-btn-group i {
    display: inline-block;
  }
  .tree-item-content:hover span.tree-item-text.one-icon {
    width: calc(100% - 33px);
  }

  .tree-item-content:hover span.tree-item-text.two-icon {
    width: calc(100% - 55px);
  }

  .tree-item-content:hover span.tree-item-text.three-icon {
    width: calc(100% - 71px);
  }
</style>
