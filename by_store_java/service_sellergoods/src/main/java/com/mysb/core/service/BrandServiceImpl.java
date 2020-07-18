package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.good.BrandDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Brand;
import com.mysb.core.pojo.good.BrandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    @Override
    public PageResult findPage(Integer page, Integer rows,Brand brand) {
        //第一个参数:当前页  第二个参数:每页数据条数
        PageHelper.startPage(page,rows);
        BrandQuery query = new BrandQuery();
        query.setOrderByClause("id desc");
        if(brand != null){
            BrandQuery.Criteria criteria = query.createCriteria();
            if(!"".equals(brand.getName()) && brand.getName() != null){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(!"".equals(brand.getFirstChar()) && brand.getFirstChar() != null){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }
        Page<Brand> brands =(Page<Brand>) brandDao.selectByExample(query);

        return new PageResult(brands.getTotal(),brands.getResult());
    }

    @Override
    public void saveBrand(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public void deleteSelectBrand(Map<String, List<Brand>> map) {
        List<Brand> idList = map.get("idList");
        if(idList!=null){
            List<Long> list = new ArrayList<>();
            for (Brand brand:idList) {
                list.add(brand.getId());
            }
            BrandQuery query = new BrandQuery();
            BrandQuery.Criteria criteria = query.createCriteria();
            criteria.andIdIn(list);
            brandDao.deleteByExample(query);
        }
    }

    @Override
    public Brand findById(Integer id) {
        Brand brand = brandDao.selectByPrimaryKey(Long.parseLong(String.valueOf(id)));
        return brand;
    }

    @Override
    public void updateBrand(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }


}