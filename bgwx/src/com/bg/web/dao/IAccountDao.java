package com.bg.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bg.web.entity.AccountInfo;

public interface IAccountDao {
	/**
	 * 修改账号密码
	 * @param accountId
	 * @param pwd
	 */
	void updateAccountPwd(String accountId,String pwd);
	/**
	 * 根据deviceid找账号
	 * @param openid
	 * @return
	 */
	AccountInfo findAccountByDeviceId(String openid);
	/**
	 * 修改账号信息
	 * @param accountId
	 * @param name
	 * @param idcard
	 */
	void updateAccountInfo(String accountId, String name, String idcard,String isCertified);
	/**
	 * 新增绑定银行卡
	 * @param accountId
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankCardno
	 * @param bankPhone
	 */
	void insertAccBankCard(String accountId, String name, String idcard,String bankno, String bankCardno, String bankPhone,String bankCardType,String type,String vertify,String hftxBindId);
	/**
	 * 修改银行卡状态
	 * @param accountId
	 * @param status
	 * @param type
	 */
	void updateBankCardInvalidByAccount(String accountId,String type);
	/**
	 * 
	 * @param accountId
	 * @param bankcardtype 
	 * @return
	 */
	Map<String, Object> queryCashCardInfo(String accountId, String bankcardtype);
	/**
	 * 
	 * @param account
	 * @param passwd
	 */
	void insertAccount(String account, String passwd,String recommender,Integer recommenderId);
	/**
	 * 
	 * @param account
	 * @param openid
	 */
	void createAccountDevice(String account, String openid);
	/**
	 * 
	 * @param account
	 * @param transType 
	 * @return
	 */
	Map<String, Object> queryLastGbpTransInfo(String account, String transType);
	/**
	 * 
	 * @param account
	 * @param transType 
	 * @return
	 */
	List<Map<String, Object>> queryGbpBankCardInfo(String account, String transType);
	/**
	 * 判断当天是否已分享过
	 * @param account
	 * @return
	 */
	boolean isShareByDay(String account,String date);
	/**
	 * 生成分享日志
	 * @param account
	 */
	void insertShareLog(String account);
	/**
	 * 
	 * @param account
	 */
	void updateGbpRateByAccount(String account,double rate);
	/**
	 * 判断该设备是否已绑定该账号
	 */
	boolean isAccountByDevice(String account, String openid);
	/**
	 * 退出删除设备账号绑定记录
	 * @param account
	 * @param openid
	 */
	void deleteAccDevice(String account, String openid);
	/**
	 * 根据openid查找推荐人account
	 * @param openid
	 * @return
	 */
	Map<String, Object> findSubscribeRecommender(String openid);
	/**
	 * 是否有验证身份
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
	 * 
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
	 */
	int countTransAmtByAccountTeam(String account, Date date);
	/**
	 * 查询账户的推荐人，三级以内
	 * @param account
	 * @return
	 */
	Map<String, Object> findRemmenderRelation(String account);
	/**
	 * 添加账户与推荐人关系
	 * @param account
	 * @param recommender
	 * @param string
	 */
	void insertAccRelationShop(String account, String recommender, int level);
	/**
	 * 根据账户ID获取账户信息
	 * @param accountId
	 * @return
	 */
	AccountInfo findAccountByAccountId(String accountId);
	/**
	 * 查看该用户的某卡是否做过认证
	 * @param accountId
	 * @param bankCardno
	 * @return
	 */
	String queryBankCardIsVertify(String accountId, String bankCardno);
	/**
	 * 新增渠道客户ID和账户ID
	 * @param accountId
	 * @param channel
	 * @param custid
	 * @param acctid
	 */
	void createChannelAcct(String accountId, String channel, String custid,String acctid);
	/**
	 * 查询用户的渠道custID和acctId
	 * @param accountid
	 * @param channel
	 * @return
	 */
	Map<String, Object> findChannelCustIdAndAcctId(String accountid,String channel);
	/**
	 * 查询银行卡信息
	 * @param bankCardno
	 * @param bankno
	 * @param name
	 * @param idcard
	 * @return
	 */
	Map<String,Object> findBankCardInfo(String accountid,String bankCardno,String bankno,String type,String name,String idcard);
	/**
	 * 修改更换已有取现卡
	 * @param accountId
	 * @param bankCardno
	 * @param bankno
	 * @param string
	 */
	void updateCashCardValid(String accountId, String bankCardno,String bankno);
	/**
	 * 
	 * @param accountid
	 * @param bankcardno
	 * @return
	 */
	Map<String,Object> findBindCardId(String accountid,String bankcardno);
	/**
	 * 
	 * @param idcard
	 * @return
	 */
	Map<String, Object> findAccountByIdcard(String idcard);
	/**
	 * 创建财务账户
	 * @param userId
	 */
	void createAccount(String userId);
	/**
	 * 查询账户余额
	 * @param accountId
	 * @return
	 */
	double findAccountBalance(String accountId);
	/**
	 * 查询上级
	 * @param account
	 * @return
	 */
	List<Map<String, Object>> findParList(Integer id);
	/**
	 * 根据账号查询上级推广人
	 * @param accountId
	 * @return
	 */
	List<Map<String,Object>> findRelationshopBySubAccount(String accountId);
	/**
	 * 新增客户产品费率
	 * @param accountid
	 * @param type
	 * @param fee
	 */
	void insertProductFee(String accountid,String type,double fee);
}
