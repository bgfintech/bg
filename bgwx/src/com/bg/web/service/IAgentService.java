package com.bg.web.service;

import java.util.List;
import java.util.Map;

public interface IAgentService {
	/**
	 * 查询客户交易详情
	 * @param accountId
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> findTransList(String accountId, String date);
	/**
	 * 判断客户是否为该渠道代理
	 * @param accountId
	 * @param agentAccountId
	 * @return
	 */
	boolean isAgentCust(String accountId,String agentAccountId);
	/**
	 * 统计用户团队交易
	 * @param accountId
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> countTeamTrans(String accountId, String date);

}
