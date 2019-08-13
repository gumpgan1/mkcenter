/**
 * @author Chnyge Lin
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import com.mktech.entity.DbDaying2;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 *
 */
public interface DbDaying2Service {
	 int deleteByPrimaryKey(Integer id);

	    int insert(DbDaying2 record);

	    int insertSelective(DbDaying2 record);

	    DbDaying2 selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(DbDaying2 record);

	    int updateByPrimaryKey(DbDaying2 record);
	    public List<Map<String, Object>> selectLatestRecord100();
	    
	    public List<Map<String, Object>> selectLatestRecord();
}
