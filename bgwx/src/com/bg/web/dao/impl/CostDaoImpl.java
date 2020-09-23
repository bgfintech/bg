package com.bg.web.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
import com.bg.web.dao.ICostDao;

@Repository("costDao")
public class CostDaoImpl extends BaseJDBCDao implements ICostDao {
	private static String QUERY_COSTLIST_COUNT="SELECT count(d.id) num FROM a_account_detail d WHERE d.accountid = ?";
	private static String QUERY_COSTLIST="select d.id,d.balanceamt,d.afterbalance,d.occurtype,d.linktype,t.name,d.inputtime,t.transtime from a_account_detail d LEFT JOIN b_transaction t on d.linkno=t.id where d.accountid=? order by d.id desc limit ?,?";
	@Override
	public int countCostList(String account) {
		int obj = this.getJdbcTemplate().query(QUERY_COSTLIST_COUNT,new Object[] {account}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int num = 0;
				if (rs.next()) {
					num = rs.getInt("num"); 
				}
				return num;
			}
		});
		return obj;
	}
	@Override
	public List<Map<String, Object>> queryCostList(String account, int offset,int limit) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_COSTLIST, new Object[]{account,offset,limit},new RowCallbackHandler() {
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
