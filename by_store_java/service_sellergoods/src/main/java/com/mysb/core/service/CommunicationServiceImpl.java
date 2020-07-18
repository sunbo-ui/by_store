package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.mysb.core.dao.communication.CommunicationDao;
import com.mysb.core.pojo.communication.Communication;
import com.mysb.core.pojo.communication.CommunicationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationDao communicationDao;
    @Override
    public void save(Communication communication) {
        System.out.println(communication);
        communicationDao.insertSelective(communication);
    }

    @Override
    public List<Communication> selectBySendId(String param) {
        System.out.println(param);
        CommunicationQuery query = new CommunicationQuery();
        CommunicationQuery.Criteria criteria = query.createCriteria();
        criteria.andSendIdEqualTo(param);
        return communicationDao.selectByExample(query);
    }
}
