<template>
    <div style="padding:20px">
        <el-card>
            <span style="color:#606266;font-size: 25px;">商品评论  ---- {{status[comment.audit]}}</span>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">商品标题:</span>
                <span style="color:#C0C4CC;font-size: 15px;margin-left: 10px;">{{title}}</span>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">商家Id:</span>
                <span style="color:#C0C4CC;font-size: 15px;margin-left: 10px;">{{comment.sellerId}}</span>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">用户Id:</span>
                <span style="color:#C0C4CC;font-size: 15px;margin-left: 10px;">{{comment.userId}}</span>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">评论时间:</span>
                <span style="color:#C0C4CC;font-size: 15px;margin-left: 10px;">{{comment.createTime}}</span>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">评分:</span>
                <el-rate
                        style="display: inline-block;margin-left: 10px;"
                        v-model="comment.starts"
                        disabled
                        show-text
                        :colors="colors"
                        :texts="texts"
                        text-color="#ff9900"
                        score-template="{value}">
                </el-rate>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">评论内容:</span>
                <p style="color:#303133;font-size: 15px;margin-left: 10px;margin-top: 5px;">{{comment.content}}</p>
            </div>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">评论图片:</span>
                <div >
                    <img :src="item.url" v-for="(item,index) in imageList" style="margin-right: 30px;"  width="300" height="300" alt="">
                </div>
            </div>
             <div style="float: right;margin: 30px">
                 <el-button type="success" @click="updateComment(1)">通过</el-button>
                 <el-button type="primary" @click="updateComment(2)">屏蔽</el-button>
                 <el-button type="danger" @click="goto('/index/comments')">返回</el-button>
             </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "CommentsDetail",
        data(){
            return{
                id:'',
                comment:{},
                texts: ['极差', '差', '一般', '好', '非常好'],
                imageList:[],
                status:{
                    0:'未审核',
                    1:'审核通过',
                    2:'评论屏蔽',
                },
                title:'',
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],

            }
        },
        methods:{
            loadComment(){
               let vm = this;
               vm.axios.post(vm.API.COMMENTS_FINDBYID_URL+"?id="+this.id).then(res=>{
                   vm.comment = res.data;
                   vm.imageList = JSON.parse(res.data.picUrl);
                   vm.loadGoods()
               })
            },
            updateComment(status){
                let vm = this;
                vm.axios.post(vm.API.COMMENTS_UPDATESTATUS_URL+"?status="+status+"&id="+this.id).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        this.$router.push({path:'/index/comments'});
                    }else {
                        vm.$message.error(res.data.message);
                    }
                })
            },
            goto(path){
                this.$router.push({path});
            },
            loadGoods(){
                let vm = this;
                vm.axios.post(vm.API.GOODS_FINDGOODSBYID_URL+"?id="+this.comment.goodsId).then(res=>{
                    vm.title = res.data.goodsName;
                })
            }
        },
        created() {
            this.id = this.$route.params.id;
            this.loadComment();

        }
    }
</script>

<style scoped>

</style>