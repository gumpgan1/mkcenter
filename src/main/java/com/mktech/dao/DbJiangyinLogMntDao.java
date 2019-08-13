package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mktech.entity.DbJiangyinLog;
import com.mktech.entity.DbJiangyinLogMnt;

@Repository
public interface DbJiangyinLogMntDao {

	void insert(DbJiangyinLogMnt dbJiangyinLogMnt);

	void update(DbJiangyinLogMnt dbJiangyinLogMnt);
	
	void updatePost60s(DbJiangyinLogMnt dbJiangyinLogMnt);

	DbJiangyinLogMnt selectNew();

	List<DbJiangyinLogMnt> selectLatest3();

	
	

}
