package com.mysb.core.service;

import com.mysb.core.pojo.ewallet.Ewallet;

import java.math.BigDecimal;

public interface EwalletService {
    Ewallet findAmountByUsername(String username);

    void save(String username);

    void insertAmount(String username, BigDecimal amount);
}
