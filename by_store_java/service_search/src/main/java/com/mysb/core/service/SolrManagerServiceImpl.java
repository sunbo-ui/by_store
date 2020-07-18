package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SolrManagerServiceImpl implements SolrManagerService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void saveItemToSolr(List list) {
        if (list != null) {
            solrTemplate.saveBeans(list);
            solrTemplate.commit();
        }
    }

    @Override
    public void deleteItemByGoodsId(List goodsIds) {
        if (goodsIds != null) {
            SimpleQuery simpleQuery = new SimpleQuery();
            Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
            simpleQuery.addCriteria(criteria);
            try {
                solrTemplate.delete(simpleQuery);
                solrTemplate.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
