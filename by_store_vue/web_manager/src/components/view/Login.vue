<template>
    <div id="login">
        <div class="border">
            <div class="yuan_fu">
                <div class="yuan"></div>
            </div>
            <div class="demo-input-suffix">
                <el-col>
                    <el-input
                            placeholder="请输入用户名"
                            prefix-icon="el-icon-user-solid"
                            v-model="username">
                    </el-input>
                    <el-input placeholder="请输入密码"
                              prefix-icon="el-icon-lock"
                              v-model="password" show-password></el-input>
                </el-col>
                <el-row>
                    <el-button type="primary" @click="login">登录</el-button>
                </el-row>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                username: '',
                password:'',
            }
        },
        methods:{
            login() {
                let vm = this;
                this.axios.post(vm.API.LOGIN_LOGIN_URL+'?username='+vm.username+'&password='+vm.password).then(res => {
                    console.log(res);
                    if(res.data.code == 200){
                        vm.$message.success(res.data.message);
                        vm.Storage.Session.set("Authorization", res.data.Authorization);
                        vm.Storage.Session.set("username", res.data.username);
                        vm.$router.push('/index/home');
                    }else {
                        vm.$message.error(res.data.message);
                    }
                })
            }
        },
        beforeCreate () {
            document.querySelector('body').setAttribute('style', 'background:#e0e0e0')
        },
        beforeDestroy () {
            document.querySelector('body').removeAttribute('style')
        }
    }
</script>

<style scoped>
    #login .border{
        background: #fff;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        width:500px;
        height: 300px;
        margin: 200px auto;
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    #login .yuan{
        width: 150px;
        height: 150px;
        border-radius: 50%;
        background:url("../../assets/logo.png");
        background-size: contain;
        background-color: gainsboro;
        position: absolute;
        left: 6px;
        top:6px;
        box-shadow: 0 2px 35px 0 #ffffff;
    }
    #login .yuan_fu{
        width: 160px;
        height: 160px;
        border-radius: 50%;
        background:#fff;
        position:absolute;
        border: 2px solid gainsboro;
        left: 160px;
        top: -80px;
        box-shadow: 0 2px 35px 0 #ffffff;
    }
    .el-input{
        margin-top: 20px;
    }
    .el-row{
       position: relative;
       top:50px;
       left:350px;
    }

</style>