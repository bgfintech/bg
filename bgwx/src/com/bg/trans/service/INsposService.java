package com.bg.trans.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.nspos.TransE1102Req;
import com.bg.trans.entity.nspos.TransE1102Rsp;
import com.bg.trans.entity.nspos.TransE1103Req;
import com.bg.trans.entity.nspos.TransE1103Rsp;

/**
 * 汇付天下智汇管家
 * @author YH
 * @date 2019年7月1日
 * @version 1.0
 */
public interface INsposService {
	TransE1103Rsp transE1103(TransE1103Req req,TransactionEntity entity);
	
	TransE1102Rsp transE1102(TransE1102Req req,TransactionEntity entity);
	
	void processNotify(HttpServletRequest request,HttpServletResponse response);
}
