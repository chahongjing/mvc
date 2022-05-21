/**
 * Created by jyzeng on 2018/11/2.
 */
import axios from 'axios';
import 'jquery';
//import Qs from 'Qs';
import toaster from '@/common/toaster';

axios.defaults.baseURL = 'http://' + process.env.baseHost + (process.env.basePort ? (':' + process.env.basePort) : '');
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
axios.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
axios.defaults.withCredentials = true;

if ($) {
  $.parseJSON = function (data) {
    if (!data || typeof data !== "string") {
      return null;
    }
    data = $.trim(data);

    if (window.JSON && window.JSON.parse) {
      return window.JSON.parse(data, function (key, value) {
        var iso, js, re;
        iso = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)Z$/;
        js = /\/Date\((\d+)\)\//gi;
        re = RegExp;
        if (typeof value === 'string') {
          if (js.test(value)) {
            return new Date(+re.$1);
          } else if (iso.test(value)) {
            return new Date(Date.UTC(+re.$1, +re.$2 - 1, +re.$3, +re.$4, +re.$5, +re.$6));
          }
        }
        return value;
      });
    }
    return null;
  }
}

axios.defaults.paramsSerializer = function (params) {
  // return Qs.stringify(params);
  if (params && Object.prototype.toString.call(params) == '[object Object]') {
    return $.param(params);
  }
  return params;
};
axios.defaults.transformRequest = [function (data) {
  if (data && Object.prototype.toString.call(data) == '[object Object]') {
    data = $.param(data);
    // data = Qs.stringify(data);
  }
  return data;
}]
axios.defaults.transformResponse = [function (data) {
  if (data && typeof(data) == 'string') {
    try{
      data = $.parseJSON(data);
    }catch(e) {}
    // var data = Qs.parse(data);
  }
  return data;
}]
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  var showMsg = true
  if (config.data) {
    if (config.data instanceof FormData) {
      showMsg = !(config.data.get('showMsg') === 'false')
    } else if (typeof config.data === 'object') {
      showMsg = !(config.data['showMsg'] === false)
    } else if (typeof config.data === 'string') {
      showMsg = !(Utility.getQuery(showMsg, config.data) === 'false')
    }
  } else {
    showMsg = !(config.params && config.params.showMsg === false)
  }
  config.showMsg = showMsg
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use(function (resp) {
  // 对响应数据做点什么
  filterResp(resp);
  return resp;
}, function (error) {
  error.data = {};
  // 对响应错误做点什么
  if(!error.response) {
    error.data.status = ResultStatus.ERROR.key;
    error.data.message = '访问服务器失败！';
  } else if (error.response.status == 401) {
    // 用户未授权
    error.data.status = ResultStatus.UNAUTHORIZED.key;
    error.data.message = '未授权！';
  } else if (error.response.status == 403) {
    // 用户未授权
    error.data.status = ResultStatus.UNAUTHENTICATION.key;
    error.data.message = '未登录！';
  } else if(error.response.status == 404) {
    console.log(error.response);
    // error.data.code = ResultStatus.UNAUTHENTICATION.code;
    // error.data.message = '未登录！';
  } else if (error.response.status == 500) {
    if(error.response.data instanceof ArrayBuffer) {
       var res = JSON.parse(Utility.readArrayBufferAsText(error.response.data));
       error.data = res;
    } else if (error.response.data instanceof Blob) {
      Utility.readBlobAsText(error.response.data, function (data) {
        var res = {};
        res.data = JSON.parse(data);
        toaster.error(res.message);
      });
      error.status = ResultStatus.ERROR.key;
      error.data.message = '下载出错！';
    }
  } else {
    error.data.status = ResultStatus.ERROR.key;
    error.data.message = '未知错误！';
  }
  // return Promise.resolve(result);
  filterResp(error);
  return Promise.reject(error);
});

function filterResp(resp) {
  var showMsg = resp.config.showMsg
  if (resp.data.status == ResultStatus.NO.key && showMsg) {
    toaster.warning(resp.data.message);
  } else if (resp.data.status == ResultStatus.ERROR.key) {
    toaster.error(resp.data.message);
  } else if(resp.data.status == ResultStatus.UNAUTHENTICATION.key) {
    window.location.hash = "/login";
  } else if(resp.data.status == ResultStatus.UNAUTHORIZED.key) {
    toaster.error(resp.data.message);
  }
}

var axiosIns = {
  getAjaxUrl: function (path) {
    if (path.indexOf('http') == 0) return path;
    return this.getContext() + process.env.proxyPrefix + path;
  },
  getContext: function () {
    return process.env.context;
  },
  get: function (path, param) {
    return axios.get(this.getAjaxUrl(path), {params: param}).catch(function (resp) {
      return resp;
    });
  },
  /**
   * post请求
   * @param  path  路径
   * @param  param 参数，可以是object，也可以是FormData，FormData中也可以包含文件
   * @return
   */
  post: function (path, param) {
    return axios.post(this.getAjaxUrl(path), param).catch(function (resp) {
      return resp;
    });
  },
  postDownload: function (path, param) {
    return axios.post(this.getAjaxUrl(path), param, {responseType: 'arraybuffer'}).catch(function (resp) {
      return resp;
    });
  },
  checkSuccess: function() {
    return {code: ResultStatus.OK.code};
  },
  checkError: function(msg) {
    return {code: ResultStatus.NO.code, msg: msg};
  },
  checkErrorWithPop: function(msg) {
    return new Promise((resolve, reject) => {
      toaster.warning(msg);
      resolve({data:{code: ResultStatus.NO.code, msg: msg}});
    });
  }
};
export default axiosIns;

// axios.all([
//   axios.get('https://api.github.com/xxx/1'),
//   axios.get('https://api.github.com/xxx/2')
// ]) .then(axios.spread(function (userResp, reposResp) {
//   // 上面两个请求都完成后，才执行这个回调方法
//   console.log('User', userResp.data);
//   console.log('Repositories', reposResp.data);
// }));
