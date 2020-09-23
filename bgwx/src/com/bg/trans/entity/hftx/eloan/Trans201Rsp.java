package com.bg.trans.entity.hftx.eloan;

public class Trans201Rsp extends CommonTransRsp {
	private String loan_amount;
	private String loan_fee_amt;
	private String company_fee_amt;
	public String getLoan_amount() {
		return loan_amount;
	}
	public void setLoan_amount(String loan_amount) {
		this.loan_amount = loan_amount;
	}
	public String getLoan_fee_amt() {
		return loan_fee_amt;
	}
	public void setLoan_fee_amt(String loan_fee_amt) {
		this.loan_fee_amt = loan_fee_amt;
	}
	public String getCompany_fee_amt() {
		return company_fee_amt;
	}
	public void setCompany_fee_amt(String company_fee_amt) {
		this.company_fee_amt = company_fee_amt;
	}
	
}
