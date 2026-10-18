<template>
  <div id="CvsSync">
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
              v-for="item in selectlist"
              :key="item.id"
              :label="item.group"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- 转化类型 -->
        <el-form-item label="">
          <el-select
            clearable
            disabled
            v-model="form.class"
            placeholder="选择转化类型"
            :popper-append-to-body="false"
          >
            <el-option label="微信复制" value="shanghai"></el-option>
            <el-option label="微信打开" value="shanghai"></el-option>
            <el-option label="咨询" value="shanghai"></el-option>
            <el-option label="表单确认提交" value="shanghai"></el-option>
            <el-option label="下载" value="shanghai"></el-option>
            <el-option label="购买" value="shanghai"></el-option>
            <el-option label="注册" value="shanghai"></el-option>
            <el-option label="登录" value="shanghai"></el-option>
            <el-option label="长按" value="shanghai"></el-option>
            <el-option label="拨打电话" value="shanghai"></el-option>
            <el-option label="短信" value="shanghai"></el-option>
            <el-option label="加入购物车" value="shanghai"></el-option>
            <el-option label="地图" value="shanghai"></el-option>
            <el-option label="QQ咨询" value="shanghai"></el-option>
            <el-option label="抽奖" value="shanghai"></el-option>
            <el-option label="投票" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 排序 -->
        <el-form-item label="">
          <el-select
            clearable
            disabled
            v-model="form.order"
            placeholder="排序"
            :popper-append-to-body="false"
          >
            <el-option label="转化类型" value="shanghai"></el-option>
            <el-option label="已到粉标记" value="shanghai"></el-option>
            <el-option label="来源关键词" value="shanghai"></el-option>
            <el-option label="来源搜索引擎" value="shanghai"></el-option>
            <el-option label="地区" value="shanghai"></el-option>
            <el-option label="访问设备" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 来源 -->
        <el-form-item label="">
          <el-select
            clearable
            disabled
            v-model="form.source"
            placeholder="选择来源"
            :popper-append-to-body="false"
          >
            <el-option label="所有" value="shanghai"></el-option>
            <el-option label="百度" value="shanghai"></el-option>
            <el-option label="360搜索" value="shanghai"></el-option>
            <el-option label="搜狗" value="shanghai"></el-option>
            <el-option label="神马" value="shanghai"></el-option>
            <el-option label="头条" value="shanghai"></el-option>
            <el-option label="谷歌" value="shanghai"></el-option>
            <el-option label="凤凰网" value="shanghai"></el-option>
            <el-option label="其他" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 数据上报状态 -->
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.state"
            placeholder="数据上报状态"
            :popper-append-to-body="false"
          >
            <el-option disabled label="等待上传" value="shanghai"></el-option>
            <el-option label="转化成功" :value="1"></el-option>
            <el-option disabled label="转化失败" value="shanghai"></el-option>
            <el-option label="条件不足" :value="0"></el-option>
            <el-option disabled label="无效转化" value="shanghai"></el-option>
            <el-option disabled label="深度转化" value="shanghai"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-input v-model="form.words" placeholder="搜索url关键词"></el-input>
        </el-form-item>
      </template>
      <!-- 表格区 -->
      <template slot="tables">
        <div class="Ctable">
          <div class="tablehead">
            <el-button type="success" size="small">百度上报</el-button>
            <el-button type="success" size="small">头条上报</el-button>
            <el-button type="success" size="small">头条上报</el-button>
            <el-button type="success" size="small">360上报</el-button>
            <el-button type="success" size="small">搜狗上报</el-button>
            <el-button type="success" size="small">腾讯上报</el-button>
            <el-button type="success" size="small">快手上报</el-button>
          </div>
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
            <el-table-column type="selection" width="50" align="center">
            </el-table-column>
            <el-table-column
              prop="address"
              label="账号"
              show-overflow-tooltip
              v-if="checkbox.zhanghao"
            >
            </el-table-column>
            <el-table-column
              prop="Landing.Group.group"
              label="落地页分组"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.luodiye"
            >
            </el-table-column>
            <el-table-column
              prop="Landing.url"
              label="访问url"
              show-overflow-tooltip
              width="150"
              v-if="checkbox.url"
            >
            </el-table-column>
            <el-table-column
              prop="Landing.remarks"
              label="备注"
              show-overflow-tooltip
              v-if="checkbox.beizhu"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="计划"
              show-overflow-tooltip
              v-if="checkbox.jihua"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="单元"
              show-overflow-tooltip
              v-if="checkbox.danyuan"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="追踪关键词"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.zhuizong"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="展现排名"
              show-overflow-tooltip
              v-if="checkbox.paiming"
            >
            </el-table-column>
            <el-table-column label="数据上报" v-if="checkbox.shangbao">
              <template slot-scope="scope">
                {{ scope.row.report == 0 ? "条件不足" : "上报成功" }}
              </template>
            </el-table-column>
            <el-table-column
              prop="cvsdate"
              label="转化时间"
              show-overflow-tooltip
              width="150"
              v-if="checkbox.shijian"
            >
            </el-table-column>
            <el-table-column
              prop="cvstime"
              label="转化时长"
              show-overflow-tooltip
              v-if="checkbox.shichang"
            >
            </el-table-column>
            <el-table-column
              prop="type"
              label="转化类型"
              show-overflow-tooltip
              v-if="checkbox.leixing"
            >
              <!--  <template slot-scope="scope">
                {{ scope.row.type == 1 ? "点击" : "长按" }}
              </template> -->
            </el-table-column>
            <el-table-column
              prop="target"
              label="转化目标"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.leixing"
            >
            </el-table-column>
            <el-table-column
              prop="city.cip"
              label="ip"
              show-overflow-tooltip
              v-if="checkbox.ip"
            >
            </el-table-column>
            <el-table-column
              prop="city.cname"
              label="地区"
              show-overflow-tooltip
              v-if="checkbox.diqu"
            >
            </el-table-column>
            <el-table-column
              prop="equipment"
              label="访问设备"
              show-overflow-tooltip
              v-if="checkbox.shebei"
            >
            </el-table-column>
            <el-table-column
              prop="stay"
              label="浏览时长"
              show-overflow-tooltip
              v-if="checkbox.liulan"
            >
            </el-table-column>
            <el-table-column
              prop="rate"
              label="访问比例"
              show-overflow-tooltip
              v-if="checkbox.bili"
            >
            </el-table-column>
            <el-table-column
              prop="wx"
              label="显示微信号"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.xianshi"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="搜索词"
              show-overflow-tooltip
              v-if="checkbox.sousuo"
            >
            </el-table-column>
            <el-table-column
              prop="search"
              label="搜索引擎"
              show-overflow-tooltip
              v-if="checkbox.yinqing"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="来源链接"
              show-overflow-tooltip
              v-if="checkbox.laiyuan"
            >
            </el-table-column>
          </el-table>

          <!-- 动态增减表头 -->
          <div class="dynamic">
            <TableSwitch @changetableitem="changebox"></TableSwitch>
          </div>
        </div>
      </template>
    </CmContainer>
  </div>
