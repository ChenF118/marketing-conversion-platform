<template>
  <div id="Addpublic">
    <Dialog :formtitle="'添加公众号'">
      <template><i class="el-icon-circle-plus-outline"></i>添加公众号</template>
      <template slot="currentForm"> 添加公众号 </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "Addpublic",

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
