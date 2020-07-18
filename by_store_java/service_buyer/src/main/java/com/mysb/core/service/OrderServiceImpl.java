package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.ewallet.EwalletDao;
import com.mysb.core.dao.integral.IntegralDao;
import com.mysb.core.dao.integralDesc.IntegralDescDao;
import com.mysb.core.dao.log.PayLogDao;
import com.mysb.core.dao.order.OrderDao;
import com.mysb.core.dao.order.OrderItemDao;
import com.mysb.core.dao.seller.SellerDao;
import com.mysb.core.dao.userDiscount.UserDiscountDao;
import com.mysb.core.pojo.entry.BuyerCart;
import com.mysb.core.pojo.entry.OrderItemEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.pojo.ewallet.EwalletQuery;
import com.mysb.core.pojo.integral.Integral;
import com.mysb.core.pojo.integral.IntegralQuery;
import com.mysb.core.pojo.integralDesc.IntegralDesc;
import com.mysb.core.pojo.log.PayLog;
import com.mysb.core.pojo.log.PayLogQuery;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.pojo.order.OrderItemQuery;
import com.mysb.core.pojo.order.OrderQuery;
import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.pojo.userDiscount.UserDiscountQuery;
import com.mysb.core.utils.Constants;
import com.mysb.core.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private EwalletDao ewalletDao;
    @Autowired
    private UserDiscountDao userDiscountDao;
    @Autowired
    private IntegralDescDao integralDescDao;
    @Autowired
    private IntegralDao integralDao;
    @Override
    public void saveOrder(String username, Order order,Integer discountId) {
        order.setUserId(username);
        //根据用户名获取购物车
        List<BuyerCart> cartList =
                (List<BuyerCart>) redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).get(username);
        ArrayList<String> orderList = new ArrayList<>();//订单id列表
        double total_money = 0;//总金额
        //遍历购物车集合
        if (cartList != null) {
            IdWorker idWorker = new IdWorker();
            for (BuyerCart buyerCart : cartList) {
                //根据购物车数据保存订单数据
                long orderId = idWorker.nextId();
                Order tbOrder = new Order();
                tbOrder.setOrderId(orderId);
                tbOrder.setUserId(username);
                tbOrder.setPaymentType(order.getPaymentType());//支付类型
                /**
                 * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
                 */
                tbOrder.setStatus("1");
                tbOrder.setCreateTime(new Date());
                tbOrder.setUpdateTime(new Date());
                tbOrder.setReceiver(order.getReceiver());//收货人
                tbOrder.setReceiverAreaName(order.getReceiverAreaName());//地址
                tbOrder.setReceiverMobile(order.getReceiverMobile());//手机
                /**
                 * 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
                 */
                tbOrder.setSourceType(order.getSourceType());
                tbOrder.setSellerId(buyerCart.getSellerId());
                //循环购物车明细
                double money = 0;
                if (buyerCart.getOrderItemList() != null) {
                    for (OrderItem orderItem : buyerCart.getOrderItemList()) {
                        //保存订单详情
                        orderItem.setId(idWorker.nextId());
                        orderItem.setOrderId(orderId);//订单id
                        orderItem.setSellerId(buyerCart.getSellerId());
                        money += orderItem.getTotalFee().doubleValue();//金额累加
                        orderItemDao.insertSelective(orderItem);
                        //插入积分明细
                        IntegralDesc integralDesc = new IntegralDesc();
                        integralDesc.setIntegralChange(String.valueOf(order.getPayment()));
                        integralDesc.setOrderId(String.valueOf(tbOrder.getOrderId()));
                        integralDesc.setUserId(tbOrder.getUserId());
                        integralDesc.setNote("商城赠送");
                        integralDesc.setCreateTime(new Date());
                        integralDescDao.insertSelective(integralDesc);
                    }
                }
                tbOrder.setPayment(new BigDecimal(money));
                orderDao.insertSelective(tbOrder);
                orderList.add(orderId + "");//添加到订单列表
                total_money += money;//累加到总金额
            }

            PayLog payLog = new PayLog();
            String outTradeNo = idWorker.nextId() + "";//支付订单号
            payLog.setOutTradeNo(outTradeNo);
            payLog.setCreateTime(new Date());
            //订单列表,逗号分离
            String ids = orderList.toString().replace("[", "")
                    .replace("]", "").replace(" ", "");
            payLog.setOrderList(ids);
            payLog.setPayType(order.getPaymentType());//支付类型
            payLog.setTotalFee((long) (Integer.parseInt(String.valueOf(order.getPayment())) * 100));//总金额(分)
            payLog.setUserId(order.getUserId());
            payLog.setCreateTime(new Date());
            if ("3".equals(order.getPaymentType())) {
                payLog.setTradeState("1");//支付类型 已付款
                Order order2 = new Order();
                order2.setStatus("2");//支付类型 已付款

                OrderQuery orderQuery = new OrderQuery();
                OrderQuery.Criteria criteria1 = orderQuery.createCriteria();
                criteria1.andUserIdEqualTo(username);

                orderDao.updateByExampleSelective(order2, orderQuery);

                //扣除钱包中的钱
                Ewallet ewallet = new Ewallet();
                ewallet.setUpdtime(new Date());

                EwalletQuery ewalletQuery = new EwalletQuery();
                EwalletQuery.Criteria criteria = ewalletQuery.createCriteria();
                criteria.andUserIdEqualTo(username);
                List<Ewallet> ewallets = ewalletDao.selectByExample(ewalletQuery);
                if (ewallets.size() > 0) {
                    BigDecimal amount = ewallets.get(0).getAmount();
                    amount = amount.subtract(order.getPayment());
                    ewallet.setAmount(amount);
                    ewallet.setId(ewallets.get(0).getId());
                    //更新
                    ewalletDao.updateByPrimaryKeySelective(ewallet);
                }
            } else {
                payLog.setTradeState("0");//支付类型 未付款
                redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//用户名做key
            }
            payLogDao.insertSelective(payLog);
        }
        //删除redis购物车
        redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).delete(order.getUserId());
        //删除优惠券
       if(discountId != -1 || !"-1".equals(discountId)){
           UserDiscountQuery query = new UserDiscountQuery();
           UserDiscountQuery.Criteria criteria = query.createCriteria();
           criteria.andDiscountIdEqualTo(discountId);
           userDiscountDao.deleteByExample(query);
       }
       //获取相应积分  10元  1积分
        Integer payment = Integer.valueOf(String.valueOf(order.getPayment()));
        if(payment > 0){
            int integral = payment/50;
            IntegralQuery integralQuery = new IntegralQuery();
            IntegralQuery.Criteria criteria = integralQuery.createCriteria();
            criteria.andUserIdEqualTo(order.getUserId());
            List<Integral> integrals = integralDao.selectByExample(integralQuery);
            Integral value = new Integral();
            value.setIntegral(String.valueOf(Integer.valueOf(integrals.get(0).getIntegral()) + integral));
            integralDao.updateByExampleSelective(value,integralQuery);
        }
    }

    @Override
    public PayLog getPayLogByUsername(String username) {
        return (PayLog) redisTemplate.boundHashOps("payLog").get(username);
    }

    @Override
    public void updatePayStatus(String username) {
        PayLog payLog = new PayLog();
        payLog.setTradeState("1");//支付类型 已付款

        PayLogQuery payLogQuery = new PayLogQuery();
        PayLogQuery.Criteria criteria = payLogQuery.createCriteria();
        criteria.andUserIdEqualTo(username);

        payLogDao.updateByExampleSelective(payLog, payLogQuery);

        Order order = new Order();
        order.setStatus("2");

        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria1 = orderQuery.createCriteria();
        criteria1.andUserIdEqualTo(username);

        orderDao.updateByExampleSelective(order, orderQuery);
        //10. 根据当前登录用户的用户名删除购物车
        redisTemplate.boundHashOps("payLog").delete(username);
    }

    @Override
    public List<OrderItemEntity> findByUsername(String username) {
        OrderQuery query = new OrderQuery();
        query.setOrderByClause("create_time desc");
        OrderQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<Order> orders = orderDao.selectByExample(query);
        List<OrderItemEntity> orderEntities = new ArrayList<>();
        for (Order order : orders) {
            Long orderId = order.getOrderId();
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            OrderItemQuery.Criteria orderCriteria = orderItemQuery.createCriteria();
            orderCriteria.andOrderIdEqualTo(orderId);
            List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrderId(String.valueOf(orderId));
            orderItemEntity.setSellerId(orderItems.get(0).getSellerId());
            orderItemEntity.setOrderItemList(orderItems);
            orderItemEntity.setStatus(order.getStatus());
            orderEntities.add(orderItemEntity);
        }
        return orderEntities;
    }

    @Override
    public PageResult pageOrderItem(Integer page, Integer pageSize, List<OrderItemEntity> orderItemEntityList) {
        int total = orderItemEntityList.size();
        int result = pageSize * page;
        int start = (page - 1) * pageSize;
        List<OrderItemEntity> list = new ArrayList<>();
        if (orderItemEntityList != null) {
            if (result >= total) {
                list = orderItemEntityList.subList(start, total);
            } else {
                list = orderItemEntityList.subList(start, result);
            }
        }
        return new PageResult(Long.parseLong(String.valueOf(total)), list);
    }

    @Override
    public void deleteOrderItemByOrderId(Long orderId, String username, String buyerMessage) {
        //删除redis
        redisTemplate.boundHashOps("payLog").delete(username);
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("0");
        order.setBuyerMessage(buyerMessage);
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageResult findOrderBySellerId(Integer page, Integer pageSize, String sellerId) {
        PageHelper.startPage(page, pageSize);
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerId);
        Page<Order> orderPage = (Page<Order>) orderDao.selectByExample(orderQuery);
        return new PageResult(orderPage.getTotal(), orderPage.getResult());
    }

    @Override
    public PageResult findOrderItemByOrderId(Integer page, Integer pageSize, Long orderId) {
        if (orderId != null) {
            PageHelper.startPage(page, pageSize);
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            OrderItemQuery.Criteria orderCriteria = orderItemQuery.createCriteria();
            orderCriteria.andOrderIdEqualTo(orderId);
            Page<OrderItem> orderItems = (Page<OrderItem>) orderItemDao.selectByExample(orderItemQuery);
            return new PageResult(orderItems.getTotal(), orderItems.getResult());
        }
        return null;
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderDao.selectByPrimaryKey(orderId);
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(status);
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public void updateStatus(Long orderId, String status, String buyerMessage) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(status);
        order.setBuyerMessage(buyerMessage);
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        //删除order  payLog不删
        orderDao.deleteByPrimaryKey(orderId);
        //删除orderItem
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
        for (OrderItem orderItem : orderItems) {
            orderItemDao.deleteByPrimaryKey(orderItem.getId());
        }
    }

    @Override
    public void gotoByOrderId(Long orderId, String username) {
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
        ArrayList<BuyerCart> cartList = new ArrayList<>();
        BuyerCart buyerCart = new BuyerCart();
        buyerCart.setSellerId(orderItems.get(0).getSellerId());
        Seller seller = sellerDao.selectByPrimaryKey(orderItems.get(0).getSellerId());
        buyerCart.setSellerName(seller.getName());
        buyerCart.setOrderItemList(orderItems);
        //将新建的购物车对象添加到购物车列表
        cartList.add(buyerCart);
        redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).put(username, cartList);
    }

    @Override
    public PayLog queryStatus(String out_trade_no) {
        return payLogDao.selectByPrimaryKey(out_trade_no);
    }

}
