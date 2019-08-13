/**
 * @author Chnyge Lin
 * @time 2018-8-2
 * @comment 
 */
package com.mktech.task;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mktech.entity.DbDaying1;
import com.mktech.entity.DbDaying2;
import com.mktech.entity.DbJiangyin;
import com.mktech.entity.DbLimo;
import com.mktech.entity.InfoLine;
import com.mktech.service.impl.DbDaying1ServiceImpl;
import com.mktech.service.impl.DbDaying2ServiceImpl;
import com.mktech.service.impl.DbJiangyinServiceImpl;
import com.mktech.service.impl.DbLimoServiceImpl;
import com.mktech.service.impl.InfoLineServiceImpl;


/**
 * @time 2018-8-2
 * @comment 用于自动刷新infoLine表中的部分数据（从工位表内）
 */
@Component
public class InfoLineTimerTask {
	@Resource
	private InfoLineServiceImpl infoLineService;
	
	@Resource
	private DbLimoServiceImpl dbLimoService; 
	
	@Resource
	private DbJiangyinServiceImpl dbJiangyinService;
	
	@Resource
	private DbDaying1ServiceImpl dbDaying1Service;
	
	@Resource
	private DbDaying2ServiceImpl dbDaying2Service;
	
	@Scheduled(cron = "0/3 * * * * * ")
	public void refreshDbLimo(){
		long tmp = new Date().getTime();
		InfoLine i = infoLineService.selectByPrimaryKey(1);
		DbLimo o = dbLimoService.selectNearestRecord2();
		long latest = Long.valueOf(o.getTimestamp());//数据表内最后一条的时间戳
		InfoLine n = new InfoLine();
		n.setId(1);
		long new_tmp = tmp+8*3600*1000;//将当前+8h进行比较
		//对于待维修产线的处理方法
		if(i.getStatus()==-1){
			n.setStatus(-1);
			n.setLastUpdate(i.getLastUpdate());
			n.setLastBegin(i.getLastBegin());
			n.setNumIn((double) 0);
			n.setNumOut((double) 0);
			infoLineService.updateByTask(n);
			return;
		}
		//先判断最近数据是否已经更新入表
		if(Long.valueOf(i.getLastUpdate())==latest){
			if((new_tmp-latest)<=60000){//时间短，来不及更新
				return;
			}else{//大于10s，判断可能已经停工
				//System.out.println("停工了！");
				n.setStatus(0);
				n.setLastUpdate(i.getLastUpdate());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn((double)0);
				n.setNumOut((double) 0);
				infoLineService.updateByTask(n);
				return;
			}
		}else{//info内不是最新
			if(latest-Long.valueOf(i.getLastUpdate())<=60000){//连续运行状态
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn(o.getL0104()+o.getL0105()+o.getL0106()+o.getL0107()+o.getL0108()+o.getL0109());
				n.setNumOut(o.getL0145());
				infoLineService.updateByTask(n);
				return;
			}else{
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(o.getTimestamp());
				n.setNumIn(o.getL0104()+o.getL0105()+o.getL0106()+o.getL0107()+o.getL0108()+o.getL0109());
				n.setNumOut(o.getL0145());
				infoLineService.updateByTask(n);	
				return;
			}
		}
	}
//	判断当前生产状态
	@Scheduled(cron = "0/3 * * * * * ")
	public void refreshDbJiangyin(){
		long tmp = new Date().getTime();
		//取出line表里对应江阴的数据
		InfoLine i = infoLineService.selectByPrimaryKey(5);
		//取出jiangyin数据表里最新的那条数据
		DbJiangyin o  = dbJiangyinService.selectNearestRecord2();
		//数据表内最后一条的时间戳
		long latest = Long.valueOf(o.getTimestamp());//数据表内最后一条的时间戳
		InfoLine n = new InfoLine();
		n.setId(5);
		long new_tmp = tmp+8*3600*1000;//将当前+8h进行比较
		//对于待维修产线的处理方法
		//生产线处于维修，需要企业告知是否修好，否则不进行更新
		if(i.getStatus()==-1){
			n.setStatus(-1);
			n.setLastUpdate(i.getLastUpdate());
			n.setLastBegin(i.getLastBegin());
			n.setNumIn((double) 0);
			n.setNumOut((double) 0);
			infoLineService.updateByTask(n);
			return;
		}
		//先判断最近数据是否已经更新入表
		//lastupdate和数据表里最近的一条记录时间相同
		if(Long.valueOf(i.getLastUpdate())==latest){
			if((new_tmp-latest)<=600000){//时间短，来不及更新
				return;
			}else{//大于10min，判断可能已经停工
				//System.out.println("停工了！");
				n.setStatus(0);
				n.setLastUpdate(i.getLastUpdate());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn((double)0);
				n.setNumOut((double) 0);
				infoLineService.updateByTask(n);
				return;
			}
		}else{//lastupdate和数据表里最近的一条记录时间相同，info内不是最新
			if(latest-Long.valueOf(i.getLastUpdate())<=600000){//连续运行状态
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn(o.getL0002()+o.getL0005()+o.getL0008());
				n.setNumOut((o.getL0002()+o.getL0005()+o.getL0008())*0.9);
				infoLineService.updateByTask(n);
				return;
			}else{
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(o.getTimestamp());
				n.setNumIn(o.getL0002()+o.getL0005()+o.getL0008());
				n.setNumOut((o.getL0002()+o.getL0005()+o.getL0008())*0.9);
				infoLineService.updateByTask(n);	
				return;
			}
		}
	}
	
