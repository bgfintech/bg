package com.bg.trans.service;


import java.util.Map;

import com.bg.trans.entity.TransactionEntity;

public interface ITransYBZFService {
	/**
	 * 
	 * @param reqDTO
	 * @param entity
	 * @return
	 */
	Map<String,Object> transferSingle(Map<String,Object> params,TransactionEntity entity);
	/**
	 * 
	 * @param param
	 * @param entity
	 * @return
	 */
	Map<String,String> authBindCardRequest(Map<String,String> param,TransactionEntity entity);
	
	/**
	 * 
	 * @param param
	 * @param entity
	 * @return
	 */
	Map<String,String> authBindCardConfirm(Map<String,String> param,TransactionEntity entity);
	/**
	 * 
	 * @param param
	 * @param entity
	 * @return
	 */
	Map<String,String> treatyPay(Map<String,String> param,TransactionEntity entity);
	/**
	 * 
	 * @param param
	 * @return
	 */
	Map<String,Object> paymentQuery(Map<String,Object> param);
	/**
	 * 
	 * @param param
	 * @return
	 */
	Map<String,String> treatPayQuery(Map<String,String> param);
	
}
