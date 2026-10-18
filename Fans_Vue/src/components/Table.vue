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
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column prop="UID" label="前缀ID" width="80"></el-table-column>
      <el-table-column
        prop="pre_project"
        label="项目前缀"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="remarks" label="备注" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="v_chat" label="微信号"> </el-table-column>

      <el-table-column label="二维码图片" width="90">
        <template slot-scope="scope">
          <el-avatar
            shape="square"
            size="medium"
            :src="scope.row.QRcode"
          ></el-avatar>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="手机号"> </el-table-column>

      <el-table-column label="展示状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#13ce66"
            inactive-color="#ff4949"
          ></el-switch>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="微信备注"> </el-table-column>
      <el-table-column prop="createtime" label="创建时间"> </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <div class="control">
            <el-button type="success" size="mini">编辑</el-button>
            <el-button type="danger" size="mini" @click="open(scope.row.id)"
              >删除</el-button
            >
          </div>
        </template>
      </el-table-column>
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
import { deleteApi } from "@/api/delete.js";
export default {
  name: "Table",
  props: ["tableData", "totalcount"],
  data() {
    return {
      multipleSelection: [],
      //   这个value要替换成数组里的数据
      currentPage: 1,
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
    // 复选框触发
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$emit("transmisson", this.multipleSelection);
    },
    // 切换每页条数
    handleSizeChange(val) {
      this.$emit("pagesizechange", val);
    },
    // 切换页码
    handleCurrentChange(val) {
      this.$emit("pagechange", val);
    },
    // 单独删除
    async singledelete(val) {
      await deleteApi(val);
      this.$emit("reducelist", val);
    },
    // 单独删除
    open(value) {
      this.$confirm("将永久删除该用户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.singledelete(value);
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
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
