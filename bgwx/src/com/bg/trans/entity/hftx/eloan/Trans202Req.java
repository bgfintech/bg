package com.bg.trans.entity.hftx.eloan;

public class Trans202Req extends CommonTransReq{
	private String business_type;
	private String product_type;
	private String cert_type;
	private String cert_id;
	private String user_name;
	private String card_no;
	private String repay_mode;
	private String trans_amt;
	private String loan_request_seq;
	private String loan_cert_type;
	private String back_cert_type;
	private String back_cer_id;
	private String back_div_details;
	private String bg_return_url;
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getCert_type() {
		return cert_type;
	}
	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}
	public String getCert_id() {
		return cert_id;
	}
	public void setCert_id(String cert_id) {
		this.cert_id = cert_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getRepay_mode() {
		return repay_mode;
	}
	public void setRepay_mode(String repay_mode) {
		this.repay_mode = repay_mode;
	}
	public String getTrans_amt() {
		return trans_amt;
	}
	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}
	public String getLoan_request_seq() {
		return loan_request_seq;
	}
	public void setLoan_request_seq(String loan_request_seq) {
		this.loan_request_seq = loan_request_seq;
	}
	public String getLoan_cert_type() {
		return loan_cert_type;
	}
	public void setLoan_cert_type(String loan_cert_type) {
		this.loan_cert_type = loan_cert_type;
	}
	public String getBack_cert_type() {
		return back_cert_type;
	}
	public void setBack_cert_type(String back_cert_type) {
		this.back_cert_type = back_cert_type;
	}
	public String getBack_cer_id() {
		return back_cer_id;
	}
	public void setBack_cer_id(String back_cer_id) {
		this.back_cer_id = back_cer_id;
	}
	public String getBack_div_details() {
		return back_div_details;
	}
	public void setBack_div_details(String back_div_details) {
		this.back_div_details = back_div_details;
	}
	public String getBg_return_url() {
		return bg_return_url;
	}
	public void setBg_return_url(String bg_return_url) {
		this.bg_return_url = bg_return_url;
	}
	
}
