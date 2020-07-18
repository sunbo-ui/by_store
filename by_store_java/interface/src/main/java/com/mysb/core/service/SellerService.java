package com.mysb.core.service;

import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.seller.Seller;

import java.util.List;

public interface SellerService {
    PageResult findPage(Integer page, Integer rows, Seller seller, Long status);

    Seller findById(String sellerId);

    void updateStatus(String sellerId, String status);

    List<Seller> findByStatus(Long status);

    void updateBySellerId(String sellerId, Seller seller);
}