	@Scheduled(cron = "0/3 * * * * * ")
	public void refreshDbDaying1(){
		long tmp = new Date().getTime();
		//取出line表里对应江阴的数据
		InfoLine i = infoLineService.selectByPrimaryKey(3);
		//取出Daying1数据表里最新的那条数据
		DbDaying1 o  = dbDaying1Service.selectNearestRecord2();
		//数据表内最后一条的时间戳
		long latest = Long.valueOf(o.getTimestamp());//数据表内最后一条的时间戳
		InfoLine n = new InfoLine();
		n.setId(3);
		long new_tmp = tmp+8*3600*1000;//将当前+8h进行比较
		//对于待维修产线的处理方法
		//生产线处于维修，需要企业告知是否修好，否则不进行更新
		if(i.getStatus()==-1){
			n.setStatus(-1);
			n.setLastUpdate(i.getLastUpdate());
			n.setLastBegin(i.getLastBegin());
			n.setNumIn((double) 0);
			n.setNumOut((double) 0);
			infoLineService.updateByTask(n);
			return;
		}
		//先判断最近数据是否已经更新入表
		//lastupdate和数据表里最近的一条记录时间相同
		if(Long.valueOf(i.getLastUpdate())==latest){
			if((new_tmp-latest)<=600000){//时间短，来不及更新
				return;
			}else{//大于10min，判断可能已经停工
				//System.out.println("停工了！");
				n.setStatus(0);
				n.setLastUpdate(i.getLastUpdate());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn((double)0);
				n.setNumOut((double) 0);
				infoLineService.updateByTask(n);
				return;
			}
		}else{//lastupdate和数据表里最近的一条记录时间相同，info内不是最新
			if(latest-Long.valueOf(i.getLastUpdate())<=600000){//连续运行状态
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn(o.getL0002()+o.getL0005());
				n.setNumOut((o.getL0002()+o.getL0005())*0.9);
				infoLineService.updateByTask(n);
				return;
			}else{
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(o.getTimestamp());
				n.setNumIn(o.getL0002()+o.getL0005());
				n.setNumOut((o.getL0002()+o.getL0005())*0.9);
				infoLineService.updateByTask(n);	
				return;
			}
		}
	}
	
