package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.track.Track;

import java.util.List;
import java.util.Map;

public interface TrackService {
    PageResult findPage(Integer page, Integer pageSize, String username);

    void saveTrack(Track track);

    void deleteTrack(Long id);

    void deleteSelect(String username, Map<String, List<Long>> map);

    void deleteAll(String username);
}
