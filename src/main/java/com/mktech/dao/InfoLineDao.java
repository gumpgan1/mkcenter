package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.InfoLine;

@Repository
public interface InfoLineDao {
    int deleteByPrimaryKey(Integer id);

    int insert(InfoLine record);

    int insertSelective(InfoLine record);

    InfoLine selectByPrimaryKey(Integer id);
    
    InfoLine checkIfExist(Integer companyId);
    
    InfoLine checkLineExist(@Param("companyId") int companyid,@Param("instruction") String instruction);

    int updateByPrimaryKeySelective(InfoLine record);

    int updateByPrimaryKey(InfoLine record);
    
    int updateByTask(InfoLine record);
    
    int updateLine(InfoLine record);
    
    List<Map<String, Object>> selectAll();
    
    List<Map<String, Object>> selectAllLines();
    
    
    
    List<Map<String, Object>> selectByCompany(int companyId);
    
    List<Map<String, Object>> selectById(int id);

	List<Map<String, Object>> selectCompanyLineByCompanyId(int companyId);

	List<Map<String, Object>> selectCompanyAll(int companyid);

    
}