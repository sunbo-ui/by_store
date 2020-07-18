package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeService {
    PageResult findPage(Integer page, Integer rows, TypeTemplate template);

    void saveType(TypeTemplate template);

    TypeTemplate findById(Integer id);

    void deleteType(Map<String, List<TypeTemplate>> map);

    void updateType(TypeTemplate template);

    List<Map> findBySpecList(Long id);
}
