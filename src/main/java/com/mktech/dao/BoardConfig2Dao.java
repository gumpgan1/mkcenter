package com.mktech.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mktech.entity.BoardConfig2;
@Repository
public interface BoardConfig2Dao {

	List<Map<String, Object>> selectConfigByCode(@Param("userId") int userId, @Param("code") String code);

	List<Map<String, Object>> selectConfig(int userId);
	void insertConfigByCode(BoardConfig2 bd2);

	void updateConfigByCode(BoardConfig2 bd2);




}
