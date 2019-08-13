/**
 * @author Chnyge Lin
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import com.mktech.entity.DbDaying1;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 *
 */
public interface DbDaying1Service {
	int deleteByPrimaryKey(Integer id);

	    int insert(DbDaying1 record);

	    int insertSelective(DbDaying1 record);

	    DbDaying1 selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(DbDaying1 record);

	    int updateByPrimaryKey(DbDaying1 record);
	    
	    public List<Map<String, Object>> selectLatestRecord100();
	    
	    public List<Map<String, Object>> selectLatestRecord();
}
