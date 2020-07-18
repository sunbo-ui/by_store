package com.mysb.core.dao.collect;

import com.mysb.core.pojo.collect.Collect;
import com.mysb.core.pojo.collect.CollectQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectDao {
    int countByExample(CollectQuery example);

    int deleteByExample(CollectQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Collect record);

    int insertSelective(Collect record);

    List<Collect> selectByExample(CollectQuery example);

    Collect selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Collect record, @Param("example") CollectQuery example);

    int updateByExample(@Param("record") Collect record, @Param("example") CollectQuery example);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);
}