/**
 * @author Chnyge Lin
 */
package com.mktech.webservices;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mktech.service.impl.SystemSummaryServviceImpl;

/**
 * 用于所有企业数据传输，根据deviceid判断相应表归属
 * 
 *
 */
@Component("DataReceiverServer")
public class DataReceiverServer {
	

	
	@Resource
	private SystemSummaryServviceImpl systemSummaryServvice;

	public int ReceiveData(String json) {
		try {
			JSONObject jsonObject = JSON.parseObject(json);
			String system_name=systemSummaryServvice.insertByDeviceId(jsonObject); 
			if(system_name!=null){
				if(system_name == "巨化重复"){
					//有重复数据
					return 100;
				}
				System.out.println("载入成功");
				return 0;				
			}else {
				System.out.println("没有找到数据");
				return 500;
				//没有找到数据
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 404;
		}

	}
}
