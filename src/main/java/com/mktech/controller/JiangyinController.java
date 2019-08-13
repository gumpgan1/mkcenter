/**
 * @author Chnyge Lin
 * @time 2018-8-22
 * @comment 
 */
package com.mktech.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.google.gson.Gson;
import com.mktech.entity.DbJiangyin;
import com.mktech.entity.DbStationData;
import com.mktech.entity.InfoStation;
import com.mktech.entity.SnUser;
import com.mktech.service.DbLimoService;
import com.mktech.service.DbStationDataService;
import com.mktech.service.InfoStationService;
import com.mktech.service.SnUserService;
import com.mktech.service.impl.DbJiangyinServiceImpl;
import com.mktech.service.impl.DbLimoServiceImpl;
import com.mktech.service.impl.InfoStationServiceImpl;
import com.mktech.utils.CommonUtil;
/**
 * @author Chnyge Lin, gump gan， szt
 * @time 2018-8-22
 * @comment 
 */

@Controller
@RequestMapping(value = "/jiangyin")
public class JiangyinController {
	
	@Resource
	private DbJiangyinServiceImpl dbJiangyinService;
	
	@Resource
	private DbStationDataService dbStationDataService;
	
	@Resource
	private InfoStationServiceImpl infoStationService;
	
	@Resource
	private SnUserService snUserService;	
	

	
	//获江阴最新数据
	@RequestMapping(value = {"/getNew"}, method = {RequestMethod.GET})
	public String getNew(HttpServletRequest request,
						HttpServletResponse response
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = dbJiangyinService.selectLatestRecord();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/get"},method = {RequestMethod.GET})
	public String getLimo500(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		String result = null;	
//		System.out.println(request.getContextPath());   /mkcenter
		List<Map<String, Object>> list = dbJiangyinService.selectLatestRecord500();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/get1"},method = {RequestMethod.GET})
	public String getLimo500new(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		String result = null;	
		List<Map<String, Object>> list = dbJiangyinService.selectLatestRecord500();
		System.out.println(list);
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value= {"/getminout100"}, method= {RequestMethod.GET})
	public String getminout100(HttpServletRequest request,
							  HttpServletResponse response)throws IOException {
		String result= null;
		List<Map<String,Object>> list = dbJiangyinService.selectLatestRecordMinute100();
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	@RequestMapping(value= {"/getminoutNew"}, method= {RequestMethod.GET})
	public String getminoutNew(HttpServletRequest request,
							  HttpServletResponse response)throws IOException {
		String result= null;
		Map<String,Object> list = dbJiangyinService.selectLatestRecordMinuteNew();
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	@RequestMapping(value= {"/gethourout"},method= {RequestMethod.GET})
	public String gethourout(HttpServletRequest request,
							HttpServletResponse response) throws IOException{
		String result=null;
		List<Map<String,Object>> list = dbJiangyinService.selectLatestRecordInHour24();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value= {"/gethourout1"},method= {RequestMethod.GET})
	public String gethourout1(HttpServletRequest request,
							HttpServletResponse response) throws IOException{
		String result=null;
		List<Map<String,Object>> list = dbJiangyinService.selectLatestRecordInHour24();
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	
	@RequestMapping(value= {"/addhourout"},method= {RequestMethod.GET})
	public String addhourout(HttpServletRequest request,
							HttpServletResponse response) throws IOException{
		String result=null;
		DbJiangyin list = dbJiangyinService.selectLatestRecordInHour();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		System.out.println(result);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	@RequestMapping(value = {"selectRange"},method = {RequestMethod.GET})
	public String selectRange(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("start") long start,
							@RequestParam("end") long end,
							@RequestParam("source") String source) throws IOException {
		List<Map<String, Object>> list = dbJiangyinService.selectDataByRange(start+8*3600*1000, end+8*3600*1000, source);
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
		List<Map<String, Object>> list = dbJiangyinService.selectRange();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/getyw"},method = {RequestMethod.GET})
	public String getJYIntoYewu(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = dbJiangyinService.initYewu();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		String result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	

	@RequestMapping(value = {"/getLatestWeek"},method = {RequestMethod.GET})
	public String getLatest(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("timestamp") Long timestamp) throws IOException {
		List<Map<String, Object>> list = dbJiangyinService.updateLatestByWeek(timestamp);
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
		List<Map<String, Object>> list = dbJiangyinService.updateYewu(start+8*3600*1000, end+8*3600*1000);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		String result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	//保存江阴自定义界面配置(先删除再保存?)
	@RequestMapping(value = {"/saveStationData"},method = {RequestMethod.POST})
	public String saveStationData(HttpServletRequest request,
							HttpServletResponse response,
							@RequestBody List<Map<String , String>> stationdata,
							@CookieValue("ticket") String ticket) throws IOException {		
		String result = dbStationDataService.insertStationData(stationdata,ticket);
		//System.out.println(stationdata+"1");
		if(result=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(result, "html", response);
		}
		return null;
	}
	
	//展示江阴自定义界面
	@RequestMapping(value = {"/getStationData"},method = {RequestMethod.GET})
	public String getStationData(HttpServletRequest request,
							HttpServletResponse response,
							@CookieValue("ticket") String ticket) throws IOException {
		String result = null;
		int line_id = snUserService.getUserByTicket(ticket).getLineid();
		List<Map<String, Object>> list =dbStationDataService.getStationDataByLineId(line_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		//System.out.println(list);
		Gson gson = new Gson();
		result = gson.toJson(list);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	
	//获江阴各站点的完整数据格式
	@RequestMapping(value = {"/getCompleteData"}, method = {RequestMethod.GET})
	public String getCompleteData(HttpServletRequest request,
								HttpServletResponse response,
								@CookieValue("ticket") String ticket) throws IOException{
		String result = null;
		int lineId = snUserService.getUserByTicket(ticket).getLineid();
		//System.out.println(lineId);
		List<Map<String, Object>> list = infoStationService.selectByLine(lineId);
		//System.out.println(list);
		for (Map<String, Object> map1 : list) {
			String statId = (String)map1.get("STAT_ID");
			List<Map<String, Object>> lest = dbJiangyinService.selectLatestRecord();
			for (Map<String, Object> map2 : lest) {
				double value = Double.parseDouble(map2.get(statId).toString()); 
				map1.put("VALUE", value);
			}	
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
		
	//获取异常推送信息(与小时均值比较)
	@RequestMapping(value = {"/getAbnormalInfo"}, method = {RequestMethod.GET})
	public String getAbnormalInfo(HttpServletRequest request,
							HttpServletResponse response,
							@CookieValue("ticket") String ticket) throws IOException{
			String result = null;
			int lineId = snUserService.getUserByTicket(ticket).getLineid();
			List<String> list = infoStationService.getAbnormalInfo(lineId);
			System.out.println(list);
			Gson gson = new Gson();
			result = gson.toJson(list);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
	}	
	
	//获取异常日志信息
	@RequestMapping(value= {"/getAbnormalLog"}, method= {RequestMethod.GET})
	public String getAbnormalLog(HttpServletRequest request,
								 HttpServletResponse response,
								 @CookieValue("ticket") String ticket) throws IOException{
		String result = null;	
		int lineId = snUserService.getUserByTicket(ticket).getLineid();
		List<Map<String, Object>> list = infoStationService.getAbnormalLog(lineId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
		
	}
}
