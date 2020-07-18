package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.mysb.core.dao.ewallet.EwalletDao;
import com.mysb.core.dao.integral.IntegralDao;
import com.mysb.core.dao.integralDesc.IntegralDescDao;
import com.mysb.core.dao.order.OrderItemDao;
import com.mysb.core.pojo.entry.IntegralEntity;
import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.pojo.ewallet.EwalletQuery;
import com.mysb.core.pojo.integral.Integral;
import com.mysb.core.pojo.integral.IntegralQuery;
import com.mysb.core.pojo.integralDesc.IntegralDesc;
import com.mysb.core.pojo.integralDesc.IntegralDescQuery;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.pojo.order.OrderItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    private IntegralDao integralDao;
    @Autowired
    private IntegralDescDao integralDescDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private EwalletDao ewalletDao;
    @Override
    public Map<String, Object> findByUsername(String username) {
        HashMap<String, Object> map = new HashMap<>();
        //总积分
        IntegralQuery integralQuery = new IntegralQuery();
        IntegralQuery.Criteria criteria = integralQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<Integral> integrals = integralDao.selectByExample(integralQuery);
        if(integrals.size() > 0){
            map.put("integral",integrals.get(0).getIntegral());
        }
        //积分明细
        IntegralDescQuery query = new IntegralDescQuery();
        IntegralDescQuery.Criteria queryCriteria = query.createCriteria();
        queryCriteria.andUserIdEqualTo(username);
        List<IntegralDesc> integralDescs = integralDescDao.selectByExample(query);
        if(integralDescs.size() > 0){
            //积分明细展示
           List<IntegralEntity> list = new ArrayList<>();
            for (IntegralDesc integralDesc : integralDescs) {
                IntegralEntity entity = new IntegralEntity();
                //取出第一个订单详情显示
                for (IntegralDesc desc : integralDescs) {
                    OrderItemQuery orderItemQuery = new OrderItemQuery();
                    OrderItemQuery.Criteria orderItemQueryCriteria = orderItemQuery.createCriteria();
                    orderItemQueryCriteria.andOrderIdEqualTo(Long.valueOf(desc.getOrderId()));
                    List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
                    if(orderItems.size()>0){
                        entity.setOrderItem(orderItems.get(0));
                        break;
                    }
                }
                entity.setIntegralDesc(integralDesc);
                list.add(entity);
            }
            map.put("result",list);
        }
        return map;
    }

    @Override
    public void exchange(String username,String integral) throws Exception {
        //总积分
        IntegralQuery integralQuery = new IntegralQuery();
        IntegralQuery.Criteria criteria = integralQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<Integral> integrals = integralDao.selectByExample(integralQuery);
        if(integrals.size() > 0){
           if(Integer.valueOf(integrals.get(0).getIntegral()) < Integer.valueOf(integral)){
               throw  new Exception("积分不足");
           }else {
               Integral value = new Integral();
               value.setIntegral(String.valueOf(Integer.valueOf(integrals.get(0).getIntegral())-Integer.valueOf(integral)));
               integralDao.updateByExampleSelective(value,integralQuery);
           }
        }
        //积分明细
        IntegralDesc integralDesc = new IntegralDesc();
        integralDesc.setUserId(username);
        integralDesc.setNote("兑换钱包");
        integralDesc.setIntegralChange("-"+integral);
        integralDesc.setCreateTime(new Date());
        integralDescDao.insertSelective(integralDesc);
        //钱包增加
        int money = Integer.valueOf(integral)/50;
        EwalletQuery query = new EwalletQuery();
        EwalletQuery.Criteria queryCriteria = query.createCriteria();
        queryCriteria.andUserIdEqualTo(username);
        List<Ewallet> ewallets = ewalletDao.selectByExample(query);
        Ewallet newEwallet = ewallets.get(0);
        Ewallet ewallet = new Ewallet();
        ewallet.setAmount(newEwallet.getAmount().add(BigDecimal.valueOf(money)));
        ewalletDao.updateByExampleSelective(ewallet,query);
    }
}
