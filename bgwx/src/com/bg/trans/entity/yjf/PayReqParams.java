package com.bg.trans.entity.yjf;

import java.util.List;

public class PayReqParams extends RequestEntity {
	private String merchOrderNo;
	private String transAmount;
	private String accountName;
	private String certNo;
	private String accountNo;
	private String accountType;
	private String bankCode;
	private String purpose;
	
	private List<ShareProfits> shareProfits;
	
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getMerchOrderNo() {
		return merchOrderNo;
	}
	public void setMerchOrderNo(String merchOrderNo) {
		this.merchOrderNo = merchOrderNo;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public List<ShareProfits> getShareProfits() {
		return shareProfits;
	}
	public void setShareProfits(List<ShareProfits> shareProfits) {
		this.shareProfits = shareProfits;
	}
	
}

class ShareProfits {
	private String shareUserId;
	private String shareAmount;
	private String memo;
	public String getShareUserId() {
		return shareUserId;
	}
	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}
	public String getShareAmount() {
		return shareAmount;
	}
	public void setShareAmount(String shareAmount) {
		this.shareAmount = shareAmount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}