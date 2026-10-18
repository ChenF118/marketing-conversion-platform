<template>
  <div id="Grouptable">
    <div class="button">
      <el-button type="warning" size="small">批量设置</el-button>
      <el-button type="warning" size="small">批量修改分组</el-button>
      <el-button type="warning" size="small">批量设置数据上报</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="DeleteMany">
        <el-button
          type="warning"
          size="small"
          slot="reference"
          style="margin-left: 10px"
          >批量删除</el-button
        >
      </el-popconfirm>
      <el-button
        type="success"
        size="small"
        style="margin-left: 10px"
        @click="exportCsv"
        >导出CSV</el-button
      >
    </div>

    <div class="group-table">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        stripe
        :border="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column fixed prop="Group.group" label="所属分组" width="120">
        </el-table-column>
        <el-table-column prop="url" label="落地页url" width="250">
        </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip>
        </el-table-column>

        <!-- 以下未添加暂时静态 -->
        <el-table-column
          prop="address"
          label="微信在线管理"
          show-overflow-tooltip
          width="110"
        >
          <el-switch
            value="false"
            active-text="开启"
            inactive-text="关闭"
            class="switchClass"
          >
          </el-switch>
        </el-table-column>
        <el-table-column prop="address" label="复制统计" show-overflow-tooltip>
          <el-switch
            value="false"
            active-text="开启"
            inactive-text="关闭"
            class="switchClass"
          >
          </el-switch>
        </el-table-column>
        <el-table-column
          prop="address"
          label="行为转化统计"
          show-overflow-tooltip
          width="110"
        >
          <el-switch
            value="false"
            active-text="开启"
            inactive-text="关闭"
            class="switchClass"
          >
          </el-switch>
        </el-table-column>
        <el-table-column
          prop="address"
          label="网页状态监控"
          show-overflow-tooltip
          width="110"
        >
          <el-switch
            value="false"
            active-text="开启"
            inactive-text="关闭"
            class="switchClass"
          >
          </el-switch>
        </el-table-column>

        <el-table-column fixed="right" label="是否启用" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enable"
              active-text="启用"
              inactive-text="关闭"
              class="switchClass"
              @change="toggleenable(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="390">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >
            <el-button size="mini" @click="handleCopy(scope.$index, scope.row)"
              >复制</el-button
            >
            <el-button
              size="mini"
              @click="handleReport(scope.$index, scope.row)"
              >数据上报</el-button
            >
            <el-button
              size="mini"
              type="primary"
              @click="showSnippet(scope.row)"
              >接入代码</el-button
            >

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

    <div class="page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalcount"
      >
      </el-pagination>
    </div>

    <div class="unshowdialog">
      <Dialog
        formtitle="编辑落地页"
        :openfromfather="isopen"
        @alreadyclose="isopen = false"
      >
        <template>编辑</template>
        <template slot="currentForm">
          <div class="half">
            <div class="form_content">
              <el-form
                ref="editform"
                :model="editform"
                :rules="addrules"
                label-width="80px"
              >
                <el-form-item label="落地页url" prop="url" required>
                  <el-input
                    type="textarea"
                    v-model="editform.url"
                    placeholder="请输入落地页链接"
                  ></el-input>
                </el-form-item>

                <el-form-item label="选择分组">
                  <el-select
                    clearable
                    v-model="editform.group"
                    placeholder="不设置"
                  >
                    <el-option
                      v-for="item in selectlist"
                      :key="item.id"
                      :label="item.group"
                      :value="item.group"
                    ></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="备注">
                  <el-input
                    v-model="editform.remarks"
                    placeholder="简单的页面备注，便于查看统计"
                  ></el-input>
                </el-form-item>

                <el-form-item label="二跳链接">
                  <el-switch v-model="editform.delivery"></el-switch>
                </el-form-item>

                <el-form-item label="追踪url">
                  <el-switch v-model="editform.track"></el-switch>
                </el-form-item>

                <el-form-item label="是否启用">
                  <el-switch
                    v-model="editform.enable"
                    active-text="启用"
                    inactive-text="不启用"
                  >
                  </el-switch>
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" @click="closedialog"
                    >取 消</el-button
                  >
                  <el-button type="primary" @click="onSubmit('editform')"
                    >提 交</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
            <div class="tell_content">
              <p>
                ( 建议从浏览器落地页地址栏复制到此位置，避免手动输入错误
                支持批量填写，一行一条)
              </p>
              <p>(可以给链接选择一个分组，便于按分组查看分析数据)</p>
              <p>
                (启用后才能使用，未启用只保存此页面相关配置信息，不能使用功能！)
              </p>
            </div>
          </div>
        </template>
      </Dialog>
    </div>
    <el-dialog title="接入代码" :visible.sync="snippetVisible" width="680px">
      <p style="margin: 0 0 8px; font-size: 13px; color: #646a73">
        把下面这段代码放到该落地页的 HTML 中（建议放在 &lt;/body&gt; 前）：
      </p>
      <el-input type="textarea" :rows="5" :value="snippet" readonly></el-input>
      <div
        style="margin-top: 12px; display: flex; gap: 8px; align-items: center"
      >
        <el-button type="primary" size="small" @click="copySnippet"
          >复制代码</el-button
        >
        <span style="font-size: 12px; color: #646a73">预览：</span>
        <a :href="previewUrl" target="_blank" style="font-size: 12px">{{
          previewUrl
        }}</a>
      </div>
      <p
        style="
          margin-top: 14px;
          font-size: 12px;
          color: #8a5a00;
          background: #fff7e8;
          border: 1px solid #ffe4ba;
          padding: 8px 10px;
          border-radius: 8px;
        "
      >
        提示：接入前请确认该落地页已「绑定微信」并在「统计链接管理」中创建了统计链接。
      </p>
    </el-dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import store from "@/store/index";
