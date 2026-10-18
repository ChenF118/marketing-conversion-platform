<template>
  <div id="Addcvstype">
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
              :rules="rules"
              label-width="110px"
            >
              <el-form-item label="转化类型" prop="type">
                <el-input
                  v-model="form.type"
                  placeholder="请输入转化类型"
                ></el-input>
              </el-form-item>
              <el-form-item label="埋点代码" prop="int">
                <el-input v-model="form.int">
                  <template slot="prepend">type_index=</template>
                </el-input>
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
            <p style="margin-bottom: 110px">转化类型与埋点代码必填</p>
          </div>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
import Bus from "@/utils/event";
import { appendcvstypeApi } from "@/api/conversion";
export default {
  name: "Addcvstype",
  props: ["dialogtitle"],

  data() {
    return {
      form: {
        type: "",
        int: 0,
      },
      rules: {
        type: { required: true, message: "请输入统计类型", trigger: "blur" },
        int: { required: true, message: "请输入埋点数字", trigger: "blur" },
      },
    };
  },
  components: {
    Dialog,
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addcvstype();
        } else {
          return false;
        }
      });
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },

    async addcvstype() {
      const { data: res } = await appendcvstypeApi(this.form);
      switch (res.code) {
        case 200:
          this.$message({
            type: "success",
            message: `添加成功!`,
          });
          this.$emit("onsubmit");
          Bus.$emit("closedialog");
          break;
        case 201:
          this.$message({
            message: "该埋点代码已存在！",
            type: "warning",
          });
          break;
        default:
          this.$message.error("错误！");
          break;
      }
    },
  },
  watch: {
    "form.type": {
      handler(val) {
        this.form.code = `type_index="${val}"`;
      },
      immediate: true,
    },
  },
};
</script>

<style lang="scss" scoped>
#Addcvstype {
  .half {
    display: flex;
  }
  .form_content {
    width: 50%;

    .code {
      height: 40px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
    }
  }
  .tell_content {
    flex: 1;
    font-size: 12px;
    padding: 10px 0;

    p {
      margin-bottom: 50px;
    }
  }
}
</style>
