package com.bg.web.service;

import java.util.List;
import java.util.Map;


public interface ICostService {
	
	/**
	 * 账单明细分页
	 * @param account
	 * @param pager
	 * @return
	 */
	List<Map<String,Object>> findCostList(String account,int offset,int limit);
	/**
	 * 账单明细总条数
	 * @param account
	 * @return
	 */
	int countCostList(String account);
	
	/**
	 * 
	 * @param account
	 * @param type
	 * @param linkno
	 * @param balance
	 * @param fee
	 */
	void recharge(String account, String type, String linkno,double fee);
	
	
}
