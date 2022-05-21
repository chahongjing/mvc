import Vue from 'vue';
import Vuex from 'vuex';
import user from './user';
import leftMenu from './leftMenu';
import breadcrumb from './breadcrumb';
import permissionList from './permissionList';
import myObjStore from './myObjStore';
import myArrStore from './myArrStore';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user, leftMenu, breadcrumb, permissionList, myObjStore, myArrStore
  }
});
