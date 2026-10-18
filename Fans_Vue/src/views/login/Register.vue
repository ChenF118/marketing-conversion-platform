<template>
  <div id="Register">
    <my-logincontainer>
      <template> 账号注册 </template>
      <template #form>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width=""
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="username">
            <el-input
              v-model="ruleForm.username"
              placeholder="用户名"
            ></el-input>
          </el-form-item>

          <el-form-item label="" prop="password">
            <el-input
              type="password"
              v-model="ruleForm.password"
              placeholder="密码"
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
              >立即注册</el-button
            >
          </el-form-item>
        </el-form>
        <div class="tologin">
          <router-link to="/login">去登陆</router-link>
        </div>
      </template>
    </my-logincontainer>
  </div>
</template>

<script>
import MyLogincontainer from "@/views/login/LoginContainer.vue";
import { validateUsername, validatePassword } from "@/utils/validate";
import { RegisterApi } from "@/api/admin";
export default {
  name: "Register",
  data() {
    return {
      ruleForm: {
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
          this.toRegister();
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
    async toRegister() {
      const { data: res } = await RegisterApi(this.ruleForm);
      if (res.code == 200) {
        this.$notify({
          title: "注册成功",
          message: "跳转至登录界面",
          type: "success",
        });
        this.$router.push("/login");
      } else {
        this.$notify({
          title: "警告",
          message: res.message,
          type: "warning",
        });
      }
    },
  },
  components: {
    MyLogincontainer,
  },
};
</script>

<style lang="scss" scoped>
.el-form {
  padding: 0 50px;
  margin-top: 50px;

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

.tologin {
  padding-right: 50px;
  a {
    color: #76b9f7;
    font-size: 14px;
    float: right;
  }
}
</style>
