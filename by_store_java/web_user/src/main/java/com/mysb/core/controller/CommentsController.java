package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.comment.Comment;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.service.CommentsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/comments")
@RestController
public class CommentsController {
    @Reference
    private CommentsService commentsService;

    @RequestMapping("/save")
    public Result saveComments(Long orderId, @RequestBody Comment comment) {
        try {
            commentsService.save(orderId, comment);
            return new Result(true, "发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发布失败");
        }
    }
    @RequestMapping("/deleteCommentById")
    public Result deleteCommentById(Long id) {
        try {
            commentsService.deleteCommentById(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace() ;
            return new Result(false, "删除失败");
        }
    }
    @RequestMapping("/findById")
    public Comment findById(Long id){
        return commentsService.findById(id);
    }

    @RequestMapping("/findByUserId")
    public PageResult findByUserId(Integer page, Integer pageSize, String userId) {
        return commentsService.findByUserId(page, pageSize, userId);
    }

    @RequestMapping("/findOrderItemByGoodsId")
    public List<OrderItem> findOrderItemByGoodsId(@RequestBody Map<String,List<Long>> reqMap) {
        List<Long> idList = reqMap.get("idList");
        System.out.println(idList);
        return commentsService.findOrderItemByGoodsId(reqMap);
    }

    @RequestMapping("/updateComment")
    public Result updateComment(@RequestBody Comment comment){
        try{
            commentsService.updateComment(comment);
            return new Result(true,"更新评论成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更新评论失败");
        }
    }

}
