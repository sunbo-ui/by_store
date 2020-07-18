package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.service.DiscountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Reference
    private DiscountService discountService;

    @RequestMapping("/findBySellerIdShow")
    public List<Discount> findBySellerIdShow(String sellerId){
        return discountService.findBySellerIdShow(sellerId);
    }

}
