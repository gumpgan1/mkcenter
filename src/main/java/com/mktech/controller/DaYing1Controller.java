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
import com.mktech.service.impl.DbDaying1ServiceImpl;
import com.mktech.service.impl.InfoStationServiceImpl;
import com.mktech.utils.CommonUtil;

@Controller
@RequestMapping(value = "/daying1")
public class DaYing1Controller {
	
	@Resource
	private DbDaying1ServiceImpl dbDaying1Service;
	
	@Resource
	private InfoStationServiceImpl infoStationService;
	
	//获取大营产线的100条数据
	@RequestMapping(value = {"/get"},method = {RequestMethod.GET})
	public String getLimo500(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		String result = null;	
//		System.out.println(request.getContextPath());   /mkcenter
		List<Map<String, Object>> list = dbDaying1Service.selectLatestRecord100();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
		//获大营1产线最新数据
		@RequestMapping(value = {"/getNew"}, method = {RequestMethod.GET})
		public String getNew(HttpServletRequest request,
							HttpServletResponse response
							) throws IOException{
			String result = null;		
			List<Map<String, Object>> list = dbDaying1Service.selectLatestRecord();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}

		//获取运行异常信息
		@RequestMapping(value = {"/getAbnormalInfo"}, method = {RequestMethod.GET})
		public String getAbnormalInfo(HttpServletRequest request,
								HttpServletResponse response,
								@CookieValue("ticket") String ticket,
								@RequestParam("line_id") int lineId) throws IOException{
				String result = null;
				List<String> list = infoStationService.getAbnormalInfo(lineId);
				//System.out.println(list);
				Gson gson = new Gson();
				result = gson.toJson(list);
				CommonUtil.writeToWeb(result, "html", response);
				return null;
		}
		
		//获取业务信息
		@RequestMapping(value = {"/getyw"},method = {RequestMethod.GET})
		public String getJYIntoYewu(HttpServletRequest request,
								HttpServletResponse response) throws IOException {
			List<Map<String, Object>> list = dbDaying1Service.initYewu();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			String result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}
		
		//获取最新一周的情况
		@RequestMapping(value = {"/getLatestWeek"},method = {RequestMethod.GET})
		public String getLatest(HttpServletRequest request,
								HttpServletResponse response,
								@RequestParam("timestamp") Long timestamp) throws IOException {
			List<Map<String, Object>> list = dbDaying1Service.updateLatestByWeek(timestamp);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			String result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}
		
		@RequestMapping(value = {"/updateyw"},method = {RequestMethod.GET})
		public String updateJyIntoYewu(HttpServletRequest request,
								HttpServletResponse response,
								@RequestParam("start") long start,
								@RequestParam("end") long end) throws IOException {
			List<Map<String, Object>> list = dbDaying1Service.updateYewu(start+8*3600*1000, end+8*3600*1000);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			String result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}
		
		//获得时间戳范围
		@RequestMapping(value = {"/getRange"}, method = {RequestMethod.GET})
		public String getRange(HttpServletRequest request,
							HttpServletResponse response
							) throws IOException{
			String result = null;		
			List<Map<String, Object>> list = dbDaying1Service.selectRange();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}

		@RequestMapping(value = {"selectRange"},method = {RequestMethod.GET})
		public String selectRange(HttpServletRequest request,
								HttpServletResponse response,
								@RequestParam("start") long start,
								@RequestParam("end") long end) throws IOException {
			List<Map<String, Object>> list = dbDaying1Service.selectDataByRange(start+8*3600*1000, end+8*3600*1000);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			Gson gson = new Gson();
			String result = gson.toJson(map);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
		}
}
