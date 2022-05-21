<template>
  <transition name="fade-in">
    <div class='nodata' v-if='!list || list.length == 0 || loading'>
      <div class='text-success' v-if='loading'><i class="fa fa-spinner fa-spin"></i>加载中...</div>
      <div v-if='!loading' class='text-info'><i class="fa fa-desktop"></i>没有数据！</div>
    </div>
  </transition>
</template>

<script>
  var containerScroll = function($this) {
	  var top = $this.scrollTop();
	  var left = $this.scrollLeft();
	  if(top) top += 'px';
	  if(left) left += 'px';
	  if($this.hasClass('fixtable-container')) {
	    $this.find('.fixtable-header-top').css('top', top);
	    $this.find('.fixtable-header-left').css('left', left);
	    $this.find('.fixtable-header-left-top').css({top: top, left: left});
	  }
	  $this.find('.nodata').css({top: top, left: left});
  }
  export default {
    name: 'tableListLoading',
    props: {list: Array, loading: Boolean},
    mounted() {
	    var parent = $(this.$el).parent();
	    parent.scroll(function() {
	      containerScroll(parent);
	    });
    },
    watch: {
      loading(curVal, oldVal){
        this.$nextTick(function() {
          var parent = $(this.$el).parent();
          containerScroll(parent);
        });
      }
    }
  }
</script>

<style scoped>
  .fade-in-enter-active {
    transition-duration: .2s;
    transition-delay: 0.2s;
  }
  .fade-in-leave-active {
    transition-duration: 0s;
    transition-delay: 0s;
    opacity: 0;
  }
  .fade-in-enter, .fade-in-leave-to{
    opacity: 0;
  }
</style>