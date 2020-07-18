<template>
    <div>
        <div class="content_center" style="height: 500px;display: flex;justify-content: center;align-items: center">
            <h3>支付失败~重新操作下,,可联系<a>管理员</a></h3>
            <div style="margin-left: 20px;">
                <el-button style="font-size: 20px;" type="success" @click="goto('/index/home')">go~go~go</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PayFail",
        methods:{
            goto(path){
                this.$router.push({
                    path:path
                })
            }
        }
    }
</script>

<style scoped>

</style>