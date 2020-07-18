package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/add")
    public Result add(String username,@RequestBody Order order,Integer discountId){
        try {
            orderService.saveOrder(username,order,discountId);
            return new Result(true,"添加订单成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加订单失败");
        }
    }
}
