package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    private SellerService sellerService;

    @RequestMapping("/findById")
    public Seller findById(String sellerId) {
        System.out.println(sellerId);
        return sellerService.findById(sellerId);
    }

    @RequestMapping("/updateBySellerId")
    public Result updateBySellerId(String sellerId, @RequestBody Seller seller) {
        try {
            sellerService.updateBySellerId(sellerId,seller);
            return new Result(true,"更新成功");
        }catch (Exception e){
            return new Result(false,"更新失败");
        }
    }
}
