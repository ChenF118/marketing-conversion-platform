<template>
  <div id="Maneger">
    <MContainer>
      <template>
        <el-form-item>
          <el-input
            v-model="manegerform.words"
            placeholder="搜索url"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <!-- <template slot="Mdialog"> </template> -->

      <!-- 表格区 -->
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
            <el-table-column
              prop="address"
              label="监控页面链接"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="任务名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="监控频率"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="通知手机号"
              show-overflow-tooltip
            >
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
export default {
  name: "Maneger",
  created() {},
  data() {
    return {
      manegerform: {
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
  },
};
</script>

<style lang="scss" scoped></style>
