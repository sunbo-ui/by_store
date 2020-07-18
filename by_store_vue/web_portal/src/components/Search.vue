<template>
    <div class="content_center" style="padding: 10px;">
        <div style="margin-bottom: 10px;">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item v-if="searchMap.category != ''">
                    <a style="cursor: pointer" @click="removeSearchItem('category')">
                        商品分类:{{searchMap.category}}
                    </a>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-if="searchMap.brand != ''">
                    <a style="cursor: pointer" @click="removeSearchItem('brand')">
                        品牌:{{searchMap.brand}}
                    </a>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-if="searchMap.price != ''">
                    <a style="cursor: pointer" @click="removeSearchItem('price')">
                        金额:{{searchMap.price}}
                    </a>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-for="(value,key) in searchMap.spec">
                    <a style="cursor: pointer" @click="removeSearchItem(key)">
                        {{key}}:{{value}}
                    </a>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div style="border: 1px solid #DCDFE6;border-bottom: none;">
            <div style="display:flex;border-bottom: 1px solid #DCDFE6" v-if="searchMap.category == ''">
                <div style="background-color: #C0C4CC;flex:1;color:#303133;text-align: center;padding-top: 10px;">
                    商品分类
                </div>
                <div style="background-color: #fff;flex: 8; padding-top: 10px;">
                    <span v-for="category in resultMap.categoryList">
                         <a style="margin-right: 10px;" @click="addSearchItem('category',category)">{{category}}</a>
                    </span>
                </div>
            </div>
            <div style="display:flex;border-bottom: 1px solid #DCDFE6" v-if="searchMap.brand == ''">
                <div style="background-color: #C0C4CC;flex:1;color:#303133;text-align: center;padding-top: 10px;">
                    品牌
                </div>
                <div style="background-color: #fff;flex: 8; padding-top: 10px;">
                   <span v-for="brand in resultMap.brandList">
                         <a style="margin-right: 10px;" @click="addSearchItem('brand',brand.name)">{{brand.name}}</a>
                    </span>
                </div>
            </div>
            <div style="display:flex;border-bottom: 1px solid #DCDFE6" v-if="searchMap.price == ''">
                <div style="background-color: #C0C4CC;flex:1;color:#303133;text-align: center;padding-top: 10px;">
                    金额
                </div>
                <div style="background-color: #fff;flex: 8; padding-top: 10px;">
                    <a style="margin-right: 20px;" @click="addSearchItem('price','0-500')">0-500</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','500-1000')">500-1000</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','1000-1500')">1000-1500</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','1500-2000')">1500-2000</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','2000-2500')">2000-2500</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','2500-3000')">2500-3000</a>
                    <a style="margin-right: 20px;" @click="addSearchItem('price','3000-*')">3000以上</a>
                </div>
            </div>
            <div style="display:flex;border-bottom: 1px solid #DCDFE6" v-for="spec in resultMap.specList"
                 v-if="searchMap.spec[spec.specName] == undefined">
                <div style="background-color: #C0C4CC;flex:1;color:#303133;text-align: center;padding-top: 10px;">
                    {{spec.specName}}
                </div>
                <div style="background-color: #fff;flex: 8; padding-top: 10px;">
                   <span v-for="option in spec.options">
                         <a style="margin-right: 10px;" @click="addSearchItem(spec.specName,option.optionName)">{{option.optionName}}</a>
                    </span>
                </div>
            </div>
        </div>
        <div class="content_center centerSearch"
             style="border: 1px solid #DCDFE6;height: 42px;width: 1180px;margin-top: 20px;background-color: #fff;box-shadow:  0 4px 6px 0px rgba(0, 0, 0, 0.3)">
            <ul>
                <li><a href="#">综合</a></li>
                <li><a href="#">销量</a></li>
                <li><a href="#">新品</a></li>
                <li><a href="#">评价</a></li>
                <li><a href="#">价格</a></li>
            </ul>
        </div>
        <div style="margin-top: 20px;">
            <el-row :gutter="16">
                <el-col :span="6" v-for="(item, index) in  resultMap.rows">
                    <el-card shadow="hover" style="margin-top: 10px;">
                        <div  @click="goto(item.goodsId)">
                            <div class="bottom clearfix">
                                <div class="p-img" >
                                    <a href="#"><img :src="item.image" width="100%"/></a>
                                </div>
                                <div class="p-name" v-html="item.title">
                                </div>
                            </div>
                            <div class="p-price">
                                <strong class="price">￥<span
                                        class="J-prom-p-100005702210">{{item.price}}</span></strong>
                            </div>
                            <div class="p-review">
                                <span class="number J-p-comm-ss-100005702210">4.3万+</span>人评价
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
        <div style="height: 20px">
            <div style="text-align: center">
                <el-pagination
                        @current-change="search"
                        :current-page.sync="searchMap.pageNo"
                        :page-size="searchMap.pageSize"
                        layout="total, prev, pager, next"
                        :total="resultMap.total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Search",
        data() {
            return {
                searchMap: {
                    'keywords': '',//搜索关键字
                    'category': '',//分类
                    'brand': '',//品牌
                    'spec': {},//规格
                    'price': '',//价格
                    'pageNo': 1,//当前页
                    'pageSize': 40,//每页展示多少条数据
                    'sort': '',//排序
                    'sortField': ''//排序的字段
                },
                resultMap: {
                    rows: [],
                    total: 0,
                    totalPages: 0,
                    categoryList: [],
                    brandList: [],
                    specList: [],
                },
            }
        },
        methods: {
            getQuery() {
                console.log(this.$route.params.id);
                this.searchMap.keywords = this.$route.params.id;
            },
            search() {
                this.searchMap.pageNo = parseInt(this.searchMap.pageNo);//转换为数字
                let vm = this;
                vm.axios.post(vm.API.ITEMSEARCH_SEARCH_URL, this.searchMap)
                    .then(function (response) {
                        console.log(response);
                        if (response.data.error == 'error') {
                            vm.$router.push({path:'/index/errorSearch'}).catch(err => {err})
                            return;
                        }
                        vm.resultMap = response.data;
                        console.log(vm.resultMap)
                    }).catch(function (reason) {
                    console.log(reason);
                });
            },
            addSearchItem(key, value) {
                //如果用户点击的是分类或者品牌
                if (key == 'category' || key == 'brand' || key == 'price') {
                    this.searchMap[key] = value;
                } else {
                    //用户点击的是规格
                    this.searchMap.spec[key] = value;
                }
                this.search();
            },
            removeSearchItem(key) {
                if (key == 'category' || key == 'brand' || key == 'price') {
                    this.searchMap[key] = '';
                } else {
                    this.searchMap.spec[key] = '';
                    delete this.searchMap.spec[key];
                }
                this.search()
            },
            goto(id){
                this.$router.push({
                    path:`/index/detailShop/${id}`
                }).catch(err => {err})
            }
        },
        watch: { //监听属性的变化
            $route(to, from) {
                this.getQuery();
                this.search();
            }
        },
        created: function () {//创建对象时调用

        },
        mounted: function () {//页面加载完
            this.getQuery();
            this.search();
        }
    }
</script>

<style scoped>
    a{
        cursor: pointer;
    }
    ul {
        list-style: none;
    }
    a{
        cursor: pointer;
    }
    .centerSearch > ul > li {
        float: left;
    }

    .centerSearch > ul > li:first-child > a {
        background-color: red;
        color: #fff;
    }

    .centerSearch > ul > li > a {
        display: block;
        width: 50px;
        height: 40px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #A8A297;
    }
</style>