package com.bg.interfaces.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.interfaces.dao.IWxminiDao;
import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;

@Repository("wxminiDao")
public class WxminiDaoImpl extends BaseJDBCDao implements IWxminiDao {
	private static Logger logger = Logger.getLogger(WxminiDaoImpl.class); 
	
	private static final String QUERY_BILLLISTCOUNT="select count(*) num from a_account_detail d where d.accountid=? ";	
	private static final String QUERY_BILLLIST="select * from a_account_detail d where d.accountid=? order by id desc limit ?,?";
	private static final String QUERY_QUERYLIST="select * from b_transaction where accountid=? and trantype=? order by id desc limit ?,?";
	
	@Override
	public int getBillListCount(String accountid) {
		int obj = this.getJdbcTemplate().query(QUERY_BILLLISTCOUNT,new Object[] {accountid}, new ResultSetExtractor<Integer>() {
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
	public List<Map<String, Object>> getBillList(String accountid, int offset,int pagesize) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BILLLIST, new Object[]{accountid,offset,pagesize},new RowCallbackHandler() {
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
	public List<Map<String, Object>> getQueryList(String accountid,String type, int offset, int pagesize) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_QUERYLIST, new Object[]{accountid,type,offset,pagesize},new RowCallbackHandler() {
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
