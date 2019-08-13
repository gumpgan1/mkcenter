package com.mktech.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.mktech.entity.DbJiangyinLogMnt;
/**
 * 
 * @author gumpgan
 *
 */

public interface DbJiangyinLogMntService {
	
	void insert(DbJiangyinLogMnt dbJiangyinLogMnt);
	
	void update(DbJiangyinLogMnt dbJiangyinLogMnt);
	
	void updatePost60s(DbJiangyinLogMnt dbJiangyinLogMnt);
	
	DbJiangyinLogMnt selectNew();
	
	List<DbJiangyinLogMnt> selectLatest3();
}
