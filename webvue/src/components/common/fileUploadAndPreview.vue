<template>
  <div class="upload-files">
    <p class="upload-tip" v-text="getTip()" v-show="type == 1"></p>
    <ul class="file-container">
      <li :class="{'disabled': isDisabled}" v-for='file in files'>
        <div class="file-box w100p h100p" v-if="getShowUrl(file)">
          <template>
            <img :data-type="mediaType.picture" class="file" :src="getShowUrl(file)" alt="图片" title="点击预览"
                 v-if="getFileMediaType(file) == mediaType.picture"/>
            <img :data-type="mediaType.audio" class="file" @click="playAudio(file)" src="~@assets/M-v-Player_17.png"
                 alt="音频" title="点击播放" v-if="getFileMediaType(file) == mediaType.audio"/>
            <img :data-type="mediaType.video" class="file" @click="playVedio(file)" src="~@assets/M-v-Player_16.png"
                 alt="视频" title="点击播放" v-if="getFileMediaType(file) == mediaType.video"/>
          </template>
          <a class="close" @click="delFile(file)" v-show="!isDisabled && type == 1" title="删除附件">
            <i>×</i>
          </a>
        </div>
      </li>
      <li :class="{'disabled': isDisabled}" v-show="type == 1 && (!files || files.length < maxFileNum)">
        <form class="fileForm w100p h100p">
          <label class="w100p h100p mb0 file file-box">
            <input type="file" class="upload" @change="addFile" ref="inputer" multiple
                   :accept="getMimeType" :disabled="isDisabled" title="点击上传文件" />
            <a class="add">
              <i class="fa fa-plus"></i>
            </a>
          </label>
        </form>
      </li>
    </ul>
  </div>
</template>

<script>
  import comSrv from '@/common/commonService';
  import Viewer from "../../../static/plugins/viewer/js/viewer.js";

  export default {
    data() {
      return {
        fileMaxSize: comSrv.uploadFileSize * 1024 * 1024, // 5mb
        fileMaxSizeStr: comSrv.uploadFileSize + 'M',
        jpgMimeTypeArr: ['image/png', 'image/jpeg', 'image/gif', 'image/jpg'],
        // audio/x-m4a,audio/*
        mp3MimeTypeArr: ['audio/mp3', 'audio/mpeg3', 'audio/x-mpeg-3', 'video/mpeg', 'video/x-mpeg'],
        // video/x-m4v,video/*
        mp4MimeTypeArr: ['video/mp4'],
        mediaType: comSrv.mediaType,
        viewer: null
      }
    },
    // type: 1:upload and preview, 2: preview
    props: ['files', 'fileSuffix', 'dataBus', 'maxFileNum', 'disabled', 'fileDomain', 'type'],
    methods: {
      init: function() {
        var me = this;
        this.$nextTick(function () {
          me.destoryCom();
          me.viewer = new Viewer($(me.$el)[0], {
            filter: function (image) {
              return image.dataset.type == me.mediaType.picture;
            }
          });
        });
      },
      addFile:function(event) {
        var inputDOM = this.$refs.inputer;
        if(this.dataBus) {
          this.dataBus.allDisabled = true;
        }
        // 通过DOM取文件数据
        var files = inputDOM.files;
        if (!this.beforeUpload(files)) {
          if(this.dataBus) {
            this.dataBus.allDisabled = false;
          }
          return;
        }
        // 上传图片
        var formData = new FormData();
        formData.append('busPath', 'workorder');
        for (var i = 0; i < files.length; i++) {
          formData.append('myfile', files[i]);
        }
        var me = this;
        this.$axios.post('/learn/testPostWithFile', formData).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            // me.fileDomain = resp.data.value.fileDomain;
            var list = resp.data.value.attachmentList;
            // var list = [{url: resp.data.value.url}];
            for (var i = 0; i < list.length; i++) {
              var fileTemp = {
                url: list[i].url,
                fileName: list[i].fileName,
                domain: me.fileDomain
              };
              fileTemp.type = comSrv.getFileMediaType(fileTemp.url);
              me.files.push(fileTemp);
            }
            me.init();
          }
          me.resetInput();
          if(me.dataBus) {
            me.dataBus.allDisabled = false;
          }
        });
      },
      beforeUpload: function (files) {
        var suffixArr = this.fileSuffix || comSrv.pictureSuffix;
        if (!files || files.length == 0) {
          this.$toaster.warning('请选择要上传的附件！');
          return false;
        }
        if (files.length + this.files.length > (this.maxFileNum || 5)) {
          this.$toaster.warning('最多可上传' + (this.maxFileNum || 5) + '个附件！');
          this.resetInput();
          return false;
        }
        for (var i = 0; i < files.length; i++) {
          if (files[i].size > this.fileMaxSize) {
            this.$toaster.warning('请选择' + this.fileMaxSizeStr + '以内的附件！');
            this.resetInput();
            return false;
          }
          var suffix = comSrv.getFileExtension(files[i].name);
          if (!suffix || !suffixArr.includes(suffix.toLowerCase().replace('.', ''))) {
            this.$toaster.warning('只能上传后缀名为' + suffixArr.join(', ') + '的附件！');
            this.resetInput();
            return false;
          }
        }
        return true;
      },
      resetInput:function() {
        $(this.$el).find('.fileForm')[0].reset();
      },
      delFile:function(key) {
        this.files.splice(this.files.indexOf(key), 1);
        this.init();
      },
      getTip: function () {
        var fsArr = this.fileSuffix || [];
        return '(建议附件格式为：' + fsArr.join(', ') + '，大小不超过' + this.fileMaxSizeStr + '，最多可上传' + (this.maxFileNum || 5) + '个附件)';
      },
      getShowUrl: function (file) {
        if(!file.url) return '';
        var url;
        var type = comSrv.getFileMediaType(file.url);
        switch (type) {
          case comSrv.mediaType.picture:
            url = this.fileDomain + '/' + file.url;
            break;
          case comSrv.mediaType.audio:
            url = this.$root.resRoot + 'img/M-v-Player_17.png';
            break;
          case comSrv.mediaType.video:
            url = this.$root.resRoot + 'img/M-v-Player_16.png';
            break;
          default:
            url = this.fileDomain + '/' + file.url;
            break;
        }
        return url;
      },
      getRealUrl: function(file) {
        return this.fileDomain + '/' + file.url;
      },
      playAudio: function (file) {
        this.$cstModal.showAudio({src: this.getRealUrl(file)});
      },
      playVedio: function (file) {
        this.$cstModal.showVideo({src: this.getRealUrl(file)});
      },
      getFileMediaType: function (file) {
        return comSrv.getFileMediaType(file.url);
      },
      destoryCom: function() {
        if(this.viewer) {
          this.viewer.destroy();
        }
      }
    },
    mounted: function () {
      this.init();
    },
    watch: {
      files: {
        handler: function (curVal, oldVal) {
          this.init();
        },
        deep: true
      }
    },
    computed: {
      getMimeType: function () {
        var arr = [];
        if (this.fileSuffix && this.fileSuffix.includes('jpg')) {
          Array.prototype.push.apply(arr, this.jpgMimeTypeArr);
        }
        if (this.fileSuffix && this.fileSuffix.includes('mp3')) {
          Array.prototype.push.apply(arr, this.mp3MimeTypeArr);
        }
        if (this.fileSuffix && this.fileSuffix.includes('mp4')) {
          Array.prototype.push.apply(arr, this.mp4MimeTypeArr);
        }
        return arr.join(',');
      },
      isDisabled: function () {
        return (this.dataBus && this.dataBus.allDisabled) || this.disabled;
      }
    }
  }
