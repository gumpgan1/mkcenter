/**
 * @author Chnyge Lin
 * @time 2018-7-3
 * @comment 
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import com.mktech.entity.DbJiangyin;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * @time 2018-7-3
 * @comment 
 */
public interface DbJiangyinService {
	 	int deleteByPrimaryKey(Integer id);

	    int insert(DbJiangyin record);

	    int insertSelective(DbJiangyin record);

	    DbJiangyin selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(DbJiangyin record);

	    int updateByPrimaryKey(DbJiangyin record);
	    List<Map<String, Object>> selectLatestRecordInHour24();
	    
	    DbJiangyin selectLatestRecordInHour();

	    public List<Map<String, Object>> selectLatestRecordMinute100();
}
