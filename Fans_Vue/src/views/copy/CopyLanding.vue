<template>
  <div id="Copylanding">
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
          <el-select clearable v-model="form.group" placeholder="选择分组">
            <el-option
              v-for="item in grouplist"
              :key="item.id"
              :label="item.group"
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
          <el-input v-model="form.url" placeholder="搜索url"></el-input>
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
            show-summary
            :summary-method="totalOutPrice"
            :border="true"
            :header-cell-style="{
              background: '#fafafa',
            }"
            empty-text="无数据"
          >
            <el-table-column
              fixed
              prop="Group.group"
              label="落地页分组"
              width="100"
            >
            </el-table-column>
            <el-table-column prop="url" label="落地页url" width="250">
            </el-table-column>
            <el-table-column
              prop="remarks"
              label="落地页备注"
              show-overflow-tooltip
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="cvscount"
              label="复制次数"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="fans" label="到粉数" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="cvscount"
              label="打开微信次数"
              show-overflow-tooltip
              width="120"
            >
            </el-table-column>
            <el-table-column prop="total" label="访问量" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              label="复制转化率"
              show-overflow-tooltip
              width="100"
            >
              <template slot-scope="scope">
                {{ copyrate(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column
              label="复制到粉率"
              show-overflow-tooltip
              width="100"
            >
              <template slot-scope="scope">
                {{ copyfans(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column
              label="到粉转化率"
              show-overflow-tooltip
              width="100"
            >
              <template slot-scope="scope">
                {{ copyfans(scope.row) }}
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
import { LandingRankApi } from "@/api/landing";
export default {
  name: "Copylanding",
  created() {
    this.grouplist = store.state.grouplist;
    this.LandingRank();
  },
  data() {
    return {
      form: {
        group: "",
        date1: "",
        date2: "",
        order: "",
        url: "",
      },
      // 表格数据
      tableData: [],
      totalcount: 0,
      // 分组
      grouplist: [],
      // 关于分页
      currentPage: 1,
      pageSize: 10,
    };
  },
  methods: {
    // 子组件传来的分页数据
    changeSize(val) {
      this.pageSize = val;
      this.LandingRank();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.LandingRank();
    },

    // 筛选搜索按钮
    filtersearch() {
      this.LandingRank();
    },

    pickdate(val) {
      this.form.date1 = val[0];
      this.form.date2 = val[1];
      this.LandingRank();
    },

    copyrate(val) {
      if (val.total == 0) {
        return "0.00 %";
      }
      const rate = ((val.cvscount / val.total) * 100).toFixed(2) + " %";
      return rate;
    },
    copyfans(val) {
      if (val.cvscount == 0) {
        return "0.00 %";
      }
      const rate = ((val.fans / val.cvscount) * 100).toFixed(2) + " %";
      return rate;
    },

    //Api
    async LandingRank() {
      const { data: res } = await LandingRankApi(
        this.currentPage,
        this.pageSize,
        this.form
      );
      this.tableData = res.data;
      this.totalcount = res.count;
    },

    totalOutPrice(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        const values = data.map((item) => Number(item[column.property]));
        if (column.property != "remarks") {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          sums[index];
        }
      });
      return sums;
    },
  },
  components: {
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
