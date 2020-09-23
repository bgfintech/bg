package com.bg.trans.service;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.yjf.AddBankCardReqParams;
import com.bg.trans.entity.yjf.AddBankCardRespParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardReqParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardRespParams;
import com.bg.trans.entity.yjf.PayReqParams;
import com.bg.trans.entity.yjf.PayRespParams;
import com.bg.trans.entity.yjf.ResponseEntity;
import com.bg.trans.entity.yjf.TreatyPayReqParams;
import com.bg.trans.entity.yjf.TreatyPayRespParams;

public interface ITransYJFService {
	/**
	 * 
	 * @param params
	 * @param entity
	 * @return
	 */
	PayRespParams pay(PayReqParams params,TransactionEntity entity);
	/**
	 * 
	 * @return
	 */
	AddBankCardRespParams addBankCard(AddBankCardReqParams params,TransactionEntity entity);
	/**
	 * 
	 * @param params
	 * @param entity
	 * @return
	 */
	ConfirmAddBankCardRespParams confirmAddBankCard(ConfirmAddBankCardReqParams params,TransactionEntity entity);
	/**
	 * 
	 * @param params
	 * @param entity
	 * @return
	 */
	TreatyPayRespParams treatyPay(TreatyPayReqParams params,TransactionEntity entity);
	/**
	 * 
	 * @param params
	 * @return
	 */
	PayRespParams payQuery(PayReqParams params,String mid);
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	TreatyPayRespParams treatyPayQuery(TreatyPayReqParams params,String mid);
	/**
	 * 
	 * @param str
	 * @return
	 */
	String sign(String str);
}
