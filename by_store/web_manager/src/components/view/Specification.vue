<template>
    <div>
        <div id="header">
            <h2>规格管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="clearBrandByUpdate" data-toggle="modal" data-target="#editModal" plain>新建</el-button>
            <el-button type="info" @click="deleteSpec" plain>删除</el-button>
            <el-button type="info" @click="reload" plain>刷新</el-button>
            <div style="float: right">
                规格名称:&nbsp;&nbsp;
                <el-input placeholder="请输入内容"  @change="pageHandler(1)" style="width: 300px;margin-right: 20px;" v-model="searchSpec.specName" class="input-with-select">
                </el-input>
                <el-button style="width: 100px;margin-right: 15px;" @click="pageHandler(1)" icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="specList"
                        tooltip-effect="dark"
                        @selection-change="handleSelectionChange"
                        style="width: 100%"
                        >
                    <el-table-column
                            type="selection"
                            width="155">
                    </el-table-column>
                    <el-table-column
                            label="规格ID"
                            prop="id"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="specName"
                            label="规格名称"
                            align="center"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" @click="findById(scope.row.id)" size="small" data-toggle="modal" data-target="#editModal" >修改</el-button>
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
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog" >
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel" v-html="title ?'规格编辑':'规格保存'"></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped"  width="800px">
                            <tr>
                                <td>规格名称</td>
                                <td><input v-model="specEntity.specification.specName" class="form-control" placeholder="规格名称" ></td>
                            </tr>
                        </table>
                        <!-- 规格选项 -->
                        <div class="btn-group" style="margin-bottom: 10px;">
                            <button type="button" @click="addRow()" class="btn btn-info" title="新建" >
                                <i class="fa fa-file-o"></i>
                                新增规格选项
                            </button>
                        </div>
                        <table class="table table-bordered table-striped table-hover dataTable">
                            <thead>
                            <tr>
                                <th class="sorting">规格选项</th>
                                <th class="sorting">排序</th>
                                <th class="sorting">操作</th>
                            <tr/>
                            </thead>
                            <tbody>
                            <tr v-for="(pojo,index) in specEntity.specOptionList" :key="index">
                                <td><input v-model="pojo.optionName"  class="form-control" placeholder="规格选项"></td>
                                <td><input v-model="pojo.orders"  class="form-control" placeholder="排序"></td>
                                <td><button @click="deleteTableRow(index)" type="button" class="btn btn-danger" title="删除" ><i class="fa fa-trash-o"></i> 删除</button></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" v-html="title ?'编辑':'保存'" @click="saveSpec" data-dismiss="modal" aria-hidden="true"></button>
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
                radio: 3,
                title:true,
                specList: [],
                searchSpec:{
                    specName:''
                },
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                specSelection: [],//选择出来要删除的
                specEntity:{
                    specOptionList:[],
                    specification:{}
                }
            }
        },

        methods: {
            reload(){
                this.$router.go(0)
            },
            handleSelectionChange(val) {
                this.specSelection = val;
                console.log(this.specSelection)
            },
            deleteSpec(){
                let vm = this;
                vm.axios.post(vm.API.SPEC_DELETE_URL,{idList:vm.specSelection}).then(res=>{
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
            pageHandler(page){
                let vm = this;
                this.page = page;
                vm.axios.post(vm.API.SPEC_FINDPAGE_URL+"?page="+vm.page+"&rows="+vm.pageSize,vm.searchSpec).then(res => {
                    console.log(res);
                    vm.specList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            addRow(){
                this.specEntity.specOptionList.push({})
            },
            deleteTableRow(index){
                this.specEntity.specOptionList.splice(index,1);
            },
            saveSpec(){
                let vm = this;
                let url = '';
                if(vm.specEntity.specification.id != null){
                    url=vm.API.SPEC_UPDATE_URL;
                }else{
                    url=vm.API.SPEC_SAVE_URL;
                }
                vm.axios.post(url,vm.specEntity).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    }else{
                        vm.$message.error(res.data.message);
                    }
                }).catch(reason => {
                    console.log(reason);
                })
            },
            findById(id){
                let vm = this;
                vm.axios.get(vm.API.SPEC_FINDBYID_URL+"/"+id).then(res=>{
                    vm.specEntity.specOptionList = res.data.specOptionList;
                    vm.specEntity.specification = res.data.specification;
                    vm.title=true;
                }).catch(reason => {
                    console.log(reason);
                })
            },
            clearBrandByUpdate() {
                this.specEntity.specification={};
                this.specEntity.specOptionList=[];
                this.title=false;
            },
        },
        created() {
            this.pageHandler(1)
        }
    }
</script>

<style scoped>
    .modal-content{
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(20%);
        width: 800px;
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