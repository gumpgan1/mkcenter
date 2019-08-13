package com.mktech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbJiangyinLogChangeDao;
import com.mktech.entity.DbJiangyinLogChange;
import com.mktech.service.DbJiangyinLogChangeService;

/**
 * 
 * @author gumpgan
 *
 */

@Service
public class DbJiangyinLogChangeServiceImpl implements DbJiangyinLogChangeService {

	@Resource
	private DbJiangyinLogChangeDao dbJangyinLogChangeMapper;
	
	@Override
	public void insert(DbJiangyinLogChange dbJiangyinLogChange) {
		dbJangyinLogChangeMapper.insert(dbJiangyinLogChange);
		
	}

}
