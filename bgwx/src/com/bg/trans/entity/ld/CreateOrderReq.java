package com.bg.trans.entity.ld;

public class CreateOrderReq extends CommonTransReq {
	private String mer_id;
	private String notify_url;
	private String goods_id;
	private String goods_inf;
	private String order_id;
	private String mer_date;
	private String amount;
	private String amt_type;
	private String mer_priv;
	private String user_ip;
	private String expand;
	private String expire_time;
	private String risk_expand;
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_inf() {
		return goods_inf;
	}
	public void setGoods_inf(String goods_inf) {
		this.goods_inf = goods_inf;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getMer_date() {
		return mer_date;
	}
	public void setMer_date(String mer_date) {
		this.mer_date = mer_date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmt_type() {
		return amt_type;
	}
	public void setAmt_type(String amt_type) {
		this.amt_type = amt_type;
	}
	public String getMer_priv() {
		return mer_priv;
	}
	public void setMer_priv(String mer_priv) {
		this.mer_priv = mer_priv;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	public String getRisk_expand() {
		return risk_expand;
	}
	public void setRisk_expand(String risk_expand) {
		this.risk_expand = risk_expand;
	}
	
	
}
