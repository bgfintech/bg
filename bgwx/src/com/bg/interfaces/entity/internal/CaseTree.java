package com.bg.interfaces.entity.internal;

import java.util.List;

public class CaseTree {
	private List<CaseT> criminal;//刑事
	private List<CaseT> civil;//民事
	private List<CaseT> administrative;//行政
	private List<CaseT> implement;//执行
	private List<CaseT> bankrupt;//破产
	private List<CaseT> preservation;//非诉保全
	
	public List<CaseT> getPreservation() {
		return preservation;
	}
	public void setPreservation(List<CaseT> preservation) {
		this.preservation = preservation;
	}
	public List<CaseT> getCriminal() {
		return criminal;
	}
	public void setCriminal(List<CaseT> criminal) {
		this.criminal = criminal;
	}
	public List<CaseT> getCivil() {
		return civil;
	}
	public void setCivil(List<CaseT> civil) {
		this.civil = civil;
	}
	public List<CaseT> getAdministrative() {
		return administrative;
	}
	public void setAdministrative(List<CaseT> administrative) {
		this.administrative = administrative;
	}
	public List<CaseT> getImplement() {
		return implement;
	}
	public void setImplement(List<CaseT> implement) {
		this.implement = implement;
	}
	public List<CaseT> getBankrupt() {
		return bankrupt;
	}
	public void setBankrupt(List<CaseT> bankrupt) {
		this.bankrupt = bankrupt;
	}
	
}
