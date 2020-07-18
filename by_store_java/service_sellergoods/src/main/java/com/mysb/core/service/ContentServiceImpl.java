package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.ad.ContentCategoryDao;
import com.mysb.core.dao.ad.ContentDao;
import com.mysb.core.pojo.ad.Content;
import com.mysb.core.pojo.ad.ContentCategory;
import com.mysb.core.pojo.ad.ContentQuery;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;

    @Autowired
    private ContentCategoryDao categoryDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findPage(Content content, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        ContentQuery query = new ContentQuery();
        query.setOrderByClause("id desc");
        ContentQuery.Criteria criteria = query.createCriteria();
        if (content != null) {
            if (content.getTitle() != null && !"".equals(content.getTitle())) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
        }
        Page<Content> contentList = (Page<Content>) contentDao.selectByExample(query);
        return new PageResult(contentList.getTotal(), contentList.getResult());
    }

    @Override
    public List<ContentCategory> findCategoryAll() {
        return categoryDao.selectByExample(null);
    }

    @Override
    public void delete(Map<String, List<Content>> map) {
        List<Content> idList = map.get("idList");
        if (idList != null) {
            for (Content content : idList) {
                //删除redis
                redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).delete(content.getCategoryId());
                //删除数据库当中的广告
                contentDao.deleteByPrimaryKey(content.getId());
            }
        }
    }

    @Override
    public void update(Content content) {
        //根据广告id查询旧广告
        Content oldContent = contentDao.selectByPrimaryKey(content.getId());
        //删除旧广告
        redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).delete(oldContent.getCategoryId());
        //根据传入的最新的广告分类id,删除集合
        redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).delete(content.getCategoryId());
        //更新数据库
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public void save(Content content) {
        contentDao.insertSelective(content);
        //根据分类id删除redis当中的集合
        redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).delete(content.getCategoryId());
    }

    @Override
    public Content findById(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> list = contentDao.selectByExample(query);
        return list;
    }

    @Override
    public List<Content> findFormRedisByCategoryId(Long categoryId) {
        List<Content> contentList =
                (List<Content>) redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).
                        get(categoryId);
        //判断redis当中是否有
        if (contentList == null) {
            //没有,查出 放入redis一份
            contentList = findByCategoryId(categoryId);
            redisTemplate.boundHashOps(Constants.BY_CONTENT_LIST_REDIS).put(categoryId, contentList);
        }
        return contentList;
    }

}
