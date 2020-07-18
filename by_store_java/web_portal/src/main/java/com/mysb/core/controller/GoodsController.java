package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.GoodsEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @RequestMapping("/findById")
    public GoodsEntity findById(Long id) {
        return goodsService.findById(id);
    }

}
