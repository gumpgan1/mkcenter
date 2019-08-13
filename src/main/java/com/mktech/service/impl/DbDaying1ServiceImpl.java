/**
 * @author Chnyge Lin
 */
package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbDaying1Dao;
import com.mktech.entity.DbDaying1;
import com.mktech.entity.DbJiangyin;
import com.mktech.service.DbDaying1Service;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 *
 */
@Service
public class DbDaying1ServiceImpl implements DbDaying1Service {
	
	@Resource
	private DbDaying1Dao dbDaying1Mapper;
	

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.deleteByPrimaryKey(id);
	}


	@Override
	public int insert(DbDaying1 record) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.insert(record);
	}

	@Override
	public int insertSelective(DbDaying1 record) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.insertSelective(record);
	}

	@Override
	public DbDaying1 selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DbDaying1 record) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DbDaying1 record) {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<Map<String, Object>> selectLatestRecord100(){
		return dbDaying1Mapper.selectLatestRecord100();
	}
	
	@Override
	public List<Map<String, Object>> selectLatestRecord() {
		// TODO Auto-generated method stub
		return dbDaying1Mapper.selectLatestRecord();
	}
	
	public DbDaying1 selectNearestRecord2(){
		return dbDaying1Mapper.selectNearestRecord2();
	}


	public DbDaying1 selectByTimeRangelimit1(String min, String max) {
		
		return dbDaying1Mapper.selectByTimeRangeLimit1(min , max);
	}


	public DbDaying1 selectfromDbDaying1Min() {
		return dbDaying1Mapper.selectLatestRecordInMin();
	}
	
	public int insertIntoMin(DbDaying1 dbDaying1) {
		return dbDaying1Mapper.insertInMin(dbDaying1);
	}
	
	public DbDaying1 selectfromDbDaying1Hour() {
		return dbDaying1Mapper.selectLatestRecordInHour();
	}
	
	public int insertIntoHour(DbDaying1 dbDaying1) {
		return dbDaying1Mapper.insertInHour(dbDaying1);
	}
	
	public DbDaying1 selectfromDbDaying1Day() {
		return dbDaying1Mapper.selectLatestRecordInDay();
	}
	
	public int insertIntoDay(DbDaying1 dbDaying1) {
		return dbDaying1Mapper.insertInDay(dbDaying1);
	}


	public List<Map<String, Object>> initYewu() {
		return dbDaying1Mapper.selectLatestRecordByAll();
	}


	public List<Map<String, Object>> updateLatestByWeek(Long timestamp) {
		timestamp+=8*3600*1000;
		return dbDaying1Mapper.selectLatestRecordByHour(String.valueOf(timestamp-604800000),String.valueOf(timestamp));
	}


	public List<Map<String, Object>> updateYewu(Long start,Long end) {
		Long range = (end - start)/1000;
		if(range<3600*24*7){
			return dbDaying1Mapper.selectLatestRecordByHour(String.valueOf(start),String.valueOf(end));
		}else{
			return dbDaying1Mapper.selectLatestRecordByDay(String.valueOf(start),String.valueOf(end));
		}
	}


	public List<Map<String, Object>> selectRange() {
		return dbDaying1Mapper.selectRange();
	}


	public List<Map<String, Object>> selectDataByRange(long start, long end) {
		Long range = (end - start)/1000;
		if(range<=3600*24){//小于1天返回原始数据
			return dbDaying1Mapper.selectLatestRecordBySec(String.valueOf(start),String.valueOf(end));
		}else if(range<= 3600*24*31){//小于一月返回分钟数据
			return dbDaying1Mapper.selectLatestRecordByMin(String.valueOf(start),String.valueOf(end));
		}else{//2678400
			return dbDaying1Mapper.selectLatestRecordByHour(String.valueOf(start),String.valueOf(end));
		}
	}



}
