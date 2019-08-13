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
import com.mktech.entity.SnUser;
import com.mktech.service.WxService;
/**
 * 
 * @author gumpgan
 *
 */
@Service
public class WxServiceImpl implements WxService{

	@Resource
	private SnUserDao snUserMapper;
	
	@Resource
	private InfoLineDao infoLineMapper;
	
	@Resource
	private InfoStationDao infoStationMapper;
	
	@Resource
	private InfoCompanyDao infoCompanyMapper;
	
	@Override
	public List<Map<String, Object>> selectCompanyLine(Integer companyId, Integer roleId) {
		try {
			if(roleId== 1){
				return infoLineMapper.selectCompanyLineByCompanyId(companyId);
			}else if(roleId == 99){
				return infoLineMapper.selectAllLines();
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

}
