<template>
  <div id="aside">
    <el-scrollbar style="height: 100%">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-vertical-demo"
        :collapse="isCollapse"
        background-color="#28333e"
        text-color="#fff"
        active-text-color="#fff"
        router
        unique-opened
      >
        <el-submenu index="/inside/code">
          <template slot="title">
            <i class="el-icon-cpu"></i>
            <span>js代码和教程</span>
          </template>
          <el-menu-item index="/inside/code/index">js代码安装</el-menu-item>
          <el-menu-item index="/inside/code/common">回传代码</el-menu-item>
          <el-menu-item index="">API接口</el-menu-item>
          <el-menu-item index="">js代码托管</el-menu-item>
          <el-menu-item index="">常见问题</el-menu-item>
        </el-submenu>

        <el-menu-item index="/inside/landing">
          <div>
            <i class="el-icon-collection-tag"></i>
            <span slot="title">落地页管理</span>
          </div>
        </el-menu-item>

        <el-menu-item index="/inside/plugin">
          <div>
            <i class="el-icon-cpu"></i>
            <span slot="title">插件管理</span>
          </div>
        </el-menu-item>

        <el-submenu index="/inside/vchat">
          <template slot="title">
            <i class="el-icon-chat-line-round"></i>
            <span>微信在线管理</span>
          </template>
          <el-menu-item index="/inside/vchat/group"
            >微信号分组管理</el-menu-item
          >
          <el-menu-item index="/inside/vchat/number">微信号管理</el-menu-item>
          <el-menu-item index="/inside/vchat/landing"
            >落地页微信号管理</el-menu-item
          >
        </el-submenu>

        <el-submenu index="/inside/copy">
          <template slot="title">
            <i class="el-icon-pie-chart"></i>
            <span>复制统计</span>
          </template>
          <el-menu-item index="/inside/copy/index">落地页统计管理</el-menu-item>
          <el-menu-item index="/inside/copy/landing"
            >复制落地页排行</el-menu-item
          >
          <el-menu-item index="/inside/copy/search"
            >复制搜索词统计</el-menu-item
          >
          <el-menu-item index="/inside/copy/area">复制地区统计</el-menu-item>
          <el-menu-item index="/inside/copy/source">复制来源统计</el-menu-item>
          <el-menu-item index="/inside/copy/track">复制追踪统计</el-menu-item>
          <el-menu-item index="/inside/copy/number"
            >复制微信号统计</el-menu-item
          >
          <el-menu-item index="/inside/copy/visitor">复制实时访客</el-menu-item>
        </el-submenu>

        <el-submenu index="/inside/conversion">
          <template slot="title">
            <i class="el-icon-finished"></i>
            <span>行为转化统计</span>
          </template>
          <el-menu-item index="/inside/conversion/link"
            >统计链接管理</el-menu-item
          >
          <el-menu-item index="/inside/conversion/type"
            >转化类型管理</el-menu-item
          >
          <el-menu-item index="/inside/conversion/rate"
            >转化率统计</el-menu-item
          >
          <el-menu-item index="/inside/conversion/landing"
            >转化落地页统计</el-menu-item
          >
          <el-menu-item index="/inside/conversion/chat"
            >转化微信号统计</el-menu-item
          >
          <el-menu-item index="/inside/conversion/sync"
            >实时转化详情</el-menu-item
          >
        </el-submenu>

        <el-submenu index="/inside/jump">
          <template slot="title">
            <i class="el-icon-edit-outline"></i>
            <span>落地页跳转管理</span>
          </template>
          <el-menu-item index="/inside/jump/index">落地页跳转设置</el-menu-item>
        </el-submenu>

        <el-submenu index="/inside/state">
          <template slot="title">
            <i class="el-icon-data-line"></i>
            <span>网页状态监控</span>
          </template>
          <el-menu-item index="/inside/state/maneger">监控管理</el-menu-item>
          <el-menu-item index="/inside/state/fault">故障记录</el-menu-item>
        </el-submenu>

        <el-submenu index="/inside/uniapp">
          <template slot="title">
            <i class="el-icon-star-off"></i>
            <span>微信小程序加粉</span>
          </template>
          <el-menu-item index="/inside/uniapp/myapp">我的小程序</el-menu-item>
          <el-menu-item index="/inside/uniapp/fans"
            >小程序真实到粉统计</el-menu-item
          >
          <el-menu-item index="/inside/uniapp/public"
            >小程序公众号关注统计</el-menu-item
          >
          <el-menu-item index="/inside/uniapp/cvs">小程序转化统计</el-menu-item>
          <el-menu-item index="/inside/uniapp/erweima"
            >二维码转化统计</el-menu-item
          >
          <el-menu-item index="/inside/uniapp/sync"
            >小程序实时访问</el-menu-item
          >
          <el-menu-item index="/inside/uniapp/detail"
            >小程序实时转化详情计</el-menu-item
          >
        </el-submenu>

        <el-submenu index="/inside/customer">
          <template slot="title">
            <i class="el-icon-headset"></i>
            <span>微信客服加粉</span>
          </template>
          <el-menu-item index="/inside/customer/index">我的客服</el-menu-item>
          <el-menu-item index="/inside/customer/fans"
            >客服真实到粉统计</el-menu-item
          >
          <el-menu-item index="/inside/customer/erweima"
            >二维码转化统计</el-menu-item
          >
          <el-menu-item index="/inside/customer/sync"
            >客服实时访问</el-menu-item
          >
          <el-menu-item index="/inside/customer/detail"
            >客服实时转化详情</el-menu-item
          >
        </el-submenu>

        <el-submenu index="/inside/visitor">
          <template slot="title">
            <i class="el-icon-data-analysis"></i>
            <span>实时访客</span>
          </template>
          <el-menu-item index="/inside/visitor/:1">实时访客</el-menu-item>
          <el-menu-item index="/inside/visitor/:2">IP统计分析</el-menu-item>
        </el-submenu>

        <el-submenu index="/inside/vcount">
          <template slot="title">
            <i class="el-icon-s-data"></i>
            <span>微信计数器</span>
          </template>
          <el-menu-item index="/inside/vcount/:1">APP下载</el-menu-item>
          <el-menu-item index="/inside/vcount/:2">已绑微信号</el-menu-item>
          <el-menu-item index="/inside/vcount/:3">实时进粉详情</el-menu-item>
          <el-menu-item index="/inside/vcount/:4">聊天记录查看</el-menu-item>
        </el-submenu>

        <el-submenu index="/inside/personal">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span>我的账号</span>
          </template>
          <el-menu-item index="/inside/personal/personal"
            >账号信息</el-menu-item
          >
          <el-menu-item index="">子账号管理</el-menu-item>
          <el-menu-item index="">升级/续费</el-menu-item>
          <el-menu-item index="">邀请注册返现</el-menu-item>
          <el-menu-item index="">问题反馈</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import Bus from "@/utils/event.js";
