<template>
  <div id="CvsType">
    <div class="typebutton">
      <my-cvstype dialogtitle="添加转化类型" @onsubmit="gettype"></my-cvstype>
    </div>

    <div class="typetable">
      <el-table
        :data="tableData"
        :border="true"
        :header-cell-style="{
          background: '#fafafa',
        }"
        tooltip-effect="dark"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="索引" width="80"> </el-table-column>
        <el-table-column prop="typename" label="转化类型">
          <template slot-scope="scope">
            <el-input
              v-model="input"
              v-if="flag == scope.row.id"
              v-focus
              :placeholder="scope.row.typename"
              @change="savename(scope.row)"
              @blur="flag = ''"
            ></el-input>
            <span v-else>{{ scope.row.typename }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="typeint" label="埋点代码"> </el-table-column>

        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
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
    </div>
  </div>
</template>

<script>
import MyCvstype from "@/components/button/Addcvstype.vue";
import {
  getcvstypeApi,
  changetypenameApi,
  deletetypeApi,
} from "@/api/conversion";

export default {
  name: "CvsType",
  created() {
    this.gettype();
  },
  data() {
    return {
      tableData: [],
      input: "",
      flag: "",
    };
  },
  methods: {
    handleEdit(index, row) {
      this.flag = row.id;
      this.input = row.typename;
    },
    handleDelete(index, row) {
      this.deletetypeApi(row.id);
    },
    savename(row) {
      this.$confirm("将修改转化类型名称, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.changename(row.id);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消修改",
          });
        });
    },

    async gettype() {
      const { data: res } = await getcvstypeApi();
      this.tableData = res.data;
    },
    async changename(int) {
      const { data: res } = await changetypenameApi(int, this.input);
      if (res.code == 200) {
        this.$message({
          type: "success",
          message: `修改成功!`,
        });
        this.gettype();
      } else {
        this.$message.error("错误！");
      }
    },
    async deletetypeApi(val) {
      const { data: res } = await deletetypeApi(val);
      if (res.code == 200) {
        this.$message({
          type: "success",
          message: `删除成功!`,
        });
        this.gettype();
      } else {
        this.$message.error("错误！");
      }
    },
  },
  directives: {
    focus: {
      inserted: function (el) {
        el.querySelector("input").focus();
      },
    },
  },
  components: {
    MyCvstype,
  },
};
</script>

<style lang="scss" scoped>
#CvsType {
  background-color: #fff;
  border-top: 1px solid #f6f6f6;
  padding: 10px 20px;

  .typebutton {
    padding-bottom: 10px;
    display: flex;
    justify-content: flex-start;
  }

  /* .typetable {
    padding: 0 20px 10px;
  } */
}
</style>
