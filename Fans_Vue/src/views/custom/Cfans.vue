<template>
  <div id="Cfans">
    <MContainer>
      <template>
        <el-form-item>
          <el-input
            v-model="appform.words"
            placeholder="输入名称查询"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="Mdialog">
        <Seelanding></Seelanding>
      </template>

      <!-- 表格区 -->
      <template slot="intable">
        <div class="intable">
          <el-button type="warning" size="small">更改创建账号</el-button>
        </div>
      </template>
      <template slot="tables">
        <div class="Ctable">
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
            empty-text="无数据"
          >
            <el-table-column type="selection" width="55" align="center">
            </el-table-column>
            <el-table-column prop="address" label="名称" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="address"
              label="创建账号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="address" label="模板" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="address"
              label="小程序标题"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="设置微信来源分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="address" label="备注" show-overflow-tooltip>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="300">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
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
      </template>
    </MContainer>
  </div>
</template>

<script>
import MContainer from "@/components/containner/MContainer.vue";
import Seelanding from "@/components/button/Seelanding.vue";
export default {
  name: "Cfans",
  created() {},
  data() {
    return {
      appform: {
        words: "",
      },
      tableData: [],
    };
  },
  methods: {
    // 表格的方法
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
  components: {
    MContainer,
    Seelanding,
  },
};
</script>

<style lang="scss" scoped>
.intable {
  display: flex;
  justify-content: flex-start;
}
</style>
