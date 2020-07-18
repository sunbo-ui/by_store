package com.mysb.core.dao.token;

import com.mysb.core.pojo.token.Token;
import com.mysb.core.pojo.token.TokenQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TokenDao {
    int countByExample(TokenQuery example);

    int deleteByExample(TokenQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Token record);

    int insertSelective(Token record);

    List<Token> selectByExample(TokenQuery example);

    Token selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Token record, @Param("example") TokenQuery example);

    int updateByExample(@Param("record") Token record, @Param("example") TokenQuery example);

    int updateByPrimaryKeySelective(Token record);

    int updateByPrimaryKey(Token record);
}