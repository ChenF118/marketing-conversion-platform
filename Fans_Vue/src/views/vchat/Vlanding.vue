<template>
  <div id="Vlanding">
    <VchatContainer
      :count="totalcount"
      @sendSize="changeSize"
      @sendCurrent="changeCurrent"
      @filtersearch="filtersearch"
    >
      <!-- 表单区 -->
      <template>
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
        <el-form-item>
          <el-input
            v-model="formInline.words"
            placeholder="输入url搜索"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 表格区 -->
      <template slot="tables">
        <div class="grouptable">
          <Vlandingtable
            :tabledata="tableData"
            @refresh="langdingwithgroup"
          ></Vlandingtable>
        </div>
      </template>
    </VchatContainer>
  </div>
</template>

<script>
import store from "@/store/index";
import VchatContainer from "@/views/vchat/VchatContainer.vue";
import Vlandingtable from "@/views/vchat/Vlandingtable.vue";
import { LandingWithGroupApi } from "@/api/landing";
export default {
  name: "Vlanding",
  created() {
    this.selectlist = store.state.grouplist;
    this.langdingwithgroup();
  },
  data() {
    return {
      formInline: {
        group: "",
        words: "",
      },
      // 表格数据
      totalcount: 0,
      tableData: [],
      // 分页
      currentPage: 1,
      pageSize: 10,
      // 落地页分组
      selectlist: [],
    };
  },
  methods: {
    // 子组件传来的分页数据
    changeSize(val) {
      this.pageSize = val;
      this.langdingwithgroup();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.langdingwithgroup();
    },
    // 筛选搜索按钮
    filtersearch() {
      this.langdingwithgroup();
    },

    //Api
    async langdingwithgroup() {
      const { data: res } = await LandingWithGroupApi(
        this.currentPage,
        this.pageSize,
        this.formInline
      );
      this.tableData = res.data;
      this.totalcount = res.count;
    },
  },
  components: {
    VchatContainer,
    Vlandingtable,
  },
};
</script>

<style lang="scss" scoped>
.grouptable {
  background-color: #fff;
}
</style>
