package com.bg.interfaces.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bg.interfaces.dao.IBPEntDao;
import com.bg.interfaces.entity.Trans1100Rsp;
import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;

@Repository("bpEntDao")
public class BPEntDaoImpl extends BaseJDBCDao implements IBPEntDao {
	
	private static final String QUERY_ACCOUNTDETAIL="select a.balance,d.* from a_account a LEFT JOIN a_account_detail d on a.accountid=d.accountid where d.accountid=? order by id desc limit 200";
	private static final String QUERY_ACCOUNTDETAILBYDATE="select a.balance,d.* from a_account a LEFT JOIN a_account_detail d on a.accountid=d.accountid where d.accountid=? and inputtime>=? and inputtime<=? order by id desc ";
	private static final String QUERY_PRODUCTFEE="select * from b_product_fee where account=? and type=?";
	private static final String QUERY_ENTTRANLIST="select DISTINCT t.name,t.idcard,d.occurtype from a_account_detail d,b_transaction t where d.linkno=t.id and d.occurtype in ('1100','1101') and d.accountid=? and inputtime>=?";
	private static final String INSERT_MONITOR="insert into b_shesu_monitor (name,idcard,sdate,edate,account) VALUES (?,?,?,?,?)";
	private static final String INSERT_MONITOR_LOG="insert into b_shesu_monitorlog (name,idcard,account,respdata,updatetime,mid) values (?,?,?,?,?,?)";
	private static final String UPDATE_MONITOR_LOG="update b_shesu_monitorlog set respdata=?,updatetime=? where id=?";
	private static final String QUERY_MONITORLIST_COUNT="select count(id) num from b_shesu_monitor where account=?";
	private static final String QUERY_MONITORLIST="select * from b_shesu_monitor where account=? order by id desc limit ?,?";
	
	@Override
	public List<Map<String, Object>> findAccountDetail(String account,String sdate, String edate) {
		String queryStr = "";
		Object[] objs = null;
		if(sdate!=null&&!"".equals(sdate)&&edate!=null&&!"".equals(edate)){
			queryStr = QUERY_ACCOUNTDETAILBYDATE;
			objs = new Object[]{account,sdate+"000000",edate+"235959"};
		}else{
			queryStr = QUERY_ACCOUNTDETAIL;
			objs = new Object[]{account};
		}	
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(queryStr, objs,new RowCallbackHandler() {
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
	public double findProductFee(String account, String type) {
		double obj = this.getJdbcTemplate().query(QUERY_PRODUCTFEE,new Object[] {account,type}, new ResultSetExtractor<Double>() {
			public Double extractData(ResultSet rs) throws SQLException, DataAccessException {
				double num = -1;
				if (rs.next()) {
					num = rs.getDouble("fee");
				}
				return num;
			}
		});
		return obj;		
	}

	@Override
	public List<Map<String, Object>> findTransList(String account, Date sdate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ENTTRANLIST, new Object[]{account,sdate},new RowCallbackHandler() {
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
	public int setMonitor(String name, String idcard, Date sdate, Date edate,String account) {
		final Object[] obj = new Object[]{name,idcard,sdate,edate,account};
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		this.getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps= con.prepareStatement(INSERT_MONITOR,Statement.RETURN_GENERATED_KEYS);
				for(int i=0;i<obj.length;i++){
					ps.setObject(i+1, obj[i]);
				}
				return ps;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();		
	}

	@Override
	public void insertMontiorLog(String name, String idcard, String account,String rspdata, Date date,int mid) {
		Object[] obj = new Object[]{name,idcard,account,rspdata,date,mid};
		this.getJdbcTemplate().update(INSERT_MONITOR_LOG,obj);		
		
	}

	@Override
	public void updateMontiorLog(int id, String rspdata, Date date) {
		Object[] obj = new Object[]{rspdata,date,id};
		this.getJdbcTemplate().update(UPDATE_MONITOR_LOG,obj);		
	}

	@Override
	public int countMonitorList(String account) {
		int obj = this.getJdbcTemplate().query(QUERY_MONITORLIST_COUNT,new Object[] {account}, new ResultSetExtractor<Integer>() {
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
	public List<Map<String, Object>> findMonitorList(String account,int offset, int limit) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_MONITORLIST, new Object[]{account,offset,limit},new RowCallbackHandler() {
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
