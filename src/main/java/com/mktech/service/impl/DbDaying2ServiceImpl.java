
package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbDaying2Dao;
import com.mktech.entity.DbDaying1;
import com.mktech.entity.DbDaying2;
import com.mktech.service.DbDaying2Service;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 *
 */
@Service
public class DbDaying2ServiceImpl implements DbDaying2Service {

	@Resource
	private DbDaying2Dao dbDaying2Mapper;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DbDaying2 record) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.insert(record);
	}

	@Override
	public int insertSelective(DbDaying2 record) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.insertSelective(record);
	}

	@Override
	public DbDaying2 selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DbDaying2 record) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DbDaying2 record) {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.updateByPrimaryKey(record);
	}
	
	public DbDaying2 selectNearestRecord2(){
		return dbDaying2Mapper.selectNearestRecord2();
	}

	public List<Map<String, Object>> selectLatestRecord100(){
		return dbDaying2Mapper.selectLatestRecord100();
	}
	
	@Override
	public List<Map<String, Object>> selectLatestRecord() {
		// TODO Auto-generated method stub
		return dbDaying2Mapper.selectLatestRecord();
	}
	
	public DbDaying2 selectByTimeRangelimit1(String min, String max) {
		
		return dbDaying2Mapper.selectByTimeRangeLimit1(min , max);
	}

	public DbDaying2 selectfromDbDaying2Min() {
		return dbDaying2Mapper.selectLatestRecordInMin();
	}
	
	public int insertIntoMin(DbDaying2 dbDaying2) {
		return dbDaying2Mapper.insertInMin(dbDaying2);
	}
	
	public DbDaying2 selectfromDbDaying2Hour() {
		return dbDaying2Mapper.selectLatestRecordInHour();
	}
	
	public int insertIntoHour(DbDaying2 dbDaying2) {
		return dbDaying2Mapper.insertInHour(dbDaying2);
	}
	
	public DbDaying2 selectfromDbDaying2Day() {
		return dbDaying2Mapper.selectLatestRecordInDay();
	}
	
	public int insertIntoDay(DbDaying2 dbDaying2) {
		return dbDaying2Mapper.insertInDay(dbDaying2);
	}

	public List<Map<String, Object>> initYewu() {
		return dbDaying2Mapper.selectLatestRecordByAll();
	}

	public List<Map<String, Object>> updateLatestByWeek(Long timestamp) {
		timestamp+=8*3600*1000;
		return dbDaying2Mapper.selectLatestRecordByHour(String.valueOf(timestamp-604800000),String.valueOf(timestamp));
	}

	public List<Map<String, Object>> updateYewu(Long start,Long end) {
		Long range = (end - start)/1000;
		if(range<3600*24*7){
			return dbDaying2Mapper.selectLatestRecordByHour(String.valueOf(start),String.valueOf(end));
		}else{
			return dbDaying2Mapper.selectLatestRecordByDay(String.valueOf(start),String.valueOf(end));
		}
	}

	public List<Map<String, Object>> selectDataByRange(long start, long end) {
		Long range = (end - start)/1000;
		if(range<=3600*24){//小于1天返回原始数据
			return dbDaying2Mapper.selectLatestRecordBySec(String.valueOf(start),String.valueOf(end));
		}else if(range<= 3600*24*31){//小于一月返回分钟数据
			return dbDaying2Mapper.selectLatestRecordByMin(String.valueOf(start),String.valueOf(end));
		}else{//2678400
			return dbDaying2Mapper.selectLatestRecordByHour(String.valueOf(start),String.valueOf(end));
		}
	}

	public List<Map<String, Object>> selectRange() {
		return dbDaying2Mapper.selectRange();
	}

}
