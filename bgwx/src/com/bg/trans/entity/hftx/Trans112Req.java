package com.bg.trans.entity.hftx;

import java.util.List;

public class Trans112Req {
	private String version;
	private String cmd_id;
	private String mer_cust_id;
	private String user_cust_id;
	private String order_id;
	private String order_date;
	private String trans_amt;
	private String bind_card_id;
	private List<DivDetailEntity> div_detail;
	private String bg_ret_url;
	private String payer_term_type;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCmd_id() {
		return cmd_id;
	}
	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}
	public String getMer_cust_id() {
		return mer_cust_id;
	}
	public void setMer_cust_id(String mer_cust_id) {
		this.mer_cust_id = mer_cust_id;
	}
	public String getUser_cust_id() {
		return user_cust_id;
	}
	public void setUser_cust_id(String user_cust_id) {
		this.user_cust_id = user_cust_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getTrans_amt() {
		return trans_amt;
	}
	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}
	public String getBind_card_id() {
		return bind_card_id;
	}
	public void setBind_card_id(String bind_card_id) {
		this.bind_card_id = bind_card_id;
	}
	public List<DivDetailEntity> getDiv_detail() {
		return div_detail;
	}
	public void setDiv_detail(List<DivDetailEntity> div_detail) {
		this.div_detail = div_detail;
	}
	public String getBg_ret_url() {
		return bg_ret_url;
	}
	public void setBg_ret_url(String bg_ret_url) {
		this.bg_ret_url = bg_ret_url;
	}
	public String getPayer_term_type() {
		return payer_term_type;
	}
	public void setPayer_term_type(String payer_term_type) {
		this.payer_term_type = payer_term_type;
	}
	
}
