<template>
    <div>
        <div id="header">
            <h2>广告管理</h2>
        </div>
        <div style="margin: 20px;">
            <el-button type="info" @click="clearContent" data-toggle="modal" data-target="#editModal" plain>新建
            </el-button>
            <el-button type="info" @click="deleteContent" plain>删除</el-button>
            <el-button type="info" plain>开启</el-button>
            <el-button type="info" plain>屏蔽</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right">
                广告标题:&nbsp;&nbsp;
                <el-input placeholder="请输入广告标题" @change="pageHandler(1)" v-model="searchContent.title"
                          style="width: 300px;margin-right: 20px;">
                </el-input>
                <el-button style="width: 100px;margin-right: 15px;" icon="el-icon-search"></el-button>
            </div>
        </div>
        <div>
            <template>
                <el-table
                        ref="multipleTable"
                        :data="contentList"
                        tooltip-effect="dark"
                        @selection-change="handleSelectionChange"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            label="广告ID"
                            prop="id"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="categoryId"
                            label="分类ID"
                            width="220">
                        <template slot-scope="scope">
                            {{category[scope.row.categoryId]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="title"
                            label="标题"
                            width="220">
                    </el-table-column>
                    <el-table-column
                            prop="url"
                            label="URL"
                            width="320">
                    </el-table-column>

                    <el-table-column
                            prop="pic"
                            label="图片"
                            width="320">
                        <template slot-scope="scope">
                            <img :src="scope.row.pic" width="200" height="100"/>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="sortOrder"
                            label="排序"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="status"
                            align="center"
                            label="是否有效"
                            width="120">
                        <template slot-scope="scope">
                            {{status[scope.row.status]}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button data-toggle="modal" @click="updateContent(scope.row.id)" data-target="#editModal"
                                       type="primary" size="small">修改
                            </el-button>
                        </template>

                    </el-table-column>
                </el-table>
            </template>
        </div>
        <!-- 编辑窗口 -->
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel">广告编辑</h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped" width="800px">
                            <tr>
                                <td>广告分类</td>
                                <td>
                                    <template>
                                        <el-select value-key="id" v-model="selCategory"
                                                   placeholder="请选择">
                                            <el-option
                                                    v-for="(item,index) in categoryList"
                                                    :key="index"
                                                    :label="item.name"
                                                    :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </td>
                            </tr>
                            <tr>
                                <td>标题</td>
                                <td><input class="form-control" placeholder="标题" v-model="content.title"></td>
                            </tr>
                            <tr>
                                <td>URL</td>
                                <td><input class="form-control" placeholder="URL" v-model="content.url"></td>
                            </tr>
                            <tr>
                                <td>排序</td>
                                <td><input class="form-control" placeholder="排序" v-model="content.sortOrder"></td>
                            </tr>
                            <tr>
                                <td>广告图片</td>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                <input type="file" id="file"/>
                                                <button @click="uploadFile" class="btn btn-primary" type="button">
                                                    上传
                                                </button>
                                            </td>
                                            <td>
                                                <img :src="imgUrl" width="200px" height="100px">
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>是否有效</td>
                                <td>
                                    <input type="checkbox" v-model="content.status" :true-value="1" :false-value="0"
                                           class="icheckbox_square-blue">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" @click="saveContent()" data-dismiss="modal" aria-hidden="true">
                            保存
                        </button>
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
                contentList: [],
                contentSelection: [],
                searchContent: {
                    title: '',
                },
                page: 1, //page
                pageSize: 5,//pageSize,  default is 10
                total: 0, //total item count
                status: {
                    0: '无效',
                    1: '有效',
                },
                content: {},//广告实体
                imgUrl:'',
                categoryList: [],//所有分类集合
                selCategory: '',//选择的分类id
                category: {}//分类id
            }
        },

        methods: {
            handleSelectionChange(val) {
                this.contentSelection = val;
                console.log(this.contentSelection)
            },
            pageHandler(page) {
                let vm = this;
                this.page = page;
                vm.axios.post(vm.API.CONTENT_FINDALL_URL + "?page=" + vm.page + "&rows=" + vm.pageSize, vm.searchContent).then(res => {
                    console.log(res);
                    vm.contentList = res.data.rows;
                    vm.total = res.data.total;
                })

                vm.axios.post(vm.API.CONTENT_FINDALLCATEGORY_URL).then(res => {
                    vm.categoryList = res.data;
                    for (let i = 0; i < res.data.length; i++) {
                        let a = res.data[i];
                        vm.category[a.id] = a.name;
                    }
                })
            },
            clearContent() {
                this.content = {};
                this.selCategory = '';
                this.imgUrl='';
            },
            deleteContent() {
                let vm = this;
                vm.axios.post(vm.API.CONTENT_DELETE_URL, {idList: vm.contentSelection}).then(res => {
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
            saveContent() {
                let vm = this;
                let url = '';
                if (vm.content.id != null) {
                    url = vm.API.CONTENT_UPDATE_URL;
                } else {
                    url = vm.API.CONTENT_SAVE_URL;
                }
                vm.content.pic = this.imgUrl;
                vm.content.categoryId = vm.selCategory;
                if(vm.content.status == null || vm.content.status ==''){
                    vm.content.status='0';
                }
                if(vm.selCategory== null || vm.selCategory ==''){
                    this.$message.error("请选择广告分类");
                    return;
                }
                if(vm.content.title== null || vm.content.title ==''){
                    this.$message.error("请填写标题");
                    return;
                }
                if(vm.content.sortOrder== null || vm.content.sortOrder ==''){
                    this.$message.error("请填写排序");
                    return;
                }else {
                    let numReg = /^[0-9]*$/
                    let numRe = new RegExp(numReg);
                    if(!numRe.test(vm.content.sortOrder)){
                        this.$message.error("请正确填写排序");
                        return;
                    }
                }
                if(vm.content.url== null || vm.content.url ==''){
                    this.$message.error("请填写url");
                    return;
                }
                if(this.imgUrl == ''){
                    this.$message.error("请上传广告图片");
                    return;
                }
                vm.axios.post(url, vm.content).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.pageHandler(1);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            updateContent(id) {
                let vm = this;
                vm.axios.get(vm.API.CONTENT_FINDBYID_URL + "?id=" + id).then(res => {
                    vm.selCategory = res.data.categoryId;
                    vm.content.id = res.data.id;
                    vm.content.pic = res.data.pic;
                    vm.imgUrl = res.data.pic;
                    vm.content.sortOrder = res.data.sortOrder;
                    vm.content.title = res.data.title;
                    vm.content.url = res.data.url;
                    vm.content.status = res.data.status;
                }).catch(reason => {
                    console.log(reason);
                })
            },
            uploadFile() {
                let formData = new FormData();
                formData.append('file', file.files[0])
                let vm = this;
                vm.axios.post(this.API.UPLOAD_UPLOADFILE_URL, formData).then(function (response) {
                    vm.imgUrl = response.data.message;
                }).catch(function (reason) {
                    console.log(reason);
                });
            },
        },
        created() {
            this.pageHandler(1);
        }
    }
</script>

<style scoped>
    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(10%);
        width: 800px;
        height: 700px;
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