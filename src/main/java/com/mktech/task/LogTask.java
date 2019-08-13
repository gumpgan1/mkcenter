package com.mktech.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mktech.entity.DbJiangyin;
import com.mktech.entity.DbJiangyinLog;
import com.mktech.entity.DbJiangyinLogChange;
import com.mktech.entity.DbJiangyinLogMnt;
import com.mktech.service.impl.DbJiangyinLogChangeServiceImpl;
import com.mktech.service.impl.DbJiangyinLogMntServiceImpl;
import com.mktech.service.impl.DbJiangyinLogServiceImpl;
import com.mktech.service.impl.DbJiangyinServiceImpl;
import com.mktech.utils.CommonUtil;

/**
 * 
 * @用于日志收集和记录
 * @author gumpgan
 *
 */

@Component
public class LogTask {

	@Resource
	private DbJiangyinServiceImpl dbJiangyinService;
	
	@Resource
	private DbJiangyinLogServiceImpl dbJiangyinLogService;
	
	@Resource
	private DbJiangyinLogMntServiceImpl dbJiangyinLogMntSerivce;
	
	@Resource
	private DbJiangyinLogChangeServiceImpl dbJiangyinLogChangeService;
	
	
	@Scheduled(cron = "0/2 * * * * * ")
	public void checkAction() throws Exception{
		//设定初始化变量
		DbJiangyinLog log = new DbJiangyinLog();
		List<String> changes = new ArrayList<String>();
		//取出最新的一条数据和其前一条数据
		DbJiangyin nowData = dbJiangyinService.selectNearestRecord2() ;
		long preTime = Long.parseLong(nowData.getTimestamp())-2000;
		DbJiangyin preData = dbJiangyinService.selectNearestRecord(String.valueOf(preTime));
		//判断喂料量是否正常
		if(nowData.getL0002()+nowData.getL0005()+nowData.getL0008()>125) {
			//将两组数据进行对比，判断动作开始
			if (compareEntity(nowData, preData) == 1) {
				//System.out.println("相同，没变化");
				return;
			} else {
				//System.out.println("进入操作开始判断");
				log.setStart1(preData.getTimestamp());
				log.setStartTime1(CommonUtil.timestamp2RealDate(preData.getTimestamp()));
				DbJiangyin temp = nowData;
				int count=0;
				while(true && count<10) {
					Thread.sleep(2000);
					DbJiangyin nextData = dbJiangyinService.selectNearestRecord2();
					if(compareEntity(temp, nextData) == 1) {
						count++;
					}else {
						count=0;
						temp= nextData;
					}
				}
				//此时temp是操作结束的时间刻，先统计changes变化
				try {
					changes.add(compareData(preData.getL0060(), temp.getL0060()));
					changes.add(compareData(preData.getL0077(), temp.getL0077()));
					changes.add(compareData(preData.getL0093(), temp.getL0093()));
					changes.add(compareData(preData.getL0033(), temp.getL0033()));
					changes.add(compareData(preData.getL0036(), temp.getL0036()));
					//System.out.println("完成注入情况");
				} catch (Exception e) {
					e.printStackTrace();
				}
				//往log里注入变化
				log.setL0060(changes.get(0));
				log.setL0077(changes.get(1));
				log.setL0093(changes.get(2));
				log.setL0033(changes.get(3));
				log.setL0036(changes.get(4));
				// 注入start2时间
				log.setStart2(temp.getTimestamp());
				log.setStartTime2(CommonUtil.timestamp2RealDate(temp.getTimestamp()));
				//找到end的时间
				Thread.sleep(2 * 1000);
				DbJiangyin endData = temp;
				while (checkEnd(endData) != 0) {
					//System.out.println("checkend()");
					Thread.sleep(2000);
					int id= temp.getId()+1;
					endData = dbJiangyinService.selectByPrimaryKey(id);
				}
				//System.out.println("完成checkend操作");
				// 获取end的实体，获取时间戳并注入
				log.setEnd(endData.getTimestamp());
				log.setEndTime(CommonUtil.timestamp2RealDate(endData.getTimestamp()));
				//获取时间差并注入
				DbJiangyinLog preLogData = dbJiangyinLogService.selectNew();
				double pretime = Double.valueOf(preLogData.getStart2());
				double thistime = Double.valueOf(log.getStart1());
				String interval = String.valueOf((thistime-pretime)/1000);
				//System.out.println("打印计算值interval"+interval);
				log.setGapTime(interval);
				//System.out.println(log.toString());
				
				//注入监控变量调控时间戳和id
				DbJiangyinLogMnt logMnt = new DbJiangyinLogMnt();
				logMnt.setId(preLogData.getId()+1);
				logMnt.setTimestamp(log.getStart1());
				logMnt.setTime(log.getStartTime1());
				//System.out.println("set成功");
				dbJiangyinLogMntSerivce.insert(logMnt);
				 
				//注入调控变量变化情况
				dbJiangyinLogService.insert(log);
				
				System.out.println("全部完成");
			}
		}else {
			//System.out.println("喂料量之和小于125，不记录");
			return;
		}
	}
	
	
	@Scheduled(cron = "0/5 * * * * * ")
	public void recordMonitor() {
		//从调控变量表取出最新
		DbJiangyinLog log = dbJiangyinLogService.selectNew();
		//从监控变量表取出最新
		DbJiangyinLogMnt logMnt = dbJiangyinLogMntSerivce.selectNew();
		//进行对比，必然相同，如果不同打印错误，之后check监控变量的表的最新的除一分钟外的值是否为空
		//不为空则直接return
		//为空的话就对进行相关值的注入。
			//获取当前时间戳过去的对应数值（瞬时值或者平均值
			//insert实体
		if(log.getId().equals(logMnt.getId())) {
			Long tmp = Long.valueOf(logMnt.getTimestamp());
			Long now = new Date().getTime();
			if(logMnt.getL0039()!=null) {
				//System.out.println("值已存在");
				return;
			}else if(now-(tmp-8*3600*1000)>30*1000 ){
				//往里面注入对应的值
				long pre60Time = Long.parseLong(logMnt.getTimestamp())-60*1000;
				long pre30Time = Long.parseLong(logMnt.getTimestamp())-30*1000;
				long pre10Time = Long.parseLong(logMnt.getTimestamp())-10*1000;
				long pre5Time = Long.parseLong(logMnt.getTimestamp())-5*1000;
				long post5Time = Long.parseLong(logMnt.getTimestamp())+5*1000;
				long post10Time = Long.parseLong(logMnt.getTimestamp())+10*1000;
				long post30Time = Long.parseLong(logMnt.getTimestamp())+30*1000;
				
				logMnt.setL0039(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0039());
				logMnt.setL0039_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0039());
				logMnt.setL0039_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0039());
				logMnt.setL0039_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0039());
				logMnt.setL0039_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0039());
				logMnt.setL0039_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0039());
				logMnt.setL0039_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0039());
				logMnt.setL0039_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0039());
				
				logMnt.setL0069(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0069());
				logMnt.setL0069_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0069());
				logMnt.setL0069_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0069());
				logMnt.setL0069_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0069());
				logMnt.setL0069_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0069());
				logMnt.setL0069_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0069());
				logMnt.setL0069_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0069());
				logMnt.setL0069_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0069());
				
				logMnt.setL0050(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0050());
				logMnt.setL0050_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0050());
				logMnt.setL0050_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0050());
				logMnt.setL0050_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0050());
				logMnt.setL0050_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0050());
				logMnt.setL0050_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0050());
				logMnt.setL0050_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0050());
				logMnt.setL0050_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0050());
				
				logMnt.setL0049(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0049());
				logMnt.setL0049_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0049());
				logMnt.setL0049_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0049());
				logMnt.setL0049_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0049());
				logMnt.setL0049_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0049());
				logMnt.setL0049_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0049());
				logMnt.setL0049_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0049());
				logMnt.setL0049_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0049());
				
				logMnt.setL0097(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0097());
				logMnt.setL0097_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0097());
				logMnt.setL0097_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0097());
				logMnt.setL0097_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0097());
				logMnt.setL0097_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0097());
				logMnt.setL0097_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0097());
				logMnt.setL0097_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0097());
				logMnt.setL0097_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0097());
				
				logMnt.setL0040(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0040());
				logMnt.setL0040_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0040());
				logMnt.setL0040_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0040());
				logMnt.setL0040_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0040());
				logMnt.setL0040_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0040());
				logMnt.setL0040_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0040());
				logMnt.setL0040_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0040());
				logMnt.setL0040_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0040());
			
			
				logMnt.setL0041(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0041());
				logMnt.setL0041_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0041());
				logMnt.setL0041_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0041());
				logMnt.setL0041_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0041());
				logMnt.setL0041_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0041());
				logMnt.setL0041_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0041());
				logMnt.setL0041_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0041());
				logMnt.setL0041_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0041());
				
				logMnt.setL0042(dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0042());
				logMnt.setL0042_pre_60s(dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0042());
				logMnt.setL0042_pre_30s(dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0042());
				logMnt.setL0042_pre_10s(dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0042());
				logMnt.setL0042_pre_5s(dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0042());
				logMnt.setL0042_post_5s(dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0042());
				logMnt.setL0042_post_10s(dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0042());
				logMnt.setL0042_post_30s(dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0042());
			
				double inValue = dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0002()+dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0005()+dbJiangyinService.selectNearestRecord(logMnt.getTimestamp()).getL0008();
				double inValue_pre_60s = dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(pre60Time)).getL0008();
				double inValue_pre_30s = dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(pre30Time)).getL0008();
				double inValue_pre_10s = dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(pre10Time)).getL0008();
				double inValue_pre_5s = dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(pre5Time)).getL0008();
				double inValue_post_5s = dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(post5Time)).getL0008();
				double inValue_post_10s = dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(post10Time)).getL0008();
				double inValue_post_30s =dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(post30Time)).getL0008();
				
				logMnt.setInValue(inValue);
				logMnt.setInValue_pre_60s(inValue_pre_60s);
				logMnt.setInValue_pre_30s(inValue_pre_30s);
				logMnt.setInValue_pre_10s(inValue_pre_10s);
				logMnt.setInValue_pre_5s(inValue_pre_5s);
				logMnt.setInValue_post_5s(inValue_post_5s);
				logMnt.setInValue_post_10s(inValue_post_10s);
				logMnt.setInValue_post_30s(inValue_post_30s);
				
				dbJiangyinLogMntSerivce.update(logMnt);
				//System.out.println("注入当前值成功");
			}else {
				//System.out.println("值为空，但没有超过30s");
				return;
			}
		}else {
			//System.out.println("id不对应，有问题");
		}
			
	}

	@Scheduled(cron = "0/20 * * * * * ")
	public void insertMintue() {
		//每次从数据库里取三条数据
		List<DbJiangyinLogMnt>  data3 = dbJiangyinLogMntSerivce.selectLatest3();
		//Long tmp = Long.valueOf(logMnt.getTimestamp());
		Long now = new Date().getTime();
		//进行for循环
		for (DbJiangyinLogMnt dbJiangyinLogMnt : data3) {
			if(dbJiangyinLogMnt.getL0039_post_60s() != null) {
				//System.out.println("一分钟后已经有值");
			}else if (now - (Long.parseLong(dbJiangyinLogMnt.getTimestamp())-8*3600*1000)>60*1000 && dbJiangyinLogMnt!=null) {
				long post60Time = Long.parseLong(dbJiangyinLogMnt.getTimestamp())+60*1000;
				dbJiangyinLogMnt.setL0042_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0042());
				dbJiangyinLogMnt.setL0069_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0069());
				dbJiangyinLogMnt.setL0050_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0050());
				dbJiangyinLogMnt.setL0049_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0049());
				dbJiangyinLogMnt.setL0097_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0097());
				dbJiangyinLogMnt.setL0040_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0040());
				dbJiangyinLogMnt.setL0041_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0041());
				dbJiangyinLogMnt.setL0039_post_60s(dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0039());
				
				double inValue_post_60s = dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0002()+dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0005()+dbJiangyinService.selectNearestRecord(String.valueOf(post60Time)).getL0008();
				dbJiangyinLogMnt.setInValue_post_60s(inValue_post_60s);
				
				//往数据库注入数值
				dbJiangyinLogMntSerivce.updatePost60s(dbJiangyinLogMnt);
				//System.out.println("注入一分钟后的值成功");
			}else {
				//System.out.println("时间没达到");
			}
		}
			
		
			//如果1分钟后的数值不为空，则跳过
            //如果1分钟后数值为空，则判断当前时间相比时间戳是否超过一分钟
				//如果超过则注入对应的值
				//没超过则跳过
		
	}
	
	
	
	@Scheduled(cron = "0/1 * * * * * ")
	public void insertChange(){
		DbJiangyinLogChange logChange = new DbJiangyinLogChange();
		//取出最新的一条数据和其前一条数据
		DbJiangyin nowData = dbJiangyinService.selectNearestRecord2() ;
		long preTime = Long.parseLong(nowData.getTimestamp())-1000;
		DbJiangyin preData = dbJiangyinService.selectNearestRecord(String.valueOf(preTime));
		
			if (compareEntity(nowData, preData) == 1) {
				//System.out.println("相同，没变化");
				return;
			} else {
				//System.out.println("1s的判断开始");
				 logChange.setL0033_before(preData.getL0033());
				 logChange.setL0060_before(preData.getL0060());
				 logChange.setL0077_before(preData.getL0077());
				 logChange.setL0093_before(preData.getL0093());
				 logChange.setL0036_before(preData.getL0036());
				 logChange.setL0033_now(nowData.getL0033());
				 logChange.setL0060_now(nowData.getL0060());
				 logChange.setL0077_now(nowData.getL0077());
				 logChange.setL0093_now(nowData.getL0093());
				 logChange.setL0036_now(nowData.getL0036());
				 logChange.setChangeTime(nowData.getTimestamp());
				 logChange.setChangeTime24(CommonUtil.timestamp2RealDate(nowData.getTimestamp()));
				 //System.out.println("changeset成功");
				 dbJiangyinLogChangeService.insert(logChange);
				 //System.out.println("注入完成");
			}
		
	}
	
	
	@Scheduled(cron = "0 0 18 * * ?")
	public void record() {
		List<Integer> arr= new ArrayList<Integer>();
		for(int id= 11586846 ; id<=11590333;id ++) {
			DbJiangyin log1= dbJiangyinService.selectByPrimaryKey(id);
			DbJiangyin log2= dbJiangyinService.selectByPrimaryKey(id+1);
			if(compare0626(log1,log2)!=1) {
				arr.add(log2.getId());
			}
		}
		//System.out.println("arr里的id"+arr.toString());
	}
	
	
	public String compareData(Double oldData, Double newData) {
		if (oldData.equals(newData)) {
			return "no changes";
		} else if (oldData < newData) {
			return oldData+" increased to "+newData;
		} else {
			return oldData+" decreased to "+newData;
		}
	}

	public int compareEntity(DbJiangyin a, DbJiangyin b) {
		int i = 1;
		if (!a.getL0060().equals(b.getL0060())) {
			i++;
		}
			
		if (!a.getL0077().equals(b.getL0077())) {
			i++;
		}

		if (!a.getL0093().equals(b.getL0093())) {
			i++;
		}
			
		if (!a.getL0033().equals(b.getL0033()) ) {
			i++;
		}
			
		if (!a.getL0036().equals(b.getL0036())) {
			i++;
		}
			
		return i;
	}

	public static int checkEnd(DbJiangyin endEntity) {
		int diffStatus = 0;
		long diff1 = (long) Math.abs(endEntity.getL0061() / endEntity.getL0060() - 1);
		long diff2 = (long) Math.abs(endEntity.getL0078() / endEntity.getL0077() - 1);
		long diff3 = (long) Math.abs(endEntity.getL0094() / endEntity.getL0093() - 1);
		long diff4 = (long) Math.abs(endEntity.getL0034() / endEntity.getL0033() - 1);
		long diff5 = (long) Math.abs(endEntity.getL0061() / endEntity.getL0060() - 1);
		if (diff1 > 0.055)
			diffStatus++;
		if (diff2 > 0.001235)
			diffStatus++;
		if (diff3 > 0.000001)
			diffStatus++;
		if (diff4 > 0.01)
			diffStatus++;
		if (diff5 > 0.00002)
			diffStatus++;
		return diffStatus;

	}
	
	public int compare0626(DbJiangyin a, DbJiangyin b) {
		int i = 1;
		if (!a.getL0001().equals(b.getL0001())) {
			i++;
		}
			
		if (!a.getL0004().equals(b.getL0004())) {
			i++;
		}

		if (!a.getL0007().equals(b.getL0007())) {
			i++;
		}
			
		if (!a.getL0022().equals(b.getL0022()) ) {
			i++;
		}
			
		if (!a.getL0024().equals(b.getL0024())) {
			i++;
		}
		
		if (!a.getL0033().equals(b.getL0033())) {
			i++;
		}
		
		if (!a.getL0051().equals(b.getL0051())) {
			i++;
		}
		
		if (!a.getL0060().equals(b.getL0060())) {
			i++;
		}
		
		if (!a.getL0077().equals(b.getL0077())) {
			i++;
		}
		
		if(Math.abs(a.getL0158()-b.getL0158())>0.5) {
			i++;
		}
		return i;
		
	}

}
