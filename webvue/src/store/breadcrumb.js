export const SET_BREADCRUMB = 'SET_BREADCRUMB';
export const CLEAR_BREADCRUMB = 'CLEAR_BREADCRUMB';
export const SET_SHOWBREADCRUMB = 'SET_SHOWBREADCRUMB';

export default {
    state: JSON.parse(sessionStorage.getItem('breadcrumb')) || {breadcurmbList:[],showBreadcrumb:true},
    mutations: {
        [SET_BREADCRUMB](state, breadcurmbList) {
            if(state.breadcurmbList == breadcurmbList) {
                sessionStorage.setItem('breadcrumb', JSON.stringify(state))
                return;
            }
          state.breadcurmbList.length = 0;
          Array.prototype.push.apply(state.breadcurmbList, breadcurmbList);
          sessionStorage.setItem('breadcrumb', JSON.stringify(state))
        },
        [CLEAR_BREADCRUMB](state) {
            state.breadcurmbList.splice(0, state.breadcurmbList.length);
        },
        [SET_SHOWBREADCRUMB](state, showBreadcrumb) {
          state.showBreadcrumb = showBreadcrumb;
          sessionStorage.setItem('breadcrumb', JSON.stringify(state))
        }
    },

    // state: JSON.parse(sessionStorage.getItem('breadcrumb')) || [],
    // mutations: {
    //     [SET_BREADCRUMB](state, breadcrumb) {
    //         sessionStorage.setItem('breadcrumb', JSON.stringify(breadcrumb))
    //         if(state == breadcrumb) return;
    //       state.length = 0;
    //       Array.prototype.push.apply(state, breadcrumb);
    //     },
    //     [CLEAR_BREADCRUMB](state) {
    //         sessionStorage.removeItem('breadcrumb');
    //         state.splice(0, state.length);
    //     }
    // },
    actions: {
        [SET_BREADCRUMB]({commit}, breadcrumb) {
            commit(SET_BREADCRUMB, breadcrumb)
        },
        [CLEAR_BREADCRUMB]({commit}) {
            commit(CLEAR_BREADCRUMB)
        },
        [SET_SHOWBREADCRUMB]({commit}, showBreadcrumb) {
            commit(SET_SHOWBREADCRUMB, showBreadcrumb)
        }
    }
}
