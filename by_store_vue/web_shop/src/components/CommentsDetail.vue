<template>
    <div style="padding:20px">
        <el-card>
            <span style="color:#606266;font-size: 25px;">商品评论</span>
            <div style="margin-top: 10px;">
                <span style="color:#C0C4CC;font-size: 15px;">商品标题:</span>
                <span style="color:#C0C4CC;font-size: 15px;margin-left: 10px;">{{title}}</span>
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
                        :colors="colors"
                        show-text
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
            <div style="margin-top: 20px;">
                <span style="color:#606266;font-size: 15px;">商家留言:</span>
                <div style="margin-top: 10px;">
                    <el-input class="mb-3"
                              type="textarea"
                              :autosize="{ minRows: 2, maxRows: 4}"
                              placeholder="请输入内容"
                              v-model="comment.shopContent">
                    </el-input>
                </div>
            </div>
             <div style="float: right;margin: 30px">
                 <el-button type="success" @click="updateCommentShop">发布</el-button>
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
            goto(path){
                this.$router.push({path});
            },
            loadGoods(){
                let vm = this;
                vm.axios.post(vm.API.GOODS_FINDGOODSBYID_URL+"?id="+this.comment.goodsId).then(res=>{
                    vm.title = res.data.goodsName;
                })
            },
            updateCommentShop(){
                let vm = this;
                vm.axios.post(vm.API.GOODS_UPDATESHOPCOMTENT_URL+"?shopContent="+this.comment.shopContent+"&id="+vm.id).then(res=>{
                    if(res.data.success){
                        vm.$message.success(res.data.message);
                        vm.$router.push({path:'/index/comments'})
                    }else {
                        vm.$message.error(res.data.message)
                    }
                })
            }
        },
        created() {
            this.id = this.$route.params.id;
            console.log(this.$route.params.id);
            this.loadComment();

        }
    }
</script>

<style scoped>

</style>