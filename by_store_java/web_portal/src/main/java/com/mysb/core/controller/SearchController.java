package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class SearchController {
    @Reference
    private SearchService searchService;

    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map<String,Object> req){
        return searchService.search(req);
    }


}
