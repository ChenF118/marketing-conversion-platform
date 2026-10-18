<template>
  <div id="Vchat">
    <router-view></router-view>
  </div>
</template>

<script>
import store from "@/store/index";
import { getgrouplistApi } from "@/api/wxmanage";
export default {
  name: "Vchat",
  created() {
    this.GetGroupList();
  },
  data() {
    return {};
  },
  methods: {
    async GetGroupList() {
      const { data: res } = await getgrouplistApi();
      localStorage.setItem("wxgrouplist", JSON.stringify(res.data));
      store.dispatch("getwxgroup", res.data);
    },
  },
};
</script>

<style lang="scss" scoped></style>
