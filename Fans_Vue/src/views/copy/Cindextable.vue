<template>
  <div id="Cindextable">
    <div class="group-table">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        stripe
        :border="true"
        :header-cell-style="{
          background: '#fafafa',
        }"
        @selection-change="handleSelectionChange"
        empty-text="没有开启复制统计的链接，请在落地页管理中开启复制统计功能"
      >
        <el-table-column type="selection" width="50" align="center">
        </el-table-column>
        <el-table-column fixed label="落地页分组">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column>
        <el-table-column prop="name" label="落地页url" width="350">
        </el-table-column>
        <el-table-column
          prop="address"
          label="落地页备注"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="address"
          label="统计微信号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="address"
          label="移动端复制设置"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="address"
          label="PC端复制设置"
          show-overflow-tooltip
        >
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >

            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "Cindextable",
  created() {},
  data() {
    return {
      tableData: [],
      switchflag: false,
      switchflags: false,
      //   分页相关
      currentPage: 1,
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
  },
  components: {},
};
</script>

<style lang="scss" scoped>
#Cindextable {
  background-color: #fafafa;
  .button {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 10px 15px;
  }

  .page {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
}
</style>
<style>
/* 修改开关样式 */
/* switch按钮样式 */
.switchClass .el-switch__label {
  position: absolute;
  display: none;
  color: #fff !important;
}
/*打开时文字位置设置*/
.switchClass .el-switch__label--right {
  z-index: 1;
}
/* 调整打开时文字的显示位子 */
.switchClass .el-switch__label--right span {
  margin-right: 13px;
  font-size: 12px;
}
/*关闭时文字位置设置*/
.switchClass .el-switch__label--left {
  z-index: 1;
}
/* 调整关闭时文字的显示位子 */
.switchClass .el-switch__label--left span {
  margin-left: 17px;
  font-size: 12px;
}
/*显示文字*/
.switchClass .el-switch__label.is-active {
  display: block;
}
/* 调整按钮的宽度 */
.switchClass.el-switch .el-switch__core,
.el-switch .el-switch__label {
  width: 45px !important;
  margin: 0;
}

/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
</style>
