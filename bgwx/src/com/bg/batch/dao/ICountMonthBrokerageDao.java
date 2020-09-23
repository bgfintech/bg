package com.bg.batch.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICountMonthBrokerageDao {
	/**
	 * 计算各三分销指定日期内的交易总额，本人交易不计算在内
	 * @param sdate
	 * @param edate
	 * @return
	 */
	List<Map<String,Object>> QueryCountTeamTranAmt(Date sdate,Date edate);
	/**
	 * 统计个人指定日期内刷卡交易金额
	 * @param sdate
	 * @param edate
	 * @return
	 */
	List<Map<String, Object>> queryAccountTranAmt(Date sdate, Date edate);
	/**
	 * 查询交易的人的上级人员
	 * @param sdate
	 * @param edate
	 * @return
	 */
	List<Map<String, Object>> queryRecommenderList();
}
