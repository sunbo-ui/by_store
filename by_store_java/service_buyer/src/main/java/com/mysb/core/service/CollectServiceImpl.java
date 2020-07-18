package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.collect.CollectDao;
import com.mysb.core.dao.item.ItemDao;
import com.mysb.core.pojo.collect.Collect;
import com.mysb.core.pojo.collect.CollectQuery;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private ItemDao itemDao;
    @Override
    public void saveCollect(Collect collect) {
        collect.setCreateTime(new Date());
        CollectQuery collectQuery = new CollectQuery();
        CollectQuery.Criteria criteria = collectQuery.createCriteria();
        criteria.andGoodsIdEqualTo(collect.getGoodsId());
        criteria.andUserIdEqualTo(collect.getUserId());
        List<Collect> collects = collectDao.selectByExample(collectQuery);
        if(collects.size()>0){
            return;
        }
        collectDao.insertSelective(collect);
    }

    @Override
    public PageResult findPage(Integer page, Integer pageSize, String username) {
        PageHelper.startPage(page,pageSize);
        if(username != null){
            CollectQuery collectQuery = new CollectQuery();
            collectQuery.setOrderByClause("create_time desc");
            CollectQuery.Criteria criteria = collectQuery.createCriteria();
            criteria.andUserIdEqualTo(username);
            Page<Collect> collects = (Page<Collect>)collectDao.selectByExample(collectQuery);
            return new PageResult(collects.getTotal(),collects.getResult());
        }
        return null;
    }

    @Override
    public void deleteCollect(Long id) {
        collectDao.deleteByPrimaryKey(id);
    }

    @Override
    public Collect findById(Long id) {
        return collectDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateNotes(Collect collect) {
        collectDao.updateByPrimaryKeySelective(collect);
    }

    @Override
    public void saveCollectBySelect(String username, Map<String, List<Long>> reqMap) {
        List<Long> idList = reqMap.get("idList");
        for (Long id : idList) {
            Item item = itemDao.selectByPrimaryKey(id);
            CollectQuery collectQuery = new CollectQuery();
            CollectQuery.Criteria criteria = collectQuery.createCriteria();
            criteria.andGoodsIdEqualTo(item.getGoodsId());
            List<Collect> collects = collectDao.selectByExample(collectQuery);
            if(collects.size()>0){
                return;
            }
            Collect collect = new Collect();
            collect.setCreateTime(new Date());
            collect.setGoodsId(item.getGoodsId());
            collect.setPicUrl(item.getImage());
            collect.setTitle(item.getTitle());
            collect.setPrice(item.getPrice());
            collect.setUserId(username);
            collectDao.insertSelective(collect);
        }
    }

    @Override
    public void deleteAllCollect(String username) {
        CollectQuery collectQuery = new CollectQuery();
        CollectQuery.Criteria criteria = collectQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        collectDao.deleteByExample(collectQuery);
    }
}
