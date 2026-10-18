<template>
  <div id="MContainer">
    <!-- 搜索区 -->
    <div class="Cmform">
      <el-form :inline="true" size="mini" class="demo-form-inline">
        <slot name="default"></slot>

        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="onSubmit"
          ></el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮区 -->
    <div class="Mdialog">
      <slot name="Mdialog"></slot>
    </div>
    <!-- 表格区 -->
    <div class="Cmtable">
      <div class="table_top">
        <slot name="intable"></slot>
      </div>
      <slot name="tables"></slot>
      <div class="page">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="2"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MContainer",
  created() {},
  data() {
    return {
      // 分页部分当前页
      currentPage: 1,
    };
  },
  methods: {
    // 搜索按钮
    onSubmit() {
      console.log("submit!");
    },
    // 分页功能两个
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  },
  components: {},
};
</script>

<style lang="scss" scoped>
#MContainer {
  background-color: #fff;
  border-top: 1px solid #f6f6f6;
  padding-bottom: 20px;

  .Cmform,
  .Mdialog {
    padding: 10px 20px;
    display: flex;
    justify-content: flex-start;

    .el-form {
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
    }
  }
  .Cmtable {
    background-color: #fafafa;
    border: 1px solid #eee;
    margin: 0 20px;

    .table_top > div {
      margin: 10px;
    }

    .page {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
  }
}
</style>
<style>
/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
.Mdialog > div {
  margin-right: 5px;
}
</style>
