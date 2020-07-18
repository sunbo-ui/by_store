package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.userDiscount.UserDiscount;
import com.mysb.core.service.UserDiscountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userDiscount")
public class UserDiscountController {
    @Reference
    private UserDiscountService userDiscountService;


    @RequestMapping("/findByUsername")
    public List<UserDiscount> findByUsername(String username) {
        return userDiscountService.findByUsername(username);
    }

    @RequestMapping("/findUserDiscount")
    public PageResult findUserDiscount(String username, Integer page, Integer pageSize) {
        return userDiscountService.findUserDiscount(username,page,pageSize);
    }

    @RequestMapping("/deleteUserDiscount")
    public Result deleteUserDiscount(Integer id) {
        try {
            userDiscountService.deleteUserDiscount(id);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
}
