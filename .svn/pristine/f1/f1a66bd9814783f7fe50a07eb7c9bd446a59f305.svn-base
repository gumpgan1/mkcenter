/**
 * @author Chnyge Lin
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.mktech.entity.DbLimo;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * 
 */
public interface DbLimoService {

	int deleteByPrimaryKey(Integer id);

	int insert(DbLimo record);

	int insertSelective(DbLimo record);

	DbLimo selectByPrimaryKey(Integer id);
	
	DbLimo selectNearestRecord(String timestamp);

	int updateByPrimaryKeySelective(DbLimo record);

	int updateByPrimaryKey(DbLimo record);

	int insertDataByJson(String json);
	
	int inserDataByJsonNew(JSONObject jsonObject);

	List<DbLimo> selectByKeyRange(Integer min, Integer max);

	List<Map<String, Object>> selectByKeyRangeIntoMap(Integer min, Integer max);

	int export2Excel(Integer min, Integer max);
	
	byte[] export2ExcelNotPernament(Integer min, Integer max);
}
