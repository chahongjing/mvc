var MYOBJ = "MYOBJ";
var MYOBJACT = "MYOBJACT";

export default {
  state: JSON.parse(sessionStorage.getItem(MYOBJ)) || {a: null, b: null},
  mutations: {
    [MYOBJ](state, value) {
      if (state == value) return;
      state.a = value.a;
      sessionStorage.setItem(MYOBJ, JSON.stringify(state));
    }
  },
  actions: {
    [MYOBJACT]({commit}, value) {
      commit(MYOBJ, value);
    }
  }
}
// this.$store.commit('MYOBJ', {a:2});
// this.$store.dispatch('MYOBJACT', {b:3});
// console.log(this.$store.state.myObjStore)
