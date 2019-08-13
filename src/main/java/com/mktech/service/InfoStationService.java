/**
 * @author Chnyge Lin
 * @time 2018-8-3
 * @comment 
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import com.mktech.entity.InfoStation;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * @comment 
 */
public interface InfoStationService {
	int deleteByPrimaryKey(Integer id);

    int insert(InfoStation record);

    int insertSelective(InfoStation record);

    InfoStation selectByPrimaryKey(Integer id);
    
    int updateByPrimaryKeySelective(InfoStation record);

    int updateByPrimaryKey(InfoStation record);
    
    String abCalculate(String ticket, String day, String sigma,int id,String name);




}
