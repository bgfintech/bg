package com.bg.trans.entity.zyht;

import java.math.BigDecimal;

public class GetDynCodeParams {
	private String transCode;//交易类型	String	4	是	是	微信：6004 支付宝: 7004
	private String accountId;//账户ID
	private String transTime;//交易时间
	private String transDate;//交易日期
	private String tranAmt;//交易金额	
	private String currency="CNY";//货币代码
	private String agentOrderNo;//商户订单号 64
	private String channelFlag;//交易渠道标识	String	1	是	否	A:微信,F:支付宝	
	private String rspCode;//银行系统应答码	String	4	否	是	0000 成功，9999 失败
	private String rspMsg;//银行系统应答消息
	private String merOrderNo;//收单系统订单号
	private String channelOrderNo;//支付渠道订单号
	private String qrCode;//二维码信息区域	
	private String payType;//支付类型	
	

	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAgentOrderNo() {
		return agentOrderNo;
	}
	public void setAgentOrderNo(String agentOrderNo) {
		this.agentOrderNo = agentOrderNo;
	}
	public String getMerOrderNo() {
		return merOrderNo;
	}
	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}
	public String getChannelOrderNo() {
		return channelOrderNo;
	}
	public void setChannelOrderNo(String channelOrderNo) {
		this.channelOrderNo = channelOrderNo;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
}
