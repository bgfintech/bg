package com.bg.batch.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.batch.dao.IShareGBPRateProcessDao;
import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
@Repository("shareGBPRateProcessDao")
public class ShareGBPRateProcessDaoImpl extends BaseJDBCDao implements	IShareGBPRateProcessDao {
	private static final String QUERY_ACCOUNTNOSHARE_DAY="select a.* from b_account a LEFT JOIN (select * from b_share_log where sharedate=?) s on a.account=s.account where s.sharedate is null";
	private static final String UPDATE_ACCOUNTGBPRATE="update b_account set gbprate=? where account=?";
	@Override
	public List<Map<String, Object>> findNoShareAccount(String date) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNTNOSHARE_DAY, new Object[]{date},new RowCallbackHandler() {
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
	public void updateBatchAccountGbpRate(List<Object[]> params) {
		this.getJdbcTemplate().batchUpdate(UPDATE_ACCOUNTGBPRATE,params);
	}
	
}
