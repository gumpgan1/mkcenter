/**
 * @author Chnyge Lin
 * @time 2018-8-1
 * @comment 
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import com.mktech.entity.InfoLine;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * @time 2018-8-1
 * @comment 
 */
public interface InfoLineService {
	int deleteByPrimaryKey(Integer id);

    int insert(InfoLine record);

    int insertSelective(InfoLine record);

    InfoLine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InfoLine record);

    int updateByPrimaryKey(InfoLine record);
}
