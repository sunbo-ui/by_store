<template>
    <div>
        <div id="header">
            <h2>商家审核</h2>
        </div>
        <div style="margin-top: 15px;float: right;margin-bottom: 30px;">
            <div>
                公司名称:
                <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="entity.name"
                          @change="pageHandler(1)" class="input-with-select">
                </el-input>
                店铺名称:
                <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="entity.nickName"
                          @change="pageHandler(1)" class="input-with-select">
                </el-input>
                <el-button style="width: 100px;margin-right: 15px;" @click="pageHandler(1)"
                           icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="sellerList"
                        tooltip-effect="dark"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            prop="sellerId"
                            label="商家ID"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="公司名称"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="nickName"
                            label="店铺名称"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="linkmanName"
                            label="联系人姓名"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="linkmanMobile"
                            label="公司电话"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="status"
                            label="状态"
                            align="center"
                            width="320">
                        <template slot-scope="scope">
                            {{status[scope.row.status]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            align="center"
                            label="操作"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button data-toggle="modal"
                                       @click="findById(scope.row.sellerId)"
                                       data-target="#sellerModal" type="primary" size="small">详情
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

        <!-- 商家详情 -->
        <div class="modal fade"
             id="sellerModal" tabindex="-1"
             role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel">商家详情</h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">

                        <template>
                            <el-tabs v-model="activeName" type="card">
                                <el-tab-pane label="基本信息" name="first">
                                    <div class="tab-pane active in" id="home">
                                        <br>
                                        <table class="table table-bordered table-striped" width="800px">
                                            <tr>
                                                <td>公司名称</td>
                                                <td>{{entity.name}}</td>
                                            </tr>
                                            <tr>
                                                <td>公司手机</td>
                                                <td>{{entity.mobile}}</td>
                                            </tr>
                                            <tr>
                                                <td>公司电话</td>
                                                <td>{{entity.telephone}}</td>
                                            </tr>
                                            <tr>
                                                <td>公司详细地址</td>
                                                <td>{{entity.addressDetail}}</td>
                                            </tr>
                                        </table>
                                    </div>
                                </el-tab-pane>
                                <el-tab-pane label="联系人" name="second">
                                    <br>
                                    <table class="table table-bordered table-striped">
                                        <tr>
                                            <td>联系人姓名</td>
                                            <td>{{entity.linkmanName}}</td>
                                        </tr>
                                        <tr>
                                            <td>联系人QQ</td>
                                            <td>{{entity.linkmanQq}}</td>
                                        </tr>
                                        <tr>
                                            <td>联系人手机</td>
                                            <td>{{entity.linkmanMobile}}</td>
                                        </tr>
                                        <tr>
                                            <td>联系人E-Mail</td>
                                            <td>{{entity.linkmanEmail}}</td>
                                        </tr>
                                    </table>
                                </el-tab-pane>
                                <el-tab-pane label="证件" name="third">
                                    <br>
                                    <table class="table table-bordered table-striped">
                                        <tr>
                                            <td>营业执照号</td>
                                            <td>{{entity.licenseNumber}}</td>
                                        </tr>
                                        <tr>
                                            <td>税务登记证号</td>
                                            <td>{{entity.taxNumber}}</td>
                                        </tr>

                                    </table>
                                </el-tab-pane>
                                <el-tab-pane label="法定代表人" name="fourth">
                                    <br>
                                    <table class="table table-bordered table-striped">
                                        <tr>
                                            <td>法定代表人</td>
                                            <td>{{entity.legalPerson}}</td>
                                        </tr>
                                        <tr>
                                            <td>法定代表人身份证号</td>
                                            <td>{{entity.legalPersonCardId}}</td>
                                        </tr>
                                    </table>
                                </el-tab-pane>
                                <el-tab-pane label="开户行" name="five">
                                    <br>
                                    <table class="table table-bordered table-striped">
                                        <tr>
                                            <td>开户行名称</td>
                                            <td>{{entity.bankUser}}</td>
                                        </tr>
                                        <tr>
                                            <td>开户行支行</td>
                                            <td>{{entity.bankName}}</td>
                                        </tr>
                                        <tr>
                                            <td>银行账号</td>
                                            <td>{{entity.bankAccount}}</td>
                                        </tr>
                                    </table>
                                </el-tab-pane>
                            </el-tabs>
                        </template>

                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" data-dismiss="modal" @click="updateStatus(entity.sellerId,'1')" aria-hidden="true">审核通过</button>
                        <button class="btn btn-danger" data-dismiss="modal" @click="updateStatus(entity.sellerId,'2')" aria-hidden="true">审核未通过</button>
                        <button class="btn btn-danger" data-dismiss="modal" @click="updateStatus(entity.sellerId,'3')" aria-hidden="true">关闭商家</button>
                        <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                activeName: 'first',
                sellerList: [],
                brand: {},//品牌实体
                searchBrand: {},//搜索品牌实体
                multipleSelection: [],
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                entity: {},//查询时传递的对象
                status:{
                    0:"未审核",
                    1:"审核通过",
                    2:"审核未通过",
                    3:"已关闭商家"
                },
            }
        },

        methods: {
            pageHandler(page) {
                let vm = this;
                this.page = page;
                vm.axios.post(vm.API.SELLER_FINDPAGESTATUS_URL + "?page=" + vm.page + "&rows=" + vm.pageSize, vm.entity).then(res => {
                    vm.sellerList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            findById(id) {
                let vm = this;
                vm.axios.get(vm.API.SELLER_FINDBYID_URL, {params: {sellerId: id}}).then(res => {
                    vm.entity = res.data;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            updateStatus(id, status) {
                let vm = this;
                vm.axios.get(vm.API.SELLER_UPDATESTATUS_URL + "?sellerId=" + id + "&status=" + status).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.entity={};
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            }
        },
        created() {
            this.pageHandler(1)
        }
    }
</script>

<style scoped>
    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(30%);
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