package com.bg.trans.entity.zyht;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

public class CycleQueryParams {
	private String transCode;//交易类型	String	4	是	是	微信：6004 支付宝: 7004
	private String accountId;//账户ID
	private String transTime;//交易时间
	private String transDate;//交易日期
	private BigDecimal tranAmt;//交易金额
	private String rspCode;//银行系统应答码	String	4	否	是	0000 成功，9999 失败
	private String rspMsg;//银行系统应答消息
	private String agentOrderNo;//商户订单号 64
	private String merOrderNo;//收单系统订单号
	private String channelOrderNo;//支付渠道订单号
	private String oriTransDate;//原交易日期	String	8	否	是	订单初始生成时交易日期:yyyyMMdd
	private String oriTransState;//原交易状态	String	4	否	是	"原交易状态：0000 成功，9999 失败"
	private String channelFlage;//交易渠道标识	String	1	是	否	A:微信,F:支付宝
	private String cancelFlag;//原交易撤销标志	String	1	否	是	"返回原交易撤销标志：0-正常；1-已撤销"
	
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
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(BigDecimal tranAmt) {
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
	public String getOriTransDate() {
		return oriTransDate;
	}
	public void setOriTransDate(String oriTransDate) {
		this.oriTransDate = oriTransDate;
	}
	public String getOriTransState() {
		return oriTransState;
	}
	public void setOriTransState(String oriTransState) {
		this.oriTransState = oriTransState;
	}
	public String getChannelFlage() {
		return channelFlage;
	}
	public void setChannelFlage(String channelFlage) {
		this.channelFlage = channelFlage;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
}
