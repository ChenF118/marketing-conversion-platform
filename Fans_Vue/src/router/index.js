import Vue from "vue";
import Router from "vue-router";

import ElementUI from "element-ui";

Vue.use(Router, ElementUI);

const originalPush = Router.prototype.push;
const originalReplace = Router.prototype.replace;
//push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};
//replace
Router.prototype.replace = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalReplace.call(this, location, onResolve, onReject);
  return originalReplace.call(this, location).catch((err) => err);
};

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "MyLogin",
    component: () => import("@/views/login/Login.vue"),
    meta: { title: "用户登录", icon: "MyLogin", noCache: true },
  },
  {
    path: "/register",
    name: "MyRegister",
    component: () => import("@/views/login/Register.vue"),
    meta: { title: "用户注册", icon: "MyRegister", noCache: true },
  },
  {
    path: "/inside",
    component: () => import("@/views/Outcontainer.vue"),
    children: [
      { path: "", redirect: "/inside/home" },
      {
        path: "home",
        name: "MyHome",
        component: () => import("@/views/Home.vue"),
        meta: { title: "我的桌面", icon: "MyHome", noCache: true },
      },
      {
        path: "code",
        component: () => import("@/views/aside/Code.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/code/index",
          },
          {
            path: "index",
            name: "MyCode",
            component: () => import("@/views/code/Index.vue"),
            meta: { title: "代码教程", icon: "MyCode", noCache: true },
          },
          {
            path: "common",
            name: "MyCodecommon",
            component: () => import("@/views/code/Common.vue"),
            meta: { title: "回传代码", icon: "MyCodecommon", noCache: true },
          },
        ],
      },
      {
        path: "landing",
        name: "MyLanding",
        component: () => import("@/views/landing/index.vue"),
        meta: { title: "落地页管理", icon: "MyLanding", noCache: true },
      },
      {
        path: "plugin",
        name: "MyPlugin",
        component: () => import("@/views/aside/Plugin.vue"),
        meta: { title: "插件管理", icon: "MyPlugin", noCache: true },
      },
      {
        path: "vchat",
        component: () => import("@/views/aside/Vchat.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/vchat/group",
          },
          {
            path: "group",
            name: "Vgroup",
            component: () => import("@/views/vchat/Vgroup.vue"),
            meta: { title: "微信分组管理", icon: "Vgroup", noCache: true },
          },
          {
            path: "number",
            name: "MyVchat2",
            component: () => import("@/views/vchat/Vnumber.vue"),
            meta: { title: "微信号管理", icon: "MyVchat", noCache: true },
          },
          {
            path: "landing",
            name: "MyVchat3",
            component: () => import("@/views/vchat/Vlanding.vue"),
            meta: { title: "落地页微信管理", icon: "MyVchat", noCache: true },
          },
        ],
      },
      {
        path: "copy",
        component: () => import("@/views/aside/Copy.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/copy/index",
          },
          {
            path: "index",
            name: "Copyindex",
            component: () => import("@/views/copy/Copyindex.vue"),
            meta: { title: "落地页统计管理", icon: "Copyindex", noCache: true },
          },
          {
            path: "landing",
            name: "CopyLanding",
            component: () => import("@/views/copy/CopyLanding.vue"),
            meta: {
              title: "复制落地页排行",
              icon: "CopyLanding",
              noCache: true,
            },
          },
          {
            path: "search",
            name: "CopySearch",
            component: () => import("@/views/copy/CopySearch.vue"),
            meta: {
              title: "复制搜索词统计",
              icon: "CopySearch",
              noCache: true,
            },
          },
          {
            path: "area",
            name: "CopyArea",
            component: () => import("@/views/copy/CopyArea.vue"),
            meta: { title: "复制地区统计", icon: "CopyArea", noCache: true },
          },
          {
            path: "source",
            name: "CopySource",
            component: () => import("@/views/copy/CopySource.vue"),
            meta: { title: "复制来源统计", icon: "CopySource", noCache: true },
          },
          {
            path: "track",
            name: "CopyTrack",
            component: () => import("@/views/copy/CopyTrack.vue"),
            meta: { title: "复制追踪统计", icon: "CopyTrack", noCache: true },
          },
          {
            path: "number",
            name: "CopyNumber",
            component: () => import("@/views/copy/CopyNumber.vue"),
            meta: {
              title: "复制微信号统计",
              icon: "CopyNumber",
              noCache: true,
            },
          },
          {
            path: "visitor",
            name: "CopyVisitor",
            component: () => import("@/views/copy/CopyVisitor.vue"),
            meta: { title: "复制实时访客", icon: "CopyVisitor", noCache: true },
          },
        ],
      },
      {
        path: "conversion",
        component: () => import("@/views/aside/Conversion.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/conversion/link",
          },
          {
            path: "link",
            name: "CvsLink",
            component: () => import("@/views/conversion/CvsLink.vue"),
            meta: { title: "统计链接管理", icon: "CvsLink", noCache: true },
          },
          {
            path: "type",
            name: "CvsType",
            component: () => import("@/views/conversion/CvsType.vue"),
            meta: { title: "转化类型管理", icon: "CvsType", noCache: true },
          },
          {
            path: "rate",
            name: "CvsRate",
            component: () => import("@/views/conversion/CvsRate.vue"),
            meta: { title: "转化率统计", icon: "CvsRate", noCache: true },
          },
          {
            path: "landing",
            name: "CvsLanding",
            component: () => import("@/views/conversion/CvsLanding.vue"),
            meta: {
              title: "转化落地页统计",
              icon: "CvsLanding",
              noCache: true,
            },
          },
          {
            path: "chat",
            name: "CvsChat",
            component: () => import("@/views/conversion/CvsChat.vue"),
            meta: { title: "转化微信号统计", icon: "CvsChat", noCache: true },
          },
          {
            path: "sync",
            name: "CvsSync",
            component: () => import("@/views/conversion/CvsSync.vue"),
            meta: { title: "实时转化统计", icon: "CvsSync", noCache: true },
          },
        ],
      },
      {
        path: "jump",
        component: () => import("@/views/aside/Jump.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/jump/index",
          },
          {
            path: "index",
            name: "JumpIndex",
            component: () => import("@/views/jump/Index.vue"),
            meta: { title: "落地页跳转设置", icon: "JumpIndex", noCache: true },
          },
        ],
      },
      {
        path: "state",
        component: () => import("@/views/aside/State.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/state/maneger",
          },
          {
            path: "maneger",
            name: "statemaneger",
            component: () => import("@/views/state/Maneger.vue"),
            meta: { title: "监控管理", icon: "statemaneger", noCache: true },
          },
          {
            path: "fault",
            name: "statefault",
            component: () => import("@/views/state/Fault.vue"),
            meta: { title: "故障记录", icon: "statefault", noCache: true },
          },
        ],
      },
      {
        path: "uniapp",
        component: () => import("@/views/aside/Uniapp.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/uniapp/myapp",
          },
          {
            path: "myapp",
            name: "myapp",
            component: () => import("@/views/uniapp/Myapp.vue"),
            meta: { title: "我的小程序", icon: "myapp", noCache: true },
          },
          {
            path: "fans",
            name: "myfans",
            component: () => import("@/views/uniapp/Myfans.vue"),
            meta: {
              title: "小程序真实到粉统计",
              icon: "myfans",
              noCache: true,
            },
          },
          {
            path: "public",
            name: "mypublic",
            component: () => import("@/views/uniapp/Mypublic.vue"),
            meta: {
              title: "小程序公众号关注统计",
              icon: "mypublic",
              noCache: true,
            },
          },
          {
            path: "cvs",
            name: "unicvs",
            component: () => import("@/views/uniapp/Unicvs.vue"),
            meta: {
              title: "小程序转化统计",
              icon: "unicvs",
              noCache: true,
            },
          },
          {
            path: "erweima",
            name: "erweima",
            component: () => import("@/views/uniapp/Erweima.vue"),
            meta: {
              title: "二维码转化统计",
              icon: "erweima",
              noCache: true,
            },
          },
          {
            path: "sync",
            name: "unisync",
            component: () => import("@/views/uniapp/Unisync.vue"),
            meta: {
              title: "小程序实时访问",
              icon: "unisync",
              noCache: true,
            },
          },
          {
            path: "detail",
            name: "unidetail",
            component: () => import("@/views/uniapp/Unidetail.vue"),
            meta: {
              title: "小程序实时转化详情",
              icon: "unidetail",
              noCache: true,
            },
          },
        ],
      },
      {
        path: "customer",
        component: () => import("@/views/aside/Customer.vue"),
        children: [
          {
            path: "",
            redirect: "/inside/state/index",
          },
          {
            path: "index",
            name: "mycustomer",
            component: () => import("@/views/custom/Vcustom.vue"),
            meta: { title: "我的客服", icon: "mycustomer", noCache: true },
          },
          {
            path: "fans",
            name: "Cfans",
            component: () => import("@/views/custom/Cfans.vue"),
            meta: { title: "客服真实到粉统计", icon: "Cfans", noCache: true },
          },
          {
            path: "erweima",
            name: "Cerweima",
            component: () => import("@/views/custom/Cerweima.vue"),
            meta: { title: "二维码转化统计", icon: "Cerweima", noCache: true },
          },
          {
            path: "sync",
            name: "Csync",
            component: () => import("@/views/custom/Csync.vue"),
            meta: { title: "客服实时访问", icon: "Csync", noCache: true },
          },
          {
            path: "detail",
            name: "Cdetail",
            component: () => import("@/views/custom/Cdetail.vue"),
            meta: { title: "客服实时访问", icon: "Cdetail", noCache: true },
          },
        ],
      },
      {
        path: "visitor/:id",
        name: "MyVisitor",
        component: () => import("@/views/aside/Visitor.vue"),
        meta: { title: "访客", icon: "MyVisitor", noCache: true },
      },
      {
        path: "vcount/:id",
        name: "MyVcount",
        component: () => import("@/views/aside/Vcount.vue"),
        meta: { title: "微信计数器", icon: "MyVcount", noCache: true },
      },
      {
        path: "personal",
        name: "MyPersonal",
        component: () => import("@/views/aside/Personal.vue"),
        meta: { title: "我的账号", icon: "MyPersonal", noCache: true },
        children: [
          { path: "", redirect: "/inside/personal/personal" },
          {
            path: "personal",
            name: "MyInformation",
            component: () => import("@/views/personal/Personal.vue"),
            meta: { title: "账号信息", icon: "MyInformation", noCache: true },
          },
        ],
      },
    ],
  },
];

const router = new Router({
  routes,
});

// 导航守卫
// 当token失效再点击路由跳转至login时会报一个错（Redirected when going from "xxx" to "xxx" via a navigation guard）但是不影响运行;
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem("Token");
  if (to.path == "/login" || to.path == "/register") {
    next();
  } else {
    if (token) {
      next();
    } else {
      ElementUI.Message({
        message: "登陆状态失效，请重新登录!",
        type: "warning",
      });
      next("/login");
    }
  }
});

export default router;
