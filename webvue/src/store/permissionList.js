export const SET_PERMISSIONLIST = 'SET_PERMISSIONLIST';
export const CLEAR_PERMISSIONLIST = 'CLEAR_PERMISSIONLIST';

export default {
    state: JSON.parse(sessionStorage.getItem('permissionList')) || [],
    mutations: {
        [SET_PERMISSIONLIST](state, permissionList) {
            sessionStorage.setItem('permissionList', JSON.stringify(permissionList))
            if(state == permissionList) return;
          state.length = 0;
          Array.prototype.push.apply(state, permissionList);
        },
        [CLEAR_PERMISSIONLIST](state) {
            sessionStorage.removeItem('permissionList')
            state.splice(0, state.length);
        }
    },
    actions: {
        [SET_PERMISSIONLIST]({commit}, permissionList) {
            commit(SET_PERMISSIONLIST, permissionList)
        },
        [CLEAR_PERMISSIONLIST]({commit}) {
            commit(CLEAR_PERMISSIONLIST)
        }
    }
}
