<template>
  <div id="LandingIndex">
    <!-- 搜索区 -->
    <div class="search">
      <Search></Search>
    </div>
    <!-- 添加区 -->
    <div class="add">
      <Add></Add>
    </div>
    <!-- 表格区 -->
    <div class="table">
      <Grouptable></Grouptable>
    </div>
  </div>
</template>

<script>
import Search from "@/views/landing/Search.vue";
import Add from "@/views/landing/Add.vue";
import Grouptable from "@/views/landing/Grouptable.vue";
import Bus from "@/utils/event";
export default {
  name: "LandingIndex",
  created() {
    Bus.$on("refreshgroup", () => {
      this.getgrouplist();
    });
  },
  data() {
    return {
      grouplist: [],
    };
  },
  components: {
    Search,
    Add,
    Grouptable,
  },
};
</script>

<style lang="scss" scoped>
#LandingIndex {
  background-color: #fff;
  border-top: 1px solid #f6f6f6;
  padding: 10px 15px;

  .add {
    margin-bottom: 10px;
  }
}
</style>
