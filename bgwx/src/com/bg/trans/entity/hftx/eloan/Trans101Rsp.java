package com.bg.trans.entity.hftx.eloan;

public class Trans101Rsp extends CommonTransRsp {
	private String bind_error_code;
	private String bind_error_message;
	public String getBind_error_code() {
		return bind_error_code;
	}
	public void setBind_error_code(String bind_error_code) {
		this.bind_error_code = bind_error_code;
	}
	public String getBind_error_message() {
		return bind_error_message;
	}
	public void setBind_error_message(String bind_error_message) {
		this.bind_error_message = bind_error_message;
	}
	
}
