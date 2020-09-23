package com.bg.interfaces.entity;

import com.bg.interfaces.entity.internal.CountDetail;

public class Trans1100Rsp {
	private String resp_code;
	private String resp_desc;
	private String mer_id;
	private String order_id;
	private CountDetail civil;
	private CountDetail implement;
	private CountDetail criminal;
	private CountDetail administrative;
	private CountDetail bankrupt;
	private CountDetail preservation;
	
	public CountDetail getPreservation() {
		return preservation;
	}
	public void setPreservation(CountDetail preservation) {
		this.preservation = preservation;
	}
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}
	public String getResp_desc() {
		return resp_desc;
	}
	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public CountDetail getCivil() {
		return civil;
	}
	public void setCivil(CountDetail civil) {
		this.civil = civil;
	}
	public CountDetail getImplement() {
		return implement;
	}
	public void setImplement(CountDetail implement) {
		this.implement = implement;
	}
	public CountDetail getCriminal() {
		return criminal;
	}
	public void setCriminal(CountDetail criminal) {
		this.criminal = criminal;
	}
	public CountDetail getAdministrative() {
		return administrative;
	}
	public void setAdministrative(CountDetail administrative) {
		this.administrative = administrative;
	}
	public CountDetail getBankrupt() {
		return bankrupt;
	}
	public void setBankrupt(CountDetail bankrupt) {
		this.bankrupt = bankrupt;
	}
	
}
