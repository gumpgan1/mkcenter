package com.mktech.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mktech.dao.AlarmTextDao;
import com.mktech.dao.DbJiangyinDao;
import com.mktech.dao.InfoStationDao;
import com.mktech.entity.AlarmText;
import com.mktech.entity.DbJiangyin;

/**
 * @author gumpgan
 * @用于记录报警信息
 */

@Component
public class AbnormalLog {

	@Resource
	private AlarmTextDao alarmTextMapper;
	
	@Resource 
	private InfoStationDao infoStationMapper;
	
	@Resource
	private DbJiangyinDao dbJiangyinMapper;
	
 
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void getAbnormalInfo() {
		System.out.println("运行了");
		List<Map<String, Object>> list = infoStationMapper.selectNeedAbStations(5);
		List<String> lest = new ArrayList<String>();
		Map<String, Object> jm = null;

			jm = dbJiangyinMapper.selectLatestRecordInMinByMap();

		
		
			for (Map<String, Object> map : list) {
				 System.out.println("1");
				 String stat_id = (String) map.get("STAT_ID"); //L00..
				 Double num = (Double) jm.get(stat_id);
				 AlarmText alarmText = new AlarmText();
				 if(num < (Double)map.get("MIN_THRESHOLD")){
					 System.out.println("2");
					 String str =  (String)map.get("NAME") + "值低于下限" ;
					 lest.add(str);				 
					 alarmText.setAlarm_show(str);
					 alarmText.setLine_id(5);
					 alarmText.setMax_threshold((Double)map.get("MAX_THRESHOLD"));
					 alarmText.setMin_threshold((Double)map.get("MIN_THRESHOLD"));
					 alarmText.setNow_data(num);
					 alarmText.setStation_id(stat_id);
					 alarmText.setStation_name((String) map.get("NAME"));
					 alarmText.setTimestamp(String.valueOf(System.currentTimeMillis()));
					 System.out.println("3");
					 System.out.println(alarmText.toString());
					 alarmTextMapper.insert(alarmText);
					 System.out.println("存储成功");
				 }else if(num > (Double)map.get("MAX_THRESHOLD")){					 
					 String str =  (String)map.get("NAME") + "值超过上限";
					 lest.add(str);
					 alarmText.setAlarm_show(str);
					 alarmText.setLine_id(5);
					 alarmText.setMax_threshold((Double)map.get("MAX_THRESHOLD"));
					 alarmText.setMin_threshold((Double)map.get("MIN_THRESHOLD"));
					 alarmText.setNow_data(num);
					 alarmText.setStation_id(stat_id);
					 alarmText.setStation_name((String) map.get("NAME"));
					 alarmText.setTimestamp(String.valueOf(System.currentTimeMillis()));
					 alarmTextMapper.insert(alarmText);
					 System.out.println("存储成功");
				 }else {
					 lest.add(null);
				 }
			}
		
	}
	
}
