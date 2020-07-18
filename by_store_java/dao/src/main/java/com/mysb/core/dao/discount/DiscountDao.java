package com.mysb.core.dao.discount;

import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.discount.DiscountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscountDao {
    int countByExample(DiscountQuery example);

    int deleteByExample(DiscountQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Discount record);

    int insertSelective(Discount record);

    List<Discount> selectByExample(DiscountQuery example);

    Discount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Discount record, @Param("example") DiscountQuery example);

    int updateByExample(@Param("record") Discount record, @Param("example") DiscountQuery example);

    int updateByPrimaryKeySelective(Discount record);

    int updateByPrimaryKey(Discount record);
}