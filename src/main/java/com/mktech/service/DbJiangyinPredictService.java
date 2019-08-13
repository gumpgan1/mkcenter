package com.mktech.service;

import java.util.List;
import java.util.Map;

public interface DbJiangyinPredictService {
	
	List<Map<String, Object>> selectLatestPredictRecord100();

	List<Map<String, Object>> selectLatestRecord();
}
