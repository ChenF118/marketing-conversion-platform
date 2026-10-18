<template>
  <div id="Addlanding">
    <Dialog :formtitle="dialogtitle">
      <template
        ><i class="el-icon-circle-plus-outline" ref="showdialog"></i
        >{{ dialogtitle }}</template
      >
      <template slot="currentForm">
        <div class="half">
          <div class="form_content">
            <el-form
              ref="form"
              :model="form"
              :rules="addrules"
              label-width="80px"
            >
              <el-form-item label="落地页url" prop="landingurl" required>
                <el-input
                  type="textarea"
                  :rows="6"
                  v-model="form.landingurl"
                  placeholder="请输入落地页链接，支持一行一个批量填写"
                ></el-input>
              </el-form-item>

              <el-form-item label="选择分组">
                <el-select
                  clearable
                  v-model="form.group"
                  placeholder="选择分组"
                >
                  <el-option
                    v-for="item in selectlist"
                    :key="item.id"
                    :label="item.group"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="备注">
                <el-input
                  v-model="form.remarks"
                  placeholder="简单的页面备注，便于查看统计"
                ></el-input>
              </el-form-item>

              <el-form-item label="二跳链接">
                <el-switch v-model="form.delivery"></el-switch>
              </el-form-item>

              <el-form-item label="追踪url">
                <el-switch v-model="form.track"></el-switch>
              </el-form-item>

              <el-form-item label="是否启用">
                <el-switch
                  v-model="form.open"
                  active-text="启用"
                  inactive-text="不启用"
                >
                </el-switch>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="closedialog">取 消</el-button>
                <el-button type="primary" @click="onSubmit('form')"
                  >提 交</el-button
                >
              </el-form-item>
            </el-form>
          </div>
          <div class="tell_content">
            <p>
              ( 支持批量：一行一个落地页链接，粘贴后提交会先预览 「可导入 / 重复
              / 格式错误」，确认后再写入 )
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
</template>

<script>
import Dialog from "@/components/Dialog.vue";
import { insertApi, importLandingApi } from "@/api/landing";
import Bus from "@/utils/event";
import store from "@/store/index";

// 与 utils/validate.js 的 validateUrl 保持一致
const URL_RE =
  /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w-]+)+[\w\-_~:/?#[\]@!$&',;=.]+$/;

export default {
  name: "Addlanding",
  props: ["dialogtitle"],
  created() {
    this.selectlist = store.state.grouplist;
  },
  data() {
    return {
      form: {
        landingurl: "",
        group: "",
        remarks: "",
        delivery: false,
        track: false,
        open: true,
      },
      addrules: {
        landingurl: [
          { required: true, message: "请输入落地页url", trigger: "blur" },
          { validator: this.checkUrls, trigger: "blur" },
        ],
      },
      selectlist: [],
    };
  },
  components: {
    Dialog,
  },
  methods: {
    splitLines(value) {
      return String(value || "")
        .split(/\r?\n/)
        .map((s) => s.trim())
        .filter(Boolean);
    },

    // 单条即时校验；多行交给服务端预览统一列出格式错误（避免逐行试错）
    checkUrls(rule, value, callback) {
      const lines = this.splitLines(value);
      if (!lines.length) return callback(new Error("请输入落地页url"));
      if (lines.length === 1 && !URL_RE.test(lines[0])) {
        return callback(new Error("请输入正确的url格式"));
      }
      callback();
    },

    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          this.$message.error("提交失败");
          return false;
        }
        if (this.splitLines(this.form.landingurl).length > 1) {
          this.batchImport();
        } else {
          this.addlanding();
        }
      });
    },

    closedialog() {
      Bus.$emit("closedialog");
    },

    escapeHtml(s) {
      return String(s)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
    },

    summaryHtml(d) {
      let html = `<p>共识别 ${d.total} 行：可导入 <b>${d.validCount}</b> 条，重复 <b>${d.duplicates.length}</b> 条，格式错误 <b>${d.invalid.length}</b> 条</p>`;
      if (d.invalid.length) {
        html += `<p>格式错误（不会导入）：</p><ul style="padding-left:18px">`;
        d.invalid.slice(0, 5).forEach((x) => {
          html += `<li>第 ${x.line} 行：${this.escapeHtml(x.url)}</li>`;
        });
        html += `</ul>`;
      }
      if (d.duplicates.length) {
        html += `<p>重复（将跳过）：</p><ul style="padding-left:18px">`;
        d.duplicates.slice(0, 5).forEach((x) => {
          html += `<li>第 ${x.line} 行：${this.escapeHtml(
            x.url
          )}（${this.escapeHtml(x.reason)}）</li>`;
        });
        html += `</ul>`;
      }
      return html;
    },

    async batchImport() {
      const preview = await importLandingApi(this.form, true);
      const res = preview.data;
      if (res.code !== 200) {
        return this.$message.error(res.message);
      }
      const d = res.data;
      if (!d.validCount) {
        return this.$alert(this.summaryHtml(d), "没有可导入的链接", {
          dangerouslyUseHTMLString: true,
        });
      }
      try {
        await this.$confirm(this.summaryHtml(d), "批量导入预览", {
          dangerouslyUseHTMLString: true,
          confirmButtonText: `确认导入 ${d.validCount} 条`,
          cancelButtonText: "取消",
          type: "warning",
        });
      } catch (e) {
        return; // 用户取消
      }

      const commit = await importLandingApi(this.form, false);
      const r = commit.data;
      if (r.code === 200) {
        this.$notify.success({
          title: "导入完成",
          message: `成功 ${r.data.inserted} 条，重复 ${r.data.duplicates.length} 条，格式错误 ${r.data.invalid.length} 条`,
        });
        Bus.$emit("closedialog");
        Bus.$emit("refreshtable");
        this.$refs.form.resetFields();
      } else {
        this.$message.error(r.message);
      }
    },

    async addlanding() {
      const { data: res } = await insertApi(this.form);
      if (res.code == 200) {
        Bus.$emit("closedialog");
        Bus.$emit("refreshtable");
        this.$message({
          message: "提交成功",
          type: "success",
        });
        this.$refs.form.resetFields();
      } else {
        return this.$message.error(res.message);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#Addlanding {
  margin-right: 10px;

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
  }

  .tell_content p {
    margin-bottom: 50px;
  }
}
</style>
