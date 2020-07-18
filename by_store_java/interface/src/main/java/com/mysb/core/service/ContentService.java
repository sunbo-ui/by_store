package com.mysb.core.service;

import com.mysb.core.pojo.ad.Content;
import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Goods;

import java.util.List;
import java.util.Map;

public interface ContentService {
    PageResult findPage(Content content, Integer page, Integer rows);

    List<ContentCategory> findCategoryAll();

    void delete(Map<String, List<Content>> map);

    void update(Content content);

    void save(Content content);

    Content findById(Long id);

    List<Content> findByCategoryId(Long categoryId);

    List<Content> findFormRedisByCategoryId(Long categoryId);
}
