package com.bg.web.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.util.DateUtil;
import com.bg.web.dao.IAgentDao;
import com.bg.web.service.IAgentService;

@Service("agentService")
public class AgentServiceImpl extends CommonServiceImpl implements IAgentService {
	private Logger logger = Logger.getLogger(AgentServiceImpl.class);
	@Autowired
	private IAgentDao agentDao;

	@Override
	public List<Map<String, Object>> findTransList(String accountId, String date) {
		List<Map<String,Object>> list = null;
		try {
			String edate = DateUtil.getRelativeDate(date, DateUtil.TERM_UNIT_DAY, 1);
			System.out.println(date+":"+edate);
			list = agentDao.findTransList(accountId,date,edate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean isAgentCust(String accountId, String agentAccountId) {
		return agentDao.isAgentCust(accountId, agentAccountId);
	}

	@Override
	public List<Map<String, Object>> countTeamTrans(String accountId,String date) {
		List<Map<String,Object>> list = null;
		try {
			
			String edate = DateUtil.getRelativeDate(date, DateUtil.TERM_UNIT_MONTH, 1);
			System.out.println(date+"++++++++"+edate+"++++++++"+accountId);
			list=agentDao.countTeamTrans(accountId,date,edate);	
			System.out.println(list.size());
			if(list.size()>0){
				DecimalFormat df = new DecimalFormat("0.00");				
				List<Map<String,Object>> sublist = agentDao.findSubAccountTrans(accountId,date,edate);
				Iterator<Map<String,Object>> it = list.iterator();
				while(it.hasNext()){
				    Map<String,Object> row = it.next();
				    boolean y = false;
					String account = (String)row.get("SUBACCOUNT");
					String ta = "*****"+account.substring(account.length()-4);			
					row.put("TACCOUNT", ta);
					int tamt = ((BigDecimal)row.get("TAMT")).intValue();
					row.put("STAMT", df.format(tamt/100d));
					for(Map<String,Object> sr:sublist){
						String sa = (String)sr.get("ACCOUNT");		
						if(sa.equals(account)){
							row.put("NAME", sr.get("NAME"));
							int bamt = ((BigDecimal)sr.get("TAMT")).intValue();
							row.put("BAMT", df.format(bamt/100d));
							y=true;
							break;
						}							
					}
					if(!y){
						row.put("NAME", "");
						row.put("BAMT", "0.00");
//						it.remove();
					}
				}
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] arg){
		for(int i=0;i<3;i++){
			System.out.println(i);
			for(int j=10;j<20;j++){
				System.out.println(j);
				if(j==15){
					break;
				}
			}
		}
	}
	
}
