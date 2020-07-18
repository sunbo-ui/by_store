package com.mysb.core.dao.communication;

import com.mysb.core.pojo.communication.Communication;
import com.mysb.core.pojo.communication.CommunicationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommunicationDao {
    int countByExample(CommunicationQuery example);

    int deleteByExample(CommunicationQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Communication record);

    int insertSelective(Communication record);

    List<Communication> selectByExample(CommunicationQuery example);

    Communication selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Communication record, @Param("example") CommunicationQuery example);

    int updateByExample(@Param("record") Communication record, @Param("example") CommunicationQuery example);

    int updateByPrimaryKeySelective(Communication record);

    int updateByPrimaryKey(Communication record);
}