package com.bg.interfaces.entity;

public class Trans1001Req {
	private String cmd_id;
	private String mer_id;
	private String order_date;
	private String order_id;
	private String trans_amt;
	private String terminalip;
	private String bg_ret_url;
	private String good_desc;
	private String pay_type;
	public String getCmd_id() {
		return cmd_id;
	}
	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getTrans_amt() {
		return trans_amt;
	}
	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}
	public String getTerminalip() {
		return terminalip;
	}
	public void setTerminalip(String terminalip) {
		this.terminalip = terminalip;
	}
	public String getBg_ret_url() {
		return bg_ret_url;
	}
	public void setBg_ret_url(String bg_ret_url) {
		this.bg_ret_url = bg_ret_url;
	}
	public String getGood_desc() {
		return good_desc;
	}
	public void setGood_desc(String good_desc) {
		this.good_desc = good_desc;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	
}
