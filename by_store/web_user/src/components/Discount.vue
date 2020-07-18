<template>
    <div>
        <div class="p-2">
            <div class="p-1">
                <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">我的优惠券</span>
            </div>
            <el-card v-if="!discountList">
                你还没有优惠券哦,快去领券吧
            </el-card>
            <el-card v-else v-for="discount in discountList" style="margin-bottom: 10px;">
                <div style="display:flex;">
                    <div style="flex: 4;">
                        <div >
                            <div style="font-size: 25px;font-weight: bold;">
                                <span> 满{{discount.conditions|moneyFormat}}</span>
                                <span class="ml-4">店铺名:{{discount.sellerId}}</span>
                            </div>
                            <div style="font-size: 20px;font-weight: bold;">
                                <span>立减{{discount.discount | moneyFormat}}</span>
                                <span style="color: #909399" class="ml-4">全店使用</span>
                            </div>
                        </div>
                        <div>
                            <span style="color:#C0C4CC">{{discount.expirationTime |dateFormat}}到期</span>
                        </div>
                    </div>
                    <div class="float-right" style="flex: 1;justify-content: center;align-items: center;display: flex">
                        <el-button type="success">进入店铺</el-button>
                        <el-button @click="deleteUserDiscount(discount.id)" type="danger">删除</el-button>
                    </div>
                </div>
            </el-card>
            <!--分页-->
            <div style="margin-top: 10px;">
                <el-pagination
                        @current-change="loadDiscount"
                        :current-page.sync="page"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from "qs";

    export default {
        name: "Discount",
        data() {
            return {
                username: '',
                discountList: [],
                page: 1,
                pageSize: 5,//一页显示三天足迹
                total: 0,
            }
        },
        methods: {
            loadDiscount(page) {
                let vm = this;
                this.page = page;
                vm.axios.get(vm.API.USERDISCOUNT_FINDBYUSERID_URL + "?username=" + this.username+"&page="+this.page+"&pageSize="+this.pageSize)
                    .then(function (response) {
                        console.log(response);
                        //取服务端响应的结果
                        vm.discountList = response.data.rows;
                        vm.total = response.data.total;

                    }).catch(function (reason) {
                    console.log(reason);
                });
            },
            deleteUserDiscount(id){
                let vm = this;
                vm.axios.get(vm.API.USERDISCOUNT_DELETE_URL + "?id="+id)
                    .then(function (res) {
                      if(res.data.success){
                          res.$message.success(res.data.message)
                          vm.loadDiscount(1);
                      }else {
                          res.$message.error(res.data.message)
                      }

                    }).catch(function (reason) {
                    console.log(reason);
                });
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadDiscount(1);
        },
        filters: {
            moneyFormat(value) {
                return "￥" + Number(value).toFixed(2);
            },
            dateFormat(value){
                let date = value.split(" ");
                return date[0];
            }
        }
    }
</script>

<style scoped>
    .el-card {
        padding: 0;
    }

</style>