package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbJiangyinLogDao;
import com.mktech.dao.DbJiangyinLogMntDao;
import com.mktech.entity.DbJiangyinLog;
import com.mktech.entity.DbJiangyinLogMnt;
import com.mktech.service.DbJiangyinLogMntService;
import com.mktech.service.DbJiangyinLogService;


/**
 * 
 * @author gumpgan
 *
 */
@Service
public class DbJiangyinLogMntServiceImpl implements DbJiangyinLogMntService {

	@Resource
	private DbJiangyinLogMntDao dbJiangyinLogMntMapper;

	@Override
	public void insert(DbJiangyinLogMnt dbJiangyinLogMnt) {
		// TODO Auto-generated method stub
		dbJiangyinLogMntMapper.insert(dbJiangyinLogMnt);
	}

	@Override
	public DbJiangyinLogMnt selectNew() {
		// TODO Auto-generated method stub
		return dbJiangyinLogMntMapper.selectNew();
	}

	@Override
	public void update(DbJiangyinLogMnt dbJiangyinLogMnt) {
		// TODO Auto-generated method stub
		dbJiangyinLogMntMapper.update(dbJiangyinLogMnt);
	}

	@Override
	public List<DbJiangyinLogMnt> selectLatest3() {
		// TODO Auto-generated method stub
		return dbJiangyinLogMntMapper.selectLatest3();
	}

	@Override
	public void updatePost60s(DbJiangyinLogMnt dbJiangyinLogMnt) {
		// TODO Auto-generated method stub
		dbJiangyinLogMntMapper.updatePost60s(dbJiangyinLogMnt);
		
	}
	
	

}