</script>

<style src="../../../static/plugins/viewer/css/viewer.css"></style>
<style scoped>
  .upload-files {
    margin: 0;
  }

  .upload-files .upload-tip{margin-bottom:5px;padding-left:5px;}

  .upload-files .file-container{font-size:0;}

  .upload-files .file-container li {
    position: relative;
    width: 70px;
    height: 70px;
    font-size: 14px;
    display: inline-block;
    margin: 5px;
    text-align: center;
    vertical-align: middle;
    overflow: hidden;
    cursor: pointer;
  }

  .file-box{width:100%;height:100%;border: 2px dashed #ccc;overflow: hidden;display:inline-block;transition:border 0.3s;}

  li img,li label{
    width: 52px;
    height: 52px;
    padding: 5px;
    vertical-align: middle;
    width: 100%;
    height: 100%;
    transition: 0.3s;
  }
  li .close{position:absolute;top:-20px;right:-20px;width:40px;height:40px;border-radius:20px;background-color: rgba(0,0,0,0.8);display:none;}
  li .close i{position:absolute;left:6px;bottom:1px;color:#fff;text-shadow: none;}
  li label{line-height: 65px;cursor:pointer;}
  li input[type=file]{width:100px;height:70px;opacity: 0;position: absolute;top:0px;left:-30px;cursor:pointer;font-size:0;}
  li label .add{width:100%;height:100%;display:block;background-color: #ccc;color:#fff!important;transition: background-color 0.3s;line-height:65px;}
  li label .add i{font-size:25px;margin-right:0;}

  li:not(.disabled):hover .file-box,li:not(.disabled):hover label{border-color: rgb(237, 114, 77);}
  li:hover .file-box img{transform: scale(1.1);}
  li:not(.disabled):hover .add{background-color: rgb(237, 114, 77);}
  li:not(.disabled):hover .close{display:block;}

  li.disabled,li.disabled img{cursor:default;}
  li.disabled{opacity: 0.7;}
</style>
