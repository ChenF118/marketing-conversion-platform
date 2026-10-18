<template>
  <div id="My-Vgedit">
    <my-dialog
      formtitle="修改分组信息"
      :openfromfather="isopen"
      @alreadyclose="closer"
    >
      <template>编辑按钮</template>
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
import { updatewxgroupApi } from "@/api/wxmanage";
export default {
  name: "My-Vgedit",
  props: ["isopenin", "fromfather"],
  data() {
    return {
      isopen: false,
      init: "",
      init1: "",
      form: {
        id: "",
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
      if (this.init == this.form.group && this.init1 == this.form.remark) {
        this.$notify({
          title: "警告",
          message: "未更改无需提交",
          type: "warning",
        });
      } else {
        this.$refs[formname].validate((valid) => {
          if (valid) {
            this.updategroup();
          } else {
            return false;
          }
        });
      }
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },
    closer() {
      this.$emit("closer");
    },

    // Api
    async updategroup() {
      const { data: res } = await updatewxgroupApi(this.form);
      if (res.code == 200) {
        this.$message({
          message: "修改成功",
          type: "success",
        });
        Bus.$emit("closedialog");
        this.$emit("refresh");
      } else if (res.code == 201) {
        this.$message({
          message: "该分组名称已存在！",
          type: "warning",
        });
      } else {
        return this.$message.error("修改失败！");
      }
    },
  },
  watch: {
    isopenin(newval) {
      this.isopen = newval;
    },
    fromfather(newval) {
      this.form.id = newval.id;
      this.form.group = newval.name;
      this.init = newval.name;
      this.form.remark = newval.remarks;
      this.init1 = newval.remarks;
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
