<template>
  <div id="Addapp">
    <Dialog :formtitle="'创建小程序'">
      <template><i class="el-icon-circle-plus-outline"></i>创建小程序</template>
      <template slot="currentForm"> 创建小程序 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Addapp",

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
