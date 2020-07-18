package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.DiscountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Reference
    private DiscountService discountService;

    @RequestMapping("/findBySeller")
    public PageResult findBySeller(Integer page,Integer rows,String username){
        return discountService.findBySeller(page,rows,username);
    }

    @RequestMapping("/findById")
    public Discount findById(Integer id){
        return discountService.findById(id);
    }

    @RequestMapping("/saveDiscount")
    public Result saveDiscount(@RequestBody Discount discount){
        try {
            System.out.println(discount);
            discountService.saveDiscount(discount);
            return new Result(true,"优惠券设置成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"优惠券设置失败");
        }
    }

    @RequestMapping("/deleteDiscount/{id}")
    public Result deleteDiscount(@PathVariable Integer id){
        try {
            discountService.deleteDiscount(id);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/deleteDiscountBySelection")
    public Result deleteDiscountBySelection(@RequestBody Map<String, List<Discount>> map){
        try {
            discountService.deleteDiscountBySelection(map);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/updateDiscount")
    public Result updateDiscount(@RequestBody Discount discount){
       try {
           discountService.updateDiscount(discount);
           return new Result(true,"更新成功");
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false,"更新失败");
       }
    }
}
