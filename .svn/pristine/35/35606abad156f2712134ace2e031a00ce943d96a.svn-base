package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.DbDaying1;
import com.mktech.entity.DbDaying2;

@Repository
public interface DbDaying2Dao {
    int deleteByPrimaryKey(Integer id);

    int insert(DbDaying2 record);

    int insertSelective(DbDaying2 record);

    DbDaying2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DbDaying2 record);

    int updateByPrimaryKey(DbDaying2 record);

	DbDaying2 selectNearestRecord2();

	List<Map<String, Object>> selectLatestRecord();

	List<Map<String, Object>> selectLatestRecord100();

	DbDaying2 selectByTimeRangeLimit1(@Param("min") String min,@Param("max") String max);

	DbDaying2 selectLatestRecordInMin();

	int insertInMin(DbDaying2 dbDaying2);

	DbDaying2 selectLatestRecordInHour();

	int insertInHour(DbDaying2 dbDaying2);

	DbDaying2 selectLatestRecordInDay();

	int insertInDay(DbDaying2 dbDaying2);

	List<Map<String, Object>> selectLatestRecordByAll();

	List<Map<String, Object>> selectLatestRecordByHour(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectLatestRecordByDay(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectLatestRecordBySec(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectLatestRecordByMin(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectRange();
}