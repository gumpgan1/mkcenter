package com.mktech.service;

import java.util.List;
import java.util.Map;

public interface ChartService {

	String updateConfigByLine(String position, String type, String chartids, String chartnames, String ticket);

	List<Map<String, Object>> selectAllConfigByLine(String ticket);
}
