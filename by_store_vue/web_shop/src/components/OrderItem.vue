<template>
    <div>
        <div style="padding:30px">
            <el-card class="box-card">
                <div slot="header" class="clearfix">
                    <span style="color: #303133;font-weight: bold;">订单详情</span><br>
                    <span style="color: #909399;font-size: 30px;">订单号:{{orderId}} --- {{status[order.status]}}</span>
                    <el-button style="float: right;" v-if="order.status === '1' ||order.status === '2'||order.status === '3'" @click="shipments(4)" type="success">立即发货</el-button>
                    <el-button style="float: right;" v-if="order.status !== '1' && order.status !== '0'&&order.status !== '3'&&order.status !== '8'" @click="shipments(3)" type="success">取消发货</el-button>
                    <el-button style="float: right;" v-if="order.status == '8'" @click="shipments(0)" type="success">同意取消订单</el-button>
                </div>
                <div>
                    <template>
                        <el-table
                                ref="multipleTable"
                                :data="orderItemList"
                                tooltip-effect="dark"
                                style="width: 100%"
                        >
                            <el-table-column
                                    label="库存id"
                                    prop="itemId"
                                    width="200">
                            </el-table-column>
                            <el-table-column
                                    prop="goodsId"
                                    label="商品Id">
                            </el-table-column>
                            <el-table-column
                                    label="商品"
                                    width="450">
                                <template slot-scope="scope">
                                    <img :src="scope.row.picPath" width="80" height="80" alt="">
                                    <span>{{scope.row.title}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="num"
                                    label="数量"
                                    width="200">
                            </el-table-column>
                            <el-table-column
                                    label="价格"
                                    width="200">
                                <template slot-scope="scope">
                                    ￥{{scope.row.price}}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="总金额"
                                    align="center"
                                    width="280">
                                <template slot-scope="scope">
                                    {{scope.row.totalFee |moneyFormat(scope.row.totalFee)}}
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                </div>

                <!--分页-->
                <div class="block">
                    <el-pagination
                            @current-change="loadOrderItemByOrderId"
                            :current-page.sync="page"
                            :page-size="pageSize"
                            layout="total, prev, pager, next"
                            :total="total">
                    </el-pagination>
                </div>
            </el-card>
            <el-card class="box-card" style="margin-top: 20px;">
                <div slot="header" class="clearfix">
                    <span style="color: #303133;font-weight: bold;">客户详情</span>
                </div>
                <div>
                    <span style="color: #909399;font-size: 20px;">联系人:</span>
                    <span style="color: #909399;font-size: 20px;margin-left: 50px;">{{order.receiver}}</span>
                </div>
                <div style="margin-top: 15px;">
                    <span style="color: #909399;font-size: 20px;">联系电话:</span>
                    <span style="color: #909399;font-size: 20px;margin-left: 50px;">{{order.receiverMobile}}</span>
                </div>
                <div style="margin-top: 15px;">
                    <span style="color: #909399;font-size: 20px;">收货地址:</span>
                    <span style="color: #909399;font-size: 20px;margin-left: 50px;">{{order.receiverAreaName}}</span>
                </div>
                <div style="margin-top: 15px;">
                    <span style="color: #909399;font-size: 20px;">下单时间:</span>
                    <span style="color: #909399;font-size: 20px;margin-left: 50px;">{{order.createTime}}</span>
                </div>
                <div style="margin-top: 15px;">
                    <span style="color: #909399;font-size: 20px;">买家留言:</span>
                    <span style="color: #909399;font-size: 20px;margin-left: 50px;">{{order.buyerMessage}}</span>
                </div>
                <div style="float: right;padding: 10px">
                    <el-button type="danger" @click="goto('/index/order')">返回</el-button>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "OrderItem",
        data() {
            return {
                orderId: '',
                orderItemList: [],
                status: {
                    0: "已取消",
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
                state: '',
                page: 1,
                pageSize: 5,
                total: 0,
                order: {},
            }
        },
        methods: {
            loadOrderItemByOrderId(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.ORDERITEM_FINDBYORDERID_URL + "?orderId=" + vm.orderId + "&page=" + vm.page + "&pageSize=" + vm.pageSize).then(res => {
                    console.log(res);
                    vm.total = res.data.total;
                    vm.orderItemList = res.data.rows;
                })
            },
            findOrderById() {
                let vm = this;
                vm.axios.get(vm.API.ORDER_FINDORDERBYID_URL + "?orderId=" + vm.orderId).then(res => {
                    vm.order = res.data;
                })
            },
            goto(path) {
                this.$router.push({path})
            },
            shipments(status){//发货
                let vm = this;
                vm.axios.get(vm.API.ORDER_UPDATESTATUS_URL + "?orderId=" + vm.orderId+"&status="+status).then(res => {
                   if(res.data.success){
                       vm.$message.success(res.data.message);
                       vm.$router.push({path:'/index/order'})
                   }else{
                       vm.$message.error(res.data.message);
                   }
                })
            }
        },
        created() {
            this.orderId = this.$route.params.id;
            this.loadOrderItemByOrderId(1);
            this.findOrderById();
        },
        filters: {
            moneyFormat(value) {
                return "￥" + value.toFixed(2);
            }
        }
    }
</script>

<style scoped>

</style>