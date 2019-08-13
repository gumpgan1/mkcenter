/**
 * @author Chnyge Lin
 * @time 2018-8-1
 * @comment 
 */
package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.InfoCompanyDao;
import com.mktech.dao.InfoLineDao;
import com.mktech.dao.InfoStationDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.InfoLine;
import com.mktech.entity.InfoStation;
import com.mktech.entity.SnUser;
import com.mktech.service.InfoLineService;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 * @time 2018-8-1
 * @comment 
 */
@Service
public class InfoLineServiceImpl implements InfoLineService {
	
	@Resource
	private InfoLineDao infoLineMapper;
	
	@Resource
	private SnUserDao snUserMapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private InfoStationDao infoStationMapper;
	
	@Resource
	private InfoCompanyDao infoCompanyMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(InfoLine record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(InfoLine record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InfoLine selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return infoLineMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(InfoLine record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(InfoLine record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public List<Map<String, Object>> selectAllWx(){
		return infoLineMapper.selectAll();
	}
	
	public List<Map<String, Object>> selectAll(String ticket){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			if(user.getRole() == 1){
				int companyid = user.getCompanyid();
				return infoLineMapper.selectCompanyAll(companyid);
			}else if(user.getRole() == 99){
				return infoLineMapper.selectAll();
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Map<String, Object>> selectByCompany(int companyId){
		return infoLineMapper.selectByCompany(companyId);
	}
	
	public List<Map<String, Object>> selectById(int id){
		return infoLineMapper.selectById(id);
	}
	
	public int updateByTask(InfoLine record) {
		return infoLineMapper.updateByTask(record);
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
	
	public List<Map<String, Object>> getAllLine(String ticket){
		try {
			if(checkAuth(ticket)){
				return infoLineMapper.selectAllLines();
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}	 
	}
	
	public List<Map<String, Object>> getCompanyLine(String ticket) {
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			if(user.getRole() == 1){
				int companyid = user.getCompanyid();
				return infoLineMapper.selectCompanyLineByCompanyId(companyid);
			}else if(user.getRole() == 99){
				return infoLineMapper.selectAllLines();
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}	
	}
	
	public String createLine(String ticket,int companyid,String instruction,String info,String shaks,String eners){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			int compid = user.getCompanyid();
			if(user.getRole() == 99 || (user.getRole() == 1 && compid == companyid)){
				InfoLine i = infoLineMapper.checkLineExist(companyid, instruction);
				if(i==null){
					InfoLine tmp = new InfoLine();
					tmp.setCompanyId(companyid);
					tmp.setInstruction(instruction);
					tmp.setStatus(0);
					tmp.setInfo(info);
					tmp.setShaks(shaks);
					tmp.setEners(eners);
					tmp.setLastUpdate("0");
					infoLineMapper.insert(tmp);
					return "0";
				}else{
					return "已有相同记录!";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}
	public String changeLine(String ticket,int id,String instruction,String info,String shaks,String eners){
		try {
			if(checkAuth(ticket)){
				InfoLine tmp = infoLineMapper.selectByPrimaryKey(id);
				if(tmp!=null){
					InfoLine i = infoLineMapper.checkLineExist(tmp.getCompanyId(), instruction);
					if(i==null||i.getId()==id){
						tmp.setInstruction(instruction);
						tmp.setInfo(info);
						tmp.setShaks(shaks);
						tmp.setEners(eners);
						infoLineMapper.updateLine(tmp);
						return "0";
					}else{
						return "已有相同记录!";
					}
				}else{
					return "找不到该记录!";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "更改失败！";
		}	 
	}
	public String deleteLine(String ticket,int id){
		try {
			if(checkAuth(ticket)){
				InfoLine i = infoLineMapper.selectByPrimaryKey(id);
				if(i!=null){
					InfoStation tmp = infoStationMapper.checkifExist(id);
					if(tmp==null){
						infoLineMapper.deleteByPrimaryKey(id);
						return "0";
					}else{
						return "存在对应工位记录，请先删除工位后再试！";
					}
				}else{
					return "找不到该记录!";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	 
	}



}
