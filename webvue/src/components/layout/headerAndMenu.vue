<template>
  <div class='h100p'>
    <app-header></app-header>
    <div class="body">
      <app-menu></app-menu>
      <div class="right-main" :class='{"fullwin":!showMenu, "showBreadcrumb":showBreadcrumb}'>
        <breadcrumb></breadcrumb>
        <div class="right-content">
          <transition name="slide-fade">
            <router-view />
          </transition>
        </div>
        <div class="footer"></div>
      </div>
    </div>
    <div class="loading" v-if="$root.showLoadingBox">
      <div>
        <i class="fa fa-spinner fa-spin fa-3x fa-fw"></i>
        <p v-text="$root.loadingText || '处理中，请稍候...'"></p>
      </div>
    </div>
  </div>
</template>

<script>
  import appHeader from '@/components/layout/appHeader';
  import appMenu from '@/components/layout/appMenu';
  import breadcrumb from '@/components/layout/breadcrumb';
  export default {
    name: 'headerAndMenu',
    computed: {
      showMenu() {
        return this.$root.getShowMenu();
      },
      showBreadcrumb() {
        return this.$root.getShowBreadcrumb();
      }
    },
    components:{appHeader, appMenu, breadcrumb}
  }
</script>
<style scoped>
  .slide-fade-enter-active {
    transition-duration: .3s;
  }
  .slide-fade-leave-active {
    transition-duration: 0s;
    opacity: 0;
  }
  .slide-fade-enter, .slide-fade-leave-to{
    transform: translateY(20px);
    opacity: 0;
  }
  .loading {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 5000;
  }

  .loading > div {
    position: absolute;
    left: 50%;
    top: 45%;
    text-align: center;
  }

  .loading > div i {
    color: #ed724d;
  }

  .loading > div p {
    line-height: 30px;
    font-size: 14px;
    color: #fff;
  }
</style>
