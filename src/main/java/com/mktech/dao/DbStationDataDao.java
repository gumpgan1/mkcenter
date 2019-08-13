package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mktech.entity.DbStationData;

@Repository
public interface DbStationDataDao {

	void insertStationData(DbStationData dbStationData);

	List<Map<String, Object>> getStationDataByLineId(int line_id);

	void deleteStationDataByLineId(int line_id);


}
