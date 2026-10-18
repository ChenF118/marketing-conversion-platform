<template>
  <div id="Sync">
    <CmContainer>
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="sync.app"
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
              v-model="sync.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              placeholder="截止日期"
              v-model="sync.date2"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="sync.sort"
            placeholder="筛选"
            :popper-append-to-body="false"
          >
            <el-option label="只看有长按识别" value="shanghai"></el-option>
            <el-option label="只看有关联访客" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="sync.words"
            placeholder="搜索小程序名称"
          ></el-input>
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
              label="访问时间"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="address" label="ip" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="address" label="地区" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="address"
              label="二维码分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="显示二维码"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="是否长按识别"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="关联访客"
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
  name: "Sync",
  created() {},
  data() {
    return {
      sync: {
        app: "",
        data1: "",
        data2: "",
        sort: "",
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
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
