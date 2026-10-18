<template>
  <div id="Buildcustom">
    <Dialog :formtitle="'创建客服'">
      <template><i class="el-icon-circle-plus-outline"></i>创建客服</template>
      <template slot="currentForm"> 创建客服 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Buildcustom",

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
