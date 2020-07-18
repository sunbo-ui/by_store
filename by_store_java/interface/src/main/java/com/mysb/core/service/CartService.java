package com.mysb.core.service;

import com.mysb.core.pojo.entry.BuyerCart;

import java.util.List;
import java.util.Map;

public interface CartService {
    /**
     * 将商品加入现有的购物车列表
     * @param cartList 现有的购物车列表
     * @param itemId 商品库存id
     * @param num 购买数量
     * @return
     */
    List<BuyerCart> addItemToCartList(List<BuyerCart> cartList, Long itemId, Integer num);

    void setCartListToRedis(String username, List<BuyerCart> cartList);

    List<BuyerCart> getCartListFromRedis(String username);

    List<BuyerCart> mergeCookieCartListToRedisCartList(List<BuyerCart> cartList, List<BuyerCart> redisCartList);

    void deleteOneCart(Long itemId, String username);

    List<BuyerCart> updateByNumCartList(Integer num, Long itemId, List<BuyerCart> cartList);

    void deleteCart(Map<String, List<Long>> reqMap, String username);

    void clearShop(List<BuyerCart> cartList,String username);
}
