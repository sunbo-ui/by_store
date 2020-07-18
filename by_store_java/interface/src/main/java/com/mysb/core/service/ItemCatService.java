package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    PageResult findByParentId(Long id, Integer page, Integer rows);

    List<ItemCat> findByParentId(Long id);

    ItemCat findOne(Long id);

    List<ItemCat> findAll();
}
