<template>
  <div id="LandingSearch">
    <el-form
      :inline="true"
      :model="formInline"
      size="small"
      class="demo-form-inline"
    >
      <el-form-item label="">
        <el-select
          clearable
          v-model="formInline.group"
          placeholder="选择分组"
          :popper-append-to-body="false"
        >
          <el-option
            v-for="item in selectlist"
            :key="item.id"
            :label="item.group"
            :value="item.group"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="">
        <el-select
          clearable
          v-model="formInline.enable"
          placeholder="是否启用"
          :popper-append-to-body="false"
        >
          <el-option label="启用" :value="true"></el-option>
          <el-option label="未启用" :value="false"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="">
        <el-input
          v-model="formInline.words"
          placeholder="搜索url,备注"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="onSubmit"
        ></el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import store from "@/store/index";
export default {
  name: "LandingSearch",
  created() {
    this.selectlist = store.state.grouplist;
  },
  data() {
    return {
      formInline: {
        group: "",
        enable: "",
        words: "",
      },
      selectlist: [],
    };
  },
  methods: {
    onSubmit() {
      Bus.$emit("landingfilter", this.formInline);
    },
  },
  components: {},
};
</script>

<style lang="scss" scoped>
// 下拉框最后一个显示不完全
::v-deep .el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}

#LandingSearch {
  .el-form {
    display: flex;
    justify-content: flex-start;
  }
}
</style>
