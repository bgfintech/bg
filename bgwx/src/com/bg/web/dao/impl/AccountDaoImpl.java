package com.bg.web.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.entity.AccountInfo;
@Repository("accountDao")
public class AccountDaoImpl extends BaseJDBCDao implements IAccountDao {
	private static final String UPDATE_ACCOUNTPWD="update b_account set passwd=? where account=?";
	private static final String QUERY_ACCOUNTBYDEVICEID="select * from b_account a,b_acct_device b where a.account=b.acct_id and  b.device_id=?";
	private static final String UPDATE_ACCOUNTINFO="update b_account set name=?,idcard=?,iscertified=?,gbprate=0.0054,gbpfee=300 where account=?";
	private static final String INSERT_ACCBANKCARD="insert b_acc_bankcard (account,bankno,bankcardno,bankcardtype,bankphone,name,idcard,type,vertify,hftxbindid,status) values (?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_BANKCARDSTATUS02_ACCOUNT="UPDATE b_acc_bankcard set status='02' where account=? and type=? and status='01'";
	private static final String QUERY_PAYBACKCARDINFO="select * from b_acc_bankcard where account=? and bankcardtype=? and status='01' and type='01' order by id desc";
	private static final String INSERT_ACCOUNT="insert into b_account (account,passwd,recommender,recommenderid,createtime) values(?,?,?,?,?)";
	private static final String INSERT_ACC_DEVICE="insert into b_acct_device (acct_id,device_id) values(?,?)";
	private static final String QUERY_LASTGBPTRANS="select * from b_transaction where accountid=? and trantype=? and status='01' order by transtime desc limit 1";
	private static final String QUERY_GBPBANKCARDINFO="select bankcardno,bankphone from b_transaction where accountid=? and trantype=? and channel='02' and status='01' group by bankcardno,bankphone order by transtime desc";
	private static final String QUERY_SHARELOG="select * from b_share_log where account=? and sharedate=?";
	private static final String INSERT_SHARELOG="insert into b_share_log (account,sharedate,createtime) values (?,?,?)";
	private static final String UPDATE_GBPRATE_ACCOUNT="update b_account set gbprate=? where account=?";
	private static final String QUERY_ACC_DEVICE_BYDEVICEID="select * from b_acct_device where acct_id=? and device_id=?";
	private static final String DELETE_ACC_DEVICE="delete from b_acct_device where acct_id=? and device_id=?";
	private static final String QUERY_LASTSUBSCRIBELOG="select a.* from b_subscribe_log l,b_account a where l.recommender=a.account and l.openid=? order by l.createtime desc limit 1";
	private static final String QUERY_VALIDBANKCARD="select * from b_acc_bankcard where account=? and vertify='1'";
	private static final String INSERT_TREATYCARDINFO="insert b_acc_bankcard (account,bankno,bankcardno,bankcardtype,bankphone,name,idcard,type,status,vertify,cvv2,cardvaliddate,treatyid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String QUERY_BANKTREATYID="select * from b_acc_bankcard where bankcardno=? and type='02' and status='01' and vertify='1' and treatyid is not null";
	private static final String QUERY_TEAMBYACCOUNT="SELECT t.subaccount,count(DISTINCT r.subaccount) AS cnum,a.name FROM b_acct_relationshop r RIGHT JOIN (SELECT subaccount FROM	b_acct_relationshop	WHERE account = ? AND LEVEL = '1') t ON t.subaccount = r.account INNER JOIN b_account a ON t.subaccount = a.account GROUP BY t.subaccount ORDER BY cnum DESC";
	private static final String QUERY_TEAMTRANSAMT="SELECT sum(tranamt) as amt FROM b_transaction WHERE status = '01' AND trantype in ('1015','1010','1001','1002')  and createtime>=? AND accountid IN (SELECT subaccount FROM b_acct_relationshop WHERE account = ?)";
	private static final String QUERY_ACCOUNT_RELATION="SELECT a.account as account,a1.account as r1,a2.account as r2 FROM b_account a LEFT JOIN b_account a1 ON a.recommender = a1.account LEFT JOIN b_account a2 ON a1.recommender = a2.account where a.account=?";
	private static final String INSERT_ACCOUNT_RELATION="insert into b_acct_relationshop (account,subaccount,level) values (?,?,?)";
	private static final String QUERY_ACCOUNTBYACCOUNTID="select a.*,d.device_id from b_account a LEFT JOIN b_acct_device d on a.account=d.acct_id WHERE a.account=? ";
	private static final String QUERY_BANKCARDACCOUNT_ISVERTIFY="select * from b_acc_bankcard where account=? and bankcardno=? and vertify='1'";
	private static final String INSERT_CHANNELACCT="insert into b_channel_acct (accountid,channel,custid,acctid) values (?,?,?,?)";
	private static final String QUERY_CHANNELACCT_ACCOUNT="select * from b_channel_acct where accountid=? and channel=?";
	private static final String QUERY_BANKCARD_CARDINFO="select * from b_acc_bankcard where account=? and bankcardno=? and bankno=? and type=? and name=? and idcard=?";
	private static final String UPDATE_CASHCARD_VALID="update b_acc_bankcard set status='01' where account=? and bankcardno=? and bankno=? and type='01'";
	private static final String QUERY_BINDCARDID="select * from b_acc_bankcard where account=? and bankcardno=? and bankcardtype='02' and type='02' and status='01'  and hftxbindid is not null";
	private static final String QUERY_ACCOUNTBYIDCARD="select * from b_account where idcard=? and iscertified='1'";
	private static final String INSERT_AACCOUNT="insert into a_account(accountid,inputtime) values (?,?)";
	private static final String QUERY_ACCOUNTBALANCE="select * from a_account where accountid=?";
	private static final String QUERY_SUPERIORACCOUNT="SELECT * FROM b_account WHERE FIND_IN_SET(id,getParAccount(?)) order by id desc";
	private static final String QUERY_RELATIONSHOP_SUBACCOUNT="SELECT * FROM b_acct_relationshop WHERE subaccount=? ORDER BY level";
	private static final String INSERT_LEGALFEE="insert into b_product_fee (account,type,fee) values (?,?,?)";
	
