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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountDao discountDao;
    @Autowired
    private UserDiscountDao userDiscountDao;
    @Override
    public PageResult findBySeller(Integer page, Integer rows, String username) {
        PageHelper.startPage(page, rows);
        DiscountQuery discountQuery = new DiscountQuery();
        discountQuery.setOrderByClause("id desc");
        DiscountQuery.Criteria criteria = discountQuery.createCriteria();
        criteria.andSellerIdEqualTo(username);
        Page<Discount> discounts = (Page<Discount>) discountDao.selectByExample(discountQuery);
        return new PageResult(discounts.getTotal(), discounts.getResult());
    }

    @Override
    public Discount findById(Integer id) {
        return discountDao.selectByPrimaryKey(id);
    }

    @Override
    public void saveDiscount(Discount discount) throws ParseException {
        discountDao.insertSelective(discount);
    }

    @Override
    public void updateDiscount(Discount discount) {
        discountDao.updateByPrimaryKeySelective(discount);
    }

    @Override
    public void deleteDiscount(Integer id) {
        discountDao.deleteByPrimaryKey(id);
        /*用户领券后进行操作*/
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria queryCriteria = query.createCriteria();
        queryCriteria.andDiscountIdEqualTo(id);

        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setIsSalte(1);
        userDiscountDao.updateByExampleSelective(userDiscount,query);
    }

    @Override
    public void deleteDiscountBySelection(Map<String, List<Discount>> map) {
        List<Discount> idList = map.get("idList");
        ArrayList<Integer> list = new ArrayList<>();
        for (Discount discount : idList) {
            list.add(discount.getId());
        }
        DiscountQuery discountQuery = new DiscountQuery();
        DiscountQuery.Criteria criteria = discountQuery.createCriteria();
        criteria.andIdIn(list);
        discountDao.deleteByExample(discountQuery);

        /*用户领券后进行操作*/
        UserDiscountQuery query = new UserDiscountQuery();
        UserDiscountQuery.Criteria queryCriteria = query.createCriteria();
        queryCriteria.andDiscountIdIn(list);

        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setIsSalte(1);
        userDiscountDao.updateByExampleSelective(userDiscount,query);
    }

    @Override
    public List<Discount> findBySellerIdShow(String sellerId) {
        DiscountQuery discountQuery = new DiscountQuery();
        DiscountQuery.Criteria criteria = discountQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerId);
        return discountDao.selectByExample(discountQuery);
    }


}
