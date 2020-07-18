<template>
    <div>
        <!--轮播图-->
        <div style="display: flex;height: 460px;" class="content_center">
            <div style="flex:1.5;background-color: #fff;" class="item">
                <div><h3><a href="#">家用电器</a></h3></div>
                <div><h3><a href="#">手机/运营商/数码</a></h3></div>
                <div><h3><a href="#">电脑/办公</a></h3></div>
                <div><h3><a href="#">家居、家具、家装、厨具</a></h3></div>
                <div><h3><a href="#">服饰内衣</a></h3></div>
                <div><h3><a href="#">图书/音像/数字商品</a></h3></div>
                <div><h3><a href="#">美装/清洁/宠物</a></h3></div>
                <div><h3><a href="#">运动/健康</a></h3></div>
                <div><h3><a href="#">房产/汽车/汽车用品</a></h3></div>
                <div><h3><a href="#">男鞋/运动/户外</a></h3></div>
                <div><h3><a href="#">理财/众筹/白条/保险</a></h3></div>
                <div><h3><a href="#">母婴/玩具/乐器</a></h3></div>
                <div><h3><a href="#">女鞋/箱包/钟表/珠宝</a></h3></div>
                <div><h3><a href="#">安装/维修/清洗/二手</a></h3></div>
                <div><h3><a href="#">工业品</a></h3></div>
            </div>
            <div style="flex:5;background-color: yellow;">
                <template>
                    <el-carousel style="height: 460px" :interval="5000" arrow="hover">
                        <el-carousel-item style="height: 460px" v-if="index<5" v-for="(item,index) in contentList">
                            <a :href="item.url">
                                <img :src="item.pic" height="460" width="705" alt="">
                            </a>
                        </el-carousel-item>
                    </el-carousel>
                </template>
            </div>
            <div style="flex:2;display:flex;justify-content: center;position: relative">
                <div style="border: 1px solid #e0e0e0;width: 270px;height: 178px; background-color: #fff;padding: 10px;">
                    <div style="text-align: center">
                        <img style="border-radius: 50%;width: 60px;height: 60px;box-shadow:  0 2px 12px 2px rgba(0, 0, 0, 0.3);"
                             :src="imgUrl" alt="">
                    </div>
                    <p style="text-align: center;margin-top: 10px;"> Hi~欢迎来到京东!</p>
                    <div style="text-align: center" v-if="username == null">
                        <a style="margin-right: 10px;color: #007bff" @click="gotoLogin">登录</a>
                        <a href="#">注册</a>
                    </div>
                    <div style="text-align: center" v-else>
                        <span style="font-weight: bold;font-size: 20px;">{{username}}</span>
                    </div>
                </div>
                <div style="position: absolute;left: 6px;top: 160px">
                    <div class="slideshowRight">
                        <ul>
                            <li>
                                <a>
                                    <img src="../assets/img/phone.png" alt="" width="40" height="40">
                                    <div>话费</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/jipiao.png" alt="" width="30" height="30">
                                    <div>机票</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/dianyingpiao.png" alt="" width="30" height="30">
                                    <div>电影票</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/play.png" alt="" width="30" height="30">
                                    <div>游戏</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/caipiao.png" alt="" width="30" height="30">
                                    <div>彩票</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/jiiayouzhan.png" alt="" width="30" height="30">
                                    <div>加油站</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/jiudian.png" alt="" width="30" height="30">
                                    <div>酒店</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/huochepiao.png" alt="" width="30" height="30">
                                    <div>火车票</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/zhongchou.png" alt="" width="30" height="30">
                                    <div>众筹</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/licai.png" alt="" width="30" height="30">
                                    <div>理财</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/lipingka.png" alt="" width="30" height="30">
                                    <div>礼品卡</div>
                                </a>
                            </li>
                            <li>
                                <a>
                                    <img src="../assets/img/baitiao.png" alt="" width="30" height="30">
                                    <div>白条</div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div style="position: absolute;left: 6px;top: 376px">
                    <img src="../assets/img/ad1.jpg" width="270" height="84" alt="">
                </div>
            </div>
        </div>
        <!--秒杀-->
        <div class="content_center seckill" style="background-color: #fff;margin-top: 20px;height: 290px">
            <ul>
                <li style="width: 190px; height: 290px;background-color: red;position: relative">
                    <a href="#">
                        <div style="font-size: 34px;color:#fff;position:absolute;top:42px;left:35px;">B悦秒杀</div>
                        <div style="font-size: 20px;color: rgba(255, 255, 255, 0.5);position:absolute;top:92px;left:35px;">
                            FLASH DEALS
                        </div>
                        <img style="width: 40px;height: 40px;position:absolute;top:122px;left:70px;"
                             src="../assets/img/sd.png" alt="">
                        <div style="font-size: 16px;color: #fff;position:absolute;top:172px;left:30px;">距离本场结束还剩</div>
                        <div>
                            <div style="text-align:center;height: 40px;width: 40px;background-color: black;position:absolute;top:210px;left:25px;">
                                <span style="color: #fff;font-size: 20px;" v-if="hour<=9">0</span>
                                <span style="color: #fff;font-size: 20px;">{{hour}}</span>
                            </div>
                            <div style="text-align:center;height: 40px;width: 40px;background-color: black;position:absolute;top:210px;left:75px;">
                                <span style="color: #fff;font-size: 20px;" v-if="min<=9">0</span>
                                <span style="color: #fff;font-size: 20px;">{{min}}</span>
                            </div>
                            <div style="text-align:center;height: 40px;width: 40px;background-color: black;position:absolute;top:210px;left:125px;">
                                <span style="color: #fff;font-size: 20px;" v-if="second<=9">0</span>
                                <span style="color: #fff;font-size: 20px;">{{second}}</span>
                            </div>
                        </div>
                    </a>
                </li>
                <li style="width: 240px; height: 290px;text-align: center">
                    <div><img src="../assets/img/skill1.webp" width="150px" height="150px" style="margin-top: 20px;"
                              alt=""></div>
                    <p style="margin-top: 20px;color: #909399">威尔贝鲁(WELLBER...</p>
                    <div>
                        <span style="background-color: red;color: #fff;display: inline-block;width: 100px;margin-left: 20px;">￥100.0</span>
                        <span style="background-color: #E4E1E1;color: #333333;display: inline-block;width: 100px;"><del>￥170.0</del></span>
                    </div>
                </li>
                <li style="width: 240px; height: 290px;text-align: center">
                    <div><img src="../assets/img/skill2.webp" width="150px" height="150px" style="margin-top: 20px;"
                              alt=""></div>
                    <p style="margin-top: 20px;color: #909399">联想ThinkPad 翼480...</p>
                    <div>
                        <span style="background-color: red;color: #fff;display: inline-block;width: 100px;margin-left: 20px;">￥100.0</span>
                        <span style="background-color: #E4E1E1;color: #333333;display: inline-block;width: 100px;"><del>￥170.0</del></span>
                    </div>
                </li>
                <li style="width: 240px; height: 290px;text-align: center">
                    <div><img src="../assets/img/skill3.webp" width="150px" height="150px" style="margin-top: 20px;"
                              alt=""></div>
                    <p style="margin-top: 20px;color: #909399">新日(Sunra) 电动车</p>
                    <div>
                        <span style="background-color: red;color: #fff;display: inline-block;width: 100px;margin-left: 20px;">￥100.0</span>
                        <span style="background-color: #E4E1E1;color: #333333;display: inline-block;width: 100px;"><del>￥170.0</del></span>
                    </div>
                </li>
                <li>
                    <div><img src="../assets/img/skill4.webp" width="150px" height="150px" style="margin-top: 20px;"
                              alt=""></div>
                    <p style="margin-top: 20px;color: #909399">梦的解析（2019精装珍藏版 无删节！）</p>
                    <div>
                        <span style="background-color: red;color: #fff;display: inline-block;width: 100px;margin-left: 20px;">￥100.0</span>
                        <span style="background-color: #E4E1E1;color: #333333;display: inline-block;width: 100px;"><del>￥170.0</del></span>
                    </div>
                </li>
            </ul>
        </div>
        <!--今日推荐-->
        <div class="content_center tui" style="background-color: #fff;margin-top: 20px;height: 290px">
            <div style="padding:10px;border-bottom: 1px solid #e0e0e0;color: #303133;font-weight: bold;font-size: 20px;">
                今日推荐
            </div>
            <ul>
                <li>
                    <a>
                        <div>
                            <img src="../assets/img/tui1.webp" alt="">
                        </div>
                        <p style="margin-top: 10px;color: #909399">雷神（ThundeRobot）</p>
                        <div>
                            <span style="color: red;font-size: 20px;font-weight: bold;">￥5790.0</span>
                        </div>
                    </a>
                </li>
                <li>
                    <a>
                        <div>
                            <img src="../assets/img/tui1.webp" alt="">
                        </div>
                        <p style="margin-top: 10px;color: #909399">雷神（ThundeRobot）</p>
                        <div>
                            <span style="color: red;font-size: 20px;font-weight: bold;">￥5790.0</span>
                        </div>
                    </a>
                </li>
                <li>
                    <a>
                        <div>
                            <img src="../assets/img/tui1.webp" alt="">
                        </div>
                        <p style="margin-top: 10px;color: #909399">雷神（ThundeRobot）</p>
                        <div>
                            <span style="color: red;font-size: 20px;font-weight: bold;">￥5790.0</span>
                        </div>
                    </a>
                </li>
                <li>
                    <a>
                        <div>
                            <img src="../assets/img/tui1.webp" alt="">
                        </div>
                        <p style="margin-top: 10px;color: #909399">雷神（ThundeRobot）</p>
                        <div>
                            <span style="color: red;font-size: 20px;font-weight: bold;">￥5790.0</span>
                        </div>
                    </a>
                </li>
                <li>
                    <a>
                        <div>
                            <img src="../assets/img/tui1.webp" alt="">
                        </div>
                        <p style="margin-top: 10px;color: #909399">雷神（ThundeRobot）</p>
                        <div>
                            <span style="color: red;font-size: 20px;font-weight: bold;">￥5790.0</span>
                        </div>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                contentList: [],
                recommendList: [],
                username: '',
                hour: 0,//时
                min: 0,//分
                second: 0,//秒
                intDiff: 5000,//倒计时总秒杀
                imgUrl: '',
            }
        },
        methods: {
            getContent() {
                let vm = this;
                vm.axios.post(vm.API.CONTENT_FINDBYCATEGORYID_URL + "?categoryId=1").then(res => {
                    let arrList = [];
                    res.data.forEach((item) => {
                        if (item.status == 1) {
                            arrList.push(item);
                        }
                    });
                    //算法排序根据  sortOrder
                    arrList.sort();
                    vm.contentList = arrList;
                })
            },
            getRecommend() {
                let vm = this;
                vm.axios.post(vm.API.CONTENT_FINDBYCATEGORYID_URL + "?categoryId=2").then(res => {
                    let arrList = [];
                    res.data.forEach((item) => {
                        if (item.status == 1) {
                            arrList.push(item);
                        }
                    });
                    //算法排序根据  sortOrder
                    arrList.sort();
                    vm.recommendList = arrList;
                    console.log(vm.recommendList);
                })
            },
            gotoLogin() {
                this.$router.push({path: '/login'}).catch(err => {
                    console.log(err)
                })
            },
            getDate() {
                if (this.intDiff > 0) {
                    this.hour = Math.floor((this.intDiff / (60 * 60)));
                    this.min = Math.floor((this.intDiff / 60) - (this.hour * 60));
                    this.second = Math.floor(this.intDiff - (this.hour * 60 * 60) - (this.min * 60));
                }
                this.intDiff--;
            },
            getInterval() {
                let vm = this;
                let timer = setInterval(function () {
                    vm.getDate();
                }, 1000);
                if (this.intDiff == 0) {
                    clearInterval(timer)
                }
            },
            loadHeaderImg() {
                let vm = this;
                vm.axios.get(vm.API.CUSTOMER_FINDBYUSERNAME_URL + "?username=" + this.username).then(res => {
                    vm.imgUrl = res.data.pic;
                })
            },

        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.getContent();
            this.getRecommend();
            this.getInterval();
            this.loadHeaderImg();
        }
    }
</script>

<style scoped>
    a {
        text-decoration: none;
    }

    .item > div {
        width: 210px;
        height: 30px;
    }

    .item > div > h3 > a {
        font-size: 14px;
        color: #808080;
        margin-left: 20px;
    }

    .item > div > h3 > a:hover {
        font-weight: bold;
    }

    .el-carousel__item h3 {
        color: #475669;
        font-size: 18px;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }

    .slideshowRight > ul > li {
        float: left;
        display: block;
        width: 67.4px;
        height: 65px;
        border-bottom: 1px solid #e0e0e0;
        border-left: 1px solid #e0e0e0;
        border-right: 1px solid #e0e0e0;
    }

    .seckill > ul {
        float: left;
    }

    .seckill > ul > li {
        float: left;
    }

    .tui > ul {
        float: left;
    }

    .tui > ul > li {
        float: left;
        width: 190px;
        height: 280px;
        margin-left: 35px;
    }

    .tui > ui > img {
        width: 140px;
        height: 140px;
        margin-top: 5px;
    }

    .slideshowRight > ul > li {
        margin-top: 5px;
        text-align: center;
    }
</style>