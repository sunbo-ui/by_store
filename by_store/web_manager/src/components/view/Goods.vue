<template>
    <div>
        <div id="header">
            <h2>商品审核</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="updateStatus(2)" plain>审核通过</el-button>
            <el-button type="info" @click="deleteShop()" plain>删除</el-button>
            <el-button type="info" @click="updateStatus(3)" plain>驳回</el-button>
            <el-button type="info" plain>刷新</el-button>
            <span style="margin-left: 50px; padding-right: 10px;">状态:</span>
            <template>
                <el-radio-group v-model="radio" @change="changeStatus(1)">
                    <el-radio :label="-1">全部</el-radio>
                    <el-radio :label="0">未申请</el-radio>
                    <el-radio :label="1">申请中</el-radio>
                    <el-radio :label="2">审核通过</el-radio>
                    <el-radio :label="3">已驳回</el-radio>
                </el-radio-group>
            </template>
            <div style="float: right">
                商品名称:&nbsp;&nbsp;
                <el-input @change="pageHandler(1)" placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="searchGoods.goodsName" class="input-with-select">
                </el-input>
                <el-button @click="pageHandler(1)" style="width: 100px;margin-right: 15px;" icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="goodsList"
                        tooltip-effect="dark"
                        @selection-change="handleSelectionChange"
                        style="width: 100%"
                        >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="商品ID"
                            prop="id"
                            width="220">
                    </el-table-column>

                    <el-table-column
                            prop="goodsName"
                            label="商品名称"
                            width="280">
                    </el-table-column>
                    <el-table-column
                            prop="price"
                            label="商品价格"
                            width="220">
                        <template slot-scope="scope">
                            {{scope.row.price | moneyFormat(scope.row.price)}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category1Id"
                            label="一级分类"
                            width="250">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category1Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category2Id"
                            label="二级分类"
                            width="200">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category2Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category3Id"
                            label="三级分类"
                            width="250">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category3Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="auditStatus"
                            label="状态"
                            width="220">
                        <template slot-scope="scope">
                            {{status[scope.row.auditStatus]}}
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <!--分页-->
            <div class="block">
                <el-pagination
                        @current-change="pageHandler,changeStatus"
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
    export default {
        data() {
            return {
                radio: -1,
                goodsList: [],
                goodsSelection: [],
                searchGoods:{
                    goodsName:""
                },
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0,
                status:{
                    0:"未申请",
                    1:"申请中",
                    2:"审核通过",
                    3:"已驳回"
                },
                itemCatList:{},//分类集合
            }
        },

        methods: {
            changeStatus(page){
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.GOODS_FINDBYSTATUS_URL+ "?page=" + vm.page + "&rows=" + vm.pageSize+"&status="+vm.radio).then(res => {
                    vm.goodsList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            handleSelectionChange(val) {
                this.goodsSelection = val;
                console.log(this.goodsSelection)
            },
            pageHandler(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.GOODS_SEARCH_URL + "?page=" + vm.page + "&rows=" + vm.pageSize, vm.searchGoods).then(res => {
                    console.log(res);
                    vm.goodsList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            loadCatData() {
                let _this = this;
                _this.axios.post(_this.API.ITEMCAT_FINDALL_URL)
                    .then(function (response) {
                        for (let i = 0; i < response.data.length; i++){
                            let category =  response.data[i];
                            _this.itemCatList[category.id] = category.name;
                        }
                        _this.pageHandler(1);
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            updateStatus(id){
                let vm = this;
                vm.axios.post(vm.API.GOODS_UPDATESTATUS_URL+"?id="+id,{idList:vm.goodsSelection}).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                }).catch(reason => {
                    console.log(reason);
                })
            },
            deleteShop(){
                let vm = this;
                vm.axios.post(vm.API.GOODS_DELETE_URL, {idList: vm.goodsSelection}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                }).catch(reason => {
                    console.log(reason);
                })
            }
        },
        created() {
            this.loadCatData();
        }
        ,filters:{
            moneyFormat(value){
                return "￥"+value.toFixed(2);
            }
        }
    }
</script>

<style scoped>

    #header {
        width: 100%;
        height: 80px;
        display: flex;
        align-items: center;
        margin-left: 20px;
        border-bottom: 1px solid #fff;
    }
</style>