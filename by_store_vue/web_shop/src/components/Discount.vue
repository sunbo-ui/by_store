<template>
    <div>
        <div id="header">
            <h2>优惠券管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" data-toggle="modal" @click="clear" data-target="#editModal" plain>新建</el-button>
            <el-button type="info" @click="deleteDiscountBySelection" plain>删除</el-button>
            <el-button type="info" plain>刷新</el-button>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="discountList"
                        @selection-change="handleSelectionChange"
                        tooltip-effect="dark"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="优惠券ID"
                            prop="id"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="discount"
                            label="折扣"
                            align="center"
                            width="250">
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量"
                            align="center"
                            width="250">
                    </el-table-column>
                    <el-table-column
                            prop="conditions"
                            align="center"
                            label="满足条件"
                            width="250">
                    </el-table-column>
                    <el-table-column
                            prop="expirationTime"
                            align="center"
                            label="过期时间"
                            width="250">
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" @click="updateDiscount(scope.row.id)" size="small"
                                       data-toggle="modal" data-target="#editModal">修改
                            </el-button>
                            <el-button type="danger" @click="deleteDiscount(scope.row.id)" size="small">删除
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
        <!-- 编辑窗口 -->
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel" v-html="discount.id  ?'优惠券编辑':'优惠券设置'"></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <div class="mt-3 mb-2">
                            <span style="font-size: 20px; ">折扣:</span>
                            <el-input type="text"  v-model="discount.discount"/>
                        </div>
                        <div class="mt-3 mb-2">
                            <span style="font-size: 20px;">满足条件(只需填写要满足的数字):</span>
                            <el-input type="text" v-model="discount.conditions"/>
                        </div>
                        <div class="mt-3 mb-2">
                            <span style="font-size: 20px;">数量:</span>
                            <el-input type="text" v-model="discount.num"/>
                        </div>
                        <div class="mt-3 mb-2">
                            <span style="font-size: 20px;">过期时间:</span>
                            <div class="block">
                                <el-date-picker
                                        v-model="discount.expirationTime"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        :default-value="new Date()"
                                        placeholder="选择日期时间">
                                </el-date-picker>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" v-html="discount.id ?'编辑':'保存'" data-dismiss="modal"
                                aria-hidden="true" @click="saveDiscount"></button>
                        <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import moment from 'moment'
    export default {
        data() {
            return {
                username: '',
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                discountList: [],
                discountSelection: [],
                discount: {},
                goodsList:[],
            }
        },

        methods: {
            pageHandler(page) {
                let vm = this;
                this.page = page;
                vm.axios.post(vm.API.DISCOUNT_FINDBYSELLER_URL + "?page=" + page + "&rows=" + this.pageSize + "&username=" + this.username)
                    .then(function (response) {
                        //取服务端响应的结果
                        vm.discountList = response.data.rows;
                        vm.total = response.data.total;
                    }).catch(function (reason) {
                    console.log(reason);
                });
                vm.axios.post(vm.API.GOODS_FINDBYSELLER_URL+"?username="+this.username)
                    .then(function (response) {
                        //取服务端响应的结果
                        console.log(response);
                        vm.goodsList = response.data;
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            getGoodsId(){
                console.log(this.discount);
            },
            handleSelectionChange(val) {
                this.discountSelection = val;
                console.log(this.discountSelection)
            },
            updateDiscount(id) {
                let vm = this;
                vm.axios.get(vm.API.DISCOUNT_FINDBYID_URL+"?id="+id).then(res=>{
                    vm.discount = res.data;
                })
            },
            clear() {
                this.discount={};
            },
            saveDiscount() {
                let vm = this;
                this.discount.sellerId = this.username;
                var myreg= new RegExp("^[1-9][0-9]*$");
                if(!myreg.test(this.discount.discount)){
                    this.$message.warning("请输入正确的折扣")
                    return;
                }
                if(!myreg.test(this.discount.conditions)){
                    this.$message.warning("请输入正确的条件")
                    return;
                }
                if(!myreg.test(this.discount.num)){
                    this.$message.warning("请输入正确的数量")
                    return;
                }
                if(!this.discount.expirationTime){
                    this.$message.warning("请选择过期时间")
                    return;
                }
                let url;
                if(!vm.discount.id){
                    url = vm.API.DISCOUNT_SAVEDISCOUNT_URL;
                }else{
                    url = vm.API.DISCOUNT_UPDATEDISCOUNT_URL;
                }
                vm.axios.post(url, this.discount).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.pageHandler(1);
                        vm.discount = {};
                    } else {
                        vm.$message.success(res.data.message)
                    }
                })

            },
            deleteDiscount(id){
                let vm = this;
                vm.axios.get(vm.API.DISCOUNT_DELETEDISCOUNT_URL+"/"+id).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.pageHandler(1);
                    } else {
                        vm.$message.success(res.data.message)
                    }
                })
            },
            deleteDiscountBySelection(){
                let vm = this;
                vm.axios.post(vm.API.DISCOUNT_DELETEDISCOUNTBYSELECTION_URL,{idList:this.discountSelection}).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.pageHandler(1);
                        vm.discount = {};
                    } else {
                        vm.$message.success(res.data.message)
                    }
                })
            }
        },
        created: function () {
            this.username = this.Storage.Session.get("username");
            this.pageHandler(1);
        },
        filters: {
            moneyFormat(value) {
                return "￥" + value.toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(20%);
        width: 800px;
        height: 600px;
    }

    #header {
        width: 100%;
        height: 80px;
        display: flex;
        align-items: center;
        margin-left: 20px;
        border-bottom: 1px solid #fff;
    }
</style>