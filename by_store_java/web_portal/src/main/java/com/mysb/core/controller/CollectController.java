package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.collect.Collect;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.CollectService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Reference
    private CollectService collectService;

    @RequestMapping("/saveCollect")
    public Result saveCollect(@RequestBody Collect collect){
        try {
           // System.out.println(collect);
            collectService.saveCollect(collect);
            return new Result(true,"已成功加入收藏");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"加入失败");
        }
    }
    @RequestMapping("/saveCollectBySelect")
    public Result saveCollect(String username , @RequestBody Map<String, List<Long> > reqMap){
        try {
            collectService.saveCollectBySelect(username,reqMap);
            return new Result(true,"已成功加入收藏");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"加入失败");
        }
    }
}
