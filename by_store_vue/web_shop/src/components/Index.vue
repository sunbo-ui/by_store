<template>
    <el-container>
        <el-header>
            <div style="float: left;display: flex;justify-content: center;align-items: center;margin-left: 10px;">
                <img :src="seller.pic" alt="" width="50">
                <span style="font-size: 24px;color: lightskyblue;margin-left: 20px;">商家管理后台</span>
            </div>
            <div style="float: right;">
                <span style="display: flex;justify-content: center;align-items: center;margin-left: 10px;">
                    <img :src="seller.pic" alt="" width="40">
                    <span style="font-size: 18px;color:lightslategrey;padding: 10px">{{username}}</span>
                    <a style="color:lightslategrey; cursor: pointer" @click="logout">退出</a>
                </span>
            </div>
        </el-header>
        <el-container>
            <el-aside width="270px">
                <div style="height:100px;display: flex;align-items: center;margin-left: 30px;">
                    <img :src="seller.pic" alt="" width="60">
                    <span style="font-size: 18px;color:lightslategrey;padding: 10px">{{username}}</span>
                </div>
                <el-menu :default-active="this.$router.path"
                         class="el-menu-vertical-demo"
                         router>
                    <el-menu-item index="/index/home"><i class="el-icon-s-home"></i>首页</el-menu-item>
                    <el-submenu index="2">
                        <template slot="title"><i class="el-icon-s-custom"></i>资料管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/password">修改密码</el-menu-item>
                            <el-menu-item index="/index/seller">商家资料</el-menu-item>
                            <el-menu-item index="/index/headerSetting">头像设置</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="3">
                        <template slot="title"><i class="el-icon-cold-drink"></i>商品管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/goodsEdit">添加商品</el-menu-item>
                            <el-menu-item index="/index/goods">商品管理</el-menu-item>
                            <el-menu-item index="/index/order">订单管理</el-menu-item>
                            <el-menu-item index="/index/discount">优惠券管理</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="4">
                        <template slot="title"><i class="el-icon-cold-drink"></i>评论管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/comments">评论管理</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="5">
                        <template slot="title"><i class="el-icon-cold-drink"></i>客服管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/communication">客户沟通</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <el-container>
                <el-main style="background-color:aliceblue;">
                    <router-view/>
                </el-main>
            </el-container>
        </el-container>
    </el-container>
</template>

<script>
    export default {
        name: "Index.vue",
        data(){
            return{
                username:'',
                seller:{},
            }
        },
        methods: {
            logout() {
                let vm = this;
                this.axios.post(vm.API.LOGIN_LOGOUT_URL).then(res => {
                    console.log(res);
                    if (res.data.code == 200) {
                        vm.$message.success(res.data.message);
                        vm.Storage.Session.remove("Authorization");
                        vm.$router.push('/login');
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            sellerId(){
                this.username =  this.Storage.Session.get("username");
                let vm = this;
                vm.axios.get(vm.API.SELLER_FINDBYID_URL + "?sellerId=" + this.username).then(res => {
                    vm.seller = res.data;
                })
            }
        },
        mounted() {
            this.sellerId()
        }
    }
</script>

<style scoped>
    .el-container {
        height: 100%;
    }

    img {
        border-radius: 50%;
    }

    .el-header {
        background-color: #fff;
        font-weight: bold;
        line-height: 60px;
    }

    .el-main {
        padding: 0px;
    }

    .el-aside {
        background-color: #fff;
    }

</style>