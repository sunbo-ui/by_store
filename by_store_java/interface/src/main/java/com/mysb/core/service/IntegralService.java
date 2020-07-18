package com.mysb.core.service;

import java.util.Map;

public interface IntegralService {
    Map<String, Object> findByUsername(String username);

    void exchange(String username,String integral) throws Exception;
}
