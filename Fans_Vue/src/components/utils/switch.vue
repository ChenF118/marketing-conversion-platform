<template>
  <div id="TableSwitch">
    <!-- 动态增减表头 -->

    <el-dropdown trigger="click" :hide-on-click="false">
      <span class="el-dropdown-link">
        <i class="el-icon-setting"></i>
      </span>

      <el-dropdown-menu slot="dropdown">
        <el-scrollbar style="height: 300px">
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.zhanghao">账号</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.luodiye">落地页分组</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.url">访问url</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.beizhu">备注</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.jihua">计划</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.danyuan">单元</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.zhuizong">追踪关键词</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.paiming">展现排名</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.shangbao">数据上报</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.shijian">转化时间</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.shichang">转化时长</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.leixing">转化类型</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.mubiao">转化目标</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.ip">ip</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.diqu">地区</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.shebei">访问设备</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.liulan">浏览时长</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.bili">访问比例</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.xianshi">显示微信号</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.sousuo">搜索词</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.yinqing">搜索引擎</el-checkbox>
          </el-dropdown-item>
          <el-dropdown-item>
            <el-checkbox v-model="checkbox.laiyuan">来源链接</el-checkbox>
          </el-dropdown-item>
        </el-scrollbar>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "TableSwitch",
  created() {},
  data() {
    return {
      // 控制表格元素是否显示
      checkbox: {
        zhanghao: false,
        luodiye: true,
        url: true,
        beizhu: true,
        jihua: false,
        danyuan: false,
        zhuizong: false,
        paiming: false,
        shangbao: true,
        shijian: true,
        shichang: true,
        leixing: true,
        mubiao: true,
        ip: true,
        diqu: true,
        shebei: true,
        liulan: true,
        bili: true,
        xianshi: true,
        sousuo: false,
        yinqing: true,
        laiyuan: true,
      },
    };
  },
  watch: {
    checkbox: {
      handler() {
        this.$emit("changetableitem", this.checkbox);
      },
      deep: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.el-icon-setting {
  font-size: 20px;
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
</style>
