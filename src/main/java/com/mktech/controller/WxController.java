package com.mktech.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.record.chart.TickRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mktech.dao.SnUserDao;
import com.mktech.entity.SnUser;
import com.mktech.service.BoardConfig2Service;
import com.mktech.service.impl.DbJiangyinServiceImpl;
import com.mktech.service.impl.InfoLineServiceImpl;
import com.mktech.service.impl.InfoStationServiceImpl;
import com.mktech.service.impl.SnUserServiceImpl;
import com.mktech.service.impl.WxServiceImpl;
import com.mktech.utils.CommonUtil;

/**
 * 设备状态监控页面
 * @author Sunzt
 * @author gumpgan
 *
 */
@Controller
@RequestMapping(value = "/weixin")
public class WxController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Resource
	private SnUserServiceImpl snUserService;
	
	@Resource
	private DbJiangyinServiceImpl dbJiangyinService;
	
	@Resource
	private InfoStationServiceImpl infoStationService;
	
	@Resource
	private InfoLineServiceImpl infoLineService;
	
	@Resource
	private WxServiceImpl wxService;
	
	@Resource
	private SnUserDao snUserMapper;
	
	//登录返回用户信息
	@RequestMapping(value = {"/login"}, method = {RequestMethod.GET})
	public String login(Model model,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletResponse response){			
		try {
			String result="";
			Map<String, String> map = snUserService.login(username, password);
			if(map.containsKey("msg")){
				model.addAttribute("msg", map.get("msg"));
				model.addAttribute("username", username);
				return "WEB-INF/login/index";
			}else if(map.containsKey("ticket")){				
				SnUser user = snUserMapper.selectByUsername(username);;
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("list", user);
				Gson gson = new Gson();
				result=gson.toJson(map1);
				CommonUtil.writeToWeb(result, "html", response);
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("登录异常"+e.getMessage());
			model.addAttribute("msg", "服务器异常");
			return "WEB-INF/login/index";
		}
		return null;
			
	}


	
	//返回所有产线公司信息
	@RequestMapping(value = {"/lines"},method = {RequestMethod.GET})
	public String indexGetLines(HttpServletRequest request,
						HttpServletResponse response) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = infoLineService.selectAllWx();		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}

	
	//根据公司id和用户id获取对应产线信息
	@RequestMapping(value = {"/getLinesById"},method = {RequestMethod.GET})
	public String getLinesById(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam("roleid") int roleid,
						@RequestParam("companyid") int companyid) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = wxService.selectCompanyLine(companyid, roleid);		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}

	
	
		
	//返回工位信息
	@RequestMapping(value = {"/getStatByLine"}, method = {RequestMethod.GET})
	public String getStatByLine(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam("lineid") int lineid
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoStationService.selectByLine(lineid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	

	//返回实时最新数据
	@RequestMapping(value = {"/jyGetNew"}, method = {RequestMethod.GET})
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

	//返回过去一段时间数据
	
	@RequestMapping(value = {"/jyGet100"},method = {RequestMethod.GET})
	public String getLimo100(HttpServletRequest request,
							HttpServletResponse response) throws IOException {
		String result = null;	
		List<Map<String, Object>> list = dbJiangyinService.selectLatestRecord500();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}

	//返回报警信息
	@RequestMapping(value = {"/jyGetAbnormalInfo"}, method = {RequestMethod.GET})
	public String getAbnormalInfo(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("lineid") Integer lineId) throws IOException{
			String result = null;
			List<String> list = infoStationService.getAbnormalInfo(lineId);
			//System.out.println(list);
			Gson gson = new Gson();
			result = gson.toJson(list);
			CommonUtil.writeToWeb(result, "html", response);
			return null;
	}

	//密码修改
		
}