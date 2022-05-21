import Vue from 'vue'
import audioPlayerModal from '@/components/modal/audioPlayerModal'
import videoPlayerModal from '@/components/modal/videoPlayerModal'

function showModal(com, options) {
  var tempOption = {};
  $.extend(true, tempOption, options || {});
  var VueComponent = Vue.extend(com);
  var vm = new VueComponent().$mount();
  Object.assign(vm, tempOption);
  document.body.appendChild(vm.$el);
  vm.show();
  return vm;
}

const modal = {
  showAudio: function (options) {
    showModal(audioPlayerModal, options);
  },
  showVideo: function (options) {
    showModal(videoPlayerModal, options);
  }
};

export default modal;
