package com.bg.trans.entity.yjf;

public class TreatyPayReqParams extends RequestEntity {
	private String orderDesc;
	private String bizTp;
	private String tradeAmount;
	private String payeeUserId;
	private String signNo;
	private String distributeAmount;
	private String origOrderNo;
	
	public String getOrigOrderNo() {
		return origOrderNo;
	}
	public void setOrigOrderNo(String origOrderNo) {
		this.origOrderNo = origOrderNo;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getBizTp() {
		return bizTp;
	}
	public void setBizTp(String bizTp) {
		this.bizTp = bizTp;
	}
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getPayeeUserId() {
		return payeeUserId;
	}
	public void setPayeeUserId(String payeeUserId) {
		this.payeeUserId = payeeUserId;
	}
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
	public String getDistributeAmount() {
		return distributeAmount;
	}
	public void setDistributeAmount(String distributeAmount) {
		this.distributeAmount = distributeAmount;
	}
	
}
