<template>
    <div>
        <div id="header">
            <h2>商品管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="goto" plain>新建</el-button>
            <el-button type="info" @click="deleteGoods" plain>删除</el-button>
            <el-button type="info" @click="updateStatus(1)" plain>提交审核</el-button>
            <el-button type="info" @click="updateStatus(0)" plain>撤销审核</el-button>
            <el-button type="info" plain>屏蔽</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right ">
                <div style="position: relative;left: -10px;display: inline-block">
                    状态：
                    <select v-model="searchEntity.auditStatus">
                        <option value="">全部</option>
                        <option value="0">未申请</option>
                        <option value="1">申请中</option>
                        <option value="2">审核通过</option>
                        <option value="3">已驳回</option>
                    </select>
                    <div style="display: inline-block;margin-left: 20px">
                        商品名称:
                        <el-input placeholder="请输入内容" style="width: 300px;" v-model="searchEntity.goodsName">
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
                        :data="goodsList"
                        @selection-change="handleSelectionChange"
                        tooltip-effect="dark"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="商品ID"
                            prop="id"
                            width="200">
                    </el-table-column>

                    <el-table-column
                            prop="goodsName"
                            label="商品名称"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            prop="price"
                            label="商品价格"
                            width="180">
                        <template slot-scope="scope">
                            {{scope.row.price | moneyFormat(scope.row.price)}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category1Id"
                            label="一级分类"
                            width="220">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category1Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category2Id"
                            label="二级分类"
                            width="220">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category2Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="category3Id"
                            label="三级分类"
                            width="220">
                        <template slot-scope="scope">
                            {{itemCatList[scope.row.category3Id]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="状态"
                            width="280">
                        <template slot-scope="scope">
                            {{status[scope.row.auditStatus]}}
                        </template>
                    </el-table-column>


                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" @click="updateGoods(scope.row.id)" size="small"
                                       data-toggle="modal" data-target="#editModal">修改
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
        data() {
            return {
                radio: 3,
                goodsList: [],
                goodsSelection: [],
                searchEntity: {},
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                status: {
                    0: "未申请",
                    1: "申请中",
                    2: "审核通过",
                    3: "已驳回"
                },
                itemCatList: {},//分类集合
            }
        },

        methods: {
            pageHandler(page) {
                let vm = this;
                this.page = page;
                let username = vm.Storage.Session.get("username");
                vm.axios.post(vm.API.GOODS_SEARCH_URL + "?page=" + page + "&rows=" + this.pageSize+"&username="+username, this.searchEntity)
                    .then(function (response) {
                        //取服务端响应的结果
                        vm.goodsList = response.data.rows;
                        vm.total = response.data.total;
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            updateGoods(id) {
                this.$router.push({
                    path: `/index/goodsEdit/${id}`,
                })
            },
            loadCatData() {
                let _this = this;
                _this.axios.post(_this.API.ITEMCAT_FINDALL_URL)
                    .then(function (response) {
                        for (let i = 0; i < response.data.length; i++) {
                            let category = response.data[i];
                            _this.itemCatList[category.id] = category.name;
                        }
                        _this.pageHandler(1);
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            handleSelectionChange(val) {
                this.goodsSelection = val;
                console.log(this.goodsSelection)
            },
            deleteGoods() {
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
            },
            goto() {
                this.$router.push({path: "/index/goodsEdit"})
            },
            updateStatus(id) {
                let vm = this;
                this.axios.post(vm.API.GOODS_UPDATESTATUS_URL + "?id=" + id, {idList: vm.goodsSelection}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                }).catch(reason => {
                    vm.$message.warning("网络超时");
                })
            }
        },
        created: function () {
            this.loadCatData();
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