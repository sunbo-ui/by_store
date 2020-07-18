package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.ad.ContentCategoryDao;
import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.ad.ContentCategoryQuery;
import com.mysb.core.pojo.entry.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdvertisingServiceImpl implements  AdvertisingService {

    @Autowired
    private ContentCategoryDao categoryDao;

    @Override
    public PageResult findAll(Integer page, Integer rows, ContentCategory category) {
        PageHelper.startPage(page,rows);
        ContentCategoryQuery query = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = query.createCriteria();
        if(category != null){
            if(category.getName() != "" && !"".equals(category.getName())&& category.getName() != null){
                criteria.andNameLike("%"+category.getName()+"%");
            }
        }
        Page<ContentCategory> list = (Page<ContentCategory>)categoryDao.selectByExample(query);
        return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    public void save(ContentCategory category) {
        categoryDao.insertSelective(category);
    }

    @Override
    public ContentCategory findById(Long id) {
        return categoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ContentCategory category) {
        categoryDao.updateByPrimaryKeySelective(category);
    }

    @Override
    public void delete(Map<String, List<ContentCategory>> map) {
        List<ContentCategory> idList = map.get("idList");
        if (idList != null) {
            for (ContentCategory goods : idList) {
                categoryDao.deleteByPrimaryKey(goods.getId());
            }
        }
    }
}
