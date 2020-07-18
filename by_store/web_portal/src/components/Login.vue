<template>
    
</template>

<script>
    export default {
        name: "Login",
        methods: {
            //获取地址栏url后的参数值
            getUrlParam(key) {
                let href = window.location.href;
                let url = href.split("?");
                if (url.length <= 1) {
                    return "";
                }
                let params = url[1].split("&");

                for (let i = 0; i < params.length; i++) {
                    let param = params[i].split("=");
                    if (key == param[0]) {
                        return param[1];
                    }
                }
            }
        },
        mounted() {
            let jsessionid =  this.getUrlParam("jsessionid");
            if(jsessionid !== '' && jsessionid!== undefined  && jsessionid !== 'null'){
                this.cookieUtil.setCookie('jsessionid',jsessionid)
                let vm = this;
                this.axios.post(this.API.BASE_SERVER_URL+"/user/login").then(res=>{
                    vm.Storage.Session.set("username",res.data.message);
                    vm.$router.go(-1)
                })
            }else{
                window.location.href = this.API.BASE_SERVER_URL+"/username"
            }
        },
        watch: {
            $route: {
                handler: function(val, oldVal){
                    console.log(val);
                },
                // 深度观察监听
                deep: true
            }
        },
    }
</script>

<style scoped>

</style>