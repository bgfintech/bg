package com.bg.trans.entity.hftx.eloan;

public class Trans308Rsp extends CommonTransRsp {
	private String acct_id;
	private String balance;
	private String acct_balance;
	private String freeze_balance;
	private String acct_state;
	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAcct_balance() {
		return acct_balance;
	}
	public void setAcct_balance(String acct_balance) {
		this.acct_balance = acct_balance;
	}
	public String getFreeze_balance() {
		return freeze_balance;
	}
	public void setFreeze_balance(String freeze_balance) {
		this.freeze_balance = freeze_balance;
	}
	public String getAcct_state() {
		return acct_state;
	}
	public void setAcct_state(String acct_state) {
		this.acct_state = acct_state;
	}
	
}
