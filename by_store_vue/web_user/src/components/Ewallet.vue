<template>
    <div>
        <div class="p-2">
            <div class="p-1">
                <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">我的钱包</span>
            </div>

            <el-card>
                <div v-if="ewallet.userId == ''">
                    <span style="color: #303133;padding: 5px;font-size: 25px;font-weight: bold;">你还没有开通钱包哦</span>
                    <span style="cursor: pointer;color: red;" @click="saveEwallet">去开通</span>
                </div>
                <div v-else style="display: flex;flex-flow: column;">
                    <div style="flex: 2;display:flex;">
                        <div style="flex:1;">
                            <!--图片-->
                            <img :src="imgUrl"
                                 style="display:block;width: 150px;height: 150px;border-radius: 50%;"></img>
                        </div>
                        <div style="flex: 1" class="p-1 mt-2">
                            <div style="color: #E6A23C;padding: 3px;font-size: 30px;font-weight: bold;">
                                <span>姓名:</span>
                                <span>{{username}}</span>
                            </div>
                            <div style="color: #C0C4CC;padding: 5px;font-size: 25px;font-weight: bold;">
                                <span>ID:</span>
                                <span>{{customer.id}}</span>
                            </div>
                            <div>
                                <el-button type="success" @click="insertAmount()">充值</el-button>
                            </div>
                        </div>
                        <div style="flex: 3;">
                            <el-input v-model="money" type="text" ref="wallet"/>
                            <div style="display:flex;flex-flow: nowrap; flex-wrap:wrap">
                                <div :class="{active:categoryIndex==1}" @click="showColor(1)" class="card">10元</div>
                                <div :class="{active:categoryIndex==2}" @click="showColor(2)" class="card">30元</div>
                                <div :class="{active:categoryIndex==3}" @click="showColor(3)" class="card">50元</div>
                                <div :class="{active:categoryIndex==4}" @click="showColor(4)" class="card">100元</div>
                                <div :class="{active:categoryIndex==5}" @click="showColor(5)" class="card">200元</div>
                                <div :class="{active:categoryIndex==6}" @click="showColor(6)" class="card">其他</div>
                            </div>
                        </div>
                    </div>
                    <div style="flex: 3;display: flex;">
                        <div style="flex: 1">
                            <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">总余额:</span>
                            <div style="color: #F56C6C;padding: 25px;font-size: 60px;font-weight: bold;">
                                {{ewallet.amount|moneyFormat}}
                            </div>
                        </div>
                        <div style="flex:2;">
                            <span style="color: #909399;font-size: 20px;" class="mt-2">最近三次消费记录</span>
                            <div @mouseenter="enter()" @mouseleave="leave()" class="mt-2" style="display: flex"
                                 v-for=" (pay,index) in payList" :key="index">
                                <div style="flex:1">
                                    <img style="display:inline-block; width: 80px;height: 80px;" :src="pay.pic" alt="">
                                </div>
                                <div style="flex:3;display: flex">
                                    <div style="flex: 3">
                                        <div>
                                            <span>订单Id:</span>
                                            <span style="color:#303133;">{{pay.orderId}}</span>
                                        </div>
                                        <div style="color: #606266">{{pay.title}}</div>
                                        <div style="color: #606266">{{pay.createTime}}</div>
                                    </div>
                                    <div style="flex: 1" class="mr-2">
                                        <div style="color: #67C23A;margin-top: 10px;">{{payType[pay.payType]}}</div>
                                    </div>
                                    <div style="flex: 1">
                                        <el-button  v-if="show" type="danger"
                                                   @click="goto(`/index/orderDetail/${pay.orderId}/${pay.status}`)">查看订单
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </el-card>

        </div>
    </div>
</template>

<script>
    import qs from "qs";
    export default {
        name: "Ewallet",
        data() {
            return {
                username: '',
                imgUrl: '',
                customer: {},
                ewallet: {
                    userId: '',
                    amount: 0,
                },
                money:10,
                payList: [],
                show: false,
                payType: {
                    1: '微信支付',
                    2: '货到付款',
                    3: '钱包支付'
                },
                categoryIndex: 1,
            }
        },
        methods: {
            insertAmount(){
                let vm = this;
                vm.axios.post(vm.API.EWALLET_INSERTAMOUNT_URL,qs.stringify({username:this.username,amount:this.money})).then(res => {
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        this.loadEwallet();
                    }else{
                        vm.$message.success(res.data.message);
                    }
                })
            },
            showColor(index) {
                this.categoryIndex = index
                if(index == 6){
                    this.$refs.wallet.focus();
                }else if(index == 5){
                    this.money = 200;
                }else if(index == 4){
                    this.money = 100;
                }else if(index == 3){
                    this.money = 50;
                }else if(index == 2){
                    this.money = 30;
                }else if(index == 1){
                    this.money = 10;
                }
            },
            goto(path) {
                this.$router.push({path: path})
            },
            enter() {
                this.show = true;
            },
            leave() {
                this.show = false;
            },
            loadHeaderImg() {
                let vm = this;
                vm.axios.get(vm.API.CUSTOMER_FINDBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    vm.customer = res.data;
                    vm.imgUrl = res.data.pic;
                })
            },
            saveEwallet() {
                let vm = this;
                vm.axios.get(vm.API.EWALLET_SAVE_URL + "?username=" + this.username).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message);
                        vm.loadEwallet();
                    } else {
                        vm.$message.success(res.data.message);
                    }
                })
            },
            loadEwallet() {
                let vm = this;
                vm.axios.get(vm.API.EWALLET_FINDAMOUNTBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    if (res.data) {
                        vm.ewallet = res.data;
                        vm.loadPay();
                    }
                })
            },
            loadPay() {
                let vm = this;
                vm.axios.get(vm.API.PAY_FINDBYUSER_URL + "?username=" + this.username).then(res => {
                    vm.payList = res.data;
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadHeaderImg();
            this.loadEwallet();
        },
        filters: {
            moneyFormat(value) {
                return "￥" + value.toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .el-card {
        padding: 0;
    }

    .card {
        display: inline-block;
        width: 80px;
        height: 80px;
        display: flex;
        justify-content: center;
        align-items: center;
        border: 1px dashed #ccc;
        margin: 10px 10px 0 10px;
        cursor: pointer;
    }

    .active {
        background-color: orange;
    }
</style>