package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.ChartConfig;
@Repository
public interface ChartConfigDao {

/*	List<Map<String, Object>> selectConfigByCode(@Param("userId") int userId, @Param("code") String code);

	List<Map<String, Object>> selectConfig(int userId);*/
	
	void insertConfigByLine(ChartConfig cc);

	void updateConfigByLine(ChartConfig cc);

	List<Map<String, Object>> selectConfigByLine(@Param("lineId")int lineId,@Param("position")String position);

	List<Map<String, Object>> selectAllConfigByLine(int lineId);



	


}
