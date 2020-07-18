package com.mysb.core.service;

import com.mysb.core.pojo.collect.Collect;
import com.mysb.core.pojo.entry.PageResult;

import java.util.List;
import java.util.Map;

public interface CollectService {
    void saveCollect(Collect collect);

    PageResult findPage(Integer page, Integer pageSize, String username);

    void deleteCollect(Long id);

    Collect findById(Long id);

    void updateNotes(Collect collect);

    void saveCollectBySelect(String username, Map<String, List<Long>> reqMap);

    void deleteAllCollect(String username);
}
