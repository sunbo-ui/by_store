<template>
    <div>
        <div style="height: 100px;background-color: #fff;">
            <div class="content_center pt-2" style="display:flex;align-items: center">
                <img src="../assets/logo.png" width="80" height="80" alt="">
                <div class="manage-title" style="color: dimgrey; font-size: 25px; margin-left: 10px;">用户注册</div>
            </div>
            <div style="height: 700px;background-color: #fff;">
                <div class="content_center" style="border:1px solid #ccc;height: 560px">
                    <div style="background-color: #cccccc;height: 43px;padding-top: 6px;">
                        <span style="float: left;font-weight: bold;font-size: 20px;margin-left: 15px;">注册新用户</span>
                        <span style="float: right;margin-right: 15px;margin-top: 5px;">我有账户,去<a style="color: blue" @click="goto">登录</a></span>
                    </div>
                    <div style="margin-top: 20px;display: flex;justify-content: center">
                        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                                 class="demo-ruleForm">
                            <el-form-item style="width: 500px;" label="用户名" prop="username">
                                <el-input v-model="ruleForm.username"></el-input>
                            </el-form-item>
                            <el-form-item style="width: 500px;" label="密码" prop="password">
                                <el-input type="password" v-model="ruleForm.password" ></el-input>
                            </el-form-item>
                            <el-form-item style="width: 500px;" label="确认密码" prop="checkPass">
                                <el-input type="password" v-model="ruleForm.checkPass" ></el-input>
                            </el-form-item>
                            <el-form-item style="width: 500px;" label="手机号码" prop="phone">
                                <el-input v-model="ruleForm.phone" ></el-input>
                            </el-form-item>
                            <el-form-item style="width: 500px;"  label="验证码" prop="smsCode">
                                <el-input  v-model="ruleForm.smsCode" ></el-input>
                                <el-button style="float:right;margin-top: 20px;" href="" @click="sendCode()">获取验证码</el-button>
                            </el-form-item>
                            <el-form-item>
                                <el-button style="width: 400px;margin-top: 10px;" type="danger" @click="register('ruleForm')">提交</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
            <div class="content_center">
                <div style="width: 1170px;height: 100px;float: left;text-align: center;margin-top: 10px;">
                    <div class="footer-footer" style="width: 1170px;height: 30px;display:flex;justify-content: center">
                        <ul>
                            <li>
                                关于我们
                                <span style="margin-left: 10px;color: #cccccc">|</span>
                            </li>
                            <li>
                                联系我们 联系客服
                                <span style="margin-left: 10px;color: #cccccc">|</span>
                            </li>
                            <li>合作招商
                                <span style="margin-left: 10px;color: #cccccc">|</span>
                            </li>
                            <li>商家帮助 <span style="margin-left: 10px;color: #cccccc">|</span></li>
                            <li>营销中心 <span style="margin-left: 10px;color: #cccccc">|</span></li>
                            <li>友情链接 <span style="margin-left: 10px;color: #cccccc">|</span></li>
                            <li>销售联盟 <span style="margin-left: 10px;color: #cccccc">|</span></li>
                            <li>友情链接 <span style="margin-left: 10px;color: #cccccc">|</span></li>
                            <li>隐私政策</li>
                        </ul>
                    </div>
                    <p>京公网安备 1****002000088号|京ICP证0*****9号|互联网药品信息服务资格证编号(京)-经营性-2014-0008|新出发京零 字第大1*****2号</p>
                    <p>京ICP备0*****1号京公网安备1***********2</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Register",
        data() {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('confirmPassword');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            var validateUser = (rule, value, callback) => {
                let vm = this;
                vm.axios.get(vm.API.CUSTOMER_FINDBYUSERNAME_URL+"?username="+value).then(res=>{
                    if(res.data){
                        callback(new Error('用户名已被注册'));
                    }
                })
                if (value === '') {
                    callback(new Error('请输入用户名'));
                } else {
                    callback();
                }
            };

            var validatePhone = (rule, value, callback) => {
                var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
                if (value === '') {
                    callback(new Error('请输入联系方式'));
                } else if(!myreg.test(value)){
                    callback(new Error('请正确输入手机号码'));
                } else {
                    callback();
                }
            };
            var validateSmsCode = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入验证码'));
                }  else {
                    callback();
                }
            };
            return {
                ruleForm: {
                    password: '',
                    username: '',
                    checkPass: '',
                    phone: '',
                    smsCode: '',
                },
                rules: {
                    password: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                    username:[
                        { validator: validateUser, trigger: 'blur' }
                    ],
                    phone:[
                        { validator: validatePhone, trigger: 'blur' }
                    ],
                    smsCode:[
                        { validator: validateSmsCode, trigger: 'blur' }
                    ]
                },

            }
        },
        methods: {
            register(formName) {
                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.axios.post(vm.API.USER_REGISTER_URL,this.ruleForm).then(res=>{
                            if(res.data.success){
                                vm.$message.success(res.data.message);
                                vm.$router.push({path:'/login'})
                            }else{
                                vm.$message.error(res.data.message);
                            }
                        })
                    } else {
                        this.$message.error("请先填写完才能注册哦");
                        return false;
                    }
                });
            },
            sendCode(){
                if(this.ruleForm.phone == null || this.ruleForm.phone == ''){
                    this.$message.info("请先填写手机号码");
                    return;
                }
                let vm = this;
                this.axios.get(vm.API.USER_SMSCODE_URL+"?phone="+this.ruleForm.phone).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            },
            goto(){
                this.$router.push({path:'/login'}).then(res =>{
                    console.log(res);
                })
            }
        }
    }
</script>

<style scoped>
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