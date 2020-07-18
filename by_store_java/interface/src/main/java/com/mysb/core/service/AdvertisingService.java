package com.mysb.core.service;

import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.entry.PageResult;

import java.util.List;
import java.util.Map;

public interface AdvertisingService {
    PageResult findAll(Integer page, Integer rows, ContentCategory category);

    void save(ContentCategory category);

    ContentCategory findById(Long id);

    void update(ContentCategory category);

    void delete(Map<String, List<ContentCategory>> map);
}
