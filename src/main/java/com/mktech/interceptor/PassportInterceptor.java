/**
 * @author Chnyge Lin
 */
package com.mktech.interceptor;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.ClearCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mktech.dao.SnUserDao;
import com.mktech.dao.SnUserTicketDao;
import com.mktech.entity.HostHolder;
import com.mktech.entity.SnUser;
import com.mktech.entity.SnUserTicket;

/**
 * @author Chnyge Lin
 * @author Sunzt
 * @author gumpgan
 *
 */
public class PassportInterceptor implements HandlerInterceptor {
	
	@Resource
	private SnUserTicketDao snUserTicketDao;
	
	@Resource
	private SnUserDao snUserDao;
	
	@Resource
	private HostHolder hostHolder;
	
	/**
	 * 请求后拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		hostHolder.clear();
		
	}

	/**
	 * 请求时拦截(渲染之前)
	 */
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		//所有的controller在渲染前都能获得user信息
		if(modelAndView!=null && hostHolder.getUser() != null){
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}

	/**
	 * 请求前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String ticket = null;
		if(httpServletRequest.getCookies()!=null){
			for(Cookie cookie: httpServletRequest.getCookies()){
				if(cookie.getName().equals("ticket")){
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		if(ticket!=null){
			SnUserTicket sut = snUserTicketDao.selectByTicket(ticket);
			if(sut == null || sut.getExipred().before(new Date()) || sut.getStatus() !=0){
				return true;
			}
			
			SnUser user = snUserDao.selectByPrimaryKey(sut.getUserid());
			hostHolder.setUser(user);
		}
		return true;
	}
	
	
}
