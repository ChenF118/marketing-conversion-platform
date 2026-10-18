<template>
  <div id="CvsRate">
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

        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择分组"
            :popper-append-to-body="false"
          >
            <el-option
              v-for="item in grouplist"
              :key="item.id"
              :label="item.group"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="">
          <el-select
            clearable
            v-model="form.cvstype"
            placeholder="选择转化类型"
            :popper-append-to-body="false"
          >
            <el-option label="点击" :value="1"></el-option>
            <el-option label="长按" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.words" placeholder="输入url搜索"></el-input>
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
              prop="Landing.neardate"
              label="日期"
              width="120"
              show-overflow-tooltip=""
            >
            </el-table-column>

            <el-table-column
              fixed
              prop="Landing.Group.group"
              label="落地页分组"
              width="100"
            >
            </el-table-column>

            <el-table-column label="落地页url" prop="Landing.url" width="250">
            </el-table-column>

            <el-table-column prop="cvstype" label="转化类型"> </el-table-column>

            <el-table-column
              prop="Landing.cvscount"
              label="转化点击量"
              width="100"
            >
            </el-table-column>

            <el-table-column prop="Landing.total" label="总访问量">
            </el-table-column>

            <el-table-column label="转化率">
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
import { getcvsrateApi } from "@/api/conversion";
export default {
  name: "CvsRate",
  created() {
    this.landinglist = store.state.landinglist;
    this.grouplist = store.state.grouplist;
    this.getcvsrate();
  },
  data() {
    return {
      form: {
        url: "",
        group: "",
        date1: "",
        date2: "",
        cvstype: "",
        words: "",
      },
      landinglist: [],
      grouplist: [],
      // 表格数据
      totalcount: 0,
      tableData: [],
      // 关于分页
      currentPage: 1,
      pageSize: 10,
    };
  },
  methods: {
    // 子组件传来的分页数据
    changeSize(val) {
      this.pageSize = val;
      this.getcvsrate();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.getcvsrate();
    },

    // 筛选搜索按钮
    filtersearch() {
      this.getcvsrate();
    },

    pickdate(val) {
      this.form.date1 = val[0];
      this.form.date2 = val[1];
      this.getcvsrate();
    },

    rate({ Landing }) {
      if (Landing.cvscount == 0) {
        return 0 + "%";
      } else {
        const rate =
          ((Landing.cvscount / Landing.total) * 100).toFixed(2) + "%";
        return rate;
      }
    },

    // Api
    async getcvsrate() {
      const { data: res } = await getcvsrateApi(
        this.currentPage,
        this.pageSize,
        this.form
      );
      this.totalcount = res.count;
      this.tableData = res.data;
    },
  },
  components: {
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
