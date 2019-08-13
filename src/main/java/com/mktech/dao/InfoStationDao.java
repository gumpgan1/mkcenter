package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.InfoStation;

@Repository
public interface InfoStationDao {


	int deleteByPrimaryKey(Integer id);

	    int insert(InfoStation record);

	    int insertSelective(InfoStation record);

	    InfoStation selectByPrimaryKey(Integer id);
	    
	    InfoStation checkifExist(Integer id);
	    
	    List<Map<String, Object>> selectByLine(Integer lineId);

	    int updateByPrimaryKeySelective(InfoStation record);

	    int updateByPrimaryKey(InfoStation record);
	    
	    int updateStat(InfoStation record);
	    
	    List<Map<String, Object>> getAllLines();
	    
	    InfoStation check(@Param("lineid") int lineid,@Param("statid") String stat_id);

		List<Map<String, Object>> getCompanyStation(int companyid);

		List<Map<String, Object>> selectNeedAbStations(int lineId);

		




}