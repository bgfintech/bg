package com.bg.batch.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ser.StdSerializers.UtilDateSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.batch.dao.ICountMonthBrokerageDao;
import com.bg.batch.entity.BrokerageEntity;
import com.bg.batch.entity.TaskConstants;
import com.bg.batch.service.AbstractBaseBatch;

@Service("CountMonthBrokerage")
public class CountMonthBrokerage extends AbstractBaseBatch {
	@Autowired
	private ICountMonthBrokerageDao countMonthBrokerageDao;

	@Override
	protected String process() throws Exception {
		logger.info("计算月返佣开始......");
		String date = this.getDeductDate();
		if(!date.substring(6,2).equals("01")){
			return TaskConstants.TaskResult.TASK_SUCCESS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date edate = sdf.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(edate);
		c.add(Calendar.MONTH, -1);
		Date sdate = c.getTime();
		//计算各分销下属三级当月交易金额及所达返佣比例
		List<Map<String,Object>> list = countMonthBrokerageDao.QueryCountTeamTranAmt(sdate, edate);
		//个人月刷卡金额
		List<Map<String,Object>> tranList = countMonthBrokerageDao.queryAccountTranAmt(sdate,edate);
		//当月有交易的人员它的上级人员
		List<Map<String,Object>> recommenderList = countMonthBrokerageDao.queryRecommenderList();
		Map<String,BrokerageEntity> accountBrokerage = new HashMap<String,BrokerageEntity>();
		//当月所有下级有交易金额有返佣的人员集合
		for(Map<String,Object> m:list){
			BrokerageEntity be = new BrokerageEntity();
			String account = (String)m.get("ACCOUNT");
			int amt = (int)m.get("AMT");
			be.setAccountId(account);
			be.setTeamTranAmt(amt);
			be.setRate(getRate(amt));
			accountBrokerage.put(account, be);			
		}
		//所有人员的上级集合 转MAP方便查询
		Map<String,BrokerageEntity> recommenders = new HashMap<String,BrokerageEntity>();
		for(Map<String,Object> r:recommenderList){
			
		}		
		logger.info("计算月返佣结束......");		
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}
	
	private double getRate(int amt){
		double rate = 0d;
		if(amt>7*10000*10000){
			rate = 0.0014;
		}else if(amt>5*10000*10000){
			rate = 0.0013;
		}else if(amt>3*10000*10000){
			rate = 0.0012;
		}else if(amt>15000*10000){
			rate = 0.0011;
		}else if(amt>8*1000*10000){
			rate = 0.0010;
		}else if(amt>4*1000*10000){
			rate = 0.0009;
		}else if(amt>2*1000*10000){
			rate = 0.0008;
		}else if(amt>8*100*10000){
			rate = 0.0007;
		}else if(amt>3*100*10000){
			rate = 0.0006;
		}else{
			rate = 0.0005;
		}
		return rate;
	}
	
	
	/*********************
	public static void main(String[] arg){
		Map<String,Integer> s = new HashMap();
		s.put("a", 1);
		s.put("b", 2);
		s.put("c", 3);
		s.put("d", 4);
		s.put("e", 5);
//		s.put("f", 6);
//		s.put("g", 7);
//		s.put("h", 8);
//		s.put("i", 9);
//		s.put("j", 10);
		Map<String,String> g = new HashMap();
//		g.put("j", "i");
//		g.put("i", "h");
//		g.put("h", "g");
//		g.put("g", "f");
//		g.put("f", "e");
		g.put("e", "d");
		g.put("d", "c");
		g.put("c", "a");
		g.put("b", "a");
		Map<String,Integer> t = new HashMap();
		for(String str:s.keySet()){
			t.put(str, 0);
		}
		
		List<Map> upl = new ArrayList();
		Map<String,Integer> up = new HashMap();
		for(String str:s.keySet()){				
			int tmp = s.get(str);
			System.out.println("str0:"+str+":"+tmp);
			String f =  g.get(str);	
			System.out.println("f1:"+f);
			if(f!=null){				
				int tmp1 = t.get(f);
				System.out.println("f0:"+f+":"+tmp1);
				t.put(f, tmp+tmp1);				
				up.put(f, 1);
			}			
		}	
		upl.add(up);
		int iup = 0;
		while(s.size()>0){		
			System.out.println(iup);
			Map tup = upl.get(iup);
			Map<String,Integer> nup = new HashMap();
			Set<Entry<String, Integer>> set=s.entrySet();
			Iterator<Entry<String, Integer>> iterator=set.iterator();
			while(iterator.hasNext()){
			    Entry<String, Integer> entry=iterator.next();
			    String str=entry.getKey();
			    System.out.println("Str:"+str);
			    if(tup.get(str)==null){
			    	String f =  g.get(str);
			    	if(f!=null){
				    	int tmp = t.get(str);
						int tmp1 = t.get(f);
						t.put(f, tmp+tmp1);
					}
			    	System.out.println("remove:"+str);
			    	iterator.remove();
				}else{
					String f =  g.get(str);
					System.out.println("f:"+f);
					if(f!=null){
						nup.put(f, 1);
					}
					
				}				
			}
			upl.add(nup);
			iup++;
		}		
		for(String str:t.keySet()){
			System.out.println(str+":"+t.get(str));
		}	
	}
	*******************************/

}
