package com.bg.trans.entity.hftx.eloan;

public class Trans102Req extends CommonTransReq {
	private String cert_type;
	private String cert_id;
	private String user_name;
	private String card_type;
	private String card_no;
	private String bank_mobile;
	private String bank_id;
	private String bind_trans_id;
	private String step_flag;
	private String sms_code;
	private String bank_province_id;
	private String bank_area_id;
	private String bg_return_url;
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
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getBank_mobile() {
		return bank_mobile;
	}
	public void setBank_mobile(String bank_mobile) {
		this.bank_mobile = bank_mobile;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}
	public String getBind_trans_id() {
		return bind_trans_id;
	}
	public void setBind_trans_id(String bind_trans_id) {
		this.bind_trans_id = bind_trans_id;
	}
	public String getStep_flag() {
		return step_flag;
	}
	public void setStep_flag(String step_flag) {
		this.step_flag = step_flag;
	}
	public String getSms_code() {
		return sms_code;
	}
	public void setSms_code(String sms_code) {
		this.sms_code = sms_code;
	}
	public String getBank_province_id() {
		return bank_province_id;
	}
	public void setBank_province_id(String bank_province_id) {
		this.bank_province_id = bank_province_id;
	}
	public String getBank_area_id() {
		return bank_area_id;
	}
	public void setBank_area_id(String bank_area_id) {
		this.bank_area_id = bank_area_id;
	}
	public String getBg_return_url() {
		return bg_return_url;
	}
	public void setBg_return_url(String bg_return_url) {
		this.bg_return_url = bg_return_url;
	}
	
}