	@Scheduled(cron = "0/3 * * * * * ")
	public void refreshDbDaying2(){
		long tmp = new Date().getTime();
		//取出line表里对应江阴的数据
		InfoLine i = infoLineService.selectByPrimaryKey(3);
		//取出Daying1数据表里最新的那条数据
		DbDaying2 o  = dbDaying2Service.selectNearestRecord2();
		//数据表内最后一条的时间戳
		long latest = Long.valueOf(o.getTimestamp());//数据表内最后一条的时间戳
		InfoLine n = new InfoLine();
		n.setId(4);
		long new_tmp = tmp+8*3600*1000;//将当前+8h进行比较
		//对于待维修产线的处理方法
		//生产线处于维修，需要企业告知是否修好，否则不进行更新
		if(i.getStatus()==-1){
			n.setStatus(-1);
			n.setLastUpdate(i.getLastUpdate());
			n.setLastBegin(i.getLastBegin());
			n.setNumIn((double) 0);
			n.setNumOut((double) 0);
			infoLineService.updateByTask(n);
			return;
		}
		//先判断最近数据是否已经更新入表
		//lastupdate和数据表里最近的一条记录时间相同
		if(Long.valueOf(i.getLastUpdate())==latest){
			if((new_tmp-latest)<=600000){//时间短，来不及更新
				return;
			}else{//大于10min，判断可能已经停工
				//System.out.println("停工了！");
				n.setStatus(0);
				n.setLastUpdate(i.getLastUpdate());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn((double)0);
				n.setNumOut((double) 0);
				infoLineService.updateByTask(n);
				return;
			}
		}else{//lastupdate和数据表里最近的一条记录时间相同，info内不是最新
			if(latest-Long.valueOf(i.getLastUpdate())<=600000){//连续运行状态
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn(o.getL0002()+o.getL0005());
				n.setNumOut((o.getL0002()+o.getL0005())*0.9);
				infoLineService.updateByTask(n);
				return;
			}else{
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(o.getTimestamp());
				n.setNumIn(o.getL0002()+o.getL0005());
				n.setNumOut((o.getL0002()+o.getL0005())*0.9);
				infoLineService.updateByTask(n);	
				return;
			}
		}
	}
	
/*	@Scheduled(cron = "0/3 * * * * * ")
	public void refreshDbZhongTian(){
		long tmp = new Date().getTime();
		//取出line表里对应江阴的数据
		InfoLine i = infoLineService.selectByPrimaryKey(5);
		//取出jiangyin数据表里最新的那条数据
		DbJiangyin o  = dbJiangyinService.selectNearestRecord2();
		//数据表内最后一条的时间戳
		long latest = Long.valueOf(o.getTimestamp());//数据表内最后一条的时间戳
		InfoLine n = new InfoLine();
		n.setId(16);
		long new_tmp = tmp+8*3600*1000;//将当前+8h进行比较
		//对于待维修产线的处理方法
		//生产线处于维修，需要企业告知是否修好，否则不进行更新
		if(i.getStatus()==-1){
			n.setStatus(-1);
			n.setLastUpdate(i.getLastUpdate());
			n.setLastBegin(i.getLastBegin());
			n.setNumIn((double) 0);
			n.setNumOut((double) 0);
			infoLineService.updateByTask(n);
			return;
		}
		//先判断最近数据是否已经更新入表
		//lastupdate和数据表里最近的一条记录时间相同
		if(Long.valueOf(i.getLastUpdate())==latest){
			if((new_tmp-latest)<=600000){//时间短，来不及更新
				return;
			}else{//大于10min，判断可能已经停工
				//System.out.println("停工了！");
				n.setStatus(0);
				n.setLastUpdate(i.getLastUpdate());
				n.setLastBegin(i.getLastBegin());
				n.setNumIn((double)0);
				n.setNumOut((double) 0);
				infoLineService.updateByTask(n);
				return;
			}
		}else{//lastupdate和数据表里最近的一条记录时间相同，info内不是最新
			if(latest-Long.valueOf(i.getLastUpdate())<=600000){//连续运行状态
				long temp = Long.valueOf(i.getLastBegin());
				String fakeTime = String.valueOf(temp - 3600*1000*11.46);
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(fakeTime);
				n.setNumIn((o.getL0002()+o.getL0005()+o.getL0008()+1)*0.84);
				n.setNumOut((o.getL0002()+o.getL0005()+o.getL0008())*0.79);
				infoLineService.updateByTask(n);
				return;
			}else{
				n.setStatus(1);
				n.setLastUpdate(o.getTimestamp());
				n.setLastBegin(o.getTimestamp());
				n.setNumIn((o.getL0002()+o.getL0005()+o.getL0008()+1)*0.84);
				n.setNumOut((o.getL0002()+o.getL0005()+o.getL0008())*0.79);
				infoLineService.updateByTask(n);	
				return;
			}
		}
	}*/
//	
//	/**
//	 * 自动向立磨的小时表添加数据
//	 */
//	@Scheduled(cron = "0/2 * * * * * ")
//	public void refreshLimoHour(){
//		DbLimo li = dbLimoService.selectfromDbLimoHour();//取最近的
//		if(li==null){
//			Long tmp = Long.valueOf("1522519200000");
//			String min = String.valueOf(tmp);
//			String max = String.valueOf(tmp+3600*1000);
//			DbLimo limo = dbLimoService.selectByTimeRangelimit1(min, max);
//			limo.setId(1);
//			limo.setTimestamp(min);
//			dbLimoService.insertIntoHour(limo);
//			System.out.println("sucess!1");
//		}else{
//			Long tmp = Long.valueOf(li.getTimestamp())+3600*1000;
//			Long now = new Date().getTime();
//			if(tmp-8*3600*1000>now){
//				return;
//			}
//			String min = String.valueOf(tmp);
//			String max = String.valueOf(tmp+3600*1000);
//			DbLimo limo = dbLimoService.selectByTimeRangelimit1(min, max);
//			if(limo==null){
//				DbLimo newLimo = new DbLimo();
//				newLimo.setId(li.getId()+1);
//				newLimo.setTimestamp(min);
//				dbLimoService.insertIntoHour(newLimo);
//			}else{
//				limo.setId(li.getId()+1);
//				limo.setTimestamp(min);
//				dbLimoService.insertIntoHour(limo);
//				System.out.println("hour!"+limo.getId());
//			}
//			
//		}
//	}

//	/**
//	 * 自动向立磨的天数表添加数据
//	 */
//	@Scheduled(cron = "0/5 * * * * * ")
//	public void refreshLimoDay(){
//		DbLimo li = dbLimoService.selectfromDbLimoDay();
//		if(li==null){
//			Long tmp = Long.valueOf("1522454400000");
//			String min = String.valueOf(tmp);
//			String max = String.valueOf(tmp+24*3600*1000);
//			DbLimo limo = dbLimoService.selectByTimeRangelimit1(min, max);
//			limo.setId(1);
//			limo.setTimestamp(min);
//			dbLimoService.insertIntoDay(limo);
//			System.out.println("day!1");
//		}else{
//			Long tmp = Long.valueOf(li.getTimestamp())+24*3600*1000;
//			Long now = new Date().getTime();
//			if(tmp-8*3600*1000>now){
//				return;
//			}
//			String min = String.valueOf(tmp);
//			String max = String.valueOf(tmp+24*3600*1000);
//			DbLimo limo = dbLimoService.selectByTimeRangelimit1(min, max);
//			if(limo==null){
//				DbLimo newLimo = new DbLimo();
//				newLimo.setId(li.getId()+1);
//				newLimo.setTimestamp(min);
//				dbLimoService.insertIntoDay(newLimo);
//			}else{
//				limo.setId(li.getId()+1);
//				limo.setTimestamp(min);
//				dbLimoService.insertIntoDay(limo);
//				System.out.println("day!"+limo.getId());
//			}
//			
//		}
//	}
//	
//	/**
//	 * 自动向立磨的分钟表添加数据
//	 */
//	@Scheduled(cron = "0/2 * * * * * ")
//	public void refreshLimoMin(){
//		DbLimo li = dbLimoService.selectfromDbLimoMin();
//		Long tmp = Long.valueOf(li.getTimestamp())+60*1000;
//		Long now = new Date().getTime();
//		while(tmp-8*3600*1000<=now){
//			String min = String.valueOf(tmp);
//			String max = String.valueOf(tmp+60*1000);
//			DbLimo limo = dbLimoService.selectByTimeRangelimit1(min, max);
//			if(limo==null){
//				DbLimo newLimo = new DbLimo();
//				newLimo.setId(li.getId()+1);
//				newLimo.setTimestamp(min);
//				dbLimoService.insertIntoMin(newLimo);
//				System.out.println("min null!"+newLimo.getId());
//			}else{
//				limo.setId(li.getId()+1);
//				limo.setTimestamp(min);
//				dbLimoService.insertIntoMin(limo);
//				System.out.println("min!"+limo.getId());
//			}
//			li = dbLimoService.selectfromDbLimoMin();
//			tmp = Long.valueOf(li.getTimestamp())+60*1000;
//		}
//			
//			
//		
//	}
	
