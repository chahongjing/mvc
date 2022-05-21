<template>
  <router-view v-if="isRouterAlive" />
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {isRouterAlive:true}
    },
    // 提供reload方法
    provide: function () {
      return {
        reload: this.reload
      }
    },
    methods: {
      // 刷新方法
      reload: function () {
        this.isRouterAlive = false;
        // 该方法会在dom更新后执行
        this.$nextTick(function () { this.isRouterAlive = true })
      }
    }
  }
</script>
