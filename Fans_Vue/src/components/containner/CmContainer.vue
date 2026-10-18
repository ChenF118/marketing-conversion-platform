<template>
  <div id="CmContainer">
    <!-- 搜索区 -->
    <div class="Cmform">
      <el-form :inline="true" size="small" class="demo-form-inline">
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
    <!-- 日期按钮区 -->
    <div class="Cmbutton" @click="logg">
      <slot name="Cmbutton">
        <el-button type="warning" size="small" @click="today">今天</el-button>
        <el-button type="warning" size="small" @click="lastday">昨天</el-button>
        <el-button type="warning" size="small" @click="lastthreeday"
          >最近三天</el-button
        >
        <el-button type="warning" size="small" @click="lastweek"
          >最近一周</el-button
        >
        <el-button type="warning" size="small" @click="lastmonth"
          >最近一月</el-button
        >
        <el-button type="warning" size="small" @click="thismonth"
          >本月</el-button
        >
      </slot>
    </div>

    <!-- 表格区 -->
    <div class="Cmtable">
      <slot name="tables"></slot>
      <div class="page">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="count"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
// :count="totalcount"          子组件传来分页使用的总条数
// @sendSize="changeSize"          父传子单页数
// @sendCurrent="changeCurrent"    父传子页码
// @filtersearch="filtersearch"       筛选搜索
// @quickpick="pickdate"              快捷日期
export default {
  name: "CmContainer",
  created() {},
  props: ["count"],
  data() {
    return {
      // 分页部分当前页
      currentPage: 1,
      // 快捷日期
      date1: "",
      date2: "",
    };
  },
  methods: {
    // 搜索按钮
    onSubmit() {
      this.$emit("filtersearch");
    },
    // 分页功能两个
    handleSizeChange(val) {
      this.$emit("sendSize", val);
    },
    handleCurrentChange(val) {
      this.$emit("sendCurrent", val);
    },

    // 快捷生成日期
    today() {
      this.date1 = new Date().toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    lastday() {
      this.date1 = new Date(new Date() - 24 * 3600 * 1000).toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    lastthreeday() {
      this.date1 = new Date(
        new Date() - 24 * 3600 * 1000 * 3
      ).toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    lastweek() {
      this.date1 = new Date(
        new Date() - 24 * 3600 * 1000 * 7
      ).toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    lastmonth() {
      this.date1 = new Date(
        new Date() - 24 * 3600 * 1000 * 30
      ).toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    thismonth() {
      const day = new Date().getDate() - 1;
      this.date1 = new Date(
        new Date() - 24 * 3600 * 1000 * day
      ).toLocaleDateString();
      this.date2 = new Date().toLocaleDateString();
    },
    // 发送日期
    logg() {
      this.$emit("quickpick", [this.date1, this.date2]);
    },
  },
  components: {},
};
</script>

<style lang="scss" scoped>
#CmContainer {
  background-color: #fff;
  border-top: 1px solid #f6f6f6;
  padding-bottom: 20px;

  .Cmform,
  .Cmbutton {
    display: inline-block;
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
    margin: 0 20px;

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
</style>
