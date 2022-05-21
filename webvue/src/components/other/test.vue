<template>
  <div class="hello h100p oya">
    <div style="width:500px;margin:auto;padding-bottom:200px;">
      <form class='myform infotip form-label-w110 block-form-group' style="width:487px;margin:auto;">
        <div class="form-group">
          <label class="form-label colon">普通文本框</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="普通文本框，colon显示冒号" maxlength="50" autofocus v-focus :disabled="allDisabled"/>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label req">带必填无冒号</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="colon显示冒号，req显示必填" maxlength="50" :disabled="allDisabled"/>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label">&nbsp;</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="没有label标签" maxlength="50" :disabled="allDisabled"/>
          </div>
        </div>
        <div class="form-group info-error">
          <label class="form-label req colon">验证不通过</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="添加 info-error 类" maxlength="50" :disabled="allDisabled"/>
          </div>
          <div class='form-info'>
            <i class='fa' title='格式不正确！'></i>
          </div>
        </div>
        <div class="form-group info-success">
          <label class="form-label colon">验证通过</label>
          <div class="form-content">
            <input type="text" class="form-control" placeholder="添加 info-success 类" maxlength="50" :disabled="allDisabled"/>
          </div>
          <div class='form-info'>
            <i class='fa' title='验证通过！'></i>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">邮箱</label>
          <div class="form-content">
            <div class="input-group">
              <div class="input-group-prepend">
                <span class="input-group-text">&yen;</span>
              </div>
              <input type="text" class="form-control border-right-0">
              <div class="input-group-append">
                <span class="input-group-text">@qq.com</span>
              </div>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">日期</label>
          <div class="form-content">
            <date-time-picker v-model='beginTime' :option='dateOpt' :disabled="allDisabled"></date-time-picker>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">日期时间</label>
          <div class="form-content" style="position:initial;">
            <date-time-range-picker v-model="beginTime" :type="2"
                                    :format="'yyyy-MM-dd HH:mm:ss'"
                                    :disabled="allDisabled"></date-time-range-picker>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">日期时间范围{{startDate}}-{{endDate}}</label>
          <div class="form-content" style="position:initial;">
            <date-time-range-picker v-model="beginTime" :from.sync="startDate" :to.sync="endDate" :type="4"
                                    :format="'yyyy-MM-dd HH:mm:ss'"
                                    :disabled="allDisabled"></date-time-range-picker>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">日期时间</label>
          <div class="form-content">
            <date-time-range-picker2 :from.sync="startDate3" :type="3" :disabled="allDisabled"></date-time-range-picker2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">日期时间范围</label>
          <div class="form-content">
            <date-time-range-picker2 :from.sync="startDate4" :to.sync="endDate4" :type="4" :disabled="allDisabled"></date-time-range-picker2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">时间</label>
          <div class="form-content">
            <date-time-range-picker2 v-model="beginTime5" :type="5" :disabled="allDisabled"></date-time-range-picker2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">月</label>
          <div class="form-content">
            <date-time-range-picker2 v-model="beginTime5" :type="7" :disabled="allDisabled"></date-time-range-picker2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">单选下拉搜索</label>
          <div class="form-content">
            <select2 :list="selectList" v-model="selectValue" :id-field="'myid'" :text-field="'name'" :disabled="allDisabled"></select2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">多选下拉搜索</label>
          <div class="form-content">
            <select2 :list="selectList" v-model="selectValueArr" :id-field="'myid'" :text-field="'name'"
                     :multiple="true" :disabled="allDisabled"></select2>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">单选</label>
          <div class="form-content">
            <label class="radio_checkbox mt2">
              <input type='radio' name="importantCustomer" :value="1" v-model="importantCustomer"
                     @change="changeImportantCst()" :disabled="allDisabled"/>
              <i></i>
              <span class="req-text ">重要客户</span>
            </label>
            <label class="radio_checkbox mt2">
              <input type='radio' name="importantCustomer" :value="2" v-model="importantCustomer"
                     @change="changeImportantCst()" :disabled="allDisabled"/>
              <i></i>
              <span class="req-text ">非重要客户</span>
            </label>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">多选</label>
          <div class="form-content">
            <label class="radio_checkbox mt2" v-for="item in chklist">
              <input type='checkbox' name="importantCustomerchk" v-model="item.checked"
                     @change="changeImportantCst()" :disabled="allDisabled"/>
              <i></i>
              <span v-text="item.name"></span>
            </label>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label colon">是否有效</label>
          <div class="form-content">
            <label class="togglecheckbox">
              <input type='checkbox' name="checkbox" v-model="yn" @change="changeYn()" :disabled="allDisabled"/>
              <i></i>
              <span>addfe</span>
            </label>
          </div>
        </div>
        <div class="form-group">
          <label class="form-label">文件：</label>
          <div class="form-content">
            <input type="file" id='testFile' class="form-control" multiple placeholder="文件" :disabled="allDisabled"/>
          </div>
          <div class='form-info'>
            <i class='fa'></i>
          </div>
        </div>
        <div class="form-group text-right mb0">
          <button type="button" class="btn btn-purple mr5" @click="axiosUploadFile">axios上传</button>
          <button type="button" class="btn btn-purple mr5" @click="axiosDownload">axios下载</button>
          <button type="button" class="btn btn-purple mr5" @click="jqueryUpload">jquery上传</button>
          <button type="button" class="btn btn-purple mr5" @click="jsDownload">js下载</button>
        </div>
      </form>
      <hr>
      <button @click="showMyLoading">显示遮罩</button>
      <button @click="showVideo">播放视频</button>
      <button @click="showAudio">播放音频</button>
      <button @click="showAlert">alert</button>
      <button @click="showConfirm">confirm</button>
      <hr>
      <button @click="showToasterInfo">toaster info</button>
      <button @click="showToasterWarn">toaster warn</button>
      <button @click="showToasterError">toaster error</button>
      <button @click="showToasterSuccess">toaster success</button>
      <button @click="changeValue">改变值</button>
      <button @click="changeDisabled">改变disabled</button>
      <hr>
      <media-player :files="mediaList" :file-domain="imgDomain" :disabled="allDisabled"></media-player>
      <hr>
      <file-upload-and-preview :files="mediaList" :file-suffix="fileSuffix" :data-bus="dataBus" :max-file-num="maxFileNum"
                               :file-domain="imgDomain" :disabled="allDisabled" :type="fileType + 1"></file-upload-and-preview>
      <hr>
      <img-slider :list="mediaList"></img-slider>
      <hr>
      <div class="mt20">
        <ultree class='w300 mytree' :plainList="treeData" :option='treeoption'></ultree>
      </div>
      <hr>
      hover
      <div style="height:50px;background-color: #aaa;" v-tooltip="html"></div>
      <hr>
      <div v-mytest="dv" style="height:50px;background-color: green;" v-text="dv"></div>
      <hr>
      <process-list :list="nodeList"></process-list>
      <hr>
      <a href='javascript:void(0)' @click="goList">列表</a>
      <hr>
      <div class='aa'>
      </div>
      <hr>
      <div class='trcon'>
        <a href='http://sortablejs.github.io/Sortable/'>Sortable.js</a>
        <table id='mainRongQi' class='w100p'>
          <thead>
          <tr>
            <th>
              内容
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for='rongQi in list'>
            <td :data-id='rongQi.id' class='sortable a'>
              <span v-for='item in rongQi.dataList' :data-id='item.id' :data-pid='rongQi.id'>
                <span v-text='item.name + "" + item.xuhao'></span>
                <i class='fa fa-arrows pointer'></i>
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class='trcon'>
        <table id='mainRongQi2' class='w100p'>
          <thead>
          <tr>
            <th>
              内容
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for='rongQi in list2'>
            <td :data-id='rongQi.id' class='sortable b'>
              <span v-for='item in rongQi.dataList' :data-id='item.id' :data-pid='rongQi.id'>
                <span v-text='item.name + "" + item.xuhao'></span>
                <i class='fa fa-arrows pointer'></i>
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
  import imgSlider from '@/components/common/imgSlider';

  var treeDataList = [
    {id: 1, name: '语文语文语文语文语文语文语文语文语文语文语文', pId: null, selected: false, isOpen: false, isLeaf: false},
    {id: 2, name: '数学数学数学数学数学数学数学', pId: null, selected: false, isOpen: false, isLeaf: false},
    {
      id: 3,
      name: '英语<b style="color:red;">我的html我的html我的html我的html</b>',
      pId: null,
      selected: false,
      isOpen: false,
      isLeaf: false
    },
    {id: 4, name: '注音注音注音注音注音注音', pId: 1, selected: false, isOpen: false, isLeaf: false},
    {id: 5, name: '成语错字识别成语错字识别成语错字识别成语错字识别成语错字识别', pId: 1, selected: false, isOpen: false, isLeaf: false},
    {id: 6, name: '正确的词正确的词正确的词正确的词', pId: 1, selected: false, isOpen: false, isLeaf: false},
    {id: 7, name: '四字成语四字成语四字成语四字成语', pId: 5, selected: false, isOpen: false, isLeaf: false},
    {id: 8, name: '七言绝句七言绝句七言绝句七言绝句', pId: 5, selected: false, isOpen: false, isLeaf: false},
    {id: 9, name: '三角函数三角函数三角函数三角函数三角函数三角函数三角函数三角函数三角函数', pId: 2, selected: false, isOpen: false, isLeaf: false},
    {id: 10, name: '立体几何立体几何立体几何立体几何立体几何', pId: 2, selected: false, isOpen: false, isLeaf: false},
    {id: 11, name: '三角函数三角函数三角函数三角函数三角函数三角函数', pId: 8, selected: false, isOpen: false, isLeaf: false}
  ];
  var treeoption = {
    id: 'abc',
    openLevel: 2,
    checktype: 'checkbox1',
    checkedResult: [treeDataList[0], treeDataList[6]],
    // showIcon:true,
    // checktype: 'radio',
    // checkedResult: treeDataList[0],
    beforeOpenClose: function (item) {
      console.log('beforeOpenClose');
      console.log(item);
    },
    afterOpenClose: function (item) {
      console.log('afterOpenClose');
      console.log(item);
    },
    beforeClick: function (item) {
      console.log('beforeClick');
      console.log(item);
    },
    afterCheck: function (item) {
      console.log('afterCheck');
      console.log(item);
    }
  }
  treeoption.afterClick = function (item) {
    console.log('afterClick');
    console.log(item);
    if (treeoption.checktype == 'radio') {
      treeoption.checkedResult = item;
    }
  };

  export default {
    name: 'HelloWorld',
    data() {
      return {
        allDisabled: false,
        dateOpt: {format: 'yyyy-mm-dd hh:ii:ss', minView: 0, disabled: false},
        selectList: [{myid: 1, name: '待处理'}, {myid: 2, name: '已接单'}, {myid: 3, name: '处理中'}, {
          myid: 4,
          name: '待审核'
        }, {myid: 5, name: '已审核'}, {myid: 6, name: '已完成'}],
        // startDate: new Date(2020, 1, 10, 20, 33, 43),
        startDate:null,
        // endDate: new Date(2020, 1, 13, 10, 6, 20),
        endDate: null,
        myDate: null,
        startDate3:null,
        startDate4:null,
        endDate4:null,
        beginTime5:null,
        treeData: treeDataList,
        treeoption: treeoption,
        selectValue: null,
        selectValueArr: [],
        html: '这是<span class="red bold">html</span>内容',
        // beginTime: new Date(),
        beginTime: null,
        yn: true,
        chklist: [{id: 1, name: 'java'}, {id: 2, name: 'js'}, {id: 3, name: 'css'}],
        importantCustomer: 2,
        mediaList: [],
        imgDomain: 'http://img.dmallcdn.com/',
        list: [],
        list2: [],
        fileList: [],
        fileSuffix:['jpg','png','gif','mp3','mp4'],
        maxFileNum: 8,
        fileType: 0,
        dataBus:{},
        nodeList: [],
        a:'a',
        b:true
      }
    },
    methods: {
      showMyLoading: function () {
        this.$root.showLoading();
        var me = this;
        setTimeout(function () {
          me.$root.hideLoading();
        }, 1500)
      },
      showToasterInfo: function () {
        this.$toaster.info('info消息！');
      },
      showToasterWarn: function () {
        this.$toaster.warning('warning消息！');
      },
      showToasterSuccess: function () {
        this.$toaster.success('success消息！');
      },
      showToasterError: function () {
        this.$toaster.error('error消息！');
      },
      showAlert: function () {
        this.$confirm.alert('保存成功！');
      },
      showConfirm: function () {
        this.$confirm.confirm('确定要退出吗?', function () {
          console.log('确定退出事件');
        });
      },
      goList: function () {
        this.$router.push('/setting/settingList');
      },
      changeImportantCst: function () {
        console.log('changeImportantCst');
      },
      showAudio: function () {
        // 小米mp3
        var src = "https://img.dmallcdn.com/workorder/202001/bcb1afff-ebaa-4b18-a6e2-6d559c647135.mp3";
        // iphone mp3
        var src = "https://img.dmallcdn.com/workorder/202001/11b63c21-9cf3-4fb2-a3cf-bcd9d35164fe.mp3";
        this.$cstModal.showAudio({src: src});
      },
      showVideo: function () {
        // 普通m4v
        var src = "https://img.dmallcdn.com/workorder/202001/1bbb3f22-2bd0-4f77-abb2-82cbacab2332.m4v";
        // 小米mp4
        // var src = "https://img.dmallcdn.com/workorder/202001/0498fa62-2a95-4031-b8c1-1cc3141b8038.mp4";
        // 小米mp4
        // var src = "https://img.dmallcdn.com/workorder/202001/23d8cf5e-ec6f-45f5-a41c-c144e85f7a80.mp4";
        // iphone mp4
        // var src = "https://img.dmallcdn.com/workorder/202001/f163094e-29bf-4abe-8727-43fd44049000.mp4";
        this.$cstModal.showVideo({src: src});

      },
      changeYn: function () {
      },
      axiosUploadFile() {
        var me = this;
        this.$axios.post('/learn/testPostWithFile', this.getFormData()).then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            console.log(resp.data.value);
            me.$toaster.success('上传成功！');
          }
        });
      },
      axiosDownload() {
        var me = this;
        this.$axios.postDownload('/learn/download').then(function (resp) {
          Utility.downloadAfterAjax(resp.data, resp.headers);
        });
      },
      jqueryUpload: function() {
        Utility.jqueryUpload('http://localhost:21000/ToolSiteMvc4J/learn/testPostWithFile', this.getFormData());
      },
      jsDownload() {
        Utility.jsDownload('http://localhost:21000/ToolSiteMvc4J/learn/download');
      },
      getFormData: function() {
        var formData = new FormData();
        // new FormData($('.myform')[0])
        formData.append('userCode', 'zjy');
        formData.append('userName', '曾军毅');
        formData.append('birthday', new Date());
        var files = $('#testFile')[0].files;
        if (files && files.length > 0) {
          for (var i = 0; i < files.length; i++) {
            formData.append('myfile', files[i]);
          }
        }
        return formData;
      },
      // region 拖拽
      initDrag() {
        var me = this;
        var sortable = $(".a");
        sortable.sortable({
          containment: sortable.closest('table'),
          connectWith: sortable,
          appendTo: sortable,
          // handle:sortable.find('.fa-arrows'),
          revert: true,
          scrollSensitivity: 20,
          start: me.dragStart,
          stop: function (event, curEle) {
            return me.dragStop(event, curEle, sortable, me.list);
          }
        }).disableSelection();


        var sortable = $(".b");
        sortable.sortable({
          containment: sortable.closest('table'),
          connectWith: sortable,
          appendTo: sortable,
          handle: sortable.find('.fa-arrows'),
          revert: true,
          scrollSensitivity: 20,
          start: me.dragStart,
          stop: function (event, curEle) {
            return me.dragStop(event, curEle, sortable, me.list2);
          }
        }).disableSelection();
      },
      dragStart(event, curEle) {
        // 设置高度
        curEle.helper.css({backgroundColor: 'rgba(255,255,255,0.5)'});
        curEle.placeholder.css({height: '29px'});
      },
      dragStop(event, curEle, obj, list) {
        var me = this;
        // 获取信息
        var map = this.findParentsAndCurrent(list, curEle);
        var newParent = map.get('newParent'), oldParent = map.get('oldParent'),
          newIndex = map.get('newIndex'), oldIndex = map.get('oldIndex'),
          current = map.get('current');

        // 不符合条件，不能再拖动
        if (current.lcseq > 0) {
          alert('不能拖动！');
          return false;
        }

        // 去掉原父级下的当前结点
        oldParent.dataList.splice(oldIndex, 1);
        // 添加到新结点
        newParent.dataList.splice(newIndex, 0, current);
        // 改父级id
        current.pId = newParent.id;
        // 处理序号
        for (var i = 0; i < newParent.dataList.length; i++) {
          newParent.dataList[i].xuhao = i;
        }
        for (var i = 0; i < oldParent.dataList.length; i++) {
          oldParent.dataList[i].xuhao = i;
        }
        // 撤销jquery的dom操作，因为数据列表已发生变化,vue会自动更新列表
        me.$nextTick(function () {
          obj.sortable("destroy");
          obj.sortable({
            containment: obj.closest('table'),
            connectWith: obj,
            appendTo: obj,
            handle: obj.find('.fa-arrows'),
            revert: true,
            scrollSensitivity: 20,
            start: me.dragStart,
            stop: function (event, curEle) {
              return me.dragStop(event, curEle, obj, me.list2);
            }
          }).disableSelection();
        });
        return false;
      },
      findParentsAndCurrent(list, curEle) {
        var map = new Map(), rongQi, newParent = curEle.item.closest('td'),
          oldParentRongQiId = curEle.item.attr('data-pid') + '', newParentRongQiId = newParent.attr('data-id') + '',
          elId = curEle.item.attr('data-id') + '', newIdList = [];
        // 查找父级
        for (var i = 0; i < list.length; i++) {
          rongQi = list[i];
          // 找到新父级
          if (rongQi.id == newParentRongQiId) {
            map.set('newParent', rongQi);
          }
          // 找到原父级
          if (rongQi.id == oldParentRongQiId) {
            map.set('oldParent', rongQi);
          }
        }
        // 新父级子节点顺序
        var children = newParent.children();
        for (var i = 0; i < children.length; i++) {
          newIdList.push($(children[i]).attr('data-id'));
        }
        // 当前元素在旧父级中的位置
        var oldParent = map.get('oldParent');
        for (var i = 0; i < oldParent.dataList.length; i++) {
          if (oldParent.dataList[i].id == elId) {
            map.set('current', oldParent.dataList[i]);
            map.set('oldIndex', i);
            break;
          }
        }
        // 当前元素在新父级中的位置
        for (var i = 0; i < newIdList.length; i++) {
          if (newIdList[i] == elId) {
            map.set('newIndex', i);
          }
        }
        return map;
      },
      // endregion
      changeValue: function() {
        this.fileType = (this.fileType + 1) % 2;
        // this.beginTime = new Date(new Date().setDate(new Date().getMonth() - (parseInt(Math.random() * 5) + 6)));
        this.startDate = new Date(new Date().setMonth(new Date().getMonth() - (parseInt(Math.random() * 5) + 1)));
        this.endDate = new Date(new Date().setMonth(new Date().getMonth() + (parseInt(Math.random() * 5) + 1)));
        // this.a = parseInt(Math.random() * 10) % 2 == 0;
        this.b = parseInt(Math.random() * 10) % 2 == 0;
      },
      changeDisabled: function() {
        this.allDisabled = !this.allDisabled;
      }
    },
    mounted: function() {
      window.vuedata = this;
      var me = this;
      var list1 = [];
      var list2 = [];
      list1.push({id: 1, pId: 'A', xuhao: 0, name: '第一个'});
      list1.push({id: 2, pId: 'A', xuhao: 1, name: '第二个'});
      list1.push({id: 3, pId: 'A', xuhao: 2, name: '第三个'});
      list2.push({id: 4, pId: 'B', xuhao: 0, name: '第四个'});
      list2.push({id: 5, pId: 'B', xuhao: 1, name: '第五个'});
      list2.push({id: 6, pId: 'B', xuhao: 2, name: '第六个'});

      this.list.push({id: 'A', dataList: list1});
      this.list.push({id: 'B', dataList: list2});

      list1 = [];
      list2 = [];
      list1.push({id: 1, pId: 'A', xuhao: 0, name: '第七个'});
      list1.push({id: 2, pId: 'A', xuhao: 1, name: '第八个'});
      list1.push({id: 3, pId: 'A', xuhao: 2, name: '第九个'});
      list2.push({id: 4, pId: 'B', xuhao: 0, name: '第十个'});
      list2.push({id: 5, pId: 'B', xuhao: 1, name: '第十一个'});
      list2.push({id: 6, pId: 'B', xuhao: 2, name: '第十二个'});

      this.list2.push({id: 'A', dataList: list1});
      this.list2.push({id: 'B', dataList: list2});

      this.$nextTick(function () {
        me.initDrag();
      });
      this.mediaList.push({url:'workorder/202001/58e03540-87c7-4c3c-ada1-c6d653e990c6.png'});
      // this.mediaList.push({url:'workorder/202001/00b865b6-25e1-4092-8446-7d4580ed897d.mp3'});
      // this.mediaList.push({url:'workorder/202001/703a14ca-e601-428d-89a6-0a1abe9fb217.mp4'});
      this.mediaList.push({url:'workorder/202001/10386cc0-8af5-4fd8-a650-39da1229fc3b.png'});
      this.mediaList.push({url:'workorder/202001/c9f82e86-0d32-459d-bf67-d9ddbc976a2e.png'});
        this.mediaList.push({url:'workorder/202001/3421b695-2db4-410e-9b81-cb2e41d284ad.png'});

      this.mediaList.push({url:'workorder/202001/ba71b956-8c62-4d51-b4b7-d17d2728da5a.png'});
      this.mediaList.push({url:'workorder/202001/2d51b60b-b2ed-4cca-ad9e-97aea00918f8.png'});
      this.mediaList.push({url:'workorder/202001/fece44dd-42f7-4629-87d6-facad535eb55.png'});

      this.nodeList.push({name: '创建', hasPass:true});
      this.nodeList.push({name: '提交', hasPass:true});
      this.nodeList.push({name: '接单', hasPass:true});
      this.nodeList.push({name: '审核内容过长', current:true});
      this.nodeList.push({name: '通过', current:false});
      this.nodeList.push({name: '关单', current:false});
    },
    computed: {
      dv: function() {
        return {a:this.a,b:this.dv2};
      },
      dv2: function() {
        return this.b;
      }
    },
    components: {imgSlider}
  }
</script>

<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
  .aa {
    width: 200px;
    height: 200px;
    background-color: #fff;
    border: 3px solid;
    border-image: linear-gradient(to bottom, red 0%, gold 100%);
    border-image-slice: 1;
  }

  .trcon {
    border: 1px solid #999;
  }

  .sortable > span {
    font-size: 14px;
    display: inline-block;
    padding: 3px 5px;
    border: 1px solid #aaa;
  }
</style>
