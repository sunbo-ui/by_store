<template>
    <div class="content_center" style="display: flex;height: 100%;width: 100%;">
        <div style="flex:1 ;">
            <div v-for="(item,key) in customerList" :class="{ 'active': index == key }" @click="loadSocket(key)"
                 style="cursor:pointer;display:flex;align-items:center;width: 100%;height: 80px;">
                <span style="padding: 10px;">
                    <img :src="item.pic" style="width: 50px;height: 50px;border-radius: 50%" alt="">
                    <span style="margin-left: 10px;font-size: 20px;">{{item.username}}</span>
                </span>
            </div>
        </div>
        <div style="display:flex;flex-direction:column;flex:2;width: 100%;height: 100%;">
            <div style="flex:3;background-color: #ced4da;">
                <!--上方信息显示-->
                <div ref="chatContent" style="flex:5;padding: 20px;position:relative;clear: both;overflow: auto">
                    <div v-for="item in messageList">
                        <!--对方的信息显示-->
                        <div v-if="item.flag == 0" style="float: left">
                            <img style="display: inline-block;border-radius: 50%" :src="item.pic" width="50" alt="">
                            <div style="display:inline-block;margin-left: 20px;">
                                <div style="display:inline-block;background-color: #fff;padding:10px;position:relative;">
                                    <div style="width: 0px;height: 0px;background-color: #fff;  border: 10px solid;
     border-color:  #ced4da white  #ced4da   #ced4da;position: absolute;top: 10px;left: -20px;"></div>
                                    <span style="overflow: auto" v-html="item.msgContent"></span>
                                </div>
                            </div>
                        </div>
                        <br><br><br><br>

                        <!--自己输入的信息-->
                        <div v-if="item.flag == 1"  style="float:right;">
                            <div style="display:inline-block;">
                                <div style="display:inline-block;background-color: #fff;padding:10px;position:relative;">
                                    <div style="width: 0px;height: 0px;background-color: #fff;  border: 10px solid;
     border-color: #ced4da #ced4da #ced4da white;position: absolute;top: 10px;right: -20px;"></div>
                                    <span style="overflow: auto" v-html="item.msgContent"></span>
                                </div>
                            </div>
                            <img style="display: inline-block;margin: 20px;border-radius: 50%" :src="item.pic" width="50" alt="">
                        </div>
                        <br><br><br><br>

                    </div>
                </div>
            </div>
            <div style="flex: 1;position:relative;">
                <vue-ueditor-wrap ref="ue" @ready="ready" v-model="msg" :config="config"></vue-ueditor-wrap>
                <el-button size="medium"
                           @click="socketSend"
                           @keydown="socketSend"
                           type="success"
                           style="position:absolute;bottom:30px;right: 20px;z-index: 999;" plain>发送
                </el-button>
            </div>
        </div>
        <div style="flex:1 ;"/>
    </div>
</template>

<script>
    import VueUeditorWrap from 'vue-ueditor-wrap' // ES6 Module
    export default {
        name: "Communication",
        data() {
            return {
                websocket: null,
                username: '',
                msg: '',
                config: {
                    UEDITOR_HOME_URL: '/umedit/',// 需要令此处的URL等于对应 ueditor.config.js 中的配置。
                    initialFrameWidth: 1200,
                    initialFrameHeight: 200,
                    // 编辑器不自动被内容撑高
                    autoHeightEnabled: false,
                    // 上传文件接口（这个地址是我为了方便各位体验文件上传功能搭建的临时接口，请勿在生产环境使用！！！）
                    serverUrl: this.API.BASE_SERVER_URL + '/umedit/jsp/controller.jsp',
                },
                customerList: [],
                index: -1,
                imgUrl: '',
                messageList: [],
            }
        },
        components: {
            VueUeditorWrap
        },
        methods: {
            ready(ue) {
                let vm = this;
                window.UE.Editor.prototype._bkGetActionUrl = window.UE.Editor.prototype.getActionUrl
                window.UE.Editor.prototype.getActionUrl = function (action) {
                    if (action === 'upload/uploadImage') {
                        return vm.API.BASE_SERVER_URL + '/upload/uploadImage'
                    } else {
                        return this._bkGetActionUrl.call(this, action)
                    }
                }
                ue.addListener('ready', () => {
                    this.$emit('getUe', this.msg)
                })
            },
            socketError() {
                this.$message.error("连接错误!");
            },
            socketClose() {
                this.$message.error("连接关闭!");
            },
            socketOpen() {
                // 打开时
                this.$message.success("连接成功!");
            },
            socketMessage(e) {
                if (e) {
                    let msg = JSON.parse(e.data);
                    console.log(msg);
                    this.messageList = msg;
                }
            },
            socketSend() {
                //先判断是否连接
                let vm = this;

                if (vm.websocket == null) {
                    vm.$message.warning("您还没有连接!!!");
                    return;
                }

                if (this.msg == null || this.msg == '') {
                    this.$message.warning("消息不能为空!!!");
                    return;
                }
                vm.axios.get(vm.API.SELLER_FINDBYID_URL + "?sellerId=" + this.username).then(res => {
                    vm.imgUrl = res.data.pic;
                    console.log( vm.imgUrl);
                })

                //发送消息
                // //构造消息的json格式
                // let msgJson = {
                //     senderId: this.username, //得到发送者标识
                //     msgContent: this.msg,
                //     toId: this.customer.username,//得到前台传过来的消息接收者id
                //     pic: this.imgUrl,
                //     flag:"1",
                // };
                // this.messageList.push(msgJson);
                // this.websocket.send(JSON.stringify(msgJson));
                // this.msg = "";
            },
            loadSocket(key) {
                this.index = key;
                //通过key获取对应得到customer信息
                this.customer = this.customerList[key];
                //判断当前连接是否为空
                if (this.websocket != null) {
                    alert("您已经连接了，请不要重复连接！");
                    return;
                }
                //开始连接
                // 首先判断是否 支持 WebSocket
                if ('WebSocket' in window) {
                    //路径ws + ip + port + 自定义路径
                    this.websocket = new WebSocket("ws://localhost:8084/websocket/" + this.username + "@" + this.customer.username + "@1");
                    //this.websocket.onclose = this.socketClose;
                    this.websocket.onopen = this.socketOpen;
                    // this.websocket.onerror = this.socketError;
                    this.websocket.onmessage = this.socketMessage;
                } else {
                    alert("浏览器不支持连接！")
                    return;
                }
            },
            loadCustomer() {
                let vm = this;
                vm.axios.get(vm.API.FINDREDISKEY).then(res => {
                    vm.customerList = res.data;
                })
            },
            scrollToBottom() {
                this.$nextTick(() => {
                    this.$refs.chatContent.scrollTop = this.$refs.chatContent.scrollHeight;
                })
            }
        },
        mounted() {
            this.scrollToBottom()
        },
        updated() {
            this.scrollToBottom()
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadCustomer();
        },
        destroyed() {
            this.socketClose()
        },

    }
</script>

<style scoped>
    .active {
        background-color: #DCDFE6;
    }
</style>