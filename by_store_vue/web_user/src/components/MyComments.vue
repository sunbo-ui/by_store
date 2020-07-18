<template>
    <div  class="p-2">
        <div style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">我的评论</div>
        <div style="padding: 10px">
            <el-card  v-if="commentsList.length == 0" >
                <span style="font-weight: bold;font-size: 20px;">亲~还没有为商品评价过呢</span>
            </el-card>
            <el-card v-else style="margin-top: 10px;" v-for="(item,index) in commentsList">
                <div>
                    <img src="../assets/logo.png" width="50" height="50" alt="">
                    <span style="margin-left: 10px;">{{userId}}</span>
                    <el-rate
                            v-model="item.starts"
                            show-text
                            style="display: inline-block;margin-left: 10px;"
                            :colors="colors"
                            :texts="texts">
                    </el-rate>
                    <div style="margin-top: 10px;">
                        <span style="color: #C0C4CC">{{item.createTime}}</span>
                    </div>
                    <p style="margin-top: 10px;">
                        <span>{{item.content.substr(0,20)}}</span>
                        <span v-if="item.content.length>20">........</span>
                    </p>
                    <div style="background-color: #EBEEF5;display: flex">
                        <div style="flex: 1">
                            <img :src="orderItemList[index].picPath" width="150" height="150" alt="">
                        </div>
                        <div style="flex: 4;margin-top: 10px;position: relative">
                            <span style="color: #606266;font-size: 22px;">{{orderItemList[index].title}}</span>
                            <div style="position:absolute;left:-10px;top:80px;color: #333;font-size: 25px;">
                                ￥{{orderItemList[index].price}}
                            </div>
                        </div>
                    </div>
                    <div style="padding: 10px;float: right">
                        <el-button type="primary" @click="findById(item.id)" data-toggle="modal"
                                   data-target="#editModal">修改评论
                        </el-button>
                        <el-button type="danger" @click="deleteComments(item.id)">删除评论</el-button>
                    </div>
                </div>
            </el-card>
            <!--分页-->
            <div style="margin-top: 10px;">
                <el-pagination
                        @current-change="loadComment"
                        :current-page.sync="page"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="total">
                </el-pagination>
            </div>

            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 v-html="'评论编辑'"></h3>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        </div>
                        <div class="modal-body">
                            <el-card class="mt-3">
                                <div>
                                    <span style="color: #909399;font-size: 25px;">评价:</span>
                                </div>
                                <div class="mt-2 mb-3">
                                    <span style="color: #909399;">商品评分:</span>
                                    <el-rate
                                            v-model="comment.starts"
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
                                          v-model="comment.content">
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
                                <div @mouseover="overShow" @mouseout="outHide()"
                                     style="margin-top: 10px; margin-right: 10px;display: inline-block;position: relative"
                                     v-for="image in imageList">
                                    <img :class="flag?'opacity':''" width="200" :src="image.url" alt="" height="200">
                                    <span style="font-weight: bold;font-size: 30px;cursor: pointer;position: absolute;left: 170px;top:0"
                                          @click="removeImage(image.url,comment.id)">×</span>
                                </div>
                            </el-card>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-success" @click="updateComment()" v-html="'编辑'" data-dismiss="modal"
                                    aria-hidden="true">
                            </button>
                            <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Comments",
        data() {
            return {
                texts: ['极差', '差', '一般', '好', '非常好'],
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                commentsList: [],
                comment: {},
                page: 1,
                pageSize: 2,
                total: 0,
                userId: '',
                goodsIdList: [],
                orderItemList: [],
                importHeader: {},
                dialogImageUrl: '',
                dialogVisible: false,
                imageList: [],
                flag: false,
            }
        },
        methods: {
            overShow() {
                this.flag = true;
            },
            outHide() {
                this.flag = false;
            },
            loadComment(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_FINDBYUSERID_URL + "?userId=" + vm.userId + "&page=" + vm.page + "&pageSize=" + vm.pageSize).then(res => {
                    console.log(res);
                    vm.commentsList = res.data.rows;
                    vm.total = res.data.total;
                    vm.commentsList.forEach(item => {
                        vm.goodsIdList.push(item.goodsId);
                    })
                    vm.loadOrderItem();
                })
            },
            loadOrderItem() {
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_FINDORDERITEMBYGOODSID_URL, {"idList": vm.goodsIdList}).then(res => {
                    console.log(res);
                    vm.orderItemList = res.data;
                })
            },
            deleteComments(id) {
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_DELETECOMMENTS_URL + "?id=" + id).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.loadComment(1);
                    } else {
                        vm.$message.error(res.data.message)
                    }
                })
            },
            findById(id) {
                let vm = this;
                vm.axios.get(vm.API.COMMENTS_FINDBYID_URL + "?id=" + id).then(res => {
                    vm.comment = res.data;
                    vm.imageList = JSON.parse(vm.comment.picUrl);
                })
            },
            updateComment() {
                let vm = this;
                vm.comment.picUrl = vm.imageList;
                vm.axios.post(vm.API.COMMENTS_UPDATECOMMENTS_URL, vm.comment).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.loadComment(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
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
            removeImage(url, id) {
                let vm = this;
                vm.axios.get(vm.API.DELETE_UPLOADURL_URL, {params: {url: url}}).then(res => {
                    if (res.data.success) {
                        vm.$message.success("删除成功");
                        let idx = this.imageList.indexOf(url);
                        this.imageList.splice(idx, 1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })

            },
            handleRemove: function (file) {
                let vm = this;
                vm.axios.get(vm.API.DELETE_UPLOADURL_URL, {params: {url: file.response.message}}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        let idx = this.imageList.indexOf(file.response.message);
                        this.imageList.splice(idx, 1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
        },
        created() {
            this.importHeader['jsessionid'] = this.cookieUtil.getCookie('jsessionid');
            this.userId = this.Storage.Session.get("username");
            this.loadComment(1);
        }
    }
</script>

<style scoped>
    .modal-content {
        width: 1200px;
        position: absolute;
        top: 0%;
        left: 50%;
        transform: translateX(-50%);
    }

    .opacity {
        opacity: 0.3;
    }

</style>