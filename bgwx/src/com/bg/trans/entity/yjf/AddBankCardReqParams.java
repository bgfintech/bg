package com.bg.trans.entity.yjf;

public class AddBankCardReqParams extends RequestEntity{
	private String signAccId;
	private String signName;
	private String signID;
	private String signMobile;
	public String getSignAccId() {
		return signAccId;
	}
	public void setSignAccId(String signAccId) {
		this.signAccId = signAccId;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getSignID() {
		return signID;
	}
	public void setSignID(String signID) {
		this.signID = signID;
	}
	public String getSignMobile() {
		return signMobile;
	}
	public void setSignMobile(String signMobile) {
		this.signMobile = signMobile;
	}
	
}
