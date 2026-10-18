import Vue from "vue";
import App from "./App.vue";

import router from "./router";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

import store from "./store/index";
import MyDialog from "./components/Dialog.vue";

Vue.config.productionTip = false;

Vue.use(ElementUI);

// 全局组件
Vue.component("MyDialog", MyDialog);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
