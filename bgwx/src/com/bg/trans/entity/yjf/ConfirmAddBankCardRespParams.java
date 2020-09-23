package com.bg.trans.entity.yjf;

public class ConfirmAddBankCardRespParams extends ResponseEntity {
	private String signNo;
	private String signAcctIssrId;
	private String signAcctType;
	private String status;
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
	public String getSignAcctIssrId() {
		return signAcctIssrId;
	}
	public void setSignAcctIssrId(String signAcctIssrId) {
		this.signAcctIssrId = signAcctIssrId;
	}
	public String getSignAcctType() {
		return signAcctType;
	}
	public void setSignAcctType(String signAcctType) {
		this.signAcctType = signAcctType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
