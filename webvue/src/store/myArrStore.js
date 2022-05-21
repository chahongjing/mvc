var MYARRAY = "MYARRAY";

export default {
  state: JSON.parse(sessionStorage.getItem(MYARRAY)) || [],
  mutations: {
    [MYARRAY](state, value) {
      if(state == value) return;
      state.length = 0;
      Array.prototype.push.apply(state, value);
      sessionStorage.setItem(MYARRAY, JSON.stringify(state))
    }
  }
}
// this.$store.commit('MYARRAY', [{z:1}]);
// console.log(this.$store.state.myArrStore)
