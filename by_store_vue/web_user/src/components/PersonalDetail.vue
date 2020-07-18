<template>
    <div>
        <div>
            <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">个人资料</span>
        </div>
        <el-card>
            <div style="font-weight: bold;font-size: 25px;">
                亲爱的{{username}}，填写真实的资料，有助于好友找到你哦。
            </div>
            <div style="display: flex;align-items: center" class="mt-2">
                <span>当前头像:</span>
                <div class="ml-3 mr-3" style="display:inline-block;width: 150px;height: 150px;border: 1px solid #ccc">
                    <img style="width: 150px;height: 150px;" :src="imgUrl" alt="">
                </div>
                <el-upload
                        :action="this.API.UPLOAD_UPLOADFILE_URL"
                        :headers="importHeader"
                        :limit="1"
                        list-type="picture-card"
                        :on-success="fileSuccess"
                        :on-exceed="limit"
                        :on-remove="handleRemove">
                    <i class="el-icon-plus"></i>
                </el-upload>
            </div>
            <el-button class="float-right mb-3" type="success" @click="saveCustomer">保存</el-button>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "personalDetail",
        data() {
            return {
                username: '',
                customer: {},
                importHeader: {},
                imgUrl: ''
            }
        },
        methods: {
            limit() {
                this.$message.warning("头像只能上传一个,若修改可以删除后再操作");
            },
            fileSuccess(res, file) {
                if (res.success) {
                    this.$message.success("上传成功");
                    this.imgUrl = res.message;
                } else {
                    this.$message.error(res.message);
                }
            },
            handleRemove: function (file) {
                let vm = this;
                vm.axios.get(vm.API.DELETE_UPLOADURL_URL, {params: {url: file.response.message}}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            loadCustomer() {
                let vm = this;
                vm.axios.get(vm.API.CUSTOMER_FINDBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    vm.customer = res.data;
                    vm.imgUrl = vm.customer.pic;
                })
            },
            saveCustomer(){
                let vm = this;
                this.customer.pic = this.imgUrl;
                this.customer.username = this.username;
                console.log(this.customer);
                vm.axios.post(vm.API.CUSTOMER_SAVE_URL,this.customer).then(res => {
                    if(res.data.success){
                        vm.$message.success(res.data.message)
                        vm.loadCustomer();
                    }else{
                        vm.$message.error(res.data.message)
                    }
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.importHeader['jsessionid'] = this.cookieUtil.getCookie('jsessionid');
            this.loadCustomer();

        }
    }
</script>

<style scoped>

</style>