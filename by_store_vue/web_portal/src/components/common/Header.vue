<template>
    <div style="background-color: #fff;height: 192px;border-bottom: 2px solid red">
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
                        <div style="font-size: 40px; margin-left: 10px;">
                            B悦商城
                        </div>
                    </div>
                    <div style="flex: 3;display: flex;justify-content: center;align-items: center;margin-top: 20px;">
                        <div>
                            <el-input style="width: 550px" placeholder="请输入内容" @keyup.enter.native="searchQuery(search)" v-model="search">
                                <el-button slot="append" icon="el-icon-search" @keyup.enter="searchQuery(search)"
                                            @click="searchQuery(search)">搜索
                                </el-button>
                            </el-input>
                            <div class="input-botton">
                                <ul>
                                    <li>运动清凉节</li>
                                    <li>数码家电</li>
                                    <li>每100-50</li>
                                    <li>家具直降</li>
                                    <li>AMD新品</li>
                                    <li>海尔巅峰</li>
                                    <li>清凉家装</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div style="flex: 1;display: flex;align-items: center;justify-content: center">
                        <el-popover
                                placement="top-start"
                                width="400"
                                trigger="hover">
                            <div v-if="index<1" v-for="(cart,index) in cartList">
                                <div>
                                    {{cart.sellerId}}
                                </div>
                                <div style="display: flex;margin-top: 5px;" v-if="index<2"
                                     v-for="(orderItem,index) in cart.orderItemList">
                                    <div style="flex: 1">
                                        <img :src="orderItem.picPath" width="50" height="50" alt="">
                                    </div>
                                    <div style="flex: 2">
                                        <a style="color: #6dbfff"
                                           @click="goto(orderItem.goodsId)">{{orderItem.title}}</a>
                                    </div>
                                    <div style="flex: 1">
                                        <span style="color: red">￥{{orderItem.price }}</span>
                                        <a @click="deleteShop(orderItem.itemId)">删除</a>
                                    </div>
                                </div>
                                <div id="more" @click="gotoCart"
                                     style="text-align: center;font-weight: bold;color: lightpink;" v-if="num=>2">
                                    ...加载更多...
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
                <li>全部商品分类</li>
                <li>限时抢购</li>
                <li>优惠券</li>
                <li>PLUS会员</li>
                <li>闪购</li>
                <li>拍卖</li>
                <li>海囤全球</li>
                <li>FM生鲜</li>
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
                cartList: [],
                num: 0,
            }
        },
        methods: {
            searchQuery(id) {
                if (id == '') {
                    id='*';
                }
                this.$router.push({
                    path: `/index/search/${id}`
                }).catch(err => {
                    err
                })
            },
            gotoHome(){
               window.location.href=this.API.BASE_USER_SERVER+"/index/home"
            },
            gotoLogin() {
                this.$router.push({path: '/login'}).catch(err => {
                    console.log(err)
                })
            },
            gotoRegister() {
                window.location.href = this.API.BASE_USER_SERVER + "/register"
            },
            logout() {
                let vm = this;
                vm.axios.post(vm.API.LOGOUT).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        window.location.href = vm.API.BASE_SERVER_URL + "/logout/cas";
                        vm.Storage.Session.remove("username");
                        vm.cookieUtil.clearCookie('jsessionid');
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            loadCartList() {
                let vm = this;
                vm.axios.post(vm.API.CART_FINDCARTLIST_URL + "?username=" + this.username).then(res => {
                    vm.cartList = res.data;
                    vm.cartList.forEach(function (item) {
                        vm.num = vm.num + item.orderItemList.length;
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
            gotoCart() {
                this.$router.push({path: '/index/cart'}).catch(reason => {
                    console.log(reason);
                })
            },
            deleteShop(id) {
                let vm = this;
                vm.axios.post(vm.API.DETELETE_CART_URL + "?itemId=" + id + "&username=" + this.username).then(res => {
                    vm.$message.success(res.data.message);
                    vm.loadCartList();
                })
            },
            goto(id) {
                this.$router.push({
                    path: `/index/detailShop/${id}`
                }).catch(err => {
                    err
                })
            },
            gotoOrder() {
                window.location.href = this.API.BASE_USER_SERVER + "/index/order"
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadCartList();
        },
        watch: { //监听属性的变化
            $route(to, from) {
                console.log(to.path)
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

    a:hover {
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

    .input-botton > ul > li:first-child {
        color: red;
    }

    .header-botton > ul > li {
        float: left;
        font-size: 16px;
        display: block;
        height: 45px;
        width: 100px;
        color: #333333;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .header-botton > ul > li:first-child {
        width: 210px;
        background-color: red;
        font-weight: bold;
        color: #fff;
    }

    .logout {
        color: #333;
        cursor: pointer;
        margin-left: 10px;
    }

    .logout:hover {
        color: lightskyblue;
    }

    #more:hover {
        cursor: pointer;
    }
</style>