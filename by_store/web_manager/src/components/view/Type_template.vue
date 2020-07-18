<template>
    <div>
        <div id="header">
            <h2>模板管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button @click="clearBrandByUpdate" data-toggle="modal" data-target="#editModal" type="info" plain>新建
            </el-button>
            <el-button type="info" @click="deleteType" plain>删除</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right">
                分类模板名称:&nbsp;&nbsp;
                <el-input @change="pageHandler(1)" placeholder="请输入内容" style="width: 300px;margin-right: 20px;"
                          v-model="searchType.name" class="input-with-select">
                </el-input>
                <el-button @click="pageHandler(1)" style="width: 100px;margin-right: 15px;"
                           icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="typeList"
                        tooltip-effect="dark"
                        @selection-change="handleSelectionChange"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="模板ID"
                            prop="id"
                            width="120">
                    </el-table-column>

                    <el-table-column
                            prop="name"
                            label="分类模板名称"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            label="关联品牌"
                            width="320">
                        <template slot-scope="scope">
                            {{jsonToString(scope.row.brandIds,'name')}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="关联规格"
                            width="320">
                        <template slot-scope="scope">
                            {{jsonToString(scope.row.specIds,'specName')}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="	扩展属性"
                            width="220">
                        <template slot-scope="scope">
                            {{jsonToString(scope.row.customAttributeItems,'attr')}}
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" @click="findById(scope.row.id)" data-toggle="modal" data-target="#editModal" size="small">修改
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
                        <h3 id="myModalLabel" v-html=" title ?'模板编辑':'模板保存'"></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped" width="800px">
                            <tr>
                                <td>商品类型</td>
                                <td>
                                    <input v-model="addname" class="form-control" placeholder="商品类型">
                                </td>
                            </tr>
                            <tr>
                                <td>关联品牌</td>
                                <td>
                                    <template>
                                        <el-select value-key="id"  v-model="sel_brand_obj" multiple placeholder="请选择">
                                            <el-option
                                                    v-for="(item,index) in brandsOptions"
                                                    :key="index"
                                                    :label="item.name"
                                                    :value="item">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </td>
                            </tr>
                            <tr>
                                <td>关联规格</td>
                                <td>
                                    <el-select value-key="id" v-model="sel_spec_obj" multiple placeholder="请选择">
                                        <el-option
                                                v-for="(item,index) in specOptions"
                                                :key="index"
                                                :label="item.specName"
                                                :value="item">
                                        </el-option>
                                    </el-select>
                                </td>
                            </tr>
                            <tr>
                                <td>扩展属性</td>
                                <td>
                                    <div class="btn-group" style="margin-bottom: 10px;">
                                        <el-button type="button" @click="addRow()" class="btn-info" title="新增扩展属性"><i
                                                class="fa fa-file-o"></i> 新增扩展属性
                                        </el-button>
                                    </div>
                                    <table class="table table-bordered table-striped" width="800px">
                                        <thead>
                                        <tr>
                                            <td><input type="checkbox" class="icheckbox_square-blue"></td>
                                            <td>属性名称</td>
                                            <td>操作</td>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="(item,index) in otherExtends" :key="index">
                                            <td><input type="checkbox" class="icheckbox_square-blue"></td>
                                            <td><input v-model="item.attr"  class="form-control" placeholder="属性名称"></td>
                                            <td>
                                                <button type="button" @click="deleteTableRow(index)" class="btn btn-danger" title="删除"><i
                                                        class="fa fa-trash-o"></i> 删除
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" @click="saveType" data-dismiss="modal" v-html="title ?'编辑':'保存'" aria-hidden="true"></button>
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
                typeList: [],
                typeSelection: [],
                searchType: {},
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                title: true,
                brandsOptions: [],//存放所有品牌
                specOptions: [],//存放所有规格
                sel_brand_obj: [],//选中的品牌
                sel_spec_obj: [],//选中的规格
                addname:'',
                otherExtends:[],
                typeId:null,
            }
        },

        methods: {
            handleSelectionChange(val) {
                this.typeSelection = val;
                console.log(this.typeSelection)
            },
            deleteType(){
                let vm = this;
                vm.axios.post(vm.API.TYPE_DELETE_URL,{idList:vm.typeSelection}).then(res=>{
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
            addRow(){
                this.otherExtends.push({})
            },
            deleteTableRow(index){
                this.otherExtends.splice(index,1);
            },
            // 定义方法：获取JSON字符串中的某个key对应值的集合
            jsonToString(jsonStr, key) {
                // 将字符串转成JSOn:
                var jsonObj = JSON.parse(jsonStr);
                var value = "";
                if(jsonObj !=null ){
                    for (var i = 0; i < jsonObj.length; i++) {
                        if (i > 0) {
                            value += ",";
                        }
                        value += jsonObj[i][key];
                    }
                    if(value == undefined || value==""||value=='undefined'){
                        return "无"
                    }
                    return value
                }

                return "无";
            },
            pageHandler(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.TYPE_FINDPAGE_URL + "?page=" + vm.page + "&rows=" + vm.pageSize, vm.searchType).then(res => {
                    vm.typeList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            clearBrandByUpdate() {
                let vm = this;
                vm.sel_brand_obj=[];
                vm.sel_spec_obj=[];
                vm.addname='';
                vm.otherExtends=[];
                vm.typeId=null;
                vm.title=false;
            },
            selLoadData() {//定义加载选项数据方法
                let vm = this;
                vm.axios.get(vm.API.BRAND_SELECT_URL)
                    .then(function (response) {
                        vm.brandsOptions = response.data;
                    }).catch(function (reason) {
                    console.log(reason);
                })
                vm.axios.get(vm.API.SPEC_SELECT_URL)
                    .then(function (response) {
                        vm.specOptions = response.data;
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            saveType(){
                let vm = this;
                let url='';
                let enetiy={
                    id:vm.typeId,
                    name:this.addname,
                    specIds:JSON.stringify(this.sel_spec_obj),
                    brandIds:JSON.stringify(this.sel_brand_obj),
                    customAttributeItems:JSON.stringify(this.otherExtends),
                };
                if(vm.typeId != null){
                    url=vm.API.TYPE_UPDATE_URL;
                }else{
                    url=vm.API.TYPE_SAVE_URL;
                }
                vm.axios.post(url,enetiy).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    }else{
                        vm.$message.error(res.data.message);
                    }
                })

            },
            findById(id){
                let vm = this;
                vm.axios.get(vm.API.TYPE_FINDBYID_URL+"/"+id).then(res=>{
                    vm.sel_brand_obj=JSON.parse(res.data.brandIds);
                    vm.sel_spec_obj=JSON.parse(res.data.specIds);
                    vm.addname=res.data.name;
                    vm.otherExtends=JSON.parse(res.data.customAttributeItems);
                    vm.typeId=res.data.id;
                    vm.title=true;
                }).catch(reason => {
                    console.log(reason);
                })
            },
        },
        created() {
            this.pageHandler(1);
            this.selLoadData();
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

    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(20%);
        width: 800px;
    }

</style>