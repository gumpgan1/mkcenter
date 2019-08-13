package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.DbStationDataDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.DbStationData;
import com.mktech.service.DbStationDataService;
/**
 * 
 * @author gumpgan
 *
 */
@Service
public class DbStationDataServiceImpl implements DbStationDataService{
	@Resource
	private DbStationDataDao dbStationDataMapper;
	@Resource
	private SnUserDao snUserMapper;
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Override
	public String insertStationData(List<Map<String , String>> stationdata , String ticket) {
		try {
			int line_id = snUserMapper.selectByPrimaryKey(snUserTicketMapper.selectByTicket(ticket).getUserid()).getLineid();
			List<Map<String, Object>> list = dbStationDataMapper.getStationDataByLineId(line_id);
			if(list != null && !list.isEmpty()) {
				//list不为空,即存在产线原配置,先删除在保存
				dbStationDataMapper.deleteStationDataByLineId(line_id);
				//进行保存
				//System.out.println(stationdata+"2");
				for (Map<String, String> map : stationdata) {
					DbStationData dbStationData = new DbStationData();				
					dbStationData.setLine_id(line_id);
					dbStationData.setStation_id(map.get("station_id"));
					dbStationData.setPosition_left(map.get("position_left"));
					dbStationData.setPosition_top(map.get("position_top"));
					System.out.println(dbStationData);
					//System.out.println(map);
					dbStationDataMapper.insertStationData(dbStationData);
				}
				return "0";
			}else {
				//不存在,直接保存
				for (Map<String, String> map : stationdata) {
					DbStationData dbStationData = new DbStationData();
					dbStationData.setLine_id(line_id);
					dbStationData.setStation_id(map.get("station_id"));
					dbStationData.setPosition_left(map.get("position_left"));
					dbStationData.setPosition_top(map.get("position_top"));
					dbStationDataMapper.insertStationData(dbStationData);
				}
				return "0";
			}
		}
		catch(Exception e){
			return "創建失敗";
		}
	}

	@Override
	public List<Map<String, Object>> getStationDataByLineId(int line_id) {
		try {
				return dbStationDataMapper.getStationDataByLineId(line_id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void deleteStationDataByLineId(int line_id) {
		dbStationDataMapper.deleteStationDataByLineId(line_id);
		
	}
	
}