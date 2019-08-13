/**
 * @author Chnyge Lin
 */
package com.mktech.service;

import com.mktech.entity.DbLimo;
import com.mktech.entity.DbShaocheng;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 *
 */
public interface DbShaochengService {
	
    int deleteByPrimaryKey(Integer id);

    int insert(DbShaocheng record);

    int insertSelective(DbShaocheng record);

    DbShaocheng selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DbShaocheng record);

    int updateByPrimaryKey(DbShaocheng record);
    
    int insertDataByJson(String json);
}
