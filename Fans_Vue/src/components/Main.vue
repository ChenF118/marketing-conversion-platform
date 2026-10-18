<template>
  <div id="Main">
    <el-container>
      <el-header height="36px">
        <BreadCrumb></BreadCrumb>
      </el-header>
      <el-main>
        <Container></Container>
      </el-main>
      <el-footer height="30px">版本信息</el-footer>
    </el-container>
  </div>
</template>

<script>
import BreadCrumb from "@/components/BreadCrumb";
import Container from "@/components/Container";
export default {
  name: "Main",
  components: {
    BreadCrumb,
    Container,
  },
};
</script>

<style lang="scss" scoped>
#Main {
  height: 100%;
  // overflow: hidden;

  .el-container {
    height: 100%;

    .el-header {
      border-bottom: 1px solid #efeef0;
    }

    .el-header,
    .el-footer {
      background-color: #fff;
      color: #333;
      text-align: center;
    }
    .el-footer {
      line-height: 30px;
      font-size: 12px;
      background-color: #f1f1f1;
    }

    .el-main {
      background-color: #e9eef3;
      color: #333;
      text-align: center;
      padding: 0;
    }
  }
}
</style>
