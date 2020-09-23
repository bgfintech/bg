package com.bg.web.entity;

public class MerchantInfo {
	/**（系统内）商户ID**/
	public String merchantId;
	/**商户名称**/
	public String merchantName;
	
	/**渠道二级商户ID**/
	public String secmerchantId;
	
	public String legalName;
	
	public String settleBankAccountNo;
	
	public String settleName;
	
	public String contactName;
	
	public String contactPhone;
	

	
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getSettleBankAccountNo() {
		return settleBankAccountNo;
	}

	public void setSettleBankAccountNo(String settleBankAccountNo) {
		this.settleBankAccountNo = settleBankAccountNo;
	}

	public String getSettleName() {
		return settleName;
	}

	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getSecmerchantId() {
		return secmerchantId;
	}

	public void setSecmerchantId(String secmerchantId) {
		this.secmerchantId = secmerchantId;
	}

	

}
