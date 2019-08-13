package com.mktech.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.SystemSummary;

@Repository
public interface SystemSummaryDao {
    int deleteByPrimaryKey(Integer systemId);

    int insert(SystemSummary record);

    int insertSelective(SystemSummary record);

    SystemSummary selectByPrimaryKey(Integer systemId);

    int updateByPrimaryKeySelective(SystemSummary record);

    int updateByPrimaryKey(SystemSummary record);
    
    SystemSummary checkIfExist(String deviceid);
    
    int updateLatestUpdateTimeByPrimaryKey(@Param("systemId") Integer systemId,@Param("timestamp") String timestamp);
}