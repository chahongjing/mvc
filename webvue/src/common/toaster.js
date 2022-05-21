import Vue from 'vue';
import Toastr from 'vue-toastr';
// import 'vue-toastr/dist/vue-toastr.css'

const VueComponent = Vue.extend(Toastr);
const vm = new VueComponent().$mount();
vm.defaultProgressBar = false;
vm.defaultPosition = "toast-bottom-right";
vm.defaultPreventDuplicates = true;
let init = false;

if (!init) {
  document.body.appendChild(vm.$el);
  init = true;
}

const toaster = {
  success: function (msg) {
    vm.s(msg);
  },
  warning: function (msg) {
    vm.w(msg, '提示信息');
  },
  warningWt: function (msg, title) {
    vm.w(msg, title);
  },
  info: function (msg) {
    vm.i(msg);
  },
  error: function (msg) {
    vm.e(msg, '错误信息');
  },
  errorWt: function (msg, title) {
    vm.e(msg, title);
  }
};

export default toaster;
