<template>
  <div id="Outcontainer">
    <el-container>
      <el-header height="50px">
        <Header></Header>
      </el-header>

      <el-container>
        <el-aside width="null">
          <Aside></Aside>
        </el-aside>
        <el-main>
          <Main></Main>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import store from "@/store/index";
import Bus from "@/utils/event.js";
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import Main from "@/components/Main";
import { getUSerApi } from "@/api/admin";
import { groupallApi, landingallApi } from "@/api/landing";
export default {
  name: "Outcontainer",
  created() {
    this.IDcode = store.state.IDcode;
    this.getuser();
    this.getgrouplist();
    this.getlandinglist();
    Bus.$on("refreshuserinfo", () => {
      this.getuser();
    });
  },
  data() {
    return {
      IDcode: "",
    };
  },
  components: {
    Aside,
    Header,
    Main,
  },
  methods: {
    // API
    async getuser() {
      const { data: res } = await getUSerApi(this.IDcode);
      localStorage.setItem("userinfo", JSON.stringify(res.data));
      store.dispatch("addInfo", res.data);
    },

    async getgrouplist() {
      const { data: res } = await groupallApi();
      localStorage.setItem("grouplist", JSON.stringify(res.data));
      store.dispatch("getgroup", res.data);
    },

    async getlandinglist() {
      const { data: res } = await landingallApi();
      sessionStorage.setItem("landinglist", JSON.stringify(res.data));
      store.dispatch("getlanding", res.data);
    },
  },
};
</script>

<style lang="scss" scoped>
#Outcontainer {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}

.el-header {
  background: linear-gradient(
    to left,
    rgba(1, 170, 237, 1),
    rgba(82, 183, 109, 1)
  );
}

.el-container {
  height: 100%;
  overflow: hidden;

  .el-aside {
    height: 100%;
  }
  .el-main {
    padding: 0;
    background-color: #fff;
  }
}
</style>
