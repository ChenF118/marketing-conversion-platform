<template>
  <div id="CvsChat">
    <CmContainer
      :count="totalcount"
      @sendSize="changeSize"
      @sendCurrent="changeCurrent"
      @filtersearch="filtersearch"
      @quickpick="pickdate"
    >
      <!-- 搜索区 -->
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.url"
            placeholder="选择url查询"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in landinglist"
              :key="item.id"
              :label="item.url"
              :value="item.id"
            ></el-option>
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
            empty-text="无数据"
          >
            <el-table-column
              prop="wxh"
              label="显示微信号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="copy" label="转化次数" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="report"
              label="数据上报成功"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="visits"
              label="总访问量"
              show-overflow-tooltip
            >
            </el-table-column>

            <el-table-column prop="" label="转化率" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ rate(scope.row) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </CmContainer>
  </div>
</template>

<script>
import store from "@/store/index";
import CmContainer from "@/components/containner/CmContainer.vue";
import { getwxApi } from "@/api/wxmanage";
export default {
  name: "CvsChat",
  created() {
    this.landinglist = store.state.landinglist;
    this.getWx();
  },
  data() {
    return {
      form: {
        url: "",
        date1: "",
        date2: "",
        words: "",
      },
      tableData: [],
      totalcount: 0,
      landinglist: [],
      currentPage: 1,
      pageSize: 10,
    };
  },
  methods: {
    changeSize(val) {
      this.pageSize = val;
      this.getWx();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.getWx();
    },

    // 筛选搜索按钮
    filtersearch() {
      this.getWx();
    },

    pickdate(val) {
      this.form.date1 = val[0];
      this.form.date2 = val[1];
    },

    rate(val) {
      const rate = ((val.copy / val.visits) * 100).toFixed(2);
      if (!isNaN(rate)) {
        return `${rate}%`;
      }
      return "暂无数据";
    },

    // Api
    async getWx() {
      const { data: res } = await getwxApi(
        this.currentPage,
        this.pageSize,
        this.form
      );
      this.tableData = res.data;
      this.totalcount = res.count;
    },
  },
  components: {
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
