package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.DbDaying1;

@Repository
public interface DbDaying1Dao {
    int deleteByPrimaryKey(Integer id);

    int insert(DbDaying1 record);

    int insertSelective(DbDaying1 record);

    DbDaying1 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DbDaying1 record);

    int updateByPrimaryKey(DbDaying1 record);

	List<Map<String, Object>> selectLatestRecord100();

	List<Map<String, Object>> selectLatestRecord();

	Map<String, Object> selectLatestRecordInMinByMap();

	DbDaying1 selectNearestRecord2();

	DbDaying1 selectByTimeRangeLimit1(@Param("min") String min,@Param("max") String max);

	DbDaying1 selectLatestRecordInMin();

	DbDaying1 selectLatestRecordInHour();
	int insertInMin(DbDaying1 dbDaying1);

	int insertInHour(DbDaying1 dbDaying1);

	DbDaying1 selectLatestRecordInDay();

	int insertInDay(DbDaying1 dbDaying1);

	List<Map<String, Object>> selectLatestRecordByAll();

	List<Map<String, Object>> selectLatestRecordByHour(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectLatestRecordByDay(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectRange();

	List<Map<String, Object>> selectLatestRecordBySec(@Param("start") String start,@Param("end") String end);

	List<Map<String, Object>> selectLatestRecordByMin(@Param("start") String start,@Param("end") String end);
}