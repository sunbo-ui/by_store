package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.comment.CommentDao;
import com.mysb.core.dao.order.OrderDao;
import com.mysb.core.dao.order.OrderItemDao;
import com.mysb.core.pojo.comment.Comment;
import com.mysb.core.pojo.comment.CommentQuery;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.order.Order;
import com.mysb.core.pojo.order.OrderItem;
import com.mysb.core.pojo.order.OrderItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CommentDao commentDao;

    @Override
    public void save(Long orderId, Comment comment) {
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        //订单中所有的商品
        List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
        for (OrderItem orderItem : orderItems) {
            Long goodsId = orderItem.getGoodsId();
            comment.setGoodsId(goodsId);
            comment.setSellerId(String.valueOf(orderItem.getSellerId()));
            comment.setAudit(0);//未审核
            comment.setCreateTime(new Date());
            commentDao.insertSelective(comment);
        }
        //设置为已评价
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("9");
        orderDao.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageResult findByPage(Integer page, Integer rows, Comment comment) {
        PageHelper.startPage(page, rows);
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setOrderByClause("create_time desc");
        if (comment != null) {
            CommentQuery.Criteria criteria = commentQuery.createCriteria();
            if (comment.getSellerId() != null && !"".equals(comment.getSellerId())) {
                criteria.andSellerIdLike("%" + comment.getSellerId() + "%");
            }
            if (comment.getUserId() != null && !"".equals(comment.getUserId())) {
                criteria.andUserIdLike(comment.getUserId());
            }
        }
        Page<Comment> comments = (Page<Comment>) commentDao.selectByExample(commentQuery);
        return new PageResult(comments.getTotal(), comments.getResult());
    }

    @Override
    public Comment findById(Long id) {
        return commentDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setAudit(status);
        commentDao.updateByPrimaryKeySelective(comment);
    }

    @Override
    public PageResult findByGoodsId(Integer page, Integer rows, Long goodsId) {
        PageHelper.startPage(page, rows);
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setOrderByClause("create_time desc");
        CommentQuery.Criteria criteria = commentQuery.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        Page<Comment> comments = (Page<Comment>) commentDao.selectByExample(commentQuery);
        List<Comment> result = comments.getResult();
        List<Comment> list = new ArrayList<>();
        for (Comment comment : result) {
            if (comment.getAudit() == 1) {
                list.add(comment);
            }
        }
        if (list != null) {
            return new PageResult(comments.getTotal(), list);
        } else {
            return new PageResult(comments.getTotal(), null);
        }
    }

    @Override
    public PageResult findByPageBySellerId(Integer page, Integer rows, String sellerId, Comment comment) {
        PageHelper.startPage(page, rows);
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setOrderByClause("create_time desc");
        if (comment != null) {
            CommentQuery.Criteria criteria = commentQuery.createCriteria();
            if (comment.getUserId() != null && !"".equals(comment.getUserId())) {
                criteria.andUserIdLike(comment.getUserId());
            }
            if(sellerId != null){
                criteria.andSellerIdEqualTo(sellerId);
            }
        }
        Page<Comment> comments = (Page<Comment>) commentDao.selectByExample(commentQuery);
        return new PageResult(comments.getTotal(), comments.getResult());
    }

    @Override
    public void updateShopContent(String shopContent,Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setShopContent(shopContent);
        commentDao.updateByPrimaryKeySelective(comment);
    }

    @Override
    public PageResult findByUserId(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page,pageSize);
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setOrderByClause("create_time desc");
        if(userId != null){
            CommentQuery.Criteria criteria = commentQuery.createCriteria();
            criteria.andUserIdEqualTo(userId);
        }
        Page<Comment> comments = (Page<Comment>)commentDao.selectByExample(commentQuery);
        return new PageResult(comments.getTotal(),comments.getResult());
    }

    @Override
    public List<OrderItem> findOrderItemByGoodsId(@RequestBody Map<String,List<Long>> reqMap) {
        List<Long> idList = reqMap.get("idList");
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        if(idList != null){
            criteria.andGoodsIdIn(idList);
        }
        List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
        return orderItems;
    }

    @Override
    public void deleteCommentById(Long id) {
        commentDao.deleteByPrimaryKey(id);
    }

    @Override
    public void updateComment(Comment comment) {
        comment.setShopContent("");
        comment.setAudit(0);
        comment.setCreateTime(new Date());
        commentDao.updateByPrimaryKeySelective(comment);
    }


}
