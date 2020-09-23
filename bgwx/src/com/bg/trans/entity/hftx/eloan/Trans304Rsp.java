package com.bg.trans.entity.hftx.eloan;

public class Trans304Rsp extends CommonTransRsp {
	private String repay_return_code;
	private String repay_return_message;
	private String fee_amt;
	private String trans_amt;
	public String getRepay_return_code() {
		return repay_return_code;
	}
	public void setRepay_return_code(String repay_return_code) {
		this.repay_return_code = repay_return_code;
	}
	public String getRepay_return_message() {
		return repay_return_message;
	}
	public void setRepay_return_message(String repay_return_message) {
		this.repay_return_message = repay_return_message;
	}
	public String getFee_amt() {
		return fee_amt;
	}
	public void setFee_amt(String fee_amt) {
		this.fee_amt = fee_amt;
	}
	public String getTrans_amt() {
		return trans_amt;
	}
	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}
	
}
