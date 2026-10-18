<template>
  <div id="Addgroup">
    <div class="group-table">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        stripe
      >
        <el-table-column prop="group" label="分组名称"> </el-table-column>
        <el-table-column prop="landing_count" label="落地页数量">
        </el-table-column>
        <el-table-column prop="remarks" label="备注"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="300">
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

    <div class="page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalcount"
      >
      </el-pagination>
    </div>

    <!-- 嵌套的表单 -->
    <div class="innercontainer" v-if="inners">
      <div class="innert">修改分组</div>
      <div class="innerform">
        <el-form
          :model="form"
          size="small"
          :rules="rules"
          ref="form"
          label-position="top"
        >
          <el-form-item label="分组名称:" prop="group">
            <el-input v-model="form.group"></el-input>
          </el-form-item>
          <el-form-item label="备注:">
            <el-input v-model="form.remarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="inners = false">取消</el-button>
            <el-button type="primary" @click="onSubmit('form')">提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import {
  getgroupApi,
  updategroupApi,
  groupdelApi,
  groupsearchApi,
} from "@/api/landing";
export default {
  name: "Addgroup",
  created() {
    this.getgroup();
    Bus.$on("refreshgroup", () => {
      this.getgroup();
    });
    Bus.$on("getsearch", (val) => {
      this.getsearch(val, this.currentPage, this.singlepage);
    });
  },
  data() {
    return {
      tableData: [],
      switchflag: false,
      switchflags: false,
      //   分页相关
      currentPage: 1,
      singlepage: 10,
      totalcount: 0,
      form: {
        id: 1,
        group: "",
        remarks: "",
      },
      rules: {
        group: [
          {
            required: true,
            message: "分组名称必填",
            trigger: "blur",
          },
        ],
      },
      dialogFormVisible: false,
      // 编辑按钮触发的表单
      inners: false,
    };
  },
  methods: {
    handleEdit(index, row) {
      this.form.id = row.id;
      this.form.group = row.group;
      this.form.remarks = row.remarks;
      this.inners = true;
    },
    // 单删
    handleDelete(index, row) {
      this.delgroup(row.id);
    },

    // 更新分组
    onSubmit(formname) {
      this.$refs[formname].validate((valid) => {
        if (valid) {
          this.updategroup(this.form);
        } else {
          return false;
        }
      });
    },

    handleSizeChange(val) {
      this.singlepage = val;
      this.getgroup();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getgroup();
    },
    // api
    async getgroup() {
      const { data: res } = await getgroupApi(
        this.currentPage,
        this.singlepage
      );
      this.tableData = res.data.data;
      this.totalcount = res.data.count;
    },
    async delgroup(val) {
      const { data: res } = await groupdelApi(val);
      if (res.code == 200) {
        this.$notify.info({
          title: "确认",
          message: "已删除分组",
        });
        this.getgroup();
      }
    },
    async updategroup(msg) {
      const { data: res } = await updategroupApi(msg);
      if (res.code == 200) {
        this.$notify({
          title: "成功",
          message: "分组信息已修改",
          type: "success",
        });
        this.inners = false;
        this.getgroup();
      }
    },
    async getsearch(msg) {
      const { data: res } = await groupsearchApi(msg);
      this.tableData = res.data.data;
      this.totalcount = res.data.count;
    },
  },
};
</script>

<style lang="scss" scoped>
#Addgroup {
  background-color: #fafafa;
  border: 1px solid #eee;
  .page {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .innercontainer {
    position: absolute;
    top: 0;
    left: calc(50% - 250px);
    width: 500px;
    z-index: 999;
    background-color: #fff;
    border: 1px solid #ccc;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.22), 0 0 6px rgba(0, 0, 0, 0.14);

    .innert {
      border-bottom: 1px solid #dcdfe6;
      font-size: 18px;
      padding: 10px;
      text-align: center;
    }
    .innerform {
      padding: 20px 40px;
    }
  }
}
</style>
