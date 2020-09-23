package com.bg.trans.entity.ld;

public class OrderPayReq extends CommonTransReq {
	private String mer_id;
	private String verify_code;
	private String trade_no;
	private String mer_cust_id;
	private String usr_busi_agreement_id;
	private String usr_pay_agreement_id;
	private String user_ip;
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getVerify_code() {
		return verify_code;
	}
	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
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
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	
}
