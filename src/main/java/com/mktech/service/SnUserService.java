/**
 * @author Chnyge Lin
 */
package com.mktech.service;

import java.util.Map;

import com.mktech.entity.SnUser;

/**
 * @author Chnyge Lin,gumpgan,Sunzt
 *
 */
public interface SnUserService {

	Map<String, String> register(String username,String password);
	
	Map<String, String> login(String username,String password);
	
	String addLoginTicket(int userId);
	
	SnUser getUserByTicket(String ticket);
	
	void logout(String ticket);
	
}
