package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.template.TypeTemplate;
import com.mysb.core.service.TypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temp")
public class TypeController {
    @Reference
    private TypeService typeTemplate;

    @RequestMapping("/findById")
    public TypeTemplate findById(Integer id){
        return typeTemplate.findById(id);
    }

    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id){
        return typeTemplate.findBySpecList(id);
    }
}
