import Vue from 'vue'
import Brand from "../components/view/Brand";
import Index from "../components/view/Index";
import Goods from "../components/view/Goods";
import Router from 'vue-router'
import Login from "../components/view/Login";
import Content from "../components/view/Content";
import Content_category from "../components/view/Content_category";
import Home from "../components/view/Home";
import Item_cat from "../components/view/Item_cat";
import Seller from "../components/view/Seller";
import Seller_auditing from "../components/view/Seller_auditing";
import Specification from "../components/view/Specification";
import Type_template from "../components/view/Type_template";
import Comments from "../components/view/Comments";
import CommentsDetail from "../components/view/CommentsDetail";
//引入页面

Vue.use(Router)

const router = new Router({
    mode: 'history',
    routes: [
        {path:'/login',component:Login,name:'Login'},
        {path: '/', redirect: '/index/home'},
        {
            name: "Index",
            path: "/index",
            component: Index,
            children:[
                {path: 'brand', component: Brand, name: 'Brand'},
                {path: 'goods', component: Goods, name: 'Goods'},
                {path:'content',component:Content,name:'Content'},
                {path:'contentCategory',component:Content_category,name:'Content_category'},
                {path:'home',component:Home,name:'Home'},
                {path:'itemCat',component:Item_cat,name:'Item_cat'},
                {path:'seller',component:Seller,name:'Seller'},
                {path:'sellerAuditing',component:Seller_auditing,name:'Seller_auditing'},
                {path:'specification',component:Specification,name:'Specification'},
                {path:'typeTemplate',component:Type_template,name:'Type_template'},
                {path:'comments',component:Comments,name:'Comments'},
                {path:'commentsDetail/:id',component:CommentsDetail,name:'CommentsDetail'},
            ]
        }
    ],
});

export default router;