import Dialog from "@/components/Dialog.vue";
// 验证规则
import { validateUrl } from "@/utils/validate";
import {
  getLandingApi,
  delsingleApi,
  dellotApi,
  updatelandingApi,
  tglandingApi,
  exportLandingCsvApi,
} from "@/api/landing";
export default {
  name: "Grouptable",
  created() {
    this.getlanding();
    this.selectlist = store.state.grouplist;
    Bus.$on("refreshtable", () => {
      this.getlanding();
    });
    Bus.$on("landingfilter", (searchform) => {
      this.filterform = searchform;
      this.getlanding();
    });
  },
  data() {
    return {
      tableData: [],
      snippetVisible: false,
      snippet: "",
      previewUrl: "",
      //   分页相关
      currentPage: 1,
      singlepage: 10,
      totalcount: 0,
      multipleSelection: [],
      // 编辑落地页
      isopen: false,
      editform: {},
      addrules: {
        url: [
          { required: true, message: "请输入落地页url", trigger: "blur" },
          { validator: validateUrl, trigger: "blur" },
        ],
      },
      filterform: {
        group: "",
        enable: "",
        words: "",
      },
      // 分组列表
      selectlist: [],
    };
  },
  methods: {
    showSnippet(row) {
      const api =
        window.location.protocol + "//" + window.location.hostname + ":8000";
      this.snippet =
        '<script src="' +
        api +
        '/sdk/landing.js" data-url="' +
        row.url +
        '" data-api="' +
        api +
        '">' +
        "</" +
        "script>";
      this.previewUrl =
        api + "/demo/landing.html?url=" + encodeURIComponent(row.url);
      this.snippetVisible = true;
    },
    copySnippet() {
      if (navigator.clipboard) navigator.clipboard.writeText(this.snippet);
      this.$message.success("接入代码已复制");
    },
    async exportCsv() {
      const res = await exportLandingCsvApi();
      const blob = new Blob([res.data], { type: "text/csv;charset=utf-8" });
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = "landing.csv";
      a.click();
      URL.revokeObjectURL(a.href);
    },

    handleSelectionChange(val) {
      this.multipleSelection = val.map((item) => {
        return item.id;
      });
    },
    handleEdit(index, row) {
      this.editform = row;
      this.isopen = true;
    },
    handleCopy(index, row) {
      console.log(index, row);
      console.log(row.id);
    },
    handleReport(index, row) {
      console.log(index, row);
      console.log(row.id);
    },
    handleDelete(index, row) {
      this.deletesingle(row.id);
    },
    DeleteMany() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选择任何数据",
          type: "warning",
        });
      } else {
        this.deletelot(this.multipleSelection);
      }
    },
    handleSizeChange(val) {
      this.singlepage = val;
      this.getlanding();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getlanding();
    },
    // api
    async getlanding() {
      const { data: res } = await getLandingApi(
        this.currentPage,
        this.singlepage,
        this.filterform
      );
      this.tableData = res.data;
      this.totalcount = res.count;
    },
    async deletesingle(val) {
      const { data: res } = await delsingleApi(val);
      if (res.code == 200) {
        this.$notify.info({
          title: "确认",
          message: "已删除本条落地页",
        });
        this.getlanding();
      }
    },
    async deletelot(val) {
      const { data: res } = await dellotApi(val);
      if (res.code == 200) {
        this.$notify.info({
          title: "确认",
          message: "已删除落地页",
        });
        this.getlanding();
      }
    },
    async updatelanding() {
      const { data: res } = await updatelandingApi(this.editform);
      if (res.code == 200) {
        Bus.$emit("closedialog");
        this.getlanding();
        this.$message({
          message: "修改成功",
          type: "success",
        });
      } else {
        return this.$message.error(res.message);
      }
    },
    async enabletoggle(val) {
      await tglandingApi(val.id, val.enable);
    },
    // 表单相关
    // 表单验证
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.updatelanding();
        } else {
          this.$message.error("提交失败");
          return false;
        }
      });
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },
    // 启用状态修改
    toggleenable(row) {
      this.enabletoggle(row);
    },
  },
  components: {
    Dialog,
  },
};
</script>

<style lang="scss" scoped>
#Grouptable {
  background-color: #fafafa;
  border: 1px solid #eee;
  .button {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 10px 0;
  }

  .page {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  // 编辑的dialog
  .unshowdialog {
    position: absolute;
    right: -999px;
  }
  .half {
    display: flex;
  }
  .form_content {
    width: 50%;
  }
  .tell_content {
    flex: 1;
    font-size: 12px;
    padding: 20px 0;

    p {
      margin-bottom: 50px;
    }
  }
}
</style>
<style>
/* 修改开关样式 */
/* switch按钮样式 */
.switchClass .el-switch__label {
  position: absolute;
  display: none;
  color: #fff !important;
}
/*打开时文字位置设置*/
.switchClass .el-switch__label--right {
  z-index: 1;
}
/* 调整打开时文字的显示位子 */
.switchClass .el-switch__label--right span {
  margin-left: 3px;
  margin-right: 10px;
  font-size: 12px;
}
/*关闭时文字位置设置*/
.switchClass .el-switch__label--left {
  z-index: 1;
}
/* 调整关闭时文字的显示位子 */
.switchClass .el-switch__label--left span {
  margin-left: 17px;
  font-size: 12px;
}
/*显示文字*/
.switchClass .el-switch__label.is-active {
  display: block;
}
/* 调整按钮的宽度 */
.switchClass.el-switch .el-switch__core,
.el-switch .el-switch__label {
  width: 50px !important;
  margin: 0;
}
/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
</style>
