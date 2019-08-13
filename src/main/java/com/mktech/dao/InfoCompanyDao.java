package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.InfoCompany;

@Repository
public interface InfoCompanyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InfoCompany record);

    int insertSelective(InfoCompany record);

    InfoCompany selectByPrimaryKey(Integer id);
    
    
    InfoCompany selectByXY(@Param("x") float x,@Param("y") float y);
   
    List<Map<String, Object>> selectAll();
    
    List<Map<String, Object>> selectAllCompany();
    
    List<Map<String, Object>> selectSelfCompanyById(Integer id);

    int updateByPrimaryKeySelective(InfoCompany record);

    int updateByPrimaryKey(InfoCompany record);


    
    
}