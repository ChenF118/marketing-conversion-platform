<template>
  <div id="VGrouptable">
    <div class="button">
      <el-popconfirm title="确定删除吗？" @confirm="handleDeletelot">
        <el-button
          type="warning"
          slot="reference"
          size="small"
          style="margin-right: 10px"
          >批量修改</el-button
        >
      </el-popconfirm>
      <el-button type="warning" size="small">批量删除</el-button>
      <el-button type="warning" size="small">上传二维码</el-button>
    </div>

    <div class="group-table">
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
        empty-text="无数据"
      >
        <el-table-column type="selection" width="50" align="center">
        </el-table-column>
        <el-table-column fixed label="分组名称" prop="Wxgroup.name" width="100">
        </el-table-column>
        <el-table-column prop="wxh" label="微信号"> </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="二维码" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handlecheck(scope.$index, scope.row)"
            >
              查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="wxname"
          label="微信号名称"
          show-overflow-tooltip
          width="100"
        >
        </el-table-column>
        <el-table-column prop="sex" label="性别" show-overflow-tooltip>
        </el-table-column>

        <el-table-column label="在线状态" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.online"
              active-text="上线"
              inactive-text="下线"
              class="switchClass"
              @change="handleChangeOnline($event, scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="自动控制" prop="control" width="100">
        </el-table-column>

        <el-table-column
          prop="address"
          label="权重"
          show-overflow-tooltip
          width="151"
        >
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.level"
              @change="handlepower($event, scope.row)"
              :min="1"
              :max="5"
              size="mini"
            ></el-input-number>
          </template>
        </el-table-column>

        <el-table-column prop="neartime" label="最近状态" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="copy" label="今日复制" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="report" label="今日上报" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="fans" label="今日到粉" show-overflow-tooltip>
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

    <!-- 二维码 -->
    <div class="qrcode">
      <my-qrcode
        :outershow="outerShow"
        @closer="outerShow = false"
        @refresh="refreshtable"
        :image="sendimg"
      ></my-qrcode>
    </div>

    <div class="unshow">
      <Addvnumber
        ref="controldialog"
        dialogtitle="编辑微信号"
        @sendform="updataweixin"
      ></Addvnumber>
    </div>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import MyQrcode from "@/components/unshowbutton/Qrcode.vue";
import Addvnumber from "@/components/button/Addvnumber.vue";
import {
  updateWxApi,
  deletewxApi,
  updateOnlineApi,
  updatelevelApi,
} from "@/api/wxmanage";
export default {
  name: "VGrouptable",
  created() {},
  props: ["tableData"],
  data() {
    return {
      num: 1,
      // 二维码
      outerShow: false,

      //   分页相关
      currentPage: 1,
      // 准备发给弹框的图片
      sendimg: {
        id: 0,
        sendimg: "",
      },
      multipleSelection: [],
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val.map((item) => {
        return item.id;
      });
    },
    handlecheck(index, row) {
      this.sendimg.id = row.id;
      this.sendimg.sendimg = row.erweima;
      this.outerShow = true;
    },
    handlepower(val, row) {
      this.changelevel(row.id, val);
    },
    handleEdit(index, row) {
      this.$refs.controldialog.opendialog(row);
    },
    handleDelete(index, row) {
      this.deleteweixin(row.id);
    },
    handleDeletelot() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选择任何数据",
          type: "warning",
        });
      } else {
        this.deleteweixin(this.multipleSelection);
        this.$emit("torefresh");
      }
    },
    handleChangeOnline(val, row) {
      if (val == true) {
        val = 1;
      } else {
        val = 0;
      }
      this.changeonline(row.id, val);
    },

    refreshtable() {
      this.$emit("torefresh");
    },

    // Api
    async updataweixin(form) {
      const { data: res } = await updateWxApi(form);
      if (res.code == 200) {
        Bus.$emit("closedialog");
        this.$message({
          message: "修改成功",
          type: "success",
        });
        this.$emit("torefresh");
      } else {
        return this.$message.error(res.message);
      }
    },
    async deleteweixin(val) {
      const { data: res } = await deletewxApi(val);
      if (res.code == 200) {
        this.$message({
          message: "删除成功",
          type: "success",
        });
        this.$emit("torefresh");
      } else {
        return this.$message.error("发生错误");
      }
    },
    async changeonline(ids, val) {
      await updateOnlineApi(ids, val);
    },
    async changelevel(ids, val) {
      await updatelevelApi(ids, val);
    },
  },
  components: {
    MyQrcode,
    Addvnumber,
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
<style>
/* 修改开关样式 */
/* switch按钮样式 */
.switchClass .el-switch__label {
  position: absolute;
  display: none;
  color: #fff !important;
}
/*打开时文字位置设置*/
.switchClass .el-switch__label--right {
  z-index: 1;
}
/* 调整打开时文字的显示位子 */
.switchClass .el-switch__label--right span {
  margin-right: 13px;
  font-size: 12px;
}
/*关闭时文字位置设置*/
.switchClass .el-switch__label--left {
  z-index: 1;
}
/* 调整关闭时文字的显示位子 */
.switchClass .el-switch__label--left span {
  margin-left: 17px;
  font-size: 12px;
}
/*显示文字*/
.switchClass .el-switch__label.is-active {
  display: block;
}
/* 调整按钮的宽度 */
.switchClass.el-switch .el-switch__core,
.el-switch .el-switch__label {
  width: 50px !important;
  margin: 0;
  padding-left: 4px;
}

/*下拉框最后一个显示不完全*/
.el-select-dropdown__wrap.el-scrollbar__wrap {
  margin-bottom: 0 !important;
}
</style>
