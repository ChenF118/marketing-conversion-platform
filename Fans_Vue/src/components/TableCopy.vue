<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="id" width="80">
        <template slot-scope="scope">{{ scope.row.date }}</template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="管理员ID"
        width="80"
      ></el-table-column>
      <el-table-column prop="name" label="项目前缀ID"></el-table-column>
      <el-table-column
        prop="address"
        label="第几次点击"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="name" label="停留时间"> </el-table-column>
      <el-table-column prop="name" label="微信号"> </el-table-column>
      <el-table-column prop="name" label="项目前缀" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="name" label="事件位置"> </el-table-column>
      <el-table-column prop="name" label="事件类型"> </el-table-column>
      <el-table-column prop="name" label="ip地址"> </el-table-column>
      <el-table-column
        prop="name"
        label="用户访问地址"
        show-overflow-tooltip
        width="110"
      >
      </el-table-column>
      <el-table-column prop="name" label="创建时间"> </el-table-column>
    </el-table>

    <div class="page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[15, 30, 45, 60]"
        :page-size="15"
        layout=" prev, pager, next, jumper, total, sizes"
        :total="totalcount"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Table",
  data() {
    return {
      tableData: [
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
      ],
      multipleSelection: [],
      //   这个value要替换成数组里的数据
      value: true,
      currentPage: 1,
      totalcount: 4,
    };
  },

  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  },
};
</script>

<style lang="scss" scoped>
.control {
  padding-right: 10px;
  display: flex;
  justify-content: center;
  .el-button--mini {
    padding: 7px 5px;
  }
}
</style>
<style>
.el-pagination .el-pagination__jump {
  margin-right: 15px;
}
.el-pagination {
  margin-top: 10px;
}
</style>
