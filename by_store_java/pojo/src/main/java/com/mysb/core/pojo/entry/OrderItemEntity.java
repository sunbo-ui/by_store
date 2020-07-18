package com.mysb.core.pojo.entry;

import com.mysb.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;

public class OrderItemEntity implements Serializable {
    private String orderId;
    private String sellerId;
    private String status;
    private List<OrderItem> orderItemList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId='" + orderId + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", status='" + status + '\'' +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
