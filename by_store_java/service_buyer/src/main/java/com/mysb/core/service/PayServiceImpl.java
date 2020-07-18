package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.mysb.core.dao.log.PayLogDao;
import com.mysb.core.dao.order.OrderDao;
import com.mysb.core.dao.order.OrderItemDao;
import com.mysb.core.pojo.entry.ExpenseLog;
import com.mysb.core.pojo.log.PayLog;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.pojo.order.OrderItemQuery;
import com.mysb.core.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PayServiceImpl implements PayService {
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;

    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Override
    public Map createNative(String outTradeNo, String totalFee) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",appid);//   公众号
        map.put("mch_id",partner);//商户号
        map.put("body","myxq");//商品描述
        map.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符串
        map.put("out_trade_no",outTradeNo);//商户订单号
        map.put("total_fee",totalFee);//总金额
        map.put("spbill_create_ip","127.0.0.1");//IP
        map.put("notify_url","http://www.baidu.com");//回调地址
        map.put("trade_type","NATIVE");//交易类型
        try{
            //生成要发送的xml,调用微信sdkd的api接口将封装的map数据自动转换成xml格式字符串
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println(xmlParam);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            //获取结果
            String result = client.getContent();
            System.out.println(result);
            //调用微信sdk的api接口将xml格式字符串自动转换成Java对象
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("code_url",resultMap.get("code_url"));
            hashMap.put("total_fee",totalFee);//总金额
            hashMap.put("out_trade_no",outTradeNo);//订单号
            return hashMap;
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        Map map = new HashMap();
        map.put("appid",appid);
        map.put("mch_id",partner);
        map.put("out_trade_no",out_trade_no);
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            Map<String, String> xml = WXPayUtil.xmlToMap(result);
            System.out.println(xml);
            return xml;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ExpenseLog> findByUsername(String username) {
        /*获取最近三个payLog*/
        List<PayLog> payLogs = payLogDao.selectByUsernameAndLimit(username);
        System.out.println(payLogs);
        List<ExpenseLog> list = new ArrayList<>();
        for (PayLog payLog : payLogs) {
            String orderList = payLog.getOrderList();
            /*根据,来分开order*/
            String[] split = orderList.split(",");
            System.out.println(split);
            for (String orderId : split) {
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
                criteria.andOrderIdEqualTo(Long.parseLong(orderId));
                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
                if(orderItems.size() > 0){
                    OrderItem orderItem = orderItems.get(0);
                    ExpenseLog expenseLog = new ExpenseLog();
                    expenseLog.setOrderId(orderItem.getOrderId());
                    expenseLog.setPic(orderItem.getPicPath());
                    expenseLog.setTitle(orderItem.getTitle());
                    Order order = orderDao.selectByPrimaryKey(Long.parseLong(orderId));
                    expenseLog.setStatus(order.getStatus());
                    expenseLog.setCreateTime(order.getCreateTime());
                    expenseLog.setPayType(payLog.getPayType());
                    list.add(expenseLog);
                }
            }
        }
        return list;
    }
}