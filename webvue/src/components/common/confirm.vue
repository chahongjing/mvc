<template>
  <common-modal :show-modal='showDialog' :options="modalOpt">
    <div class="modal-header" slot='headerSlot'>
      <h5 class="modal-title" v-text='title'></h5>
      <button type="button" class="close" @click='defaultClose()'>
        <span class='closeicon' title="关闭">&times;</span>
      </button>
    </div>
    <div class="modal-body" slot="bodySlot" v-text='message'>
    </div>
    <div class="modal-footer" slot="footerSlot">
      <button type="button" :class='getCancelCls' v-if='closeBtn.show'
              @click='defaultClose()'>
        <i :class='getCancelIconCls'></i><span v-text='closeBtn.text'></span>
      </button>
      <button type="button" :class='getConfirmCls' v-if='confirmBtn.show'
              @click='defaultConfirm()'>
        <i :class='getConfirmIconCls'></i><span v-text='confirmBtn.text'></span>
      </button>
    </div>
  </common-modal>
</template>

<script>
  import commonModal from '@/components/common/commonModal';

  export default {
    name: 'confirm',
    data () {
      return {
        showDialog: false,
        title: '确认信息',
        message: '确定要退出吗？',
        closeBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '关闭', fn: null},
        confirmBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '确定', fn: null},
        modalOpt: {width: '350px'}
      }
    },
    methods: {
      show: function () {
        this.showDialog = true;
      },
      defaultClose: function () {
        var me = this;
        if (me.closeBtn && me.closeBtn.fn) {
          me.closeBtn && me.closeBtn.fn();
        }
        me.showDialog = false;
      },
      defaultConfirm: function () {
        var me = this;
        if (me.confirmBtn && me.confirmBtn.fn) {
          me.confirmBtn && me.confirmBtn.fn();
        }
        me.showDialog = false;
      }
    },
    computed: {
      getCancelCls: function () {
        var obj = {'btn btn-secondary':true};
        if (this.closeBtn && this.closeBtn.cls) {
          obj[this.closeBtn.cls] = true;
        }
        return obj;
      },
      getCancelIconCls: function () {
        var obj = {'fa fa-times':true};
        if (this.closeBtn && this.closeBtn.iconCls) {
          obj[this.closeBtn.iconCls] = true;
        }
        return obj;
      },
      getConfirmCls: function () {
        var obj = {'btn btn-purple':true};
        if (this.confirmBtn && this.confirmBtn.cls) {
          obj[this.confirmBtn.cls] = true;
        }
        return obj;
      },
      getConfirmIconCls: function () {
        var obj = {'fa fa-check':true};
        if (this.confirmBtn && this.confirmBtn.iconCls) {
          obj[this.confirmBtn.iconCls] = true;
        }
        return obj;
      }
    },
    components: {commonModal}
  }
</script>
