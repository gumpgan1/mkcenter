/**
 * @author Chnyge Lin
 * @time 2018-8-3
 * @comment 
 */
package com.mktech.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import com.mktech.dao.AlarmTextDao;
import com.mktech.dao.DbDaying1Dao;
import com.mktech.dao.DbJiangyinDao;
import com.mktech.dao.InfoCompanyDao;
import com.mktech.dao.InfoLineDao;
import com.mktech.dao.InfoStationDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.AlarmText;
import com.mktech.entity.DbJiangyin;
import com.mktech.entity.InfoLine;
import com.mktech.entity.InfoStation;
import com.mktech.entity.SnUser;
import com.mktech.service.InfoStationService;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 * @time 2018-8-3
 * @comment 
 */
@Service
public class InfoStationServiceImpl implements InfoStationService {
	
	@Resource
	private InfoStationDao infoStationMapper;
	
	@Resource
	private SnUserDao snUserMapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private InfoLineDao infoLineMapper;
	
	@Resource
	private InfoCompanyDao infoCompanyMapper;
	
	@Resource
	private DbJiangyinDao dbJiangyinMapper;
	
	@Resource
	private DbDaying1Dao dbDaying1Mapper;
	
	@Resource
	private AlarmTextDao alarmTextMapper;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(InfoStation record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(InfoStation record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InfoStation selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(InfoStation record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(InfoStation record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<Map<String, Object>> selectByLine(Integer lineId){
		return infoStationMapper.selectByLine(lineId);
	}
	/**
	 * 检查是否有权限进行用户管理(要求为非普通用户)
	 * @param ticket
	 * @return
	 */
	public boolean checkAuth(String ticket){
		int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
		if(snUserMapper.selectByPrimaryKey(id).getRole()>0){
			return true;
		}else{
			return false;
		}
	}
	public List<Map<String, Object>> getAllStation(String ticket){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			if(user.getRole() == 99){
				return infoStationMapper.getAllLines();
			}else if(user.getRole() == 1) {
				int companyid = user.getCompanyid();
				return infoStationMapper.getCompanyStation(companyid);
			}
			else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}	 
	}
	
	public String createStation(String ticket,int lineid,String statid,
			String name,String nickname,String unit,String info,double min,double max,String relate1n,String relate1v,String relate2n,String relate2v,int abnormal ){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user1 = snUserMapper.selectByPrimaryKey(id);
			Integer compid = user1.getCompanyid(); //用户所在企业ID
			InfoLine line = infoLineMapper.selectByPrimaryKey(new Integer(lineid));
			Integer companyId = line.getCompanyId();    //产线所在企业ID
			
			if(user1.getRole()==99 ||(user1.getRole()==1 && compid == companyId )){
				InfoStation i = infoStationMapper.check(lineid, statid);
				if(i==null){
					InfoStation tmp = new InfoStation();
					tmp.setLineId(lineid);
					tmp.setStatId(statid);
					tmp.setName(name);
					tmp.setNickname(nickname);
					tmp.setUnit(unit);
					tmp.setInstruction(info);
					tmp.setMaxValue(max+(max-min)*0.1);
					tmp.setMinValue(min-(max-min)*0.1);
					tmp.setMaxThreshold(max);
					tmp.setMinThreshold(min);
					tmp.setRelate1(relate1n+","+relate1v);
					tmp.setRelate2(relate2n+","+relate2v);
					tmp.setAbnormal(abnormal);
					infoStationMapper.insert(tmp);
					return "0";
				}else{
					return "已存在相同记录，请修改工位号！";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}
	public String changeStation(String ticket,int id,
			String name,String nickname, String unit,String info,double min,double max,String relate1n,String relate1v,String relate2n,String relate2v,int abnormal){
		try {
			if(checkAuth(ticket)){
				InfoStation tmp = infoStationMapper.selectByPrimaryKey(id);
				if(tmp!=null){
					tmp.setName(name);
					tmp.setNickname(nickname);
					tmp.setUnit(unit);
					tmp.setInstruction(info);
					tmp.setMaxValue(max+(max-min)*0.1);
					tmp.setMinValue(min-(max-min)*0.1);
					tmp.setMaxThreshold(max);
					tmp.setMinThreshold(min);
					tmp.setRelate1(relate1n+","+relate1v);
					tmp.setRelate2(relate2n+","+relate2v);
					tmp.setAbnormal(abnormal);
					infoStationMapper.updateStat(tmp);
					return "0";
				}else{
					return "找不到该记录！";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}
	public String deleteStation(String ticket,int id){
		try {
			if(checkAuth(ticket)){
				InfoStation i = infoStationMapper.selectByPrimaryKey(id);
				if(i!=null){
					infoStationMapper.deleteByPrimaryKey(id);
					return "0";
				}else{
					return "不存在该记录，无法删除！";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}

	public List<String> getAbnormalInfo(int lineId) {
		List<Map<String, Object>> list = infoStationMapper.selectNeedAbStations(lineId);
		List<String> lest = new ArrayList<String>();
		Map<String, Object> jm = null;
		if(lineId==5) {
			jm = dbJiangyinMapper.selectLatestRecordInMinByMap();
		}else if(lineId ==3) {
			jm = dbDaying1Mapper.selectLatestRecordInMinByMap();
		}
		
		
		
			for (Map<String, Object> map : list) {
				 String stat_id = (String) map.get("STAT_ID"); //L00..
				 System.out.println(stat_id);
				 Double num = (Double) jm.get(stat_id);
				 System.out.println(num);
				 if(num < (Double)map.get("MIN_THRESHOLD")){
					 String str =  (String)map.get("NAME") + "值低于下限" ;
					 lest.add(str);				 
				 }else if(num > (Double)map.get("MAX_THRESHOLD")){					 
					 String str =  (String)map.get("NAME") + "值超过上限";
					 lest.add(str);;
				 }else {
					 lest.add(null);
				 }
			}
		
		return lest;
	}
	
	public List<Map<String, Object>>  getAbnormalLog (int lineId){
		
		
		return alarmTextMapper.selectByLine(lineId);
		
	}

	
	public String abCalculate(String ticket, String max, String min,int id,String name) {
		Double max1 = Double.parseDouble(max);
		Double min1 = Double.parseDouble(min);
		System.out.println("成功进入service方法");
		try {
			if(checkAuth(ticket)){
				InfoStation tmp = infoStationMapper.selectByPrimaryKey(id);
				if(tmp!=null){
					if(name.indexOf("振动")!=-1) {
						tmp.setMaxThreshold(max1);
						tmp.setMinThreshold(0.0);
					}else {
						tmp.setMaxThreshold(max1);
						tmp.setMinThreshold(min1);
					}
					
					infoStationMapper.updateStat(tmp);
					return "0";
				}else{
					return "找不到该记录！";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}



}
