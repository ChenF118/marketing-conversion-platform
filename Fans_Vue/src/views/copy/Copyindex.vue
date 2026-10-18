<template>
  <div id="Copyindex">
    <VchatContainer>
      <!-- 表单区 -->
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="formInline.group"
            placeholder="选择分组"
            :popper-append-to-body="false"
          >
            <el-option label="一组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="formInline.url"
            placeholder="输入url搜索"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 表格区 -->
      <template slot="tables">
        <Cindextable></Cindextable>
      </template>
    </VchatContainer>
  </div>
</template>

<script>
import VchatContainer from "@/views/vchat/VchatContainer.vue";
import Cindextable from "@/views/copy/Cindextable.vue";
export default {
  name: "Copyindex",
  created() {},
  data() {
    return {
      formInline: {
        group: "",
        url: "",
      },
    };
  },
  methods: {},
  components: {
    VchatContainer,
    Cindextable,
  },
};
</script>

<style lang="scss" scoped></style>
<style>
/* 下拉选择框不遮盖 */
.el-form-item--mini.el-form-item {
  margin-bottom: 0 !important;
}
</style>
