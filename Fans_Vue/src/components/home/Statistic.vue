<template>
  <div id="Statistic">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>访问PV</h3>
          <p>{{ statistic.pv }}</p>
        </div></el-col
      >
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>访问UV</h3>
          <p>{{ statistic.uv }}</p>
        </div></el-col
      >
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>访问IP</h3>
          <p>{{ statistic.ip }}</p>
        </div></el-col
      >
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>复制数量</h3>
          <p>{{ statistic.count }}</p>
        </div></el-col
      >
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>打开微信</h3>
          <p>{{ statistic.open }}</p>
        </div></el-col
      >
      <el-col :xs="24" :sm="12" :md="8" :lg="4"
        ><div class="grid-content bg-purple">
          <h3>行为转化</h3>
          <p>{{ statistic.cvs }}</p>
        </div></el-col
      >
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Statistic",
  props: ["statistic"],
  components: {},
};
</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  height: 82px;
  padding: 10px 15px;
  margin-bottom: 10px;
  text-align: left;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-around;

  h3 {
    font-size: 12px;
    color: #999;
    font-weight: 400;
  }
  p {
    color: #009688;
  }
}
</style>
