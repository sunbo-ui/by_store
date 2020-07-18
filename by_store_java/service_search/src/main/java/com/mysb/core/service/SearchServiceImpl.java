package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.mysb.core.pojo.item.Item;
import com.mysb.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> req) {
        //设置高亮
        Map<String, Object> map = highligntSearch(req);
        //根据查询参数,到solr中获取对应的分类结果集,由于分类重复,需要分组
        ArrayList<String> list = findGroupCategoryList(req);
        map.put("categoryList", list);
        //判断传入的参数是否有分类名称
        String category = String.valueOf(req.get("category"));
        if (category != null && !"".equals(category)) {
            //如果有参数,则根据分类查询对应的品牌集合和规格集合
            Map specListAndBrandList = findSpecListAndBrandList(category);
            map.putAll(specListAndBrandList);
        } else {
            //没有默认根据第一个分类查询对应的品牌集合和规格集合
            try {
                Map specListAndBrandList = findSpecListAndBrandList(list.get(0));
                map.putAll(specListAndBrandList);
            } catch (Exception e) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("error", "error");
                return hashMap;
            }

        }
        return map;
    }

    public Map findSpecListAndBrandList(String category) {
        //根据分类名称查询对应的模板id
        Long templateId = (Long) redisTemplate.boundHashOps(Constants.BY_CATEGORY_LIST_REDIS).
                get(category);
        //查询品牌
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps(Constants.BY_BRAND_LIST_REDIS).get(templateId);

        //查询规格
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps(Constants.BY_SPEC_LIST_REDIS).get(templateId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    public ArrayList<String> findGroupCategoryList(Map<String, Object> req) {
        ArrayList<String> list = new ArrayList<>();
        //获取查询关键字
        String keywords = String.valueOf(req.get("keywords"));
        //创建查询对象
        SimpleQuery simpleQuery = new SimpleQuery();
        //创建查询条件
        Criteria criteria = new Criteria("item_keywords");
        if(!"*".equals(keywords)){
            criteria.is(keywords);
        }
        //将查询条件放入查询对象
        simpleQuery.addCriteria(criteria);
        //创建分组对象
        GroupOptions options = new GroupOptions();
        //设置根据分类域进行分组
        options.addGroupByField("item_category");
        //将分组对象放入查询对象中
        simpleQuery.setGroupOptions(options);

        //分组查询分类集合
        GroupPage<Item> items = solrTemplate.queryForGroupPage(simpleQuery, Item.class);
        //获取结果集中分类域的集合
        GroupResult<Item> category = items.getGroupResult("item_category");
        //获取分类域的实体集合
        Page<GroupEntry<Item>> groupEntries = category.getGroupEntries();
        //遍历实体集合得到实体对象
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            list.add(groupValue);
        }
        return list;
    }

    public Map<String, Object> highligntSearch(Map<String, Object> req) {
        String keywords = String.valueOf(req.get("keywords"));
        Integer pageNo = Integer.parseInt(String.valueOf(req.get("pageNo")));
        Integer pageSize = Integer.parseInt(String.valueOf(req.get("pageSize")));
        //获取页面点击的分类过滤条件
        String category = String.valueOf(req.get("category"));
        //获取页面点击的品牌过滤条件
        String brand = String.valueOf(req.get("brand"));
        //获取页面点击的规格过滤条件
        String spec = String.valueOf(req.get("spec"));

        String price = String.valueOf(req.get("price"));

        //创建高亮
        SimpleHighlightQuery simpleHighlightQuery = new SimpleHighlightQuery();
        //创建高亮选项对象
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置哪个域需要高亮显示
        highlightOptions.addField("item_title");
        //设置高亮前缀
        highlightOptions.setSimplePrefix("<em style =\"color:red\">");
        highlightOptions.setSimplePostfix("</em>");
        //将高亮选项加入到查询对学校
        simpleHighlightQuery.setHighlightOptions(highlightOptions);

        //分类过滤查询
        if(category != null &&category!= "null"&&!"".equals(category)){
            FilterQuery query = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_category").is(category);
            query.addCriteria(criteria);
            simpleHighlightQuery.addFilterQuery(query);
        }

        if(brand != null && brand != "null"&&!"".equals(brand)){
            FilterQuery query = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_brand").is(brand);
            query.addCriteria(criteria);
            simpleHighlightQuery.addFilterQuery(query);
        }

        if(spec != null && spec != "null"&&!"".equals(spec)){
            Map map = JSON.parseObject(spec, Map.class);
            if(map != null && map.size()>0){
                Set<Map.Entry<String,String>> set = map.entrySet();
                for (Map.Entry<String,String> entry : set) {
                    FilterQuery query = new SimpleFilterQuery();
                    Criteria criteria = new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
                    query.addCriteria(criteria);
                    simpleHighlightQuery.addFilterQuery(query);
                }
            }
        }

        if(price != null && price != "null" && !"".equals(price)){
            String[] split = price.split("-");
            if(split != null && split.length == 2){
                if(!"0".equals(split[0])){
                    FilterQuery query = new SimpleFilterQuery();
                    Criteria criteria = new Criteria("item_price").greaterThanEqual(split[0]);
                    query.addCriteria(criteria);
                    simpleHighlightQuery.addFilterQuery(query);
                }
                if(!"*".equals(split[1])){
                    FilterQuery query = new SimpleFilterQuery();
                    Criteria criteria = new Criteria("item_price").lessThanEqual(split[1]);
                    query.addCriteria(criteria);
                    simpleHighlightQuery.addFilterQuery(query);
                }
            }
        }

        Criteria criteria = new Criteria("item_keywords");
        if(!"*".equals(keywords)){
            criteria.is(keywords);
        }
        //分页
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        Integer start = (pageNo - 1) * pageSize;
        simpleHighlightQuery.addCriteria(criteria);
        simpleHighlightQuery.setOffset(start);
        simpleHighlightQuery.setRows(pageSize);

        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(simpleHighlightQuery, Item.class);

        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        //定义存在高亮item集合
        ArrayList<Item> list = new ArrayList<>();
        for (HighlightEntry<Item> highlightEntry : highlighted) {
            //获取到不带高亮的实体对象
            Item entity = highlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
            if (highlights != null && highlights.size() > 0) {
                //获取高亮标题集合
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (snipplets != null && snipplets.size() > 0) {
                    String title = snipplets.get(0);
                    entity.setTitle(title);
                }
            }
            list.add(entity);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", items.getContent());
        map.put("totalPages", items.getTotalPages());
        map.put("total", items.getTotalElements());

        return map;
    }
}