</template>

<script>
import CmContainer from "@/components/containner/CmContainer.vue";
import TableSwitch from "@/components/utils/switch.vue";
import { getmonitorApi } from "@/api/conversion";
import store from "@/store/index";
export default {
  name: "CvsSync",
  created() {
    this.getmonitor();
    this.selectlist = store.state.grouplist;
    this.landinglist = store.state.landinglist;
  },
  data() {
    return {
      // 搜索表单
      form: {
        url: "",
        group: "",
        date1: "",
        date2: "",
        class: "",
        order: "",
        source: "",
        state: "",
        words: "",
      },
      // 表格数据
      tableData: [],
      totalcount: 0,
      // 分组
      selectlist: [],
      landinglist: [],

      // 关于分页
      currentPage: 1,
      pageSize: 10,
      checkbox: {
        zhanghao: false,
        luodiye: true,
        url: true,
        beizhu: true,
        jihua: false,
        danyuan: false,
        zhuizong: false,
        paiming: false,
        shangbao: true,
        shijian: true,
        shichang: true,
        leixing: true,
        mubiao: true,
        ip: true,
        diqu: true,
        shebei: true,
        liulan: true,
        bili: true,
        xianshi: true,
        sousuo: false,
        yinqing: true,
        laiyuan: true,
      },
    };
  },
  methods: {
    // 表格的方法
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    // 子组件传来的分页数据
    changeSize(val) {
      this.pageSize = val;
      this.getmonitor();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.getmonitor();
    },

    changebox(val) {
      this.checkbox = val;
    },

    filtersearch() {
      this.getmonitor();
    },

    // 快捷修改日期
    pickdate(val) {
      this.form.date1 = val[0];
      this.form.date2 = val[1];
    },

    // Api
    async getmonitor() {
      /* const data = await getmonitorApi(
        this.currentPage,
        this.pageSize,
        this.form
      );
      console.log(data); */
      const { data: res } = await getmonitorApi(
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
    TableSwitch,
  },
};
</script>

<style lang="scss" scoped>
.Ctable {
  position: relative;

  .tablehead {
    padding: 10px;
    display: flex;
    justify-content: flex-start;
  }

  .dynamic {
    position: absolute;
    right: 10px;
    top: -35px;
  }
}
.el-icon-arrow-down {
  font-size: 12px;
}
.demonstration {
  display: block;
  color: #8492a6;
  font-size: 14px;
  margin-bottom: 20px;
}
</style>
<style>
/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
</style>
