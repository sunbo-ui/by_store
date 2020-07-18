package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.mysb.core.dao.seller.SellerDao;
import com.mysb.core.dao.token.TokenDao;
import com.mysb.core.dao.user.UserDao;
import com.mysb.core.pojo.seller.Seller;
import com.mysb.core.pojo.token.Token;
import com.mysb.core.pojo.token.TokenQuery;
import com.mysb.core.pojo.user.User;
import com.mysb.core.pojo.user.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoginSerivceImpl implements LoginSerivce {
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void register(Seller seller) {
        seller.setCreateTime(new Date());
        //审核状态注册的时候默认为0 ,未审核
        seller.setStatus("0");
        sellerDao.insertSelective(seller);
    }

    @Override
    public void saveToken(String token, String name) {
        Token tokenSave = new Token();
        tokenSave.setSellerId(name);
        tokenSave.setToken(token);
        tokenDao.insertSelective(tokenSave);
    }

    @Override
    public String selectUsernameByToken(String token) {
        TokenQuery query = new TokenQuery();
        TokenQuery.Criteria criteria = query.createCriteria();
        criteria.andTokenEqualTo(token);
        List<Token> tokens = tokenDao.selectByExample(query);
        return tokens.get(0).getSellerId();
    }

    @Override
    public void deleteToken(String authorization) {
        TokenQuery query = new TokenQuery();
        TokenQuery.Criteria criteria = query.createCriteria();
        criteria.andTokenEqualTo(authorization);
        tokenDao.deleteByExample(query);
    }

    @Override
    public User user(String username) {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(userQuery);
        return users.get(0);
    }


}
