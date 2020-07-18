package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.SpecEntity;
import com.mysb.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecService {
    List<Specification> findAll();

    PageResult findPage(Integer page, Integer rows, Specification spec);

    void saveSpec(SpecEntity spec);

    SpecEntity findById(Integer id);

    void updateSpec(SpecEntity specEntity);

    void deleteSpec(Map<String, List<Specification>> map);
}
