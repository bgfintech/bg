package com.bg.interfaces.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.interfaces.dao.IWxminiDao;
import com.bg.interfaces.service.IWxminiService;

@Service("wxminiService")
public class WxminiServiceImpl implements IWxminiService {
	
	@Autowired
	private IWxminiDao wxminiDao;
	
	@Override
	public int getBillListCount(String accountid) {
		return wxminiDao.getBillListCount(accountid);
	}

	@Override
	public List<Map<String, Object>> getBillList(String accountid, int offset,int pagesize) {
		return wxminiDao.getBillList(accountid, offset, pagesize);
	}

	@Override
	public List<Map<String, Object>> getQueryList(String accountid,String type, int offset, int pagesize) {
		String t = "1".equals(type)?"1100":"1101";
		return wxminiDao.getQueryList(accountid,t,offset,pagesize);
	}



	
	
	

}
