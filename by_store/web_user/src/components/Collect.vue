<template>
    <div>
        <div  class="p-2">
            <div>
                <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;"> 我的收藏</span>
                <el-button @click="deleteAll" style="float: right; margin-right: 10px;" v-if="collectList.length > 0" type="danger"  >全部删除</el-button>
            </div>
            <div style="padding: 10px">
                <el-card  v-if="collectList.length == 0" >
                    <span style="font-weight: bold;font-size: 20px;">亲~还没有心动的宝贝,再去逛逛吧</span>
                </el-card>
                <el-card v-else style="margin-top: 10px;" v-for="(item,index) in collectList">
                    <div style="background-color: #F2F6FC;display: flex">
                        <div style="flex: 1;cursor: pointer;" @click="goto(item.goodsId)">
                            <img  :src="item.picUrl" width="150" height="150" alt="">
                        </div>
                        <div style="flex: 4;margin-top: 10px;position: relative">
                            <span @click="goto(item.goodsId)"  style="cursor: pointer;color: #606266;font-size: 22px;">{{item.title}}</span>
                            <div style="position:absolute;left:0px;top:80px;color: #333;font-size: 25px;">
                                ￥{{item.price}}
                            </div>
                            <div style="float: right;position: absolute; top:87px;right: 10px">
                                <el-button type="primary" @click="findById(item.id)"   v-html="item.notes ? '修改备注':'添加备注'" data-toggle="modal" data-target="#editModal"></el-button>
                                <el-button type="danger" @click="deleteCollect(item.id)">删除收藏</el-button>
                            </div>
                        </div>
                    </div>
                    <div style="padding: 10px">
                        <span style="color: #909399">备注:</span>
                        <span style="display: block;padding: 10px">{{item.notes}}</span>
                    </div>
                </el-card>
                <!--分页-->
                <div style="margin-top: 10px;">
                    <el-pagination
                            @current-change="loadCollect"
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
                                <h3   v-html="collect.notes ? '编辑页面':'添加页面'"></h3>
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            </div>
                            <div class="modal-body">
                                <el-card>
                                    <div style="padding: 10px;color: #606266;font-size: 18px;"  v-html="collect.notes ? '编辑备注:':'添加备注:'"></div>
                                    <el-input class="mb-3"
                                              type="textarea"
                                              :autosize="{ minRows: 2, maxRows: 4}"
                                              placeholder="请输入内容"
                                              v-model="collect.notes">
                                    </el-input>
                                </el-card>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-success" @click="updateNotes" v-html="collect.notes ? '编辑':'保存'" data-dismiss="modal" aria-hidden="true">
                                </button>
                                <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Collect",
        data() {
            return {
                collectList: [],
                page: 1,
                pageSize: 5,
                total: 0,
                username: '',
                collect:{},
            }
        },
        methods: {
            loadCollect(page) {
                this.page = page;
                let vm = this;
                vm.axios.get(vm.API.COLLECT_FINDPAGE_URL, {
                    params: {
                        page: this.page,
                        pageSize: this.pageSize,
                        username: this.username
                    }
                }).then(res => {
                    console.log(res);
                    vm.total = res.data.total;
                    vm.collectList = res.data.rows;
                })
            },
            deleteCollect(id) {
                let vm = this;
                vm.axios.get(vm.API.COLLECT_DELETE_URL,{params:{id:id}}).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.loadCollect(1);
                    } else {
                        vm.$message.error(res.data.message)
                    }
                })
            },
            deleteAll(){
                let vm = this;
                vm.axios.get(vm.API.COLLECT_DELETEALL_URL,{params:{username: this.Storage.Session.get("username")}}).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.loadCollect(1);
                    } else {
                        vm.$message.error(res.data.message)
                    }
                })
            },
            findById(id){
                let vm = this;
                vm.axios.get(vm.API.COLLECT_FINDBYID_URL,{params:{id:id}}).then(res=>{
                    vm.collect = res.data;
                })
            },
            updateNotes(){
                let vm = this;
                vm.axios.post(vm.API.COLLECT_UPDATENOTES_URL,this.collect).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.loadCollect(1);
                    } else {
                        vm.$message.error(res.data.message)
                    }
                })
            },
            goto(id){
                window.location.href = this.API.BASE_PORTAL_URL+"/index/detailShop/"+id;
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadCollect(1);
        }
    }
</script>

<style scoped>
    .modal-content {
        position: absolute;
        width: 800px;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(35%);
    }
</style>