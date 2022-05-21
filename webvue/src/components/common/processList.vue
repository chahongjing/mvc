<template>
  <div>
    <ul class="node-list">
      <li v-for="node in list" v-tooltip="node.name">
        <span class="node" :class="getNodeClass(node)">
          <span class="node-circle"></span>
        </span>
        <p class="ellipsis label-text" v-text="node.name"></p>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "processList",
    props: ['list'],
    data: function () {
      return {
      }
    },
    methods: {
      // 初始化
      init: function () {
      },
      getNodeClass: function(node) {
        var obj = {};
        if(node.hasPass) {
          obj['before'] = true;
        } else if(node.current) {
          obj['current'] = true;
        } else {
          obj['after'] = true;
        }
        return obj;
      }
    },
    mounted: function () {
      this.init();
    },
    watch: {
      list: {
        handler: function (curVal, oldVal) {
          this.init();
        },
        deep: true
        // immediate: true
      }
    }
  }
</script>

<style scoped>
  .node-list{font-size: 0;display:flex;}
  .node-list li{display:inline-block;font-size:13px;cursor:default;text-align:center;position:relative;flex:1;}
  .node{width:100%;height:30px;display:block;position: relative;}
  .node::before,.node::after{content:' ';display: inline-block;width:50%;height:4px;position:absolute;
  top:50%;left:0;transform: translateY(-50%);background-color: #d0d0d0;}
  .node::after{left:auto;right:0;}
  li:first-child .node::before,li:last-child .node::after{display:none;}

  .node-circle{display:inline-block;margin:auto;width:18px;height:18px;border-radius: 50%;border:1px solid #9e9e9e; background-color:#fff;padding:2px;position:absolute;top:50%;transform: translate(-50%,-50%);z-index: 1;}
  .node-circle::before{content: '';display:block;border:5px solid transparent;margin-top:13px;width:7px;height:7px;}
  .node-circle::after{transition:0.3s;content:'';width:calc(100% - 4px);height:calc(100% - 4px);display:inline-block;background-color: #d0d0d0;position:absolute;border-radius: 50%;top:2px;left:2px;}
  .node.current .node-circle{border-color:#09b526;}
  .node.current .node-circle::after{background-color: #4de968;}
  .node.current .node-circle::before{border-bottom-color: #e92159;}
  .label-text{margin-top:2px;margin-bottom:0;}

  .node.before .node-circle{border-color:#d25f13;}
  .node.before .node-circle::after{background-color: #e97f39;}
  li:hover .node.before .node-circle::after{background-color: #d25f13;}
  .node.before::before,.node.before::after{background-color: #e97f39;}
  .node.current::before{background-color: #e97f39;}
  .node.current::after{background-color: #d0d0d0;}
  li:hover .node.current .node-circle::after{background-color: #09b526;}
  .node.after::before,.node.after::after{background-color: #d0d0d0;}
  li:hover .node.after .node-circle::after{background-color: #9e9e9e;}

  .node-list .orange {
    background: #e97f39;
  }

  .node-list .green {
    background: #4de968;
  }

  .node-list .blue {
    background: #849ee9;
  }

  .node-list .red {
    background: #e983af;
  }

  .node-list .greap {
    background: #b964e9;
  }

  .node-list .pink {
    background: #e94494;
  }

  .node-list .yellow {
    background: #e9d172;
  }

  .node-list .darkred {
    background: #e92159;
  }

  .node-list .grey {
    background: #d0d0d0;
  }
</style>
