package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.item.ItemCatDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.item.ItemCat;
import com.mysb.core.pojo.item.ItemCatQuery;
import com.mysb.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findByParentId(Long id, Integer page, Integer rows) {
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        //分类名称作为key ,typeId作为模板
        for (ItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps(Constants.BY_CATEGORY_LIST_REDIS).
                    put(itemCat.getName(),itemCat.getTypeId());
        }
        PageHelper.startPage(page,rows);
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(id);
        Page<ItemCat> itemCats1 = (Page<ItemCat>)itemCatDao.selectByExample(query);
        return new PageResult(itemCats1.getTotal(),itemCats1.getResult());
    }

    @Override
    public List<ItemCat> findByParentId(Long id) {

        //根据父类id查询子集
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(id);
        return  itemCatDao.selectByExample(query);
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }
}
