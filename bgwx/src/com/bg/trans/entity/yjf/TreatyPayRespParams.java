package com.bg.trans.entity.yjf;

public class TreatyPayRespParams extends ResponseEntity {
	private String tradeNo;
	private String tradeAmount;
	private String payerAmount;
	private String partnerAmount;
	private String payeeAmount;
	private String fee;
	private String tradeFinishTime;
	private String tradeStatus;
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getPayerAmount() {
		return payerAmount;
	}
	public void setPayerAmount(String payerAmount) {
		this.payerAmount = payerAmount;
	}
	public String getPartnerAmount() {
		return partnerAmount;
	}
	public void setPartnerAmount(String partnerAmount) {
		this.partnerAmount = partnerAmount;
	}
	public String getPayeeAmount() {
		return payeeAmount;
	}
	public void setPayeeAmount(String payeeAmount) {
		this.payeeAmount = payeeAmount;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTradeFinishTime() {
		return tradeFinishTime;
	}
	public void setTradeFinishTime(String tradeFinishTime) {
		this.tradeFinishTime = tradeFinishTime;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	
}
