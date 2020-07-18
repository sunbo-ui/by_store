<template>
    <div class="p-4">
        <div>
            <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">头像设置</span>
        </div>
        <el-card>

            <div style="display: flex;align-items: center" class="mt-2">
                <span>当前头像:</span>
                <div class="ml-3 mr-3" style="display:inline-block;width: 150px;height: 150px;border: 1px solid #ccc">
                    <img style="width: 150px;height: 150px;" :src="imgUrl" alt="">
                </div>
                <el-upload
                        :action="this.API.UPLOAD_UPLOADFILE_URL"
                        :limit="1"
                        list-type="picture-card"
                        :on-success="fileSuccess"
                        :on-exceed="limit"
                        :on-remove="handleRemove">
                    <i class="el-icon-plus"></i>
                </el-upload>
            </div>
            <el-button class="float-right mb-3" type="success" @click="saveSeller">保存</el-button>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "HeaderSetting",
        data() {
            return {
                username: '',
                seller: {
                    pic:'',
                },
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
                    console.log(this.imgUrl);
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
            loadSeller() {
                let vm = this;
                vm.axios.get(vm.API.SELLER_FINDBYID_URL + "?sellerId=" + this.username).then(res => {
                    vm.seller = res.data;
                    vm.imgUrl = vm.seller.pic;
                })
            },
            saveSeller(){
                let vm = this;
                this.seller.pic = this.imgUrl;
                vm.axios.post(vm.API.SELLER_UPDATEBYID_URL+"?sellerId="+this.username,this.seller).then(res => {
                    if(res.data.success){
                        vm.$message.success(res.data.message)
                        vm.loadSeller();
                    }else{
                        vm.$message.error(res.data.message)
                    }
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadSeller();

        }
    }
</script>

<style scoped>

</style>