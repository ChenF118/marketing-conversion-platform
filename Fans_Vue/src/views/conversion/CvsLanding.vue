<template>
  <div id="CvsLanding">
    <CmContainer>
      <!-- 搜索区 -->
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.url"
            placeholder="选择url查询"
            :popper-append-to-body="false"
          >
            <el-option label="选择url查询" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 默认时间选择 -->
        <el-form-item>
          <el-col :span="11">
            <el-date-picker
              type="date"
              placeholder="开始日期"
              v-model="form.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              placeholder="截止日期"
              v-model="form.date2"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
        </el-form-item>

        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择分组"
            :popper-append-to-body="false"
          >
            <el-option label="一组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input v-model="form.words" placeholder="搜索url"></el-input>
        </el-form-item>
      </template>
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
              label="落地页分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="落地页url"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="落地页备注"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="转化次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="总访问量"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="转化率"
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
  name: "CvsLanding",
  created() {},
  data() {
    return {
      form: {
        url: "",
        group: "",
        date1: "",
        date2: "",
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
  },
  components: {
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
