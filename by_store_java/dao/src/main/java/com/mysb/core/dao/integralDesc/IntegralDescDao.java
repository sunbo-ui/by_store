package com.mysb.core.dao.integralDesc;

import com.mysb.core.pojo.integralDesc.IntegralDesc;
import com.mysb.core.pojo.integralDesc.IntegralDescQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IntegralDescDao {
    int countByExample(IntegralDescQuery example);

    int deleteByExample(IntegralDescQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(IntegralDesc record);

    int insertSelective(IntegralDesc record);

    List<IntegralDesc> selectByExample(IntegralDescQuery example);

    IntegralDesc selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IntegralDesc record, @Param("example") IntegralDescQuery example);

    int updateByExample(@Param("record") IntegralDesc record, @Param("example") IntegralDescQuery example);

    int updateByPrimaryKeySelective(IntegralDesc record);

    int updateByPrimaryKey(IntegralDesc record);
}