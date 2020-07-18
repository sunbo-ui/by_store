<template>
    <div style="flex: 6">
        <div style="background-color: #fff; margin-left: 5px;margin: 10px 0px 0 10px;">
            <el-card>
                <div style="background-color: lavender;font-weight: bold;" class="all">
                    <span style="width: 250px;text-align: center;display: inline-block">宝贝</span>
                    <span style="width: 100px;text-align: center;display: inline-block">单价</span>
                    <span style="width: 100px;text-align: center;display: inline-block">数量</span>
                    <span style="width: 100px;text-align: center;display: inline-block">商品操作</span>
                    <span style="width: 120px;text-align: center;display: inline-block">实付款</span>
                    <span style="width: 150px;text-align: center;display: inline-block">交易状态</span>
                    <span style="width:100px;text-align: center;display: inline-block">交易操作</span>
                </div>
                <!--已付款-->
                <div v-if="orderItemList.length ===0">
                    亲~还没有买过东西哦,再看看吧
                </div>
                <div v-else v-for="orderItem in orderItemList">
                    <div style="padding-top: 10px;margin-left: 5px;">
                        <span style="margin-left: 5px">订单号: {{orderItem.orderId}}</span>
                        <span style="display: inline-block;background-color: lightpink;color:#fff;margin-left: 20px;padding:5px">{{orderItem.sellerId}}</span>
                        <hr>
                    </div>
                    <div v-for="(item,index) in orderItem.orderItemList" v-if="index<1">
                        <div class="seller cart" style="height: 100px;padding-left: 5px;">
                            <div style="width:220px;margin-left: 20px;">
                                <div style="display:flex;align-items: center">
                                    <span></span>
                                    <img style="margin-right: 5px;" :src="item.picPath" width="60"
                                         height="60"
                                         alt="">
                                    <span>{{item.title}}</span>
                                </div>
                            </div>
                            <div style="width: 100px;text-align: center">￥{{item.price}}</div>
                            <div style="width: 100px;text-align: center">{{item.num}}</div>
                            <div style="width: 100px;text-align: center">
                                <div style="display: flex;align-items: center;justify-content: center;flex-flow: column">
                                    <span>{{status[orderItem.status]}}</span><br>
                                    <a v-if="orderItem.status !== '0'" href="#" @click="findById(orderItem.orderId)"
                                       data-toggle="modal" data-target="#edit"> 退货/退款</a>
                                </div>
                            </div>
                            <div style="width: 150px;text-align: center">
                                <div style="display: flex;align-items: center;justify-content: center;flex-flow: column">
                                    <span>{{item.totalFee|moneyFormat}}</span><br>
                                    <span>（含运费：￥0.00）</span>
                                </div>
                            </div>
                            <div style="width: 100px;text-align: center;">
                                <div v-if="orderItem.status ==='1'" style="display: flex;align-items: center;justify-content: center;flex-flow: column">
                                    <span>未付款</span><br>
                                    <a style="color: lightskyblue" @click="goto(`/index/orderDetail/${orderItem.orderId}/${orderItem.status}`)">订单详情</a><br>
                                    <a  @click="findById(orderItem.orderId)" data-toggle="modal"
                                       data-target="#editModal">取消订单</a>
                                </div>
                                <div v-else style="display: flex;align-items: center;justify-content: center;flex-flow: column">
                                    <span>{{status[orderItem.status]}}</span><br>
                                    <a style="color: lightskyblue" @click="goto(`/index/orderDetail/${orderItem.orderId}/${orderItem.status}`)">订单详情</a><br>
                                </div>
                            </div>
                            <div style="width: 100px;margin-left: 30px;">
                                <div v-if="orderItem.status === '0'" >
                                    <el-button type="danger" @click="deleteOrder(orderItem.orderId)" size="small" plain>
                                        删除订单
                                    </el-button>
                                </div>
                                <div style="display: flex;align-items: center;justify-content: center;flex-flow: column" v-else-if="orderItem.status === '9'">
                                        <span style="color: red">感谢评价</span>(●'◡'●)
                                    <a  @click="findById(orderItem.orderId)">删除</a>
                                </div>
                                <div v-else-if="orderItem.status === '8'">
                                    <span>请等待商家回应~</span>
                                </div>
                                <div v-else-if="orderItem.status != '1'">
                                    <el-button @click="shipments(7,orderItem.orderId)"  type="primary" size="small" plain>确认收货
                                    </el-button>
                                </div>
                                <div v-else>
                                    <el-button @click="payGo(orderItem.orderId)" type="primary" size="small" plain>去付款
                                    </el-button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--分页-->
                <div>
                    <el-pagination
                            @current-change="loadOrderList"
                            :current-page.sync="page"
                            :page-size="pageSize"
                            layout="total, prev, pager, next"
                            :total="total">
                    </el-pagination>
                </div>
                <!--取消訂單-->
                <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3>确定取消订单吗?</h3>
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            </div>
                            <div class="modal-body">
                                <table class="table table-bordered table-striped" width="800px">
                                    <tr>
                                        <td>原因:</td>
                                        <td><input v-model="buyerMessage" class="form-control" placeholder="取消原因"></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-success" @click="orderItemUpdateStatus(orderId)"
                                        data-dismiss="modal" aria-hidden="true">确定取消
                                </button>
                                <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--退货页面-->
                <div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3>确定要退货吗?</h3>
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            </div>
                            <div class="modal-body">
                                <table class="table table-bordered table-striped" width="800px">
                                    <tr>
                                        <td>原因:</td>
                                        <td><input v-model="buyerMessage" class="form-control" placeholder="退货原因"></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-success" @click="shipments(8)" data-dismiss="modal"
                                        aria-hidden="true">确定
                                </button>
                                <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Order",
        data() {
            return {
                username: '',
                orderItemList: [],
                page: 1, //page
                pageSize:5,//pageSize,  default is 10
                total: 0, //total item count
                buyerMessage:'',
                orderId:null,
                status: {
                    0: "订单已取消",
                    1: "未付款",
                    2: "已付款",
                    3: "未发货",
                    4: "已发货",
                    5: "交易成功",
                    6: "交易关闭",
                    7: "待评价",
                    8:"退货申请中",
                    9: "已评价",
                },
            }
        },
        methods: {
            payGo(orderId){
                //取出订单内容存入redis
                let vm = this;
                vm.axios.get(vm.API.ORDER_GOTOBYORDERID_URL,{params:{orderId:orderId,username:vm.username}}).then(res=>{
                    if(res.data.success){
                        window.location.href=vm.API.BASE_PORTAL_URL+"/index/account/order";
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            },
            goto(path) {
                this.$router.push({path: path})
            },
            loadOrderList(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.ORDER_FINDBYUSERNAME_URL + "?username=" + vm.username+"&page="+vm.page+"&pageSize="+vm.pageSize).then(res => {
                    console.log(res);
                    vm.orderItemList = res.data.rows;
                    vm.total = res.data.total;
                })
            },
            shipments(status){
                let vm = this;
                vm.axios.get(vm.API.ORDER_UPDATESTATUS_URL + "?orderId=" + vm.orderId+"&status="+status+"&buyerMessage="+vm.buyerMessage).then(res => {
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.loadOrderList(1);
                        vm.buyerMessage = '';
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            },
            findById(id){
                this.orderId = id;
            },
            orderItemUpdateStatus(orderId){
                let vm  = this;
                vm.axios.post(vm.API.ORDERITEM_DELETE_URL+"?orderId="+orderId+"&username="+vm.username+"&buyerMessage="+vm.buyerMessage).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.loadOrderList(1);
                        vm.buyerMessage = '';
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            },
            deleteOrder(orderId){
                let vm  = this;
                vm.axios.post(vm.API.ORDERI_DELETE_URL+"?orderId="+orderId).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.loadOrderList(1);
                        vm.buyerMessage = '';
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadOrderList(1);
        },
        filters:{
            moneyFormat(value){
                return "￥"+value.toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .extra {
        height: 20px;
    }
    .header-card {
        width: 250px;
        height: 250px;
        background-color: red;
        position: absolute;
        top: 20px;
        left: -250px;
        box-shadow: 5px 5px 12px 0 rgba(0, 0, 0, 0.4);
        z-index: 2;
    }

    .user-content-right-top {
        z-index: 1;
        margin-left: 260px;
        padding-top: 30px;
    }
    a:hover{
        color: lightcoral;
    }

    .header-card-content {
        display: inline-block;
        font-size: 10px;
        padding-top: 3px;
        width: 95px;
        height: 25px;
        text-align: center;
        border-radius: 15px;
        border: 1px solid #fff
    }

    .header-card-content a {
        color: #fff;
    }

    .user-content-right-top li {
        float: left;
        margin-right: 30px;
    }

    .user-content-right-top li div {
        margin-bottom: 10px;
    }

    .cart > div {
        display: inline-block;
    }
    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(45%);
    }

</style>