package com.bg.trans.entity.hftx.eloan;

public class Trans306Rsp extends CommonTransRsp {
	private String cert_type;
	private String cert_id;
	private String card_list;
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
	public String getCard_list() {
		return card_list;
	}
	public void setCard_list(String card_list) {
		this.card_list = card_list;
	}
	
}
