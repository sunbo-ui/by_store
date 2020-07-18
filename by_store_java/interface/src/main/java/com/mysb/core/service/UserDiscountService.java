package com.mysb.core.service;

import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.userDiscount.UserDiscount;

import java.util.List;

public interface UserDiscountService {
    void saveUserDiscount(Discount discount, String username);

    Boolean findDiscountId(Integer discountId);

    List<UserDiscount> findByUsername(String username);


    PageResult findUserDiscount(String username, Integer page, Integer pageSize);

    void deleteUserDiscount(Integer id);

    List<Discount> findBySellerAndUserName(String username, String sellerId);
}
