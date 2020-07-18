<template>
    <div id="main">
        <h1 style="margin-left: 20px;margin-top: 10px;">
            <span style="font-size: 20px">系统面板</span>
            <span style="font-size: 8px;margin-left: 10px;">首页</span>
        </h1>
        <div style="height: 900px;margin-top: 30px; ">
            <div>
                <el-col :span="11" >
                    <el-card shadow="hover" class="firstLeft">
                        <h1 style="font-size: 50px;">1800</h1>
                        <p>订单总数</p>
                    </el-card>
                    <el-button class="payDetail">详细</el-button>
                </el-col>
                <el-col :span="11" class="right">
                    <el-card shadow="hover" class="firstRight">
                        <h1 style="font-size: 50px;">20</h1>
                        <p>待支付订单</p>
                    </el-card>
                    <el-button class="payDetail">详细</el-button>
                </el-col>
            </div>
            <div>
                <el-col :span="11" style="margin-top: 30px;">
                    <el-card shadow="hover" style="background-color:#E6A23C">
                        <h1 style="font-size: 50px;">3800</h1>
                        <p>在线用户</p>
                    </el-card>
                    <el-button class="payDetail">详细</el-button>
                </el-col>
                <el-col :span="11" class="right" style="margin-top: 30px;">
                    <el-card shadow="hover" style="background-color:#F56C6C">
                        <h1 style="font-size: 50px;">36000</h1>
                        <p>日访问量</p>
                    </el-card>
                    <el-button class="payDetail">详细</el-button>
                </el-col>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "home"
    }
</script>

<style scoped>
    #main{
        padding: 20px;
    }
    .el-card{
        height: 150px;
    }
    .firstLeft{
        background-color: #00c0ef !important;
    }
    .firstRight{
        background-color: #67C23A !important
    }
    .right{
        float: right;
    }
    .el-card__body{
        margin-top: 30px;
    }

    .payDetail{
        width: 100%;
        height: 25px;
        background-color: rgba(0, 0, 0, 0.1);
        color: rgba(255, 255, 255, 0.8);
        position: relative;
        top: -28px;
        border: 0;
    }
</style>