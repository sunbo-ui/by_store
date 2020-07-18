<template>
    <div>
        <div class="content_center" style="text-align: center">
            <div v-if="url!=null">
                <div style="margin-top:  10px;">
                    <p>请使用微信扫一扫</p>
                    <p>扫描二维码支付</p>
                </div>
                <vue-qr :logoSrc="imageUrl" :text="url" :size="400"></vue-qr>
                <h4 style="margin-top: 10px;">订单提交成功,请您及时付款! 订单号:{{out_trade_no}}</h4>
                <h4><em>￥{{money}}</em>元</h4>
            </div>
            <div v-else style="height:500px;display: flex;justify-content: center;align-items: center">
                <p style="font-size: 30px;">请先去逛逛商店吧(●'◡'●)</p>
                <div style="margin-left: 20px;">
                    <el-button style="font-size: 20px;" type="success" @click="goto('/index/home')">go~go~go</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import vueQr from 'vue-qr'

    export default {
        name: "Pay",
        data() {
            return {
                imageUrl: require("../assets/logo.png"),
                money: 0,
                out_trade_no: '',//支付订单号
                url: null,
                username: '',
            }
        },
        methods: {
            createNative() {
                let vm = this;
                vm.axios.post(vm.API.PAY_CREATENATIVE_URL + "?username=" + vm.username).then(res => {
                    //显示订单号和金额
                    vm.money = (res.data.total_fee / 100).toFixed(2);
                    vm.out_trade_no = res.data.out_trade_no;
                    console.log(res.data);
                    vm.url = res.data.code_url;
                    //vm.queryPayStatus()//调用查询支付结果
                })
            },
            queryPayStatus() {
                let vm = this;
                vm.axios.get(vm.API.PAY_QUERYPAYSTATUS_URL+"?out_trade_no="+vm.out_trade_no+"&username="+vm.username).then(res=>{
                    if(res.data.success){
                        vm.$router.push({
                            path:'/index/account/paySuccess',
                            params:vm.money,
                        })
                    }else{
                        if(res.data.message =='二维码超时'){
                            vm.createNative()
                        }else{
                            vm.$router.push({
                                path:'/index/account/payFail',
                            })
                        }
                    }
                })
            },

            goto(path) {
                this.$router.push({
                    path: path
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.createNative()
        },
        components: {
            vueQr
        },
    }
</script>

<style scoped>

</style>