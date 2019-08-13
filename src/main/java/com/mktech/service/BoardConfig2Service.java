package com.mktech.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BoardConfig2Service {

	List<Map<String, Object>> selectConfig(int lineId , String ticket);
	
	List<Map<String, Object>> selectConfigByCode(String ticket , String code);

	String updateConfigByCode(String ticket , int lineId, String code, String stats, String pars);

}
