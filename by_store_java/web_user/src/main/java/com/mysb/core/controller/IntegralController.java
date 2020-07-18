package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.IntegralService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/integral")
public class IntegralController {
    @Reference
    private IntegralService integralService;

    @RequestMapping("/findByUsername")
    public Map<String,Object> findByUsername(String username){
        return integralService.findByUsername(username);
    }

    @RequestMapping("/exchange")
    public Result exchange(String username,String integral){
        try {
            integralService.exchange(username,integral);
           return new Result(true,"兑换成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"兑换失败");
        }
    }
}
