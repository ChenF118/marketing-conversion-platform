<template>
  <div id="Mypublic">
    <MContainer>
      <template>
        <el-form-item>
          <el-input
            v-model="publicform.words"
            placeholder="搜索公众号"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="Mdialog">
        <Addpublic></Addpublic>
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
            empty-text="没有数据"
          >
            <el-table-column
              prop="address"
              label="公众号名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="公众号原始ID"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="address" label="备注" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="address"
              label="今日关注数量"
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
import Addpublic from "@/components/button/Addpublic.vue";
export default {
  name: "Mypublic",
  created() {},
  data() {
    return {
      publicform: {
        words: "",
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
    Addpublic,
  },
};
</script>

<style lang="scss" scoped></style>
