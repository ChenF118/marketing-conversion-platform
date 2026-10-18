<template>
  <div id="Myfans">
    <MContainer>
      <!-- 表单区 -->
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="faultform.company"
            placeholder="选择企业"
            :popper-append-to-body="false"
          >
            <el-option label="选择企业" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="faultform.words"
            placeholder="搜索企业微信号/微信号"
          ></el-input>
        </el-form-item>
      </template>

      <!-- 按钮区 -->
      <template slot="Mdialog">
        <Vcompany></Vcompany>
        <Vrelation></Vrelation>
      </template>

      <!-- 表格区 -->
      <template slot="intable">
        <div class="intable">
          <el-button type="warning" size="small">批量删除</el-button>
        </div>
      </template>
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
            empty-text="没有添加企业微信号！"
          >
            <el-table-column type="selection" width="55" align="center">
            </el-table-column>
            <el-table-column
              prop="address"
              label="企业名称"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="企业微信账号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号状态"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="二维码"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号备注"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="今日复制"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="今日长按识别"
              show-overflow-tooltip
            >
            </el-table-column>
          </el-table>
        </div>
      </template>
    </MContainer>
  </div>
</template>

<script>
import MContainer from "@/components/containner/MContainer.vue";
import Vcompany from "@/components/button/Vcompany.vue";
import Vrelation from "@/components/button/Vrelation.vue";
export default {
  name: "Myfans",
  created() {},
  data() {
    return {
      faultform: {
        company: "",
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
    Vcompany,
    Vrelation,
  },
};
</script>

<style lang="scss" scoped>
.intable {
  display: flex;
  justify-content: flex-start;
}
</style>
