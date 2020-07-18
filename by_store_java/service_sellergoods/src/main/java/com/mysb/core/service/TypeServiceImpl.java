package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.good.BrandDao;
import com.mysb.core.dao.specification.SpecificationDao;
import com.mysb.core.dao.specification.SpecificationOptionDao;
import com.mysb.core.dao.template.TypeTemplateDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Brand;
import com.mysb.core.pojo.good.BrandQuery;
import com.mysb.core.pojo.specification.Specification;
import com.mysb.core.pojo.specification.SpecificationOption;
import com.mysb.core.pojo.specification.SpecificationOptionQuery;
import com.mysb.core.pojo.specification.SpecificationQuery;
import com.mysb.core.pojo.template.TypeTemplate;
import com.mysb.core.pojo.template.TypeTemplateQuery;
import com.mysb.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeTemplateDao templateDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao optionDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult findPage(Integer page, Integer rows, TypeTemplate template) {
        List<TypeTemplate> templates = templateDao.selectByExample(null);
        for (TypeTemplate typeTemplate : templates) {
            String brandIds = typeTemplate.getBrandIds();
            List<Map> brandList = JSON.parseArray(brandIds, Map.class);
            redisTemplate.boundHashOps(Constants.BY_BRAND_LIST_REDIS).put(typeTemplate.getId(),brandList);

            List<Map> specList = findBySpecList(typeTemplate.getId());
            redisTemplate.boundHashOps(Constants.BY_SPEC_LIST_REDIS).put(typeTemplate.getId(),specList);
        }
        //第一个参数:当前页  第二个参数:每页数据条数
        PageHelper.startPage(page, rows);
        TypeTemplateQuery query = new TypeTemplateQuery();
        query.setOrderByClause("id desc");
        if (template != null) {
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            if (template.getName() != null && !"".equals(template.getName())) {
                criteria.andNameLike("%" + template.getName() + "%");
            }
        }
        Page<TypeTemplate> temp = (Page<TypeTemplate>) templateDao.selectByExample(query);
        return new PageResult(temp.getTotal(), temp.getResult());

    }

    @Override
    public void saveType(TypeTemplate template) {
        templateDao.insertSelective(template);
    }

    @Override
    public TypeTemplate findById(Integer id) {
        return templateDao.selectByPrimaryKey(Long.parseLong(String.valueOf(id)));
    }

    @Override
    public void deleteType(Map<String, List<TypeTemplate>> map) {
        List<TypeTemplate> idList = map.get("idList");
        if(idList!=null){
            List<Long> list = new ArrayList<>();
            for (TypeTemplate type:idList) {
                list.add(type.getId());
            }
            TypeTemplateQuery query = new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            criteria.andIdIn(list);
            templateDao.deleteByExample(query);
        }
    }

    @Override
    public void updateType(TypeTemplate template) {
        templateDao.updateByPrimaryKeySelective(template);
    }

    @Override
    public List<Map> findBySpecList(Long id) {
        TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
        String specIds = typeTemplate.getSpecIds();
        //[{"id":42,"specName":"选择颜色"},{"id":43,"specName":"选择版本"},{"id":44,"specName":"套餐"}]
        List<Map> maps = JSON.parseArray(specIds,Map.class);
        if(maps != null){
            for (Map map : maps){
                Long specId = Long.parseLong(String.valueOf(map.get("id")));
                SpecificationOptionQuery SpecificationOptionQuery = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = SpecificationOptionQuery.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                List<SpecificationOption> optionList = optionDao.selectByExample(SpecificationOptionQuery);
                map.put("options",optionList);
            }
        }
        return maps;
    }

}



