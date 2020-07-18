import Vue from 'vue'
import App from './App.vue'
import jquery from 'jquery/dist/jquery.js'
import './plugins/element.js'
import "popper.js/dist/popper.js";
import 'bootstrap/dist/js/bootstrap.min.js'
import 'element-ui/lib/theme-chalk/index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import router from "./router/index.js";
import axios from 'axios'
import API from './util/APIUtil'
import Storage from "./util/StorageUtil";
import cookieUtil from "../../web_user/src/util/CookieUtil";
//定义全局变量
Vue.prototype.API = API;
Vue.prototype.axios=axios;
Vue.prototype.Storage=Storage;
Vue.prototype.cookieUtil = cookieUtil;
Vue.prototype.$ = jquery;

Vue.config.productionTip = false
axios.defaults.withCredentials = true
axios.create({
  withCredentials:true,
  timeout: 5000,
})

axios.interceptors.request.use(
    config => {
      if (cookieUtil.getCookie('jsessionid')) {
        config.headers['jsessionid'] = cookieUtil.getCookie('jsessionid');
      }
      return config;
    },
    error => {
      return Promise.reject(error);
    });
// 路由拦截
// 设置路由的前置守卫（路由跳转之前）
router.beforeEach((to, from, next) => {
    if (to.matched.length === 0) { // 如果未匹配到路由
        next('/404')
    } else {
        if(to.path.indexOf('/account') !=-1){
            let user= Storage.Session.get("username");
            if(!user){
                router.push({
                    path:'/login',
                    name:'Login',
                })
            }
        }
        next() // 如果匹配到正确跳转
    }

});
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
