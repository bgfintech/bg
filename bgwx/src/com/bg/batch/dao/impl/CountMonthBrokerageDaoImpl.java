package com.bg.batch.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.batch.dao.ICountMonthBrokerageDao;
import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;

@Repository("countMonthBrokerageDao")
public class CountMonthBrokerageDaoImpl extends BaseJDBCDao implements ICountMonthBrokerageDao {
	
	private static final String QUERY_COUNTTEAMTRANAMT="SELECT r.account,sum(tranamt) AS amt FROM	b_acct_relationshop r LEFT JOIN b_transaction t ON r.subaccount = t.accountid WHERE	t.STATUS = '01' AND t.trantype IN ('1015', '1010') AND t.createtime >= ? and t.createtime < ? GROUP BY r.account";
	private static final String QUERY_ACCOUNTTRANAMT="SELECT accountid, sum(tranamt) AS amt FROM b_transaction t WHERE t.STATUS = '01' AND t.trantype IN ('1015', '1010')AND t.createtime >= ? AND t.createtime < ? GROUP BY t.accountid";
	private static final String QUERY_RECOMMENDERLIST="select * from b_acct_relationshop";
	
	@Override
	public List<Map<String, Object>> QueryCountTeamTranAmt(Date sdate,Date edate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_COUNTTEAMTRANAMT, new Object[]{sdate,edate},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});
		return datas;
	}

	@Override
	public List<Map<String, Object>> queryAccountTranAmt(Date sdate, Date edate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNTTRANAMT, new Object[]{sdate,edate},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});
		return datas;
	}
	
	@Override
	public List<Map<String, Object>> queryRecommenderList() {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_RECOMMENDERLIST, new Object[]{},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});
		return datas;
	}

}
