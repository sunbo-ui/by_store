package com.mysb.core.service;

import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.pojo.user.User;

public interface LoginSerivce {
    void register(Seller seller);

    void saveToken(String token, String name);

    String selectUsernameByToken(String token);

    void deleteToken(String authorization);

    User user(String username);
}
