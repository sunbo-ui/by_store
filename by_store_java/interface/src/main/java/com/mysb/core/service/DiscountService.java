package com.mysb.core.service;

import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.entry.PageResult;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface DiscountService {
    PageResult findBySeller(Integer page, Integer rows, String username);

    Discount findById(Integer id);

    void saveDiscount(Discount discount) throws ParseException;

    void updateDiscount(Discount discount);

    void deleteDiscount(Integer id);

    void deleteDiscountBySelection(Map<String, List<Discount>> map);

    List<Discount> findBySellerIdShow(String sellerId);
}
