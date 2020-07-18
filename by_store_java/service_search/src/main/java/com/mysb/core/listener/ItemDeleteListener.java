package com.mysb.core.listener;

import com.mysb.core.dao.item.ItemDao;
import com.mysb.core.pojo.item.Item;
import com.mysb.core.pojo.item.ItemQuery;
import com.mysb.core.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

public class ItemDeleteListener implements MessageListener {
    @Autowired
    private SolrManagerService solrManagerService;
    @Autowired
    private ItemDao itemDao;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId = atm.getText();
            ArrayList<Long> list = new ArrayList<>();
            list.add(Long.parseLong(goodsId));
            solrManagerService.deleteItemByGoodsId(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
