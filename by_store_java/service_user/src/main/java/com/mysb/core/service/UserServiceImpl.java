package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.mysb.core.dao.customer.CustomerDao;
import com.mysb.core.dao.integral.IntegralDao;
import com.mysb.core.pojo.customer.Customer;
import com.mysb.core.pojo.customer.CustomerQuery;
import com.mysb.core.pojo.integral.Integral;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ActiveMQQueue smsDestination;
    @Autowired
    private IntegralDao integralDao;
    @Override
    public Customer findById(String username) {
        CustomerQuery customerQuery = new CustomerQuery();
        CustomerQuery.Criteria criteria = customerQuery.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<Customer> customers = customerDao.selectByExample(customerQuery);

        if(customers.size() <= 0){
            return null;
        }
        return customers.get(0);
    }

    @Override
    public void sendCode(final String phone) {
        //1. 生成一个随机6为数字, 作为验证码
        StringBuffer sb = new StringBuffer();
        for (int i =1; i < 7; i++) {
            int s  = new Random().nextInt(10);
            sb.append(s);
        }
        //2. 手机号作为key, 验证码作为value保存到redis中, 生存时间为10分钟
        redisTemplate.boundValueOps(phone).set(sb.toString(), 60 * 10, TimeUnit.SECONDS);
        final String smsCode = sb.toString();

        //3. 将手机号, 短信内容, 模板编号, 签名封装成map消息发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("mobile", phone);//手机号
                message.setString("template_code", "SMS_169111508");//模板编码
                message.setString("sign_name", "疯码");//签名
                Map map=new HashMap();
                map.put("code", smsCode);	//验证码
                message.setString("param", JSON.toJSONString(map));
                return (Message) message;
            }
        });
    }

    @Override
    public void register(Customer customer) throws Exception {
        String smscode = customer.getSmscode();
        String code = (String) redisTemplate.boundValueOps(customer.getPhone()).get();
        if(code != null && smscode != null && smscode.equals(code)){
            if(!"".equals(smscode) && !"".equals(code)){
                CustomerQuery customerQuery = new CustomerQuery();
                CustomerQuery.Criteria criteria = customerQuery.createCriteria();
                criteria.andUsernameEqualTo(customer.getUsername());
                List<Customer> customers = customerDao.selectByExample(customerQuery);
                if(customers.size() > 0){
                    //用户名已被注册
                    throw new Exception("用户名已被注册");
                }
                //保存到数据库
                customerDao.insertSelective(customer);
                //开通积分
                Integral integral = new Integral();
                integral.setUserId(customer.getUsername());
                integral.setIntegral("0");
                integralDao.insertSelective(integral);
            }
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        CustomerQuery customerQuery = new CustomerQuery();
        CustomerQuery.Criteria criteria = customerQuery.createCriteria();
        criteria.andUsernameEqualTo(customer.getUsername());
        customerDao.updateByExampleSelective(customer,customerQuery);
    }

    @Override
    public List<Customer> findFormRedisByKeys(Set keys) {
        ArrayList<String> list = new ArrayList<>();
        for (Object key : keys) {
            String val = String.valueOf(key);
            //发送者@接收者_All
            String[] split = val.split("@");
            list.add(String.valueOf(split[0]));
        }
        if("[]".equals(list) || list.isEmpty()){
            return null;
        }
        CustomerQuery customerQuery = new CustomerQuery();
        CustomerQuery.Criteria criteria = customerQuery.createCriteria();
        criteria.andUsernameIn(list);
        List<Customer> customers = customerDao.selectByExample(customerQuery);
        if(customers.size() <= 0){
            return null;
        }
        return customers;
    }
}
