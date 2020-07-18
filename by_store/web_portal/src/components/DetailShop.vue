<template>
    <div>
        <div>
            <div style="background-color:#eaeaea;padding-top: 7px;padding-bottom: 10px;">
                <div class="content_center">
                    <el-breadcrumb separator="/">
                        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{category1.name}}</el-breadcrumb-item>
                        <el-breadcrumb-item>{{category2.name}}</el-breadcrumb-item>
                        <el-breadcrumb-item>{{category3.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
            </div>
            <div class="content_center" style="display: flex;margin-top: 15px;">
                <div style="flex: 1;">
                    <img v-if="imgUrl != ''" :src="imgUrl" alt="" width="300" height="300">
                    <!--缩略图-->
                    <div style="position: relative">
                        <img src="../assets/img/left.png" @click="prevImg()"
                             style="position: absolute;left: 0px;top: 10px;cursor: pointer" width="20" height="80"
                             alt="">
                        <img src="../assets/img/right.png" @click="nextImg()"
                             style="position: absolute;right: 0px;top: 10px;cursor: pointer" width="20" height="80"
                             alt="">
                    </div>
                    <div style="height:80px;width:270px;margin-top: 10px;margin-left: 25px;overflow: hidden">
                        <div class="smallImage">
                            <img class="imageItem" :key="index" @mouseenter="changeImg(item)"
                                 style="margin-right: 5px;display:inline-block;box-sizing: border-box"
                                 :src="item" v-for="(item,index) in imageList" width="80" height="80" alt="">
                        </div>
                    </div>
                </div>
                <div style="flex: 2;padding: 10px;">
                    <h2>{{sku.title}}</h2>
                    <h6>{{goodsEntity.goods.caption}}</h6>
                    <h6>价格: <span style="font-size: 20px;color: red">{{sku.price |moneyFormat}}</span></h6>
                    <h6>优惠券
                        <el-card v-for="(item,index) in discountList" style="margin-top: 10px;" :key="index"
                                 class="box-card">
                            <div slot="header" class="clearfix">
                                <span>优惠券</span>
                                <el-button v-if="!item.coupon" @click="getDiscount(item)"
                                           style="float: right; padding: 3px 0" type="text">领券
                                </el-button>
                                <el-button v-else style="float: right; padding: 3px 0;color:#c3c3c3" type="text">已领券
                                </el-button>
                                <el-button v-if="item.num == 0" style="float: right; padding: 3px 0;color:#c3c3c3"
                                           type="text">优惠券已领取完
                                </el-button>
                            </div>
                            <div class="text item">
                                <span style="font-size: 23px;">满{{item.conditions}}</span>
                                <span class="ml-2">立减{{item.discount}}</span>
                                <span class="ml-2">{{item.expirationTime}}到期</span>
                            </div>
                        </el-card>
                    </h6>
                    <div v-for="(spec,index) in goodsEntity.goodsDesc.specificationItems" :key="index"
                         style="padding-top: 20px;">
                        <span style="color:#909399;margin-right: 10px;">{{spec.specName}}:</span>
                        <el-radio-group v-model="spec.selOption" @change="onRadioChange(spec.specName,spec.selOption)">
                            <el-radio border :label="option" v-for="(option,i) in spec.optionName" :key="i">{{option}}
                            </el-radio>
                        </el-radio-group>
                    </div>
                    <div style="margin-top: 50px;">
                        <template>
                            <el-input-number v-model="num" :min="1"></el-input-number>
                        </template>
                        <el-button @click="addCart()"
                                   style="margin-left: 50px;background-color: red;color: white;font-weight: bold;">
                            <i style="width: 20px" class="el-icon-shopping-cart-2"></i>
                            加入购物车
                        </el-button>
                        <el-button type="primary" @click="addCollect()">
                            加入收藏
                        </el-button>
                    </div>
                </div>
                <div style="flex: 1;">
                    <div>
                        <div>
                            <span style="font-size: 20px;font-weight: bold;">包装列表</span>
                            <div v-if="goodsEntity.goodsDesc.packageList">{{goodsEntity.goodsDesc.packageList}}</div>
                            <div v-else>商家没标注哦</div>
                        </div>
                        <div style="margin-top: 30px;">
                            <span style="font-size: 20px;font-weight: bold;">售后服务</span>
                            <div v-if="goodsEntity.goodsDesc.saleService">
                                {{goodsEntity.goodsDesc.saleService}}
                            </div>
                            <div v-else>商家没标注哦</div>
                        </div>
                    </div>
                </div>
            </div>
            <div style="margin-top: 30px;" class="content_center">
                <el-card>
                    <div v-if="commentsList.length == 0">
                        暂未有人评价/(ㄒoㄒ)/~~
                    </div>
                    <div v-else>
                        <div>
                            <span>累积评分:</span>
                            <span style="color: #C0C4CC;margin-left: 20px;">共评价{{num}}+</span>
                            <el-rate
                                    v-model="rate"
                                    disabled
                                    show-score
                                    :colors="colors"
                                    text-color="#ff9900"
                                    score-template="{value}">
                            </el-rate>
                        </div>
                        <div style="margin-top: 10px;">
                            客户评价:
                            <div style="margin-top: 10px;" v-for="comment in commentsList">
                                <div style="display: flex;">
                                    <div style="flex: 1">
                                        <img style="border-radius: 50%" src="../assets/logo.png" alt="" width="50"
                                             height="50">
                                    </div>
                                    <div style="flex:12">
                                        <span style="font-weight: bold;font-size: 15px;">{{comment.userId}}</span>
                                        <el-rate
                                                style="display: inline-block;margin-left: 10px;"
                                                v-model="comment.starts"
                                                :colors="colors"
                                                disabled
                                                show-score
                                                text-color="#ff9900"
                                                score-template="{value}">
                                        </el-rate>
                                        <div style="color: #C0C4CC">{{comment.createTime}}</div>
                                    </div>
                                </div>
                                <p style="text-indent: 2rem;margin-top: 10px;">
                                    {{comment.content}}
                                </p>
                                <div>
                                    <el-dialog :visible.sync="dialogVisible">
                                        <img width="100%" :src="dialogImageUrl" alt="">
                                    </el-dialog>
                                    <img @click="handlePictureCardPreview(img.url)"
                                         v-for="img in JSON.parse(comment.picUrl)"
                                         :src="img.url" alt="" width="80" height="80">
                                </div>
                                <div v-if="comment.shopContent">
                                    <span>商家留言:</span>
                                    <p style="color: burlywood">
                                        {{comment.shopContent}}
                                    </p>
                                </div>
                            </div>
                        </div>
                        <!--分页-->
                        <div class="block">
                            <el-pagination
                                    @current-change="loadComments"
                                    :current-page.sync="page"
                                    :page-size="pageSize"
                                    layout="total, prev, pager, next"
                                    :total="total">
                            </el-pagination>
                        </div>
                    </div>
                </el-card>
            </div>
        </div>
        <img style="width: 100px;height: 100px;background-color: red;border-radius: 50%;position: fixed;right: 300px;bottom:50px"
             src="../assets/img/relation.jpg" width="100%" height="100%" @click="goto('/index/communication')" alt="">
    </div>
</template>

<script>
    export default {
        name: "DetailShop",
        data() {
            return {
                dialogVisible: false,
                dialogImageUrl: '',
                id: null,
                num: 1,
                goodsEntity: {
                    goods: {},
                    goodsDesc: {
                        specificationItems: {
                            selOption: {}
                        }
                    },
                    itemList: []
                },//最终保存商品的实体
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                imgUrl: '',
                imageList: [],
                imageListLength: 1,
                marginTop: 0,
                sku: {
                    id: '',
                    price: 0,
                    title: '',
                    spec: {},
                },
                flag: false,
                category1: '',
                category2: '',
                category3: '',
                rate: 0,
                num: 0,
                page: 1,
                pageSize: 10,
                total: 0,
                commentsList: [],
                next: 3,//商品列表
                discountList: [],//优惠券
            }
        },
        methods: {
            changeImg(item) {
                this.imgUrl = item;
                let vm = this;
                this.$(".imageItem").bind('mouseenter', function () {
                    vm.$(this).addClass("imageChange").siblings().removeClass("imageChange");
                })
            },
            prevImg() {
                this.imageListLength = this.imageListLength - 1;
                if (this.imageListLength <= 0) {
                    this.imageListLength = 1;
                    return;
                } else {
                    //margin-top+80px
                    this.marginTop = this.marginTop + 80;
                    this.$(".smallImage").animate({marginTop: this.marginTop + "px"})
                }
            },
            nextImg() {
                this.imageListLength = this.imageListLength + 1;
                if (this.imageListLength > Math.ceil(this.imageList.length / 3)) {
                    this.imageListLength = Math.ceil(this.imageList.length / 3);
                    return;
                } else {
                    //margin-top-80px
                    this.marginTop = this.marginTop - 80;
                    this.$(".smallImage").animate({marginTop: this.marginTop + "px"})
                }
            },
            addCollect() {
                let user = this.Storage.Session.get("username");
                if (user === null) {
                    this.$message.error("请先登录后再进行操作哦")
                    return;
                }
                let collect = {
                    userId: user,
                    goodsId: this.id,
                    price: this.sku.price,
                    picUrl: this.imgUrl,
                    title: this.sku.title,
                }
                let vm = this;
                vm.axios.post(vm.API.COLLECT_SAVE_URL, collect).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            handlePictureCardPreview(url) {
                this.dialogImageUrl = url;
                this.dialogVisible = true;
            },
            onRadioChange(key, item) {
                for (let i = 0; i < this.goodsEntity.goodsDesc.specificationItems.length; i++) {
                    if (this.goodsEntity.goodsDesc.specificationItems[i].specName == key) {
                        this.$set(this.goodsEntity.goodsDesc.specificationItems[i], "selOption", item);
                        this.compareObj()
                    }
                }
            },
            loadData() {
                this.id = this.$route.params.id;
                if (this.id != undefined) {
                    let _this = this;
                    _this.axios.get(_this.API.GOODS_FINDBYID_URL + "?id=" + this.id).then(response => {
                        //1.回显商品
                        _this.goodsEntity.goods = response.data.goods;
                        this.axios.post(_this.API.ITEMCAT_FINDONE_URL + "?id=" + _this.goodsEntity.goods.category1Id).then(res => {
                            _this.category1 = res.data;
                        })
                        this.axios.post(_this.API.ITEMCAT_FINDONE_URL + "?id=" + _this.goodsEntity.goods.category2Id).then(res => {
                            _this.category2 = res.data;
                        })
                        this.axios.post(_this.API.ITEMCAT_FINDONE_URL + "?id=" + _this.goodsEntity.goods.category3Id).then(res => {
                            _this.category3 = res.data;
                        })
                        //2.商品描述
                        _this.goodsEntity.goodsDesc = response.data.goodsDesc;
                        _this.goodsEntity.goodsDesc.itemImages = JSON.parse(response.data.goodsDesc.itemImages);
                        //库存列表
                        _this.goodsEntity.itemList = response.data.itemList;
                        for (let i = 0; i < _this.goodsEntity.itemList.length; i++) {
                            _this.goodsEntity.itemList[i].spec = JSON.parse(_this.goodsEntity.itemList[i].spec);
                        }
                        _this.sku.id = _this.goodsEntity.itemList[0].id;
                        _this.sku.price = _this.goodsEntity.itemList[0].price;
                        _this.sku.title = _this.goodsEntity.itemList[0].title;
                        _this.sku.spec = _this.goodsEntity.itemList[0].spec;
                        //设置示例图片
                        _this.imgUrl = _this.goodsEntity.goodsDesc.itemImages[0].url;
                        //设置缩略图
                        _this.goodsEntity.goodsDesc.itemImages.forEach(item => {
                            _this.imageList.push(item.url);
                        });
                        _this.goodsEntity.goodsDesc.specificationItems = JSON.parse(response.data.goodsDesc.specificationItems);
                        for (let i = 0; i < _this.goodsEntity.goodsDesc.specificationItems.length; i++) {
                            _this.$set(_this.goodsEntity.goodsDesc.specificationItems[i], "selOption",
                                response.data.itemList[0].spec[_this.goodsEntity.goodsDesc.specificationItems[i].specName]);
                        }
                        _this.footer();
                        _this.loadDiscount();
                    })
                }

            },
            compareObj() {
                let selList = {};
                for (let i = 0; i < this.goodsEntity.goodsDesc.specificationItems.length; i++) {
                    selList[this.goodsEntity.goodsDesc.specificationItems[i].specName] = this.goodsEntity.goodsDesc.specificationItems[i].selOption;
                }
                for (let i = 0; i < this.goodsEntity.itemList.length; i++) {
                    for (let j = 0; j < this.goodsEntity.goodsDesc.specificationItems.length; j++) {
                        if (this.goodsEntity.itemList[i].spec[this.goodsEntity.goodsDesc.specificationItems[j].specName] ===
                            selList[this.goodsEntity.goodsDesc.specificationItems[j].specName]) {
                            this.flag = true;
                        } else {
                            this.flag = false;
                            break;
                        }
                    }
                    if (this.flag) {
                        this.sku.id = this.goodsEntity.itemList[i].id;
                        this.sku.price = this.goodsEntity.itemList[i].price;
                        this.sku.title = this.goodsEntity.itemList[i].title;
                        this.sku.spec = this.goodsEntity.itemList[i].spec;
                        return;
                    }
                }

            },
            addCart() {
                let vm = this;
                let name = this.Storage.Session.get("username");
                vm.axios.get(vm.API.CART_ADDGOODSTOCARTLIST_URL, {
                    params: {
                        itemId: this.sku.id,
                        num: this.num,
                        username: name
                    }
                }).then(res => {
                    vm.$router.push({path: "/index/cart"}).catch(err => {
                        console.log(err);
                    })
                })
            },
            loadComments(page) {
                this.page = page;
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_FINDBYGOODSID_URL + "?goodsId=" + this.id +
                    "&page=" + vm.page + "&rows=" + vm.pageSize).then(res => {
                    vm.commentsList = res.data.rows;
                    vm.total = res.data.total;
                    let sum = 0;
                    for (let i = 0; i < vm.commentsList.length; i++) {
                        sum += vm.commentsList[i].starts;
                    }
                    vm.rate = Number((sum / vm.commentsList.length).toFixed(1));//评分平均数
                    vm.num = vm.commentsList.length; //评价个数
                })
            },
            footer() {
                let vm = this;
                let track = {
                    goodsId: this.id,
                    userId: this.Storage.Session.get("username"),
                    picUrl: this.imgUrl,
                    price: this.sku.price,
                    title: this.sku.title,
                };
                vm.axios.post(vm.API.TRACH_SAVE_URL, track).then(res => {

                })
            },
            loadDiscount() {
                let vm = this;
                vm.axios.post(vm.API.DISCOUNT_FINDBYSELLER_USER_URL + "?sellerId=" + this.goodsEntity.goods.sellerId)
                    .then(function (response) {
                        //取服务端响应的结果
                        vm.discountList = response.data;
                        if (vm.Storage.Session.get("username") === null) {
                            return;
                        } else {
                            vm.discountList.forEach(item => {
                                vm.axios.post(vm.API.USERDISCOUNT_FINDBYDISCOUNTID_URL + "/" + item.id)
                                    .then(res => {
                                        if (res.data.success) {
                                            vm.$set(item, "coupon", true);
                                            console.log(vm.discountList);
                                        }
                                    });
                            })
                        }
                    }).catch(function (reason) {
                });
            },
            getDiscount(item) {
                let vm = this;
                let user = this.Storage.Session.get("username");
                if (user === null) {
                    this.$router.push({path: '/login'}).catch(err => {
                        console.log(err)
                    })
                    return;
                }
                vm.axios.post(vm.API.USERDISCOUNT_SAVE_URL + "?username=" + user, item).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.loadDiscount();
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            goto(url){
                if(this.Storage.Session.get("username")){
                    this.$router.push({
                        path:url,
                        query:{
                            msgKey:this.goodsEntity.goods.sellerId,
                        }
                    });
                }else {
                    //以后改为匿名对象
                    this.$message.error("请先登录~")
                }
            }
        },
        created() {
            this.loadData();
            this.loadComments(1);

        },
        watch: { //监听属性的变化
            $route(to, from) {
                this.loadData()
            }
        },
        filters: {
            moneyFormat(value) {
                return "￥" + value.toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .left {
        margin-top: 80px;
    }

    .right {
        margin-top: -80px;
    }

    .imageChange {
        border: 3px solid red;
    }
</style>