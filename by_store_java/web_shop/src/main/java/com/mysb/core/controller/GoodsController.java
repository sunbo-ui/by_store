package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.GoodsEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.service.GoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;


    @RequestMapping("/add")
    public Result add(String username,@RequestBody GoodsEntity goodsEntity) {
        try {
            //获取当前登录用户的用户名
            goodsEntity.getGoods().setSellerId(username);
            goodsService.add(goodsEntity);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(String username,@RequestBody Goods goods, Integer page, Integer rows) {
        //获取当前登录用户的用户名
        goods.setSellerId(username);
        PageResult result = goodsService.findPage(goods, page, rows);
        return result;
    }

    @RequestMapping("/findBySellerId")
    public List<Goods> findBySellerId(String username) {
       return goodsService.findBySellerId(username);
    }

    @RequestMapping("/findById")
    public GoodsEntity findById(Long id) {
        return goodsService.findById(id);
    }

    @RequestMapping("/update")
    public Result update(String username,@RequestBody GoodsEntity goodsEntity) {
        try {
            //获取当前登录用户的用户名
            goodsEntity.getGoods().setSellerId(username);
            goodsService.update(goodsEntity);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody Map<String, List<Goods>> map) {
        try {
            goodsService.delete(map);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long id, @RequestBody Map<String, List<Goods>> map) {
        try {
            /**
             *    0:"未申请",
             *    1:"申请中",
             *    2:"审核通过",
             *    3:"已驳回"
             */
            goodsService.updateStatus(id, map);
            return new Result(true, "修改状态成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改状态失败");
        }
    }
}
