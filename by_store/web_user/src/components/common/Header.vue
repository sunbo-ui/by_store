<template>
    <div style="background-color: #fff;height: 192px;">
        <div style="background-color:#eaeaea; height: 30px">
            <div class="content_center">
                <div class="left">
                    <div v-if="!flag">
                        <span>您好! 请</span>
                        <span class="logout" style="margin: 0 10px 0 10px" @click="gotoLogin">登录</span>
                        <a class="logout" @click="gotoRegister">免费注册</a>
                    </div>
                    <div v-if="flag">
                        <span>欢迎</span>
                        <span style="color:#333333;font-weight: bold;margin-left: 10px;">{{username}}</span>
                        <span class="logout" @click="logout">退出</span>
                    </div>
                </div>
                <div class="right">
                    <ul>
                        <li @click="gotoOrder">我的订单 |</li>
                        <li @click="gotoHome">我的B悦 |</li>
                        <li>B悦会员 |</li>
                        <li>企业采购 |</li>
                        <li>客户服务 |</li>
                        <li>网站导航</li>
                    </ul>
                </div>
            </div>
        </div>
        <div>
            <div class="content_center">
                <div style="display: flex">
                    <div style="flex: 1;height: 100px;display: flex;align-items: center;">
                        <div>
                            <img src="../../assets/logo.png" width="60" height="60" alt="">
                        </div>
                        <div style="font-size: 35px; margin-left: 10px;">
                            B悦商城
                        </div>
                    </div>
                    <div style="flex: 3;display: flex;justify-content: center;align-items: center;margin-top: 20px;">
                        <div>
                            <el-input style="width: 550px" placeholder="请输入内容" @keyup.enter.native="searchQuery(search)"  v-model="search">
                                <el-button slot="append" icon="el-icon-search"   @click="searchQuery(search)">搜索
                                </el-button>
                            </el-input>
                        </div>
                    </div>
                    <div style="flex: 1;display: flex;align-items: center;justify-content: center">
                        <el-popover
                                placement="top-start"
                                style="margin-top: 21px;"
                                width="400"
                                trigger="hover">
                            <div v-for="cart in cartList">
                                <div>
                                    {{cart.sellerId}}
                                </div>
                                <div style="display: flex;margin-top: 5px;"  v-for="orderItem in cart.orderItemList">
                                    <div style="flex: 1" >
                                        <img :src="orderItem.picPath"  width="50" height="50" alt="">
                                    </div>
                                    <div style="flex: 2">
                                        <a style="color: #6dbfff" @click="gotoCart">{{orderItem.title}}</a>
                                    </div>
                                    <div style="flex: 1">
                                        <span style="color: red">￥{{orderItem.price}}</span>
                                        <a @click="deleteShop(orderItem.itemId)">删除</a>
                                    </div>
                                </div>
                                <hr>
                            </div>
                            <el-button slot="reference">
                                <el-badge :value="num" class="item">
                                    <i style="width: 20px" class="el-icon-shopping-cart-2"></i>
                                </el-badge>
                                <span>我的购物车</span>
                            </el-button>
                        </el-popover>
                        <img style="position: relative;right: -80px;top: 10px" src="../../assets/img/gifgoods.gif"
                             alt="">
                    </div>
                </div>
            </div>
        </div>
        <div class="content_center header-botton">
            <ul>
                <li>我的B悦</li>
                <li>账户设置</li>
                <li>消息</li>
                <li>闪购</li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            return {
                search: '',
                username: '',
                flag: false,
                cartList:[],
                num:0,
            }
        },
        methods: {
            searchQuery(id) {
                if (id == '') {
                    this.$message.info("请输入搜索内容")
                    return;
                }
                window.location.href = this.API.BASE_PORTAL_URL+"/index/search/"+id;
            },
            gotoLogin() {
                this.$router.push({path: '/login'}).catch(err => {
                    console.log(err)
                })
            },
            gotoRegister() {
                this.$router.push({path:'/register'});
            },
            gotoHome(){
                this.$router.push({path:'/index/home'});
            },
            gotoOrder() {
                this.$router.push({path:'/index/order'});
            },
            logout() {
                let vm = this;
                vm.axios.post(vm.API.LOGOUT).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message)
                        window.location.href = vm.API.BASE_SERVER_URL + "/logout/cas";
                        vm.Storage.Session.remove("username");
                        vm.cookieUtil.clearCookie('jsessionid');
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            },
            loadCartList() {
                let vm = this;
                vm.axios.post(vm.API.CART_FINDCARTLIST_URL + "?username=" + this.username).then(res => {
                    vm.cartList = res.data;
                    vm.cartList.forEach(function (item) {
                        vm.num = vm.num+item.orderItemList.length;
                    })
                    if (vm.cartList.length > 0) {
                        vm.flag = false;
                    } else {
                        vm.flag = true;
                    }
                    if (vm.cartList.length == 1 && vm.cartList[0].orderItemList.length == 0) {
                        vm.cartList = [];
                    }
                    if (vm.username !== '' && vm.username !== null && vm.username !== undefined
                        && vm.username !== 'undefined' && vm.username !== 'null') {
                        vm.flag = true;
                    } else {
                        vm.flag = false;
                    }
                })
            },
            gotoCart(){
                window.location.href=this.API.BASE_PORTAL_URL+"/index/cart"
            },
            deleteShop(id) {
                let vm = this;
                vm.axios.post(vm.API.DETELETE_CART_URL + "?itemId=" + id + "&username=" + this.username).then(res => {
                    vm.$message.success(res.data.message);
                    vm.loadCartList();
                })
            }
        },
        created() {
             this.username = this.Storage.Session.get("username");
            this.loadCartList();
        },
        watch:{
          $router(){
              this.loadCartList();
          }
        },
        filters: {
            moneyFormat(val) {
                return "￥" + val.toFixed(2);
            }
        }
    }
</script>

<style>
    .el-badge__content.is-fixed {
        position: absolute;
        top: -15px;
        right: -75px;
    }

    a {
        text-decoration: none;
        cursor: pointer;
    }
    a:hover{
        color: deeppink;
    }
    .left {
        float: left;
        font-size: 13px;
        padding-top: 5px;
    }

    .left > a {
        color: #28a3ef;
    }

    .right {
        float: right;
        padding-top: 5px;
    }

    ul {
        list-style: none;
    }

    .right > ul > li {
        float: left;
        padding-left: 5px;
        font-size: 13px;
        cursor: pointer;
    }
    .right > ul > li:hover{
        color: lightcoral;
    }

    .input-botton > ul > li {
        float: left;
        padding-left: 15px;
        font-size: 13px;
    }


    .header-botton > ul > li {
        float: left;
        font-size: 16px;
        display: block;
        height: 45px;
        width: 100px;
        color: #333333;
        display: flex;
        align-items: center;
    }

    .header-botton > ul > li:first-child {
        width: 200px;
        font-size: 20px;
        margin-left: 10px;
    }

    .logout {
        color: #333;
        cursor: pointer;
        margin-left: 10px;
    }

    .logout:hover {
        color: lightskyblue;
    }
</style>