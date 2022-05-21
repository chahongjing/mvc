<template>
  <div>
    <div class="jp-jplayer"></div>
    <div class="jp-audio">
      <div class="jp-type-single">
        <div class="jp-gui jp-interface">
          <div class="jp-controls">
            <button class="jp-play" role="button" tabindex="0" title="播放">播放</button>
            <button class="jp-stop" role="button" tabindex="0" title="暂停">暂停</button>
          </div>
          <div class="jp-progress">
            <div class="jp-seek-bar">
              <div class="jp-play-bar"></div>
            </div>
          </div>
          <div class="jp-volume-controls">
            <button class="jp-mute" role="button" tabindex="0" title="静音">静音</button>
            <button class="jp-volume-max" role="button" tabindex="0" title="最大音量">最大音量</button>
            <div class="jp-volume-bar">
              <div class="jp-volume-bar-value"></div>
            </div>
          </div>
          <div class="jp-time-holder">
            <div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
            <div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
            <div class="jp-toggles">
              <button class="jp-repeat" role="button" tabindex="0" title="重复播放">重复播放</button>
            </div>
          </div>
        </div>
        <div class="jp-details hide">
          <div class="jp-title" aria-label="title" v-text="src"></div>
        </div>
        <div class="jp-no-solution hide">
          <span>flash升级</span>
          您的浏览器需要<a href="http://get.adobe.com/flashplayer/" target="_blank">升级flash组件</a>才能播放音频
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import "../../../static/plugins/jPlayer/js/jquery.jplayer.min.js";
  import commonSrv from '@/common/commonService';

  export default {
    data() {
      return {
        showDialog: false,
        title: '音频播放',
        closeBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '关闭', fn: null},
        modalOpt: {width: '420px'},
        isInit: false
      }
    },
    props: ['src'],
    methods: {
      show: function () {
        this.showDialog = true;
      },
      hide: function () {
        this.showDialog = false;
        // 删除弹框元素
        var me = this;
        setTimeout(function () {
          me.$destroy(true);
          me.$el.parentNode.removeChild(me.$el);
        }, 200);
      },
      defaultClose: function () {
        var me = this;
        if (me.closeBtn && me.closeBtn.fn) {
          me.closeBtn && me.closeBtn.fn();
        }
        me.showDialog = false;
        this.hide();
      },
      defaultConfirm: function () {
        var me = this;
        if (me.confirmBtn && me.confirmBtn.fn) {
          me.confirmBtn && me.confirmBtn.fn();
        }
        me.showDialog = false;
        me.hide();
      },
      init: function () {
        if (this.isInit) {
          this.destory();
        }
        if (this.src) {
          this.initPlayer();
        }
      },
      initPlayer: function () {
        var me = this;
        me.isInit = true;
        var suffix = this.getMediaSuffix(me.src);
        var obj = {};
        obj[suffix] = me.src;
        this.$nextTick(function () {
          $(me.$el).find('.jp-jplayer').jPlayer({
            ready: function (event) {
              // $(this).jPlayer("setMedia", {
              //   m4a: me.src,
              // });
              $(this).jPlayer("setMedia", obj);
            },
            swfPath: "../../../static/plugins/jPlayer/js/",
            // supplied: "m4a, mp3, oga",
            wmode: "window",
            supplied: suffix,
            // solution:'flash, html',
            useStateClassSkin: true,
            autoBlur: false,
            smoothPlayBar: true,
            keyEnabled: true,
            remainingDuration: true,
            cssSelectorAncestor:'.jp-audio',
            toggleDuration: true
          });
        });
      },
      getMediaSuffix: function(src) {
        var suffix = commonSrv.getFileExtension(src).replace('.', '');
        switch (suffix) {
          case 'mp3':
            suffix = 'mp3';
            break;
          case 'mp4':
            suffix = 'm4a';
            break;
          case 'webm':
            suffix = 'webma';
            break;
          case 'ogg':
            suffix = 'oga';
            break;
          case 'wav':
            suffix = 'waa';
            break;
          case 'flv':
            suffix = 'fla';
            break;
          case 'rtmp':
            suffix = 'rtmpa';
            break;
          default: break;
        }
        return suffix;
      }
    },
    mounted: function () {
      this.init();
    },
    destory: function () {
      $(this.$el).find('.jp-jplayer').jPlayer("pause").jPlayer( "clearMedia" ).jPlayer("destroy");
      this.isInit = false;
    },
    watch: {
      src: function (curVal, oldVal) {
        this.init();
      }
    }
  }
</script>

<style src="../../../static/plugins/jPlayer/css/jplayer.blue.monday.css"></style>
<style scoped>
  .jp-audio {
    border: none;
  }
</style>
