package com.mktech.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Request;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.mktech.service.DbJiangyinPredictService;
import com.mktech.utils.CommonUtil;

/**
 * 预测数据
 * @author Sunzt
 * @author gumpgan
 *
 */
@Controller
@RequestMapping(value = "/predict")
public class PredictController {
	
	@Resource
	private DbJiangyinPredictService dbJiangyinPredictService;
	
	@RequestMapping(value = {"/get100"},method = {RequestMethod.GET})
	public String getJinagyinPredict100new(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		String result = null;	
		List<Map<String, Object>> list = dbJiangyinPredictService.selectLatestPredictRecord100();
		System.out.println(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value= {"/getNew"},method = {RequestMethod.GET})
	public String getJiangyinPredictnew(HttpServletRequest request,
										HttpServletResponse response)throws IOException {
		String result = null;		
		List<Map<String, Object>> list = dbJiangyinPredictService.selectLatestRecord();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
		
	}
}
