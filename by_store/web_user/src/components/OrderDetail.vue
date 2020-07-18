<template>
    <div>
        <div style="flex:6;margin-top: 10px;">
            <el-card >
                <div slot="header" class="clearfix">
                    <span style="color: #303133;font-weight: bold;">订单详情</span><br>
                    <span style="color: #909399;font-size: 30px;">订单号:{{orderId}} --- {{status[state]}}</span>
                     <el-button style="float: right" type="danger" @click="goto('/index/order')">我的订单</el-button>
                </div>
                <div style="width: 950px">
                    <template>
                        <el-table
                                ref="multipleTable"
                                :data="orderItemList"
                                tooltip-effect="dark"
                                style="width: 100%;"
                        >
                            <el-table-column
                                    label="库存id"
                                    prop="itemId"
                                    width="100">
                            </el-table-column>
                            <el-table-column
                                    prop="goodsId"
                                    width="180"
                                    label="商品Id">
                            </el-table-column>
                            <el-table-column
                                    label="商品"
                                    width="300">
                                <template slot-scope="scope">
                                    <img :src="scope.row.picPath" width="80" height="80" alt="">
                                    <span>{{scope.row.title}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="num"
                                    label="数量"
                                    width="100">
                            </el-table-column>
                            <el-table-column
                                    label="价格"
                                    width="100">
                                <template slot-scope="scope">
                                    ￥{{scope.row.price}}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="总金额"
                                    align="center"
                                    width="100">
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
            }
        },
        methods: {
            loadOrderItemByOrderId(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.ORDERITEM_FINDBYORDERID_URL + "?orderId=" + vm.orderId + "&page=" + vm.page + "&pageSize=" + vm.pageSize).then(res => {
                    vm.total = res.data.total;
                    vm.orderItemList = res.data.rows;
                })
            },
            goto(path) {
                this.$router.push({path})
            },
        },
        created() {
            this.orderId = this.$route.params.id;
            this.state = this.$route.params.status;
            this.loadOrderItemByOrderId(1);
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