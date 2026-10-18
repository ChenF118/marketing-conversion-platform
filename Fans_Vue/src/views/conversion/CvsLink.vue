<template>
  <div id="CvsLink">
    <VchatContainer
      :count="totalcount"
      @sendSize="changeSize"
      @sendCurrent="changeCurrent"
      @filtersearch="filtersearch"
    >
      <!-- 搜索区 -->
      <template>
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

        <el-form-item>
          <el-input v-model="form.words" placeholder="输入url查询"></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="vdialog">
        <AddCvs
          dialogtitle="添加统计转化类型"
          :cvstypelist="cvstypelist"
          @onsubmit="submitform"
        ></AddCvs>
      </template>

      <!-- 表格区 -->
      <template slot="tables">
        <div class="cvstable">
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
            empty-text="请添加落地页链接！"
          >
            <el-table-column
              fixed
              prop="landing.Group.group"
              label="落地页分组"
              width="100"
            >
            </el-table-column>
            <el-table-column label="落地页url" width="250">
              <template slot-scope="scope">{{
                scope.row.landing.url
              }}</template>
            </el-table-column>
            <el-table-column label="转化类型" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ whichtype(scope.row.type) }}
              </template>
            </el-table-column>
            <el-table-column prop="cvscount" label="计数方式" width="120">
              <template slot-scope="scope">
                {{
                  scope.row.cvscount == 0 ? "点击多次计多次" : "点击多次计一次"
                }}
              </template>
            </el-table-column>
            <el-table-column
              prop="cvsmode"
              label="统计类型"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="cvstype"
              label="统计方式"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="code"
              label="埋点代码"
              show-overflow-tooltip
              width="130"
            >
            </el-table-column>
            <el-table-column
              prop="message"
              label="模糊匹配信息"
              show-overflow-tooltip
              width="120"
            >
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="250">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >编辑
                </el-button>
                <el-button
                  size="mini"
                  @click="handleSet(scope.$index, scope.row)"
                  >设置
                </el-button>
                <el-popconfirm
                  title="确定删除这条吗？"
                  @confirm="handleDelete(scope.$index, scope.row)"
                >
                  <el-button
                    size="mini"
                    type="danger"
                    slot="reference"
                    style="margin-left: 10px"
                    >删除</el-button
                  >
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </VchatContainer>

    <!-- 编辑调起的表单 -->
    <div class="editform">
      <unshowdialog
        :isopenin="isopened"
        :formfather="fatherform"
        :cvstypelist="cvstypelist"
        @closer="isopened = false"
        @editedform="toeditform"
      ></unshowdialog>
      <my-setting></my-setting>
    </div>
  </div>
</template>

<script>
import store from "@/store/index";
import Bus from "@/utils/event";
import VchatContainer from "@/views/vchat/VchatContainer.vue";
import AddCvs from "@/components/button/AddCvs.vue";
import unshowdialog from "@/views/conversion/unshowdialog.vue";
import MySetting from "@/views/conversion/setting.vue";
import {
  appendcvslinkApi,
  deletecvslinkApi,
  updatecvslinkApi,
  getcvslinkApi,
  cvstypelistApi,
} from "@/api/conversion";
export default {
  name: "CvsLink",
  created() {
    this.grouplist = store.state.grouplist;
    this.getcvslink();
    this.typelist();
  },
  data() {
    return {
      form: {
        group: "",
        words: "",
      },
      // 表格数据
      tableData: [],
      totalcount: 0,
      // 分组
      grouplist: [],
      // 关于分页
      currentPage: 1,
      pageSize: 10,
      isopened: false,
      // 准备传给子组件的数据
      fatherform: [],
      setform: {
        id: "",
        landings: "",
        second: 0,
      },
      cvstypelist: [],
    };
  },
  methods: {
    changeSize(val) {
      this.pageSize = val;
      this.getcvslink();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.getcvslink();
    },

    // 筛选搜索按钮
    filtersearch() {
      this.getcvslink();
    },

    submitform(val) {
      this.addcsslink(val);
    },

    handleEdit(index, row) {
      this.fatherform = row;
      this.isopened = true;
    },
    // 编辑提交传来
    toeditform(form) {
      this.updatecvslink(form);
    },

    handleSet(index, row) {
      this.setform.landings = row.landing.url;
      this.setform.id = row.id;
      this.setform.second = row.second;
      Bus.$emit("tosetsecond", this.setform);
    },
    // 单删
    handleDelete(index, row) {
      this.delcvslink(row.id);
    },

    // Api
    async getcvslink() {
      const { data: res } = await getcvslinkApi(
        this.currentPage,
        this.pageSize,
        this.form
      );
      this.tableData = res.data;
      this.totalcount = res.count;
    },
    // add
    async addcsslink(value) {
      const { data: res } = await appendcvslinkApi(value);
      if (res.code == 200) {
        this.$message({
          message: "添加成功",
          type: "success",
        });
        this.getcvslink();
      } else if (res.code == 201) {
        this.$message({
          message: res.message,
          type: "warning",
        });
      } else {
        return this.$message.error("添加出错！");
      }
    },
    // del
    async delcvslink(ids) {
      const { data: res } = await deletecvslinkApi(ids);
      if (res.code == 200) {
        this.$notify.info({
          title: "确认",
          message: "已删除该统计链接",
        });
        this.getcvslink();
      }
    },
    // update
    async updatecvslink(val) {
      const { data: res } = await updatecvslinkApi(val);
      if (res.code == 200) {
        this.$message({
          message: "修改成功",
          type: "success",
        });
        this.getcvslink();
      } else {
        return this.$message.error("修改失败！");
      }
    },

    async typelist() {
      const { data: res } = await cvstypelistApi();
      this.cvstypelist = res.data;
    },

    whichtype(val) {
      let result = "";
      switch (val) {
        case 1:
          result = "点击";
          break;
        case 2:
          result = "长按识别";
          break;
        case 3:
          result = "微信复制按钮点击";
          break;
        case 4:
          result = "微信打开按钮点击";
          break;
        case 5:
          result = "咨询按钮点击";
          break;
        case 6:
          result = "下载按钮点击";
          break;
        case 7:
          result = "购买按钮点击";
          break;
        case 8:
          result = "注册按钮点击";
          break;
        case 9:
          result = "登录按钮点击";
          break;
        case 10:
          result = "拨打电话按钮点击";
          break;
        case 11:
          result = "短信按钮点击";
          break;
        case 12:
          result = "加入购物车按钮点击";
          break;
        case 13:
          result = "地图按钮点击";
          break;
        case 14:
          result = "QQ咨询按钮点击";
          break;
        case 15:
          result = "抽奖按钮点击";
          break;
        case 16:
          result = "投票按钮点击";
          break;
        case 17:
          result = "表单确认提交按钮点击";
          break;
        default:
          result = "";
          break;
      }
      return result;
    },
  },
  components: {
    VchatContainer,
    AddCvs,
    unshowdialog,
    MySetting,
  },
};
</script>

<style lang="scss" scoped>
#CvsLink {
  background-color: #fff;
  padding-bottom: 20px;

  .editform {
    display: inline-block;
    position: absolute;
    right: -999px;
  }
}
</style>
