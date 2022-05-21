<template>
  <common-modal :show-modal='showDialog'>
    <div class="modal-header" slot='headerSlot'>
      <h5 class="modal-title" v-text='title'></h5>
      <button type="button" class="close" @click='defaultClose()'>
        <span class='closeicon' title="关闭">&times;</span>
      </button>
    </div>
    <div class="modal-body" slot="bodySlot" v-text='message'>
    </div>
    <div class="modal-footer" slot="footerSlot">
      <button type="button" class='btn btn-secondary' @click='defaultClose()'>
        <i class="fa fa-times"></i><span>关闭</span>
      </button>
      <button type="button" class='btn btn-purple' @click='defaultConfirm()'>
        <i class='fa fa-check'></i><span>确定</span>
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
        confirmBtn: {show: true, cls: '', showIcon: true, iconCls: '', text: '确定', fn: null}
      }
    },
    methods: {
      show: function () {
        this.showDialog = true;
      },
      hide: function() {
        this.showDialog = false;
        // 删除弹框元素
        var me = this;
        setTimeout(function() {
          me.$destroy(true);
          me.$el.parentNode.removeChild(me.$el);
        }, 200);
      },
      defaultClose: function () {
        var me = this;
        if (me.closeBtn && me.closeBtn.fn) {
          me.closeBtn && me.closeBtn.fn();
        }
        me.showDialog = false;
        this.hide();
      },
      defaultConfirm: function () {
        var me = this;
        if (me.confirmBtn && me.confirmBtn.fn) {
          me.confirmBtn && me.confirmBtn.fn();
        }
        me.showDialog = false;
        me.hide();
      }
    },
    components: {commonModal}
  }
</script>
