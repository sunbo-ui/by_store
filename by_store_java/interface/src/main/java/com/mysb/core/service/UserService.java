package com.mysb.core.service;

import com.mysb.core.pojo.customer.Customer;

import java.util.List;
import java.util.Set;

public interface UserService {
    Customer findById(String username);

    void sendCode(String phone);

    void register(Customer customer) throws Exception;

    void saveCustomer(Customer customer);

    List<Customer> findFormRedisByKeys(Set keys);
}
