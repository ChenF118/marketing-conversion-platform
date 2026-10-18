<template>
  <div id="Detail">
    <CmContainer>
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.app"
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
              v-model="detail.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              placeholder="截止日期"
              v-model="detail.date2"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.landing"
            placeholder="选择落地页分组"
            :popper-append-to-body="false"
          >
            <el-option label="选择落地页分组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.url"
            placeholder="选择url查看"
            :popper-append-to-body="false"
          >
            <el-option label="选择url查看" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.state"
            placeholder="数据上报状态"
            :popper-append-to-body="false"
          >
            <el-option label="等待上传" value="shanghai"></el-option>
            <el-option label="转化成功" value="shanghai"></el-option>
            <el-option label="转化失败" value="shanghai"></el-option>
            <el-option label="条件不足" value="shanghai"></el-option>
            <el-option label="无效转化" value="shanghai"></el-option>
            <el-option label="深度转化" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.fans"
            placeholder="是否到粉"
            :popper-append-to-body="false"
          >
            <el-option label="已到粉" value="shanghai"></el-option>
            <el-option label="已关注" value="shanghai"></el-option>
            <el-option label="否" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.source"
            placeholder="选择来源"
            :popper-append-to-body="false"
          >
            <el-option label="所有" value="shanghai"></el-option>
            <el-option label="百度" value="shanghai"></el-option>
            <el-option label="360" value="shanghai"></el-option>
            <el-option label="搜狗" value="shanghai"></el-option>
            <el-option label="神马" value="shanghai"></el-option>
            <el-option label="头条" value="shanghai"></el-option>
            <el-option label="谷歌" value="shanghai"></el-option>
            <el-option label="凤凰网" value="shanghai"></el-option>
            <el-option label="其他" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="detail.state"
            placeholder="是否到粉"
            :popper-append-to-body="false"
          >
            <el-option label="已到粉" value="shanghai"></el-option>
            <el-option label="已关注" value="shanghai"></el-option>
            <el-option label="否" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="detail.words"
            placeholder="搜索url/关键词/微信号"
          ></el-input>
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
            <el-table-column type="selection" width="55" align="center">
            </el-table-column>
            <el-table-column
              prop="address"
              label="账号"
              show-overflow-tooltip
              v-if="checkbox.zhanghao"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="落地页分组"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.luodiye"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="访问url"
              show-overflow-tooltip
              v-if="checkbox.url"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="小程序名称"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.uniapp"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="小程序模板"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.unimuban"
            >
            </el-table-column>
            <el-table-column
              prop="address"
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
            <el-table-column
              prop="address"
              label="数据上报"
              show-overflow-tooltip
              v-if="checkbox.shangbao"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="二维码分组"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.erweima"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="显示二维码"
              show-overflow-tooltip
              width="100"
              v-if="checkbox.xserweima"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="长按识别时间"
              show-overflow-tooltip
              width="110"
              v-if="checkbox.changan"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="真实到粉"
              show-overflow-tooltip
              v-if="checkbox.zhenshi"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="真实到粉微信"
              show-overflow-tooltip
              width="110"
              v-if="checkbox.zsdfwx"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="真实到粉时间"
              show-overflow-tooltip
              width="110"
              v-if="checkbox.zsdfsj"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="ip"
              show-overflow-tooltip
              v-if="checkbox.ip"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="地区"
              show-overflow-tooltip
              v-if="checkbox.diqu"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="访问设备"
              show-overflow-tooltip
              v-if="checkbox.shebei"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="转化时长"
              show-overflow-tooltip
              v-if="checkbox.liulan"
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="访问比例"
              show-overflow-tooltip
              v-if="checkbox.bili"
            >
            </el-table-column>
            <el-table-column
              prop="address"
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
              prop="address"
              label="搜索引擎"
              show-overflow-tooltip
              v-if="checkbox.yinqing"
            >
            </el-table-column>
          </el-table>

          <!-- 动态增减表头 -->
          <div class="dynamic">
            <el-dropdown trigger="click" :hide-on-click="false">
              <span class="el-dropdown-link">
                <i class="el-icon-setting" style="color: #409eff"></i>
              </span>

              <el-dropdown-menu slot="dropdown">
                <el-scrollbar style="height: 300px">
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.zhanghao">账号</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.luodiye"
                      >落地页分组</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.url">访问url</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.uniapp"
                      >小程序名称</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.unimuban"
                      >小程序模板</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.beizhu">备注</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.jihua">计划</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.danyuan">单元</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.zhuizong"
                      >追踪关键词</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.paiming"
                      >展现排名</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.shangbao"
                      >数据上报</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.erweima"
                      >二维码分组</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.xserweima"
                      >显示二维码</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.changan"
                      >长按识别时间</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.zhenshi"
                      >真实到粉</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.zsdfwx"
                      >真实到粉微信</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.zsdfsj"
                      >真实到粉时间</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.ip">ip</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.diqu">地区</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.shebei"
                      >访问设备</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.liulan"
                      >转化时长</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.bili">访问比例</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.xianshi"
                      >显示微信号</el-checkbox
                    >
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.sousuo">搜索词</el-checkbox>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-checkbox v-model="checkbox.yinqing"
                      >搜索引擎</el-checkbox
                    >
                  </el-dropdown-item>
                </el-scrollbar>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </template>
    </CmContainer>
  </div>
</template>

<script>
import CmContainer from "@/components/containner/CmContainer.vue";
export default {
  name: "Detail",
  created() {},
  data() {
    return {
      detail: {
        app: "",
        landing: "",
        data1: "",
        data2: "",
        url: "",
        state: "",
        fans: "",
        source: "",
        words: "",
      },
      tableData: [],
      checkbox: {
        zhanghao: false,
        luodiye: true,
        url: true,
        uniapp: true,
        unimuban: true,
        beizhu: true,
        jihua: false,
        danyuan: false,
        zhuizong: false,
        paiming: false,
        shangbao: true,
        erweima: true,
        xserweima: true,
        changan: true,
        zhenshi: true,
        zsdfwx: true,
        zsdfsj: true,
        ip: true,
        diqu: true,
        shebei: true,
        liulan: true,
        bili: true,
        xianshi: true,
        sousuo: false,
        yinqing: true,
      },
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

    .el-icon-setting {
      font-size: 20px;
    }
  }
}
</style>
