package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mktech.entity.SnUser;

@Repository
public interface SnUserDao {
    int deleteByPrimaryKey(Integer userid);

    int insert(SnUser record);

    int insertSelective(SnUser record);

    SnUser selectByPrimaryKey(Integer userid);
    
    SnUser selectByUsername(String username);

    int updateByPrimaryKeySelective(SnUser record);

    int updateByPrimaryKey(SnUser record);
    
    List<Map<String,Object>> selectUser(int userid);
    
    List<Map<String,Object>> selectAll();

	List<Map<String, Object>> selectUserByCompany(int companyid);
}