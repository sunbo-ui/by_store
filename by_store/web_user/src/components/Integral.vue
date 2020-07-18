<template>
    <div>
        <div class="p-2">
            <div class="p-1">
                <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">我的悦豆</span>

            </div>

            <el-card>
                <div>
                    <div style="display: flex">
                        <div style="flex: 1;display:flex;justify-content: center;align-items: center">
                            <span style="color:#909399;font-size: 30px;font-weight: bold;">可用的悦豆</span>
                            <span style="color:#F56C6C;font-size: 30px;font-weight: bold;margin-left: 30px;">{{integral}}</span>

                        </div>
                        <div style="flex: 2" class="p-1 mt-2">
                            <span style="color: #909399">换取规则:</span>
                            <span style="color: #909399">50悦豆可换取一元</span>
                            <el-input type="text" v-model="money" ref="wallet"/>
                            <div style="display:flex;flex-flow: nowrap; flex-wrap:wrap">
                                <div :class="{active:categoryIndex==1}" @click="showColor(1)" class="card">50悦豆</div>
                                <div :class="{active:categoryIndex==2}" @click="showColor(2)" class="card">100悦豆</div>
                                <div :class="{active:categoryIndex==3}" @click="showColor(3)" class="card">500悦豆</div>
                                <div :class="{active:categoryIndex==4}" @click="showColor(4)" class="card">1000悦豆</div>
                                <div :class="{active:categoryIndex==5}" @click="showColor(5)" class="card">2000悦豆</div>
                                <div :class="{active:categoryIndex==6}" @click="showColor(6)" class="card">其他</div>
                            </div>
                            <div class="mt-3">
                                <el-button type="success" @click="insertEwallet()">兑换</el-button>
                            </div>
                        </div>
                    </div>
                    <template>
                        <el-table
                                :data="integralList"
                                stripe
                                style="width: 100%">
                            <el-table-column
                                    label="来源/用途"
                                    width="280">
                                <template slot-scope="scope">
                                    <div style="display: flex">
                                        <div style="flex: 1">
                                            <img style="width: 100px;height: 100px;" :src="scope.row.orderItem.picPath"
                                                 alt="">
                                        </div>
                                        <div style="flex: 2;display: flex;flex-direction: column;margin-left: 10px;">
                                            <div>{{scope.row.orderItem.title}}</div>
                                            <div v-if="scope.row.orderItem.goodsId" style="margin-top: 10px;">商品编号:{{scope.row.orderItem.goodsId}}</div>
                                            <div style="margin-top: 10px;">悦豆操作</div>
                                        </div>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="积分变化"
                                    width="180">
                                <template slot-scope="scope">
                                    <div style="display: flex">
                                        <span v-if="scope.row.integralDesc.integralChange > 0"
                                              style="color: red;font-weight: bold;">+{{scope.row.integralDesc.integralChange}}</span>
                                        <span v-else style="color: lime;font-weight: bold;">{{scope.row.integralDesc.integralChange}}</span>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="日期">
                                <template slot-scope="scope">
                                    <div style="display: flex">
                                        {{scope.row.integralDesc.createTime}}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="address"
                                    label="备注">
                                <template slot-scope="scope">
                                    <div style="display: flex">
                                        {{scope.row.integralDesc.note}}
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                </div>
            </el-card>

        </div>
    </div>
</template>

<script>
    import qs from "qs";

    export default {
        name: "Integral",
        data() {
            return {
                username: '',
                integralList: [],
                integral: 0,
                categoryIndex:1,
                money:0,
            }
        }
        ,
        methods: {
            loadIntegral() {
                let vm = this;
                vm.axios.get(vm.API.INTEGRAL_FINDBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    vm.integral = res.data.integral;
                    vm.integralList = res.data.result;
                })
            },
            showColor(index) {
                this.categoryIndex = index
                if (index == 6) {
                    this.$refs.wallet.focus();
                } else if (index == 5) {
                    this.money = 2000;
                } else if (index == 4) {
                    this.money = 1000;
                } else if (index == 3) {
                    this.money = 500;
                } else if (index == 2) {
                    this.money = 100;
                } else if (index == 1) {
                    this.money = 50;
                }
            },
            insertEwallet(){
                let vm = this;
                if(this.money>this.integral){
                    vm.$message.warning("悦豆不足哦")
                    return;
                }
                vm.axios.post(vm.API.INTEGRAL_EXCHANGE_URL+"?username="+this.username+"&integral="+this.money).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message)
                        vm.loadIntegral();
                    }else{
                        vm.$message.error(res.data.message)
                    }
                })
            }
        }
        ,
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadIntegral()
        }
        ,
        filters: {
            moneyFormat(value) {
                return "￥" + Number(value).toFixed(2);
            }
        }
    }
</script>

<style scoped>
    .el-card {
        padding: 0;
    }

    .active {
        background-color: orange;
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

</style>