package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.item.ItemCat;
import com.mysb.core.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findByParentId")
    public PageResult findByParentId(Long id, Integer page, Integer rows){
        return itemCatService.findByParentId(id,page,rows);
    }
    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        return  itemCatService.findOne(id);
    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }
}
