<template>
    <div>
        <div id="header">
            <h2>广告分类管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info"  @click="clearContent" data-toggle="modal" data-target="#editModal" plain>新建</el-button>
            <el-button type="info" @click="deleteCategory" plain>删除</el-button>
            <el-button type="info" plain>开启</el-button>
            <el-button type="info" plain>屏蔽</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right">
                名称:&nbsp;&nbsp;
                <el-input placeholder="请输入内容" @change="pageHandler(1)" v-model="searchEntity.name" style="width: 300px;margin-right: 20px;">
                </el-input>
                <el-button style="width: 100px;margin-right: 15px;" icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="categoryList"
                        tooltip-effect="dark"
                        @selection-change="handleSelectionChange"
                        style="width: 100%"
                        >
                    <el-table-column
                            type="selection"
                            width="155">
                    </el-table-column>
                    <el-table-column
                            label="类目ID"
                            prop="id"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="name"
                            label="分类名称"
                            align="center"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button data-toggle="modal" @click="updateCategory(scope.row.id)" data-target="#editModal" type="primary" size="small">修改</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </div>
        <!-- 编辑窗口 -->
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog" >
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel">广告分类编辑</h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped"  width="800px">
                            <tr>
                                <td>分类名称</td>
                                <td><input  class="form-control" placeholder="分类名称" v-model="category.name">  </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" @click="saveCategory" data-dismiss="modal" aria-hidden="true">保存</button>
                        <button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">关闭</button>
                    </div>
                </div>
            </div>
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
                categoryList: [],
                categorySelection: [],
                searchEntity:{
                    name:"",
                },
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                category:{
                    id:null,
                    name:""
                }
            }
        },

        methods: {
            handleSelectionChange(val) {
                this.categorySelection = val;
                console.log(this.categorySelection)
            },
            pageHandler(page){
                let vm = this;
                this.page = page;
                vm.axios.post(vm.API.CONTENT_CATEGORY_FINDALL_URL+"?page="+vm.page+"&rows="+vm.pageSize,vm.searchEntity).then(res => {
                    console.log(res);
                    vm.categoryList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            clearContent(){
                this.category.name="";
                this.category.id = null;
            },
            saveCategory(){
                let vm = this;
                let url = '';
                if(vm.category.id != null){
                    url=vm.API.CONTENT_CATEGORY_UPDATE_URL;
                }else{
                    url=vm.API.CONTENT_CATEGORY_SAVE_URL;
                }
                vm.axios.post(url,vm.category).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            updateCategory(id){
                let vm = this;
                vm.axios.post(vm.API.CONTENT_CATEGORY_FINDBYID_URL+"?id="+id).then(res=>{
                    console.log(res);
                    vm.category.name = res.data.name
                   vm.category.id = res.data.id
                })
            },
            deleteCategory(){
                let vm = this;
                vm.axios.post(vm.API.CONTENT_CATEGORY_DELETE_URL,{idList:vm.categorySelection}).then(res=>{
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            }
        },
        created() {
            this.pageHandler(1);
        }
    }
</script>

<style scoped>
    .modal-content{
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(60%);
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