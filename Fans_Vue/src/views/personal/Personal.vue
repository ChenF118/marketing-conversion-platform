<template>
  <div id="Personal">
    <el-row :gutter="20">
      <el-col :span="8"
        ><div class="grid-content bg-purple left">
          <p>管理员</p>
          <div class="avatar">
            <el-avatar :size="100" :src="userinfo.header"></el-avatar>
            <span>账号信息</span>
            <div></div>
          </div>
          <div class="btm">
            <i class="el-icon-reading">用户昵称</i>
            <div>{{ userinfo.username }}</div>
          </div>
          <div class="btm">
            <i class="el-icon-user">权限</i>
            <div>{{ userinfo.power }}</div>
          </div>
        </div></el-col
      >
      <!-- 右侧 -->
      <el-col :span="16"
        ><div class="grid-content bg-purple right">
          <el-tabs v-model="activeName">
            <el-tab-pane label="修改信息" name="second">
              <el-form
                :model="ruleForm"
                :rules="rules"
                ref="ruleForm"
                label-width=""
                class="demo-ruleForm"
              >
                <el-form-item label="用户名" prop="username">
                  <el-input
                    v-model="ruleForm.username"
                    placeholder="请输入用户名"
                  ></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                  <el-input
                    type="password"
                    v-model="ruleForm.password"
                    placeholder="请输入密码"
                  ></el-input>
                </el-form-item>

                <el-form-item label="">
                  <el-upload
                    class="avatar-uploader"
                    action="#"
                    :http-request="httpRequest"
                    :show-file-list="false"
                  >
                    <img
                      v-if="ruleForm.imageUrl"
                      :src="ruleForm.imageUrl"
                      class="avatar"
                    />
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" @click="submitForm('ruleForm')"
                    >提交修改</el-button
                  >
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="账户" name="first">
              <el-timeline>
                <el-timeline-item color="#46a6ff">
                  <el-card>
                    <h4>已使用配额</h4>
                    <p>提交于 2022/11/30 10.40</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item color="#46a6ff">
                  <el-card>
                    <h4>绑定信息</h4>
                    <p>提交于 2022/11/30 10.40</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-tab-pane>
          </el-tabs>
        </div></el-col
      >
    </el-row>
  </div>
</template>

<script>
import store from "@/store";
import Bus from "@/utils/event.js";
import { validateUsername, validatePassword } from "@/utils/validate";
import { ChangePassApi } from "@/api/admin";
export default {
  name: "myPersonal",
  created() {
    this.userinfo = store.state.userinfo;
    this.ruleForm.id = this.userinfo.id;
  },
  data() {
    return {
      userinfo: null,
      activeName: "second",
      labelPosition: "top",
      ruleForm: {
        id: 0,
        username: "",
        password: "",
        imageUrl: "",
        power: 5,
      },
      rules: {
        username: [
          { required: true, message: "账号不可为空", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不可为空", trigger: "blur" },
          { validator: validatePassword, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changePass();
        } else {
          return false;
        }
      });
    },
    // 头像
    getFile(file2) {
      this.getBase64(file2).then((res) => {
        this.ruleForm.imageUrl = res;
      });
    },
    beforeAvatarUpload(file) {
      const isJPG =
        ["image/jpeg", "image/PNG", "image/jpg"].indexOf(file.type) == -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (isJPG) {
        return this.$message.error("上传图片只能是 JPG或PNG 格式!");
      }
      if (!isLt2M) {
        return this.$message.error("上传图片大小不能超过 2MB!");
      }
      this.getFile(file);
    },
    getBase64(file1) {
      return new Promise((resolve, reject) => {
        let reader = new FileReader();
        let fileResult = "";
        reader.readAsDataURL(file1); //开始转
        reader.onload = function () {
          fileResult = reader.result;
        }; //转 失败
        reader.onerror = function (error) {
          reject(error);
        }; //转 结束 resolve 出去
        reader.onloadend = function () {
          resolve(fileResult);
        };
      });
    },
    httpRequest(data) {
      this.beforeAvatarUpload(data.file);
    },

    // Api
    async changePass() {
      const { data: res } = await ChangePassApi(this.ruleForm);
      if (res.code == 200) {
        this.$notify({
          title: "成功",
          message: "信息已修改",
          type: "success",
        });
        Bus.$emit("refreshuserinfo");
        this.userinfo = store.state.userinfo;
      } else {
        this.$notify({
          title: "警告",
          message: res.message,
          type: "warning",
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#Personal {
  margin: 10px;
}
/* el row部分 */
.el-row {
  margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #ffffff;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
/* 自定大小 */
.left {
  height: 550px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
.left p {
  padding: 18px 20px;
  border-bottom: 1px solid #e6ebf5;
}
.right {
  height: 550px;
  padding: 18px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}
/* 卡片 */
.avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 10px;
}
.avatar span {
  margin-top: 10px;
  margin-bottom: 30px;
  font-weight: 700;
}
.avatar > div i {
  text-align: left;
}
.btm {
  padding: 0 20px;
  margin-top: 20px;
}
.btm div {
  color: #777;
  font-size: 14px;
  margin-top: 10px;
  border-top: 1px solid #e6ebf5;
  padding-top: 10px;
}
/* 右侧 */
.el-button {
  color: #fff;
  background-color: #46a6ff;
}
.el-form {
  width: 50%;
  margin: 0 auto;

  .el-button--primary {
    width: 100%;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 78px;
    height: 78px;
    line-height: 78px;
    text-align: center;
  }
  .avatar {
    width: 78px;
    height: 78px;
    display: block;
  }
}
</style>
