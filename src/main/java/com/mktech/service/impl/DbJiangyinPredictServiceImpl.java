package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbJiangyinPredictDao;
import com.mktech.service.DbJiangyinPredictService;

/**
 * 
 * @author gumpgan
 *
 */
@Service
public class DbJiangyinPredictServiceImpl implements DbJiangyinPredictService {

	@Resource
	private DbJiangyinPredictDao dbJiangyinPredictMapper;
	
	@Override
	public List<Map<String, Object>> selectLatestPredictRecord100() {
		return dbJiangyinPredictMapper.selectLatestPredictRecord100();
	}
	
	@Override
	public List<Map<String, Object>> selectLatestRecord() {
		// TODO Auto-generated method stub
		return dbJiangyinPredictMapper.selectLatestRecord();
	}
	
}
