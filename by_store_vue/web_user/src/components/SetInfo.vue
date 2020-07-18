<template>
    <div class="content_center" style="display: flex">
        <el-container style="height: 818px; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <el-menu :default-active="this.$router.path"
                         :default-openeds="['1']"
                         class="el-menu-vertical-demo"
                         router>
                    <el-menu-item index="/index/home">首页</el-menu-item>
                    <el-submenu index="1">
                        <template slot="title">设置</template>
                        <el-menu-item index="/setInfo/personalDetail">个人信息</el-menu-item>
                        <el-menu-item index="/setInfo/address">收货地址</el-menu-item>
                        <el-menu-item index="1-2">账户安全</el-menu-item>
                        <el-menu-item index="2-3">我的尺码</el-menu-item>
                        <el-menu-item index="2-4-1">账号绑定</el-menu-item>
                        <el-menu-item index="3-2">健康档案</el-menu-item>
                        <el-menu-item index="3-3">分享绑定</el-menu-item>
                        <el-menu-item index="3-3">邮件订阅</el-menu-item>
                        <el-menu-item index="3-3">应用授权</el-menu-item>
                        <el-menu-item index="3-3">分享绑定</el-menu-item>
                        <el-menu-item index="3-3">快捷支付</el-menu-item>
                        <el-menu-item index="3-3">增票资质</el-menu-item>
                        <el-menu-item index="3-3">企业发票</el-menu-item>
                        <el-menu-item index="3-3">采购需求单</el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <el-container>
                <el-main>
                    <router-view/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "SetInfo"
    }
</script>

<style scoped>
    .setInfo-left > div > a {
        text-decoration: none;
        color: #333333;
        font-size: 8px;
    }

    .setInfo-left > div > a:hover {
        color: lightcoral;
    }

    .setInfo-left {
        background-color: #fff;
        height: 800px;
        padding-left: 20px;
        padding-top: 30px;
        margin-right: 5px;
        float: left;
    }

    .setInfo-left > div {
        margin-top: 15px;
    }

</style>