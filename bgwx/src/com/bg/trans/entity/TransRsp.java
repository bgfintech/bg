package com.bg.trans.entity;

import java.util.HashMap;
import java.util.Map;

public class TransRsp {
	public static final String CODE_SUCCESS="00";
	public static final String CODE_FAIL="99";
	
	private String code;
	private String msg;
	private Map<String,Object> params=new HashMap<String,Object>();
	
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
