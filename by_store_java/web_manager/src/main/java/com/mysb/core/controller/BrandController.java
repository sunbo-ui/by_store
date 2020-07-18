package com.mysb.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.pojo.good.Brand;
import com.mysb.core.service.BrandService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<Brand> findAll(){
        List<Brand> list = brandService.findAll();
        return list;
    }

    @RequestMapping("/findPage")
    public PageResult pageBrand(Integer page,Integer rows,@RequestBody Brand brand){
        PageResult res = brandService.findPage(page, rows,brand);
        return res;
    }

    @RequestMapping("/saveBrand")
    public Result saveBrand(@RequestBody Brand brand){
        try {
            brandService.saveBrand(brand);
            return new Result(true,"保存成功");
        }catch (Exception e){
            return new Result(false,"保存失败");
        }
    }

    @RequestMapping("/deleteBrand")
    public Result deleteBrand(@RequestBody Map<String,List<Brand>> map){
        try {
            brandService.deleteSelectBrand(map);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/updateBrand")
    public Result updateBrand(@RequestBody Brand brand){
        try {
            brandService.updateBrand(brand);
            return new Result(true,"修改成功");
        }catch (Exception e){
            return new Result(false,"修改失败");
        }
    }

    @RequestMapping("/findById/{id}")
    public Brand findById(@PathVariable Integer id){
        try {
            Brand brand = brandService.findById(id);
            return brand;
        }catch (Exception e){
            return null;
        }
    }

}