	/**
	 * 自动向江阴的分钟表添加数据
	 */
	@Scheduled(cron = "0/10 * * * * * ") //cron = "0/10 * * * * ?"
	public void refreshJiangyinMins(){
		DbJiangyin li = dbJiangyinService.selectfromDbJiangyinMin();
		if(li==null){
			Long tmp = Long.valueOf("1534809600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			jy.setId(1);
			jy.setTimestamp(min);
			dbJiangyinService.insertIntoMin(jy);
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+60*1000;
			Long now = new Date().getTime();
			if(tmp-8*3600*1000+60*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			//取范围内平均数
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			if(jy==null){//塞入空记录
				DbJiangyin newjy = new DbJiangyin();
				newjy.setId(li.getId()+1);
				newjy.setTimestamp(min);
				dbJiangyinService.insertIntoMin(newjy);
			}else{
				jy.setId(li.getId()+1);
				jy.setTimestamp(min);
				dbJiangyinService.insertIntoMin(jy);
			}
		}
	}
	/**
	 * 自动向江阴的小时表添加数据
	 */
	@Scheduled(cron = "* 0/15 * * * * ")
	public void refreshJiangyinHour(){
		DbJiangyin li = dbJiangyinService.selectfromDbJiangyinHour();
		if(li==null){
			Long tmp = Long.valueOf("1534809600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			jy.setId(1);
			jy.setTimestamp(min);
			dbJiangyinService.insertIntoHour(jy);
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+3600*1000;
			Long now = new Date().getTime();
			if(tmp-7*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			//取范围内平均数
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			if(jy==null){
				DbJiangyin newjy = new DbJiangyin();
				newjy.setId(li.getId()+1);
				newjy.setTimestamp(min);
				dbJiangyinService.insertIntoHour(newjy);
			}else{
				jy.setId(li.getId()+1);
				jy.setTimestamp(min);
				dbJiangyinService.insertIntoHour(jy);
			}
			
		}
	}
	/**
	 * 自动向江阴的天数表添加数据
	 */
	@Scheduled(cron = "* * 0/4 * * * ")
	public void refreshJiangyinDay(){
		DbJiangyin li = dbJiangyinService.selectfromDbJiangyinDay();
		if(li==null){
			Long tmp = Long.valueOf("1534809600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			jy.setId(1);
			jy.setTimestamp(min);
			dbJiangyinService.insertIntoDay(jy);
			System.out.println("day!1");
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+24*3600*1000;
			Long now = new Date().getTime();
			if(tmp+16*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			//取范围内平均数
			DbJiangyin jy = dbJiangyinService.selectByTimeRangelimit1(min, max);
			if(jy==null){
				DbJiangyin newjy = new DbJiangyin();
				newjy.setId(li.getId()+1);
				newjy.setTimestamp(min);
				dbJiangyinService.insertIntoDay(newjy);
			}else{
				jy.setId(li.getId()+1);
				jy.setTimestamp(min);
				dbJiangyinService.insertIntoDay(jy);
				System.out.println("day!"+jy.getId());
			}
			
		}
	}
	
	/**
	 * 自动向大营1的分钟表添加数据
	 */
	@Scheduled(cron = "0/10 * * * * * ") //cron = "0/10 * * * * ?"
	public void refreshDaying1Mins(){
		DbDaying1 li = dbDaying1Service.selectfromDbDaying1Min();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying1Service.insertIntoMin(dy);
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+60*1000;
			Long now = new Date().getTime();
			if(tmp-8*3600*1000+60*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			//取范围内平均数
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			if(dy==null){//塞入空记录
				DbDaying1 newdy = new DbDaying1();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying1Service.insertIntoMin(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying1Service.insertIntoMin(dy);
			}
		}
	}
	/**
	 * 自动向大营1的小时表添加数据
	 */
	@Scheduled(cron = "* 0/15 * * * * ")
	public void refreshDaying1Hour(){
		DbDaying1 li = dbDaying1Service.selectfromDbDaying1Hour();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying1Service.insertIntoHour(dy);
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+3600*1000;
			Long now = new Date().getTime();
			if(tmp-7*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			//取范围内平均数
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			if(dy==null){
				DbDaying1 newdy = new DbDaying1();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying1Service.insertIntoHour(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying1Service.insertIntoHour(dy);
			}
			
		}
	}
	/**
	 * 自动向大营1的天数表添加数据
	 */
	@Scheduled(cron = "* * 0/4 * * * ")
	public void refreshDaying1Day(){
		DbDaying1 li = dbDaying1Service.selectfromDbDaying1Day();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying1Service.insertIntoDay(dy);
			System.out.println("day!1");
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+24*3600*1000;
			Long now = new Date().getTime();
			if(tmp+16*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			//取范围内平均数
			DbDaying1 dy = dbDaying1Service.selectByTimeRangelimit1(min, max);
			if(dy==null){
				DbDaying1 newdy = new DbDaying1();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying1Service.insertIntoDay(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying1Service.insertIntoDay(dy);
				System.out.println("day!"+dy.getId());
			}
			
		}
	}
	
	/**
	 * 自动向大营2的分钟表添加数据
	 */
	@Scheduled(cron = "0/10 * * * * * ") //cron = "0/10 * * * * ?"
	public void refreshDaying2Mins(){
		DbDaying2 li = dbDaying2Service.selectfromDbDaying2Min();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying2Service.insertIntoMin(dy);
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+60*1000;
			Long now = new Date().getTime();
			if(tmp-8*3600*1000+60*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+60*1000);
			//取范围内平均数
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			if(dy==null){//塞入空记录
				DbDaying2 newdy = new DbDaying2();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying2Service.insertIntoMin(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying2Service.insertIntoMin(dy);
			}
		}
	}
	/**
	 * 自动向大营2的小时表添加数据
	 */
	@Scheduled(cron = "* 0/15 * * * * ")
	public void refreshDaying2Hour(){
		DbDaying2 li = dbDaying2Service.selectfromDbDaying2Hour();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying2Service.insertIntoHour(dy);
			System.out.println("Hour!1");
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+3600*1000;
			Long now = new Date().getTime();
			if(tmp-7*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+3600*1000);
			//取范围内平均数
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			if(dy==null){
				DbDaying2 newdy = new DbDaying2();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying2Service.insertIntoHour(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying2Service.insertIntoHour(dy);
				System.out.println("hour!"+dy.getId());
			}
			
		}
	}
	/**
	 * 自动向大营2的天数表添加数据
	 */
	@Scheduled(cron = "* * 0/4 * * * ")
	public void refreshDaying2Day(){
		DbDaying2 li = dbDaying2Service.selectfromDbDaying2Day();
		if(li==null){
			Long tmp = Long.valueOf("1559001600000");
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			dy.setId(1);
			dy.setTimestamp(min);
			dbDaying2Service.insertIntoDay(dy);
			System.out.println("day!1");
		}else{
			Long tmp = Long.valueOf(li.getTimestamp())+24*3600*1000;
			Long now = new Date().getTime();
			if(tmp+16*3600*1000>now){
				return;
			}
			String min = String.valueOf(tmp);
			String max = String.valueOf(tmp+24*3600*1000);
			//取范围内平均数
			DbDaying2 dy = dbDaying2Service.selectByTimeRangelimit1(min, max);
			if(dy==null){
				DbDaying2 newdy = new DbDaying2();
				newdy.setId(li.getId()+1);
				newdy.setTimestamp(min);
				dbDaying2Service.insertIntoDay(newdy);
			}else{
				dy.setId(li.getId()+1);
				dy.setTimestamp(min);
				dbDaying2Service.insertIntoDay(dy);
				System.out.println("day!"+dy.getId());
			}
			
		}
	}
	
}
