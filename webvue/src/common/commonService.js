var commonService = {
  mediaType: {
    picture: 1,
    audio: 2,
    video: 3
  },
  uploadFileSize: 5, // mb
  pictureSuffix: ['png', 'jpeg', 'gif', 'jpg', 'bmp'],
  audioSuffix: ['mp3'],
  videoSuffix: ['mp4'],
  getPagerInfo: function (pager, callback) {
    pager.callback = callback;
    pager.loading = false;
    return pager;
  },
  getPagerModel: function (pagerId) {
    var pms = sessionStorage.getItem('pagerModel');
    var allModel, model;
    if (pms) {
      allModel = JSON.parse(pms);
    } else {
      allModel = {};
      model = {};
      model.pageNum = 1;
      model.pageSize = 10;
      model.filter = {};
      allModel[pagerId] = model;
    }
    return allModel[pagerId];
  },
  setPagerModel: function (pagerId, pagerModel) {
    var pms = sessionStorage.getItem('pagerModel');
    var model;
    if (pms) {
      model = JSON.parse(pms);
    } else {
      model = {};
    }
    model[pagerId] = pagerModel;
    sessionStorage.setItem('pagerModel', JSON.stringify(model));
  },
  clearPagerModel: function (pagerId) {
    if (pagerId) {
      var pms = sessionStorage.getItem('pagerModel');
      if (pms) {
        var obj = JSON.parse(pms);
        delete obj[pagerId];
      }
    } else {
      sessionStorage.removeItem('pagerModel');
    }
  },
  getFileMediaType: function (fileName) {
    var suffix = this.getFileExtension(fileName);
    if (!suffix) return '';
    suffix = suffix.toLowerCase().replace('.', '');
    if (this.pictureSuffix.includes(suffix)) return this.mediaType.picture;
    if (this.audioSuffix.includes(suffix)) return this.mediaType.audio;
    if (this.videoSuffix.includes(suffix)) return this.mediaType.video;
    return '';
  },
  getFileExtension: function (fileName) {
    if (!fileName || fileName.indexOf(".") == -1) return '';
    var start = fileName.lastIndexOf(".");
    var end = fileName.length;
    return fileName.substring(start, end);
  }
};

export default commonService;
