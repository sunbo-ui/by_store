package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.seller.SellerDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;

    @Override
    public PageResult findPage(Integer page, Integer rows, Seller seller, Long status) {
        PageHelper.startPage(page, rows);
        SellerQuery query = new SellerQuery();
        query.setOrderByClause("create_time desc");
        SellerQuery.Criteria criteria = query.createCriteria();
        if (seller != null) {
            if (!"".equals(seller.getNickName()) && seller.getNickName() != null) {
                criteria.andNickNameLike("%" + seller.getNickName() + "%");
            }
            if (!"".equals(seller.getName()) && seller.getName() != null) {
                criteria.andNameLike("%" + seller.getName() + "%");
            }
            if (seller.getStatus() != null && !"".equals(seller.getStatus())) {
                criteria.andStatusLike("%" + seller.getStatus() + "%");
            }
            if (status == 1) {
                //只显示未审核的
                criteria.andStatusEqualTo("0");
            }
        }

        Page<Seller> sellers = (Page<Seller>) sellerDao.selectByExample(query);
        return new PageResult(sellers.getTotal(), sellers.getResult());
    }

    @Override
    public Seller findById(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setStatus(status);
        seller.setSellerId(sellerId);
        sellerDao.updateByPrimaryKeySelective(seller);
    }

    @Override
    public List<Seller> findByStatus(Long status) {
        List<Seller> sellers = new ArrayList<>();
        if (status == 0) {
            sellers = sellerDao.selectByExample(null);
        } else {
            SellerQuery query = new SellerQuery();
            SellerQuery.Criteria criteria = query.createCriteria();
            criteria.andStatusEqualTo(String.valueOf(status));
            sellers = sellerDao.selectByExample(query);
        }
        return sellers;
    }

    @Override
    public void updateBySellerId(String sellerId, Seller seller) {
        seller.setSellerId(sellerId);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
