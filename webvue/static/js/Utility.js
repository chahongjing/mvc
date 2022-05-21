window.Utility = window.Utility || {};
(function (ns) {
  // 函数名称： registerNameSpace
  // 函数功能： 注册命名空间
  // 函数参数： nameSpace    命名空间: a.b.c
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-02-22 21:01:54
  ns.registerNameSpace = function (nameSpace) {
    var arrNameSpace;
    var ns;

    if (!nameSpace) {
      return window;
    }
    arrNameSpace = nameSpace.split(".");
    ns = window;
    for (var i = 0; i < arrNameSpace.length; i++) {
      if (i == 0 && arrNameSpace[i] == "window") {
        continue;
      }

      ns[arrNameSpace[i]] = ns[arrNameSpace[i]] || {};
      ns = ns[arrNameSpace[i]];
    }
    return ns;
  }

  // region Ajax
  ns.getAjaxUrl = function (path) {
    return this.getContext() + path;
  }
  ns.getContext = function () {
    return ctx;
  }

  ns.get = function (path, param) {
    return $.ajax({
      type: 'get',
      url: this.getAjaxUrl(path),
      data: param
    });
  }
  ns.post = function (path, param) {
    return $.ajax({
      type: 'post',
      url: this.getAjaxUrl(path),
      data: param
    });
  }
  ns.getFormData = function (path, formData) {
    return $.ajax({
      type: 'get',
      url: this.getAjaxUrl(path),
      data: formData,
      processData: false,
      contentType: false
    });
  }
  ns.postFormData = function (path, formData) {
    return $.ajax({
      type: 'post',
      url: this.getAjaxUrl(path),
      data: formData,
      processData: false,
      contentType: false
    });
  }

  ns.downloadAfterAjax = function (data, headers) {
    if (!(data instanceof ArrayBuffer || data instanceof Blob)) return;
    var fileName = headers['Content-Disposition'];
    if(!fileName) {
      fileName = headers['content-disposition'];
    }
    var contentType = headers['Content-Type'];
    if(!contentType) {
      contentType = headers['content-type'];
    }
    ns.downloadWithFileNameAfterAjax(data, fileName, contentType);
  }
  ns.downloadWithFileNameAfterAjax = function (data, fileName, contentType) {
    if (fileName) {
      fileName = decodeURIComponent(fileName).replace(/attachment;\s*filename=/ig, '');
    }
    if (!contentType) {
      contentType = 'application/octet-stream';
    }
    var file = new Blob([data], {type: contentType});
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
      window.navigator.msSaveOrOpenBlob(file, fileName);
    } else {
      var urlCom = (window.URL || window.webkitURL);
      var urlObj = urlCom.createObjectURL(file);
      var a = document.createElement("a");
      a.style.display = 'none';
      a.setAttribute("download", fileName);
      // a.download = fileName;
      a.href = urlObj;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      urlCom.revokeObjectURL(urlObj);
    }
  }
  ns.jsDownload = function (url, param) {
    var xhr;
    if (window.XMLHttpRequest) {
      xhr = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
      xhr = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
      return;
    }
    xhr.withCredentials = true;
    param = param || {};
    xhr.open('GET', url + "?" + $.param(param), true);
    // xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.responseType = "arraybuffer";
    xhr.onload = function () {
      if (this.status == 200) {
        Utility.downloadAfterAjax(this.response, Utility.getXhrHeaders(this));
      } else {
        Utility.readArrayBufferAsText(this.response, function (data) {
          alert(data);
        });
      }
    }
    xhr.send();
  }
  ns.jqueryUpload = function (url, formData) {
    var $form = $('#formId');
    $.ajax({
      type: 'POST',
      url: url,
      data: formData,
      processData: false,
      contentType: false, // 如果form没有指定enctype，则可以在此处指定
      xhrFields: {
        withCredentials: true
      },
      success: function (data) {
        if (data.status == ResultStatus.OK.key) {
          console.log('上传成功！' + JSON.stringify(data.value));
        } else {
          alert(data.message);
        }
      },
      error: function (xhr, type, message) {
        if (xhr.status == 511) {
          window.location.reload();
        }
      }
    });
  }
  // 不支持
  ns.jqueryDownload = function (url) {
    $.ajax({
      type: 'POST',
      url: url,
      processData: false,
      contentType: false, // 如果form没有指定enctype，则可以在此处指定
      dataType: "arraybuffer",
      success: function (data, a, b, c) {
        // todo
        Utility.downloadAfterAjax(data, Utility.getXhrHeaders(b));
        if (data.status == ResultStatus.OK.key) {
          alert('上传成功！' + data.value);
        } else {
          alert(data.message);
        }
      },
      error: function (xhr, type, message) {
        if (xhr.status == 511) {
          window.location.reload();
        }
      }
    });
  };
  ns.readArrayBufferAsText = function (data) {
    var enc = new TextDecoder('utf-8');
    return enc.decode(new Uint8Array(data));
  }
  ns.readBlobAsText = function (data, callback) {
    var reader = new FileReader();
    reader.readAsText(data, 'utf-8');
    reader.onload = function (e) {
      callback && callback(reader.result);
    }
  }
  ns.imgFileToBase64 = function (imgFile, callback) {
    //限定上传文件的类型，判断是否是图片类型
    if (!/image\/\w+/.test(file.type)) {
      return false;
    }
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
      callback && callback(this.result);
    }
  }

  ns.getXhrHeaders = function (xhr) {
    var arr = xhr.getAllResponseHeaders().split('\r\n');
    var res = {};
    for (var ind in arr) {
      var temp = arr[ind];
      var arrTemp = temp.split(':');
      if (arrTemp.length > 1) {
        var key = arrTemp[0].trim();
        arrTemp.splice(0, 1);
        res[key] = arrTemp.join(':').trim();
      }
    }
    return res;
  }

  // 函数名称： processAjax
  // 函数功能： JQuery Ajax操作
  // 函数参数： optionData.url: ajax请求地址; optionData.getData: 用户数据(Json); optionData.postData: 用户数据(Json)
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-02-22 21:01:54
  ns.processAjax = function (optionData) {
    var url, ret;

    if (!optionData.url) {
      return {"Status": "ERROR", "Message": "optionData.url参数未设置!", Value: null}
    }
    if (optionData.getData && typeof (optionData.getData) != "object") {
      return {"Status": "ERROR", "Message": "optionData.getData类型不匹配或参数未设置!", Value: null}
    }
    if (optionData.postData && typeof (optionData.postData) != "object") {
      return {"Status": "ERROR", "Message": "optionData.postData类型不匹配或参数未设置!", Value: null}
    }

    if (!optionData.getData) {
      url = optionData.url;
    } else {
      url = optionData.url + (optionData.url.indexOf("?") > -1 ? "&" : "?") + $.param(optionData.getData);
    }

    $.ajax({
      url: url,
      type: "post",
      async: (optionData.async === false ? false : true),
      dataType: "text",
      data: optionData.postData,
      success: function (data) {
        ret = handlerAjaxResult(data, optionData);
      },
      error: function (data) {
        ret = handlerAjaxResult(data.responseText, optionData);
      }
    });

    return ret;
  }

  ns.$ajaxCommon = function (url, param, successCalback, errorCalback) {
    // 调整为全局变量
    var loadingCount = 0;
    $.ajax({
      url: 'http://localhost:20000/ToolSiteMvc4J/comm/getEnums1',
      beforeSend: function (xhr) {
        if (loadingCount == 0) {
          //showLoading();
        }
        loadingCount++;
      },
      complete: function (xhr, textStatus) {
        // this
        debugger;
        console.log('complete');
        if (loadingCount == 1) {
          // hideLoading();
        }
        loadingCount--;
      },
      success: function (data, textStatus) {
        // this, data: {status,message,value}
        handleJqueryAjax(data, successCalback, errorCalback);
      },
      error: function (xhr, textStatus, errorThrown) {
        // this
        var data = {status: 'ERROR', message: '系统错误！'};
        handleJqueryAjax(data, successCalback, errorCalback);
      }
    })
  }

  function handleJqueryAjax(data, successCalback, errorCalback) {
    if (data.status == 'OK') {
      successCalback && successCalback(data);
    } else if (data.status == 'NO') {
      if (errorCalback) {
        errorCalback(data);
      } else {
        // toaster.warn(data.message);
      }
    } else if (data.status == 'UNAUTHORIZED') {
      // goto no permission
    } else if (data.status == 'UNAUTHENTICATION') {
      // goto no login
    } else if (data.status == 'ERROR') {
      if (errorCalback) {
        errorCalback(data);
      } else {
        // toaster.error(data.message);
      }
    }
  }

  function handlerAjaxResult(data, optionData) {
    var jReturn, startIndex, endIndex;
    var ret = {};
    try {
      jReturn = eval("(" + data + ")");
    } catch (ex) {
      startIndex = data.indexOf("<title>");
      if (startIndex > 0) {
        endIndex = data.indexOf("</title>");
        jReturn = {Status: "ERROR", Message: data.substring(startIndex + 7, endIndex), Value: null};
      } else if (!data) {
        jReturn = {Status: "ERROR", Message: "请求返回数据为空！", Value: null};
      } else {
        jReturn = {Status: "ERROR", Message: "返回Json数据失败！", Value: data};
      }
    }
    if (optionData.callBack && typeof optionData.callBack === "function") {
      ret = optionData.callBack(jReturn);
    } else {
      ret = jReturn;
    }
    return ret;
  }

  // endregion

  // region Date 扩展
  // 函数名称： format
  // 函数功能： 用日期格式化(yyyy-MM-dd HH:mm:ss)
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.format = function (format) {
    var o = {
      "M+": this.getMonth() + 1,
      "d+": this.getDate(),
      "H+": this.getHours(),
      "h+": this.getHours() - 12,
      "m+": this.getMinutes(),
      "s+": this.getSeconds(),
      "q+": Math.floor((this.getMonth() + 3) / 3),
      "S": this.getMilliseconds()
    }

    if (/(y+)/.test(format)) {
      format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
      if (new RegExp("(" + k + ")").test(format)) {
        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
      }
    }
    return format;
  }
  // 函数名称： addYear
  // 函数功能： 年份加减(负值表示减)
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addYear = function (value) {
    this.setFullYear(this.getFullYear() + value);
    return this;
  }
  // 函数名称： addMonth
  // 函数功能： 月份加减
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addMonth = function (value) {
    this.setMonth(this.getMonth() + value);
    return this;
  }
  // 函数名称： addDay
  // 函数功能： 天数加减
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addDay = function (value) {
    this.setDay(this.getDay() + value);
    return this;
  }
  // 函数名称： addHours
  // 函数功能： 小时加减
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addHours = function (value) {
    this.setHours(this.getHours() + value);
    return this;
  }
  // 函数名称： addMinutes
  // 函数功能： 分钟加减
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addMinutes = function (value) {
    this.setMinutes(this.getMinutes() + value);
    return this;
  }
  // 函数名称： addSeconds
  // 函数功能： 秒加减
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.addSeconds = function (value) {
    this.setSeconds(this.getSeconds() + value);
    return this;
  }
  // 函数名称： minus
  // 函数功能： 日期相减得到毫秒数
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.minus = function (value) {
    return this.getTime() - value.getTime();
  }
  // 函数名称： minus
  // 函数功能： 判断日期值是否相等
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.eq = function (value) {
    return this.minus(value) == 0;
  }
  // 函数名称： minus
  // 函数功能： 判断日期值是否大于指定的日期
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.gt = function (value) {
    return this.minus(value) > 0;
  }
  // 函数名称： minus
  // 函数功能： 判断日期值是否小于指定的日期
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Date.prototype.lt = function (value) {
    return this.minus(value) < 0;
  }
  // endregion

  // region Array 扩展
  Array.isArray = Array.isArray || function (arg) {
    return Object.prototype.toString.call(arg) === '[object Array]';
  };
  // 数组求差集[1,2],[2,3]结果为[1,3]
  Array.intersect = Array.intersect || function(one, two) {
    if(!Array.isArray(one) || !Array.isArray(two)) return [];
    return $.merge($.grep(one, function (i) {
        return $.inArray(i, two) == -1;
      }), $.grep(two, function (i) {
        return $.inArray(i, one) == -1;
      })
    );
  };
  // endregion

  // region String 扩展
  // 函数名称： toDate
  // 函数功能： 字符串转日期对象
  // 函数参数： format: 格式化参数
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  String.prototype.toDate = function (format) {
    var t = ['y+', 'M+', 'd+', 'H+', 'h+', 'm+', 's+', 'S'];
    var v = [];
    var iso = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)Z$/;
    var js = /\/Date\((\d+)\)\//gi;
    var re = RegExp;

    if (js.test(this)) {
      return new Date(+re.$1);
    } else if (iso.test(this)) {
      return new Date(Date.UTC(+re.$1, +re.$2 - 1, +re.$3, +re.$4, +re.$5, +re.$6));
    }

    for (var k in t) {
      var temp = new RegExp("(" + t[k] + ")", 'g');
      temp.test(format);
      var index = temp.lastIndex;
      if (index == 0) {
        v[k] = 0;
        continue;
      }
      var length = format.match(temp)[0].length;
      index -= length;
      v[k] = parseInt(this.substr(index, length));
      if(k == 4) {
        v[4] += 12;
      }
    }
    v[1]--;
    // v[4] += 12;
    return new Date(v[0], v[1], v[2], v[3] || v[4], v[5], v[6]);
  }
  // 函数名称： format
  // 函数功能： 字符串格式化
  // 函数参数： args: 格式化参数
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
      if (arguments.length == 1 && typeof (args) == "object") {
        for (var key in args) {
          if (args[key] !== undefined) {
            var reg = new RegExp("({" + key + "})", "g");
            result = result.replace(reg, args[key]);
          }
        }
      } else {
        // const args = Array.prototype.slice.call(arguments, 1);
        var params = arguments;
        result = result.replace(/\{(\d+)\}/g, function (m, i) {
          return params[i];
        });
      }
    }
    return result;
  };
  // 用正则表达式去掉字符串前后空格
  String.prototype.trim = String.prototype.trim || function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
  }
  // 用正则表达式去掉字符串前面的空格
  String.prototype.trimLeft = String.prototype.trimLeft || function () {
    return this.replace(/^\s*/g, "");
  }
  // 用正则表达式去掉字符串后面的空格
  String.prototype.trimRight = String.prototype.trimRight || function () {
    return this.replace(/\s*$/g, "");
  }
  // 判断以某个字符串结尾
  String.prototype.endWith = function (endStr) {
    if (endStr === null || endStr === undefined) return false;
    var lastL = this.length - endStr.length;
    return (lastL >= 0 && this.lastIndexOf(endStr) == lastL);
  }
  // endregion

  // region Number扩展
  // 函数名称： plus
  // 函数功能： 数字相加
  // 函数参数： num: 操作数, prec: 保留精度
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Number.prototype.plus = function (num, prec) {
    var p = Math.max(this.getPrecision(), parseFloat(num).getPrecision());
    var m = Math.pow(10, p);
    return Math.round((this * m + num * m) / Math.pow(10, p - prec)) / Math.pow(10, prec);
  }

  // 函数名称： minus
  // 函数功能： 数字相减
  // 函数参数： num: 操作数, prec: 保留精度
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Number.prototype.minus = function (num, prec) {
    var p = Math.max(this.getPrecision(), parseFloat(num).getPrecision());
    var m = Math.pow(10, p);
    return Math.round((this * m - num * m) / Math.pow(10, p - prec)) / Math.pow(10, prec);
  }

  // 函数名称： multiple
  // 函数功能： 数字相乘
  // 函数参数： num: 操作数, prec: 保留精度
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Number.prototype.multiple = function (num, prec) {
    var m1 = this.getPrecision();
    var m2 = parseFloat(num).getPrecision();
    return Math.round(((this * Math.pow(10, m1)) * (num * Math.pow(10, m2))) / Math.pow(10, m1 + m2 - prec)) / Math.pow(10, prec);
  }

  // 函数名称： divide
  // 函数功能： 数字相除
  // 函数参数： num: 操作数, prec: 保留精度
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Number.prototype.divide = function (num, prec) {
    var p = Math.max(this.getPrecision(), parseFloat(num).getPrecision());
    var m = Math.pow(10, p);
    return Math.round(((this * m) / (num * m)) * Math.pow(10, prec)) / Math.pow(10, prec);
  }

  // 函数名称： getPrecision
  // 函数功能： 获取数字的精度
  // 函数参数： 无
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2014-05-11 11:57:24
  Number.prototype.getPrecision = function () {
    var s = this.toString();
    var p = s.indexOf(".");
    return p == -1 ? 0 : (s.length - p - 1);
  }

  // 如(3, '￥', ',', '.')
  Number.prototype.format = function (precision, prefixSymbol, thousand, decimal) {
    precision = !isNaN(precision = Math.abs(precision)) ? precision : 2;
    prefixSymbol = !prefixSymbol ? '' : prefixSymbol;
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
      negative = number < 0 ? "-" : "",
      i = parseInt(number = Math.abs(+number || 0).toFixed(precision), 10) + "",
      j = (j = i.length) > 3 ? j % 3 : 0;
    return prefixSymbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (precision ? decimal + Math.abs(number - i).toFixed(precision).slice(2) : "");
  };
  // endregion

  ns.parseJSON = function (data) {
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

  /// 添加转json对象时对日期的处理
  if ($) {
    $.parseJSON = ns.parseJSON;
  }

  ns.getRawType = function (value) {
    return Object.prototype.toString.call(value).slice(8, -1)
  }
  ns.isNumber = function (value, min, max, prec) {
    if (isNaN(value)) {
      return {status: false, code: 1, msg: '请输入数字！'}
    }
    value = parseFloat(value);
    if ((prec || prec === 0) && value.getPrecision() > prec) {
      return {status: false, code: 2, msg: '应保留' + prec + '位小数！'};
    }
    if (!isNaN(min) && value < min) {
      return {status: false, code: 3, msg: '输入的数字应大于' + min + '！'};
    }
    if (!isNaN(max) && value > max) {
      return {status: false, code: 4, msg: '输入的数字应小于' + max + '！'};
    }
    return {status: true, code: 0, msg: ''};
  }

  // 函数名称： handleEvent
  // 函数功能： 给原生对象添加或移除一个或多个事件
  // 函数参数： bom: 原生bom对象，如window或document.body, div
  // 函数参数： eventType: 事件类型，如'resize', 'click'
  // 函数参数： funcList: 要执行函数，可以是单个函数，也可以是函数数组
  // 函数参数： type: 操作类型，如'add', 'remove'
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2013-12-19 22:46:14
  ns.handleEvent = function (bom, eventType, funcList, type) {
    if (!bom || !eventType || !funcList) return;
    var eventListener;
    if (type == 'remove') {
      eventListener = bom.removeEventListener || bom.detachEvent;
    } else {
      eventListener = bom.addEventListener || bom.attachEvent;
    }
    eventType = (window.addEventListener ? '' : 'on') + eventType;
    if (typeof (funcList) == 'function') {
      eventListener(eventType, funcList);
    } else if (Array.isArray(funcList)) {
      for (var i = 0; i < funcList.length; i++) {
        eventListener(eventType, funcList[i]);
      }
    }
  }

  // 函数名称： htmlEncode
  // 函数功能： html编码
  // 函数参数： str: 要编码的字符串
  // 返 回 值： 无
  // 创 建 人： zengjy01
  // 创建日期： 2013-12-19 22:46:14
  ns.htmlEncode = function (str) {
    // 复制权 &copy; 注册 &reg; 乘号 &times; 除号 &divide; 单引号 &apos; &mdash;
    //return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/'/g, "&apos;").replace(/"/g, "&quot;");
    var regExp = /[<>&'"]/g;

    return str.toString().replace(regExp, function (strChar, intPosition, strOldText) {
      switch (strChar) {
        case "<":
          return "&lt;";
          break;
        case ">":
          return "&gt;";
          break;
        case "&":
          return "&amp;";
          break;
        case "'":
          return "&apos;";
          break;
        case "\"":
          return "&quot;";
          break;
        default:
          return strChar;
          break;
      }
    });
  }
  ns.htmlEncode2 = function (value) {
    return $('<div/>').text(value).html();
  }
  //Html解码获取Html实体
  ns.htmlDecode = function htmlDecode(value) {
    return $('<div/>').html(value).text();
  }

  ns.initialQuery = function (url) {
    var reg, regKeyValue;
    var arrQuery, arrKeyValue;

    reg = new RegExp("\\?(.*)$", "i");
    regKeyValue = new RegExp("(.+)=(.*)", "i");

    if (!url) {
      url = window.location.href;
    }

    // 将查询信息放在window.Query集合里
    window.Query = [];
    arrQuery = url.match(reg);
    if (!arrQuery || !arrQuery[1]) {
      return;
    }
    arrQuery = arrQuery[1].split("&");

    for (var i = 0; i < arrQuery.length; i++) {
      arrKeyValue = arrQuery[i].match(regKeyValue);
      if (!arrKeyValue) {
        continue;
      }
      window.Query[arrKeyValue[1]] = arrKeyValue[2];
    }
  }
  ns.getQuery = function(key, url) {
    if(!key || !url) return '';
    var regKey = new RegExp("(\\?|&)" + key + "=([^&]*)(&|$)", 'i')
    var group = url.match(regKey);
    if(group) return group[2]
    return '';
  }
  /**
   * 获取文件名称
   * @param path
   * @returns {*}
   */
  ns.getFileName = function (path) {
    if (path.lastIndexOf("\\") == -1) return "";
    var pos = path.lastIndexOf("\\");
    return path.substring(pos + 1);
  }
  /**
   * 获取文件后缀
   * @param fileName
   * @returns {*}
   */
  ns.getFileExtension = function (fileName) {
    if (!fileName || fileName.indexOf(".") == -1) return '';
    var start = fileName.lastIndexOf(".");
    var end = fileName.length;
    return fileName.substring(start, end);
  }
})(window.Utility);

/// 系统常量
window.Constant = {
  EmptyGuid: "00000000-0000-0000-0000-000000000000"
}
window.ResultStatus = {
  "OK": {"key": "OK", "value": 1, "code": "", "name": "成功", "order": 0},
  "NO": {"key": "NO", "value": 2, "code": "", "name": "失败", "order": 0},
  "UNAUTHENTICATION": {"key": "UNAUTHENTICATION", "value": 3, "code": "", "name": "未登录", "order": 0},
  "UNAUTHORIZED": {"key": "UNAUTHORIZED", "value": 4, "code": "", "name": "未授权", "order": 0},
  "ERROR": {"key": "ERROR", "value": 5, "code": "", "name": "错误", "order": 0}
};
