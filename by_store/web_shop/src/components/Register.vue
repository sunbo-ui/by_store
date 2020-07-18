<template>
    <div>
        <!--头部-->
        <div style="margin-bottom: 20px;">
            <!--头部-->
            <div style="display:flex;justify-content:center;width: 60%; height: 90px; padding-top: 10px; padding-left:20px;">
                <img src="../assets/logo.png" alt="" width="80" height="80" style="float:left; margin-right: 10px;">
                <div style="color: dimgrey; font-size: 25px;margin: 30px 0 0 50px;">欢迎入驻</div>
            </div>
        </div>
        <!--content-->
            <el-row>
                <el-col :span="12" :offset="6">
                    <div style="width: 100%;background-color: #ccc;height: 30px;">
                        <span style="float: left;font-weight: bold;font-size: 20px;margin-left: 10px;">申请入驻</span>
                        <span style="float: right;margin-top: 3px;margin-right: 5px;">我有账号,<router-link to='/login'>去登录</router-link></span>
                    </div>
                </el-col>
            </el-row>
            <el-row >
                <el-col :span="12" :offset="6">
                   <div style="border: 1px solid #ccc;padding: 10px 10px 0 0;">
                       <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                           <el-form-item label="登陆名:" prop="sellerId">
                               <el-input v-model="ruleForm.sellerId" placeholder="登陆名"></el-input>
                           </el-form-item>
                           <el-form-item label="登陆密码:" prop="password">
                               <el-input type="password" v-model="ruleForm.password" placeholder="登陆密码"></el-input>
                           </el-form-item>
                           <el-form-item label="确认密码:" prop="checkPass">
                               <el-input type="password" v-model="ruleForm.checkPass" placeholder="确认密码"></el-input>
                           </el-form-item>
                           <el-dropdown placement="top-end">
                               <el-button type="primary" style="margin-left: 30px;">
                                   下列选填:<i class="el-icon-arrow-down el-icon--right"></i>
                               </el-button>
                              <div>
                                  <el-dropdown-menu slot="dropdown">
                                      <el-form-item label="店铺名称:" prop="nickName">
                                          <el-input v-model="ruleForm.nickName" placeholder="店铺名称"></el-input>
                                      </el-form-item>
                                      <el-form-item label="公司名称:" prop="name">
                                          <el-input v-model="ruleForm.name" placeholder="公司名称"></el-input>
                                      </el-form-item>
                                      <el-form-item label="公司电话:" prop="telephone">
                                          <el-input v-model="ruleForm.telephone" placeholder="公司电话"></el-input>
                                      </el-form-item>
                                      <el-form-item label="公司地址:" prop="addressDetail">
                                          <el-input v-model="ruleForm.address" placeholder="公司地址"></el-input>
                                      </el-form-item>
                                      <el-form-item label="详细地址:" prop="addressDetail">
                                          <el-input v-model="ruleForm.addressDetail" placeholder="详细地址"></el-input>
                                      </el-form-item>
                                      <el-form-item label="联系人姓名:" prop="linkmanName">
                                          <el-input v-model="ruleForm.linkmanName" placeholder="联系人姓名"></el-input>
                                      </el-form-item>
                                      <el-form-item label="联系人QQ:" prop="linkmanQq">
                                          <el-input v-model="ruleForm.linkmanQq" placeholder="联系人QQ"></el-input>
                                      </el-form-item>
                                      <el-form-item label="联系人手机:" prop="linkmanMobile">
                                          <el-input v-model="ruleForm.linkmanMobile" placeholder="联系人手机"></el-input>
                                      </el-form-item>
                                      <el-form-item label="联系人邮箱:" prop="linkmanEmail">
                                          <el-input v-model="ruleForm.linkmanEmail" placeholder="联系人邮箱"></el-input>
                                      </el-form-item>
                                      <el-form-item label="营业执照号:" prop="licenseNumber">
                                          <el-input v-model="ruleForm.licenseNumber" placeholder="营业执照号"></el-input>
                                      </el-form-item>
                                      <el-form-item label="税务登记号:" prop="taxNumber">
                                          <el-input v-model="ruleForm.taxNumber" placeholder="税务登记号"></el-input>
                                      </el-form-item>
                                      <el-form-item label="法定代表人:" prop="legalPerson">
                                          <el-input v-model="ruleForm.legalPerson" placeholder="法定代表人"></el-input>
                                      </el-form-item>
                                      <el-form-item label="法定身份证:" prop="legalPersonCardId">
                                          <el-input v-model="ruleForm.legalPersonCardId" placeholder="法定身份证"></el-input>
                                      </el-form-item>
                                      <el-form-item label="开户行名称:" prop="bankUser">
                                          <el-input v-model="ruleForm.bankUser" placeholder="开户行名称"></el-input>
                                      </el-form-item>
                                      <el-form-item label="开户行支行:" prop="bankName">
                                          <el-input v-model="ruleForm.bankName" placeholder="开户行支行"></el-input>
                                      </el-form-item>
                                      <el-form-item label="银行账号:" prop="bankAccount">
                                          <el-input v-model="ruleForm.bankAccount" placeholder="银行账号"></el-input>
                                      </el-form-item>
                                  </el-dropdown-menu>
                              </div>
                           </el-dropdown>
                           <div class="controls ml-4 mb-3 mt-3" >
                               <input name="m1" type="checkbox" value="2" checked=""><span>同意协议并注册  <a href="sampling.html">《品优购商家入驻协议》</a></span>
                           </div>
                           <el-form-item>
                               <el-button type="primary" style="width: 300px;margin-left: 150px;" @click="submitForm('ruleForm')">立即创建</el-button>
                           </el-form-item>
                       </el-form>
                   </div>
                </el-col>
            </el-row>
    </div>
</template>

<script>
    export default {
        data() {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
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
            return {
                ruleForm: {
                    sellerId:'',
                    password: '',
                    nickName:'',
                    name:'',
                    telephone:'',
                    address:'',
                    addressDetail:'',
                    linkmanName:'',
                    linkmanQq:'',
                    linkmanMobile:'',
                    linkmanEmail:'',
                    licenseNumber:'',
                    taxNumber:'',
                    legalPerson:'',
                    legalPersonCardId:'',
                    bankUser:'',
                    bankName:'',
                    bankAccount:'',
                    checkPass:'',
                },
                rules: {
                    sellerId: [
                        {required: true, message: '请输入登录名', trigger: 'blur'},
                        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    checkPass: [
                        {validator: validatePass2, trigger: 'blur'}
                    ],
                }
            };
        },

        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let vm = this;
                        this.axios.post(vm.API.SELLER_REGISTER_URL,this.ruleForm)
                            .then(function (response) {
                              //  console.log(response);
                               if(response.data.success){
                                   vm.$message.success(response.data.message);
                                   vm.$router.push('/login');
                               }else{
                                   vm.$message.error(response.data.message);
                               }
                            }).catch(function (reason) {
                            console.log(reason);
                        })
                    } else {
                        this.$message.error("输入有误,请检查后再注册!")
                        return false;
                    }
                });
            },

        }
    }
</script>

<style >
    .el-dropdown-menu{
        width: 800px;
        transform-origin:unset !important;
        position: absolute;
        top: 0 !important;
        left: 0!important;
    }
</style>