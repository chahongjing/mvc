<template>
  <div class='maincontent listcontent'>
    <form class='myform infotip form-label-w110 block-form-group'>
      <div class="form-group" v-for="item in switchList">
        <label class="form-label colon" v-text="item.name"></label>
        <div class="form-content">
          <label class="togglecheckbox">
            <input type='checkbox' :name="item.key" true-value="1" false-value="0" v-model="item.value" @change="changeSwitch(item)" :disabled="allDisabled"/>
            <i></i>
            <span></span>
          </label>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "list",
  data () {
    return {
      allDisabled: true,
      switchList: []
    }
  },
  methods: {
    init: function() {
      var me = this;
      me.$axios.get('/switch/queryAllSwitchList').then(function (resp) {
        if(resp.data.status === ResultStatus.OK.key) {
          me.switchList = resp.data.value;
          me.allDisabled = false;
        }
      });
    },
    changeSwitch: function(item) {
      var me = this;
      me.allDisabled = true;
      me.$axios.get('/switch/updateSwitch', {key: item.key, value: item.value}).then(function (resp) {
        if(resp.data.status === ResultStatus.OK.key) {
          me.$toaster.success(resp.data.value);
          me.allDisabled = false;
        }
      });
    }
  },
  mounted: function () {
    this.init();
  }
}
</script>

<style scoped>
  .myform{width:200px;margin:0 auto;}
</style>
