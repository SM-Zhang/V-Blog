import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import App from "./App.vue";
import router from "./router";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// import Hljs from 'highlight.js';
import 'highlight.js/styles/dark.css';
// import marked from 'marked'
// import Highlight from '@/utils/highlightUtil'
import VCharts from 'v-charts'
import store from './store/index';
import './permission'

Vue.use(ElementUI);
Vue.use(mavonEditor);
// Vue.use(Highlight)
Vue.use(VCharts)
// Vue.use(marked)
Vue.config.productionTip = process.env.NODE_ENV === 'production';
console.log("Vue.config.productionTip === ")
console.log(process.env.NODE_ENV)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
