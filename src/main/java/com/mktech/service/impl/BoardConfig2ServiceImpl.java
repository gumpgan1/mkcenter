package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.dao.BoardConfig2Dao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.BoardConfig2;
import com.mktech.entity.SnUser;
import com.mktech.service.BoardConfig2Service;

@Service
public class BoardConfig2ServiceImpl implements BoardConfig2Service {
	
	@Resource
	private BoardConfig2Dao boardConfig2Mapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private SnUserDao snUserMapper;

	@Override
	public List<Map<String, Object>> selectConfigByCode(String ticket , String code) {
		int userId = snUserTicketMapper.selectByTicket(ticket).getUserid();
		int lineId = snUserMapper.selectByPrimaryKey(userId).getLineid();
		List<Map<String, Object>> list = boardConfig2Mapper.selectConfigByCode(userId , code);
//		System.out.println(list.size());
		if(list.size()==0){
			return null;
		}else{
			return list;
		}
	}

	@Override
	public String updateConfigByCode(String ticket, int lineId, String code, String stats, String pars) {
			try {
				int userId = snUserTicketMapper.selectByTicket(ticket).getUserid();
				List<Map<String, Object>> list = boardConfig2Mapper.selectConfigByCode(userId, code);
				/*System.out.println(stats);
				System.out.println(lineId);
				System.out.println(list);
				System.out.println(pars);*/
				if(list.size() == 0) {
					BoardConfig2 bd21 = new BoardConfig2();
					bd21.setCode(code);
					bd21.setLineId(lineId);
					bd21.setPars(pars);
					bd21.setStas(stats);
					bd21.setUserid(userId);
					boardConfig2Mapper.insertConfigByCode(bd21);
					return "0";
				}else {
					BoardConfig2 bd22 = new BoardConfig2();
					bd22.setCode(code);
					bd22.setLineId(lineId);
					bd22.setPars(pars);
					bd22.setStas(stats);
					bd22.setUserid(userId);
					boardConfig2Mapper.updateConfigByCode(bd22);
					return "0";
				}
			} catch (Exception e) {
				return "更新失败";
			}
		
	}

	@Override
	public List<Map<String, Object>> selectConfig(int lineId, String ticket) {
		int userId = snUserTicketMapper.selectByTicket(ticket).getUserid();
		List<Map<String, Object>> list = boardConfig2Mapper.selectConfig(userId);
//		System.out.println(list.size());
		if(list.size()==0){
			return null;
		}else{
			return list;
		}
	}
	
/*	public boolean checkAuth(String ticket){
		int id = snUserTicketMapper.selectByTicket(ticket).getUserid();
		if(snUserMapper.selectByPrimaryKey(id).getRole()>0){
			return true;
		}else{
			return false;
		}
	}*/

}
