package com.mysb.core.service;

import com.mysb.core.pojo.communication.Communication;

import java.util.List;

public interface CommunicationService {
    void save(Communication communication);

    List<Communication> selectBySendId(String param);
}
