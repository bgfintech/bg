package com.bg.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bg.web.dao.ICreditCardDao;
import com.bg.web.service.ICreditCardService;

@Service("creditCardService")
public class CreditCardServiceImpl implements ICreditCardService {
	@Autowired
	private ICreditCardDao creditCardDao;
	
	@Override
	public List<Map<String, Object>> findBindCreditCardList(String accountId) {
		return creditCardDao.findBindCreditCardList(accountId);
	}

	@Override
	public List<Map<String, Object>> findRepayPlanByPlanId(String planId) {
		return creditCardDao.findRepayPlanByPlanId(planId);
	}

	@Override
	public Map<String, Object> findCardInfoByCardno(String cardno) {
		return creditCardDao.findCardInfoByCardno(cardno);
	}

	@Override
	@Transactional
	public void createBatchRepayPlan(List<Object[]> datas) {
		creditCardDao.createBatchRepayPlan(datas);		
	}

	@Override
	public boolean isSetRepayPlanByCardno(String cardno) {
		return creditCardDao.isSetRepayPlanByCardno(cardno);
	}

	@Override
	public List<Map<String, Object>> findRunRepayPlan(String date) {
		return creditCardDao.findRunRepayPlan(date);
	}

	@Override
	public List<Map<String, Object>> findRunPayPlan(String date) {
		return creditCardDao.findRunPayPlan(date);
	}

	@Override
	public void updateInsteadRepayPlan(int pid, String status, Date date) {
		creditCardDao.updateInsteadRepayPlan(pid,status,date);
	}

}
