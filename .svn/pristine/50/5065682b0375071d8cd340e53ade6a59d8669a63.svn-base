package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mktech.entity.DbJuhua;

@Repository
public interface DbJuhuaDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DbJuhua record);

    int insertSelective(DbJuhua record);

    DbJuhua selectByPrimaryKey(Integer id);
    
    DbJuhua selectNearestRecord(String timestamp);
    
    DbJuhua selectNearestRecord2();

    List<Map<String, Object>> selectLatestRecord();
    
    List<Map<String, Object>> selectLatestRecord500();
    
    int updateByPrimaryKeySelective(DbJuhua record);

    int updateByPrimaryKey(DbJuhua record);
    
    List<Map<String, Object>> selectLatestRecordByAll();
}
