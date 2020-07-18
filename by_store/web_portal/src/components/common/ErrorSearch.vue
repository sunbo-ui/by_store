<template>
    <div class="content_center">
        <div  style="height:500px;display: flex;justify-content: center;align-items: center">
            <p style="font-size: 30px;">没有您想要的东西哦(●'◡'●),逛逛别的吧~</p>
            <div style="margin-left: 20px;">
                <el-button style="font-size: 20px;" type="success" @click="goto('/index/home')">go~go~go</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "errorSearch",
        methods:{
            goto(path){
                this.$router.push({
                    path:path
                })
            }
        },
    }
</script>

<style scoped>

</style>