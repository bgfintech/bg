package com.bg.trans.entity.hftx.eloan;

public class Trans204Req extends CommonTransReq {
	private String loan_request_seq;
	private String card_no;
	private String bg_return_url;
	public String getLoan_request_seq() {
		return loan_request_seq;
	}
	public void setLoan_request_seq(String loan_request_seq) {
		this.loan_request_seq = loan_request_seq;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getBg_return_url() {
		return bg_return_url;
	}
	public void setBg_return_url(String bg_return_url) {
		this.bg_return_url = bg_return_url;
	}
	
}
