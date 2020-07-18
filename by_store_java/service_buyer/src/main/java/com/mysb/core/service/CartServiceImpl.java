package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.mysb.core.dao.item.ItemDao;
import com.mysb.core.pojo.entry.BuyerCart;
import com.mysb.core.pojo.item.Item;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<BuyerCart> addItemToCartList(List<BuyerCart> cartList, Long itemId, Integer num) {
        //根据SKU id查询商品信息
        Item item = itemDao.selectByPrimaryKey(itemId);
        //判断商品是否存在
        if (item == null) {
            throw new RuntimeException("此商品不存在");
        }
        //判断商品状态是否为2已审核,状态不对抛异常
        if (!"2".equals(item.getStatus())) {
            throw new RuntimeException("此商品审核未通过");
        }
        //获取商家id
        String sellerId = item.getSellerId();
        //根据商家id查询购物车列表中是否存在该商家的商品
        BuyerCart buyerCart = this.findBuyerCartBySellerId(cartList, sellerId);
        if (buyerCart == null) {
            System.out.println("bbbbb");
            //新建购物车
            buyerCart = new BuyerCart();
            buyerCart.setSellerId(sellerId);
            buyerCart.setSellerName(item.getSeller());
            //创建购物项集合
            ArrayList<OrderItem> items = new ArrayList<>();
            //创建购物项
            OrderItem orderItem = this.createOrderItem(item, num);
            items.add(orderItem);
            buyerCart.setOrderItemList(items);
            //将新建的购物车对象添加到购物车列表
            cartList.add(buyerCart);
        } else {
            System.out.println("aaaaaaaa");
            //如果购物车列表存在该商家的购物车(查询购物车明细中是否存在该商品)
            List<OrderItem> orderItemList = buyerCart.getOrderItemList();
            OrderItem orderItem = this.findOrderItemByItemId(orderItemList, itemId);
            //判断购物车明细是否为空
            if (orderItem == null) {
                orderItem = this.createOrderItem(item, num);
                //将新增的购物项加入到购物项集合中
                orderItemList.add(orderItem);
            } else {
                orderItem.setNum(orderItem.getNum() + num);
                //设置总价格
                orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(orderItem.getNum())));
                //如果购物车明细中数量操作后小于等于0则移除
                if (orderItem.getNum() <= 0) {
                    orderItemList.remove(orderItem);
                }

                //如果购物车中购物车明细列表为空,则移除
                if (orderItemList.size() <= 0) {
                    cartList.remove(buyerCart);
                }
            }
        }
        //返回购物车列表对象
        return cartList;
    }

    @Override
    public void setCartListToRedis(String username, List<BuyerCart> cartList) {
        redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).put(username, cartList);
    }

    @Override
    public List<BuyerCart> getCartListFromRedis(String username) {
        List<BuyerCart> cartList = (List<BuyerCart>) redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).get(username);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public List<BuyerCart> mergeCookieCartListToRedisCartList(List<BuyerCart> cookieCartList, List<BuyerCart> redisCartList) {
        if (cookieCartList != null) {
            //遍历cookie购物车集合
            for (BuyerCart buyerCart : cookieCartList) {
                //遍历cookie购物车中的购物项集合
                for (OrderItem orderItem : buyerCart.getOrderItemList()) {
                    //将cookie中的购物项,加入到redise额购物车集合中
                    redisCartList = addItemToCartList(redisCartList, orderItem.getItemId(), orderItem.getNum());
                }
            }
        }
        return redisCartList;
    }

    @Override
    public void deleteOneCart(Long itemId, String username) {
        List<BuyerCart> cartList = this.getCartListFromRedis(username);
        for (int i = 0; i < cartList.size(); i++) {
            List<OrderItem> orderItemList = cartList.get(i).getOrderItemList();
            for (int j = 0; j < orderItemList.size(); j++) {
                OrderItem orderItem = orderItemList.get(j);
                if (orderItem.getItemId().equals(itemId)) {
                    orderItemList.remove(j);
                    if (orderItemList.size() == 0) {
                        cartList.remove(i);
                    }
                }
            }
        }
        this.setCartListToRedis(username, cartList);
    }

    @Override
    public List<BuyerCart> updateByNumCartList(Integer num, Long itemId, List<BuyerCart> cartList) {
        Item item = itemDao.selectByPrimaryKey(itemId);
        String status = item.getStatus();
        System.out.println(status);
        if (!"2".equals(status)) {
            return null;
        }
        for (int i = 0; i < cartList.size(); i++) {
            for (int j = 0; j < cartList.get(i).getOrderItemList().size(); j++) {
                OrderItem orderItem = cartList.get(i).getOrderItemList().get(j);
                if (itemId.equals(orderItem.getItemId())) {
                    orderItem.setNum(num);
                }
            }
        }
        return cartList;
    }

    @Override
    public void deleteCart(Map<String, List<Long>> reqMap, String username) {
        List<Long> list = reqMap.get("idList");//选中的库存
        List<BuyerCart> cartList = this.getCartListFromRedis(username);//购物车
        //删除redis当中的购物车
        redisTemplate.boundHashOps(Constants.BY_CART_LIST_REDIS).delete(username);
        for (int i = 0; i < cartList.size(); i++) {
            //获取对应的购物详情
            List<OrderItem> orderItemList = cartList.get(i).getOrderItemList();
            ArrayList<OrderItem> deleteItems = new ArrayList<>();
            for (int j = 0; j < orderItemList.size(); j++) {
                //一个购物详情
                OrderItem orderItem = orderItemList.get(j);
                for (int k = 0; k < list.size(); k++) {
                    //与list集合比较 相同删除
                    if (orderItem.getItemId().equals(list.get(k))) {
                        deleteItems.add(orderItem);
                        break;
                    }
                }
            }
            orderItemList.removeAll(deleteItems);
            //删除购物商家
            if (orderItemList.size() == 0) {
                cartList.remove(i);
            }
        }
        //删除购物车
        if (cartList.size() == 0) {
            cartList = null;
        }
        //重新添加
        this.setCartListToRedis(username, cartList);
    }

    @Override
    public void clearShop(List<BuyerCart> cartList,String username) {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            for (int j = 0; j < cartList.get(i).getOrderItemList().size(); j++) {
                OrderItem orderItem = cartList.get(i).getOrderItemList().get(j);
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                if (!"2".equals(item.getStatus())) {
                    orderItems.add(cartList.get(i).getOrderItemList().get(j));
                }
            }
            cartList.get(i).getOrderItemList().removeAll(orderItems);
            //删除购物商家
            if (cartList.get(i).getOrderItemList().size() == 0) {
                cartList.remove(i);
            }
        }
        //删除购物车
        if (cartList.size() == 0) {
            cartList = null;
        }
        //重新添加
        this.setCartListToRedis(username, cartList);

    }


    private OrderItem findOrderItemByItemId(List<OrderItem> orderItemList, Long itemId) {
        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                if (orderItem.getItemId().equals(itemId)) {
                    return orderItem;
                }
            }
        }
        return null;
    }

    private OrderItem createOrderItem(Item item, Integer num) {
        if (num < 0) {
            throw new RuntimeException("购买数量非法!");
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setNum(num);
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        //总价
        orderItem.setTotalFee(item.getPrice().multiply(new BigDecimal(num)));
        return orderItem;
    }

    private BuyerCart findBuyerCartBySellerId(List<BuyerCart> cartList, String sellerId) {
        if (cartList != null) {
            for (BuyerCart buyerCart : cartList) {
                if (buyerCart.getSellerId().equals(sellerId)) {
                    return buyerCart;
                }
            }
        }
        return null;
    }
}
