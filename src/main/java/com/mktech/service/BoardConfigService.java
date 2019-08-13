/**
 * @author Chnyge Lin
 * @time 2018-8-6
 * @comment 
 */
package com.mktech.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mktech.entity.BoardConfig;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * @time 2018-8-6
 * @comment 
 */
public interface BoardConfigService {
	int deleteByPrimaryKey(Integer id);

    int insert(BoardConfig record);

    int insertSelective(BoardConfig record);

    BoardConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoardConfig record);

    int updateByPrimaryKey(BoardConfig record);
    
    List<Map<String, Object>> selectConfigByCode(@Param("lineId") int lineid,@Param("code") String code);
}
