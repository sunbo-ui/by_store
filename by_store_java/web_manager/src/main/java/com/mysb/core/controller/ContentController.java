package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.ad.Content;
import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 广告内容
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    @RequestMapping("/findAll")
    public PageResult findAll(@RequestBody Content content, Integer page, Integer rows) {
        PageResult result = contentService.findPage(content, page, rows);
        return result;
    }

    @RequestMapping("/category")
    public List<ContentCategory> category() {
        return contentService.findCategoryAll();
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody Map<String, List<Content>> map) {
        try {
            contentService.delete(map);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Content content) {
        try {
            contentService.update(content);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("/save")
    public Result save(@RequestBody Content content) {
        try {
            contentService.save(content);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    @RequestMapping("/findById")
    public Content save(Long id) {
        return contentService.findById(id);
    }
}
