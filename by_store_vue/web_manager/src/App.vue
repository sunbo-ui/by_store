<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>

<script>

    export default {
        name: 'App',
        components: {},

    }
</script>

<style>

    html, body {
        width: 100%;
        height: 100%;
    }
    *{
        margin: 0;
        padding: 0;
    }
    .el-menu-item-group__title {
        display: none;
    }

    #app {
        width: 100%;
        height: 100%;
    }

    .el-card__body {
        padding-left: 20px;
    }
    .el-select{
        width: 100%;
    }
</style>
