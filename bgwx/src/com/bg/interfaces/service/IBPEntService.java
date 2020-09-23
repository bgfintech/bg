package com.bg.interfaces.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bg.interfaces.entity.Trans1100Rsp;

public interface IBPEntService {
	/**
	 * 
	 * @param name
	 * @param id
	 * @param type
	 * @param t
	 * @param account
	 * @return
	 */
	Object entstatService(String name,String id,String type,String t,String account);
	/**
	 * 
	 * @param name
	 * @param id
	 * @param type
	 * @param t
	 * @param account
	 * @return
	 */
	Object entoutService(String name,String id,String type,String t,String account);
	
	/**
	 * 消费记录返回Json
	 * {"balance":"0.00",
	 * 	 "tranlist":[{"transtime":"2019年10年11日 14:14:14",
	 * 				  "type":"风险信息统计",
	 * 				  "amt":"0.00"
	 * 				},{...}]
	 * } 
	 * @param account
	 * @param sdate
	 * @param edate
	 * @return
	 */
	String expensesRecord(String account,String sdate,String edate);
	/**
	 * 查询缓存信息
	 * @param account
	 * @return
	 */
	Object entstatCache(String account,String name,String id);
	/**
	 * 查询缓存信息
	 * @param account
	 * @return
	 */
	Object entoutCache(String account,String name,String id);
	
	/**
	 * 查询最近1小时内能查询涉诉列表
	 * @param account
	 * @return
	 */
	List<Map<String,Object>> findTransListDesc(String account);
	/**
	 * 涉诉提示列表统计
	 * @param account
	 * @return
	 */
	int countMonitorList(String account);
	/**
	 * 涉诉提示列表分页
	 * @param account
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> findMonitorList(String account, int offset, int limit);
	/**
	 * 
	 * @param name
	 * @param idcard
	 * @return
	 */
	boolean isMonitorByCustomer(String name,String idcard);
	/**
	 * 设置监控记录
	 * @param name
	 * @param idcard
	 * @param sdate
	 * @param edate
	 * @param account
	 */
	String setMonitor(String name,String idcard,String type,Date sdate,int days,String account);
}
