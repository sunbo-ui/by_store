package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.AdvertisingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/contentCategory")
@RestController
public class AdvertisingController {
    @Reference
    private AdvertisingService advertisingService;

    @RequestMapping("/findAll")
    public PageResult findAll(Integer page, Integer rows, @RequestBody ContentCategory category) {
        return advertisingService.findAll(page, rows, category);
    }

    @RequestMapping("/save")
    public Result save(@RequestBody ContentCategory category) {
        try {
            advertisingService.save(category);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            return new Result(false, "保存失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody ContentCategory category) {
        try {
            advertisingService.update(category);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody Map<String, List<ContentCategory>> map) {
        try {
            advertisingService.delete(map);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/findById")
    public ContentCategory findById(Long id) {
        return advertisingService.findById(id);
    }
}
