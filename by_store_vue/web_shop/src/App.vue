<template>
  <div id="app">
    <router-view />
  </div>
</template>

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
  .el-tabs__item{
    background-color: #fff;
    color: #CCCCCC;
  }
  #app {
    width: 100%;
    height: 100%;
  }

  .el-card__body {
    padding-left: 20px;
  }
</style>
