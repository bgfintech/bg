package com.bg.trans.entity.hftx.eloan;

public class Trans303Req extends CommonTransReq {
	private String bind_request_seq;
	private String cert_type;
	private String cert_id;
	private String card_no;
	public String getBind_request_seq() {
		return bind_request_seq;
	}
	public void setBind_request_seq(String bind_request_seq) {
		this.bind_request_seq = bind_request_seq;
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
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	
}
