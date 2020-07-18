package com.mysb.core.service;

import com.mysb.core.pojo.address.Address;
import com.mysb.core.pojo.entry.PageResult;

public interface AddressService {
    PageResult findByUsername(Integer page, Integer rows, String username);

    void saveAddress(String username,Address address);

    void setDefault(String username,Long id);

    void updateAddress(Address address);

    Address findById(Long id);

    void deleteAddress(Long id);
}
