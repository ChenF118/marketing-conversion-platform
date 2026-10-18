<template>
  <div id="Customapp">
    <Dialog :formtitle="'定制小程序'">
      <template>定制小程序</template>
      <template slot="currentForm"> 定制小程序 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Customapp",

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