	@Override
	public void updateAccountPwd(String accountId, String pwd) {
		Object[] obj = new Object[]{pwd,accountId};
		this.getJdbcTemplate().update(UPDATE_ACCOUNTPWD,obj);
	}
	@Override
	public AccountInfo findAccountByDeviceId(String openid) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNTBYDEVICEID, new Object[]{openid},new RowCallbackHandler() {
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
		AccountInfo accountInfo = null;
		if(datas.size()!=0){
			Map<String,Object> m = datas.get(0);
			String account = (String)m.get("ACCOUNT");
			String passwd=(String)m.get("PASSWD");			
			accountInfo = new AccountInfo();
			accountInfo.setAccount(account);
			accountInfo.setPasswd(passwd);
			if(m.get("NAME")!=null){
				accountInfo.setName((String)m.get("NAME"));
			}
			if(m.get("IDCARD")!=null){
				accountInfo.setIdcard((String)m.get("IDCARD"));
			}
			if(m.get("GBPRATE")!=null){
				accountInfo.setGbprate((double)m.get("GBPRATE"));
			}
			if(m.get("GBPFEE")!=null){
				accountInfo.setGbpfee((int)m.get("GBPFEE"));
			}
			if(m.get("RECOMMENDER")!=null){
				accountInfo.setRecommender((String)m.get("RECOMMENDER"));
			}
			accountInfo.setIscertified((String)m.get("ISCERTIFIED"));
			accountInfo.setDeviceId(openid);
			accountInfo.setQrcodeurl((String)m.get("QRCODEURL"));
			accountInfo.setQrcodeexpiredate((Date)m.get("QRCODEEXPIREDATE"));	
			//accountInfo.setQrcodeurl2((String)m.get("QRCODEURL2"));
			//accountInfo.setActivatetype((String)m.get("ACTIVATETYPE"));
			//accountInfo.setIsactivate((String)m.get("ISACTIVATE"));		
			accountInfo.setCreatetime((Date)m.get("CREATETIME"));
			accountInfo.setId((int)m.get("ID"));
		}		
		return accountInfo;
	}
	@Override
	public void updateAccountInfo(String account, String name, String idcard,String isCertified) {
		Object[] obj = new Object[]{name,idcard,isCertified,account};
		this.getJdbcTemplate().update(UPDATE_ACCOUNTINFO,obj);
	}
	@Override
	public void insertAccBankCard(String account, String name, String idcard,String bankno, String bankCardno, String bankPhone,String bankCardType,String type,String vertify,String hftxBindId) {
		Object[] obj = new Object[]{account,bankno,bankCardno,bankCardType,bankPhone,name,idcard,type,vertify,hftxBindId,"01"};
		this.getJdbcTemplate().update(INSERT_ACCBANKCARD,obj);
	}
	@Override
	public void updateBankCardInvalidByAccount(String accountId,String type) {
		Object[] obj = new Object[]{accountId,type};
		this.getJdbcTemplate().update(UPDATE_BANKCARDSTATUS02_ACCOUNT,obj);
	}
	@Override
	public Map<String, Object> queryCashCardInfo(String accountId,String bankcardtype) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_PAYBACKCARDINFO, new Object[]{accountId,bankcardtype},new RowCallbackHandler() {
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
		return datas.size()==0?new HashMap<String,Object>():datas.get(0);
	}
	@Override
	public void insertAccount(String account, String passwd,String recommender,Integer recommenderId) {
		Object[] obj = new Object[]{account,passwd,recommender,recommenderId,new Date()};
		this.getJdbcTemplate().update(INSERT_ACCOUNT,obj);
	}
	@Override
	public void createAccountDevice(String account, String openid) {
		Object[] obj = new Object[]{account,openid};
		this.getJdbcTemplate().update(INSERT_ACC_DEVICE,obj);
	}
	@Override
	public List<Map<String, Object>> queryGbpBankCardInfo(String account,String transType) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_GBPBANKCARDINFO, new Object[]{account,transType},new RowCallbackHandler() {
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
	public Map<String, Object> queryLastGbpTransInfo(String account,String transType) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_LASTGBPTRANS, new Object[]{account,transType},new RowCallbackHandler() {
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
	public boolean isShareByDay(String account,String date) {		
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_SHARELOG, new Object[]{account,date},new RowCallbackHandler() {
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
	public void insertShareLog(String account) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String sd = sdf.format(d);				
		Object[] obj = new Object[]{account,sd,d};
		this.getJdbcTemplate().update(INSERT_SHARELOG,obj);
	}
	@Override
	public void updateGbpRateByAccount(String account, double rate) {
		Object[] obj = new Object[]{rate,account};
		this.getJdbcTemplate().update(UPDATE_GBPRATE_ACCOUNT,obj);
	}
	@Override
	public boolean isAccountByDevice(String account, String openid) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACC_DEVICE_BYDEVICEID, new Object[]{account,openid},new RowCallbackHandler() {
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
	public void deleteAccDevice(String account, String openid) {
		Object[] obj = new Object[]{account,openid};
		this.getJdbcTemplate().update(DELETE_ACC_DEVICE,obj);
	}
	@Override
	public Map<String,Object> findSubscribeRecommender(String openid) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_LASTSUBSCRIBELOG, new Object[]{openid},new RowCallbackHandler() {
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
	public boolean isIdentity(String account) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_VALIDBANKCARD, new Object[]{account},new RowCallbackHandler() {
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
	public void insertTreatyCardInfo(String account, String name,String idcard, String bankno, String bankCardno, String bankPhone,String cvv,String cardvaliddate,String treatyId) {
		Object[] obj = new Object[]{account,bankno,bankCardno,"02",bankPhone,name,idcard,"02","01","1",cvv,cardvaliddate,treatyId};
		this.getJdbcTemplate().update(INSERT_TREATYCARDINFO,obj);
	}
	@Override
	public String findBankCardTreatyId(String bankcardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BANKTREATYID, new Object[]{bankcardno},new RowCallbackHandler() {
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
		return datas.size()==0?null:(String)datas.get(0).get("TREATYID");
	}
	@Override
	public List<Map<String, Object>> findTeamByAccount(String account) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_TEAMBYACCOUNT, new Object[]{account},new RowCallbackHandler() {
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
	public int countTransAmtByAccountTeam(String account, Date date) {
		int obj = this.getJdbcTemplate().query(QUERY_TEAMTRANSAMT,new Object[] {date,account}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int num = 0;
				if (rs.next()) {
					num = rs.getInt("amt");
				}
				return num;
			}
		});
		return obj;		
	}
	@Override
	public Map<String, Object> findRemmenderRelation(String account) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNT_RELATION, new Object[]{account},new RowCallbackHandler() {
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
	public void insertAccRelationShop(String account, String recommender, int level) {
		Object[] obj = new Object[]{recommender,account,level};
		this.getJdbcTemplate().update(INSERT_ACCOUNT_RELATION,obj);
	}
	@Override
	public AccountInfo findAccountByAccountId(String accountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNTBYACCOUNTID, new Object[]{accountId},new RowCallbackHandler() {
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
		AccountInfo accountInfo = null;
		if(datas.size()!=0){
			Map<String,Object> m = datas.get(0);
			String account = (String)m.get("ACCOUNT");
			String passwd=(String)m.get("PASSWD");			
			accountInfo = new AccountInfo();
			accountInfo.setAccount(account);
			accountInfo.setPasswd(passwd);
			if(m.get("NAME")!=null){
				accountInfo.setName((String)m.get("NAME"));
			}
			if(m.get("IDCARD")!=null){
				accountInfo.setIdcard((String)m.get("IDCARD"));
			}
			if(m.get("GBPRATE")!=null){
				accountInfo.setGbprate((double)m.get("GBPRATE"));
			}
			if(m.get("GBPFEE")!=null){
				accountInfo.setGbpfee((int)m.get("GBPFEE"));
			}
			if(m.get("RECOMMENDER")!=null){
				accountInfo.setRecommender((String)m.get("RECOMMENDER"));
				accountInfo.setRecommenderid((int)m.get("RECOMMENDERID"));
			}
			accountInfo.setIscertified((String)m.get("ISCERTIFIED"));
			accountInfo.setQrcodeurl((String)m.get("QRCODEURL"));
			accountInfo.setQrcodeexpiredate((Date)m.get("QRCODEEXPIREDATE"));	
			accountInfo.setCreatetime((Date)m.get("CREATETIME"));
			accountInfo.setId((int)m.get("ID"));
			
			accountInfo.setDeviceId((String)m.get("DEVICE_ID"));
		}		
		return accountInfo;
	}
	@Override
	public String queryBankCardIsVertify(String accountId, String bankCardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BANKCARDACCOUNT_ISVERTIFY, new Object[]{accountId,bankCardno},new RowCallbackHandler() {
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
		String str = "0";
		if(datas.size()!=0){
			str = "1";
		}
		return str;
	}
	@Override
	public void createChannelAcct(String accountId, String channel,String custid, String acctid) {
		Object[] obj = new Object[]{accountId,channel,custid,acctid};
		this.getJdbcTemplate().update(INSERT_CHANNELACCT,obj);
	}
	@Override
	public Map<String, Object> findChannelCustIdAndAcctId(String accountid,String channel) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_CHANNELACCT_ACCOUNT, new Object[]{accountid,channel},new RowCallbackHandler() {
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
		return datas.size()==0?new HashMap<String,Object>():datas.get(0);
	}
	@Override
	public Map<String, Object> findBankCardInfo(String accountid,String bankCardno,String bankno,String type,String name, String idcard) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BANKCARD_CARDINFO, new Object[]{accountid,bankCardno,bankno,type,name,idcard},new RowCallbackHandler() {
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
	public void updateCashCardValid(String accountId, String bankCardno,String bankno) {
		Object[] obj = new Object[]{accountId,bankCardno,bankno};
		this.getJdbcTemplate().update(UPDATE_CASHCARD_VALID,obj);
	}
	@Override
	public Map<String, Object> findBindCardId(String accountid,String bankcardno) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_BINDCARDID, new Object[]{accountid,bankcardno},new RowCallbackHandler() {
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
	public Map<String, Object> findAccountByIdcard(String idcard) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_ACCOUNTBYIDCARD, new Object[]{idcard},new RowCallbackHandler() {
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
	public void createAccount(String userId) {
		Object[] obj = new Object[]{userId,new Date()};
		this.getJdbcTemplate().update(INSERT_AACCOUNT,obj);		
	}
	@Override
	public double findAccountBalance(String accountId) {
		Object[] obj = new Object[]{accountId};
		double d = this.getJdbcTemplate().query(QUERY_ACCOUNTBALANCE, obj, new ResultSetExtractor<Double>() {
			@Override
			public Double extractData(ResultSet rs) throws SQLException,DataAccessException {
				if(rs.next()){
					return rs.getDouble("balance");
				}else{
					return -1d;
				}
				
			}
		});
		return d;
	}
	@Override
	public List<Map<String, Object>> findParList(Integer id) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_SUPERIORACCOUNT, new Object[]{id},new RowCallbackHandler() {
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
	public List<Map<String, Object>> findRelationshopBySubAccount(String accountId) {
		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		this.getJdbcTemplate().query(QUERY_RELATIONSHOP_SUBACCOUNT, new Object[]{accountId},new RowCallbackHandler() {
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
	public void insertProductFee(String accountid, String type, double fee) {
		Object[] obj = new Object[]{accountid,type,fee};
		this.getJdbcTemplate().update(INSERT_LEGALFEE,obj);		
	}
}
