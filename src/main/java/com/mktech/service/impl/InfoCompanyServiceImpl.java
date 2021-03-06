/**
 * @author Chnyge Lin
 * @time 2018-7-31
 * @comment 
 */
package com.mktech.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.InfoCompanyDao;
import com.mktech.dao.InfoLineDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.InfoCompany;
import com.mktech.entity.SnUser;
import com.mktech.service.InfoCompanyService;
import com.mktech.utils.MD5Util;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 * @time 2018-7-31
 * @comment 
 */
@Service
public class InfoCompanyServiceImpl implements InfoCompanyService {
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private SnUserDao snUserMapper;
	
	@Resource
	private InfoCompanyDao infoCompanyMapper;
	
	@Resource
	private InfoLineDao infoLineMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(InfoCompany record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(InfoCompany record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InfoCompany selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public int updateByPrimaryKeySelective(InfoCompany record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(InfoCompany record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public List<Map<String, Object>> selectAll(){
		return infoCompanyMapper.selectAll();
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
	
	public List<Map<String, Object>> getAllCompany(String ticket){
		try {
			if(checkAuth(ticket)){
				return infoCompanyMapper.selectAllCompany();
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}	 
	}
	
	public List<Map<String, Object>> getSelfCompany(String ticket) {
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			if(user.getRole() == 1){
				int companyid = user.getCompanyid();
				return infoCompanyMapper.selectSelfCompanyById(companyid);
			}else if (user.getRole() == 99){ 
				return infoCompanyMapper.selectAllCompany();
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public String createCompany(String ticket,String name,String province,String city,String address,
			String phone,String href,String instruction,float x,float y){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user = snUserMapper.selectByPrimaryKey(id);
			if(user.getRole() == 99){
				 InfoCompany info = infoCompanyMapper.selectByXY(x, y);
				 if(info==null){
					 InfoCompany newinfo = new InfoCompany();
					 newinfo.setName(name);
					 newinfo.setAddress(province+city+address);
					 newinfo.setInstruction(instruction);
					 newinfo.setPhone(phone);
					 newinfo.setHref(href);
					 newinfo.setLocationX(x);
					 newinfo.setLocationY(y);
					 infoCompanyMapper.insert(newinfo);
					 return "0";
				 }else{
					 return "有重复的坐标，请稍作调整！";
				 }
			}else{
				return "权限不足，请使用设计人员及以上身份操作!";
			}
		} catch (Exception e) {
			return "创建失败";
		}	 
	}
	
	public String updateCompany(int id,String ticket,String name,String address,
			String phone,String href,String instruction,float x,float y){
		try {
			if(checkAuth(ticket)){
				 InfoCompany info = infoCompanyMapper.selectByXY(x, y);
				 if(info==null||info.getId()==id){
					 InfoCompany i = infoCompanyMapper.selectByPrimaryKey(id);
					 if(i!=null){
						 i.setName(name);
						 i.setAddress(address);
						 i.setPhone(phone);
						 i.setHref(href);
						 i.setInstruction(instruction);
						 i.setLocationX(x);
						 i.setLocationY(y);
						 infoCompanyMapper.updateByPrimaryKey(i);
						 return "0";
					 }else{
						 return "更新失败：查询不到该条记录！";
					 }
				 }else{
					 return "有重复的坐标，请稍作调整！";
				 }
			}else{
				return "权限不足，请使用设计人员及以上身份操作!";
			}
		} catch (Exception e) {
			return "更新失败";
		}	 
	}
	
	public String deleteCompany(String ticket,int id){
		try {
			if(checkAuth(ticket)){
				 InfoCompany info = infoCompanyMapper.selectByPrimaryKey(id);
				 if(info!=null){
					 if(infoLineMapper.checkIfExist(id)==null){
						  infoCompanyMapper.deleteByPrimaryKey(id);
						  return "0";
					 }else{
						 return "存在对应产线记录，请先删除产线后再试！";
					 }
					
				 }else{
					 return "无此记录！";
				 }
			}else{
				return "权限不足，请使用设计人员及以上身份操作!";
			}
		} catch (Exception e) {
			return "删除失败";
		}	 
	}




}
