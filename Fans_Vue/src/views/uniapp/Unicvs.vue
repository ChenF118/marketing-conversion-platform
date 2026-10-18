<template>
  <div id="Unicvs">
    <CmContainer>
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="cvsform.app"
            placeholder="选择小程序"
            :popper-append-to-body="false"
          >
            <el-option label="选择小程序" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 默认时间选择 -->
        <el-form-item>
          <el-col :span="11">
            <el-date-picker
              type="date"
              placeholder="开始日期"
              v-model="cvsform.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              placeholder="截止日期"
              v-model="cvsform.date2"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
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
              label="小程序名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="小程序模板"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="访问次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="长按识别次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="长按识别转化率"
              show-overflow-tooltip
            >
            </el-table-column>
          </el-table>
        </div>
      </template>
    </CmContainer>
  </div>
</template>

<script>
import CmContainer from "@/components/containner/CmContainer.vue";
export default {
  name: "Unicvs",
  created() {},
  data() {
    return {
      cvsform: {
        app: "",
        data1: "",
        data2: "",
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
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
