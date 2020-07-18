package com.mysb.core.dao.ewallet;

import com.mysb.core.pojo.ewallet.Ewallet;
import com.mysb.core.pojo.ewallet.EwalletQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EwalletDao {
    int countByExample(EwalletQuery example);

    int deleteByExample(EwalletQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Ewallet record);

    int insertSelective(Ewallet record);

    List<Ewallet> selectByExample(EwalletQuery example);

    Ewallet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Ewallet record, @Param("example") EwalletQuery example);

    int updateByExample(@Param("record") Ewallet record, @Param("example") EwalletQuery example);

    int updateByPrimaryKeySelective(Ewallet record);

    int updateByPrimaryKey(Ewallet record);
}