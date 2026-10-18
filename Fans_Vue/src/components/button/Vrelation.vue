<template>
  <div id="Vrelation">
    <Dialog :formtitle="'管理企业微信号'">
      <template
        ><i class="el-icon-circle-plus-outline"></i>管理企业微信号</template
      >
      <template slot="currentForm"> 管理企业微信号 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Vrelation",

  data() {
    return {};
  },

  watch: {},
  components: {
    Dialog,
  },
  methods: {},
};
</script>

<style lang="scss" scoped></style>
<style></style>
