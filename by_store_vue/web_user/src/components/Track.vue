<template>
    <div>
        <div class="p-2">
            <div>
                <span style="color: #303133;padding: 5px;font-size: 20px;font-weight: bold;">我的足迹</span>
                <div v-if="newTrackList.length > 0" class="float-right mr-2">
                    <el-button type="success" class="mr-1" @click="selectCheck">
                        <span v-if="flag == 0">选择删除</span>
                        <span v-if="flag == 1">取消选择</span>
                    </el-button>
                    <div style="display: inline-block">
                        <el-button v-if="flag == 0" @click="open" type="danger">删除全部</el-button>
                        <el-button v-if="flag == 1" @click="selectDel" type="danger">确定删除</el-button>
                    </div>
                </div>
            </div>
            <div style="padding: 10px">
                <el-card v-if="newTrackList.length == 0">
                    <span style="font-weight: bold;font-size: 20px;">亲~还没有心动的宝贝,再去逛逛吧</span>
                </el-card>
                <el-card v-else style="margin-top: 10px;" v-for="track in newTrackList">
                    <div>
                        <span v-if="flag == 1">
                            <input @change="selectByTime(track)" v-model="track.checked" type="checkbox"
                                   style="zoom:180%;"></input>
                        </span>
                        {{track.time}}
                    </div>
                    <div class="cart" style="background-color: #F2F6FC;display: flex;margin-top: 30px;"
                         v-for="item in track.trackList">
                        <span v-if="flag == 1"> <input v-model="item.checked" @change="handleCheck(item)"
                                                       type="checkbox"
                                                       style="zoom:180%;position: relative;top: 50%;transform: translateY(-50%);margin: 0 5px 0 5px"></input></span>
                        <div style="flex: 1;  cursor: pointer;" @click="goto(item.goodsId)">
                            <img :src="item.picUrl" width="150" height="150" alt="">
                        </div>
                        <div style="flex: 4;margin-top: 10px;position: relative">
                            <span style="color: #606266;font-size: 22px;  cursor: pointer;" @click="goto(item.goodsId)">{{item.title}}</span>
                            <div style="position:absolute;left:0px;top:80px;color: #333;font-size: 25px;">
                                ￥{{item.price}}
                            </div>
                            <div style="float: right;position: absolute; top:87px;right: 10px">
                                <el-button type="danger" @click="deleteTrack(item.id)">删除足迹</el-button>
                            </div>
                        </div>
                    </div>
                </el-card>
                <!--分页-->
                <div style="margin-top: 10px;">
                    <el-pagination
                            @current-change="loadTrack"
                            :current-page.sync="page"
                            :page-size="pageSize"
                            layout="total, prev, pager, next"
                            :total="total">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import qs from 'qs'
    export default {
        name: "Track",
        data() {
            return {
                trackList: [],
                page: 1,
                pageSize: 3,//一页显示三天足迹
                total: 0,
                username: '',
                newTrackList: [],
                time: [],//中间值
                flag: 0,//选择删除
                selectDelTrackList: [],
            }
        },
        methods: {
            refactorTrack() {
                let vm = this;
                this.trackList.forEach(item => {
                    let newTrackObj = {
                        time: '',
                        trackList: []
                    };
                    let d = new Date(item.createTime);
                    let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
                    if (vm.time.indexOf(datetime) >= 0) {
                        return;
                    }

                    newTrackObj.time = datetime;
                    vm.time.push(datetime);
                    this.trackList.forEach(citem => {
                        let c = new Date(citem.createTime);
                        let time = c.getFullYear() + '-' + (c.getMonth() + 1) + '-' + c.getDate();
                        if (datetime === time) {
                            newTrackObj.trackList.push(citem);
                        }
                    });
                    vm.newTrackList.unshift(newTrackObj);
                })

                this.newTrackList.forEach(item => {
                    this.$set(item, "checked", false) //Vue 解决不能检测到对象属性的添加或删除
                    // item.checked = false; //如果为真实数据直接设置的对象改变值不会更新视图
                    if (item.trackList) {
                        item.trackList.forEach((citem) => {
                            this.$set(citem, "checked", false)
                            // citem.checked = false;
                        })
                    }
                });

            },
            loadTrack(page) {
                this.page = page;
                let vm = this;
                this.newTrackList=[];
                vm.time=[];
                vm.axios.get(vm.API.TRACH_FINDPAGE_URL, {
                    params: {
                        page: this.page,
                        pageSize: this.pageSize,
                        username: this.username
                    }
                }).then(res => {
                    vm.total = res.data.total;
                    vm.trackList = res.data.rows;
                    console.log(res.data);
                    vm.refactorTrack();
                })

            },
            deleteTrack(id) {
                let vm = this;
                vm.axios.get(vm.API.TRACH_DELETE_URL, {params: {id: id}}).then(res => {
                    if (res.data.success) {
                        vm.$message.success(res.data.message)
                        vm.loadTrack(1);
                    } else {
                        vm.$message.error(res.data.message)
                    }
                })
            },
            goto(id) {
                window.location.href = this.API.BASE_PORTAL_URL + "/index/detailShop/" + id;
            },
            selectCheck() {
                this.flag = !this.flag;
            },
            open() {
                this.$confirm('确定删除全部吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let vm = this;
                    vm.axios.post(vm.API.TRACK_DELETEALL_URL,qs.stringify({username:this.Storage.Session.get("username")})).then(res=>{
                        if (res.data.success) {
                            vm.$message.success(res.data.message)
                            vm.loadTrack(1);
                        } else {
                            vm.$message.error(res.data.message)
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            selectByTime(val) {
                val.trackList.forEach(item => {
                    item.checked = val.checked;
                    if (val.checked) {
                        //添加选中id
                        this.selectDelTrackList.push(item.id);
                    } else {
                        let index = this.selectDelTrackList.indexOf(item.id);
                        this.selectDelTrackList.splice(index, 1);
                    }
                })
            },
            handleCheck(item) {
                this.newTrackList.forEach(track => {
                    if (track.trackList) {
                        //判断是否商品全选
                        var bool = track.trackList.every(citem => citem.checked);
                        if (bool) {
                            //商品全选则勾选对应商家
                            track.checked = true;
                        } else {
                            track.checked = false;
                        }
                    }
                })
                if(item.checked){
                    //添加选中id
                    this.selectDelTrackList.push(item.id);
                }else{
                    let index = this.selectDelTrackList.indexOf(item.id);
                    this.selectDelTrackList.splice(index, 1);
                }
            },
            selectDel(){
                let vm = this;
                vm.axios.post(vm.API.TRACK_DELETESELECT_URL+"?username="+this.Storage.Session.get("username"),{idList:vm.selectDelTrackList})
                    .then(res=>{
                        if (res.data.success) {
                            vm.$message.success(res.data.message)
                            vm.loadTrack(1);
                        } else {
                            vm.$message.error(res.data.message)
                        }
                })
            }
        },
        created() {
            this.username = this.Storage.Session.get("username");
            this.loadTrack(1);
        },
        watch: {}
    }
</script>

<style scoped>

</style>