package com.bg.trans.entity.yjf;

public class AddBankCardRespParams extends ResponseEntity {
	private String signNo;
	private String smsKey;
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
	public String getSmsKey() {
		return smsKey;
	}
	public void setSmsKey(String smsKey) {
		this.smsKey = smsKey;
	}
	
}
