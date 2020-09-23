package com.bg.interfaces.entity;

import com.bg.interfaces.entity.internal.BankruptDetail;
import com.bg.interfaces.entity.internal.CaseDetail;
import com.bg.interfaces.entity.internal.CaseTree;
import com.bg.interfaces.entity.internal.ImplementDetail;
import com.bg.interfaces.entity.internal.PreservationDetail;

public class Trans1101Rsp {
	private String resp_code;
	private String resp_desc;
	private String mer_id;
	private String order_id;
	private CaseDetail civil;
	private CaseDetail criminal;
	private CaseDetail administrative;
	private ImplementDetail implement;
	private BankruptDetail bankrupt;
	private PreservationDetail preservation;
	private CaseTree cases_tree;
	
	
	public PreservationDetail getPreservation() {
		return preservation;
	}
	public void setPreservation(PreservationDetail preservation) {
		this.preservation = preservation;
	}
	public CaseTree getCases_tree() {
		return cases_tree;
	}
	public void setCases_tree(CaseTree cases_tree) {
		this.cases_tree = cases_tree;
	}
	public ImplementDetail getImplement() {
		return implement;
	}
	public void setImplement(ImplementDetail implement) {
		this.implement = implement;
	}
	public BankruptDetail getBankrupt() {
		return bankrupt;
	}
	public void setBankrupt(BankruptDetail bankrupt) {
		this.bankrupt = bankrupt;
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
	public CaseDetail getCivil() {
		return civil;
	}
	public void setCivil(CaseDetail civil) {
		this.civil = civil;
	}
	public CaseDetail getCriminal() {
		return criminal;
	}
	public void setCriminal(CaseDetail criminal) {
		this.criminal = criminal;
	}
	public CaseDetail getAdministrative() {
		return administrative;
	}
	public void setAdministrative(CaseDetail administrative) {
		this.administrative = administrative;
	}
	
}
