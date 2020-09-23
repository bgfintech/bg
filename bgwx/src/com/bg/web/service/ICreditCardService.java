package com.bg.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author YH
 * @date 2018年7月6日
 * @version 1.0
 */
public interface ICreditCardService {
	/**
	 * 根据账号查看绑定的信用卡信息
	 * @param accountId
	 * @return
	 */
	List<Map<String,Object>> findBindCreditCardList(String accountId);
	/**
	 * 根据ID查看信用卡代还计划
	 * @param bankCardNo
	 * @return
	 */
	List<Map<String,Object>> findRepayPlanByPlanId(String planId);
	/**
	 * 
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
	 * 
	 * @param cardno
	 * @return
	 */
	boolean isSetRepayPlanByCardno(String cardno);
	/**
	 * 查询要执行的还款计划
	 * @param strdate 
	 * @return
	 */
	List<Map<String, Object>> findRunRepayPlan(String strdate);
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
