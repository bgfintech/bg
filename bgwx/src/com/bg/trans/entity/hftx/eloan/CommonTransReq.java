package com.bg.trans.entity.hftx.eloan;

public class CommonTransReq {
	private String app_token;
	private String client_name;
	private String company_name;
	private String request_seq;
	private String request_date;
	private String signature;
	public String getApp_token() {
		return app_token;
	}
	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