export default {
  name: "Aside",
  created() {
    this.activeIndex = this.$route.path;
    Bus.$on("share", (val) => {
      this.isCollapse = val;
    });
  },
  data() {
    return {
      isCollapse: false,
      activeIndex: "",
    };
  },
  watch: {
    $route(route) {
      this.activeIndex = route.path;
    },
  },
  components: {},
  methods: {
    // 不需要
    /* handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }, */
  },
};
</script>

<style lang="scss" scoped>
#aside {
  height: 100%;
  background-color: #28333e;
  color: #fff;
  padding-top: 10px;
  overflow: hidden;

  .el-menu {
    height: 100%;
    border: 0;
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
  .el-menu-item {
    width: 100%;
    height: 36px;
    line-height: 36px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
}
</style>
<style>
.el-menu .el-submenu__title {
  height: 36px;
  line-height: 36px;
  font-size: 14px;
  text-align: left;
}
.el-menu .el-submenu .el-menu-item {
  width: 100%;
  height: 36px;
  line-height: 36px;
  padding: 0 45px;
  min-width: 200px;
  text-indent: 5px;
}
.el-menu-item:hover {
  background: #009688 !important;
}
.el-menu-item.is-active {
  background: #009688 !important;
}
.el-submenu__title.is-active {
  background: #009688 !important;
}
.el-scrollbar .el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
