package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.track.TrackDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.track.Track;
import com.mysb.core.pojo.track.TrackQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TrackServiceImpl implements TrackService {
    @Autowired
    private TrackDao trackDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findPage(Integer page, Integer pageSize, String username) {
        PageHelper.startPage(page, pageSize);
        TrackQuery trackQuery = new TrackQuery();
        trackQuery.setOrderByClause("create_time desc");
        TrackQuery.Criteria criteria = trackQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        Page<Track> tracks = (Page<Track>) trackDao.selectByExample(trackQuery);
        return new PageResult(tracks.getTotal(), tracks.getResult());
    }

    @Override
    public void saveTrack(Track track) {
        if (track.getUserId() == null || "null".equals(track.getUserId()) || "".equals(track.getUserId())) {
            List<Track> trackList = (List<Track>)redisTemplate.boundHashOps("username").get("track");
            if(trackList == null){
                trackList = new ArrayList<>();
            }
            //根据goodsId查询是否走过 如果走过更新现在时间
            TrackQuery trackQuery = new TrackQuery();
            TrackQuery.Criteria criteria = trackQuery.createCriteria();
            criteria.andGoodsIdEqualTo(track.getGoodsId());
            List<Track> tracks = trackDao.selectByExample(trackQuery);
            if (tracks.size() == 0) {
                trackList.add(track);
            }
            System.out.println(trackList);
            //存到redis当中   goodsId 作为 key  保存足迹
            redisTemplate.boundHashOps("username").put("track", trackList);
        } else {
            List<Track> trackRedis = (List<Track>) redisTemplate.boundHashOps("username").get("track");
            System.out.println(trackRedis);
            if (trackRedis != null) {
                for (Track trackRedi : trackRedis) {
                    trackRedi.setUserId(track.getUserId());
                    trackDao.insertSelective(trackRedi);
                }
                this.save(track);
                return;
            }
            //存到数据库当中
            this.save(track);
        }
    }

    public void save( Track track){
        //根据goodsId查询是否走过 如果走过更新现在时间
        TrackQuery trackQuery = new TrackQuery();
        TrackQuery.Criteria criteria = trackQuery.createCriteria();
        criteria.andGoodsIdEqualTo(track.getGoodsId());
        List<Track> tracks = trackDao.selectByExample(trackQuery);
        if (tracks.size() == 0) {
            //没有 就把保存
            trackDao.insertSelective(track);
        } else {
            //更新时间
            Track dataTrack = tracks.get(0);
            track.setId(dataTrack.getId());
            trackDao.updateByPrimaryKeySelective(track);
        }
        redisTemplate.boundHashOps("username").delete("track");
    }

    @Override
    public void deleteTrack(Long id) {
        trackDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteSelect(String username, Map<String, List<Long>> map) {
        List<Long> idList = map.get("idList");
        TrackQuery trackQuery = new TrackQuery();
        TrackQuery.Criteria criteria = trackQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        criteria.andIdIn(idList);
        trackDao.deleteByExample(trackQuery);
    }

    @Override
    public void deleteAll(String username) {
        TrackQuery trackQuery = new TrackQuery();
        TrackQuery.Criteria criteria = trackQuery.createCriteria();
        criteria.andUserIdEqualTo(username);
        trackDao.deleteByExample(trackQuery);
    }
}
