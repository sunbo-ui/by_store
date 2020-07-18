package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.service.CommentsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comments")
@RestController
public class CommentsController {
    @Reference
    private CommentsService commentsService;

    @RequestMapping("/findByGoodsId")
    public PageResult findByGoodsId(Integer page, Integer rows,Long goodsId){
        return commentsService.findByGoodsId(page,rows,goodsId);
    }
}
