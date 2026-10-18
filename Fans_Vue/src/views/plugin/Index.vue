<template>
  <div id="PluginIndex">
    <el-container>
      <el-header height="50px">
        <!-- 已安装 -->
        <Dialog :formtitle="'已安装插件'">
          <template>已安装插件</template>
          <template slot="currentForm"> 已安装插件 </template>
        </Dialog>
        <!-- 表单留言查看 -->
        <Dialog :formtitle="'表单留言查看'">
          <template>表单留言查看</template>
          <template slot="currentForm"> 表单留言查看 </template>
        </Dialog>
        <!-- 我的定制插件 -->
        <Dialog :formtitle="'我的定制插件'">
          <template>我的定制插件</template>
          <template slot="currentForm"> 我的定制插件 </template>
        </Dialog>
        <!-- 上报插件开发 -->
        <Dialog :formtitle="'上报插件开发'">
          <template>上报插件开发</template>
          <template slot="currentForm"> 上报插件开发 </template>
        </Dialog>
        <!-- 兼容性问题提交 -->
        <Dialog :formtitle="'兼容性问题提交'">
          <template>兼容性问题提交</template>
          <template slot="currentForm"> 兼容性问题提交 </template>
        </Dialog>
      </el-header>
      <el-main>
        <el-empty description="暂无插件"></el-empty>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
export default {
  name: "PluginIndex",
  props: [],
  data() {
    return {};
  },
  // 父传来
  watch: {},
  components: {
    Dialog,
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
#PluginIndex {
  border-top: 1px solid #f6f6f6;
  .el-header {
    background-color: #fff;
    color: #333;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    #Dialog {
      margin-right: 5px;
    }
  }

  .el-main {
    background-color: #e9eef3;
    color: #333;
  }
}
</style>
