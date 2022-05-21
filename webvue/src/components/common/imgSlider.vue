<template>
  <div class="slider-wrapper theme-default">
    <div class="nivoSlider">
      <img v-for="img in list" :src="imgDomain + img.url" />
<!--      <img src="images/toystory.jpg" data-thumb="images/toystory.jpg" alt=""/>-->
<!--      <a href="http://dev7studios.com">-->
<!--        <img src="images/up.jpg" data-thumb="images/up.jpg" alt="" title="This is an example of a caption"/>-->
<!--      </a>-->
<!--      <img src="images/walle.jpg" data-thumb="images/walle.jpg" alt="" data-transition="slideInLeft"/>-->
<!--      <img src="images/nemo.jpg" data-thumb="images/nemo.jpg" alt="" />-->
    </div>
    <div class="nivo-html-caption">
      <strong>This</strong> is an example of a <em>HTML</em> caption with <a href="#">a link</a>.
    </div>
  </div>
</template>

<script>
  import "../../../static/plugins/slider/js/jquery.nivo.slider.js";
  export default {
    name: "imgSlider",
    props: ['list'],
    data: function () {
      return {
        imgDomain: 'http://img.dmallcdn.com/'
      }
    },
    methods: {
      // 初始化
      init: function () {
        this.$nextTick(function() {
          $(this.$el).find('.nivoSlider').nivoSlider({manualAdvance:true});
        })
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

<style src="../../../static/plugins/slider/css/default.css"></style>
<style src="../../../static/plugins/slider/css/nivo-slider.css"></style>
<style>
</style>
