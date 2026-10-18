<template>
  <div id="MyAddgroup">
    <my-dialog
      formtitle="添加分组"
      :openfromfather="isopen"
      @alreadyclose="isopen = false"
    >
      <template><i class="el-icon-circle-plus-outline"></i>添加分组 </template>

      <template slot="currentForm">
        <div class="content">
          <div class="flex">
            <div class="form">
              <el-form
                :model="form"
                size="small"
                :rules="rules"
                ref="form"
                label-width="150px"
              >
                <el-form-item label="分组名称:" prop="group">
                  <el-input v-model="form.group"></el-input>
                </el-form-item>
                <el-form-item label="备注:">
                  <el-input v-model="form.remark"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit('form')"
                    >提交</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
            <div class="formright">
              <p>填写分组名称</p>
              <p style="margin-top: 32px">( 备注非必填 )</p>
            </div>
          </div>
        </div>
      </template>
    </my-dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import { appendwxgroupApi } from "@/api/wxmanage";
export default {
  name: "MyAddgroup",
  props: ["isopenin"],
  data() {
    return {
      isopen: false,
      form: {
        group: "",
        remark: "",
      },
      rules: {
        group: [
          {
            required: true,
            message: "分组名称必填",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    onSubmit(formname) {
      this.$refs[formname].validate((valid) => {
        if (valid) {
          this.addgroup();
        } else {
          return false;
        }
      });
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },

    // Api
    async addgroup() {
      const { data: res } = await appendwxgroupApi(this.form);
      if (res.code == 200) {
        Bus.$emit("closedialog");
        this.$emit("refresh");
        this.$message({
          message: "提交成功",
          type: "success",
        });
        this.$refs.form.resetFields();
        this.form.remark = "";
      } else {
        return this.$message.error(res.message);
      }
    },
  },
  watch: {
    isopenin(newval) {
      this.isopen = newval;
    },
  },
};
</script>

<style lang="scss" scoped>
.content {
  height: 434px;

  .flex {
    width: 100%;
    display: flex;
    margin-top: 40px;

    .form {
      width: 60%;
      margin-right: 20px;
    }

    .formright {
      p {
        margin-top: 5px;
      }
    }
  }
}
</style>
