export const SET_MENULIST = 'SET_MENULIST';
export const CLEAR_MENULIST = 'CLEAR_MENULIST';
export const SET_SHOWMENU = 'SET_SHOWMENU';

export default {
    state: JSON.parse(sessionStorage.getItem('leftMenu')) || {menuList:[],showMenu:true},
    mutations: {
        [SET_MENULIST](state, menuList) {
            if(state.menuList == menuList) {
                sessionStorage.setItem('leftMenu', JSON.stringify(state))
                return;
            }
          state.menuList.length = 0;
          Array.prototype.push.apply(state.menuList, menuList);
          sessionStorage.setItem('leftMenu', JSON.stringify(state))
        },
        [CLEAR_MENULIST](state) {
            //sessionStorage.removeItem('leftMenu')
            state.menuList.splice(0, state.menuList.length);
        },
        [SET_SHOWMENU](state, showMenu) {
            state.showMenu = showMenu;
          sessionStorage.setItem('leftMenu', JSON.stringify(state))
        }
    },
    actions: {
        [SET_MENULIST]({commit}, leftMenu) {
            commit(SET_MENULIST, leftMenu)
        },
        [CLEAR_MENULIST]({commit}) {
            commit(CLEAR_MENULIST)
        },
        [SET_SHOWMENU]({commit}, showMenu) {
            commit(SET_SHOWMENU, showMenu)
        }
    }
}
