/**
 * @author Chnyge Lin
 */
package com.mktech.controller;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.javassist.compiler.ast.DoubleConst;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.mktech.entity.InfoCompany;
import com.mktech.service.impl.InfoCompanyServiceImpl;
import com.mktech.service.impl.InfoLineServiceImpl;
import com.mktech.service.impl.InfoStationServiceImpl;
import com.mktech.service.impl.SnUserServiceImpl;
import com.mktech.utils.CommonUtil;
import com.mktech.python.threthod.*;

/**
 * controller 4 index
 * 
 * @author Chnyge Lin
 * @author gumpgan
 * @author szt
 * 
 */
@Controller
@RequestMapping(value = "/adms")
public class ManagerController {
	
	@Resource
	private SnUserServiceImpl snUserService;
	
	@Resource
	private InfoCompanyServiceImpl infoCompanyService;
	
	@Resource
	private InfoLineServiceImpl infoLineService;
	
	@Resource
	private InfoStationServiceImpl infoStationService;
	
	
	/**
	 * 修改密码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = { "/changeP" }, method = {RequestMethod.POST})
	public String changePassword(HttpServletRequest request,
			@RequestParam("oldPassword") String oldString,
			@RequestParam("newPassword") String newString,
			@CookieValue("ticket") String ticket,
			HttpServletResponse response) throws IOException {	
		    int i = snUserService.setPassword(ticket, oldString, newString);
			if(i==1){
				CommonUtil.writeToWeb("修改成功！", "html", response);
			}else if(i==0){
				CommonUtil.writeToWeb("原密码错误！", "html", response);
			}else if(i==500){
				CommonUtil.writeToWeb("修改失败，请稍候再试！", "html", response);
			}
			return null;
	}
	
	
	@RequestMapping(value={"/changeSelf"},method = {RequestMethod.POST})
	public String changeSelfInfo(HttpServletRequest request,
								HttpServletResponse response,
								@RequestParam("name") String name,
								@RequestParam("sex") String sex,
								@RequestParam("phone") String phone,
								@RequestParam("address") String address,
								@RequestParam("email") String email,
								@RequestParam("instruction") String instruction,
								@CookieValue("ticket") String ticket) throws IOException{
		MultipartHttpServletRequest m = request instanceof MultipartHttpServletRequest?(MultipartHttpServletRequest) request:null; 
		String type = m.getFile("uploadimage").getContentType();
		long size = m.getFile("uploadimage").getSize();
		if(type.equals("image/jpeg")||type.equals("image/jpg")||type.equals("image/png")||type.equals("application/octet-stream")){
		
			if(size>10240000){
				CommonUtil.writeToWeb("图片太大，请选择10M以内的图片！", "html", response);
				return null;
			}else{
				byte[] file = m.getFile("uploadimage").getBytes();
				int i;
				if(size<10){
					i = snUserService.setSelfinfowithoutFile(ticket, name, sex, phone, email, address, instruction);
				}else{
					i = snUserService.setSelfinfo(ticket, name, sex, phone, email, address, instruction, file);
				}
				if(i==1){
					CommonUtil.writeToWeb("修改成功！", "html", response);
				}else if(i==500){
					CommonUtil.writeToWeb("修改失败！", "html", response);
				}else{
					CommonUtil.writeToWeb("异常错误！", "html", response);
				}
			}
		}else{
			CommonUtil.writeToWeb("图片格式不正确，请重新选择！", "html", response);
		}
		return null;
	}
	
	@RequestMapping(value = {"/getUser"}, method = {RequestMethod.POST})
	public String getDefaultConfig(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = snUserService.selectUser(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/getAllUser"}, method = {RequestMethod.GET})
	public String getAll(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = snUserService.selectAll(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/getCompanyUser"}, method = {RequestMethod.GET})
	public String getCompanyUser(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;		
		List<Map<String, Object>> list = snUserService.selectCompanyUser(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value={"/createUser"},method={RequestMethod.POST})
	public String createUser(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("username") String username,
							@RequestParam("autority") int role,
							@RequestParam("companyid") int companyid,
							@RequestParam("lineid") int lineid,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = snUserService.createUser(ticket, username, role , companyid, lineid);
		System.out.println(i);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/updateUser"},method={RequestMethod.POST})
	public String updateUser(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("userid") int userid,
							@RequestParam("username") String username,
							@RequestParam("autority") int role,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = snUserService.updateUser(userid, ticket, username, role);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/deleteUser"},method={RequestMethod.POST})
	public String deleteUser(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("userid") int userid,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = snUserService.deleteUser(ticket, userid);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value = {"/getAllCompany"}, method = {RequestMethod.GET})
	public String getCompany(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoCompanyService.getAllCompany(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/getSelfCompany"}, method = {RequestMethod.GET})
	public String getSelfCompany(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoCompanyService.getSelfCompany(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	
	@RequestMapping(value={"/createCompany"},method={RequestMethod.POST})
	public String createCompany(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("name") String name,
							@RequestParam("province") String province,
							@RequestParam("city") String city,
							@RequestParam("address") String address,
							@RequestParam("phone") String phone,
							@RequestParam("href") String href,
							@RequestParam("instruction") String instruction,
							@RequestParam("location_X") String x,
							@RequestParam("location_Y") String y,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoCompanyService.createCompany(ticket, name, province, city, address, phone, href, instruction, Float.valueOf(x), Float.valueOf(y));
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/updateCompany"},method={RequestMethod.POST})
	public String updateCompany(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("id") int id,
							@RequestParam("name") String name,
							@RequestParam("address") String address,
							@RequestParam("phone") String phone,
							@RequestParam("href") String href,
							@RequestParam("instruction") String instruction,
							@RequestParam("location_X") String x,
							@RequestParam("location_Y") String y,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoCompanyService.updateCompany(id, ticket, name, address, phone, href, instruction, Float.valueOf(x), Float.valueOf(y));
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/deleteCompany"},method={RequestMethod.POST})
	public String deleteCompany(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("companyid") int id,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoCompanyService.deleteCompany(ticket, id);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value = {"/getAllLines"}, method = {RequestMethod.GET})
	public String getLine(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoLineService.getAllLine(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	
	@RequestMapping(value = {"/getCompanyLines"}, method = {RequestMethod.GET})
	public String getCompanyLine(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoLineService.getCompanyLine(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value={"/createLine"},method={RequestMethod.POST})
	public String createLine(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("companyid") int companyid,
							@RequestParam("name") String name,
							@RequestParam("E_MIN") String e_min,
							@RequestParam("E_MAX") String e_max,
							@RequestParam("S_MIN") String s_min,
							@RequestParam("S_MAX") String s_max,
							@RequestParam("info") String info,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoLineService.createLine(ticket, companyid, name, info, s_min+","+s_max, e_min+","+e_max);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/updateLine"},method={RequestMethod.POST})
	public String updateLine(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("lineid") int id,
							@RequestParam("name") String name,
							@RequestParam("E_MIN") String e_min,
							@RequestParam("E_MAX") String e_max,
							@RequestParam("S_MIN") String s_min,
							@RequestParam("S_MAX") String s_max,
							@RequestParam("info") String info,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoLineService.changeLine(ticket, id, name, info, s_min+","+s_max, e_min+","+e_max);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	@RequestMapping(value={"/deleteLine"},method={RequestMethod.POST})
	public String deleteLine(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("lineid") int id,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoLineService.deleteLine(ticket, id);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value = {"/getAllStats"}, method = {RequestMethod.GET})
	public String getStat(HttpServletRequest request,
						HttpServletResponse response,
						@CookieValue("ticket") String ticket
						) throws IOException{
		String result = null;	
		List<Map<String, Object>> list = infoStationService.getAllStation(ticket);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		Gson gson = new Gson();
		result = gson.toJson(map);
		CommonUtil.writeToWeb(result, "html", response);
		return null;
	}
	
	@RequestMapping(value = {"/getStatByLine"}, method = {RequestMethod.GET})
	public String getStatByLine(HttpServletRequest request,
						HttpServletResponse response,
						@RequestParam("lineid") int lineid,
						@CookieValue("ticket") String ticket
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
	
	@RequestMapping(value={"/createStat"},method={RequestMethod.POST})
	public String createStat(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("lineid") int lineid,
							@RequestParam("statid") String statid,
							@RequestParam("name") String name,
							@RequestParam("nickname") String nickname,
							@RequestParam("unit") String unit,
							@RequestParam("instruction") String info,
							@RequestParam("min") String min,
							@RequestParam("max") String max,
							@RequestParam("relate1n") String relate1n,
							@RequestParam("relate2n") String relate2n,
							@RequestParam("relate1v") String relate1v,
							@RequestParam("relate2v") String relate2v,
							@RequestParam("abnormal") int abnormal,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoStationService.createStation(ticket, lineid, statid, name, nickname, unit, info, Double.valueOf(min),Double.valueOf(max), relate1n, relate1v, relate2n, relate2v, abnormal);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	@RequestMapping(value={"/updateStat"},method={RequestMethod.POST})
	public String updateStat(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("id") int id,
							@RequestParam("name") String name,
							@RequestParam("nickname") String nickname,
							@RequestParam("unit") String unit,
							@RequestParam("instruction") String info,
							@RequestParam("min") String min,
							@RequestParam("max") String max,
							@RequestParam("relate1n") String relate1n,
							@RequestParam("relate2n") String relate2n,
							@RequestParam("relate1v") String relate1v,
							@RequestParam("relate2v") String relate2v,
							@RequestParam("abnormal") int abnormal,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoStationService.changeStation(ticket, id, name,nickname,unit, info, Double.valueOf(min), Double.valueOf(max), relate1n, relate1v, relate2n, relate2v,abnormal);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value={"/deleteStat"},method={RequestMethod.POST})
	public String deleteStat(HttpServletRequest request,
							HttpServletResponse response,
							@RequestParam("id") int id,
							@CookieValue("ticket") String ticket) throws IOException{
		String i = infoStationService.deleteStation(ticket, id);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		return null;
	}
	
	@RequestMapping(value= {"/abCalculate"},method= {RequestMethod.POST})
	public String abCalculate(HttpServletRequest request,
							  HttpServletResponse response,
							  @RequestParam("ab_statid1") String ab_statid,
							  @RequestParam("ab_id") int ab_id,							  
							  @RequestParam("ab_day") String day,
							  @RequestParam("ab_sigma") String sigma,
							  @RequestParam("ab_name1") String name,
							  @CookieValue("ticket") String ticket)throws IOException, InterruptedException{
		int day1=Integer.parseInt(day);
		int sigma1=Integer.parseInt(sigma);
		String j=threthods.linjin(sigma1, ab_statid, day1);	
		System.out.println(j+"运行结果");
		int point=j.indexOf(",");
		String min=j.substring(10, point);
		String max=j.substring(point+2, j.length()-10);
		String i=infoStationService.abCalculate(ticket,max,min,ab_id,name);
		if(i=="0"){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			CommonUtil.writeToWeb(i, "html", response);
		}
		
		
		return null;
	}

}
	
 