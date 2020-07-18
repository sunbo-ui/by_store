package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.OrderItemEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/findByUsername")
    public PageResult findByUsername(Integer page,Integer pageSize,String username) {
        List<OrderItemEntity> byUsername = orderService.findByUsername(username);
        return orderService.pageOrderItem(page,pageSize,byUsername);
    }

    @RequestMapping("/deleteOrderItemById")
    public Result deleteOrderItemById(Long orderId,String username,String buyerMessage){
        try{
            orderService.deleteOrderItemByOrderId(orderId,username,buyerMessage);
            return new Result(true,"订单已取消");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"订单删除失败");
        }
    }
    @RequestMapping("/gotoByOrderId")
    public Result gotoByOrderId(Long orderId,String username){
        try{
            orderService.gotoByOrderId(orderId,username);
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"操作失败,请联系管理员!");
        }
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long orderId, String status,String buyerMessage){
        try {
            System.out.println(status);
            orderService.updateStatus(orderId,status,buyerMessage);
            return new Result(true,"操作成功");
        }catch (Exception e){
            return  new Result(false,"操作失败");
        }
    }
    @RequestMapping("/deleteOrderById")
    public Result deleteOrderById(Long orderId){
        try {
            orderService.deleteOrderById(orderId);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return  new Result(false,"删除失败");
        }
    }
    @RequestMapping("/findOrderById")
    public Order findOrderById(Long orderId){
        return orderService.findOrderById(orderId);
    }
    @RequestMapping("/findOrderItemByOrderId")
    public PageResult findOrderItemByOrderId(Integer page, Integer pageSize,Long orderId){
        return orderService.findOrderItemByOrderId(page,pageSize,orderId);
    }
}
