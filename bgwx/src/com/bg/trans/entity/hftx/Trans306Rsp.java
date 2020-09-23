package com.bg.trans.entity.hftx;

import java.util.List;

public class Trans306Rsp {
	private String cmd_id;
	private String resp_code;
	private String resp_desc;
	private String mer_cust_id;
	private String counts;
	private List<CardListEntity> card_list;
	public String getCmd_id() {
		return cmd_id;
	}
	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}
	public String getResp_desc() {
		return resp_desc;
	}
	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}
	public String getMer_cust_id() {
		return mer_cust_id;
	}
	public void setMer_cust_id(String mer_cust_id) {
		this.mer_cust_id = mer_cust_id;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public List<CardListEntity> getCard_list() {
		return card_list;
	}
	public void setCard_list(List<CardListEntity> card_list) {
		this.card_list = card_list;
	}
		
	
}