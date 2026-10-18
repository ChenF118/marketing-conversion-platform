<template>
  <div id="TagView">
    <ScrollPane ref="scrollPane" class="tags-view-wrapper">
      <router-link
        v-for="(tag, index) in pathList"
        :key="tag.path"
        :to="tag.path"
      >
        <el-tag
          closable
          :type="isActive(tag) ? 'success' : ''"
          @close.capture.prevent="closetags(tag, index)"
        >
          {{ tag.meta.title }}
        </el-tag>
      </router-link>
    </ScrollPane>
    <br />
  </div>
</template>

<script>
// ScrollPane预留了滚动自定义事件暂未使用
import ScrollPane from "./ScrollPane";
import store from "@/store/index";
export default {
  name: "TagView",
  created() {},
  computed: {
    pathList() {
      return store.state.tagList;
    },
  },
  updated() {},
  components: {
    ScrollPane,
  },
  data() {
    return {
      activeIndex: "/home",
    };
  },
  watch: {
    $route(route) {
      this.activeIndex = route.path;
      this.addTags();
    },
  },
  mounted() {
    this.addTags();
  },
  methods: {
    // 判断是否是选定的路由
    isActive(route) {
      return route.path === this.$route.path;
    },
    // 关闭路由后决定下一个被选中的路由
    closetags(tag, index) {
      store.dispatch("removeTag", tag);
      if (tag.path == this.$route.path) {
        if (index > 0) {
          this.$router.replace(this.pathList[index - 1].path);
        } else if (index == 0) {
          this.$router.replace(this.pathList[0].path);
        }
      }
    },
    // 添加路由到store
    addTags() {
      /* const { name } = this.$route;
      console.log(name, this.$route); */
      // 只显示含meta.tltle的路由
      let matched = this.$route.matched.filter(
        (item) => item.meta && item.meta.title
      );
      if (matched) {
        store.dispatch("asyncAddTag", matched[0]);
      }
      return false;
    },
  },
};
</script>

<style lang="scss" scoped>
#TagView {
  width: 100%;
  height: 36px;
  background-color: #fff;
  padding-left: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  span {
    margin-right: 5px;
  }
}
</style>
