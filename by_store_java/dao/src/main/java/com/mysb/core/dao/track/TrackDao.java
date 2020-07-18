package com.mysb.core.dao.track;

import com.mysb.core.pojo.track.Track;
import com.mysb.core.pojo.track.TrackQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackDao {
    int countByExample(TrackQuery example);

    int deleteByExample(TrackQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Track record);

    int insertSelective(Track record);

    List<Track> selectByExample(TrackQuery example);

    Track selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Track record, @Param("example") TrackQuery example);

    int updateByExample(@Param("record") Track record, @Param("example") TrackQuery example);

    int updateByPrimaryKeySelective(Track record);

    int updateByPrimaryKey(Track record);
}