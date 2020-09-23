package com.bg.web.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.soofa.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
import com.bg.web.dao.IAgentDao;

@Repository("agentDao")
public class AgentDaoImpl extends BaseJDBCDao implements IAgentDao {
	private Logger logger = Logger.getLogger(AgentDaoImpl.class);

	private static final String QUERY_TRANSLISTACCOUNTID="select * from b_transaction where accountid=? and createtime BETWEEN ? and ? and trantype in ('1010','1015','1019') order by id";
	private static final String QUERY_RELATIONSHOP="select * from b_acct_relationshop where subaccount=? and account=? ";
	private static final String QUERY_COUNTTEAMTRANS="SELECT t.subaccount,count(DISTINCT r.subaccount) as cnum,case when sum(s.tranamt) is null then 0 else sum(s.tranamt) end as tamt FROM b_acct_relationshop r RIGHT JOIN (SELECT subaccount	FROM b_acct_relationshop WHERE	account = ?	AND LEVEL = '1') t ON t.subaccount = r.account LEFT JOIN (select * from b_transaction where createtime>=? and createtime<? and status='01' and trantype in ('1010','1015')  ) s on r.subaccount=s.accountid group by t.subaccount order by cnum desc";
	private static final String QUERY_COUNTSUBACCOUNTTRANS="select r.subaccount as account,a.name,case when sum(tranamt) is null then 0 else sum(tranamt) end as tamt from b_acct_relationshop r LEFT JOIN b_account a ON a.account = r.subaccount LEFT JOIN (select * from b_transaction where status='01' and createtime>=? and createtime<? and trantype in ('1010','1015')) t on r.subaccount=t.accountid  where r.account=? and r.level=1 and a.name is not null group by r.subaccount";
	@Override
	public List<Map<String, Object>> findTransList(String accountId,String sdate, String edate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_TRANSLISTACCOUNTID, new Object[]{accountId,sdate,edate},new RowCallbackHandler() {
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
	public boolean isAgentCust(String accountId, String agentAccountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_RELATIONSHOP, new Object[]{accountId,agentAccountId},new RowCallbackHandler() {
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
		return datas.size()==0?false:true;
	}


	@Override
	public List<Map<String, Object>> countTeamTrans(String accountId,String sdate, String edate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_COUNTTEAMTRANS, new Object[]{accountId,sdate,edate},new RowCallbackHandler() {
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
	public List<Map<String, Object>> findSubAccountTrans(String accountId,String sdate, String edate) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_COUNTSUBACCOUNTTRANS, new Object[]{sdate,edate,accountId},new RowCallbackHandler() {
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
