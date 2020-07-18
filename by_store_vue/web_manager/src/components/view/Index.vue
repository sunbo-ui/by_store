<template>
    <el-container>
        <el-header>
            <div style="float: left;display: flex;justify-content: center;align-items: center;margin-left: 10px;">
                <img src="../../assets/logo.png" alt="" width="50">
                <span style="font-size: 24px;color: lightskyblue;margin-left: 20px;">运营商后台</span>
            </div>
            <div  style="float: right;">
                <span style="display: flex;justify-content: center;align-items: center;margin-left: 10px;">
                    <img src="../../assets/logo.png" alt="" width="30">
                    <span style="font-size: 18px;color:lightslategrey;padding: 10px">{{username}}</span>
                    <a style="color:lightslategrey; cursor: pointer" @click="logout">退出</a>
                </span>
            </div>

        </el-header>
        <el-container>
            <el-aside width="270px">
                <div style="height:100px;display: flex;align-items: center;margin-left: 10px;">
                    <img src="../../assets/logo.png" alt="" width="60">
                    <span style="font-size: 18px;color:lightslategrey;padding: 10px">{{username}}</span>
                </div>
                <el-menu :default-active="this.$router.path"
                         class="el-menu-vertical-demo"
                         router>
                    <el-menu-item index="/index/home"><i class="el-icon-s-home"></i>首页</el-menu-item>
                    <el-submenu index="2">
                        <template slot="title"><i class="el-icon-s-custom"></i>商家管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/sellerAuditing">商家审核</el-menu-item>
                            <el-menu-item index="/index/seller">商家管理</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="3">
                        <template slot="title"><i class="el-icon-cold-drink"></i>商品管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/brand">品牌管理</el-menu-item>
                            <el-menu-item index="/index/specification">规格管理</el-menu-item>
                            <el-menu-item index="/index/typeTemplate">模板管理</el-menu-item>
                            <el-menu-item index="/index/itemCat">分类管理</el-menu-item>
                            <el-menu-item index="/index/goods">商品审核</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="4">
                        <template slot="title"><i class="el-icon-data-analysis"></i>广告管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/contentCategory">广告分类管理</el-menu-item>
                            <el-menu-item index="/index/content">广告管理</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="5">
                        <template slot="title"><i class="el-icon-s-ticket"></i>订单管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="/index/comments">评论管理</el-menu-item>
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
            }
        },
        mounted() {
            this.username = this.Storage.Session.get("username")
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
    .el-main{
        padding: 0px;
    }

    .el-aside {
        background-color: #fff;
    }

</style>