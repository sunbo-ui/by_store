import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from "../components/Index";
import Login from "../components/Login";
import Register from "../components/Register";
import SetInfo from "../components/SetInfo";
import Address from "../components/Address";
import Error from "../components/Error";
import Home from "../components/Home";
import Order from "../components/Order";
import OrderDetail from "../components/OrderDetail";
import MyComments from "../components/MyComments";
import CommentsShop from "../components/CommentsShop";
import Collect from "../components/Collect";
import Track from "../components/Track";
import Ewallet from "../components/Ewallet";
import PersonalDetail from "../components/PersonalDetail";
import Discount from "../components/Discount";
import Integral from "../components/Integral";


Vue.use(VueRouter)

const routes = [
    {path: '/', redirect: '/index/home'},
    {path: '/index', redirect: '/index/home'},
    {path: '/login', component: Login, name: 'Login'},
    {path: '/register', component: Register, name: 'Register'},
    {path: '/commentsShop/:orderId', component: CommentsShop, name: 'CommentsShop'},
    {path: '/404', component: Error, name: 'Error'},
    {
        path: '/index',
        component: Index,
        name: 'Index',
        children: [
            {path: 'home', component: Home, name: 'Home'},
            {path: 'order', component: Order, name: 'Order'},
            {path: 'orderDetail/:id/:status', component: OrderDetail, name: 'OrderDetail'},
            {path:'myComments',component:MyComments,name:'MyComments'},
            {path:'collect',component:Collect,name:'Collect'},
            {path:'track',component:Track,name:'Track'},
            {path:'ewallet',component:Ewallet,name:'Ewallet'},
            {path:'discount',component:Discount,name:'Discount'},
            {path:'integral',component:Integral,name:'Integral'},
        ]
    },
    {
        path: '/setInfo',
        component: SetInfo,
        name: 'SetInfo',
        children: [
            {path: 'address', component: Address, name: 'Address'},
            {path: 'personalDetail', component: PersonalDetail, name: 'PersonalDetail'},
        ]
    },

]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router
