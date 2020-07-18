<template>
    <div>
        <div id="header">
            <h2 style="margin-left: 10px;margin-top: 10px;">订单管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="deleteOrder" plain>删除</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right ">
                <div style="position: relative;left: -10px;display: inline-block">
                    <div style="display: inline-block;margin-left: 20px">
                        订单编号:
                        <el-input placeholder="请输入内容" style="width: 300px;" v-model="orderId">
                        </el-input>
                    </div>
                </div>
                <el-button style="width: 100px;margin-right: 15px;" @click="pageHandler(1)"
                           icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="orderList"
                        @selection-change="handleSelectionChange"
                        tooltip-effect="dark"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="订单编号"
                            prop="orderId"
                            width="400">
                    </el-table-column>
                    <el-table-column
                            prop="userId"
                            label="用户"
                            width="300">
                    </el-table-column>
                    <el-table-column
                            prop="createTime"
                            label="下单时间"
                            width="300">
                    </el-table-column>
                    <el-table-column
                            label="状态"
                            align="center"
                            width="380">
                        <template slot-scope="scope">
                            {{status[scope.row.status]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template  slot-scope="scope">
                            <el-button type="primary" size="small" @click="goto(scope.row.orderId)">查看详情
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
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
</template>

<script>
    export default {
        name: "Order",
        data(){
            return{
                page:1,
                pageSize:10,
                total:0,
                orderList:[],
                orderSelection:[],//选择订单
                status:{
                    0:"已取消",
                    1:"未付款",
                    2:"已付款",
                    3:"未发货",
                    4:"已发货",
                    5:"交易成功",
                    6:"交易关闭",
                    7:"待评价",
                    8:"退货申请中",
                    9: "已评价",
                },
                orderId:'',
            }
        },
        methods:{
            handleSelectionChange(val) {
                this.orderSelection = val;
                console.log(this.orderSelection)
            },
            pageHandler(page){
                this.page = page;
                let vm = this;
                let sellerId = this.Storage.Session.get("username");
                vm.axios.get(vm.API.ORDER_FINDBYSELLERID_URL+"?sellerId="+sellerId+"&page="+vm.page+"&pageSize="+vm.pageSize).then(res=>{
                    console.log(res);
                    vm.orderList = res.data.rows;
                    vm.total = res.data.total;
                })
            },
            deleteOrder(){

            },
            goto(id){
                this.$router.push({
                    path:`/index/orderItem/${id}`,
                })
            }
        },
        created() {
            this.pageHandler(1)
        }
    }
</script>

<style scoped>

</style>