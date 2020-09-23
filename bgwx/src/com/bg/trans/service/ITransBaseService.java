package com.bg.trans.service;

import java.util.List;

import com.bg.trans.entity.TransRsp;
import com.bg.trans.entity.TransactionEntity;
import com.bg.web.entity.AccountInfo;

public interface ITransBaseService {

	TransactionEntity queryOrder(String orderno);
	/**
	 * 查询交易
	 * @param reqno
	 * @param mid
	 * @return
	 */
	TransactionEntity queryOrderByReqNo(String reqno,String mid);
	/**
	 * 根据商户ID和交易日期查询交易
	 * @param merchantId
	 * @param date
	 * @return
	 */
	List<TransactionEntity> queryMerchantOrder(String merchantId,String date);
	/**
	 * 根据用户ID和交易日期查询快捷支付交易
	 * @param merchantId
	 * @param date
	 * @return
	 */
	List<TransactionEntity> queryPayOrder(String accountId,String date);
	/**
	 * 修改三要素验证结果
	 * @param name
	 * @param idcard
	 * @param bankno
	 * @param bankcardno
	 * @param vertify
	 * @param bankcardtype 
	 */
	void updateThreeVertify(String name,String idcard,String bankno,String bankcardno,String vertify, String bankcardtype);
	
	/**
	 * 第三方支付开户
	 * 
	 */
	TransRsp createTransAccount(String name,String idcard,String mobile,String prov,String area,TransactionEntity entity);
	/**
	 * 绑定取现银行卡
	 * @return
	 */
	TransRsp bindCashCard(String custId,String cardno,String bankId,String prov,String area,TransactionEntity entity);
	/**
	 * 
	 * @return
	 */
	String bankno2name(String bankno);
	/**
	 * 同名卡支付申请
	 * @return
	 */
	TransRsp gbpPayApply(AccountInfo accountInfo,String custId,String custAcctId,String bankCardno,String bankphone,String transamt,TransactionEntity entity);
	/**
	 * 同名卡支付确认
	 * @return
	 */
	TransRsp gbpPayConfirm(String bindcardid,String custId,String orderno,String orderDate,String smscode,String openid,String accountid);
	/**
	 * 
	 * @param accountId
	 */
	double balanceToCash(String accountId,String deviceId);
	/**
	 * 
	 * @param accountId
	 */
	double merchantBalanceToCash(String mid,double amt,String linkno);
	/**
	 * 
	 * @param entity
	 * @return 
	 */
	int insertTransaction(TransactionEntity entity);
	/**
	 * 
	 * @param orderNo
	 * @param transStatus
	 * @param channelNo
	 * @param rspdata
	 */
	void updateTransaction(String orderNo, String transStatus,String channelNo, String rspdata);
	/**
	 * 
	 * @param accountId
	 * @param amount
	 * @param occurType
	 * @param linkno
	 * @param linkType
	 */
	void updateAccountBalance(String accountId, double amount,String occurType, String linkno, String linkType);
}
