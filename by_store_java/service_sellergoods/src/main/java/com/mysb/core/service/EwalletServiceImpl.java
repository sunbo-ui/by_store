package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.mysb.core.dao.ewallet.EwalletDao;
import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.pojo.ewallet.EwalletQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class EwalletServiceImpl implements  EwalletService {
    @Autowired
    private EwalletDao ewalletDao;
    @Override
    public Ewallet findAmountByUsername(String username) {
        EwalletQuery query = new EwalletQuery();
        EwalletQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<Ewallet> ewallets = ewalletDao.selectByExample(query);
        if(ewallets.size() > 0){
            return  ewallets.get(0);
        }
        return null;
    }

    @Override
    public void save(String username) {
        Ewallet ewallet = new Ewallet();
        ewallet.setAmount(new BigDecimal("0"));
        ewallet.setUpdtime(new Date());
        ewallet.setUserId(username);
        ewalletDao.insertSelective(ewallet);
    }

    @Override
    public void insertAmount(String username, BigDecimal amount) {
        EwalletQuery ewalletQuery = new EwalletQuery();
        EwalletQuery.Criteria criteria = ewalletQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<Ewallet> ewallets = ewalletDao.selectByExample(ewalletQuery);
        Ewallet ewallet = ewallets.get(0);
        ewallet.setAmount(ewallet.getAmount().add(amount));
        ewalletDao.updateByPrimaryKeySelective(ewallet);
    }

}
