<template>
    <div style="display:flex;flex-flow: column;">
        <div class="user-content-right-top" style="position: relative">
            <div class="header-card">
                <div style="text-align: center;">
                    <img :src="imgUrl" width="80" height="80" style="border-radius: 50%;margin-top: 20px;"
                         alt="">
                    <div style="color: #fff;font-size: 25px;font-weight: bold;">{{username}}</div>
                </div>
                <div style="margin-top: 10px;margin-left: 20px;">
                    <span class="header-card-content"><a href="#">京享值 4870</a></span>
                    <span style="margin-left: 10px;" class="header-card-content"><a href="#">小白信用 86.2</a></span>
                </div>
                <div style="margin-top: 20px;background-color: black;height:60px;display:flex;">
                    <div style="flex: 2;margin-top: 5px;margin-left: 10px   ;">
                        <span style="color: #f4ca3a;font-size: 8px;font-weight: bold;">
                        开通PLUS会员尊享
                    </span><br>
                        <span style="color: #f4ca3a;font-size: 8px;">全年360元运费券礼包</span>
                    </div>
                    <div style="flex: 1;margin-right: 10px;margin-top: 15px;">
                        <el-button size="mini" style="background-color: #f4ca3a;">立即开通</el-button>
                    </div>
                </div>
            </div>
            <div style="background-color: #fff;">
                <div style="height: 40px;padding: 15px 0 0px 15px;font-size: 17px;">我的钱包</div>
                <hr>
                <div style="display:flex;justify-content: center;align-items: center">
                    <div style="flex: 1">
                        <div style="font-weight: bold;font-size: 22px;">{{discountList.length}}</div>
                        <div class="mt-2">优惠券</div>
                        <div class="extra"></div>
                        <div><a href="#">领券</a></div>
                    </div>
                    <div style="flex: 1">
                        <div style="font-weight: bold;font-size: 22px;">{{integral}}</div>
                        <div class="mt-2">悦豆</div>
                        <div class="extra"></div>
                        <div><a href="#">赚悦豆 花悦豆</a></div>
                    </div>
                    <div style="flex: 1">
                        <div style="font-weight: bold;font-size: 22px;">{{amount|moneyFormat}}</div>
                        <div class="mt-2">余额</div>
                        <div class="extra"></div>
                        <div><a href="#" @click="goto('/index/ewallet')">充值</a></div>
                    </div>
                    <div style="flex: 1">
                        <div style="font-weight: bold;font-size: 22px;">0.00</div>
                        <div class="mt-2">白条本月代还</div>
                        <div style="color: #C0C4CC;font-size: 10px;">可用余额10000.00</div>
                        <div><a href="#">提现</a></div>
                    </div>
                    <div style="flex: 1">
                        <div style="font-weight: bold;font-size: 22px;">0.00</div>
                        <div>小金库昨日收益</div>
                        <div style="color: #C0C4CC;font-size: 10px;">总额0.00</div>
                        <div><a href="#">转入小金库,马上赚钱</a></div>
                    </div>
                </div>
            </div>
        </div>
        <div style="background-color: #fff; margin-left: 20px;margin: 100px 10px 0 10px;">
            <div>
                <div style="background-color: lavender;margin-top: 10px;font-weight: bold;" class="all">
                    <span style="width: 250px;text-align: center;display: inline-block">宝贝</span>
                    <span style="width: 100px;text-align: center;display: inline-block">单价</span>
                    <span style="width: 100px;text-align: center;display: inline-block">数量</span>
                    <span style="width: 100px;text-align: center;display: inline-block">商品操作</span>
                    <span style="width: 120px;text-align: center;display: inline-block">实付款</span>
                    <span style="width: 150px;text-align: center;display: inline-block">交易状态</span>
                    <span style="width:100px;text-align: center;display: inline-block">交易操作</span>
                </div>
                <!--已付款-->
                <div v-for="orderItem in orderItemList">
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
                            <div style="width: 100px;text-align: center">{{item.price}}</div>
                            <div style="width: 100px;text-align: center">{{item.num}}</div>
                                <div  style="width: 100px;text-align: center">
                                <div>
                                    <span>{{status[orderItem.status]}}</span><br>
                                    <a v-if="orderItem.status !== '0'" href="#" @click="findById(orderItem.orderId)"
                                       data-toggle="modal" data-target="#edit"> 退货/退款</a>
                                </div>
                            </div>
                            <div style="width: 150px;text-align: center">
                                <span>{{item.totalFee|moneyFormat}}</span><br><br>
                                <span>（含运费：￥0.00）</span>
                            </div>
                            <div style="width: 100px;text-align: center">
                                <div v-if="orderItem.status ==='1'">
                                    <span>未付款</span><br>
                                    <a style="color: lightskyblue" @click="goto(`/index/orderDetail/${orderItem.orderId}/${orderItem.status}`)">订单详情</a><br>
                                    <a  @click="findById(orderItem.orderId)" data-toggle="modal"
                                       data-target="#editModal">取消订单</a>
                                </div>
                                <div v-else>
                                    <span>{{status[orderItem.status]}}</span><br>
                                    <a style="color: lightskyblue" @click="goto(`/index/orderDetail/${orderItem.orderId}/${orderItem.status}`)">订单详情</a><br>
                                </div>
                            </div>
                            <div style="width: 100px;margin-left: 30px;">
                                <div v-if="orderItem.status === '0'">
                                    <el-button type="danger" @click="deleteOrder(orderItem.orderId)" size="small" plain>
                                        删除订单
                                    </el-button>
                                </div>
                                <div v-else-if="orderItem.status === '7'">
                                    <el-button type="success" @click="goto(`/commentsShop/${orderItem.orderId}`)" size="small" plain>去评价
                                    </el-button>
                                </div>
                                <div v-else-if="orderItem.status === '8'">
                                    <span>请等待商家回应~</span>
                                </div>
                                <div style="display: flex;align-items: center;justify-content: center;flex-flow: column" v-else-if="orderItem.status === '9'">
                                    <span style="color: red">感谢评价</span>(●'◡'●)
                                </div>
                                <div v-else-if="orderItem.status != '1'">
                                    <el-button type="primary" @click="shipments(7,orderItem.orderId)" size="small" plain>确认收货
                                    </el-button>
                                </div>
                                <div v-else>
                                    <el-button @click="payGo(orderItem.orderId)" type="danger" size="small" plain>去付款
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
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                username: '',
                orderItemList: [],
                page: 1, //page
                pageSize:2,//pageSize,  default is 10
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
                imgUrl:'',
                amount:0,
                discountList:[],
                integral:0,
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
            shipments(status,orderId){
                let vm = this;
                vm.axios.get(vm.API.ORDER_UPDATESTATUS_URL + "?orderId=" + orderId+"&status="+status+"&buyerMessage="+vm.buyerMessage).then(res => {
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
            },
            loadHeaderImg(){
                let vm = this;
                vm.axios.get(vm.API.CUSTOMER_FINDBYUSERNAME_URL+"?username="+this.username).then(res=>{
                    vm.imgUrl = res.data.pic;
                })
            },
            loadEwallet() {
                let vm = this;
                vm.axios.get(vm.API.EWALLET_FINDAMOUNTBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    if (res.data) {
                        vm.amount = res.data.amount;
                    }
                })
            },
            loadDiscount() {
                let vm = this;
                vm.axios.post(vm.API.DISCOUNT_FINDBYUSERID_URL + "?username=" +  this.username)
                    .then(function (response) {
                        //取服务端响应的结果
                        vm.discountList = response.data;
                    }).catch(function (reason) {
                    console.log(reason);
                });
            },
            loadIntegral(){
                let vm = this;
                vm.axios.get(vm.API.INTEGRAL_FINDBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    vm.integral = res.data.integral;
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadOrderList(1);
            this.loadHeaderImg();
            this.loadEwallet();
            this.loadDiscount();
            this.loadIntegral();
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
        left: -270px;
        box-shadow: 5px 5px 12px 0 rgba(0, 0, 0, 0.4);
        z-index: 2;
    }

    .user-content-right-top {
        z-index: 1;
        margin-left: 260px;
        padding-top: 30px;
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