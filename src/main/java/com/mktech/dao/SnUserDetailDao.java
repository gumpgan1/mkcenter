package com.mktech.dao;

import com.mktech.entity.SnUserDetail;

public interface SnUserDetailDao {
	int deleteByPrimaryKey(Integer id);

    int insert(SnUserDetail record);

    int insertSelective(SnUserDetail record);

    SnUserDetail selectByPrimaryKey(Integer id);
    
    SnUserDetail selectByUser(Integer userid);

    int updateByPrimaryKeySelective(SnUserDetail record);

    int updateByPrimaryKeyWithBLOBs(SnUserDetail record);

    int updateByPrimaryKey(SnUserDetail record);
}