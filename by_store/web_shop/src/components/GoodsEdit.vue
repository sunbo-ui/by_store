<template>
    <div>
        <template>
            <el-tabs v-model="activeName" type="card">
                <el-tab-pane label="商品基本信息" name="first" style="padding: 20px;">
                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;">商品分类:</div>
                        <div class="col-md-10 data">
                            <table>
                                <tr>
                                    <td>
                                        <template>
                                            <el-select value-key="id" v-model="catSelected1"
                                                       @change="getCatSelected(1)" placeholder="请选择">
                                                <el-option
                                                        v-for="(item,index) in categoryList1"
                                                        :key="index"
                                                        :label="item.name"
                                                        :value="item.id">
                                                </el-option>
                                            </el-select>
                                        </template>
                                    </td>
                                    <td>
                                        <template>
                                            <el-select value-key="id" v-model="catSelected2" @change="getCatSelected(2)"
                                                       placeholder="请选择">
                                                <el-option
                                                        v-for="(item,index) in categoryList2"
                                                        :key="index"
                                                        :label="item.name"
                                                        :value="item.id">
                                                </el-option>
                                            </el-select>
                                        </template>
                                    </td>
                                    <td>
                                        <template>
                                            <el-select value-key="id" v-model="catSelected3" @change="getCatSelected(3)"
                                                       placeholder="请选择">
                                                <el-option
                                                        v-for="(item,index) in categoryList3"
                                                        :key="index"
                                                        :label="item.name"
                                                        :value="item.id">
                                                </el-option>
                                            </el-select>
                                        </template>
                                    </td>
                                    <td>
                                        模板ID:{{tempType}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;">商品名称:</div>
                        <div class="col-md-10 data">
                            <el-input v-model="goodsEntity.goods.goodsName" placeholder="请输入商品名称"></el-input>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;text-indent:30px">品 牌 :</div>
                        <div class="col-md-10 data">
                            <template>
                                <el-select value-key="id" v-model="selBrand" @change="getCatSelected(3)"
                                           placeholder="请选择">
                                    <el-option
                                            v-for="(item,index) in brandList"
                                            :key="index"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </template>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;text-indent:10px">副 标 题:</div>
                        <div class="col-md-10 data">
                            <el-input v-model="goodsEntity.goods.caption" placeholder="副标题"></el-input>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;text-indent:10px">商 城 价:</div>
                        <div class="col-md-10 data">
                            <el-input v-model="goodsEntity.goods.price" placeholder="商 城 价"></el-input>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div class="title editer" style="font-size: 20px;">商品介绍:</div>
                        <div class="col-md-10 data editer">
                            <vue-ueditor-wrap ref="ue" @ready="ready" v-model="msg" :config="config"></vue-ueditor-wrap>
                        </div>
                    </div>


                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;">包装列表:</div>
                        <div class="col-md-10 data">
                            <textarea v-model="goodsEntity.goodsDesc.packageList" rows="4" class="form-control"
                                      placeholder="包装列表"></textarea>
                        </div>
                    </div>

                    <div class="row pl-4 pb-3">
                        <div style="font-size: 20px;">售后服务:</div>
                        <div class="col-md-10 data">
                            <textarea rows="4" v-model="goodsEntity.goodsDesc.saleService" class="form-control"
                                      placeholder="售后服务"></textarea>
                        </div>
                    </div>

                </el-tab-pane>
                <el-tab-pane label="商品图片" name="second">
                    <div style="padding: 20px;">
                        <div class="row data-type">
                            <!-- 颜色图片 -->
                            <div style="margin-bottom: 10px;">
                                <el-button type="success " data-target="#uploadModal" data-toggle="modal"><i
                                        class="fa fa-file-o"></i>新建
                                </el-button>
                            </div>
                            <table class="table table-bordered table-striped table-hover dataTable">
                                <thead>
                                <tr>
                                    <th class="sorting">颜色</th>
                                    <th align="center" class="sorting">图片</th>
                                    <th class="sorting">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(imgItem,index) in imgList" :key="index">
                                    <td>{{imgItem.color}}</td>
                                    <td align="center">
                                        <img alt="" :src="imgItem.url" width="100px" height="100px">
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger" title="删除"
                                                @click="delImg(imgItem.url,index)"><i class="fa fa-trash-o"></i> 删除
                                        </button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="规格" name="third">
                    <div v-show="isType">
                        <div class="row pl-4 pb-3">
                            <div class="col-md-2" style="font-weight: bold;font-size: 20px;">是否启用规格</div>
                            <div class="col-md-10 data">
                                <input type="checkbox" v-model="isEnableSpec" :true-value="1" :false-value="0">
                            </div>
                        </div>
                        <div v-if="isEnableSpec == 1">
                            <div>
                                <div class="row pl-4 pb-3" v-for="(item,index) in specList" :key="index">
                                    <div class="col-md-2 title" style="font-weight: bold;font-size: 20px;">
                                        {{item.specName}}
                                    </div>
                                    <div class="col-md-10 data">
                                        <span v-for="optionItem in item.options" :key="optionItem.id">
                                            <span style="margin-right: 20px;">
                                                <input type="checkbox"
                                                       :checked="checkAttributeValue(item.specName,optionItem.optionName)"
                                                       @click="updateSpecAttribute($event,item.specName,optionItem.optionName)">{{optionItem.optionName}}
                                            </span>
                                        </span>
                                    </div>
                                </div>
                                <div>
                                    <table class="table table-bordered table-striped table-hover dataTable">
                                        <thead>
                                        <tr>
                                            <th class="sorting" v-for="specItem in specificationItems">
                                                {{specItem.specName}}
                                            </th>
                                            <th class="sorting">价格</th>
                                            <th class="sorting">库存</th>
                                            <th class="sorting">是否启用</th>
                                            <th class="sorting">是否默认</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="row in rowList">
                                            <td v-for="specItem in specificationItems">
                                                {{row.spec[specItem.specName]}}
                                            </td>
                                            <td><input class="form-control" v-model="row.price" placeholder="价格"></td>
                                            <td><input class="form-control" v-model="row.num" placeholder="库存数量"></td>
                                            <td><input type="checkbox" v-model="row.status"></td>
                                            <td><input type="checkbox" v-model="row.isDefault"></td>
                                        </tr>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-show="!isType" style="height: 300px;">
                        <h1 style="color: darkslategray;text-align: center"> 请先选择模板哦!</h1>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </template>
        <!-- 上传窗口 -->
        <div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="myModalLabel">上传商品图片</h3>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped">
                            <tr>
                                <td>颜色</td>
                                <td><input class="form-control" v-model="imgColor" placeholder="请输入颜色"></td>
                            </tr>
                            <tr>
                                <td>图片</td>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                <input type="file" id="file"/>
                                                <button @click="uploadFile" class="btn btn-primary" type="button"
                                                        style="margin-top: 10px;">上传
                                                </button>
                                            </td>
                                            <td>
                                                <img :src="imgUrl" width="50px" height="50px" alt=""/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" data-dismiss="modal" @click="imgSave" aria-hidden="true">保存
                        </button>
                        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">关闭</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-toolbar list-toolbar">
            <button class="btn btn-success" @click="saveGoods" style="margin-left: 10px;">保存</button>
            <button class="btn btn-secondary" style="margin-left: 10px;">返回列表</button>
        </div>
        <div style="height:50px;"></div>
    </div>
</template>

<script>
    import VueUeditorWrap from 'vue-ueditor-wrap' // ES6 Module

    export default {
        data() {
            return {
                activeName: 'first',
                fileList: [],
                msg: '',
                config: {
                    UEDITOR_HOME_URL: '/umedit/',// 需要令此处的URL等于对应 ueditor.config.js 中的配置。
                    initialFrameWidth: 1384,
                    initialFrameHeight: 350,
                    // 编辑器不自动被内容撑高
                    autoHeightEnabled: false,
                    // 上传文件接口
                    serverUrl: this.API.BASE_SERVER_URL+'/umedit/jsp/controller.jsp',
                },
                categoryList1: [],//分类1数据列表
                categoryList2: [],//分类2数据列表
                categoryList3: [],//分类3数据列表
                grade: 1,  //记录当前级别
                catSelected1: null,//分类1选中的id
                catSelected2: null,//分类2选中的id
                catSelected3: null,//分类3选中的id,
                tempType: 0,//模板id
                brandList: [],
                selBrand: null,//选择品牌
                imgUrl: '', //上传图片的URL
                imgColor: '',
                imgList: [],
                specList: [],//从服务器获取的所有规格列表,
                isEnableSpec: 1, //是否启用规格
                isType: false,
                specificationItems: [],//当前选中规格
                rowList: [],
                goodsEntity: {
                    goods: {},
                    goodsDesc: {},
                    itemList: {}
                },//最终保存商品的实体
                id: null,
            };
        },    // 注册组件
        components: {
            VueUeditorWrap
        },
        methods: {
            loadCateData(id) {
                let vm = this;
                vm.axios.post(vm.API.ITEMCAT_FINDBYPARENTID_URL + "?id=" + id)
                    .then(function (response) {
                        if (vm.grade == 1) {
                            //取服务端响应的结果
                            vm.categoryList1 = response.data;
                        }
                        if (vm.grade == 2) {
                            //取服务端响应的结果
                            vm.categoryList2 = response.data;
                        }
                        if (vm.grade == 3) {
                            //取服务端响应的结果
                            vm.categoryList3 = response.data;
                        }
                    }).catch(function (reason) {
                    console.log(reason);
                })
            },
            getCatSelected(grade) {
                if (grade == 1) { //第1级选项改变
                    this.categoryList2 = [];//清空二级分类数据
                    this.categoryList3 = []; //清空三级分类数据
                    this.catSelected2 = null;   //默认选择
                    this.catSelected3 = null; //默认选择
                    this.grade = grade + 1; // 加载第2级的数据
                    this.tempType = 0;
                    this.isType = false;
                    this.loadCateData(this.catSelected1);
                }
                if (grade == 2) { //第2级选项改变
                    this.categoryList3 = [];//清空三级分类数据
                    this.catSelected3 = null;//默认选择
                    this.tempType = 0;
                    this.isType = false;
                    this.grade = grade + 1;// 加载第3级的数据
                    this.loadCateData(this.catSelected2);
                }
                if (grade == 3) { //第3级选项改变
                    //加载模板
                    let vm = this;
                    console.log(this.catSelected3);
                    vm.axios.post(vm.API.ITEMCAT_FINDONE_URL + "?id=" + vm.catSelected3).then(res => {
                        vm.tempType = res.data.typeId;
                        vm.isType = true;
                    })
                }
            },
            ready(ue) {
                let vm = this;
                window.UE.Editor.prototype._bkGetActionUrl = window.UE.Editor.prototype.getActionUrl
                window.UE.Editor.prototype.getActionUrl = function (action) {
                    if (action === 'upload/uploadImage') {
                        return vm.API.BASE_SERVER_URL+'/upload/uploadImage'
                    } else {
                        return this._bkGetActionUrl.call(this, action)
                    }
                }
                ue.addListener('ready', () => {
                    this.$emit('getUe', this.msg)
                })
            },
            uploadFile: function () {
                let formData = new FormData();
                formData.append('file', file.files[0])
                const instance = this.axios.create({
                    withCredentials: false
                });
                let vm = this;
                instance.post(this.API.UPLOAD_UPLOADFILE_URL, formData).then(function (response) {
                    vm.imgUrl = response.data.message;
                }).catch(function (reason) {
                    console.log(reason);
                });
            },
            imgSave: function () {
                if (this.imgColor == '' || this.imgUrl == '') {
                    this.$message.error("请输入颜色或上传图片");
                    return;
                }
                let imgObj = {color: this.imgColor, url: this.imgUrl};
                this.imgList.push(imgObj);
            },
            delImg(url, index) {
                let _this = this;
                _this.axios.get(this.API.UPLOAD_DELIMG_URL + "?url=" + url)
                    .then(function (response) {
                        // 从数组中移除
                        _this.imgList.splice(index, 1);
                        _this.$message.success(response.data.message);
                    }).catch(function (reason) {
                    console.log(reason);
                });
            },
            searchObjectByKey(list, keyName, keyValue) {
                for (let i = 0; i < list.length; i++) {
                    if (list[i][keyName] == keyValue) {
                        return list[i];
                    }
                }
                return null;
            },
            updateSpecAttribute($event, name, value) {
                let obj = this.searchObjectByKey(this.specificationItems, "specName", name);
                if (obj != null) {
                    if ($event.target.checked) {//判断是否被选中
                        obj.optionName.push(value)
                    } else {
                        obj.optionName.splice(obj.optionName.indexOf(value), 1);
                    }
                    if (obj.optionName.length == 0) {
                        let idx = this.specificationItems.indexOf(obj);
                        this.specificationItems.splice(idx, 1);
                    }
                } else {
                    this.specificationItems.push({"specName": name, "optionName": [value]});
                }
                this.createRowList();
            },
            createRowList() {
                let rowList = [
                    {spec: {}, price: 0, num: 9999, status: '0', isDefault: '0'}
                ];
                /**
                 * {'specName':'选择颜色','optionName':[ "秘境黑","星云紫"]}
                 * {'specName':'"选择版本"','optionName':[ ""8G+128G""]}
                 */
                for (let i = 0; i < this.specificationItems.length; i++) {
                    let specObj = this.specificationItems[i];
                    let specName = specObj.specName;//选择演示
                    let specOption = specObj.optionName;//选项
                    let newRowList = [];
                    for (let j = 0; j < rowList.length; j++) {
                        let oldRow = rowList[j];
                        for (let k = 0; k < specOption.length; k++) {
                            let newRow = JSON.parse(JSON.stringify(oldRow));
                            newRow.spec[specName] = specOption[k];
                            newRowList.push(newRow);
                        }
                    }
                    rowList = newRowList;
                }
                this.rowList = rowList;
            },
            saveGoods() {//保存商品
                this.goodsEntity.goods.category1Id = this.catSelected1;
                this.goodsEntity.goods.category2Id = this.catSelected2;
                this.goodsEntity.goods.category3Id = this.catSelected3;
                this.goodsEntity.goods.typeTemplateId = this.tempType,
                    this.goodsEntity.goods.brandId = this.selBrand,
                    this.goodsEntity.goods.isEnableSpec = this.isEnableSpec,
                    this.goodsEntity.goodsDesc.itemImages = this.imgList,
                    this.goodsEntity.goodsDesc.specificationItems = this.specificationItems,
                    this.goodsEntity.goodsDesc.introduction = this.$refs.ue.value

                this.goodsEntity.itemList = this.rowList;
                if (this.goodsEntity.goods.goodsName == undefined) {
                    this.$message.warning("请填写商品名称后再保存~");
                    return;
                }
                if (this.selBrand == "-1" || this.catSelected3 == "-1") {
                    this.$message.warning("请选择品牌或分类后再保存~");
                    return;
                }
                if (this.goodsEntity.goods.price == undefined) {
                    this.$message.warning("请填写商品价格后再保存~");
                    return;
                }
                let numReg = /^[0-9]*$/
                let numRe = new RegExp(numReg)
                if (!numRe.test(this.goodsEntity.goods.price)) {
                    this.$message.warning("请正确填写商品价格后再保存~");
                    return;
                }
                //发送请求
                let vm = this;
                let url = '';
                let username = vm.Storage.Session.get("username");
                if (this.id != null) {
                    url =  vm.API.GOODS_UPDATE_URL+"?username="+username;
                } else {
                    url = vm.API.GOODS_ADD_URL+"?username="+username;
                }
                this.axios.post(url, this.goodsEntity)
                    .then(function (response) {
                        console.log(response.data);
                        if (response.data.success) {
                            vm.$message.success(response.data.message);
                            vm.$router.push({path: '/index/goods'});
                        } else {
                            vm.$message.error(response.data.message);
                        }
                    })
            },
            checkAttributeValue(specName, optionName) { //判断当前规格是否为选中状态
                let items = this.specificationItems;
                let obj = this.searchObjectByKey(items, "specName", specName);
                if (obj != null) {
                    if (obj.optionName.indexOf(optionName) >= 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            },
            clearGoodEntity() {
                this.id = null;
                this.msg = '',
                    this.categoryList1 = [],//分类1数据列表
                    this.categoryList2 = [],//分类2数据列表
                    this.categoryList3 = [],//分类3数据列表
                    this.grade = 1,  //记录当前级别
                    this.catSelected1 = null,//分类1选中的id
                    this.catSelected2 = null,//分类2选中的id
                    this.catSelected3 = null,//分类3选中的id,
                    this.tempType = 0,//模板id
                    this.brandList = [],
                    this.selBrand = null,//选择品牌
                    this.imgUrl = '', //上传图片的URL
                    this.imgColor = '',
                    this.imgList = [],
                    this.specList = [],//从服务器获取的所有规格列表,
                    this.isEnableSpec = 1, //是否启用规格
                    this.isType = false,
                    this.specificationItems = [],//当前选中规格
                    this.rowList = [],
                    this.goodsEntity = {
                        goods: {},
                        goodsDesc: {},
                        itemList: {}
                    },//最终保存商品的实体
                this.id = null
            }
        },
        created() {
            this.loadCateData(0);
            this.clearGoodEntity();
        },
        mounted() {
            let id = this.$route.params.id;
            this.id = id;
            if (id != null) {
                let _this = this;
                _this.axios.get(_this.API.GOODS_FINDBYID_URL + "?id=" + id).then(response => {
                    console.log(response);
                    //1.回显商品
                    _this.goodsEntity.goods = response.data.goods;
                    //2.商品描述
                    _this.goodsEntity.goodsDesc = response.data.goodsDesc;
                    //3.分类模板
                    _this.tempType = response.data.goods.typeTemplateId;
                    //回显图片
                    _this.imgList = JSON.parse(response.data.goodsDesc.itemImages);
                    //回显富文本
                    _this.msg = response.data.goodsDesc.introduction;
                    //选中规格
                    _this.specificationItems = JSON.parse(response.data.goodsDesc.specificationItems);
                    //库存列表
                    _this.rowList = response.data.itemList;
                    for (let i = 0; i < _this.rowList.length; i++) {
                        _this.rowList[i].spec = JSON.parse(_this.rowList[i].spec);
                    }
                    console.log(_this.specificationItems);
                    _this.catSelected1 = response.data.goods.category1Id;
                    //控制选项默认选中状态
                    if (response.data.goods.category1Id >= 0) {
                        _this.grade = 2
                        _this.loadCateData(_this.catSelected1);
                        _this.catSelected2 = response.data.goods.category2Id;
                        if (_this.catSelected2 >= 0) {
                            _this.axios.post(_this.API.ITEMCAT_FINDBYPARENTID_URL + "?id=" + _this.catSelected2)
                                .then(function (response) {
                                    //取服务端响应的结果
                                    _this.categoryList3 = response.data;
                                }).catch(function (reason) {
                                console.log(reason);
                            })
                            _this.catSelected3 = response.data.goods.category3Id;
                            _this.isType = true;
                        }
                    }
                    _this.isEnableSpec = response.data.goods.isEnableSpec;
                })
            }
        },
        watch: { //监听属性的变化
            tempType(newValue, oldValue) {
                let vm = this;
                vm.brandList = [];
                vm.selBrand = null;
                vm.axios.post(vm.API.TYPE_FINDBYID_URL + "?id=" + newValue)
                    .then(function (response) {
                        vm.brandList = JSON.parse(response.data.brandIds);
                        if (vm.goodsEntity.goods.brandId != null) {
                            vm.selBrand = vm.goodsEntity.goods.brandId;
                        }
                    }).catch(function (reason) {
                    console.log(reason);
                });
                vm.axios.get(vm.API.TYPE_SPECLIST_URL + "?id=" + newValue)
                    .then(function (response) {
                        vm.specList = response.data;
                    }).catch(function (reason) {
                    console.log(reason);
                });
            }
        },
    };

</script>

<style scoped>
    .modal-content {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translateX(-50%) translateY(30%);
    }
</style>