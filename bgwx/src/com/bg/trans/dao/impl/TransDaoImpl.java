package com.bg.trans.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.TransactionEntity;
import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
@Repository("transDao")
public class TransDaoImpl extends BaseJDBCDao implements ITransDao {
	private static final String INSERT_TRANSACTION="insert into b_transaction (orderno,reqcontent,respcontent,merchantid,secmerchantid,tranamt,rate,fee,name,idcard,bankno,bankcardno,bankphone,cvv2,cardvaliddate,channel,paychannel,trantype,reqno,status,originalorderno,transtime,accountid,deviceid,isnotice,notifycontent,secreqcontent,secrespcontent) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_TRANSACTION="update b_transaction set status=?,channelno=?,notifycontent=? where orderno=?";
	private static final String QUERU_TRANSACTION_ORDERNO="select * from b_transaction where orderno=?";
	private static final String QUERU_TRANSACTION_REQNO="select * from b_transaction where reqno=? and secmerchantid=? ";
	private static final String QUERY_TRANSACTOIN_MERCHANTID="select * from b_transaction where secmerchantid=? and status='01' order by transtime desc limit 500";
	private static final String QUERY_TRANSACTION_MERCHANTIDBYDAY = "select * from b_transaction where secmerchantid=? and status='01' and transtime >= ? and transtime <= ? order by transtime desc";
	private static final String UPDATE_TRANSCTION_NOTICE="update b_transaction set isnotice='1' where orderno=?";
	private static final String INSERT_NOTICE_LOG="insert into b_notice_log (orderno) values (?)";
	private static final String QUERY_NOTICE_ORDER="select * from b_transaction where isnotice='0' and trantype='1010' and status!='03'";
	private static final String QUERY_TRANSACTOIN_PAY="select * from b_transaction where accountid=? and status='01' and trantype in ('1001','1002','1010','1015') order by transtime desc limit 50";
	private static final String QUERY_TRANSACTOIN_PAYBYDAY="select * from b_transaction where accountid=? and status='01' and trantype in ('1001','1002','1010','1015') and transtime >= ? and transtime <= ? order by transtime desc";
	private static final String UPDATE_ACCT_BANKCARD_VERTIFY="update b_acc_bankcard set vertify=? where name=? and idcard=? and bankno=? and bankcardno=? and bankcardtype=?";
	private static final String QUERY_AREA_CHANNEL="select * from d_area where channel=?";
	private static final String QUERY_BANKNO_CHANNEL="select * from d_bankno where channel=?";
	private static final String QUERY_REWARDSPREAD_ACCOUNTID="select * from b_reward_log where type in ('3001','3003') and byaccount=? order by id desc limit 1";
	private static final String INSERT_REWARDLOG = "insert into b_reward_log(accountid,amount,type,inputtime,byaccount,linkno,linktype,rewardamt,channelaccount,remark) values (?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_ACCOUNTBALANCE="update a_account set balance=?,updatetime=? where accountid=?";
	private static final String INSERT_ACCOUNTDETAIL="insert into a_account_detail(accountid,balanceamt,lastbalance,afterbalance,occurtype,linkno,linktype,inputtime) values (?,?,?,?,?,?,?,?)";
	private static final String QUERY_SUMAMTANDNUMBYACCOUNT="select sum(tranamt) as amt,count(*) as num from b_transaction where accountid=? and trantype='1010' and status='01'";
	private static final String UPDATE_REWARDLOGREMARK="update b_reward_log set remark=? where id=?";
	private static final String QUERY_LASTQUICKPAYAPPLY_BANKCARDNO="select * from b_transaction where trantype='1009' and bankcardno=? and `status`='01' order by id desc limit 1";
	private static final String INSERT_BINDCARD="insert into b_bindcard (name,idcard,bankcardno,bankphone,treatychannel,treatyid,remark,createtime) values (?,?,?,?,?,?,?,?)";
	
