package com.mysb.core.dao.customer;

import com.mysb.core.pojo.customer.Customer;
import com.mysb.core.pojo.customer.CustomerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDao {
    int countByExample(CustomerQuery example);

    int deleteByExample(CustomerQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    List<Customer> selectByExample(CustomerQuery example);

    Customer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Customer record, @Param("example") CustomerQuery example);

    int updateByExample(@Param("record") Customer record, @Param("example") CustomerQuery example);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);
}