<template>
    <div style="padding: 20px">
        <template>
            <el-tabs v-model="activeName" type="card" >
                <el-tab-pane label="修改密码" name="first">
                    <el-form ref="form" :model="form" label-width="180px">
                        <el-form-item label="原密码">
                            <el-input v-model="form.pwd" placeholder="请输入原密码"></el-input>
                        </el-form-item>
                        <el-form-item label="新密码">
                            <el-input v-model="form.newPwd" placeholder="请输入新密码"></el-input>
                        </el-form-item>
                        <el-form-item label="确认新密码">
                            <el-input v-model="form.newPwdTwo" placeholder="确认新密码"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary">修改</el-button>
                            <el-button>保存</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Password",
        data(){
            return {
                activeName: 'first',
                form: {
                   pwd:'',
                   newPwd:'',
                   newPwdTwo:'',
                }
            };
        }
    }
</script>

<style  scoped>
</style>