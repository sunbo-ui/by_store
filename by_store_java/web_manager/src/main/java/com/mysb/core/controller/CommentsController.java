package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.comment.Comment;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.CommentsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comments")
@RestController
public class CommentsController {
    @Reference
    private CommentsService commentsService;

    @RequestMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows, @RequestBody Comment comment){
        return commentsService.findByPage(page,rows,comment);
    }

    @RequestMapping("/findById")
    public Comment findById(Long id){
        return commentsService.findById(id);
    }

    @RequestMapping("/findByGoodsId")
    public PageResult findByGoodsId(Integer page, Integer rows,Long goodsId){
        return commentsService.findByGoodsId(page,rows,goodsId);
    }
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long id,Integer status){
        try {
            commentsService.updateStatus(id,status);
            return new Result(true,"操作成功");
        }catch (Exception e){
            return  new Result(false,"操作失败");
        }
    }
}
