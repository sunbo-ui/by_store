package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.service.EwalletService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
