package com.mysb.core.dao.integral;

import com.mysb.core.pojo.integral.Integral;
import com.mysb.core.pojo.integral.IntegralQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IntegralDao {
    int countByExample(IntegralQuery example);

    int deleteByExample(IntegralQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Integral record);

    int insertSelective(Integral record);

    List<Integral> selectByExample(IntegralQuery example);

    Integral selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Integral record, @Param("example") IntegralQuery example);

    int updateByExample(@Param("record") Integral record, @Param("example") IntegralQuery example);

    int updateByPrimaryKeySelective(Integral record);

    int updateByPrimaryKey(Integral record);
}