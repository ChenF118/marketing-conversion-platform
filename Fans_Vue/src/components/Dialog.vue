<template>
  <div id="Dialog">
    <el-button @click="dialogFormVisible = true" size="small">
      <slot name="default"></slot>
    </el-button>

    <el-dialog :title="formtitle" :visible.sync="dialogFormVisible" width="70%">
      <div class="scrollcontent">
        <el-scrollbar style="height: 100%">
          <slot name="currentForm"></slot>
        </el-scrollbar>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
export default {
  name: "Dialog",
  created() {
    Bus.$on("closedialog", () => {
      this.dialogFormVisible = false;
    });
  },
  props: ["formtitle", "openfromfather"],
  data() {
    return {
      dialogFormVisible: false,
    };
  },
  computed: {},
  // 父传来
  watch: {
    openfromfather() {
      if (this.openfromfather) {
        this.dialogFormVisible = true;
      }
    },
    dialogFormVisible(newval) {
      if (!newval) {
        this.$emit("alreadyclose");
      }
    },
  },
};
</script>

<style scoped>
.el-dialog__header {
  border-bottom: 1px solid #f6f6f6;
}
/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
</style>
<style lang="scss">
// 使dialog头部有个分割线
.el-dialog__header {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #dcdfe6;
}
.el-dialog__body {
  height: 70vh;
  position: relative;
  overflow: hidden;

  .scrollcontent {
    height: 100%;
  }
}
</style>