	@Override
	public int insertTransaction(TransactionEntity entity){
		final Object[] obj = new Object[]{entity.getOrderno(),entity.getReqcontent(),entity.getRespcontent(),entity.getMerchantid(),
				entity.getSecmerchantid(),entity.getTranamt(),entity.getRate(),entity.getFee(),entity.getName(),entity.getIdcard(),
				entity.getBankno(),entity.getBankcardno(),entity.getBankphone(),entity.getCvv2(),entity.getCardvaliddate(),entity.getChannel(),entity.getPaychannel(),
				entity.getTrantype(),entity.getReqno(),entity.getStatus(),entity.getOriginalorderno(),entity.getTranstime(),
				entity.getAccountid(),entity.getDeviceid(),entity.getIsnotice(),entity.getNotifycontent(),entity.getSecreqcontent(),entity.getSecrespcontent()};
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		this.getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps= con.prepareStatement(INSERT_TRANSACTION,Statement.RETURN_GENERATED_KEYS);
				for(int i=0;i<obj.length;i++){
					ps.setObject(i+1, obj[i]);
				}
				return ps;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();		
	}
	@Override
	public void updateTransaction(TransactionEntity entity){
		Object[] obj = new Object[]{entity.getStatus(),entity.getChannelno(),entity.getNotifycontent(),entity.getOrderno()};
		this.getJdbcTemplate().update(UPDATE_TRANSACTION,obj);
	}
	@Override
	public TransactionEntity queryTransactionByOrderNo(String orderNo) {
		final List<TransactionEntity> datas = new ArrayList<TransactionEntity>();
		this.getJdbcTemplate().query(QUERU_TRANSACTION_ORDERNO, new Object[]{orderNo},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToObject(rs, datas,TransactionEntity.class);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});
		if(datas.size()==0){
			return null;
		}else{
			return datas.get(0);
		}
	}
	@Override
	public List<TransactionEntity> queryMerchantOrder(String secMerchantId,String date) {		
		String sql = null;
		Object[] obj = null;
		if(date==null){
			sql = QUERY_TRANSACTOIN_MERCHANTID;
			obj = new Object[]{secMerchantId};
		}else{
			sql = QUERY_TRANSACTION_MERCHANTIDBYDAY;
			String start = date+"000000";
			String end = date+"235959";
			obj = new Object[]{secMerchantId,start,end};
			System.out.println(QUERY_TRANSACTION_MERCHANTIDBYDAY);
			System.out.println(secMerchantId+":"+start+":"+end);
		}
		final List<TransactionEntity> datas = new ArrayList<TransactionEntity>();
		this.getJdbcTemplate().query(sql,obj, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToObject(rs, datas,TransactionEntity.class);
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
	@Transactional
	public boolean updateNotice(String orderno) {
		Object[] obj = new Object[]{orderno};
		this.getJdbcTemplate().update(UPDATE_TRANSCTION_NOTICE,obj);
		this.getJdbcTemplate().update(INSERT_NOTICE_LOG,obj);
		return true;
	}
	@Override
	public List<TransactionEntity> queryNoticeOrder() {
		final List<TransactionEntity> datas = new ArrayList<TransactionEntity>();
		this.getJdbcTemplate().query(QUERY_NOTICE_ORDER, new Object[]{},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToObject(rs, datas,TransactionEntity.class);
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
	public List<TransactionEntity> queryPayOrder(String accountId, String date) {
		String sql = null;
		Object[] obj = null;
		if(date==null){
			sql = QUERY_TRANSACTOIN_PAY;
			obj = new Object[]{accountId};
		}else{
			sql = QUERY_TRANSACTOIN_PAYBYDAY;
			String start = date+"000000";
			String end = date+"235959";
			obj = new Object[]{accountId,start,end};
		}
		final List<TransactionEntity> datas = new ArrayList<TransactionEntity>();
		this.getJdbcTemplate().query(sql,obj, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToObject(rs, datas,TransactionEntity.class);
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
	public void updateThreeVertify(String name, String idcard, String bankno,String bankcardno, String vertify,String bankcardtype) {
		Object[] obj = new Object[]{vertify,name,idcard,bankno,bankcardno,bankcardtype};
		this.getJdbcTemplate().update(UPDATE_ACCT_BANKCARD_VERTIFY,obj);
	}
	@Override
	public TransactionEntity queryTransactionByReqNo(String reqno,String mid) {
		final List<TransactionEntity> datas = new ArrayList<TransactionEntity>();
		this.getJdbcTemplate().query(QUERU_TRANSACTION_REQNO, new Object[]{reqno,mid},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToObject(rs, datas,TransactionEntity.class);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});
		if(datas.size()==0){
			return null;
		}else{
			return datas.get(0);
		}
	}
	@Override
	public List<Map<String, Object>> findAreaByChannel(String channelHftx) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_AREA_CHANNEL, new Object[]{channelHftx},new RowCallbackHandler() {
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
	public List<Map<String, Object>> findBanknoByChannel(String channelHftx) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BANKNO_CHANNEL, new Object[]{channelHftx},new RowCallbackHandler() {
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
	public Map<String, Object> findRewardSpread(String accountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_REWARDSPREAD_ACCOUNTID, new Object[]{accountId},new RowCallbackHandler() {
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
	public int insertRewardLog(String accountId, String recommender, double amt,String type,String linkno,String linkType,double allamt,String channelAccount,String remark) {
		final Object[] obj = new Object[]{recommender,amt,type,new Date(),accountId,linkno,linkType,allamt,channelAccount,remark};
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		this.getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps= con.prepareStatement(INSERT_REWARDLOG,Statement.RETURN_GENERATED_KEYS);
				for(int i=0;i<obj.length;i++){
					ps.setObject(i+1, obj[i]);
				}
				return ps;
			}
		},keyHolder);
		return keyHolder.getKey().intValue();
	}
	@Override
	public void insertAAccountDetail(String accountId,double amount, double lastBalance,double afterBalance, String occurType, String linkno,String linkType) {
		Object[] obj = new Object[]{accountId,amount,lastBalance,afterBalance,occurType,linkno,linkType,new Date()};
		this.getJdbcTemplate().update(INSERT_ACCOUNTDETAIL,obj);	
	}
	@Override
	public void updateAccountBalance(String accountId, double newbalance,Date updateTime) {
		Object[] obj = new Object[]{newbalance,updateTime,accountId};
		this.getJdbcTemplate().update(UPDATE_ACCOUNTBALANCE,obj);
		
	}
	@Override
	public Map<String, Object> findAllTransAmtByAccount(String accountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_SUMAMTANDNUMBYACCOUNT, new Object[]{accountId},new RowCallbackHandler() {
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
	public void updateRewardLogRemark(int id, String format) {
		Object[] obj = new Object[]{format,id};
		this.getJdbcTemplate().update(UPDATE_REWARDLOGREMARK,obj);
	}
	@Override
	public Map<String, Object> findLastQuickPayApplyByBankCardno(String bankCardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_LASTQUICKPAYAPPLY_BANKCARDNO, new Object[]{bankCardno},new RowCallbackHandler() {
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
	public void insertBindBankCard(String name, String idcard,String bankCardno, String bankphone, String treatychannel,String treatyid, String remark) {
		Object[] obj = new Object[]{name,idcard,bankCardno,bankphone,treatychannel,treatyid,remark,new Date()};
		this.getJdbcTemplate().update(INSERT_BINDCARD,obj);
	}
	
}
