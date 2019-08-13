package com.mktech.entity;

import com.mktech.utils.ObjectUtil;

public class SnUser {
    private Integer userid;

    private String username;

    private String password;

    private String salt;
    
    private Integer role;
    
    private Integer lineid;
    
    private Integer companyid;
    

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }
    
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	
	public Integer getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public Integer getLineid() {
		return lineid;
	}

	public void setLineid(Integer lineid) {
		this.lineid = lineid;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ObjectUtil.getString(this, this.getClass());
	}
}