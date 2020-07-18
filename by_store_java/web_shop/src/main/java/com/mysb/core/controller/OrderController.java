package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/findOrderBySellerId")
    public PageResult findOrderBySellerId(Integer page, Integer pageSize, String sellerId){
        return orderService.findOrderBySellerId(page, pageSize, sellerId);
    }
    @RequestMapping("/findOrderById")
    public Order findOrderById(Long orderId){
        return orderService.findOrderById(orderId);
    }
    @RequestMapping("/findOrderItemByOrderId")
    public PageResult findOrderItemByOrderId(Integer page, Integer pageSize,Long orderId){
        return orderService.findOrderItemByOrderId(page,pageSize,orderId);
    }
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long orderId, String status){
        try {
            orderService.updateStatus(orderId,status);
            return new Result(true,"操作成功");
        }catch (Exception e){
            return  new Result(false,"操作失败");
        }
    }
}
