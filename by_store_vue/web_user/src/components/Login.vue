<template>
    <div>
<!--        <div style="height: 100px;background-color: #fff;">-->
<!--            <div class="content_center pt-2" style="display:flex;align-items: center">-->
<!--                <img src="../assets/logo.png" width="80" height="80" alt="">-->
<!--                <img src="../assets/img/welcome.png" alt="" width="120" height="50"-->
<!--                     style="float:left; margin-left: 10px;">-->
<!--            </div>-->
<!--            <div style="background-color: #E93854;height: 487px">-->
<!--                <div class="content_center loginBgc">-->
<!--                    <div style="width: 420px;height: 413px;float: right;margin-top: 40px;">-->
<!--                        <el-tabs type="border-card" v-model="activeName">-->
<!--                            <el-tab-pane name="first" label="扫码登录">-->
<!--                                <div style="display: flex;align-items: center;justify-content: center">-->
<!--                                    <div style="padding-top: 30px;border: 1px solid #ccc;height: 313px;width: 380px;text-align: center">-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </el-tab-pane>-->
<!--                            <el-tab-pane name="second" label="账号登录">-->
<!--                                <div style="padding-top: 30px;border: 1px solid #ccc;height: 313px;width: 380px;">-->
<!--                                    <div style="text-align: center">-->
<!--                                        <el-input placeholder="请输入用户名" v-model="username" style="width: 320px;">-->
<!--                                            <template slot="prepend">-->
<!--                                                <i class="el-icon-user-solid"></i>-->
<!--                                            </template>-->
<!--                                        </el-input>-->
<!--                                        <el-input placeholder="请输入密码" v-model="password" show-password-->
<!--                                                  style="width: 320px;margin-top: 20px;">-->
<!--                                            <template slot="prepend">-->
<!--                                                <i class="el-icon-lock"></i>-->
<!--                                            </template>-->
<!--                                        </el-input>-->
<!--                                    </div>-->
<!--                                    <div>-->
<!--                                        <el-checkbox style="margin-left: 30px;margin-top: 20px;" v-model="checked">-->
<!--                                            自动登录-->
<!--                                        </el-checkbox>-->
<!--                                        <span style="margin-right: 30px;margin-top: 20px;float: right">忘记密码?</span>-->
<!--                                    </div>-->
<!--                                    <div style="margin-left: 30px;">-->
<!--                                        <el-button @click="login()" style="width: 320px;margin-top: 20px;"-->
<!--                                                   type="danger">立即登录-->
<!--                                        </el-button>-->
<!--                                    </div>-->
<!--                                    <div>-->

<!--                                        <a @click="goto"-->
<!--                                           style="margin-right: 30px;margin-top: 20px;float: right">马上注册</a>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </el-tab-pane>-->
<!--                        </el-tabs>-->
<!--                    </div>-->
<!--                </div>-->
<!--            </div>-->
<!--            <div class="content_center">-->
<!--                <div style="width: 1170px;height: 100px;float: left;text-align: center;margin-top: 10px;">-->
<!--                    <div class="footer-footer" style="width: 1170px;height: 30px;display:flex;justify-content: center">-->
<!--                        <ul>-->
<!--                            <li>-->
<!--                                关于我们-->
<!--                                <span style="margin-left: 10px;color: #cccccc">|</span>-->
<!--                            </li>-->
<!--                            <li>-->
<!--                                联系我们 联系客服-->
<!--                                <span style="margin-left: 10px;color: #cccccc">|</span>-->
<!--                            </li>-->
<!--                            <li>合作招商-->
<!--                                <span style="margin-left: 10px;color: #cccccc">|</span>-->
<!--                            </li>-->
<!--                            <li>商家帮助 <span style="margin-left: 10px;color: #cccccc">|</span></li>-->
<!--                            <li>营销中心 <span style="margin-left: 10px;color: #cccccc">|</span></li>-->
<!--                            <li>友情链接 <span style="margin-left: 10px;color: #cccccc">|</span></li>-->
<!--                            <li>销售联盟 <span style="margin-left: 10px;color: #cccccc">|</span></li>-->
<!--                            <li>友情链接 <span style="margin-left: 10px;color: #cccccc">|</span></li>-->
<!--                            <li>隐私政策</li>-->
<!--                        </ul>-->
<!--                    </div>-->
<!--                    <p>京公网安备 1****002000088号|京ICP证0*****9号|互联网药品信息服务资格证编号(京)-经营性-2014-0008|新出发京零 字第大1*****2号</p>-->
<!--                    <p>京ICP备0*****1号京公网安备1***********2</p>-->
<!--                </div>-->
<!--            </div>-->
<!--        </div>-->
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                activeName: 'second',
                checked: true,
                username: '',
                password: '',
            }
        },
        methods: {
            goto() {
                this.$router.push({path: '/register'})
            },
            //获取地址栏url后的参数值
            getUrlParam(key) {
                let href = window.location.href;
                let url = href.split("?");
                if (url.length <= 1) {
                    return "";
                }
                let params = url[1].split("&");

                for (let i = 0; i < params.length; i++) {
                    let param = params[i].split("=");
                    if (key == param[0]) {
                        return param[1];
                    }
                }
            }
        },
        mounted() {
            let jsessionid =  this.getUrlParam("jsessionid");
            console.log(jsessionid)
            if(jsessionid !== '' && jsessionid!== undefined  && jsessionid !== 'null'){
                this.cookieUtil.setCookie('jsessionid',jsessionid)
                let vm = this;
                this.axios.post(vm.API.BASE_SERVER_URL+"/user/login").then(res=>{
                    vm.Storage.Session.set("username",res.data.message);
                    vm.$router.go(-1)
                })
            }else{
                window.location.href = this.API.BASE_SERVER_URL+"/username"
            }
        },

    }
</script>

<style scoped>
    .loginBgc {
        background: url('../assets/img/loginbg.png') no-repeat;
        height: 487px;
    }

    ul {
        list-style: none;
    }

    .footer-footer > ul > li {
        float: left;
        margin-left: 30px;
    }

    p {
        float: left;
        width: 1170px;
    }
</style>