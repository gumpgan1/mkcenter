
package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mktech.dao.BoardConfigDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.BoardConfig;
import com.mktech.entity.InfoLine;
import com.mktech.service.BoardConfigService;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 * @time 2018-8-6
 * @comment 
 * 
 */
@Service
public class BoardConfigServiceImpl implements BoardConfigService {
	
	@Resource
	private BoardConfigDao boardConfigMapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource 
	private SnUserDao snUserMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(BoardConfig record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(BoardConfig record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BoardConfig selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(BoardConfig record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(BoardConfig record) {
		// TODO Auto-generated method stub
		return 0;
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
	
	@Override
	public List<Map<String, Object>> selectConfigByCode(int lineid, String code) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = boardConfigMapper.selectConfigByCode(lineid, code);
//		System.out.println(list.size());
		if(list.size()==0){
			BoardConfig bc = new BoardConfig();
			bc.setLineId(lineid);
			bc.setCode(code);
			boardConfigMapper.insert(bc);
			return boardConfigMapper.selectConfigByCode(lineid, code);
		}else{
			return list;
		}
	}
	

	public String updateConfigByCode(String ticket,int id, String code,String stats,String pars) {
		try {
			if(checkAuth(ticket)){
				BoardConfig bc = boardConfigMapper.selectByPrimaryKey(id);
				if(bc.getCode().equals(code)){
					bc.setStas(stats);
					bc.setPars(pars);
					boardConfigMapper.updateByPrimaryKey(bc);
					return "0";
				}else{
					return "修改失败!";
				}
			}else{
				return "权限不足，需要设计人员及以上权限！";
			}
		} catch (Exception e) {
			return "更改失败！";
		}	 
	}
	
	
	
	public List<Map<String, Object>> selectDefaultConfigByLine(int lineId){
		return boardConfigMapper.selectDefaultConfigByLine(lineId);
	}
	
	public List<Map<String, Object>> selectYwConfigByLine(int lineId){
		return boardConfigMapper.selectDefaultConfigYwByLine(lineId);
	}
}
