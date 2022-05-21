<template>
  <ul>
    <tree-item v-for='model in recursionList' :model="model" :option='mergeOpt' :key='model.id'></tree-item>
  </ul>
</template>

<script>
import treeItem from '@/components/common/treeItem';
  var defaultOption = {
    id: 'defaulttree',
    checktype: 'radio',
    openLevel: 0,
    openIcon: 'fa-minus-circle text-primary',
    closeIcon: 'fa-plus-circle text-primary',
    leafIcon: 'fa-lightbulb-o text-success',
    folderIcon: 'fa-heart text-danger',
    checkedResult: null
  };
  var mergeDefaultOpt = {};
  $.extend(true, mergeDefaultOpt, defaultOption);

  export default {
    name: 'ulTree',
    props: {
      plainList: Array,
      option: Object
    },
    data: function(){
      return {
        mergeOpt: null,
        recursionList: []
      }
    },
    methods: {
      clickItem: function(item) {
        if(this.option.beforeClick && this.option.beforeClick(item) === false) {
          return false;
        }
        // 点击操作
        item.selected = true;
        for(var i = 0; i < this.plainList.length; i++) {
          if(this.plainList[i] == item) continue;
          this.plainList[i].selected = false;
        }
        this.option.afterClick && this.option.afterClick(item);
      },
      checkedItem: function(item) {
        this.option.afterCheck && this.option.afterCheck(item);
      },
      handlerData: function() {
        // 变成递归数据
        this.recursionList = [];
        this.plainToRecursionList(this.plainList || []);
        var opt = {};
        $.extend(true, opt, mergeDefaultOpt, this.option);
        opt.checkedResult = this.option.checkedResult;
        opt.clickItem = this.clickItem;
        opt.checkedItem = this.checkedItem;
        this.mergeOpt = opt;
        // 处理level等
        for(var i = 0; i < this.recursionList.length; i++) {
          var item = this.recursionList[i];
          item.level = 0;
          if(item.level < this.mergeOpt.openLevel - 1) {
            item.isOpen = true;
          }
          if(item.children && item.children.length > 0) {
            item.isLeaf = false;
            this.handlerChildren(item);
          } else {
            item.isLeaf = true;
          }
        }
      },
      plainToRecursionList: function(plainList) {
        for (var i = 0; i < plainList.length; i++) {
          var parents = plainList.filter(item => item.id == plainList[i].pId);
          plainList[i].children = [];
          if(parents && parents.length) {
            parents[0].children.push(plainList[i]);
          } else {
            this.recursionList.push(plainList[i]);
          }
        }
      },
      handlerChildren:function(parent) {
        if(!parent || !parent.children || parent.children.length == 0) return;
        for(var i = 0; i < parent.children.length; i++) {
          var child = parent.children[i];
          child.level = parent.level + 1;
          if(child.level < this.mergeOpt.openLevel - 1) {
            child.isOpen = true;
          }
          if(child.children && child.children.length > 0) {
            child.isLeaf = false;
            this.handlerChildren(child);
          } else {
            child.isLeaf = true;
          }
        }
      }
    },
    mounted: function() {
      this.handlerData();
    },
    watch: {
      list: function(curVal, oldVal){
        this.handlerData();
      },
      'mergeOpt.checkedResult':function() {
        this.option.checkedResult = this.mergeOpt.checkedResult;
      },
      'option.checkedResult':function() {
         this.mergeOpt.checkedResult = this.option.checkedResult;
      }
    },
    components: {treeItem}
  };
</script>
