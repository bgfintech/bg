package com.bg.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bg.trans.dao.ITransDao;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.dao.ICostDao;
import com.bg.web.service.ICostService;

@Service("costService")
public class CostServiceImpl implements ICostService {
	
	@Autowired
	private ICostDao costDao;
	@Autowired
	private ITransDao transDao;
	
	@Autowired
	private IAccountDao accountDao;
	
	
	@Override
	public List<Map<String, Object>> findCostList(String account, int offset,int limit) {
		return costDao.queryCostList(account, offset, limit);
	}

	@Override
	public int countCostList(String account) {
		return costDao.countCostList(account);
	}
	
	/**
	 * 充值
	 * @param account
	 * @param type
	 */
	@Transactional
	@Override
	public void recharge(String account,String type,String linkno,double fee){	
		double balance = accountDao.findAccountBalance(account);
		double after = UtilData.add(balance, fee);
		transDao.insertAAccountDetail(account,fee,balance,after,type,linkno,"b_transaction");
		transDao.updateAccountBalance(account,after,new Date());		
	}

}
