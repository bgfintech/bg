package com.bg.trans.entity.gyzj;

public class StatData {
	private CountDetail count;//总案统计
	private Type administrative;//行政
	private Type civil;//民事
	private Type implement;//执行
	private Type criminal;//刑事
	private Type bankrupt;//清算破产
	private Type preservation;//诉前财产保全
	

	public Type getPreservation() {
		return preservation;
	}
	public void setPreservation(Type preservation) {
		this.preservation = preservation;
	}
	public CountDetail getCount() {
		return count;
	}
	public void setCount(CountDetail count) {
		this.count = count;
	}
	public Type getAdministrative() {
		return administrative;
	}
	public void setAdministrative(Type administrative) {
		this.administrative = administrative;
	}
	public Type getCivil() {
		return civil;
	}
	public void setCivil(Type civil) {
		this.civil = civil;
	}
	public Type getImplement() {
		return implement;
	}
	public void setImplement(Type implement) {
		this.implement = implement;
	}
	public Type getCriminal() {
		return criminal;
	}
	public void setCriminal(Type criminal) {
		this.criminal = criminal;
	}
	public Type getBankrupt() {
		return bankrupt;
	}
	public void setBankrupt(Type bankrupt) {
		this.bankrupt = bankrupt;
	}
}
