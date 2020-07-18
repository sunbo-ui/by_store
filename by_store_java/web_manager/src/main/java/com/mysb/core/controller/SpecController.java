package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.entry.SpecEntity;
import com.mysb.core.pojo.good.Brand;
import com.mysb.core.pojo.specification.Specification;
import com.mysb.core.service.BrandService;
import com.mysb.core.service.SpecService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spec")
public class SpecController {
    @Reference
    private SpecService specService;

    @RequestMapping("/findAll")
    public List<Specification> findAll(){
        return specService.findAll();
    }

    @RequestMapping("/findPage")
    public PageResult pageBrand(Integer page, Integer rows,@RequestBody Specification spec){
        return specService.findPage(page,rows,spec);
    }

    @RequestMapping("/saveSpec")
    public Result saveSpec(@RequestBody SpecEntity specEntity){
        try {
            specService.saveSpec(specEntity);
            return new Result(true,"保存成功");
        }catch (Exception e){
            return new Result(false,"保存失败");
        }
    }

    @RequestMapping("/deleteSpec")
    public Result deleteSpec(@RequestBody Map<String,List<Specification>> map){
        try {
            specService.deleteSpec(map);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/updateSpec")
    public Result updateSpec(@RequestBody SpecEntity specEntity){
        try {
            specService.updateSpec(specEntity);
            return new Result(true,"修改成功");
        }catch (Exception e){
            return new Result(false,"修改失败");
        }
    }

    @RequestMapping("/findById/{id}")
    public SpecEntity findById(@PathVariable Integer id){
        try {
            return specService.findById(id);
        }catch (Exception e){
            return null;
        }
    }

}
