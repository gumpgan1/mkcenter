package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.DbLimo;

@Repository
public interface DbLimoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DbLimo record);

    int insertSelective(DbLimo record);

    DbLimo selectByPrimaryKey(Integer id);
    
    DbLimo selectByTimeRangeLimit1(@Param("min") String min,@Param("max") String max);
    
    DbLimo selectNearestRecord(String timestamp);
    
    DbLimo selectNearestRecord2();
    
    List<DbLimo> selectByKeyRange(@Param("min") Integer min,@Param("max") Integer max);
    
    List<Map<String, Object>> selectByKeyRangeIntoMap(@Param("min") Integer min,@Param("max") Integer max);
    
    List<Map<String, Object>> selectLatestRecord();
    
    List<Map<String, Object>> selectLatestRecord500();

    int updateByPrimaryKeySelective(DbLimo record);

    int updateByPrimaryKey(DbLimo record);
    
    List<Map<String, Object>> selectLatestRecordByAll();
    
    List<Map<String, Object>> selectLatestRecordByDay(@Param("start") String start,@Param("end") String end);
    
    List<Map<String, Object>> selectLatestRecordByHour(@Param("start") String start,@Param("end") String end);
    
    List<Map<String, Object>> selectLatestRecordByMin(@Param("start") String start,@Param("end") String end);
    
    List<Map<String, Object>> selectLatestRecordBySec(@Param("start") String start,@Param("end") String end);

    DbLimo selectLatestRecordInHour();
    
    int insertInHour(DbLimo dblimo);
    
    DbLimo selectLatestRecordInMin();
    
    int insertInMin(DbLimo dblimo);
    
    DbLimo selectLatestRecordInDay();
    
    int insertInDay(DbLimo dblimo);
    
    List<Map<String, Object>> selectRange();
}