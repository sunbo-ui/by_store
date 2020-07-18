<template>
    <div>
        <div id="header">
            <h2>品牌管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="clearBrandByUpdate" data-toggle="modal" data-target="#editModal" plain>新建
            </el-button>
            <el-button type="info" @click="deleteBrand" plain>删除</el-button>
            <el-button type="info" plain @click="reload">刷新</el-button>
            <div style="float: right;">
                <div>
                    品牌名称:
                    <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="searchBrand.name"
                              class="input-with-select" @change="pageHandler(1)">
                    </el-input>
                    品牌首字母:
                    <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="searchBrand.firstChar"
                              class="input-with-select"  @change="pageHandler(1)">
                    </el-input>
                    <el-button style="width: 100px;margin-right: 15px;" @click="pageHandler(1)" icon="el-icon-search"></el-button>
                </div>
            </div>
        </div>

        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="brandList"
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="品牌ID"
                            prop="id"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="name"
                            label="品牌名称"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="firstChar"
                            label="品牌首字母"
                            width="420">
                    </el-table-column>

                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button type="primary" @click="findByid(scope.row.id)" size="small" data-toggle="modal"
                                       data-target="#editModal">修改
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
                        <h3  v-html="title ? '品牌编辑':'品牌保存'"></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped" width="800px">
                            <tr>
                                <td>品牌名称</td>
                                <td><input v-model="brand.name" class="form-control" placeholder="品牌名称"></td>
                            </tr>
                            <tr>
                                <td>首字母</td>
                                <td><input v-model="brand.firstChar" class="form-control" placeholder="首字母"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" @click="saveBrand"  v-html="title ? '编辑':'保存'" data-dismiss="modal" aria-hidden="true">
                        </button>
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
                brandList: [],
                searchBrand: {},
                brand: {
                    id:'',
                    name: '',
                    firstChar: ''
                },//品牌对象
                page: 1, //page
                pageSize: 10,//pageSize,  default is 10
                total: 0, //total item count
                brandSelection: [],//选择出来要删除的
                title:true,
            }
        },

        methods: {
            handleSelectionChange(val) {
                this.brandSelection = val;
                console.log(this.brandSelection)
            },
            pageHandler(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.BRAND_FINDPAGE_URL+"?page="+vm.page+"&rows="+vm.pageSize,vm.searchBrand).then(res => {
                    vm.brandList = res.data.rows;
                    vm.total = res.data.total;
                }).catch(reason => {
                    console.log(reason + "分页网络超时");
                })
            },
            clearBrandByUpdate() {
                this.brand.name = '';
                this.brand.id = '';
                this.brand.firstChar = ''
                this.title = false;
            },
            saveBrand() {
                let vm = this;
                let url = '';
                if (vm.brand.id != null && vm.brand.id != '') {
                    url=vm.API.BRAND_UPDATE_URL
                } else {
                    url= vm.API.BRAND_SAVE_URL
                }
                vm.axios.post(url, vm.brand).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            deleteBrand() {
                let vm = this;
                vm.axios.post(vm.API.BRAND_DELETE_URL, {idList: vm.brandSelection}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            findByid(id) {
                let vm = this;
                vm.axios.get(vm.API.BRAND_FINDBYID_URL + "/" + id).then(res => {
                    vm.brand.id = res.data.id;
                    vm.brand.name = res.data.name;
                    vm.brand.firstChar = res.data.firstChar;
                    vm.title = true;
                })
            },
            reload(){
                this.$router.go(0)
            }
        },
        created() {
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

    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(45%);
    }

</style>