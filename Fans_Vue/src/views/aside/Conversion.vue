<template>
  <div id="Conversion">
    <router-view></router-view>
  </div>
</template>

<script>
import store from "@/store/index";
import { landingallApi } from "@/api/landing";
export default {
  name: "Conversion",
  created() {
    this.getlandinglist();
  },
  data() {
    return {};
  },
  methods: {
    async getlandinglist() {
      const { data: res } = await landingallApi();
      sessionStorage.setItem("landinglist", JSON.stringify(res.data));
      store.dispatch("getlanding", res.data);
    },
  },
};
</script>

<style lang="scss" scoped></style>
