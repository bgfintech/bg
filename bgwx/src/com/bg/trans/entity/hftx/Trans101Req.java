package com.bg.trans.entity.hftx;

public class Trans101Req {
	private String version; 
	private String cmd_id;
	private String mer_cust_id;
	private String order_id;
	private String order_date;
	private String solo_flg; //个体户标识 0否个人1是个体户 可选
	private String user_name;
	private String cert_id;//身份证号
	private String user_mobile;
	private String vali_date; //身份证有效期
	private String cust_prov;
	private String cust_area;
	private String bg_ret_url;
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
	public String getSolo_flg() {
		return solo_flg;
	}
	public void setSolo_flg(String solo_flg) {
		this.solo_flg = solo_flg;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCert_id() {
		return cert_id;
	}
	public void setCert_id(String cert_id) {
		this.cert_id = cert_id;
	}
	public String getUser_mobile() {
		return user_mobile;
	}
	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}
	public String getVali_date() {
		return vali_date;
	}
	public void setVali_date(String vali_date) {
		this.vali_date = vali_date;
	}
	public String getCust_prov() {
		return cust_prov;
	}
	public void setCust_prov(String cust_prov) {
		this.cust_prov = cust_prov;
	}
	public String getCust_area() {
		return cust_area;
	}
	public void setCust_area(String cust_area) {
		this.cust_area = cust_area;
	}
	public String getBg_ret_url() {
		return bg_ret_url;
	}
	public void setBg_ret_url(String bg_ret_url) {
		this.bg_ret_url = bg_ret_url;
	}
	
}
