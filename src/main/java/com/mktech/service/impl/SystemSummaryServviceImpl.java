/**
 * @author Chnyge Lin
 */
package com.mktech.service.impl;

import java.io.Console;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mktech.dao.DbDaying1Dao;
import com.mktech.dao.DbDaying2Dao;
import com.mktech.dao.DbJiangyinDao;
import com.mktech.dao.DbJuhuaDao;
import com.mktech.dao.DbJuhuaOldDao;
import com.mktech.dao.DbLimoDao;
import com.mktech.dao.DbShaochengDao;
import com.mktech.dao.ILineDao;
import com.mktech.dao.SystemSummaryDao;
import com.mktech.entity.DbDaying1;
import com.mktech.entity.DbDaying2;
import com.mktech.entity.DbJiangyin;
import com.mktech.entity.DbJuhua;
import com.mktech.entity.DbJuhuaOld;
import com.mktech.entity.DbLimo;
import com.mktech.entity.DbShaocheng;
import com.mktech.entity.Line;
import com.mktech.entity.SystemSummary;
import com.mktech.service.SystemSummaryService;
import com.mktech.utils.CommonUtil;

/**
 * @author Chnyge Lin, gumpgan, Sunzt
 *
 */
@Service
public class SystemSummaryServviceImpl implements SystemSummaryService{
	
	@Resource
	private SystemSummaryDao systemSummaryMapper;
	
	@Resource
	private DbLimoDao dbLimoMapper;
	
	@Resource
	private DbShaochengDao dbShaochengMapper;
	
	@Resource
	private DbDaying1Dao dbDaying1Mapper;
	
	@Resource
	private DbDaying2Dao dbDaying2Mapper;
	
	@Resource
	private DbJiangyinDao dbJiangyinMapper;
	
	@Resource
	private DbJuhuaDao dbJuhuaMapper;
	
	@Resource
	private DbJuhuaOldDao dbJuhuaOldMapper;
	
	@Override
	public SystemSummary CheckIfExist(String deviceid) {
		// TODO Auto-generated method stub
		return systemSummaryMapper.checkIfExist(deviceid);
	}
	
	
	public DbJuhuaOld checkIfExistTime(String timestamp) {
		// TODO Auto-generated method stub
		return dbJuhuaOldMapper.checkIfExistTime(timestamp);
	}
	
	
	@Override
	public String insertByDeviceId(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String deviceid = jsonObject.getString("deviceid");
		SystemSummary ss = systemSummaryMapper.checkIfExist(deviceid);

		try {
			if(ss == null){
				return "Warnning : cannot find responed system! ";
			}else{
				JSONObject messageObject = jsonObject.getJSONObject("message");
				JSONObject dataObject = messageObject.getJSONObject("data");
				String systemName = ss.getName();
				System.out.println("systemName="+systemName);
				if(systemName.equals("LIMO")){
					//插入方法----
					DbLimo dbLimo = (DbLimo) JSONObject.toJavaObject(dataObject, DbLimo.class);
					dbLimo.setTimestamp(jsonObject.getString("timestamp"));
					dbLimoMapper.insert(dbLimo);
					//更新summary表最近更新时间
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
					return systemName;
				}else if (systemName.equals("SHAOCHENG")){
					DbShaocheng dbShaocheng = (DbShaocheng)JSONObject.toJavaObject(dataObject, DbShaocheng.class);
					dbShaocheng.setTimestamp(jsonObject.getString("timestamp"));
					dbShaochengMapper.insert(dbShaocheng);
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));	
					return systemName;
				}else if (systemName.equals("DAYING_1")){
					DbDaying1 dbDaying1  = (DbDaying1) JSONObject.toJavaObject(dataObject, DbDaying1.class);
					dbDaying1.setTimestamp(jsonObject.getString("timestamp"));
					dbDaying1Mapper.insert(dbDaying1);
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
					return systemName;
				}else if (systemName.equals("DAYING_2")){
					DbDaying2 dbDaying2 = (DbDaying2) JSONObject.toJavaObject(dataObject,DbDaying2.class);
					dbDaying2.setTimestamp(jsonObject.getString("timestamp"));
					dbDaying2Mapper.insert(dbDaying2);
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
					return systemName;
				}else if (systemName.equals("JIANGYIN")){
					DbJiangyin dbJiangyin = (DbJiangyin) JSONObject.toJavaObject(dataObject,DbJiangyin.class);
					dbJiangyin.setTimestamp(jsonObject.getString("timestamp"));
					dbJiangyinMapper.insert(dbJiangyin);
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
					return systemName;
				}else if(systemName.equals("JUHUA")){
					DbJuhua dbJuhua = (DbJuhua) JSONObject.toJavaObject(dataObject,DbJuhua.class);
					dbJuhua.setTimestamp(jsonObject.getString("timestamp"));
					dbJuhuaMapper.insert(dbJuhua);
					systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
					return systemName;
				}else if(systemName.equals("JUHUAOLD")){
					String timestamp = jsonObject.getString("timestamp");
					DbJuhuaOld aa =dbJuhuaOldMapper.checkIfExistTime(timestamp);
					if(	aa==null){
					DbJuhuaOld dbJuhuaOld = (DbJuhuaOld) JSONObject.toJavaObject(dataObject,DbJuhuaOld.class);
					dbJuhuaOld.setTimestamp(jsonObject.getString("timestamp"));	
						dbJuhuaOldMapper.insert(dbJuhuaOld);
						systemSummaryMapper.updateLatestUpdateTimeByPrimaryKey(ss.getSystemId(), jsonObject.getString("timestamp"));
						return systemName;
					}else{
						systemName = "巨化重复";
						return systemName;
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return e.getCause().toString();
		}
		return null;
	
	
	}
}
