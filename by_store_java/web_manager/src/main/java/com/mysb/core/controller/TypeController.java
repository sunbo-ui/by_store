package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.entry.SpecEntity;
import com.mysb.core.pojo.specification.Specification;
import com.mysb.core.pojo.template.TypeTemplate;
import com.mysb.core.service.TypeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/type")
public class TypeController {
    @Reference
    private TypeService typeService;

    @RequestMapping("/findPage")
    public PageResult pageBrand(Integer page, Integer rows, @RequestBody TypeTemplate template){
        return typeService.findPage(page,rows,template);
    }

    @RequestMapping("/saveType")
    public Result saveType(@RequestBody TypeTemplate template){
        try {
            typeService.saveType(template);
            return new Result(true,"保存成功");
        }catch (Exception e){
            return new Result(false,"保存失败");
        }
    }
    @RequestMapping("/updateType")
    public Result updateType(@RequestBody TypeTemplate template){
        try {
            typeService.updateType(template);
            return new Result(true,"更新成功");
        }catch (Exception e){
            return new Result(false,"更新失败");
        }
    }
    @RequestMapping("/deleteType")
    public Result deleteType(@RequestBody Map<String,List<TypeTemplate>> map){
        try {
            typeService.deleteType(map);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/findById/{id}")
    public TypeTemplate findById(@PathVariable Integer id){
        try {
            return typeService.findById(id);
        }catch (Exception e){
            return null;
        }
    }
}
