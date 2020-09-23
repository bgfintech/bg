package com.bg.web.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
import com.bg.web.dao.ICreditCardDao;

@Repository("creditCardDao")
public class CreditCardDaoImpl extends BaseJDBCDao implements ICreditCardDao {
	private static final String QUERY_CREDITCARD_ACCOUNT="SELECT b.*,p.isplan as isplan,p.planid FROM b_acc_bankcard b LEFT JOIN (select 1 as isplan,bankcardno,planid from b_insteadrepay_plan where status='0' group by bankcardno,planid) p on b.bankcardno = p.bankcardno WHERE	b.account=? AND b.STATUS = '01' AND b.bankcardtype = '02' AND b.type = '02' AND b.vertify = '1' and treatyid is not null";
	private static final String QUERY_REPAYPLAN_PLANID="select * from b_insteadrepay_plan where planid=? order by id";
	private static final String QUERY_CARDINFO="select * from b_acc_bankcard where bankcardno=? and STATUS = '01' AND bankcardtype = '02' AND type = '02' AND vertify = '1'";
	private static final String INSERT_INSTEADREPAYPLAN="insert into b_insteadrepay_plan (planid,bankcardno,type,tranamt,transdate,runtime,status) values (?,?,?,?,?,?,?)";
	private static final String QUERY_REPAYPLANSTATUS0_CARDNO="select * from b_insteadrepay_plan where bankcardno=? and status='0'";
	private static final String QUERY_RUNREPAYPLAN_RUNTIME="select b.*,p.id as pid,p.planid,p.type as ptype,p.tranamt,p.transdate,p.status as pstatus from b_insteadrepay_plan p,b_acc_bankcard b where p.bankcardno=b.bankcardno and p.runtime<? and p.type='01' and p.status='0' and b.vertify='1' and b.status='01'";
	private static final String QUERY_RUNPAYPLAN_RUNTIME="select b.*,p.id as pid,p.planid,p.type as ptype,p.tranamt,p.transdate,p.status as pstatus from b_insteadrepay_plan p,(select * from b_insteadrepay_plan where type='02' and status='0' and runtime<?) p1,b_acc_bankcard b where p.bankcardno=b.bankcardno and p.planid=p1.planid and p.transdate=p1.transdate and b.vertify='1' and b.status='01'";
	private static final String UPDATE_INSTEADREPAYPLAN_STATUS="update b_insteadrepay_plan set status=?,updatetime=? where id=?";
	@Override
	public List<Map<String, Object>> findBindCreditCardList(String accountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_CREDITCARD_ACCOUNT, new Object[]{accountId},new RowCallbackHandler() {
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
	public List<Map<String, Object>> findRepayPlanByPlanId(String planId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_REPAYPLAN_PLANID, new Object[]{planId},new RowCallbackHandler() {
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
	public Map<String, Object> findCardInfoByCardno(String cardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_CARDINFO, new Object[]{cardno},new RowCallbackHandler() {
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
		return datas.size()==0?null:datas.get(0);
	}

	@Override
	public void createBatchRepayPlan(List<Object[]> datas) {
		this.getJdbcTemplate().batchUpdate(INSERT_INSTEADREPAYPLAN,datas);				
	}

	@Override
	public boolean isSetRepayPlanByCardno(String cardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_REPAYPLANSTATUS0_CARDNO, new Object[]{cardno},new RowCallbackHandler() {
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
		return datas.size()>0?true:false;
	}

	@Override
	public List<Map<String, Object>> findRunRepayPlan(String date) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_RUNREPAYPLAN_RUNTIME, new Object[]{date},new RowCallbackHandler() {
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
	public List<Map<String, Object>> findRunPayPlan(String date) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_RUNPAYPLAN_RUNTIME, new Object[]{date},new RowCallbackHandler() {
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
	public void updateInsteadRepayPlan(int pid, String status, Date date) {
		Object[] obj = new Object[]{status,date,pid};
		this.getJdbcTemplate().update(UPDATE_INSTEADREPAYPLAN_STATUS,obj);
	}

}
