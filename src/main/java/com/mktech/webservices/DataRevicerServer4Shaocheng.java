package com.mktech.webservices;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mktech.service.impl.DbLimoServiceImpl;
import com.mktech.service.impl.DbShaochengServiceImpl;
import com.mktech.service.impl.LineServiceImpl;

/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */

/**
 * 类HelloServer.java的实现描述：TODO 类实现描述
 * 
 * @author HEBI 2012-8-20 下午05:08:58
 */
@Component("DataRevicerServer4Shaocheng")
public class DataRevicerServer4Shaocheng {

	
	@Resource
	private DbShaochengServiceImpl dbShaochengService;

	public int ReceiveData(String jsonobject) {

		if (dbShaochengService.insertDataByJson(jsonobject) == 1) {
			System.out.println("insert success!");
			return 0;
		}
		return 500;
	}

}
