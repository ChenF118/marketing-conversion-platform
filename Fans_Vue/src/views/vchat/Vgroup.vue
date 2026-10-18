<template>
  <div id="Vgroup">
    <VchatContainer
      :count="totalcount"
      @sendSize="changeSize"
      @sendCurrent="changeCurrent"
      @filtersearch="filtersearch"
    >
      <!-- 表单区 -->
      <template>
        <el-form-item>
          <el-input v-model="filter" placeholder="输入分组名称查询"></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="vdialog">
        <Addgroup @refresh="torefresh"></Addgroup>
      </template>

      <!-- 表格区 -->
      <template slot="tables">
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          style="width: 100%"
          stripe
          :border="true"
          :header-cell-style="{
            background: '#fafafa',
          }"
          empty-text="没有开启微信管理的链接，请在落地页管理中开启微信在线管理功能"
        >
          <el-table-column fixed label="分组名称" prop="name">
          </el-table-column>
          <el-table-column prop="weixin_count" label="微信号数量">
          </el-table-column>
          <el-table-column prop="remarks" label="备注" show-overflow-tooltip>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="300">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)"
                >编辑</el-button
              >

              <el-popconfirm
                title="确定删除这条吗？"
                @confirm="handleDelete(scope.$index, scope.row)"
              >
                <el-button
                  size="mini"
                  type="danger"
                  slot="reference"
                  style="margin-left: 10px"
                  >删除</el-button
                >
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </template>
    </VchatContainer>

    <div class="unshow">
      <my-vgedit
        :isopenin="editbt"
        :fromfather="tochild"
        @closer="editbt = false"
        @refresh="torefresh"
      ></my-vgedit>
    </div>
  </div>
</template>

<script>
import VchatContainer from "@/views/vchat/VchatContainer.vue";
import Addgroup from "@/components/button/Addgroup.vue";
import MyVgedit from "@/components/unshowbutton/VgEdit.vue";
import { getwxgroupApi, delgroupApi } from "@/api/wxmanage";
export default {
  name: "Vgroup",
  created() {
    this.GetWxgroup();
  },
  data() {
    return {
      filter: "",
      // 表格数据
      totalcount: 0,
      tableData: [],
      // 分页
      currentPage: 1,
      pageSize: 10,
      // 编辑按钮
      editbt: false,
      // 发给编辑按钮的数据
      tochild: {},
    };
  },
  components: {
    VchatContainer,
    Addgroup,
    MyVgedit,
  },
  methods: {
    // 子组件传来的分页数据
    changeSize(val) {
      this.pageSize = val;
      this.GetWxgroup();
    },
    changeCurrent(val) {
      this.currentPage = val;
      this.GetWxgroup();
    },

    // 筛选搜索按钮
    filtersearch() {
      this.GetWxgroup();
    },

    // 编辑按钮
    handleEdit(index, row) {
      this.editbt = true;
      this.tochild = row;
    },
    // 删除按钮
    handleDelete(index, row) {
      this.DelGroup(row.id);
    },

    // 更新分组
    torefresh() {
      this.GetWxgroup();
    },

    // Api
    async GetWxgroup() {
      const { data: res } = await getwxgroupApi(
        this.currentPage,
        this.pageSize,
        this.filter
      );
      this.totalcount = res.count;
      this.tableData = res.data;
    },
    async DelGroup(val) {
      const { data: res } = await delgroupApi(val);
      if (res.code == 200) {
        this.$message({
          message: "删除成功",
          type: "success",
        });
        this.GetWxgroup();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.grouptable {
  background-color: #fff;
}
.unshow {
  position: absolute;
  right: -888px;
}
</style>
