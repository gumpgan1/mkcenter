package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.BoardConfig;

@Repository
public interface BoardConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BoardConfig record);

    int insertSelective(BoardConfig record);

    BoardConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoardConfig record);

    int updateByPrimaryKey(BoardConfig record);
    
    List<Map<String, Object>> selectConfigByCode(@Param("lineId") int lineId,@Param("code") String code);
    
    List<Map<String, Object>> selectDefaultConfigByLine(int lineId);
    
    List<Map<String, Object>> selectDefaultConfigYwByLine(int lineId);
   
}