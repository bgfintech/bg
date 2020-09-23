package com.bg.trans.service;

import javax.servlet.http.HttpServletResponse;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.ld.ApplySmsBindReq;
import com.bg.trans.entity.ld.ApplySmsBindRsp;
import com.bg.trans.entity.ld.ConfirmSmsBindReq;
import com.bg.trans.entity.ld.ConfirmSmsBindRsp;
import com.bg.trans.entity.ld.CreateOrderReq;
import com.bg.trans.entity.ld.CreateOrderRsp;
import com.bg.trans.entity.ld.OrderPayReq;
import com.bg.trans.entity.ld.OrderPayRsp;
import com.bg.trans.entity.ld.QueryOrderReq;
import com.bg.trans.entity.ld.QueryOrderRsp;
import com.bg.trans.entity.ld.UnbindReq;
import com.bg.trans.entity.ld.UnbindRsp;

public interface ITransLDService {
	ApplySmsBindRsp applySmsBind(ApplySmsBindReq req,TransactionEntity entity);
	
	ConfirmSmsBindRsp confirmSmsBind(ConfirmSmsBindReq req,TransactionEntity entity);
	
	CreateOrderRsp createOrder(CreateOrderReq req);
	
	OrderPayRsp orderPay(OrderPayReq req,TransactionEntity entity);
	
	QueryOrderRsp queryOrder(QueryOrderReq req);
	
	String processNofity(String requestParam,HttpServletResponse response);
	
	UnbindRsp unbind(UnbindReq req,TransactionEntity entity);
}
