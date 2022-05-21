<template>
  <div class="p20 oya h100p">
    <div id='' class='content h100p'>
      <div class='wrapper'>
        <div class='light'><i></i></div>
        <hr class='line-left'></hr>
        <hr class='line-right'></hr>
        <div class='main'>
          <h1 class='title'>客服系统</h1>
          <div class='year'>
            <h2><a href='javascript:void(0)' @click="showAll()">全部<i></i></a></h2>
            <div class='list'>
              <ul>
                <li class='cls' :class="{'hightlight': log.open}" v-for="log in list">
                  <p class='date'><span v-text="$options.filters.formatDate(log.upgradeTime, 'yyyy-MM-dd')"
                                        :title="$options.filters.formatDate(log.upgradeTime)"></span>
                    <span class='detail' @click="showDetail(log)"></span></p>
                  <p class='intro' v-text="log.title"></p>
                  <div class='more'>
                    <p v-for="(item,index) in getTextList(log.contentList)">
                      <span v-text="index + 1 + '. '"></span>
                      <i class="fa mr0" :class="getItemIcon(item)"></i>
                      <span v-text="item.text"></span>
                    </p>
                    <p v-for="(item,index) in getLinkList(log.contentList)" v-if="getLinkList(log.contentList).length > 0" style="margin-top:15px;">
                      <span v-text="index + 1 + '. '"></span>
                      <i class="fa mr0" :class="getItemIcon(item)"></i>
                      <a :href="item.link" v-text="item.text" target="_blank"></a>
                    </p>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "upgradeLogPreview",
    data: function () {
      return {
        list: []
      }
    },
    mounted: function () {
      this.init();
    },
    methods: {
      init: function () {
        this.queryAllLog();
      },
      queryAllLog: function () {
        var me = this;
        me.$axios.get('/upgradeLog/queryList').then(function (resp) {
          if (resp.data.status == ResultStatus.OK.key) {
            for(var i = 0; i < resp.data.value.length; i++) {
              resp.data.value[i].open = i == 0;
            }
            me.list = resp.data.value;
          }
        });
      },
      showDetail: function(log) {
        var tag = !!log.open;
        if(tag) {
          log.open = false;
          return;
        }
        for(var i = 0; i < this.list.length; i++) {
          if(this.list[i] == log) {
            this.list[i].open = true;
          } else {
            this.list[i].open = false;
          }
        }
      },
      showAll: function() {
        for(var i = 0; i < this.list.length; i++) {
          this.list[i].open = true;
        }
      },
      getItemIcon: function(item) {
        var obj = {};
        var icon = ['fa-yelp', 'fa-bug', 'fa-file-text-o']
        obj[icon[item.type - 1]] = true;
        return obj;
      },
      getTextList: function(list) {
        return list.filter(function(item) {
          return item.type != 3;
        })
      },
      getLinkList: function(list) {
        return list.filter(function(item) {
          return item.type == 3;
        })
      }
    }
  }
</script>

<style scoped>
  .content {
    width: 100%;
    padding: 50px 0 10px;
    background: url(../../../static/img/timeline/content-bg.png);
  }

  .content .wrapper {
    width: 980px;
    position: relative;
    margin: 0 auto;
    background: url(../../../static/img/timeline/release-bg.png) no-repeat right top;
    min-height: 520px;
  }

  .content .light {
    position: absolute;
    top: -50px;
    left: 55px;
    width: 150px;
    height: 190px;
    background: url(../../../static/img/timeline/light-top.png) no-repeat top center;
  }

  .content .light i {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    background: url(../../../static/img/timeline/light.png) no-repeat top center;
  }

  hr {
    border: none;
    height: 0;
    border-top: 1px dashed #2d2f34;
    border-bottom: 1px dashed #474954;
  }

  .content hr.line-left {
    position: absolute;
    left: 0;
    top: 15px;
    width: 70px;
  }

  .content hr.line-right {
    position: absolute;
    right: 0;
    top: 15px;
    width: 460px;
  }

  .content .main {
    background: url(../../../static/img/timeline/line-bg.png) repeat-y 249px 0;
  }

  .main .title {
    position: absolute;
    line-height: 40px;
    padding-left: 50px;
    left: 230px;
    top: 0px;
    color: #58a6fb;
    font-size: 24px;
    background: url(../../../static/img/timeline/clock.png) no-repeat left top;
  }

  .main .year {
    position: relative;
    z-index: 100;
  }

  .main .year h2 {
    height: 40px;
    width: 170px;
    padding-right: 16px;
    font-size: 24px;
    line-height: 40px;
    text-align: right;
  }

  .main .year h2 a {
    color: #58a6fb;
    text-decoration: none;
  }

  .main .year .list {
    maring: 10px 0;
    position: relative;
    transition: 1s linear;
  }

  .main .year .list ul li {
    background: url(../../../static/img/timeline/circle.png) no-repeat 235px 10px;
    padding: 10px 0 0;
    color: #a1a4b8;
    list-style: none;
    clear: both;
    overflow: hidden;
  }

  .main .year .list ul li.hightlight {
    background-image: url(../../../static/img/timeline/circle-h.png);
  }

  .main .year .list ul li .date {
    font-size: 18px;
    float: left;
    display: block;
    width: 200px;
    line-height: 30px;
    text-align: right;
    color: #63d029;
    position: relative;
  }

  .main .year .list ul li .intro, .main .year .list ul li .more {
    text-align: left;
    float: left;
    display: block;
    width: 400px;
    margin-left: 80px;
    line-height: 24px;
  }

  .more p {
    margin-bottom: 0px;
  }

  .main .year .list ul li .intro {
    font-size: 18px;
    color: #63d029;
    line-height: 29px;
  }

  .date .detail {
    border: 7px solid transparent;
    display: block;
    position: absolute;
    top: 8px;
    right: -32px;
    border-top: 7px solid green;
    transition: 0.5s;
    transform: rotate(-90deg);
  }

  .main .year .list ul li.hightlight .date .detail {
    transform: rotate(0deg);
    transform-origin: 50% 50% 0;
  }
  .cls .more{height:0;transition: 0.3s;}
  .cls.hightlight .more{height:auto;}
</style>
