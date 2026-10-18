<template>
  <div id="LandingAdd">
    <!-- 添加落地页 -->
    <Addlanding dialogtitle="添加落地页"></Addlanding>

    <!-- 落地页分组 -->
    <Dialog :formtitle="'落地页分组'">
      <template><i class="el-icon-circle-plus-outline"></i>落地页分组</template>
      <template slot="currentForm">
        <!-- 搜索 -->
        <div class="group-search">
          <el-input
            v-model="groupsearch"
            placeholder="输入分组名称查询"
            size="small"
          ></el-input>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            @click="searchgroup"
          ></el-button>
        </div>

        <!-- 添加分组 -->
        <div class="BTaddgroup">
          <el-button @click="inners = true" size="small">
            <i class="el-icon-circle-plus-outline"></i>添加分组
          </el-button>
        </div>

        <!-- 弹窗内表格 -->
        <div class="group-table">
          <el-scrollbar style="height: 100%">
            <AddGroup></AddGroup>
          </el-scrollbar>
        </div>

        <div class="innercontainer" v-if="inners">
          <div class="innert">添加分组</div>
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
                <el-input v-model="form.remark"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="inners = false"
                  >取消</el-button
                >
                <el-button type="primary" @click="onSubmit('form')"
                  >提交</el-button
                >
              </el-form-item>
            </el-form>
          </div>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import Dialog from "@/components/Dialog.vue";
import Addlanding from "@/components/button/Addlanding.vue";
import AddGroup from "@/views/landing/Addgroup.vue";
import { addgroupApi } from "@/api/landing";
// 验证规则

export default {
  name: "LandingAdd",
  created() {},
  data() {
    return {
      groupsearch: "",
      currentPage: 1,
      tableData: [],
      inners: false,
      form: {
        group: "",
        remark: "",
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
    };
  },
  components: {
    Dialog,
    Addlanding,
    AddGroup,
  },
  methods: {
    searchgroup() {
      Bus.$emit("getsearch", this.groupsearch);
    },

    onSubmit(formname) {
      this.$refs[formname].validate((valid) => {
        if (valid) {
          this.addgroup();
        } else {
          return false;
        }
      });
    },

    // Api
    async addgroup() {
      const { data: res } = await addgroupApi(this.form);
      if (res.code == 200) {
        this.inners = false;
        Bus.$emit("refreshgroup");
        this.$message({
          message: "提交成功",
          type: "success",
        });
        this.$refs.form.resetFields();
        this.form.remark = "";
      } else {
        return this.$message.error(res.message);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#LandingAdd {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  position: relative;

  .group-search {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 20px;

    .el-input {
      width: 300px;
      margin-right: 20px;
    }
  }
  .BTaddgroup {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-start;
  }
  .group-table {
    height: 50vh;
    padding-bottom: 5px;
  }

  .innercontainer {
    position: absolute;
    top: 50px;
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
