<template>
    <div>
        <div style="background-color: #fff;">
            <div style="padding: 20px 0 20px 30px;">
                <el-button @click="clearAddress" data-toggle="modal" data-target="#editModal"
                           style="background-color: #f5fbef;color: #000000;font-weight: bold;font-size: 20px;">新增收货地址
                </el-button>
                <span style="color: #C0C4CC"> 您已创建{{total}} 个收货地址，最多可创建25 个</span>
            </div>
            <div style="padding: 20px">
                <el-card class="box-card" style="margin-top: 10px;" v-for="item in addressList">
                    <div>
                        <span style="color: #999;font-weight: bold;font-size: 20px;">{{item.contact}}-{{item.address.substr(0,2)}}</span>
                        <span v-if="item.isDefault == 1" class="default">默认地址</span>
                    </div>
                    <div style="margin-top:20px;">
                        <span style="color: #C0C4CC">收货人:</span>
                        <span style="padding-left: 20px;">{{item.contact}}</span>
                    </div>
                    <div style="margin-top:10px;">
                        <span style="color: #C0C4CC">地址:</span>
                        <span style="padding-left: 20px;">{{item.address}}</span>
                    </div>
                    <div style="margin-top:10px;">
                        <span style="color: #C0C4CC">手机:</span>
                        <span style="padding-left: 20px;">{{item.mobile}}</span>
                    </div>
                    <div style="margin-top:10px;">
                        <span style="color: #C0C4CC">备注:</span>
                        <span style="padding-left: 20px;">{{item.notes}}</span>
                    </div>
                    <div style="float: right;padding:10px">
                        <el-button type="primary"  data-toggle="modal" data-target="#editModal" @click="echoUpdate(item.id)">编辑</el-button>
                        <el-button v-if="item.isDefault == 0" @click="setDefault(item.id)" type="warning">设为默认地址</el-button>
                        <el-button @click="deleteAddress(item.id)" type="danger">删除</el-button>
                    </div>
                </el-card>
            </div>
            <!--分页-->
            <div class="block">
                <el-pagination
                        @current-change="pageHandler"
                        :current-page.sync="page"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="total">
                </el-pagination>
            </div>
        </div>

        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 v-html="ruleForm.id!=null ? '编辑收货地址':'添加收货地址'"></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px"
                                 class="demo-ruleForm">
                            <el-form-item label="收货人" prop="contact">
                                <el-input v-model="ruleForm.contact"></el-input>
                            </el-form-item>
                            <el-form-item label="详细地址" prop="address">
                                <el-input v-model="ruleForm.address"></el-input>
                            </el-form-item>
                            <el-form-item label="手机号码" prop="mobile">
                                <el-input v-model="ruleForm.mobile"></el-input>
                            </el-form-item>
                            <el-form-item label="备注" prop="notes">
                                <el-input type="textarea" v-model="ruleForm.notes"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" v-html="ruleForm.id!=null ? '编辑':'保存'" @click="submitForm('ruleForm')"></el-button>
                                <el-button @click="resetForm('ruleForm')">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Address",
        data() {
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
            return {
                ruleForm: {
                    id:null,
                    contact: '',//联系人
                    address: '',
                    mobile: '',
                    notes: '',
                },
                page: 1, //page
                pageSize: 2,//pageSize,  default is 10
                total: 0, //total item count
                username:'',
                rules: {
                    contact: [
                        {required: true, message: '请您填写收货人姓名', trigger: 'blur'},
                    ],
                    address: [
                        {required: true, message: '请您填写收货人详细地址', trigger: 'blur'}
                    ],
                    mobile: [
                        {validator: validatePhone, trigger: 'blur'}
                    ],
                },
                addressList: [],
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let vm = this;
                        let url = '';
                        if(vm.ruleForm.id === null){
                            url = vm.API.ADDRESS_SAVE_URL+"?username="+vm.username;
                        }else{
                            url = vm.API.ADDRESS_UPDATE_URL
                        }
                        vm.axios.post(url,vm.ruleForm).then(res => {
                            if(res.data.success){
                                vm.$message.success(res.data.message);
                                vm.pageHandler(1);
                            }else{
                                vm.$message.error(res.data.message);
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            pageHandler(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.ADDRESS_FINDBYUSERNAME_URL + "?username=" + vm.username + "&page=" + vm.page + "&rows=" + vm.pageSize).then(res => {
                    console.log(res);
                    vm.addressList = res.data.rows;
                    vm.total = res.data.total;
                    for(let i = 0;i<vm.addressList.length;i++){
                        vm.addressList[i].mobile = vm.addressList[i].mobile.replace(vm.addressList[i].mobile.substring(3,9),"*******");
                    }
                })
            },
            setDefault(id){
                let vm = this;
                vm.axios.get(vm.API.ADDRESS_SETDEFAULT_URL+ "?username=" + vm.username +"&id="+id).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    }else{
                        vm.$message.error(res.data.message)
                    }
                })
            },
            echoUpdate(id){
                let vm = this;
                vm.axios.get(vm.API.ADDRESS_UPDATEBYID_URL+ "?id="+id).then(res=>{
                    vm.ruleForm = res.data;
                    vm.ruleForm.mobile = vm.ruleForm.mobile.replace(vm.ruleForm.mobile.substring(3,9),"*******");
                })
            },
            clearAddress(){
                this.ruleForm.id = null,
                this.ruleForm.contact = '',
                this.ruleForm.address = '',
                this.ruleForm.mobile = '',
                this.ruleForm.notes = ''
            },
            deleteAddress(id){
                let vm = this;
                vm.axios.get(vm.API.ADDRESS_DELETE_URL+"?id="+id).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    }else{
                        vm.$message.error(res.data.message)
                    }
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.pageHandler(1);
        }
    }
</script>

<style scoped>
    .default {
        margin-left: 30px;
        display: inline-block;
        width: 80px;
        height: 30px;
        background-color: #ffaa45;
        text-align: center;
        font-size: 15px;
        color: #fff;
        padding-top: 5px;
    }

    .modal-content {
        position: absolute;
        top: 30%;
        left: 50%;
        transform: translateX(-50%) translateY(45%);
    }

</style>