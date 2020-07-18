<template>
    <div id="wrapper">
        <Header v-if="!(path === '/login') && !(path === '/register')"></Header>
        <router-view/>
        <Footer v-if="!(path==='/login') && !(path==='/register')"></Footer>
    </div>
</template>

<script>
    import Footer from "./components/common/Footer";
    import Header from "./components/common/Header";

    export default {
        name: 'App',
        components: {
            Header,
            Footer,
        },
        data() {
            return {
                path: '',
            }
        },
        mounted() {
            this.path = this.$route.path;
        },
        watch: {
            $route: {
                handler(val, oldVal) {
                  this.path = val.path;
                },
                // 深度观察监听
                deep: true
            }
        }
    }
</script>

<style>
    a {
        cursor: pointer;
        text-decoration: none;
    }
    .el-badge__content.is-fixed{
        position: absolute;
        top: -15px;
        right: -75px;
    }
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box
    }

    .content_center {
        width: 1200px;
        margin: 0 auto;
    }

    html, body {
        background-color: #F0F3EF;
    }

    #wrapper {
        background-color: #F0F3EF;
    }

    .el-tabs__item {
        width: 210px;
        text-align: center;
    }
</style>
