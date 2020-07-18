package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.userDiscount.UserDiscount;
import com.mysb.core.service.UserDiscountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userDiscount")
public class UserDiscountController {
    @Reference
    private UserDiscountService userDiscountService;

    @RequestMapping("/saveUserDiscount")
    public Result saveUserDiscount(@RequestBody Discount discount, String username) {
        try {
            userDiscountService.saveUserDiscount(discount, username);
            return new Result(true, "领券成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "领券失败");
        }
    }

    @RequestMapping("/findDiscountId/{discountId}")
    public Result findDiscountId(@PathVariable Integer discountId) {
        Boolean flage = userDiscountService.findDiscountId(discountId);
        return new Result(flage, "");
    }

    @RequestMapping("/findByUsername")
    public List<UserDiscount> findByUsername(String username) {
        return userDiscountService.findByUsername(username);
    }

    @RequestMapping("/findBySellerAndUserName")
    public List<Discount> findBySellerAndUserName(String username,String sellerId) {
        return userDiscountService.findBySellerAndUserName(username,sellerId);
    }


}
