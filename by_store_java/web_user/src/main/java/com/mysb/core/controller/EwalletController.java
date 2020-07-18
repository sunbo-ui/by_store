package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.service.EwalletService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/ewallet")
public class EwalletController {
    @Reference
    private EwalletService ewalletService;

    @RequestMapping("/findAmountByUsername")
    public Ewallet findAmountByUsername(String username){
        Ewallet amountByUsername = ewalletService.findAmountByUsername(username);
        if( amountByUsername != null){
            return amountByUsername ;
        }
        return null;
    }

    @RequestMapping("/save")
    public Result save(String username){
        try {
            ewalletService.save(username);
            return new Result(true, "开通成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "开通失败");
        }
    }
    @RequestMapping("/insertAmount")
    public Result insertAmount(String username, BigDecimal amount){
        try {
            ewalletService.insertAmount(username,amount);
            return new Result(true, "充值成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "充值失败");
        }
    }
}
