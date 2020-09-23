package com.bg.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.dao.IMerchantDao;
import com.bg.web.entity.AccountInfo;
import com.bg.web.service.AccountServiceI;

@Service("accoutService")
public class AccountServiceImpl extends CommonServiceImpl implements AccountServiceI{
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private IMerchantDao merchantDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private ITransDao transDao;
	
	@Override
	public boolean checkOpenid(String openid) {		
		AccountInfo account = accountDao.findAccountByDeviceId(openid);
		if(account!=null){
			return true;
		}		
		return false;
	}

	@Override
	public String getMerIdByOpenid(String openid) {
		String sql="select mer_id from b_mer_bind a,b_acct_device b where a.acct_id=b.acct_id and b.device_id=?";
		List<Map<String,Object>> rs =this.findForJdbc(sql, openid);
		if(rs!=null&&rs.size()>0){
			Map<String,Object> rt = (Map<String,Object>)rs.get(0);
			String merId = (String)rt.get("mer_id");
			return merId;
			
		}
		return null;
	}

	@Override
	public AccountInfo getAccountByOpenid(String openid) {		
		return accountDao.findAccountByDeviceId(openid);		
	}

	@Override
	public String getSecMerIdByOpenid(String openid) {
		String sql="select b.secMerchantId from b_mer_bind a,b_merchant b,b_acct_device c where a.mer_id=b.merchantId and a.acct_id=c.acct_id and c.device_id=?";
		List<Map<String,Object>> rs =this.findForJdbc(sql, openid);
		if(rs!=null&&rs.size()>0){
			Map<String,Object> rt=(Map<String,Object>)rs.get(0);
			String secmerid=(String)rt.get("secMerchantId");
			return secmerid;
		}
		return null;
	}

	@Override
	public boolean checkAccount(String user, String passwd) {
		String sql="select id from b_account where account=? and passwd=?";
		List<Map<String,Object>> rs = this.findForJdbc(sql, user,passwd);
		if(rs!=null&&rs.size()>0){
			return true;
		}
		return false;
	}
	@Override
	public boolean checkAccount(String user) {
		String sql="select id from b_account where account=? ";
		List<Map<String,Object>> rs = this.findForJdbc(sql, user);
		if(rs!=null&&rs.size()>0){
			return true;
		}
		return false;
	}
	

	@Override
	public void createAccountDevice(String user,String deviceId) {
		accountDao.createAccountDevice(user,deviceId);
	}


	@Override
	public boolean checkMerchant(String merchantId) {
		return merchantDao.checkMerchant(merchantId);
	}

	@Override
	public void updatePassword(String accountId, String pwd) {
		accountDao.updateAccountPwd(accountId, pwd);
	}

	@Override
	@Transactional
	public void addCustomerAndBankCard(String accountId, String name,String idcard, String bankno, String bankCardno, String bankPhone) {
		/**********暂时不用绑卡校验，只要绑卡默认成功************************/
		accountDao.updateAccountInfo(accountId, name,idcard,"1");
		accountDao.updateBankCardInvalidByAccount(accountId,"01");
		String vertify = accountDao.queryBankCardIsVertify(accountId,bankCardno);
		accountDao.insertAccBankCard(accountId,name,idcard,bankno,bankCardno,bankPhone,"01","01",vertify,"");		
		
	}
	
	@Override
	@Transactional
	public void changeCashCard(String accountId, String name,String idcard, String bankno, String bankCardno) {
		accountDao.updateBankCardInvalidByAccount(accountId,"01");
		accountDao.updateCashCardValid(accountId,bankCardno,bankno);		
	}
	
	
	@Override
	@Transactional
	public void updateAccountInfo(String userid, String name,String idcard,String isCertified,String custid,String acctid,String channel){
		accountDao.updateAccountInfo(userid,name,idcard,isCertified);
		if(PARA_CONSTATNTS.Channel_HFTX.equals(channel)){
			accountDao.createChannelAcct(userid,channel,custid,acctid);
		}
		double balance = accountDao.findAccountBalance(userid);
		if(balance==-1){
			accountDao.createAccount(userid);
		}		
	}
	@Override
	public Map<String, Object> queryCashCardInfo(String accountId,String bankcardtype) {
		return accountDao.queryCashCardInfo(accountId,bankcardtype);
	}

