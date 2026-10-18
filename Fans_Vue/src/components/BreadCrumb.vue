<template>
  <div id="BreadCrumb">
    <el-breadcrumb class="app-breadcrumb" separator="/">
      <transition-group name="breadcrumb">
        <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
          <!-- 如果是数组的最后一项 -->
          <span
            v-if="
              item.redirect === 'noRedirect' || index == levelList.length - 1
            "
            class="no-redirect"
            >{{ item.meta.title }}</span
          >
          <!-- 不是数组的最后一项 -->
          <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
        </el-breadcrumb-item>
      </transition-group>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  name: "BreadCrumb",
  created() {
    this.getBreadcrumb();
  },
  data() {
    return {
      levelList: null,
    };
  },
  watch: {
    $route(route) {
      // 重定向页面不更新面包屑
      if (route.path.startsWith("/redirect/")) {
        return;
      }
      this.getBreadcrumb();
    },
  },
  methods: {
    getBreadcrumb() {
      // 只显示含meta.tltle的路由
      let matched = this.$route.matched.filter(
        (item) => item.meta && item.meta.title
      );
      // 面包屑拼接完整数组，而tags数组只取数组的最后一项即所选择的一项
      const first = matched[0];
      // 如果路由有name且不是首页则给路由信息数组拼接上首页的路由信息
      if (!this.isDashboard(first)) {
        matched = [
          { path: "/inside/home", meta: { title: "我的桌面" } },
        ].concat(matched);
      }
      // 这里将路由信息传递给store
      /* const pathTarget = matched[matched.length - 1];
      store.dispatch("asyncAddTag", pathTarget); */
      // 给最终需要的数组过滤掉没有meta.title和设定了不进入面包屑队列的路由
      this.levelList = matched.filter(
        (item) => item.meta && item.meta.title && item.meta.breadcrumb !== false
      );
    },
    // 路由无name属性返回false，非首页返回false，如果是首页返回true
    isDashboard(route) {
      const name = route && route.name;
      if (!name) {
        return false;
      }
      return name.trim().toLocaleLowerCase() === "MyHome".toLocaleLowerCase();
    },
    // 编辑url信息
    /* pathCompile(path) {
      const { params } = this.$route;
      var toPath = pathToRegexp.compile(path);
      return toPath(params);
    }, */
    // 点击面包屑跳转页面
    handleLink(item) {
      // console.log(item);
      const { redirect, path } = item;
      if (redirect) {
        this.$router.push(redirect);
        return;
      }
      // this.$router.push(this.pathCompile(path));
      this.$router.push(path);
    },
  },
};
</script>

<style lang="scss" scoped>
#BreadCrumb {
  height: 36px;

  .app-breadcrumb.el-breadcrumb {
    width: 100%;
    display: inline-block;
    font-size: 14px;
    line-height: 36px;
    margin-left: 8px;
    .no-redirect {
      color: #97a8be;
      cursor: text;
    }
  }
}
</style>
