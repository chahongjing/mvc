import Vue from 'vue'
import message from '@/components/common/confirm'
const VueComponent = Vue.extend(message);
const vm = new VueComponent().$mount();

let init = false;
let defaultBtnOption = {
  title: '提示',
  closeBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '关闭', fn: null},
  confirmBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '确定', fn: null}
}

function showModal(option) {
  Object.assign(vm, option);

  if (!init) {
    document.body.appendChild(vm.$el);
    init = true;
  }

  return vm.show();
}

const confirm = {
  confirmCore: function (options) {
    var tempOption = {};
    $.extend(true, tempOption, defaultBtnOption, options);
    showModal(tempOption);
  },
  alert: function (msg, title) {
    var tempOption = {};
    $.extend(true, tempOption, defaultBtnOption, {closeBtn: {show:false}});
    if (!(msg === undefined || msg === null)) tempOption.message = msg;
    if (!(title === undefined || title === null)) tempOption.title = title || '提示';
    showModal(tempOption);
  },
  confirm: function (msg, fn, title) {
    var tempOption = {};
    $.extend(true, tempOption, defaultBtnOption);
    if (!(msg === undefined || msg === null)) tempOption.message = msg;
    if (!(fn === undefined || fn === null)) tempOption.confirmBtn.fn = fn;
    if (!(title === undefined || title === null)) tempOption.title = title;
    showModal(tempOption);
  }
};

export default confirm;
