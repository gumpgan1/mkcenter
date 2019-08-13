package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface DbJiangyinPredictDao {
	
	List<Map<String, Object>> selectLatestPredictRecord100();

	List<Map<String, Object>> selectLatestRecord();
}
