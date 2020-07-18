<template>
    <div style="padding: 20px">
        <template>
            <el-tabs v-model="activeName" type="card">
                <el-tab-pane label="修改密码" name="first">
                    <el-form ref="form" :model="form" label-width="180px">
                        <el-form-item label="公司名称">
                            <el-input v-model="form.name" placeholder="公司名称"></el-input>
                        </el-form-item>
                        <el-form-item label="公司手机">
                            <el-input v-model="form.mobile" placeholder="公司手机"></el-input>
                        </el-form-item>
                        <el-form-item label="公司电话">
                            <el-input v-model="form.telephone" placeholder="公司电话"></el-input>
                        </el-form-item>
                        <el-form-item label="公司详细地址">
                            <el-input v-model="form.addressDetail" placeholder="公司详细地址"></el-input>
                        </el-form-item>
                        <el-form-item label="联系人姓名">
                            <el-input v-model="form.linkmanName" placeholder="联系人姓名"></el-input>
                        </el-form-item>
                        <el-form-item label="联系人QQ">
                            <el-input v-model="form.linkmanQq" placeholder="联系人QQ"></el-input>
                        </el-form-item>
                        <el-form-item label="联系人手机">
                            <el-input v-model="form.linkmanMobile" placeholder="联系人手机"></el-input>
                        </el-form-item>
                        <el-form-item label="联系人EMAIL">
                            <el-input v-model="form.linkmanEmail" placeholder="联系人EMAIL"></el-input>
                        </el-form-item>
                        <el-form-item label="营业执照号">
                            <el-input v-model="form.license_number" placeholder="营业执照号"></el-input>
                        </el-form-item>
                        <el-form-item label="税务登记证号">
                            <el-input v-model="form.taxNumber" placeholder="税务登记证号"></el-input>
                        </el-form-item>
                        <el-form-item label="组织机构代码证">
                            <el-input v-model="form.orgNumber" placeholder="组织机构代码证"></el-input>
                        </el-form-item>
                        <el-form-item label="法定代表人">
                            <el-input v-model="form.legalPerson" placeholder="法定代表人"></el-input>
                        </el-form-item>
                        <el-form-item label="法定代表人身份证号">
                            <el-input v-model="form.legalPersonCardId" placeholder="法定代表人身份证号"></el-input>
                        </el-form-item>
                        <el-form-item label="开户行名称">
                            <el-input v-model="form.bankUser" placeholder="开户行名称"></el-input>
                        </el-form-item>
                        <el-form-item label="开户行支行">
                            <el-input v-model="form.bankName" placeholder="开户行支行"></el-input>
                        </el-form-item>
                        <el-form-item label="银行账号">
                            <el-input v-model="form.bankAccount" placeholder="银行账号"></el-input>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary">修改</el-button>
                            <el-button @click="saveSeller()">保存</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Password",
        data() {
            return {
                activeName: 'first',
                form: {
                    name: '',
                    mobile: '',
                    telephone: '',
                    addressDetail: '',
                    linkmanName: '',
                    linkmanQq: '',
                    linkmanMobile: '',
                    linkmanEmail: '',
                    license_number: '',
                    taxNumber: '',
                    orgNumber: '',
                    legalPerson: '',
                    bankUser: '',
                    bankName: '',
                    bankAccount: '',
                }
            };
        },
        methods: {
            loadSeller(){
                let vm = this;
                let username =  vm.Storage.Session.get("username");
                vm.axios.get(vm.API.SELLER_FINDBYID_URL,{params: {sellerId: username}}).then(res=>{
                    vm.form = res.data;
                })
            },
            saveSeller(){
                let vm = this;
                let username =  vm.Storage.Session.get("username");
                vm.axios.post(vm.API.SELLER_UPDATEBYID_URL+"?sellerId="+username,vm.form).then(res=>{
                    if (res.data.success){
                        vm.$message.success(res.data.message)
                    }else{
                        vm.$message.error(res.data.message)
                    }
                    vm.loadSeller();
                })
            }
        },
        created() {
            this.loadSeller();
        }
    }
</script>

<style scoped>

</style>