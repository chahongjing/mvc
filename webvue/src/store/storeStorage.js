import 'vue';

import store from 'store';
import sessionStorage from 'store/storages/sessionStorage';
import localStorage from 'store/storages/localStorage';

export const sessionStor = store.createStore(sessionStorage);
export const localStor = store.createStore(localStorage);

export const STORAGE_KEYS = {
  CITY_ID: 'EVENTS_CITY_ID',
  SPRING_BUY_INFO: 'SPRING_BUY_INFO',
  SPRING_TRADE_NO: 'SPRING_TRADE_NO',
  SPRING_PAY_AGREEMENT: 'SPRING_PAY_AGREEMENT',
  DONT_CHECK_NAVIGATION: 'DONT_CHECK_NAVIGATION',
};

export const storePlugin = (V) => {
  Object.defineProperty(V.prototype, '$session', {value: sessionStor, writable: true});
  Object.defineProperty(V.prototype, '$local', {value: localStor, writable: true});
  Object.defineProperty(V.prototype, '$STORAGE_KEYS', {value: STORAGE_KEYS, writable: true});
};

export default sessionStor;
