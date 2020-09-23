package com.bg.trans.entity.gyzj;

public class CaseT {
	private String c_ah;
	private String n_ajbs;
	private String stage_type;
	private String case_type;
	private CaseT next;
	public String getC_ah() {
		return c_ah;
	}
	public void setC_ah(String c_ah) {
		this.c_ah = c_ah;
	}
	public String getN_ajbs() {
		return n_ajbs;
	}
	public void setN_ajbs(String n_ajbs) {
		this.n_ajbs = n_ajbs;
	}
	public String getStage_type() {
		return stage_type;
	}
	public void setStage_type(String stage_type) {
		this.stage_type = stage_type;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}
	public CaseT getNext() {
		return next;
	}
	public void setNext(CaseT next) {
		this.next = next;
	}
	
}
