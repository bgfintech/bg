package com.bg.web.dao;

import java.util.List;
import java.util.Map;

public interface IAgentDao {

	List<Map<String, Object>> findTransList(String accountId, String sdate,String edate);

	boolean isAgentCust(String accountId,String agentAccountId);
	/**
	 * 统计用户团队交易情况
	 * @param accountId
	 * @param date
	 * @param edate
	 * @return MAP:
	 * 			ACCOUNT:账号
	 * 	   		CNUM:团队人数
	 * 			TAMT:交易总额
	 */
	List<Map<String, Object>> countTeamTrans(String accountId, String date,String edate);
	/**
	 * 1级下线的交易量统计
	 * @param accountId
	 * @param date
	 * @param edate
	 * @return MAP:
	 * 			  ACCOUNT:
	 * 			  TAMT:
	 */
	List<Map<String, Object>> findSubAccountTrans(String accountId,String date, String edate);
}
