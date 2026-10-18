<template>
  <div id="CopyNumber">
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
            placeholder="选择微信分组"
            :popper-append-to-body="false"
          >
            <el-option label="选择微信分组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="" style="width: 100px">
          <el-select
            clearable
            v-model="form.order"
            placeholder="排序"
            :popper-append-to-body="false"
          >
            <el-option label="复制次数" value="shanghai"></el-option>
            <el-option label="打开次数" value="shanghai"></el-option>
            <el-option label="访问量" value="shanghai"></el-option>
            <el-option label="转化率" value="shanghai"></el-option>
            <el-option label="到粉数" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.words" placeholder="搜索微信号"></el-input>
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
              label="微信号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="复制次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="数据上报成功"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="到粉数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="打开微信次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="复制到粉率"
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
  name: "CopyNumber",
  created() {},
  data() {
    return {
      form: {
        group: "",
        date1: "",
        date2: "",
        order: "",
        words: "",
        url: "",
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
