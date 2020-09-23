package com.bg.interfaces.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bg.interfaces.entity.Trans1100Rsp;

public interface IBPEntDao {
	/**
	 * 查询账户消费记录
	 * @param account
	 * @param sdate
	 * @param edate
	 * @return
	 */
	List<Map<String,Object>> findAccountDetail(String account,String sdate,String edate);
	
	double findProductFee(String account,String type);
	/**
	 * 查询涉诉信息列表
	 * @param account
	 * @param sdate
	 * @return
	 */
	List<Map<String,Object>> findTransList(String account,Date sdate);
	/**
	 * 
	 * @param name
	 * @param idcard
	 * @param sdate
	 * @param edate
	 * @param account
	 */
	int setMonitor(String name, String idcard, Date sdate, Date edate,String account);
	/**
	 * 增加监控记录
	 * @param name
	 * @param idcard
	 * @param account
	 * @param rspdata
	 * @param date
	 */
	void insertMontiorLog(String name, String idcard, String account,String rspdata, Date date,int mid);
	/**
	 * 更新监控记录
	 * @param id
	 * @param rspdata
	 * @param date
	 */
	void updateMontiorLog(int id,String rspdata,Date date);
	/**
	 * 
	 * @param account
	 * @return
	 */
	int countMonitorList(String account);
	/**
	 * 
	 * @param account
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> findMonitorList(String account, int offset,int limit);
}
