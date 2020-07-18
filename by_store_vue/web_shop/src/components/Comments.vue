<template>
    <div>
        <div id="header">
            <h2>评论管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right;">
                <div>
                    客户Id:
                    <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;"
                              v-model="searchComment.userId" class="input-with-select" @change="pageHandler(1)">
                    </el-input>
                    <el-button style="width: 100px;margin-right: 15px;" @click="pageHandler(1)"
                               icon="el-icon-search"></el-button>
                </div>
            </div>
        </div>

        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="commentsList"
                        tooltip-effect="dark"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            prop="id"
                            label="评论Id"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="userId"
                            align="center"
                            label="客户Id"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="goodsId"
                            align="center"
                            label="商品Id"
                            width="300">
                    </el-table-column>
                    <el-table-column
                            prop="starts"
                            align="center"
                            label="评分"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="content"
                            align="center"
                            label="评价内容"
                            width="200">
                        <template slot-scope="scope">
                            {{scope.row.content.substr(0,4)}}
                            <span v-if="scope.row.content.length>4">........</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" size="mini"
                                       @click="goto(`/index/commentsDetail/${scope.row.id}`)">
                                查看详情
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
        name: "Comments",

        data() {
            return {
                commentsList: [],
                searchComment: {},
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                sellerId:'',
            }
        },

        methods: {
            pageHandler(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_FINDPAGEBySellerId_URL + "?page=" + vm.page + "&rows=" + vm.pageSize+"&sellerId="+this.sellerId, vm.searchComment).then(res => {
                    console.log(res);
                    vm.commentsList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            goto(path) {
                this.$router.push({path});
            },
        },
        created() {
            this.sellerId = this.Storage.Session.get("username");
            this.pageHandler(1);
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