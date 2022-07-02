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
                <option v-for="item in dataTypeOption" :value="item.value" v-text="item.name" />
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label req colon">操作类型{{opType}}</label>
            <div class="form-content">
              <select class='form-control' v-model="opType">
                <option v-for="item in dataOpOption" :value="item.value" v-text="item.name" />
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
          <div class="form-group" v-if="dataType === 3 && (opType === 0 || opType === 1)">
            <label class="form-label req colon">字段</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="字段"
                     v-model='field' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="opType === 1 || opType === 3 || opType === 4">
            <label class="form-label req colon">值</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="值"
                     v-model='value' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="dataType === 4 && (opType === 1 || opType === 3)">
            <label class="form-label req colon">分数</label>
            <div class="form-content">
              <input type="text" class="form-control" placeholder="分数"
                     v-model='score' :disabled='allDisabled'/>
            </div>
          </div>
          <div class="form-group" v-if="opType === 0">
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
      dataType: 0,
      opType: 0,
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
        if(resp.data.status === ResultStatus.OK.value) {
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
      var arrDataType = [0, 3]
      var arrOpType = [3, 4]
      var list = []
      for(var ind in window.enumMap['RedisOpType']) {
        list.push(window.enumMap['RedisOpType'][ind]);
      }
      if(arrDataType.includes(this.dataType)) {
        list = list.filter(item => !arrOpType.includes(item.value))
      }
      return list
    }
  }
}
</script>

<style scoped>

</style>
