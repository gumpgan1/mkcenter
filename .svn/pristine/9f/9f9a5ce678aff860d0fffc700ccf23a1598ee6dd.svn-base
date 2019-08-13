/**
 * @author Chnyge Lin
 */
package com.mktech.service.impl;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mktech.dao.ILineDao;
import com.mktech.entity.Line;
import com.mktech.service.ILineService;



/**
 * @author Chnyge Lin
 *
 */
@Service
public class LineServiceImpl implements ILineService {
	
	@Resource  
	private ILineDao  lineADOMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return lineADOMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 直接插入实体
	 */
	@Override
	public int insert(Line record) {
		// TODO Auto-generated method stub
		return lineADOMapper.insert(record);
	}

	@Override
	public int insertSelective(Line record) {
		// TODO Auto-generated method stub
		return lineADOMapper.insertSelective(record);
	}

	@Override
	public Line selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return lineADOMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Line record) {
		// TODO Auto-generated method stub
		return lineADOMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Line record) {
		// TODO Auto-generated method stub
		return lineADOMapper.updateByPrimaryKey(record);
	}
	/**
	 * insert new data according to json(auto -> id)
	 * @param json
	 * @return
	 */
	public int insertDataByJson(String json){
		
		JSONObject jsonObject = JSON.parseObject(json);
		try {
			JSONObject messageObject = jsonObject.getJSONObject("message");
			JSONObject dataObject = messageObject.getJSONObject("data");
			Line lineA = (Line) JSONObject.toJavaObject(dataObject,
					Line.class);
			lineA.setTimestamp(jsonObject.getString("timestamp"));
			this.insert(lineA);
//			System.out.println("insert success!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 1;
		
	}
	
}
