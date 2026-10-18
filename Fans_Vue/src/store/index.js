import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    /* tagList: JSON.parse(sessionStorage.getItem("paths")) || [
      { path: "/inside/home", meta: { title: "我的桌面" } },
    ], */
    tagList: [{ path: "/inside/home", meta: { title: "我的桌面" } }],
    // 落地页分组
    grouplist: JSON.parse(localStorage.getItem("grouplist")) || [],
    // 落地页链接
    landinglist: JSON.parse(sessionStorage.getItem("landinglist")) || [],
    // 微信号分组
    wxgrouplist: JSON.parse(localStorage.getItem("wxgrouplist")) || [],

    // 身份
    IDcode: sessionStorage.getItem("IDcode") || "",
    userinfo: JSON.parse(localStorage.getItem("userinfo")) || null,
  },
  mutations: {
    // 组合标签
    ADD_TAG(state, paths) {
      const flag = state.tagList.some((item) => item.path === paths.path);
      if (flag) {
        return;
      } else {
        state.tagList.push(Object.assign({}, paths));
        /*  sessionStorage.setItem("paths", JSON.stringify(state.tagList)); */
      }
    },

    REMOVE_TAG(state, paths) {
      for (var i = 0; i < state.tagList.length; i++) {
        if (state.tagList[i].path == paths.path) {
          state.tagList.splice(i, 1);
          /*  sessionStorage.setItem("paths", JSON.stringify(state.tagList)); */
        }
      }
    },
    // 更新落地页分组
    GET_GROUP(state, list) {
      state.grouplist = list;
    },
    // 更新落地页
    GET_LANDING(state, list) {
      state.landinglist = list;
    },
    // 更新微信分组
    GET_WXGROUP(state, list) {
      state.wxgrouplist = list;
    },

    // 添加身份码
    ADD_IDCODE(state, val) {
      state.IDcode = val;
    },
    // 用户信息
    ADD_INFO(state, msg) {
      state.userinfo = msg;
    },
  },
  actions: {
    // 组合标签
    asyncAddTag(context, view) {
      context.commit("ADD_TAG", view);
    },
    // 移除路由信息
    removeTag(context, view) {
      context.commit("REMOVE_TAG", view);
    },
    // 更新落地页分组
    getgroup(context, val) {
      context.commit("GET_GROUP", val);
    },
    // 更新落地页
    getlanding(context, val) {
      context.commit("GET_LANDING", val);
    },
    // 更新微信分组
    getwxgroup(context, val) {
      context.commit("GET_WXGROUP", val);
    },
    // 添加身份码
    addIdcode(context, code) {
      context.commit("ADD_IDCODE", code);
    },
    // 用户信息
    addInfo(context, message) {
      context.commit("ADD_INFO", message);
    },
  },
  getters: {},
});

export default store;
