
package com.mktech.service.impl;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mktech.dao.InfoCompanyDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserDetailDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.InfoCompany;
import com.mktech.entity.SnUser;
import com.mktech.entity.SnUserDetail;
import com.mktech.entity.SnUserTicket;
import com.mktech.service.SnUserService;
import com.mktech.utils.MD5Util;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 * 
 */
@Service
public class SnUserServiceImpl implements SnUserService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SnUserServiceImpl.class);

	@Resource
	private SnUserDao snUserMapper;
	
	@Resource
	private InfoCompanyDao infoCompanyMapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private SnUserDetailDao snUserDetailMapper;

	@Override
	public Map<String, String> register(String username, String password) {

		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		// 判断是否已经存在相同用户
		SnUser user = snUserMapper.selectByUsername(username);
		if (user != null) {
			map.put("msg", "用户名已存在");
			return map;
		}

		SnUser snUser = new SnUser();
		snUser.setUsername(username);
		snUser.setSalt(UUID.randomUUID().toString().substring(0, 5));
		snUser.setPassword(MD5Util.MD5(password + snUser.getSalt()));
		try {
			snUserMapper.insert(snUser);
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			map.put("msg", "创建失败");
			return map;
		}

	}

	@Override
	public Map<String, String> login(String username, String password) {

		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		// 判断是否存在用户
		SnUser user = snUserMapper.selectByUsername(username);
		if (user == null) {
			map.put("msg", "该用户不存在");
			return map;
		}
		
		if(!MD5Util.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("msg", "密码错误");
			return map;
		}
		String ticket = addLoginTicket(user.getUserid());
		map.put("ticket", ticket);
		return map;
	}

	public int setPassword(String ticket,String oldString,String newString){
		try {
			SnUser user = snUserMapper.selectByPrimaryKey(snUserTicketMapper.selectByTicket(ticket).getUserid());
			if(MD5Util.MD5(oldString+user.getSalt()).equals(user.getPassword())){
				String regix = "^\\w{6,16}$";
			    if(Pattern.matches(regix, newString)){
			    	//新密码满足正则，且能找到相应记录
			    	user.setSalt(UUID.randomUUID().toString().substring(0, 5));
					user.setPassword(MD5Util.MD5(newString+user.getSalt()));
					snUserMapper.updateByPrimaryKey(user);
					return 1;
			    }else{
			    	return 500;
			    }
			}else{
				return 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 500;
		}
	}
	
	public int setSelfinfo(String ticket,String name,String sex,String tel,String email,String address,String ins,byte[] file){
		try {
			SnUserDetail detail = snUserDetailMapper.selectByUser(snUserTicketMapper.selectByTicket(ticket).getUserid());
			detail.setName(name);
			detail.setSex(sex);
			detail.setPhone(tel);
			detail.setEmail(email);
			detail.setAddress(address);
			detail.setInstruction(ins);
			detail.setPhoto(file);
			snUserDetailMapper.updateByPrimaryKeyWithBLOBs(detail);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			return 500;
		}
	}
	
	public int setSelfinfowithoutFile(String ticket,String name,String sex,String tel,String email,String address,String ins){
		try {
			SnUserDetail detail = snUserDetailMapper.selectByUser(snUserTicketMapper.selectByTicket(ticket).getUserid());
			detail.setName(name);
			detail.setSex(sex);
			detail.setPhone(tel);
			detail.setEmail(email);
			detail.setAddress(address);
			detail.setInstruction(ins);
			snUserDetailMapper.updateByPrimaryKeyWithBLOBs(detail);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			return 500;
		}
	}
	@Override
	public String addLoginTicket(int userId) {
		SnUserTicket sut = new SnUserTicket();
		sut.setUserid(userId);
		Date now = new Date();
		now.setTime(3600*24*1000+now.getTime());
		sut.setExipred(now);
		sut.setStatus(0);
		sut.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		snUserTicketMapper.insert(sut);
		return sut.getTicket();
		
	}

	@Override
	public void logout(String ticket) {
		snUserTicketMapper.updateStatusForLoggout(ticket);
	}

	@Override
	public SnUser getUserByTicket(String ticket) {
		// TODO Auto-generated method stub
		SnUser snUser = snUserMapper.selectByPrimaryKey(snUserTicketMapper.selectByTicket(ticket).getUserid());
		return snUser;
	}
	
	public SnUser selectById(int id){
		return snUserMapper.selectByPrimaryKey(id);
	}
	
	public List<Map<String, Object>> selectUser(String ticket){
		return snUserMapper.selectUser(snUserTicketMapper.selectByTicket(ticket).getUserid());
	}
	
	public List<Map<String, Object>> selectAll(String ticket){
		int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
		if(snUserMapper.selectByPrimaryKey(id).getRole()!=99){
			return null;
		}else{
			return snUserMapper.selectAll();
		}	
	}
	
	public List<Map<String, Object>> selectCompanyUser(String ticket){
		int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
		SnUser user = snUserMapper.selectByPrimaryKey(id);
		if(user.getRole() ==1){
			int companyid = user.getCompanyid();
			return snUserMapper.selectUserByCompany(companyid);
		}else if(user.getRole() == 99){
			return snUserMapper.selectAll();
		}else {
			return null;
		}
	}
	
	/**
	 * 检查是否有权限进行用户管理（要求为管理员）
	 * @param ticket
	 * @return
	 */
	public boolean checkAuth(String ticket){
		int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
		if(snUserMapper.selectByPrimaryKey(id).getRole()==99 || snUserMapper.selectByPrimaryKey(id).getRole()== 1 ){
			return true;
		}else{
			return false;
		}
	}
	
	public String createUser(String ticket,String username,int role, int companyid ,int lineid){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user1 = snUserMapper.selectByPrimaryKey(id);
					
			if(user1.getRole()==99 ||(user1.getRole()==1 && role ==0)){
				SnUser user = snUserMapper.selectByUsername(username);
				if (user != null) {
					return "用户名已存在！";
				}else{
					SnUser snUser = new SnUser();					
					snUser.setUsername(username);					
					snUser.setRole(role);					
					snUser.setCompanyid(companyid);
					snUser.setLineid(lineid);
					snUser.setSalt(UUID.randomUUID().toString().substring(0, 5));					
					snUser.setPassword(MD5Util.MD5("123456" + snUser.getSalt()));
					snUserMapper.insert(snUser);
					//System.out.println("7");
					return "0";
				}
			}else{
				return "权限不足，请使用管理员身份操作!";
			}
		} catch (Exception e) {
			return "创建失败！";
		}	
	}
	
	public String updateUser(int userid,String ticket,String username,int role){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user1 = snUserMapper.selectByPrimaryKey(id);
			if(user1.getRole() == 99){
				SnUser user = snUserMapper.selectByUsername(username);
				if (user != null && userid!=user.getUserid()) {
					return "用户名已存在！";
				}else{
					SnUser snUser = snUserMapper.selectByPrimaryKey(userid);
					snUser.setUsername(username);
					snUser.setRole(role);
					snUserMapper.updateByPrimaryKey(snUser);
					return "0";
				}
			}else if(user1.getRole() == 1 && role == 0) {
				SnUser user = snUserMapper.selectByUsername(username);
				if(user.getRole() == 1) {
					return "权限不足,无法操作";
				}else {
					if (user != null && userid!=user.getUserid()) {
						return "用户名已存在！";
					}else{
						SnUser snUser = snUserMapper.selectByPrimaryKey(userid);
						snUser.setUsername(username);
						snUser.setRole(role);
						snUserMapper.updateByPrimaryKey(snUser);
						return "0";
					}
				}
				
			}
			else{
				return "权限不足，请使用管理员身份操作!";
			}
		} catch (Exception e) {
			return "更新失败!";
		}
	}
	
	public String deleteUser(String ticket,int userid){
		try {
			int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
			SnUser user1 = snUserMapper.selectByPrimaryKey(id);
			if(user1.getRole() == 99){
				snUserMapper.deleteByPrimaryKey(userid);
				return "0";
			}else if(user1.getRole() == 1) {
				SnUser user = snUserMapper.selectByPrimaryKey(userid);
				if (user == null) {
					return "未找到该用户!";
				}else{
					if(user.getRole() == 1){
						return "权限不足,无法操作";
					}else{
						snUserMapper.deleteByPrimaryKey(userid);
						return "0";
					}
				}
			}
			else{
				return "权限不足，请使用管理员身份操作!";
			}
		} catch (Exception e) {
			return "删除失败!";
		}
	}

	public Map<String, Integer> checkLine(String username) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		SnUser user = snUserMapper.selectByUsername(username);
		if(user.getRole() == 99) {
			map.put("99",99);
			return map;
		}
		if(user.getRole() == 1) {
			map.put("1", 1);
			map.put("lineId", user.getLineid());
			return map;
		}
		if(user.getRole() == 0) {
			map.put("0", 0);
			map.put("lineId", user.getLineid());
			return map;
		}
		return null;
	}
}
