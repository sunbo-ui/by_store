<template>
    <div>
        <div class="content_center">
            <div v-if="cartList != ''">
                <form action="#">
                    <div>
                        <div style="background-color: lavender;margin-top: 10px;font-weight: bold;" class="all">
                            <input type="checkbox" v-model="checkedAll" @change="checkAll"> 全部
                            <span style="margin-left: 150px;">商品</span>
                            <span style="margin-left: 250px;">单价(元)</span>
                            <span style="margin-left: 150px;">数量</span>
                            <span style="margin-left: 150px;">小计(元)</span>
                            <span style="margin-left: 100px;">操作</span>
                        </div>
                        <div style="background-color: #fff;margin-bottom: 10px;" v-for="cart in cartList">
                            <div style="padding-top: 10px;margin-left: 5px; " class="sellerCheck">
                                <input type="checkbox" v-model="cart.checked" @change="_checkAll(cart)">
                                <span style="margin-left: 5px">{{cart.sellerId}}</span>
                                <hr>
                            </div>
                            <div class="seller cart" style="height: 100px;padding-left: 5px;"
                                 v-for="(orderItem,index) in cart.orderItemList">
                                <input type="checkbox" v-model="orderItem.checked" @change="handleCheck(orderItem,index)">
                                <div style="width:400px;margin-left: 20px;">
                                    <div style="display:flex;align-items: center">
                                        <span></span>
                                        <img style="margin-right: 5px;" :src="orderItem.picPath" width="80" height="80"
                                             alt="">
                                        <span>{{orderItem.title}}</span>
                                    </div>
                                </div>
                                <div style="width: 150px;text-align: center">￥{{orderItem.price }}</div>
                                <template>
                                    <el-input-number style="width: 200px;text-align: center;margin-left: 30px;"
                                                     v-model="orderItem.num"
                                                     @change="updateNumChange(orderItem.num,orderItem.itemId)"
                                                     :min="1"
                                                     label="描述文字"></el-input-number>
                                </template>
                                <div style="width: 150px;text-align: center">{{orderItem.num*orderItem.price|moneyFormat}}
                                </div>
                                <div style="width: 200px;">
                                    <el-button type="danger" @click="deleteShop(orderItem.itemId)" size="small" plain>删除
                                    </el-button>
                                    <el-button type="primary" @click="addCollect(orderItem)" size="small" plain>加入我的收藏</el-button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="height: 45px;width:1100px;border: 2px solid #ccc;margin-top: 30px;margin-left: 50px;border-radius: 5px">
                        <div class="all" style="float: left;margin-top: 7px;margin-left: 5px;">
                            <input type="checkbox" v-model="checkedAll" @change="checkAll"> 全部
                            <el-button type="primary" plain size="mini" style="margin-left: 10px;"
                                       @click="deleteCheckShop()">删除选中的商品
                            </el-button>
                            <el-button type="primary" @click="collectShop()" plain size="mini">移到我的收藏</el-button>
                            <el-button type="primary" plain size="mini" @click="clearShop()">清除下柜商品</el-button>
                        </div>
                        <div style="float: right;margin-left: 30px;">
                            <el-button style="height: 42px" type="danger" @click="goto('/index/account/order')">结算</el-button>
                        </div>
                        <div style="float:right;font-size: 15px;">
                            <span>已选择{{totalValue.totalNum}}件商品</span>
                            <span>总价(不含运费):
                            <span style="color: red">{{totalValue.totalMoney |moneyFormat}}</span><br>
                         已节省:￥0.00
                        </span>
                        </div>
                    </div>
                </form>
            </div>
            <div v-else style="height:500px;display: flex;justify-content: center;align-items: center">
                <p style="font-size: 30px;">购物车空空的哟~请先去逛逛商店吧(●'◡'●)</p>
                <div style="margin-left: 20px;">
                    <el-button style="font-size: 20px;" type="success" @click="goto('/index/home')">go~go~go</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Cart",
        data() {
            return {
                cartList: [],
                flag: true,
                totalValue: {
                    totalNum: 0,
                    totalMoney: 0,
                },//总价
                checkedAll: false, //控制是否全选
                selectCheckItemId: [],
            }
        },
        mounted() {
            this.cartList.forEach(item => {
                this.$set(item, "checked", false) //Vue 解决不能检测到对象属性的添加或删除
                // item.checked = false; //如果为真实数据直接设置的对象改变值不会更新视图
                if (item.orderItemList) {
                    item.orderItemList.forEach((citem) => {
                        this.$set(citem, "checked", false)
                        // citem.checked = false;
                    })
                }
            });
        },
        methods: {
            addCollect(orderItem){
                let user = this.Storage.Session.get("username");
                if(user === null){
                    this.$router.push({path: '/login'}).catch(err => {
                        console.log(err)
                    })
                    return;
                }
                let collect = {
                    userId:user,
                    goodsId:orderItem.goodsId,
                    price:orderItem.price,
                    picUrl:orderItem.picPath,
                    title:orderItem.title,
                }
                let vm = this;
                vm.axios.post(vm.API.COLLECT_SAVE_URL,collect).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                    }else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            //全选
            checkAll() {
                this.cartList.forEach(item => {
                    item.checked = this.checkedAll;
                    if (item.orderItemList) {
                        item.orderItemList.forEach(citem => {
                            citem.checked = this.checkedAll;
                            if (this.checkedAll) {
                                this.selectCheckItemId.push(citem.itemId);
                            } else {
                                this.selectCheckItemId = [];
                            }
                        })
                    }
                })
            },
            //商家全选
            _checkAll(val) {
                val.orderItemList.forEach(item => {
                    item.checked = val.checked;
                    if (val.checked) {
                        this.selectCheckItemId.push(item.itemId);
                    } else {
                        let index = this.selectCheckItemId.indexOf(item.itemId);
                        this.selectCheckItemId.splice(index, 1);
                    }
                });
                //全选判断 如果商家全选则勾选全选
                if (this.cartList.every(item => item.checked)) {
                    this.checkedAll = true;
                } else {
                    this.checkedAll = false;
                }
            },
            //商品选择框
            handleCheck(item, index) {
                var check = []; //用于存放商家的checked
                this.cartList.forEach((items, index) => {
                    if (items.orderItemList) {
                        //判断是否商品全选
                        var bool = items.orderItemList.every(citem => citem.checked);
                        if (bool) {
                            //商品全选则勾选对应商家
                            items.checked = true;
                        } else {
                            items.checked = false;
                        }
                        check.push(bool);
                    }
                })

                if (item.checked) {
                    this.selectCheckItemId.push(item.itemId);
                } else {
                    let index = this.selectCheckItemId.indexOf(item.itemId);
                    this.selectCheckItemId.splice(index, 1);
                }
                //如果商家全选则勾选全选
                if (check.indexOf(false) == -1) {
                    this.checkedAll = true;
                } else {
                    this.checkedAll = false;
                }

            },
            updateNumChange(num, itemId) {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.get(vm.API.UPDATEBYNUM_CART_URL, {
                    params: {
                        num: num,
                        username: username,
                        itemId: itemId
                    }
                }).then(res => {
                    vm.$message.success(res.data.message)
                    vm.loadCartList()
                });
            },
            loadCartList() {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.post(vm.API.CART_FINDCARTLIST_URL + "?username=" + username).then(res => {
                    console.log(res.data);
                    vm.cartList = res.data;
                    if (vm.cartList.length > 0) {
                        vm.flag = false;
                    } else {
                        vm.flag = true;
                    }
                    if (vm.cartList.length == 1 && vm.cartList[0].orderItemList.length == 0) {
                        vm.cartList = [];
                    }
                    vm.total();
                })
            },
            total() {
                if (this.cartList.length <= 0) {
                    return;
                }
                let vm = this;
                vm.totalValue.totalNum = 0;
                vm.totalValue.totalMoney = 0;
                this.cartList.forEach(function (item) {
                    item.orderItemList.forEach(function (orderItem) {
                        vm.totalValue.totalNum += orderItem.num;
                        vm.totalValue.totalMoney += (orderItem.num * orderItem.price);
                    })
                })
            },
            deleteCheckShop() {
                let vm = this;
                let username = this.Storage.Session.get("username");
                if (this.selectCheckItemId == '') {
                    vm.$message.info("请选择要删除的商品信息");
                    return;
                }
                vm.axios.post(vm.API.DETELETE_CART_URL + "?username=" + username, {idList: vm.selectCheckItemId}).then(res => {
                    if (res.data.success) {
                        if (!res.data.flage) {
                            vm.$message.info(res.data.message);
                            return;
                        }
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                    vm.loadCartList();
                    vm.selectCheckItemId = [];
                })
            },
            deleteShop(id) {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.post(vm.API.DETELETE_ONE_CART_URL + "?itemId=" + id + "&username=" + username).then(res => {
                    if (res.data.success) {
                        if (!res.data.flage) {
                            vm.$message.info(res.data.message);
                            return;
                        }
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                    vm.loadCartList();
                })
            },
            //清除下柜商品
            clearShop() {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.post(vm.API.CLEARSHOP_CART_URL + "?username=" + username).then(res => {
                    if (res.data.success) {
                        vm.$message.info(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                    vm.loadCartList();
                    vm.selectCheckItemId = [];
                })
            },
            goto(path){
                this.$router.push({path:path})
            },
            collectShop(){
                let vm = this;
                let username = this.Storage.Session.get("username");
                if(username == null){
                    vm.$message.info("请登录后再收藏(＾Ｕ＾)ノ~");
                    return;
                }
                if (this.selectCheckItemId == '') {
                    vm.$message.info("请选择要收藏的商品信息");
                    return;
                }
                vm.axios.post(vm.API.COLLECT_SECLECT_URL + "?username=" + username, {idList: vm.selectCheckItemId}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                    } else {
                        vm.$message.error(res.data.message);
                    }
                })
            }
        },
        created() {
            this.loadCartList();

        },
        filters: {
            moneyFormat(val) {
                return "￥" + val.toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .cart > div {
        display: inline-block;
    }
</style>