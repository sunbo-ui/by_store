package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.collect.Collect;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.CollectService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Reference
    private CollectService collectService;

    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer pageSize, String username) {
        return collectService.findPage(page, pageSize, username);
    }

    @RequestMapping("/deleteCollect")
    public Result deleteCollect(Long id) {
        try {
            collectService.deleteCollect(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
    @RequestMapping("/deleteAllCollect")
    public Result deleteAllCollect(String username) {
        try {
            collectService.deleteAllCollect(username);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
    @RequestMapping("/updateNotes")
    public Result updateNotes(@RequestBody Collect collect) {
        try {
            collectService.updateNotes(collect);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

    @RequestMapping("/findById")
    public Collect findById(Long id) {
        return collectService.findById(id);
    }
}
