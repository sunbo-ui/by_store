package com.mysb.core.service;

import com.mysb.core.pojo.entry.OrderItemEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.log.PayLog;
import com.mysb.core.pojo.order.Order;

import java.util.List;

public interface OrderService {
    void saveOrder(String username, Order order,Integer discountId);

    PayLog getPayLogByUsername(String username);

    void updatePayStatus(String username);

    List<OrderItemEntity> findByUsername(String username);

    PageResult pageOrderItem(Integer page, Integer pageSize, List<OrderItemEntity> byUsername);

    void deleteOrderItemByOrderId(Long orderId, String username,String buyerMessage);

    PageResult findOrderBySellerId(Integer page, Integer pageSize, String sellerId);

    PageResult findOrderItemByOrderId(Integer page, Integer pageSize,Long orderId);

    Order findOrderById(Long orderId);

    void updateStatus(Long orderId, String status);
    /*退货*/
    void updateStatus(Long orderId, String status, String buyerMessage);

    void deleteOrderById(Long orderId);

    void gotoByOrderId(Long orderId,String username);

    PayLog queryStatus(String out_trade_no);
}
