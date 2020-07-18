package com.mysb.core.pojo.entry;

import com.mysb.core.pojo.integralDesc.IntegralDesc;
import com.mysb.core.pojo.order.OrderItem;

import java.io.Serializable;

public class IntegralEntity implements Serializable {
    private OrderItem orderItem;
    private IntegralDesc integralDesc;
    public IntegralDesc getIntegralDesc() {
        return integralDesc;
    }

    public void setIntegralDesc(IntegralDesc integralDesc) {
        this.integralDesc = integralDesc;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
