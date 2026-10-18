<template>
  <div id="CopyVisitor">
    <CmContainer>
      <!-- 搜索区 -->
      <template>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择url查询"
            :popper-append-to-body="false"
          >
            <el-option label="选择url查询" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <!-- 默认时间选择 -->
        <el-form-item>
          <el-col :span="11">
            <el-date-picker
              type="date"
              placeholder="开始日期"
              v-model="form.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-date-picker
              placeholder="截止日期"
              v-model="form.date2"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择落地页分组"
            :popper-append-to-body="false"
          >
            <el-option label="一组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择微信分组"
            :popper-append-to-body="false"
          >
            <el-option label="选择微信分组" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.order"
            placeholder="排序"
            :popper-append-to-body="false"
          >
            <el-option label="访问时间" value="shanghai"></el-option>
            <el-option label="打开微信" value="shanghai"></el-option>
            <el-option label="已到粉标记" value="shanghai"></el-option>
            <el-option label="复制微信号" value="shanghai"></el-option>
            <el-option label="来源关键词" value="shanghai"></el-option>
            <el-option label="来源搜索引擎" value="shanghai"></el-option>
            <el-option label="地区" value="shanghai"></el-option>
            <el-option label="访问设备" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="选择来源"
            :popper-append-to-body="false"
          >
            <el-option label="选择来源" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select
            clearable
            v-model="form.group"
            placeholder="数据上报状态"
            :popper-append-to-body="false"
          >
            <el-option label="等待上传" value="shanghai"></el-option>
            <el-option label="转化成功" value="shanghai"></el-option>
            <el-option label="转化失败" value="shanghai"></el-option>
            <el-option label="条件不足" value="shanghai"></el-option>
            <el-option label="无效转化" value="shanghai"></el-option>
            <el-option label="深度转化" value="shanghai"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.words"
            placeholder="搜索url/微信号/关键词"
          ></el-input>
        </el-form-item>
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
            empty-text="无数据"
          >
            <el-table-column type="selection" width="50" align="center">
            </el-table-column>
            <el-table-column
              prop="address"
              label="落地页分组"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="访问url"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="address" label="备注" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="address"
              label="数据上报"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="复制时间"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="复制时长"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="复制微信号"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="是否到粉"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="address"
              label="微信号分组"
              show-overflow-tooltip
            >
            </el-table-column>
          </el-table>
        </div>
      </template>
    </CmContainer>
  </div>
</template>

<script>
import CmContainer from "@/components/containner/CmContainer.vue";
export default {
  name: "CopyVisitor",
  created() {},
  data() {
    return {
      form: {
        group: "",
        date1: "",
        date2: "",
        order: "",
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
  },
  components: {
    CmContainer,
  },
};
</script>

<style lang="scss" scoped></style>
