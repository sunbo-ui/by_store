import Vue from "vue";
import App from "./App.vue";
import jquery from 'jquery/dist/jquery.js'
import './router/element.js'
import "popper.js/dist/popper.js";
import 'bootstrap/dist/js/bootstrap.min.js'
import 'bootstrap/dist/css/bootstrap.min.css'
import Storage from './util/StorageUtil'
import axios from 'axios'
import API from './util/APIUtil'
import router from "./router/router";

Vue.prototype.$ = jquery;
Vue.prototype.axios=axios;
Vue.prototype.Storage=Storage;

Vue.prototype.API=API;
Vue.config.productionTip = false;

axios.create({
    withCredentials:true,
    timeout: 5000,
})

axios.interceptors.request.use(
    config => {
        if (Storage.Session.get('Authorization')) {
            config.headers['Authorization'] = Storage.Session.get('Authorization');
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    });

// 路由拦截
// 设置路由的前置守卫（路由跳转之前）
router.beforeEach((to, from, next) => {
    const Authorization =Storage.Session.get("Authorization");
    if (to.name === 'Login' || to.name === 'Register' ) {
        next();
    } else {
        if (Authorization !== null) {
                next();
        } else {
            next({path:"/login"})
        }
    }
});
new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
