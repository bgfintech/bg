package com.bg.trans.entity.gyzj;

public class OutData {
	private CountDetail count;
	private CaseData administrative;
	private CaseData civil;
	private CaseData criminal;
	private ImplementData implement;
	private BankruptData bankrupt;	
	private PreservationData preservation;
	private CaseTree cases_tree;
	
	public PreservationData getPreservation() {
		return preservation;
	}
	public void setPreservation(PreservationData preservation) {
		this.preservation = preservation;
	}
	public CountDetail getCount() {
		return count;
	}
	public void setCount(CountDetail count) {
		this.count = count;
	}
	public CaseData getAdministrative() {
		return administrative;
	}
	public void setAdministrative(CaseData administrative) {
		this.administrative = administrative;
	}
	public CaseData getCivil() {
		return civil;
	}
	public void setCivil(CaseData civil) {
		this.civil = civil;
	}
	public CaseData getCriminal() {
		return criminal;
	}
	public void setCriminal(CaseData criminal) {
		this.criminal = criminal;
	}
	public ImplementData getImplement() {
		return implement;
	}
	public void setImplement(ImplementData implement) {
		this.implement = implement;
	}
	public BankruptData getBankrupt() {
		return bankrupt;
	}
	public void setBankrupt(BankruptData bankrupt) {
		this.bankrupt = bankrupt;
	}
	public CaseTree getCases_tree() {
		return cases_tree;
	}
	public void setCases_tree(CaseTree cases_tree) {
		this.cases_tree = cases_tree;
	}
	
	
}
