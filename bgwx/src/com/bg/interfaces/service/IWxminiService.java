package com.bg.interfaces.service;

import java.util.List;
import java.util.Map;

public interface IWxminiService {
	int getBillListCount(String accountid);
	
	List<Map<String,Object>> getBillList(String accountid,int offset,int pagesize);

	List<Map<String, Object>> getQueryList(String accountid, String type,int offset, int pagesize);
}
