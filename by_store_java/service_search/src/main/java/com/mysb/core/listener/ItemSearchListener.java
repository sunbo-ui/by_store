package com.mysb.core.listener;

import com.alibaba.fastjson.JSON;
import com.mysb.core.dao.item.ItemDao;
import com.mysb.core.pojo.item.Item;
import com.mysb.core.pojo.item.ItemQuery;
import com.mysb.core.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;
import java.util.Map;

public class ItemSearchListener implements MessageListener {
    @Autowired
    private SolrManagerService solrManagerService;
    @Autowired
    private ItemDao itemDao;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId = atm.getText();
            ItemQuery itemQuery = new ItemQuery();
            ItemQuery.Criteria queryCriteria = itemQuery.createCriteria();
            queryCriteria.andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<Item> items = itemDao.selectByExample(itemQuery);
            if(items != null){
                for (Item item : items) {
                    String spec = item.getSpec();
                    Map map = JSON.parseObject(spec, Map.class);
                    item.setSpecMap(map);
                }
            }
            System.out.println("来了solr");
            solrManagerService.saveItemToSolr(items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
