package com.bg.trans.entity.hftx.eloan;

public class CommonTransRsp {
	private String request_seq;
	private String request_date;
	private String response_code;
	private String response_message;
	private String signature;
	public String getRequest_seq() {
		return request_seq;
	}
	public void setRequest_seq(String request_seq) {
		this.request_seq = request_seq;
	}
	public String getRequest_date() {
		return request_date;
	}
	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public String getResponse_message() {
		return response_message;
	}
	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
