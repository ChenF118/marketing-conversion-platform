<template>
  <div id="AddCvs">
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
              <el-form-item label="选择统计链接" prop="uid" required>
                <el-select
                  clearable
                  v-model="form.uid"
                  placeholder="选择统计链接"
                >
                  <el-option
                    v-for="item in selectlist"
                    :key="item.id"
                    :label="item.url"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="转化类型">
                <el-select v-model="form.type" placeholder="请选择">
                  <el-option
                    v-for="item in cvstypelist"
                    :label="item.typename"
                    :value="item.typeint"
                    :key="item.typeint"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="统计类型">
                <el-radio-group v-model="form.cvstype">
                  <el-radio :label="1">点击</el-radio>
                  <el-radio :label="2">长按</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="计数方式">
                <el-radio-group v-model="form.cvscount">
                  <el-radio :label="1">多次触发计一次</el-radio>
                  <el-radio :label="0">多次触发计多次</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="统计方式">
                <el-radio-group v-model="form.cvsmode">
                  <el-radio :label="1">精确匹配</el-radio>
                  <el-radio :label="0">模糊匹配</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="埋点代码">
                <el-input v-model="form.code" disabled></el-input>
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
            <p style="margin-bottom: 110px">
              (如果没有选项，请在落地页管理中开启自定义转化统计功能)
            </p>
            <p>(长按可统计二维码长按识别等长按动作)</p>
            <p style="margin-bottom: 100px">
              (选择针对同一访客统多次触发是否重复统计)
            </p>
            <p>(请将埋点代码添加在需要统计的按钮上)</p>
          </div>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
import Bus from "@/utils/event";
import store from "@/store/index";
export default {
  name: "AddCvs",
  props: ["dialogtitle", "cvstypelist"],
  created() {
    this.selectlist = store.state.landinglist;
  },
  data() {
    return {
      form: {
        uid: "",
        type: 1,
        cvstype: 1,
        cvscount: 1,
        cvsmode: 1,
        code: "",
      },
      rules: {
        uid: { required: true, message: "请选择统计链接", trigger: "blur" },
      },
      selectlist: [],
    };
  },
  components: {
    Dialog,
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit("onsubmit", this.form);
          Bus.$emit("closedialog");
        } else {
          return false;
        }
      });
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
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
#AddCvs {
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
