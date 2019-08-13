package com.mktech.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mktech.service.ChartService;
import com.mktech.dao.BoardConfig2Dao;
import com.mktech.dao.ChartConfigDao;
import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.ChartConfig;

/**
 * 
 * @author gumpgan
 *
 */

@Service
public class ChartServiceImpl implements ChartService {

	@Resource
	private ChartConfigDao chartMapper;
	
	@Resource
	private SnUserTicketDao snUserTicketMapper;
	
	@Resource
	private SnUserDao snUserMapper;
	
	@Override
	public String updateConfigByLine(String position, String type, String chartids, String chartnames, String ticket) {
		/**/
			System.out.println("成功进入impl");
			int userId = snUserTicketMapper.selectByTicket(ticket).getUserid();
			int lineId = snUserMapper.selectByPrimaryKey(userId).getLineid();
			List<Map<String, Object>> list = chartMapper.selectConfigByLine(lineId,position);
			/*System.out.println(stats);
			System.out.println(lineId);
			System.out.println(list);
			System.out.println(pars);*/
			if(list.size() == 0) {
				System.out.println("成功进入判断");
				ChartConfig cc = new ChartConfig();
				cc.setPosition(position);
				cc.setLineId(lineId);
				cc.setType(type);
				cc.setChartid(chartids);
				cc.setChartname(chartnames);
				System.out.println("set成功");
				chartMapper.insertConfigByLine(cc);
				return "0";
			}else {
				ChartConfig cc1 = new ChartConfig();
				cc1.setPosition(position);
				cc1.setLineId(lineId);
				cc1.setChartid(chartids);
				cc1.setType(type);
				cc1.setChartname(chartnames);
				chartMapper.updateConfigByLine(cc1);
				return "0";
			}
		}

	@Override
	public List<Map<String, Object>> selectAllConfigByLine(String ticket) {
		System.out.println("成功进入get的impl");
		int userId = snUserTicketMapper.selectByTicket(ticket).getUserid();
		int lineId = snUserMapper.selectByPrimaryKey(userId).getLineid();
		List<Map<String, Object>> list = chartMapper.selectAllConfigByLine(lineId);
		if(list.size()==0){
			return null;
		}else{
			return list;
		}

	} 
	

}
