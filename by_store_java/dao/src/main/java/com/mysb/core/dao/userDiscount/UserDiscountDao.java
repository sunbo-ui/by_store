package com.mysb.core.dao.userDiscount;

import com.mysb.core.pojo.userDiscount.UserDiscount;
import com.mysb.core.pojo.userDiscount.UserDiscountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDiscountDao {
    int countByExample(UserDiscountQuery example);

    int deleteByExample(UserDiscountQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDiscount record);

    int insertSelective(UserDiscount record);

    List<UserDiscount> selectByExample(UserDiscountQuery example);

    UserDiscount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserDiscount record, @Param("example") UserDiscountQuery example);

    int updateByExample(@Param("record") UserDiscount record, @Param("example") UserDiscountQuery example);

    int updateByPrimaryKeySelective(UserDiscount record);

    int updateByPrimaryKey(UserDiscount record);
}