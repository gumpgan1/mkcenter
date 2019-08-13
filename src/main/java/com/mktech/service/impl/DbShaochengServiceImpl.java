/**
` * @author Chnyge Lin
 */
package com.mktech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mktech.dao.DbShaochengDao;
import com.mktech.entity.DbLimo;
import com.mktech.entity.DbShaocheng;
import com.mktech.service.DbShaochengService;

/**
 * @author Chnyge Lin,gumpgan, Sunzt
 *
 */
@Service
public class DbShaochengServiceImpl implements DbShaochengService {

	@Resource
	private DbShaochengDao dbShaochengMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DbShaocheng record) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.insert(record);
	}

	@Override
	public int insertSelective(DbShaocheng record) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.insertSelective(record);
	}

	@Override
	public DbShaocheng selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DbShaocheng record) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DbShaocheng record) {
		// TODO Auto-generated method stub
		return dbShaochengMapper.updateByPrimaryKey(record);
	}

	@Override
	public int insertDataByJson(String json) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = JSON.parseObject(json);
		try {
			JSONObject messageObject = jsonObject.getJSONObject("message");
			JSONObject dataObject = messageObject.getJSONObject("data");
			DbShaocheng dbShaocheng = (DbShaocheng) JSONObject.toJavaObject(dataObject, DbShaocheng.class);
			dbShaocheng.setTimestamp(jsonObject.getString("timestamp"));
			this.insert(dbShaocheng);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}


}
