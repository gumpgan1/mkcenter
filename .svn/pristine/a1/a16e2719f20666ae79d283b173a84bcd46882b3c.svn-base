/**
 * @author Chnyge Lin
 */
package com.mktech.webservices;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.mktech.service.impl.DbLimoServiceImpl;
import com.mktech.service.impl.DbShaochengServiceImpl;

/**
 * @author Chnyge Lin
 *
 */
@Component("DataRevicerServer4Limo")
public class DataRevicerServer4Limo {
	
	@Resource
	private DbLimoServiceImpl dbLimoService;

	public int ReceiveData(String jsonobject) {
		if (dbLimoService.insertDataByJson(jsonobject) == 1) {
			System.out.println("insert success!");
			return 0;
		}
		return 500;
	}
}
