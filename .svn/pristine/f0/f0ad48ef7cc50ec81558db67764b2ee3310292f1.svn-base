package com.mktech.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mktech.service.ChartService;
import com.mktech.utils.CommonUtil;
/**
 * 
 * @author gumpgan
 *
 */
@Controller
@RequestMapping(value = "/chart")
public class ChartController {
	
	@Resource
	private ChartService chartService;
	
	//获得设备状态监控自定义配置
	@RequestMapping(value = {"/getConfig"}, method = {RequestMethod.GET})
	public String getConfig(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = chartService.selectAllConfigByLine(ticket);
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	//更新曲线配置
	@RequestMapping(value = {"/updateConfig"}, method = {RequestMethod.POST})
	public String updateConfig(HttpServletRequest request,
						HttpServletResponse response,
						@RequestBody Map<String , String> map ,
						@CookieValue("ticket") String ticket
						) throws IOException{
		/*System.out.println(stats);
		System.out.println(pars);*/
		System.out.println(map);
		String position=map.get("position");
		String type=map.get("type");
		String chartids=map.get("chartids");
		String chartnames=map.get("chartnames");
		System.out.println(chartids);
		String i = chartService.updateConfigByLine(position,type, chartids, chartnames,ticket);
		System.out.println(i);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
}
