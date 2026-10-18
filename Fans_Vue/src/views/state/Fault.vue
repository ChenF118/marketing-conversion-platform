<template>
  <div id="Fault">
    <MContainer>
      <template>
        <el-form-item>
          <el-input
            v-model="faultform.words"
            placeholder="搜索名称/url"
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
              label="任务名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="监控页面链接"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="故障时间"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="故障原因"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="短信通知"
              show-overflow-tooltip
            >
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
  name: "Fault",
  created() {},
  data() {
    return {
      faultform: {
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
