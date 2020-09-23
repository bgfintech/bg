package com.bg.trans.entity.yjf;

public class ConfirmAddBankCardReqParams extends RequestEntity {
	private String signNo;
	private String authMsg;
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
	public String getAuthMsg() {
		return authMsg;
	}
	public void setAuthMsg(String authMsg) {
		this.authMsg = authMsg;
	}
	
}
