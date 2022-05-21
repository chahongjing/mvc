<template>
  <div>
    <div class="modal-backdrop fade hide">
    </div>
    <div class="modal fade" tabindex="-1">
      <div class="modal-dialog modal-dialog-centered" :style='getStyle()'>
        <div class='blankheader'></div>
        <div class="modal-content">
          <slot name='headerSlot'/>
          <slot name='bodySlot'/>
          <slot name='footerSlot'/>
        </div>
        <div class='blankfooter'></div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'commonModal',
    props: {showModal: false, options:null},
    watch: {
      showModal(curVal, oldVal) {
        var cur = $(this.$el);
        var back = cur.find('.modal-backdrop');
        var modal = cur.find('.modal');

        if (curVal) {
          back.removeClass('hide').show().addClass('show');
          modal.show();
          setTimeout(function () {
            modal.addClass('show')
          }, 40);
        } else {
          var me = this;
          modal.removeClass('show');
          setTimeout(function () {
            modal.hide();
            back.hide().removeClass('show').addClass('hide');
          }, 250);
        }
      }
    },
    methods: {
      getStyle: function () {
        var obj = {};
        if (this.options && this.options.width) {
          obj.width = this.options.width;
        }
        if (this.options && this.options.height) {
          obj.height = this.options.height;
        }
        if (this.options && this.options.minWidth) {
          obj.minWidth = this.options.minWidth;
        }
        if (this.options && this.options.maxWidth) {
          obj.maxWidth = this.options.maxWidth;
        }
        if (this.options && this.options.minHeight) {
          obj.minHeight = this.options.minHeight;
        }
        if (this.options && this.options.maxHeight) {
          obj.maxHeight = this.options.maxHeight;
        }
        return obj;
      }
    }
  }
</script>

<style scoped>
  .modal-backdrop.hide {
    display: none;
  }

  .modal-dialog-centered {
    /*flex-flow: column;*/
    margin-left: auto;
    margin-right: auto;
    max-width: initial!important;
  }

  .blankheader {
    display: flex;
    flex: 1;
  }

  .blankfooter {
    display: flex;
    flex: 1.6;
  }

  .modal.fade.show {
    opacity: 1;
  }

  .modal-content {
    border-radius: .3rem;
    border-width: 0;
    box-shadow: 0 0 4px #224acd;
  }
</style>
