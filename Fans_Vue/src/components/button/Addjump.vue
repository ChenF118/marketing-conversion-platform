<template>
  <div id="Addjump">
    <Dialog :formtitle="'添加跳转'">
      <template><i class="el-icon-circle-plus-outline"></i>添加跳转</template>
      <template slot="currentForm"> 添加跳转 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Addjump",

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
