<template>
  <div id="Container">
    <el-scrollbar style="height: 100%">
      <Tagview></Tagview>
      <router-view> </router-view>
    </el-scrollbar>
  </div>
</template>

<script>
import Tagview from "@/components/TagsView/index";
export default {
  name: "Container",

  data() {
    return {};
  },
  components: {
    Tagview,
  },
};
</script>

<style lang="scss" scoped>
#Container {
  height: 100%;
  overflow: hidden;
}
</style>
