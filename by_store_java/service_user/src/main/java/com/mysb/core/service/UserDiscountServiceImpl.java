package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.discount.DiscountDao;
import com.mysb.core.dao.userDiscount.UserDiscountDao;
import com.mysb.core.pojo.discount.Discount;
import com.mysb.core.pojo.discount.DiscountQuery;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.userDiscount.UserDiscount;
import com.mysb.core.pojo.userDiscount.UserDiscountQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDiscountServiceImpl implements UserDiscountService {

    @Autowired
    private UserDiscountDao userDiscountDao;
    @Autowired
    private DiscountDao discountDao;

    @Override
    public void saveUserDiscount(Discount discount, String username) {
        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setUserId(username);
        userDiscount.setSellerId(discount.getSellerId());
        userDiscount.setIsSalte(0);
        userDiscount.setDiscountId(discount.getId());
        userDiscountDao.insertSelective(userDiscount);

        /*优惠券减少*/
        discount.setNum(String.valueOf(Integer.valueOf(discount.getNum()) - 1));
        if ("0".equals(discount.getNum())) {
            return;
        }
        discountDao.updateByPrimaryKeySelective(discount);
    }

    @Override
    public Boolean findDiscountId(Integer discountId) {
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria criteria = query.createCriteria();
        criteria.andDiscountIdEqualTo(discountId);
        List<UserDiscount> userDiscounts = userDiscountDao.selectByExample(query);
        if (userDiscounts.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<UserDiscount> findByUsername(String username) {
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(username);
        return userDiscountDao.selectByExample(query);
    }

    @Override
    public PageResult findUserDiscount(String username, Integer page, Integer pageSize) {
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<UserDiscount> list = userDiscountDao.selectByExample(query);
        if (list.size() > 0) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (UserDiscount discount : list) {
                arrayList.add(discount.getDiscountId());
            }
            PageHelper.startPage(page,pageSize);
            DiscountQuery discountQuery = new DiscountQuery();
            DiscountQuery.Criteria queryCriteria = discountQuery.createCriteria();
            queryCriteria.andIdIn(arrayList);
            Page<Discount> discounts = ( Page<Discount>)discountDao.selectByExample(discountQuery);
            return new PageResult(discounts.getTotal(),discounts.getResult());
        }
        return null;
    }

    @Override
    public void deleteUserDiscount(Integer id) {
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria criteria = query.createCriteria();
        criteria.andDiscountIdEqualTo(id);
        userDiscountDao.deleteByExample(query);
    }

    @Override
    public List<Discount> findBySellerAndUserName(String username, String sellerId) {
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(username);
        List<UserDiscount> list = userDiscountDao.selectByExample(query);
        if (list.size() > 0) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (UserDiscount discount : list) {
                arrayList.add(discount.getDiscountId());
            }
            DiscountQuery discountQuery = new DiscountQuery();
            DiscountQuery.Criteria queryCriteria = discountQuery.createCriteria();
            queryCriteria.andIdIn(arrayList);
            queryCriteria.andSellerIdEqualTo(sellerId);
            return discountDao.selectByExample(discountQuery);
        }
        return null;
    }

}
