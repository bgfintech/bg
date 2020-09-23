package com.bg.interfaces.entity;

import java.util.Map;

public class SendSmsReq {
	private String phone;
	private String templateId;
	private String signName;
	private Map<String,String> templateParam;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public Map<String, String> getTemplateParam() {
		return templateParam;
	}
	public void setTemplateParam(Map<String, String> templateParam) {
		this.templateParam = templateParam;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	
}
