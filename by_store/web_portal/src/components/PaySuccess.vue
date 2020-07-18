<template>
    <div>
        <div class="content_center"
             style="height: 500px;display: flex;flex-direction: column;justify-content: center;align-items: center">
               <h3>支付成功~可以去个人页面查看收货详情哦(*^_^*)</h3><br>
               <h6>{{time}}秒后跳转个人页面~~</h6>
<!--            <div v-else style="height:500px;display: flex;justify-content: center;align-items: center">-->
<!--                <p style="font-size: 30px;">请先去逛逛商店吧(●'◡'●)</p>-->
<!--                <div style="margin-left: 20px;">-->
<!--                    <el-button style="font-size: 20px;" type="success" @click="goto('/index/home')">go~go~go</el-button>-->
<!--                </div>-->
<!--            </div>-->
        </div>
    </div>
</template>

<script>
    export default {
        name: "PaySuccess",
        data() {
            return {
                time: 3,
                money:null,
            }
        },
        methods:{
            goto(path){
                this.$router.push({
                    path:path
                })
            }
        },
        created() {
            this.money = this.$route.params;
            let vm = this;
            let timer = setInterval(function () {
                console.log(vm.time)
                vm.time--;
                if (vm.time <= 0) {
                    clearInterval(timer);
                    window.location.href = vm.API.BASE_USER_SERVER+"/index/home"
                }
            }, 1000)

        }
    }
</script>

<style scoped>

</style>