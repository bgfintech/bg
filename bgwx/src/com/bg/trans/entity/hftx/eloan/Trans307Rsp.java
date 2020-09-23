package com.bg.trans.entity.hftx.eloan;

public class Trans307Rsp extends CommonTransRsp {
	private String cash_return_code;
	private String cash_return_message;
	private String cash_amt;
	private String cash_fee_amt;
	public String getCash_return_code() {
		return cash_return_code;
	}
	public void setCash_return_code(String cash_return_code) {
		this.cash_return_code = cash_return_code;
	}
	public String getCash_return_message() {
		return cash_return_message;
	}
	public void setCash_return_message(String cash_return_message) {
		this.cash_return_message = cash_return_message;
	}
	public String getCash_amt() {
		return cash_amt;
	}
	public void setCash_amt(String cash_amt) {
		this.cash_amt = cash_amt;
	}
	public String getCash_fee_amt() {
		return cash_fee_amt;
	}
	public void setCash_fee_amt(String cash_fee_amt) {
		this.cash_fee_amt = cash_fee_amt;
	}

	
}
