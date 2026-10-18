<template>
  <div id="JumpIndex">
    <MContainer>
      <template>
        <el-form-item>
          <el-input
            v-model="jumpform.user"
            placeholder="输入url查询"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="Mdialog">
        <Addjump></Addjump>
      </template>

      <!-- 表格区 -->
      <template slot="tables">
        <div class="Ctable">
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
            @selection-change="handleSelectionChange"
            empty-text="没有添加落地页跳转链接！"
          >
            <el-table-column
              prop="address"
              label="跳转前url"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="跳转后url"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="跳转类型"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="参数透传"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="同ip设置"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="跳转条件"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="是否启用"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="300">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >编辑</el-button
                >

                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </MContainer>
  </div>
</template>

<script>
import MContainer from "@/components/containner/MContainer.vue";
import Addjump from "@/components/button/Addjump.vue";
export default {
  name: "JumpIndex",
  created() {},
  data() {
    return {
      jumpform: {
        user: "",
      },
      tableData: [],
    };
  },
  methods: {
    // 表格的方法
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
  },
  components: {
    MContainer,
    Addjump,
  },
};
</script>

<style lang="scss" scoped></style>
