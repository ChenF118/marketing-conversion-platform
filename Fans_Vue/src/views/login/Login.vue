<template>
  <div id="Login">
    <my-logincontainer>
      <template> 账号登录 </template>
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

          <div class="log_others">
            <router-link to="/register">账号注册</router-link>
            <router-link to="">忘记密码</router-link>
          </div>

          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')"
              >登录</el-button
            >
          </el-form-item>
        </el-form>
      </template>
    </my-logincontainer>
  </div>
</template>

<script>
import store from "@/store/index";
import MyLogincontainer from "@/views/login/LoginContainer.vue";
import { validateUsername, validatePassword } from "@/utils/validate";
import { LoginApi } from "@/api/admin";
export default {
  name: "Login",
  data() {
    return {
      ruleForm: {
        username: "",
        password: "",
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
          this.Login();
        } else {
          return false;
        }
      });
    },
    async Login() {
      const { data: res } = await LoginApi(this.ruleForm);
      switch (res.code) {
        case 200:
          sessionStorage.setItem("Token", `Bearer ${res.Token}`);
          sessionStorage.setItem("IDcode", res.IDcode);
          store.dispatch("addIdcode", res.IDcode);
          this.$message({
            message: "登录成功！",
            type: "success",
          });
          this.$router.push("/inside/home");
          break;
        case 201:
          this.$message({
            message: res.message,
            type: "warning",
          });
          break;
        case 202:
          this.$message.error(res.message);
          break;
        default:
          this.$message.error("未知错误");
          break;
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

  .log_others {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 12px;
    margin-bottom: 20px;

    a {
      color: #000;
      text-decoration: none;

      &:nth-child(1) {
        color: #76b9f7;
      }
    }
  }
}
</style>
