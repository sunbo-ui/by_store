package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<Brand> findAll();//查询所有

    /**
     *
     * @param page 当前页
     * @param rows 一次查询多少条记录
     * @return 分页数据
     */
    PageResult findPage(Integer page,Integer rows,Brand brand);

    void saveBrand(Brand brand);//保存品牌

    void deleteSelectBrand(Map<String, List<Brand>> map);

    Brand findById(Integer id);

    void updateBrand(Brand brand);
}
