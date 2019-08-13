package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mktech.entity.DbJuhuaOld;
import com.mktech.entity.SystemSummary;

@Repository
public interface DbJuhuaOldDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DbJuhuaOld record);

    int insertSelective(DbJuhuaOld record);

    DbJuhuaOld selectByPrimaryKey(Integer id);
    
    DbJuhuaOld selectNearestRecord(String timestamp);
    
    DbJuhuaOld selectNearestRecord2();

    List<Map<String, Object>> selectLatestRecord();
    
    List<Map<String, Object>> selectLatestRecord500();
    
    int updateByPrimaryKeySelective(DbJuhuaOld record);

    int updateByPrimaryKey(DbJuhuaOld record);
    
    List<Map<String, Object>> selectLatestRecordByAll();
	
	DbJuhuaOld checkIfExistTime(String timestamp);
}
