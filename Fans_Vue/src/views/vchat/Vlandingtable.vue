<template>
  <div id="VGrouptable">
    <div class="button">
      <el-button type="warning" size="small" @click="EditMany"
        >批量设置</el-button
      >
      <el-popconfirm title="确定删除吗？" @confirm="DeleteMany">
        <el-button
          type="warning"
          size="small"
          slot="reference"
          style="margin-left: 10px"
          >批量删除</el-button
        >
      </el-popconfirm>
    </div>

    <div class="group-table">
      <el-table
        ref="multipleTable"
        :data="tabledata"
        tooltip-effect="dark"
        style="width: 100%"
        stripe
        :border="true"
        :header-cell-style="{
          background: '#fafafa',
        }"
        @selection-change="handleSelectionChange"
        empty-text="请在落地页中开启微信管理，已开启请刷新！"
      >
        <el-table-column type="selection" width="50" align="center">
        </el-table-column>

        <el-table-column
          prop="wxgroup.name"
          fixed
          label="落地页分组"
          width="100"
        >
        </el-table-column>

        <el-table-column prop="url" label="落地页url" width="250">
        </el-table-column>

        <el-table-column prop="remarks" label="落地页备注" width="100">
        </el-table-column>

        <el-table-column prop="wxgroup.name" label="设置微信号" width="100">
        </el-table-column>
        <el-table-column
          prop="wxgroup.name"
          label="二维码"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="同访客微信号固定" width="145">
          <template slot-scope="scope">
            {{ scope.row.same || "未设置" }}
          </template>
        </el-table-column>

        <el-table-column label="分享微信号固定" width="140">
          <template slot-scope="scope">
            {{ scope.row.share || "未设置" }}
          </template>
        </el-table-column>

        <el-table-column label="移动端复制设置" width="140">
          <template slot-scope="scope">
            {{ scope.row.mclick || scope.row.mtouch || "未设置" }}
          </template>
        </el-table-column>

        <el-table-column label="PC端复制设置" width="140">
          <template slot-scope="scope">
            {{ scope.row.pclick || scope.row.pselect || "未设置" }}
          </template>
        </el-table-column>

        <el-table-column label="微信号来源" width="100">
          <template slot-scope="scope">
            {{ from(scope.row.from) }}
          </template>
        </el-table-column>
        <el-table-column label="微信号显示" width="100">
          <template slot-scope="scope">
            {{ scope.row.show || "未设置" }}
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="150">
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

    <div class="unshow">
      <my-landwithgroup
        dialogtitle="落地页微信号修改"
        ref="controldialog"
        @refresh="fresh"
      ></my-landwithgroup>
    </div>
  </div>
</template>

<script>
import { UnbindingApi, UnbindingLotApi } from "@/api/landing";
import MyLandwithgroup from "@/components/unshowbutton/Landwithgroup.vue";
export default {
  name: "VGrouptable",
  props: ["tabledata"],
  data() {
    return {
      multipleSelection: [],
    };
  },
  methods: {
    //刷新界面
    fresh() {
      this.$emit("refresh");
    },

    handleSelectionChange(val) {
      this.multipleSelection = val.map((item) => {
        return item.id;
      });
    },
    // 编辑
    handleEdit(index, row) {
      this.$refs.controldialog.opendialog(row);
    },
    // 删除
    handleDelete(index, row) {
      this.Unbinding(row.id);
    },
    // 批量删除
    DeleteMany() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选择任何数据",
          type: "warning",
        });
      } else {
        this.Unbindinglot();
      }
    },

    // 批量修改
    EditMany() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选择任何数据",
          type: "warning",
        });
      } else {
        this.$refs.controldialog.OpenFromEditMany(this.multipleSelection);
      }
    },
    // 表格渲染判断
    from(val) {
      let words = "";
      switch (val) {
        case 1:
          words = "分组管理";
          break;
        case 2:
          words = "手动指定";
          break;
        default:
          words = "未设置";
      }
      return words;
    },

    //Api
    async Unbinding(id) {
      const { data: res } = await UnbindingApi(id);
      if (res.code == 200) {
        this.fresh();
        this.$message({
          message: "已删除绑定",
          type: "success",
        });
      } else {
        return this.$message.error("出错了");
      }
    },
    // 批量解绑
    async Unbindinglot() {
      const { data: res } = await UnbindingLotApi(this.multipleSelection);
      if (res.code == 200) {
        this.$message({
          message: "修改成功",
          type: "success",
        });
        this.$emit("refresh");
      } else {
        return this.$message.error("发生错误！");
      }
    },
  },
  components: {
    MyLandwithgroup,
  },
};
</script>

<style lang="scss" scoped>
#VGrouptable {
  background-color: #fafafa;
  border: 1px solid #eee;
  .button {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 10px 15px;
  }
  .page {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .unshow {
    position: absolute;
    right: -999px;
  }
}
</style>
