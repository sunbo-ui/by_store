package com.mysb.core.service;

import com.mysb.core.pojo.entry.GoodsEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.pojo.item.Item;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    void add(GoodsEntity goodsEntity);

    PageResult findPage(Goods goods, Integer page, Integer rows);

    GoodsEntity findById(Long id);

    void update(GoodsEntity goodsEntity);

    void delete(Map<String, List<Goods>> map);

    void updateStatus(Long id,Map<String,List<Goods>> map);

    List<Item> findItemByGoodsIdAndStatus(Long id, Map<String, List<Goods>> map);

    public List<Long> findItemByGoodsId(Map<String,List<Goods>> map);

    Goods findGoodsById(Long id);

    PageResult findByStatus(Integer page, Integer rows, String status);

    List<Goods> findBySellerId(String username);
}
