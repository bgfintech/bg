package com.bg.interfaces.entity;

public class Trans1026Req {
	private String cmd_id;
	private String mer_id;
	private String order_date;
	private String order_id;
	private String product_type;
	private String loan_purpose;
	private String loan_start_date;
	private String loan_end_date;
	private String loan_period;
	private String loan_period_type;
	private String contract_no;
	private String loan_amount;
	private String entrusted_flag;
	private String user_cert_type;
	private String user_cert_id;
	private String user_name;
	private String user_card_no;
	private String user_amount;      //借款人金额
	private String user_cash_method; //取现方式 T0 即时到账 T1第二日到账
	private String merchant_cert_type;	//受托支付企业证件类型0-普通营业执照企业； 01-三证合一企业
	private String merchant_cert_id;	//受托支付企业证件号
	private String merchant_amount;		//受托支付金额
	private String installment_number;	//分期期数
	private String installment_rate;	//分期利率
	private String payment_method;
	private String loan_comment;
	private String auditor;
	private String audit_time;
	private String bg_ret_url;
	
	public String getLoan_purpose() {
		return loan_purpose;
	}
	public void setLoan_purpose(String loan_purpose) {
		this.loan_purpose = loan_purpose;
	}
	public String getCmd_id() {
		return cmd_id;
	}
	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}
	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getLoan_start_date() {
		return loan_start_date;
	}
	public void setLoan_start_date(String loan_start_date) {
		this.loan_start_date = loan_start_date;
	}
	public String getLoan_end_date() {
		return loan_end_date;
	}
	public void setLoan_end_date(String loan_end_date) {
		this.loan_end_date = loan_end_date;
	}
	public String getLoan_period() {
		return loan_period;
	}
	public void setLoan_period(String loan_period) {
		this.loan_period = loan_period;
	}
	public String getLoan_period_type() {
		return loan_period_type;
	}
	public void setLoan_period_type(String loan_period_type) {
		this.loan_period_type = loan_period_type;
	}
	public String getContract_no() {
		return contract_no;
	}
	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}
	public String getLoan_amount() {
		return loan_amount;
	}
	public void setLoan_amount(String loan_amount) {
		this.loan_amount = loan_amount;
	}
	public String getEntrusted_flag() {
		return entrusted_flag;
	}
	public void setEntrusted_flag(String entrusted_flag) {
		this.entrusted_flag = entrusted_flag;
	}
	public String getUser_cert_type() {
		return user_cert_type;
	}
	public void setUser_cert_type(String user_cert_type) {
		this.user_cert_type = user_cert_type;
	}
	public String getUser_cert_id() {
		return user_cert_id;
	}
	public void setUser_cert_id(String user_cert_id) {
		this.user_cert_id = user_cert_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_card_no() {
		return user_card_no;
	}
	public void setUser_card_no(String user_card_no) {
		this.user_card_no = user_card_no;
	}
	public String getUser_amount() {
		return user_amount;
	}
	public void setUser_amount(String user_amount) {
		this.user_amount = user_amount;
	}
	public String getUser_cash_method() {
		return user_cash_method;
	}
	public void setUser_cash_method(String user_cash_method) {
		this.user_cash_method = user_cash_method;
	}
	public String getMerchant_cert_type() {
		return merchant_cert_type;
	}
	public void setMerchant_cert_type(String merchant_cert_type) {
		this.merchant_cert_type = merchant_cert_type;
	}
	public String getMerchant_cert_id() {
		return merchant_cert_id;
	}
	public void setMerchant_cert_id(String merchant_cert_id) {
		this.merchant_cert_id = merchant_cert_id;
	}
	public String getMerchant_amount() {
		return merchant_amount;
	}
	public void setMerchant_amount(String merchant_amount) {
		this.merchant_amount = merchant_amount;
	}
	public String getInstallment_number() {
		return installment_number;
	}
	public void setInstallment_number(String installment_number) {
		this.installment_number = installment_number;
	}
	public String getInstallment_rate() {
		return installment_rate;
	}
	public void setInstallment_rate(String installment_rate) {
		this.installment_rate = installment_rate;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public String getLoan_comment() {
		return loan_comment;
	}
	public void setLoan_comment(String loan_comment) {
		this.loan_comment = loan_comment;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	public String getBg_ret_url() {
		return bg_ret_url;
	}
	public void setBg_ret_url(String bg_ret_url) {
		this.bg_ret_url = bg_ret_url;
	}
	
	
}