	@Override
	@Transactional
	public void createAccountAndDevice(String account, String passwd,String openid,String recommender) {	
		//必须有推荐人才可以注册
		AccountInfo accountinfo = accountDao.findAccountByAccountId(recommender);
		Integer recommenderId = accountinfo.getId();
		/** 根据openid找推荐人,可以有无推荐人进行注册
		Map<String,Object> rerow = accountDao.findSubscribeRecommender(openid);
		String recommender = null;
		Integer recommenderId = null;
		if(rerow!=null){
			recommender = (String)rerow.get("ACCOUNT");
			recommenderId = (Integer)rerow.get("ID");
		}		
		*/
		accountDao.insertAccount(account,passwd,recommender,recommenderId);
		accountDao.createAccountDevice(account,openid);
		if(recommenderId!=null){			
			List<Map<String,Object>> rl = accountDao.findParList(recommenderId);
			for(int i=0;i<rl.size();i++){
				Map<String,Object> row = rl.get(i);
				String rd = (String)row.get("ACCOUNT");
				accountDao.insertAccRelationShop(account,rd,i+1);
			}
		}			
	}

	@Override
	public Map<String, Object> queryLastGbpTransInfo(String account,String transType) {
		return accountDao.queryLastGbpTransInfo(account,transType);
	}
	@Override
	public List<Map<String, Object>> queryGbpBankCardInfo(String account,String transType) {
		return accountDao.queryGbpBankCardInfo(account,transType);
	}

	@Override
	public void createShare(AccountInfo accountinfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date();
		String today = sdf.format(d);
		String account = accountinfo.getAccount();		
		boolean bool = accountDao.isShareByDay(account,today);	
		accountDao.insertShareLog(account);
		if(!bool){
			double yr = accountinfo.getGbprate();
			if(yr>0.0048){
				double nrate = yr-0.0001;
				accountDao.updateGbpRateByAccount(accountinfo.getAccount(),nrate);
			}			
		}		
	}

	@Override
	public boolean isAccountByDevice(String account, String openid) {
		return accountDao.isAccountByDevice(account,openid);
	}

	@Override
	public void logout(String account, String openid) {
		accountDao.deleteAccDevice(account,openid);		
	}

	@Override
	public boolean isIdentity(String account) {
		return accountDao.isIdentity(account);
	}

	@Override
	public void insertTreatyCardInfo(String accountId, String name,String idcard, String bankno, String bankCardno, String bankPhone,String cvv,String cardvaliddate,String treatyId) {
		accountDao.insertTreatyCardInfo(accountId, name, idcard, bankno, bankCardno, bankPhone,cvv, cardvaliddate,treatyId);
	}

	@Override
	public String findBankCardTreatyId(String bankcardno) {
		return accountDao.findBankCardTreatyId(bankcardno);
	}

	@Override
	public List<Map<String, Object>> findTeamByAccount(String account) {
		return accountDao.findTeamByAccount(account);
	}

	@Override
	public int countTransAmtByAccountTeam(String account, Date date) {
		return accountDao.countTransAmtByAccountTeam(account,date);
	}

	@Override
	public void createAccount(String account, String passwd) {
		accountDao.insertAccount(account,passwd,null,null);
	}

	@Override
	public AccountInfo getAccountByAccountId(String accountId) {
		return accountDao.findAccountByAccountId(accountId);
	}

	@Override
	public Map<String,Object> findChannelCustIdAndAcctId(String accountid, String channel) {
		return accountDao.findChannelCustIdAndAcctId(accountid,channel);
	}

	@Override
	@Transactional
	public void addCashCard(String accountid, String name, String idcard,String bankno, String bankCardno, String bankPhone,String bankCardType,String bindCardId) {			
		accountDao.updateBankCardInvalidByAccount(accountid,"01");
		accountDao.insertAccBankCard(accountid, name, idcard, bankno, bankCardno, bankPhone, bankCardType, "01", "0",bindCardId);		
	}

	@Override
	public Map<String, Object> findBankCardInfo(String accountid,String bankcardno, String bankno, String type, String name,String idcard) {
		return accountDao.findBankCardInfo(accountid, bankcardno, bankno, type, name, idcard);
	}

	@Override
	public Map<String, Object> findAccountByIdcard(String idcard) {
		return accountDao.findAccountByIdcard(idcard);
	}

	@Override
	public double findAccountBalance(String accountId) {
		return accountDao.findAccountBalance(accountId);
	}

	@Override
	public Map<String, Object> findSubscribeRecommender(String openid) {
		return accountDao.findSubscribeRecommender(openid);
	}

	@Override
	@Transactional
	public void createLegalAccount(String accountid, double entstat,double entout) {
		accountDao.createAccount(accountid);
		accountDao.insertProductFee(accountid, "1100", entstat);
		accountDao.insertProductFee(accountid, "1101", entout);
	}

	@Override
	@Transactional
	public void manualRecharge(String accountid, String type, double amount) {
		double balance = accountDao.findAccountBalance(accountid);
		double after = UtilData.add(balance, amount);
		String linktype = null;
		if("1".equals(type)){
			linktype="b_transaction";
		}
		transDao.insertAAccountDetail(accountid,amount,balance,after,"1033",null,linktype);
		transDao.updateAccountBalance(accountid,after,new Date());		
		
	}


}
