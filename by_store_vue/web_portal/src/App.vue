<template>
    <div id="wrapper">
            <Header></Header>
            <Index></Index>
            <Footer></Footer>
    </div>
</template>

<script>
    import Header from './components/common/Header';
    import Footer from "./components/common/Footer";
    import Index from "./components/Index";
    export default {
        name: 'App',
        components: {
            Header,
            Footer,
            Index
        }
    }
</script>

<style>
    *{
        margin: 0;
        padding: 0;
        box-sizing: border-box
    }
    .content_center{
        width: 1200px;
        margin: 0 auto;
    }
    html,body{
        background-color: #F0F3EF;
    }
    #wrapper{
        background-color: #F0F3EF;
    }

</style>
