package com.bg.trans.entity.ld;

public class ApplySmsBindRsp extends CommonTransRsp{
	private String mer_id;
	private String ret_code;
	private String ret_msg;
	private String bind_id;
	private String usr_busi_agreement_id;
	private String usr_pay_agreement_id;
	
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getBind_id() {
		return bind_id;
	}
	public void setBind_id(String bind_id) {
		this.bind_id = bind_id;
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
