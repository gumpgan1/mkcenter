/**
 * @author Chnyge Lin
 */
package com.mktech.entity;

import org.springframework.stereotype.Component;

/**
 * @author Chnyge Lin
 *
 */
@Component
public class HostHolder {
	
	private static ThreadLocal<SnUser> snuser = new ThreadLocal<SnUser>();

	public SnUser getUser(){
		return snuser.get();
	}
	
	public void setUser(SnUser user){
		snuser.set(user);
	}
	
	public void clear(){
		snuser.remove();
	}

	
}
