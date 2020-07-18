<template>
    <div class="content_center">
        <div class="mt-3">
            <el-card class="box-card">
                <div>
                    <span style="color: #909399;font-size: 30px;">订单号:{{orderId}}</span>
                </div>
                <div>
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
                                    width="140">
                            </el-table-column>
                            <el-table-column
                                    prop="goodsId"
                                    width="180"
                                    label="商品Id">
                            </el-table-column>
                            <el-table-column
                                    label="商品"
                                    width="400">
                                <template slot-scope="scope">
                                    <img :src="scope.row.picPath" width="80" height="80" alt="">
                                    <span>{{scope.row.title}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="num"
                                    label="数量"
                                    width="140">
                            </el-table-column>
                            <el-table-column
                                    label="价格"
                                    width="140">
                                <template slot-scope="scope">
                                    ￥{{scope.row.price}}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="总金额"
                                    align="center"
                                    width="140">
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
            <el-card class="mt-3">
                <div>
                    <span style="color: #909399;font-size: 25px;">评价:</span>
                </div>
                <div class="mt-2 mb-3">
                    <span style="color: #909399;">商品评分:</span>
                    <el-rate
                            v-model="starts"
                            show-text
                            style="display: inline-block;margin-left: 10px;"
                            :colors="colors"
                            :texts="texts">
                    </el-rate>
                </div>
                <el-input class="mb-3"
                          type="textarea"
                          :autosize="{ minRows: 2, maxRows: 4}"
                          placeholder="请输入内容"
                          v-model="content">
                </el-input>
                <el-upload
                        :action="this.API.UPLOAD_UPLOADFILE_URL"
                        :headers="importHeader"
                        list-type="picture-card"
                        :on-preview="handlePictureCardPreview"
                        :on-success="fileSuccess"
                        :on-remove="handleRemove">
                    <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
                <el-button style="float: right;margin:10px" @click="saveComment()" type="success">发布</el-button>
                <el-button style="float: right;margin:10px" @click="goto('/index/home')" type="danger">返回</el-button>
            </el-card>
    </div>
</template>

<script>
    export default {
        name: "CommentsShop",
        data() {
            return {
                orderId: '',
                orderItemList: [],
                page: 1,
                pageSize: 5,
                total: 0,
                starts: null,//评价星星
                content: '',
                texts: ['极差', '差', '一般', '好', '非常好'],
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                imageList: [],
                dialogVisible: false,
                importHeader: {},
                dialogImageUrl: '',
                username: '',
                flag: false,
                orderItem: {},
            }
        },
        methods: {
            selectShop(goodsId) {
                this.flag = true;
                let vm = this;
                let list = [];
                list.push(goodsId)
                    vm.axios.post(vm.API.COMMENTS_FINDORDERITEMBYGOODSID_URL, {"idList": list}).then(res => {
                    console.log(res);
                    vm.orderItem = res.data[0];
                })
            },
            loadOrderItemByOrderId(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.ORDERITEM_FINDBYORDERID_URL + "?orderId=" + vm.orderId + "&page=" + vm.page + "&pageSize=" + vm.pageSize).then(res => {
                    vm.total = res.data.total;
                    vm.orderItemList = res.data.rows;
                })
            },
            fileSuccess(res, file) {
                if (res.success) {
                    this.$message.success("上传成功");
                    this.dialogImageUrl = res.message;
                    this.imageList.push({'url': res.message});
                    console.log(this.imageList)
                } else {
                    this.$message.error(res.message);
                }
            },
            handleRemove(file) {
                let vm = this;
                vm.axios.get(vm.API.DELETE_UPLOADURL_URL, {params: {url: file.response.message}}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            saveComment() {
                let comment = {
                    starts: this.starts,
                    content: this.content,
                    picUrl: this.imageList,
                    userId: this.username,
                }
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_SAVE_URL + "?orderId=" + this.orderId, comment).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.$router.push({path: '/index/home'});
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            goto(path) {
                this.$router.push({path});
            }
        },
        created() {
            this.importHeader['jsessionid'] = this.cookieUtil.getCookie('jsessionid');
            this.orderId = this.$route.params.orderId;
            this.username = this.Storage.Session.get("username");
            this.loadOrderItemByOrderId(1);
        },
        filters: {
            moneyFormat(value) {
                return "￥" + value.toFixed(2);
            }
        }
    }
</script>

<style>
    .el-rate__icon {
        font-size: 25px;
    }
</style>