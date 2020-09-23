package com.bg.trans.entity.hftx;

import java.util.List;

public class Trans216Req {
	private String version;
	private String cmd_id;
	private String mer_cust_id;
	private String user_cust_id;
	private String order_id;
	private String order_date;
	private String trans_amt;
	private List<DivDetailEntity> div_detail;
	private String in_cust_id;
	private String in_acct_id;
	private String card_no;
	private String card_mobile;
	private String card_prov;
	private String card_area;
	private String bg_ret_url;
	private String payer_term_type;//付款交易终端类型 01 电脑02手机
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
	
	public List<DivDetailEntity> getDiv_detail() {
		return div_detail;
	}
	public void setDiv_detail(List<DivDetailEntity> div_detail) {
		this.div_detail = div_detail;
	}
	public String getIn_cust_id() {
		return in_cust_id;
	}
	public void setIn_cust_id(String in_cust_id) {
		this.in_cust_id = in_cust_id;
	}
	public String getIn_acct_id() {
		return in_acct_id;
	}
	public void setIn_acct_id(String in_acct_id) {
		this.in_acct_id = in_acct_id;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getCard_mobile() {
		return card_mobile;
	}
	public void setCard_mobile(String card_mobile) {
		this.card_mobile = card_mobile;
	}
	public String getCard_prov() {
		return card_prov;
	}
	public void setCard_prov(String card_prov) {
		this.card_prov = card_prov;
	}
	public String getCard_area() {
		return card_area;
	}
	public void setCard_area(String card_area) {
		this.card_area = card_area;
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
