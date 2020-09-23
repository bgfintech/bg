package com.bg.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bg.web.entity.AccountInfo;
import com.bg.web.entity.BAccountEntity;
import com.bg.web.entity.BAcctDeviceEntity;

public interface AccountServiceI {
	
	/**
	 * 登录校验用户名密码
	 * @param openid
	 * @return
	 */
	boolean checkAccount(String user,String passwd);
	/**
	 * 检查账号是否存在
	 * @param openid
	 * @return
	 */
	boolean checkAccount(String user);
	/**
	 * 检查微信是否关注过公众号
	 * @param openid
	 * @return
	 */
	boolean checkOpenid(String openid);
	
	
	/**
	 * 追加账户的设备标识(微信:openid)
	 * @param user
	 * @param passwd
	 */
	void createAccountDevice(String user,String deviceId);
	/**
	 * 根据openid获取系统商户ID
	 * @param openid
	 * @return
	 */
	String getMerIdByOpenid(String openid);
	/**
	 * 根据openid获取二级商户ID
	 * @param openid
	 * @return
	 */
	String getSecMerIdByOpenid(String openid);
	/**
	 * 根据openid获取账户信息
	 * @param openid
	 * @return
	 */
	AccountInfo getAccountByOpenid(String openid);
	/**
	 * 检查绑定商户是否存在
	 * @param marchantId
	 * @return
	 */
	boolean checkMerchant(String merchantId);
	/**
	 * 修改密码
	 * @param accountId
	 * @param pwd
	 */
	void updatePassword(String accountId,String pwd);
	/**
	 * 根据账号更新账户信息 
	 * @param accountId
	 * @param name
	 * @param idcard
	 * @param isCertified
	 */
	void updateAccountInfo(String accountId, String name,String idcard,String isCertified,String custId,String acctId,String channel);
	/**
	 * 
	 * @param accountId
	 * @param bankCardType 
	 * @return
	 */
	Map<String,Object> queryCashCardInfo(String accountId, String bankCardType);
	/**
	 * 创建账号并绑定设备ID
	 * @param account
	 * @param passwd
	 * @param openid
	 */
	void createAccountAndDevice(String account, String passwd, String openid,String recommender);
	/**
	 * 创建账号
	 * @param account
	 * @param passwd
	 * @param openid
	 */
	void createAccount(String account, String passwd);
	
	List<Map<String, Object>> queryGbpBankCardInfo(String account, String transcodeGbptreatyconfirm);
	/**
	 * 
	 * @param account
	 * @param transcodeGbptreatyconfirm 
	 * @return
	 */
	Map<String, Object> queryLastGbpTransInfo(String account, String transcodeGbptreatyconfirm);
	/**
	 * 分享后记录日志及修改优惠费率
	 * @param accountinfo
	 */
	void createShare(AccountInfo accountinfo);
	/**
	 * 判断该设备是否已绑定该账号
	 * @param user
	 * @param openid
	 * @return
	 */
	boolean isAccountByDevice(String user, String openid);
	/**
	 * 退出
	 * @param account
	 * @param openid
	 */
	void logout(String account, String openid);
	/**
	 * 该账户是否已认证有效身份
	 * @param account
	 * @return
	 */
	boolean isIdentity(String account);
	/**
	 * 新增快捷协议代扣卡信息
	 * @param accountId
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankCardno
	 * @param bankPhone
	 * @param treatyId
	 */
	void insertTreatyCardInfo(String accountId, String name, String idcard,String bankno, String bankCardno, String bankPhone,String cvv,String cardvaliddate,String treatyId);
	/**
	 * 根据卡号查看快捷协议代扣协议ID
	 * @param bankcardno
	 * @return
	 */
	String findBankCardTreatyId(String bankcardno);
	/**
	 * 根据账户查询下级人员及下级人员推广人数
	 * @param account
	 * @return
	 */
	List<Map<String, Object>> findTeamByAccount(String account);
	/**
	 * 统计从DATE后的团队交易额
	 * @param account
	 * @return
	 */
	int countTransAmtByAccountTeam(String account,Date date);
	/**
	 * 根据 账户Id获取账户信息
	 * @param accountId
	 * @return
	 */
	AccountInfo getAccountByAccountId(String accountId);
	/**
	 * 查询用户的渠道custID和acctId
	 * @param accountid
	 */
	Map<String,Object> findChannelCustIdAndAcctId(String accountid,String channel);
	/**
	 * 
	 * @param accountid
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankCardno
	 * @param bankPhone
	 * @param vertify
	 * @param vertify2 
	 * @param type 
	 */
	void addCashCard(String accountid, String name, String idcard,String bankno, String bankCardno, String bankPhone, String bankcardtype,String bindCardId);
	/**
	 * 查询银行卡信息
	 * @param accountid
	 * @param bankcardno
	 * @param bankno
	 * @param bankcardtype
	 * @param name
	 * @param idcard
	 * @return
	 */
	Map<String,Object> findBankCardInfo(String accountid,String bankcardno,String bankno,String type,String name,String idcard);
	/**
	 * 新增或更换取现卡
	 * @param accountId
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankCardno
	 * @param bankPhone
	 */
	void changeCashCard(String accountId, String name, String idcard,String bankno, String bankCardno);
	/**
	 * 根据身份证号查询账户
	 * @param idcard
	 * @return
	 */
	Map<String,Object> findAccountByIdcard(String idcard);
	/**
	 * 查询账户余额
	 * @param accountId
	 * @return
	 */
	double findAccountBalance(String accountId);
	/**
	 * 根据openid查询推荐人
	 * @param openid
	 * @return
	 */
	Map<String,Object> findSubscribeRecommender(String openid);
	void addCustomerAndBankCard(String accountId, String name, String idcard,String bankno, String bankCardno, String bankPhone);
	/**
	 * 创建司法账户和设置产品费率
	 * @param accountid
	 * @param entstat
	 * @param entout
	 */
	void createLegalAccount(String accountid,double entstat,double entout);
	/**
	 * 手动调整充值和赠送
	 * @param accountid
	 * @param type
	 * @param amount
	 */
	void manualRecharge(String accountid, String type, double amount);
	
}
