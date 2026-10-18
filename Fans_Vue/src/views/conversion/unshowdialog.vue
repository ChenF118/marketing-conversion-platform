<template>
  <div id="unshowdialog">
    <Dialog
      formtitle="修改统计信息"
      :openfromfather="isopen"
      @alreadyclose="closer"
    >
      <template>编辑</template>
      <template slot="currentForm">
        <div class="half">
          <div class="form_content">
            <el-form ref="form" :model="form" label-width="100px">
              <el-form-item label="统计链接">
                <el-input v-model="form.landingurl" disabled></el-input>
              </el-form-item>

              <el-form-item label="转化类型">
                <el-select clearable v-model="form.type" placeholder="请选择">
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
                <el-button type="primary" @click="onSubmit">提 交</el-button>
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
import Bus from "@/utils/event";
import store from "@/store/index";
import Dialog from "@/components/Dialog.vue";
export default {
  name: "unshowdialog",
  created() {
    this.selectlist = store.state.landinglist;
  },
  props: ["isopenin", "formfather", "cvstypelist"],
  data() {
    return {
      isopen: false,
      form: {
        id: "",
        landingurl: "",
        type: 1,
        cvstype: 1,
        cvscount: 0,
        cvsmode: 0,
        code: "",
      },
      selectlist: [],
    };
  },
  methods: {
    onSubmit() {
      this.$emit("editedform", this.form);
      Bus.$emit("closedialog");
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },
    closer() {
      this.isopen = false;
      this.$emit("closer");
    },
  },
  components: {
    Dialog,
  },
  watch: {
    isopenin(newval) {
      this.isopen = newval;
    },
    formfather(newval) {
      this.form.id = newval.id;
      this.form.landingurl = newval.landing.url;
      this.form.type = newval.type;
      this.form.code = newval.code;
      /* this.form.cvstype = newval.cvstype;
      this.form.cvscount = newval.cvscount;
      this.form.cvsmode = newval.cvsmode; */
    },
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
</style>
