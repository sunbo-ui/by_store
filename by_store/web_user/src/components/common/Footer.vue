<template>
    <!--页面底部-->
    <div style="background-color: #EAEAEA;width: 100%;height: 400px;">
        <div class="content_center">
            <div style="height: 100px;width: 1170px; border-bottom: 1px solid #E4E1E1" class="footer-header">
                <ul>
                    <li>品类齐全，轻松购物</li>
                    <li>多仓直发，极速配送</li>
                    <li>正品行货，精致服务</li>
                    <li>天天低价，畅选无忧</li>
                </ul>
            </div>
            <div style="padding:10px 50px;float: left;border-bottom: 1px solid #E4E1E1">
                <div style="width: 183px;height: 150px;float: left">
                    <h5>购物指南</h5>
                    <ul>
                        <li>购物流程</li>
                        <li>会员介绍</li>
                        <li>生活旅行/团购</li>
                        <li>常见问题</li>
                        <li>购物指南</li>
                    </ul>
                </div>
                <div style="width: 183px;height: 150px;float: left">
                    <h5>配送方式</h5>
                    <ul>
                        <li>上门自提</li>
                        <li>211限时达</li>
                        <li>配送服务查询</li>
                        <li>配送费收取标准</li>
                        <li>海外配送</li>
                    </ul>
                </div>
                <div style="width: 183px;height: 150px;float: left">
                    <h5>支付方式</h5>
                    <ul>
                        <li>货到付款</li>
                        <li>在线支付</li>
                        <li>分期付款</li>
                        <li>邮局汇款</li>
                        <li>公司转账</li>
                    </ul>
                </div>
                <div style="width: 183px;height: 150px;float: left">
                    <h5>售后服务</h5>
                    <ul>
                        <li>售后政策</li>
                        <li>价格保护</li>
                        <li>退款说明</li>
                        <li>返修/退换货</li>
                        <li>取消订单</li>
                    </ul>
                </div>
                <div style="width: 183px;height: 150px;float: left">
                    <h5>特色服务</h5>
                    <ul>
                        <li>夺宝岛</li>
                        <li>DIY装机</li>
                        <li>延保服务</li>
                    </ul>
                </div>
                <div style="width: 183px;height: 150px;float: left">
                    <h5>自营覆盖区县</h5>
                    <ul>
                        <li>已向全国2661个区县提供自营配送服务，支持货到付款、POS机刷卡和售后上门服务。</li>
                        <li style="float: right"><a href="#">查看详情</a></li>
                    </ul>
                </div>
            </div>
            <div style="width: 1170px;height: 100px;float: left;text-align: center;margin-top: 10px;">
                <div class="footer-footer"style="width: 1170px;height: 30px;display:flex;justify-content: center" >
                    <ul>
                        <li>关于我们 |</li>
                        <li>联系我们 联系客服 |</li>
                        <li>合作招商 |</li>
                        <li>商家帮助 |</li>
                        <li>营销中心 |</li>
                        <li>友情链接 |</li>
                        <li>销售联盟 |</li>
                        <li>友情链接 |</li>
                        <li>隐私政策 |</li>
                    </ul>
                </div>
                <p>京公网安备 1****002000088号|京ICP证0*****9号|互联网药品信息服务资格证编号(京)-经营性-2014-0008|新出发京零 字第大1*****2号</p>
                <p>京ICP备0*****1号京公网安备1***********2</p>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Footer"
    }
</script>

<style scoped>
    ul{
        list-style: none;
    }
    .footer-header > ul > li {
        width: 292px;
        height: 82px;
        float: left;
        font-size: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .footer-footer > ul >li{
        float: left;

    }
    p{
        float: left;
        width: 1170px;
    }
</style>