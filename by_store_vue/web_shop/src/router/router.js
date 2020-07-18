import Vue from 'vue'
import Router from 'vue-router'
import Login from "../components/Login";
import Register from "../components/Register";
import GoodsEdit from "../components/GoodsEdit";
import Goods from "../components/Goods";
import Password from "../components/Password";
import Seller from "../components/Seller";
import Index from "../components/Index";
import Home from "../components/Home";
import Order from "../components/Order";
import OrderItem from "../components/OrderItem";
import CommentsDetail from "../components/CommentsDetail";
import Comments from "../components/Comments";
import Discount from "../components/Discount";
import Communication from "../components/Communication";
import HeaderSetting from "../components/HeaderSetting";
//引入页面

Vue.use(Router)

const router = new Router({
    mode: 'history',
    routes: [
        {path: '/login', component: Login, name: 'Login'},
        {path: '/register', component: Register, name: "Register"},
        {path: '/', redirect: '/index/home'},
        {path: '/index', redirect: '/index/home'},
        {
            name: "Index",
            path: "/index",
            component: Index,
            children: [
                {path: 'home', component: Home, name: 'Home'},
                {path: 'goodsEdit', component: GoodsEdit, name: 'GoodsEdit'},
                {path: 'goodsEdit/:id', component: GoodsEdit, name: 'GoodsEdit'},
                {path: 'goods', component: Goods, name: 'Goods'},
                {path: 'password', component: Password, name: 'Password'},
                {path: 'seller', component: Seller, name: 'Seller'},
                {path: 'order', component: Order, name: 'Order'},
                {path: 'orderItem/:id', component: OrderItem, name: 'OrderItem'},
                {path: 'commentsDetail/:id', component: CommentsDetail, name: 'CommentsDetail'},
                {path: 'comments', component: Comments, name: 'Comments'},
                {path: 'discount', component: Discount, name: 'Discount'},
                {path: 'communication', component: Communication, name: 'Communication'},
                {path: 'headerSetting', component: HeaderSetting, name: 'HeaderSetting'},
            ]
        }
    ],
});


export default router;