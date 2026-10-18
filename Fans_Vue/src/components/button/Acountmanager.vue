<template>
  <div id="Acountmaneger">
    <Dialog :formtitle="'小程序账号管理'">
      <template>小程序账号管理</template>
      <template slot="currentForm"> 小程序账号管理 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Acountmaneger",

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
