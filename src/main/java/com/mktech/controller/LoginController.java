/**
 * @author Chnyge Lin
 */
package com.mktech.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.DbLimo;
import com.mktech.entity.SnUser;
import com.mktech.entity.SnUserTicket;
import com.mktech.service.impl.DbLimoServiceImpl;
import com.mktech.service.impl.SnUserServiceImpl;
import com.mktech.utils.CommonUtil;

/**
 * controller 4 index
 * 
 * @author Chnyge Lin
 * @author gumpgan
 * @author szt
 * 
 */
@Controller
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private SnUserServiceImpl snUserService;
	
	@Resource
	private DbLimoServiceImpl dbLimoService;
	
	@Resource
	private SnUserTicketDao snUserTicketDao;
	
	@RequestMapping(path = {"/reg"}, method = {RequestMethod.POST})
	public String reg(Model model,
					@RequestParam("username") String username,
					@RequestParam("password") String password){
		
		try {
			Map<String, String> map = snUserService.register(username, password);
			if(map.containsKey("msg")){
				model.addAttribute("msg", map.get("msg"));
				return "WEB-INF/login/index";
			}
			return "redirect:/";
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("注册异常"+e.getMessage());
			model.addAttribute("msg", "服务器异常");
			return "WEB-INF/login/index";
		}
	}
	
	/**
	 * 重定向
	 * @return
	 */
	@RequestMapping(value = { "/index" }, method = {RequestMethod.GET})
	public String index() {
		return "WEB-INF/login/index";
	}
	/**
	 * 登录
	 * @param model
	 * @param username
	 * @param password
	 * @param next
	 * @param response
	 * @return
	 */
	@RequestMapping(path = {"/login"},method = {RequestMethod.POST})
	public String login(Model model,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value = "next",required = false) String next,
			HttpServletResponse response){
		try {
			Map<String, String> map = snUserService.login(username, password);
			if(map.containsKey("msg")){
				model.addAttribute("msg", map.get("msg"));
				model.addAttribute("username", username);
				return "WEB-INF/login/index";
			}else if(map.containsKey("ticket")){
				Cookie cookie = new Cookie("ticket", map.get("ticket"));
				cookie.setPath("/");
				cookie.setMaxAge(3600*24);
//				long tmptime = Long.parseLong("1522701557277");
				Date date = new Date();
				String tmptime = CommonUtil.BJTime2Timestamp(String.valueOf(date.getTime()));
				response.addCookie(cookie);
				model.addAttribute("tmpname",username);
				model.addAttribute("tmptime",tmptime);
				Map<String, Integer> mep = snUserService.checkLine(username);
				if(mep.containsKey("99") || mep.containsKey("1")) {
					return "redirect:0729/index.html";
				}else {
					if(mep.get("lineId") == 5) {
						return "redirect:0729/jiangyin.html";
					}
					if(mep.get("lineId") == 3) {
						return "redirect:0729/daying1.html";
					}
					if(mep.get("lineId") == 4) {
						return "redirect:0729/daying2.html";
					}
					if(mep.get("lineId") == 16) {
						return "redirect:jiangyin/jiangyin.html";
					}
				}
			}
			return "redirect:/";
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("登录异常"+e.getMessage());
			model.addAttribute("msg", "服务器异常");
			return "WEB-INF/login/index";
		}
	}
	
	/**
	 * 登出
	 * @param ticket
	 * @return
	 */
	@RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
	public String logout(@CookieValue("ticket") String ticket){
		snUserService.logout(ticket);
		return "redirect:/";
	}
	
	/**
	 * 控制器tab入口
	 * @param model
	 * @param response
	 * @param ticket
	 * @return
	 */
	@RequestMapping(path = {"/ControllerIndex"},method = {RequestMethod.GET})
	public String ControllerIndex(Model model,
									HttpServletResponse response,
									@CookieValue("ticket") String ticket){
		long tmptime = Long.parseLong("1522701557277");
		System.out.println(snUserService.getUserByTicket(ticket).getUsername());
		model.addAttribute("tmpname",snUserService.getUserByTicket(ticket).getUsername());
		model.addAttribute("tmptime",tmptime);
		return "WEB-INF/controller_main/new_index";
	}
	
	/**
	 * 数据查询tab入口
	 * @param model
	 * @param response
	 * @param ticket
	 * @return
	 */
	@RequestMapping(path = {"/ControllerTable"},method = {RequestMethod.GET})
	public String ControllerTable(Model model,
									HttpServletResponse response,
									@CookieValue("ticket") String ticket){
		long tmptime = Long.parseLong("1522701557277");
		System.out.println(snUserService.getUserByTicket(ticket).getUsername());
		model.addAttribute("tmpname",snUserService.getUserByTicket(ticket).getUsername());
		model.addAttribute("tmptime",tmptime);
		return "WEB-INF/controller_main/new_table";
	}
	
	@RequestMapping(path={"/checkLogin"},method={RequestMethod.POST})
	public String checkLogin(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String ticket = null;
		if(request.getCookies()!=null){
			for(Cookie cookie: request.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket = cookie.getValue();
					break;
				}
			}
		}
		if(ticket==null){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			SnUserTicket sut = snUserTicketDao.selectByTicket(ticket);
			if(sut == null || sut.getExipred().before(new Date()) || sut.getStatus() !=0){
				CommonUtil.writeToWeb("0", "html", response);
			}else{
				SnUser user = snUserService.selectById(sut.getUserid());
				CommonUtil.writeToWeb(user.getUsername(), "html", response);
			}
		}
		
		return null;
		
	}
	
	@RequestMapping(path={"/checkPr"},method={RequestMethod.POST})
	public String checPr(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String ticket = null;
		if(request.getCookies()!=null){
			for(Cookie cookie: request.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket = cookie.getValue();
					break;
				}
			}
		}
		if(ticket==null){
			CommonUtil.writeToWeb("0", "html", response);
		}else{
			SnUserTicket sut = snUserTicketDao.selectByTicket(ticket);
			if(sut == null || sut.getExipred().before(new Date()) || sut.getStatus() !=0){
				CommonUtil.writeToWeb("0", "html", response);
			}else{
				SnUser user = snUserService.selectById(sut.getUserid());
				CommonUtil.writeToWeb(user.getUsername()+"&"+user.getRole(), "html", response);
			}
		}
		
		return null;
		
	}
	/**
	 * 测试方法入口
	 * testLin.do?
	 * pathvariable
	 * @throws IOException 
	 */
	@RequestMapping(path = {"/testLin"},method = {RequestMethod.GET})
	@ResponseBody
	public String testLin(Model model,
			@RequestParam("min") Integer min,
			@RequestParam("max") Integer max,
			HttpServletResponse response) throws IOException{

		if(min>max){
			return "wrong!";
		}else if (max-min>=100000){
			return "Range is unsupported!Supported number of data should not be more than 100,000. ";
		}
		byte[] bytes = dbLimoService.export2ExcelNotPernament(min, max);
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + UUID.randomUUID() + ".xlsx");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		//测试是否能释放内存
		bytes = null;
		return "success!";
		
	}
	
	
}
