package com.bg.trans.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.sd.OrderCreateRspParam;

public interface ITransSDService {
	/**
	 * 统一下单
	 * @param entity
	 * @param te
	 * @return
	 */
	JSONObject createOrder(HttpServletRequest request);
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	JSONObject queryOrder(HttpServletRequest request,TransactionEntity transEntity);
	/**
	 * 代付
	 * @param request
	 * @return
	 */
	JSONObject agentPay(HttpServletRequest request);
	
	/**
	 * 查询代付订单
	 * @param request
	 * @return
	 */
	JSONObject queryAgentPay(HttpServletRequest request,TransactionEntity transEntity);
}
