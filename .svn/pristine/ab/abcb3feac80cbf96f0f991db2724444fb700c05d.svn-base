/**
 * @author Chnyge Lin
 */
package com.mktech.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

/**
 * @author Chnyge Lin
 *
 */
public class DownloadUtil {
	
	public static void downloadExcel(InputStream inputStream,HttpServletResponse response,String excelName){
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;" + "filename=" + excelName);
		try{
			FileCopyUtils.copy(inputStream,response.getOutputStream());
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
}
