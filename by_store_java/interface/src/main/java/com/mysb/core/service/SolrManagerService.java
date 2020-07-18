package com.mysb.core.service;

import java.util.List;

public interface SolrManagerService {
    public void saveItemToSolr(List list);
    void deleteItemByGoodsId(List goodsIds);
}