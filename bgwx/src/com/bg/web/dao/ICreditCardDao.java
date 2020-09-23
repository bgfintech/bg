package com.bg.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICreditCardDao {
	/**
	 * 
	 * @param accountId
	 * @return
	 */
	List<Map<String,Object>> findBindCreditCardList(String accountId);
	/**
	 * 根据卡号查看信用卡代还计划
	 * @param bankCardNo
	 * @return
	 */
	List<Map<String, Object>> findRepayPlanByPlanId(String planId);
	/**
	 * 根据卡号查询卡信息
	 * @param cardno
	 * @return
	 */
	Map<String, Object> findCardInfoByCardno(String cardno);
	/**
	 * 
	 * @param datas
	 */
	void createBatchRepayPlan(List<Object[]> datas);
	/**
	 * 是否已设置还款计划
	 * @param cardno
	 * @return
	 */
	boolean isSetRepayPlanByCardno(String cardno);
	/**
	 * 
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> findRunRepayPlan(String date);
	/**
	 * 
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> findRunPayPlan(String date);
	/**
	 * 
	 * @param pid
	 * @param status
	 * @param date
	 */
	void updateInsteadRepayPlan(int pid, String status, Date date);
}
