<template>
    <div>
        <div class="content_center" style="display: flex">
            <div class="user-content-left" style="flex: 1">
                <span style="font-weight: bold;font-size: 17px;">订单中心</span>
                <div><a @click="goto('/index/home')">首页</a></div>
                <div><a @click="goto('/index/order')">我的订单</a></div>
                <div><a @click="goto('/index/myComments')">订单评价</a></div>
                <hr>
                <span style="font-weight: bold;font-size: 17px;">我的关注</span>
                <div><a @click="goto('/index/collect')">关注商品</a></div>
                <div><a>关注店铺</a></div>
                <hr>
                <span style="font-weight: bold;font-size: 17px;">我的活动</span>
                <div><a @click="goto('/index/track')">我的足迹</a></div>
                <div><a @click="goto('/index/discount')">我的优惠券</a></div>
                <div><a @click="goto('/index/ewallet')">B悦钱包</a></div>
                <div><a @click="goto('/index/integral')">我的悦豆</a></div>
                <hr>
                <span style="font-weight: bold;font-size: 17px;">设置</span>
                <div><a @click="goto('/setInfo/personalDetail')">个人信息</a></div>
                <div><a @click="goto('/setInfo/address')">收货地址</a></div>
            </div>
            <div style="flex: 5">
                <router-view/>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                goto(path) {
                    this.$router.push({path})
                }
            }
        }
    }
</script>
<style scoped>
    .user-content-left {
        background-color: #fff;
        height: 800px;
        padding-left: 20px;
        padding-top: 50px;
        margin-right: 5px;
        float: left;
    }

    .user-content-left > div > a {
        text-decoration: none;
        color: #333333;
    }

    .user-content-left > div > a:hover {
        color: lightcoral;
    }


    .user-content-left > div {
        margin-top: 15px;
    }

    .user-content-left > div > a {
        font-size: 8px;
    }
</style>