<template>
  <div class='list-pager'>
    <div class='footer-left inline-block pl5' :style="{'width':'calc(100% - ' + width + 'px)'}">
      共&nbsp;<span v-text='pagerInfo && pagerInfo.total || 0'></span>&nbsp;条记录,
      当前&nbsp;<span v-text='pagerInfo && pagerInfo.pageNum || 0'></span>/<span
      v-text='(pagerInfo && pagerInfo.pages || 0)'></span>&nbsp;页
      <i class='fa fa-refresh fa-spin text-success' v-if='pagerInfo && pagerInfo.loading'></i>
    </div>
    <div class='footer-right inline-block text-right pr5' :style="{'width':width + 'px'}">
      <ul class="pagination inline-block">
        <li v-for='item in getList' :class='{"page-item":true,"active":item.active,"disabled":item.disabled}' :title='item.title'>
          <a class="page-link" href="javascript:void(0)" @click='jumpPage(item.page)' v-html='item.text'>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'pagination',
    props: {pagerInfo: {pageNum: null, pages: null, total: null, callback: null, loading: false}},
    data () {
      return {
        width: 0
      }
    },
    methods: {
      jumpPage(page) {
        if (!this.pagerInfo || page < 1 || page == this.pagerInfo.pageNum || page > this.pagerInfo.pages || this.pagerInfo.loading) {
          return;
        }
        this.pagerInfo.callback && this.pagerInfo.callback(page);
      }
    },
    computed: {
      getList: function () {
        var me = this;
        var list = [];
        if (me.pagerInfo == null || !me.pagerInfo.pageNum) {
          return list;
        }
        list.push({page: me.pagerInfo.pageNum, text: me.pagerInfo.pageNum, active: true, title:'当前第' + me.pagerInfo.pageNum + '页'});
        var temp;
        for (var i = 1; i < 5; i++) {
          temp = me.pagerInfo.pageNum - i;
          if (temp > 0 && temp <= me.pagerInfo.pages) {
            list.unshift({page: temp, text: temp,title:'第' + temp + '页'});
            if (list.length == 5) break;
          }
          temp = me.pagerInfo.pageNum + i;
          if (temp > 0 && temp <= me.pagerInfo.pages) {
            list.push({page: temp, text: temp,title:'第' + temp + '页'});
            if (list.length == 5) break;
          }
        }
        me.width = list.length * 45 + 130;
        // 上一页
        list.unshift({page: me.pagerInfo.pageNum - 1, disabled: me.pagerInfo.pageNum <= 1, text: '&lsaquo;',title:'上一页'});
        // 首页
        list.unshift({page: 1, disabled: me.pagerInfo.pageNum <= 1, text: '&laquo;',title:'第一页'});
        // 下一页
        list.push({
          page: me.pagerInfo.pageNum + 1,
          disabled: me.pagerInfo.pageNum >= me.pagerInfo.pages,
          text: '&rsaquo;',
          title:'下一页'
        });
        // 末页
        list.push({page: me.pagerInfo.pages, disabled: me.pagerInfo.pageNum >= me.pagerInfo.pages, text: '&raquo;',title:'最后一页'});
        return list;
      }
    }
  }
</script>

<style scoped>
  .list-pager {
    margin-top: 5px;
    overflow: hidden;
  }

  .pagination .page-item {
    float: left;height:33px;
  }

  .footer-left, .footer-right {
    line-height: 40px;
    height: 40px;
    float: left;
  }
  .pagination li:nth-child(-n + 2) a,.pagination li:nth-last-child(-n + 2) a{font-size:20px;padding-top: .1rem;padding-bottom: .3rem;transition:0.3s;}
  .pagination li:nth-child(-n + 2):hover a,.pagination li:nth-last-child(-n + 2):hover a{color:#222;}
</style>
