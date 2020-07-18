<template>
    <div>
        <div class="content_center">
            <form action="#">
                <div>
                    <div style="background-color: lavender;margin-top: 10px;font-weight: bold;" class="all">
                        <span style="margin-left: 250px;">商品</span>
                        <span style="margin-left: 250px;">单价(元)</span>
                        <span style="margin-left: 155px;">数量</span>
                        <span style="margin-left: 150px;">小计(元)</span>
                    </div>
                    <div style="background-color: #fff;margin-bottom: 10px;" v-for="cart in cartList">
                        <div style="padding-top: 10px;margin-left: 5px; " class="sellerCheck">
                            <span style="margin-left: 5px">{{cart.sellerId}}</span>
                            <hr>
                        </div>
                        <div class="seller cart" style="height: 100px;padding-left: 5px;"
                             v-for="(orderItem,index) in cart.orderItemList">
                            <div style="width:420px;margin-left: 20px;">
                                <div style="display:flex;align-items: center">
                                    <span></span>
                                    <img style="margin-right: 5px;" :src="orderItem.picPath" width="80" height="80"
                                         alt="">
                                    <span>{{orderItem.title}}</span>
                                </div>
                            </div>
                            <div style="width: 200px;text-align: center">￥{{orderItem.price }}</div>
                            <div style="width: 180px;text-align: center;margin-left: 30px;">{{orderItem.num}}</div>
                            <div style="width: 200px;text-align: center">{{orderItem.num*orderItem.price|moneyFormat}}
                            </div>

                        </div>
                    </div>
                </div>
                <el-card class="box-card">
                    <div>
                        <span>选择收货地址:</span>
                        <div v-if="addressList.length <=0">
                            <el-button type="primary" plain style="margin-top: 10px;" @click="goto">去设置收货地址</el-button>
                        </div>
                        <el-radio-group v-model="address">
                            <el-radio-button style="margin-left: 20px;" :label="item.address"
                                             v-for="item in addressList">
                                {{item.address}} --- {{item.mobile}} --- {{item.contact}}
                            </el-radio-button>
                        </el-radio-group>
                        <br>
                        <span style="margin-top: 10px;">选择支付方式:</span>
                        <el-radio-group v-model="order.paymentType">
                            <el-radio-button style="margin-left: 20px;" :label="1">微信支付</el-radio-button>
                            <el-radio-button style="margin-left: 20px;" :label="2">货到付款</el-radio-button>
                            <el-radio-button style="margin-left: 20px;" :label="3">B悦钱包</el-radio-button>
                        </el-radio-group>
                        <div v-if="cartList.length==1">
                            <span style="margin-top: 10px;">选择优惠券:</span>
                            <el-radio-group @change="change" v-if="userDiscountList.length > 0" v-model="userDiscount">
                                <el-radio-button v-for="item in userDiscountList" style="margin-left: 20px;"
                                                 :label="item">
                                    满{{item.conditions|moneyFormat}},立减{{item.discount|moneyFormat}}
                                </el-radio-button>
                            </el-radio-group>
                            <span v-else style="color: #c3c3c3;margin-left: 30px;font-size: 20px;">无</span>
                        </div>
                    </div>
                    <div style="font-size: 18px;">
                        <span>已选择{{totalValue.totalNum}}件商品</span>
                        <span>总价(不含运费):
                            <span style="color: red;font-size: 25px;">{{totalValue.totalMoney |moneyFormat}}</span><br>
                         已节省:￥0.00
                        </span>
                        <el-button style="float:right;height: 42px" type="danger" @click="submitOrder()">去支付</el-button>
                    </div>
                </el-card>
            </form>
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
                selectCheckSellerId: [],
                address: '',//选择的收货地址
                mobile: '',
                contact: '',
                addressList: [],
                order: {
                    paymentType: 1,
                    receiverAreaName: '',
                    receiverMobile: '',
                    receiver: '',
                    sourceType: '',
                },
                username: '',
                userDiscount: {},
                userDiscountList: [],
                money: 0,
            }
        },
        methods: {
            goto() {
                window.location.href = this.API.BASE_USER_SERVER + "/setInfo/address"
            },
            change() {
                //优惠券
                console.log(this.userDiscount);
                let vm = this;
                this.userDiscountList.forEach(item => {
                    if (item.id == vm.userDiscount.id) {
                        let dateBool = new Date() > vm.userDiscount.expirationTime ? true : false;
                        if (dateBool) {
                            //过期
                            this.$message.warning("优惠券已过期")
                            return;
                        }
                        let bool = Number(item.conditions) <= vm.totalValue.totalMoney ? true : false;
                        if (!bool) {
                            vm.totalValue.totalMoney = vm.money;
                            this.$message.warning("优惠券没有满足条件")
                            return;
                        } else {
                            //满足优惠券条件
                            vm.totalValue.totalMoney = vm.totalValue.totalMoney - Number(item.discount);
                        }
                    }
                })
            },
            loadCartList() {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.post(vm.API.CART_FINDCARTLIST_URL + "?username=" + username).then(res => {
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
                    vm.loadDiscount();
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
                vm.money = vm.totalValue.totalMoney;
            },
            loadAddress() {
                let vm = this;
                let username = this.Storage.Session.get("username");
                vm.axios.get(vm.API.ADDRESS_FINDBYUSERNAME_URL + "?username=" + username + "&page=" + 1 + "&rows=" + 3).then(res => {
                    vm.addressList = res.data.rows;
                    for (let i = 0; i < vm.addressList.length; i++) {
                        if (vm.addressList[i].isDefault === "1") {
                            vm.address = vm.addressList[i].address;
                            vm.mobile = vm.addressList[i].mobile;
                            vm.contact = vm.addressList[i].contact;
                        }
                    }
                })
            },
            submitOrder() {
                if (this.addressList.length <= 0) {
                    this.$message.info("请先设置收货地址哦~");
                    return;
                }
                this.order.receiverAreaName = this.address//地址
                this.order.receiverMobile = this.mobile;//电话
                this.order.receiver = this.contact;//联系人
                this.order.sourceType = '2';
                this.order.payment = this.totalValue.totalMoney;//实付金额
                let vm = this;
                if (vm.userDiscount.id == undefined) {
                    vm.userDiscount.id = -1;
                }
                if (vm.order.paymentType === 3) {//B悦钱包
                    vm.axios.post(vm.API.EWALLET_FINDAMOUNTBYUSERNAME_URL + "?username=" + vm.username).then(res => {
                        if (res.data == undefined) {
                            let amount = res.data.amount;
                            if (amount > 0) {
                                if ((amount - this.totalValue.totalMoney) > 0) {
                                    vm.axios.post(vm.API.ORDER_ADD_URL + "?username=" + vm.username + "&discountId=" + vm.userDiscount.id, this.order).then(res => {
                                        if (res.data.success) {
                                            if (vm.order.paymentType === 1) {
                                                this.$message.success('二维码先不能使用请选择其他支付方式');
                                                //因微信太贵 不能使用  想使用自己充钱
                                                //vm.$router.push({path:'/index/account/pay'})
                                            } else if (vm.order.paymentType === 3) {
                                                vm.$router.push({path: '/index/account/paySuccess'})
                                            } else {
                                                window.location.href = vm.API.BASE_USER_SERVER + "/index/home"
                                            }
                                        }
                                    })
                                } else {
                                    this.$message.error('钱包余额不足');
                                    return;
                                }
                            } else {
                                this.$message.warning('钱包里已经空空的了哦(⊙o⊙)');
                                return;
                            }
                        } else {
                            this.$message.warning('钱包还没开通哦亲!~');
                            return;
                        }
                    })
                } else {
                    vm.axios.post(vm.API.ORDER_ADD_URL + "?username=" + vm.username + "&discountId=" + vm.userDiscount.id, this.order).then(res => {
                        if (res.data.success) {
                            if (vm.order.paymentType === 1) {
                                this.$message.success('二维码先不能使用请选择其他支付方式');
                                //因微信太贵 不能使用  想使用自己充钱
                                //vm.$router.push({path:'/index/account/pay'})
                            } else if (vm.order.paymentType === 3) {
                                vm.$router.push({path: '/index/account/paySuccess'})
                            } else {
                                window.location.href = vm.API.BASE_USER_SERVER + "/index/home"
                            }
                        }
                    })
                }

            },
            getDiscount() {
                console.log(this.userDiscount);
            },
            loadDiscount() {
                let vm = this;
                vm.axios.get(vm.API.USERDISCOUNT_FINDBYSELLERANDUSERNAME + "?username=" + this.username + "&sellerId=" + this.cartList[0].sellerId).then(res => {
                    vm.userDiscountList = res.data;
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadCartList();
            this.loadAddress();

        },
        filters: {
            moneyFormat(val) {
                return "￥" + Number(val).toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .cart > div {
        display: inline-block;
    }
</style>