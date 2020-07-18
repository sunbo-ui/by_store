<template>
    <div>
        <div id="header">
            <h2>分类管理</h2>
        </div>
        <el-breadcrumb separator-class="el-icon-arrow-right" >
            <el-breadcrumb-item><a @click="grade=1,nextGrade({id:0})">首页</a></el-breadcrumb-item>
            <el-breadcrumb-item><a  @click="grade=2,nextGrade(entity1)">{{entity1.name}}</a></el-breadcrumb-item>
            <el-breadcrumb-item><a @click="grade=3,nextGrade(entity2)">{{entity2.name}}</a></el-breadcrumb-item>
        </el-breadcrumb>

        <div style="margin: 20px;">
            <el-button type="info" plain>新建</el-button>
            <el-button type="info" plain>删除</el-button>
            <el-button type="info" plain>刷新</el-button>
            <div style="float: right">
                分类模板名称:&nbsp;&nbsp;
                <el-input placeholder="请输入内容" style="width: 300px;margin-right: 20px;" v-model="search" class="input-with-select">
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
                        style="width: 100%"
                        >
                    <el-table-column
                            type="selection"
                            width="155">
                    </el-table-column>
                    <el-table-column
                            label="分类ID"
                            prop="id"
                            width="320">
                    </el-table-column>

                    <el-table-column
                            prop="name"
                            label="分类名称"
                            width="400">
                    </el-table-column>
                    <el-table-column
                            prop="typeId"
                            label="类型模板ID"
                            width="320">
                    </el-table-column>
                    <el-table-column
                            prop="address"
                            label="操作"
                            align="center"
                            show-overflow-tooltip>
                        <template slot-scope="scope">
                            <el-button @click=" setGrade(grade+1) ,nextGrade(scope.row)"  type="primary" size="small">查询下级</el-button>
                            <el-button  data-toggle="modal" data-target="#editModal" type="primary" size="small">修改</el-button>
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
                        <h3 id="myModalLabel">商品分类编辑</h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped"  width="800px">
                            <tr>
                                <td>上级商品分类</td>
                                <td>
                                    男装 >> 卫衣
                                </td>
                            </tr>
                            <tr>
                                <td>商品分类名称</td>
                                <td><input  class="form-control" placeholder="商品分类名称">  </td>
                            </tr>
                            <tr>
                                <td>类型模板</td>
                                <td>
                                    <input select2 config="options['type_template']" placeholder="商品类型模板" class="form-control" type="text"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" data-dismiss="modal" aria-hidden="true">保存</button>
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
                search:"",
                categoryList: [],
                multipleSelection: [],
                grade:1, /*当前级别*/
                entity1:{}, /*面包屑导航1*/
                entity2:{} ,/*面包屑导航2*/
                page:1,
                pageSize:10,
                total:100,
                catId:0,
            }
        },

        methods: {
            pageHandler(page,id){
                this.page = page;
                let vm = this;
                id=vm.catId;
                vm.axios.post(vm.API.ITEMCAT_FINDBYPARENTID_URL+'?id='+id+"&page="+vm.page+"&rows="+vm.pageSize).then(res=>{
                    vm.categoryList = res.data.rows;
                    vm.total=res.data.total
                    console.log(res);
                })
            },
            setGrade:function(value){
                this.grade = value;
            },
            nextGrade:function (entity) { //当前点击的分类
                if (this.grade == 1){//如果当前是第一级, 面包屑导航为空
                    this.entity1 = {};
                    this.entity2 = {};
                }
                if (this.grade == 2){ //第2级,把当前的分类显示在第1个面包屑上
                    this.entity1 = entity;
                    this.entity2 = {};
                }
                if (this.grade == 3){ //第3级,把当前点击分类显示到第二个面包屑上
                    this.entity2 = entity;
                }
                this.catId=entity.id
                this.pageHandler(1,this.catId);
            }
        },
        created() {
            this.pageHandler(1,0);
        }
    }
</script>

<style scoped>
    .modal-content{
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(40%);
        width: 800px;
        height: 400px;
    }
    #header {
        width: 100%;
        height: 80px;
        display: flex;
        align-items: center;
        margin-left: 20px;
    }
    .el-breadcrumb{
        margin-left: 15px;
        font-size:20px;
    }
</style>