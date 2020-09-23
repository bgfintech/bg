package com.bg.trans.service;

import com.bg.trans.entity.TransactionEntity;
import com.bg.web.entity.AccountInfo;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyResultDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.BankCardDetailDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardCollectFromBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardPayToBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardSmsCollectConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTradeResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectResultDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.DownloadAccountFileReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.DownloadAccountFileRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.RefundReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.RefundRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.BaseSecMerchantRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantResponseDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantResponseDTO;
import com.lycheepay.gateway.client.dto.secmerchant.UpdateSecMerchantRequestDTO;

public interface ITransKFTService {
	/**
	 * 
	 * @param reqDTO 
	 * 	setOriginalOrderNo//原订单编号，查询的订单号
	 * @return
	 */
	TradeQueryRespDTO initiativePayQuery(TradeQueryReqDTO reqDTO);
	/**
	 * 主扫
	 * @param reqDTO
	 * setOrderNo//交易编号 
	 * setAmount//此次交易的具体金额,单位:分,不支持小数点
	 * setTradeName//商品描述,简要概括此次交易的内容.可能会在用户App上显示,建议用类似真实的内容，如：“购买洗衣液1.9kg”
	 * setBankNo//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
	 * setIsS0//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务。如果无此业务权限，此参数为1，则返回失败。 可空 
	 * @return
	 */
	ActiveScanPayRespDTO initiativePayZdsm(ActiveScanPayReqDTO reqDTO,TransactionEntity entity);
	/**
	 * 二级商户入驻
	 * @param reqDTO
	 * @return
	 */
	SettledSecMerchantResponseDTO settledSecMerchant(SettledSecMerchantRequestDTO reqDTO);
	/**
	 * 二级商户信息更新
	 * @param reqDTO
	 * @return
	 */
	BaseSecMerchantRespDTO updateSecMerchant(UpdateSecMerchantRequestDTO reqDTO);
	/**
	 * 二级商户查询
	 * @param reqDTO
	 * @return
	 */
	QuerySecMerchantResponseDTO querySecMerchant(QuerySecMerchantRequestDTO reqDTO);
	/**
	 * 被扫
	 * setSecMerchantId//二级商户ID 可空
	 * setAuthCode//扫码支付授权码，通过扫码枪等设备从用户微信/支付宝等APP读取的信息
	 * setAmount//此次交易的具体金额,单位:分,不支持小数点
	 * setTradeName//商品描述,简要概括此次交易的内容.可能会在用户App上显示
	 * setBankNo//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
	 * @param reqDTO
	 * @return
	 */
	PassiveScanPayRespDTO initiativePayBdsm(PassiveScanPayReqDTO reqDTO,AccountInfo acctinfo);
	/**
	 * 退款
	 * @param reqDTO
	 * @return
	 */
	RefundRespDTO initiativePayRefund(RefundReqDTO reqDTO,AccountInfo acctinfo);
	/**
	 * 从本地库中查询交易
	 * @param orderNo
	 * @return
	 */
	TransactionEntity queryTransByOrderNo(String orderNo);
	/**
	 * 更新交易信息
	 * @param orderNo
	 * @param status
	 * @param channelNo
	 */
	void updateTransaction(String orderNo, String status, String channelNo,String notifycontent);
	String ZDPayStatusToCode(String status);
	/**
	 * 短信快捷申请
	 * @param reqDTO
	 * @return
	 */
	SmsQuickPayApplyRespDTO smsQuickPayApply(SmsQuickPayApplyReqDTO reqDTO,TransactionEntity entity);
	/**
	 * 短信快捷确认
	 * @param reqDTO
	 * @return
	 */
	SmsQuickPayConfirmRespDTO smsQuickPayConfirm(SmsQuickPayConfirmReqDTO reqDTO,TransactionEntity entity);
	/**
	 * 快捷支付状态转码
	 * @param status
	 * @return
	 */
	String QPayStatusToCode(String status);
	/**
	 * 银行行号转银行名称
	 * @param bankno
	 * @return
	 */
	String banknoToBankName(String bankno);
	/**
	 * 
	 * @param dto
	 * @param accountinfo 
	 * @return
	 */
	SameIDCreditCardTradeResultDTO sameIDSmsCollect(SameIDCreditCardCollectFromBankAccountDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 
	 * @param dto
	 * @param accountInfo
	 * @return
	 */
	SameIDCreditCardTradeResultDTO sameIDSmsCollectConfirm(SameIDCreditCardSmsCollectConfirmDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 三要素验证
	 * @param dto
	 * @return
	 */
	SameIDCreditCardTradeResultDTO threeMessageVerify(BankCardDetailDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 同名卡代付
	 * @param dto
	 * @return
	 */
	SameIDCreditCardTradeResultDTO sameIDGbpPay(SameIDCreditCardPayToBankAccountDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 快捷协议代扣协议申请
	 * @param dto
	 * @param entity
	 * @return
	 */
	TreatyApplyResultDTO treatyCollectApply(TreatyApplyDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 快捷协议代扣申请确定
	 * @param dto
	 * @param entity
	 * @return
	 */
	TreatyConfirmResultDTO confirmTreatyCollectApply(TreatyConfirmDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 快捷协议代扣
	 * @param dto
	 * @param entity
	 * @return
	 */
	SameIDCreditCardTreatyCollectResultDTO sameIDCreditCardTreatyCollect(SameIDCreditCardTreatyCollectDTO dto,TransactionEntity entity,String gbpTransMerchantId);
	/**
	 * 根据根据选择交易账号
	 * @param bankno
	 * @return
	 */
	String selectGbpTransMerchantId(String bankno);
	/**
	 * 
	 * @param bankno
	 * @return
	 */
	String banknoToKFTbankno(String bankno);
	/**
	 * 公众号支付
	 * @param dto
	 * @return
	 */
	PublicNoPayRespDTO publicNoPay(PublicNoPayReqDTO dto,TransactionEntity entity);
	/**
	 * 
	 * @param payChannel
	 * @return
	 */
	String payChannelToKFTchannel(String payChannel);
	
}
