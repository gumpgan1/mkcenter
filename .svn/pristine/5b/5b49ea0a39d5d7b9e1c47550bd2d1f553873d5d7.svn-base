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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mktech.service.BoardConfig2Service;
import com.mktech.utils.CommonUtil;

/**
 * 设备状态监控页面
 * @author Sunzt
 *
 */
@Controller
@RequestMapping(value = "/board")
public class BoardController {
	
	@Resource
	private BoardConfig2Service boardConfig2Service;
	
	//获得设备状态监控自定义配置
	@RequestMapping(value = {"/getConfig"}, method = {RequestMethod.GET})
	public String getConfig(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam("line_id") int lineId,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = boardConfig2Service.selectConfig(lineId , ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	//更新设备状态监控自定义配置
	@RequestMapping(value = {"/updateConfig"}, method = {RequestMethod.POST})
	public String updateConfig(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam("code") String code,
						@RequestParam("stats") String stats,
						@RequestParam("pars") String pars,
						@RequestParam("line_id") int lineId,
						@CookieValue("ticket") String ticket
						) throws IOException{
		/*System.out.println(stats);
		System.out.println(pars);*/
		String i = boardConfig2Service.updateConfigByCode(ticket,lineId, code, stats, pars);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
}
