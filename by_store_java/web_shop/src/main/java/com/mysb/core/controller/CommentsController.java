package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.comment.Comment;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.service.CommentsService;
import com.mysb.core.service.GoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comments")
@RestController
public class CommentsController {
    @Reference
    private CommentsService commentsService;
    @Reference
    private GoodsService goodsService;

    @RequestMapping("/findByPageBySellerId")
    public PageResult findByPageBySellerId(Integer page, Integer rows,String sellerId, @RequestBody Comment comment){
        return commentsService.findByPageBySellerId(page,rows,sellerId,comment);
    }

    @RequestMapping("/findById")
    public Comment findById(Long id){
        return commentsService.findById(id);
    }

    @RequestMapping("/findByGoodsId")
    public PageResult findByGoodsId(Integer page, Integer rows,Long goodsId){
        return commentsService.findByGoodsId(page,rows,goodsId);
    }
    @RequestMapping("/updateShopContent")
    public Result updateShopContent(String shopContent,Long id){
         try{
             commentsService.updateShopContent(shopContent,id);
             return new Result(true,"发布成功");
         }catch (Exception e){
             e.printStackTrace();
             return new Result(false,"发布失败");
         }
    }
    @RequestMapping("/findGoodsById")
    public Goods findGoodsById(Long id) {
        return goodsService.findGoodsById(id);
    }
}
