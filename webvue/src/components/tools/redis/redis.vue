<template>
  <div class='maincontent'>
    <div class="mypanel" style="width:500px;margin:auto;margin-top:20px;">
      <div class="panel-heading font-bold">redis操作</div>
      <div class="panel-body">
        <form class='myform infotip form-label-w110 block-form-group'>
          <div class="form-group">
            <label class="form-label req colon">数据类型</label>
            <div class="form-content">
              <select class='form-control' v-model="dataType">
                <option v-for="item in dataTypeOption" :value="item.key" v-text="item.name" />
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label req colon">操作类型</label>
            <div class="form-content">
              <select class='form-control' v-model="opType">
                <option v-for="item in dataOpOption" :value="item.key" v-text="item.name" />
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label req colon">键</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="键"
                     v-model='key' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="dataType === 'HASH' && (opType === 'GET' || opType === 'SET')">
            <label class="form-label req colon">字段</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="字段"
                     v-model='field' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="opType === 'SET' || opType === 'ADD_ITEM' || opType === 'DEL_ITEM'">
            <label class="form-label req colon">值</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="值"
                     v-model='value' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="dataType === 'ZSET' && (opType === 'SET' || opType === 'ADD_ITEM')">
            <label class="form-label req colon">分数</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="分数"
                     v-model='score' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="opType === 'GET'">
            <label class="form-label req colon">结果</label>
            <div class="form-content">
               <textarea type="text" class="form-control" placeholder="结果"
                         v-model='opResult' readonly="" />
            </div>
          </div>

          <div class="form-group text-right mb0">
            <button type="button" class="btn btn-purple mr5" @click="opRedis" :disabled='allDisabled'>
              <i class='fa fa-save'></i>操作
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "redis",
  data () {
    return {
      allDisabled: true,
      dataType: 'STRING',
      opType: 'GET',
      key: '',
      field: '',
      value: '',
      score: null,
      dataTypeOption: window.enumMap['RedisDataType'],
      opResult: ''
    }
  },
  methods: {
    init: function () {
      this.allDisabled = false;
    },
    opRedis: function() {
      var me = this;
      me.allDisabled = true;
      var param = {
        dataType: this.dataType,
        opType: this.opType,
        key: this.key,
        field: this.field,
        value: this.value,
        score: this.score
      };
      me.$axios.post('/redis/optRedis', param).then(function (resp) {
        if(resp.data.status === ResultStatus.OK.key) {
          me.$toaster.success('操作成功！');
          me.opResult = JSON.stringify(resp.data.value);
        }
        me.allDisabled = false
      });
    }
  },
  mounted: function () {
    this.init();
  },
  computed: {
    dataOpOption: function() {
      var arrDataType = ['STRING', 'HASH']
      var arrOpType = ['ADD_ITEM', 'DEL_ITEM']
      var list = []
      for(var ind in window.enumMap['RedisOpType']) {
        list.push(window.enumMap['RedisOpType'][ind]);
      }
      if(arrDataType.includes(this.dataType)) {
        list = list.filter(item => !arrOpType.includes(item.key))
      }
      return list
    }
  }
}
</script>

<style scoped>

</style>
