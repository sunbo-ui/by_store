package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.ExpenseLog;
import com.mysb.core.service.PayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("/findByUsername")
    public List<ExpenseLog> findByUsername(String username){
        List<ExpenseLog> byUsername = payService.findByUsername(username);
        return byUsername;
    }
}