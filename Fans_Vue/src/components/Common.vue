<template>
  <div id="Common">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/code/index"
          ><el-button round>JS代码安装</el-button></router-link
        >
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/code/index"
          ><el-button round>教程文档</el-button></router-link
        >
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/copy/visitor"
          ><el-button round>复制实时访客</el-button></router-link
        >
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/visitor/:1"
          ><el-button round>全部实时访客</el-button></router-link
        >
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/vchat/landing"
          ><el-button round>落地页微信管理设置</el-button></router-link
        >
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/copy/index"
          ><el-button round>落地页统计管理设置</el-button>
        </router-link>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/landing"
          ><el-button round>落地页管理</el-button>
        </router-link>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <router-link to="/inside/personal/:3"
          ><el-button round>升级续费</el-button>
        </router-link>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Common",
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
  margin-bottom: 10px;

  .el-button {
    width: 100%;
    padding: 0;
    height: 36px;
    line-height: 36px;
    font-size: 12px;
  }
}
</style>
