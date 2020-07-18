import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../components/Index'
import Home from "../components/Home";
import Login from "../components/Login";
import Search from "../components/Search";
import ErrorSearch from "../components/common/ErrorSearch";
import DetailShop from "../components/DetailShop";
import Cart from "../components/Cart";
import Error from "../components/Error";
import Order from "../components/Order";
import Pay from "../components/Pay";
import PaySuccess from "../components/PaySuccess";
import PayFail from "../components/PayFail";
import Communication from "../components/Communication";

Vue.use(VueRouter)

const routes = [
    {path:'/login',component:Login,name:'Login'},
    {path:'/404',component:Error,name:'Error'},
    {path: '/', redirect: '/index/home'},
    {
        name: "Index",
        path: "/index",
        component: Index,
        children:[
            {path:'home',component:Home,name:'Home'},
            {path:'search',component:Search,name:'Search'},
            {path:'search/:id',component:Search,name:'Search'},
            {path:'errorSearch',component:ErrorSearch,name:'ErrorSearch'},
            {path:'detailShop/:id',component:DetailShop,name:'DetailShop'},
            {path:'communication',component:Communication,name:'Communication'},
            {path:'cart',component:Cart,name:'Cart'},
            {path:'account/order',component:Order,name:'Order'},
            {path:'account/pay',component:Pay,name:'Pay'},
            {path:'account/paySuccess',component:PaySuccess,name:'PaySuccess'},
            {path:'account/paySuccess/:money',component:PaySuccess,name:'PaySuccess'},
            {path:'account/payFail',component:PayFail,name:'PayFail'},

        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})
export default router
