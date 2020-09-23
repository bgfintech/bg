package com.bg.trans.entity.ld;

public class UnbindReq extends CommonTransReq {
	private String mer_id;
	private String mer_cust_id;
	private String usr_busi_agreement_id;
	private String usr_pay_agreement_id;
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getMer_cust_id() {
		return mer_cust_id;
	}
	public void setMer_cust_id(String mer_cust_id) {
		this.mer_cust_id = mer_cust_id;
	}
	public String getUsr_busi_agreement_id() {
		return usr_busi_agreement_id;
	}
	public void setUsr_busi_agreement_id(String usr_busi_agreement_id) {
		this.usr_busi_agreement_id = usr_busi_agreement_id;
	}
	public String getUsr_pay_agreement_id() {
		return usr_pay_agreement_id;
	}
	public void setUsr_pay_agreement_id(String usr_pay_agreement_id) {
		this.usr_pay_agreement_id = usr_pay_agreement_id;
	}
	
}
