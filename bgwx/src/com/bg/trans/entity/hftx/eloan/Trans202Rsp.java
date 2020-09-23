package com.bg.trans.entity.hftx.eloan;

public class Trans202Rsp extends CommonTransRsp {
	private String fee_amt;
	private String trans_amt;
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
