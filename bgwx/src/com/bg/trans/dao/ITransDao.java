package com.bg.trans.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bg.trans.entity.TransactionEntity;

public interface ITransDao {
	/**
	 * 记录交易流水
	 * @param entity
	 */
	int insertTransaction(TransactionEntity entity);
	/**
	 * 更新流水状态
	 * @param entity
	 */
	void updateTransaction(TransactionEntity entity);
	/**
	 * 查询交易
	 * @param orderNo
	 * @return
	 */
	TransactionEntity queryTransactionByOrderNo(String orderNo);

	List<TransactionEntity> queryMerchantOrder(String secMerchantId,String date);
	/**
	 * 根据订单号更新通知记录
	 * @param orderno
	 * @return
	 */
	boolean updateNotice(String orderno);
	
	List<TransactionEntity> queryNoticeOrder();
	/**
	 * 根据用户ID和交易日期查询快捷支付交易
	 * @param accountId
	 * @param date
	 * @return
	 */
	List<TransactionEntity> queryPayOrder(String accountId, String date);
	/**
	 * 
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankcardno
	 * @param vertify
	 * @param bankcardtype 
	 */
	void updateThreeVertify(String name, String idcard, String bankno,String bankcardno, String vertify, String bankcardtype);
	/**
	 * 查询交易根据reqno和商户ID和
	 * @param reqno
	 * @return
	 */
	TransactionEntity queryTransactionByReqNo(String reqno,String mid);
	/**
	 * 查询地区代码对应数据
	 * @param channelHftx
	 * @return
	 */
	List<Map<String, Object>> findAreaByChannel(String channelHftx);
	/**
	 * 查询银行行号对应数据
	 * @param channelHftx
	 * @return
	 */
	List<Map<String, Object>> findBanknoByChannel(String channelHftx);
	/**
	 * 查询用户推广奖励
	 * @param userId
	 * @return
	 */
	Map<String, Object> findRewardSpread(String userId);
	/**
	 * 新增奖励记录
	 * @param account
	 * @param recommender
	 * @param amt
	 * @param type
	 */
	int insertRewardLog(String account, String recommender, double amt,String type,String linkno,String linkType,double allamt,String channelAccount,String remark);
	/**
	 * 增加账户变动明细
	 * @param accountId
	 * @param balance
	 */
	void insertAAccountDetail(String accountId,double amount, double lastBalance,double afterBalance,String occurType,String linkno,String linkType);
	/**
	 * 
	 * @param accountId
	 * @param after
	 * @param date
	 */
	void updateAccountBalance(String accountId, double newbalance, Date updateTime);
	/**
	 * 查询客户交易1010的成功的金额和笔数
	 * @param accountId
	 * @return Map amt:总金额
	 * 			   num:总笔数
	 */
	Map<String, Object> findAllTransAmtByAccount(String accountId);
	/**
	 * 更新奖励日志中的累计交易金额remark
	 * @param id
	 * @param format
	 */
	void updateRewardLogRemark(int id, String format);
	/**
	 * 根据卡号查询最后一笔快捷支付申请记录
	 * @param bankCardno
	 * @return
	 */
	Map<String,Object> findLastQuickPayApplyByBankCardno(String bankCardno);
	
	/**
	 * 填加绑卡信息
	 * @param name
	 * @param idcard
	 * @param bankCardno
	 * @param bankphone
	 * @param treatychannel
	 * @param treatyid
	 * @param remark
	 */
	void insertBindBankCard(String name,String idcard,String bankCardno,String bankphone,String treatychannel,String treatyid,String remark);
}
