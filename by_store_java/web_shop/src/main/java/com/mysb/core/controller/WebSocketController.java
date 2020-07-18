package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.customer.Customer;
import com.mysb.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/socket")
public class WebSocketController{

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private UserService userService;

    @RequestMapping("/findRedisKeys")
    public  List<Customer> findRedisKeys(){
        Set keys = redisTemplate.boundHashOps("customer_seller").keys();
        if(keys == null || "".equals(keys) || "[]".equals(keys)||"null".equals(keys)){
            return null;
        }
        return userService.findFormRedisByKeys(keys);
    }
}