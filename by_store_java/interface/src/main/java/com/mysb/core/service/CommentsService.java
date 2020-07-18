package com.mysb.core.service;


import com.mysb.core.pojo.comment.Comment;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.order.OrderItem;

import java.util.List;
import java.util.Map;

public interface CommentsService {
    void save(Long orderId, Comment comment);

    PageResult findByPage(Integer page, Integer rows, Comment comment);

    Comment findById(Long id);

    void updateStatus(Long id,Integer status);

    PageResult findByGoodsId(Integer page, Integer rows, Long goodsId);

    PageResult findByPageBySellerId(Integer page, Integer rows, String sellerId, Comment comment);

    void updateShopContent(String shopContent,Long id);

    PageResult findByUserId(Integer page, Integer pageSize, String userId);

    List<OrderItem> findOrderItemByGoodsId(Map<String,List<Long>> reqMap);

    void deleteCommentById(Long id);

    void updateComment(Comment comment);

}
