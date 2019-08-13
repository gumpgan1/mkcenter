package com.mktech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbJiangyinLogDao;
import com.mktech.entity.DbJiangyinLog;
import com.mktech.service.DbJiangyinLogService;

/**
 * 
 * @author gumpgan
 *
 */

@Service
public class DbJiangyinLogServiceImpl implements DbJiangyinLogService {

	@Resource
	private DbJiangyinLogDao dbJiangyinLogMapper;
	
	@Override
	public void insert(DbJiangyinLog dbJiangyinLog) {
		//System.out.println("调用了service方法");
		dbJiangyinLogMapper.insert(dbJiangyinLog);
		//往另一个监控变量变化表里注入时间戳
	}

	@Override
	public DbJiangyinLog selectNew() {
		//System.out.println("调用了select方法");
		return dbJiangyinLogMapper.selectNew();
	}
	
	
}
