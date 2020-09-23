package com.bg.trans.service;

import java.util.Map;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.hftx.Trans101Req;
import com.bg.trans.entity.hftx.Trans101Rsp;
import com.bg.trans.entity.hftx.Trans103Req;
import com.bg.trans.entity.hftx.Trans104Req;
import com.bg.trans.entity.hftx.Trans104Rsp;
import com.bg.trans.entity.hftx.Trans105Req;
import com.bg.trans.entity.hftx.Trans105Rsp;
import com.bg.trans.entity.hftx.Trans110Req;
import com.bg.trans.entity.hftx.Trans110Rsp;
import com.bg.trans.entity.hftx.Trans111Req;
import com.bg.trans.entity.hftx.Trans111Rsp;
import com.bg.trans.entity.hftx.Trans112Req;
import com.bg.trans.entity.hftx.Trans112Rsp;
import com.bg.trans.entity.hftx.Trans201Req;
import com.bg.trans.entity.hftx.Trans202Req;
import com.bg.trans.entity.hftx.Trans202Rsp;
import com.bg.trans.entity.hftx.Trans203Req;
import com.bg.trans.entity.hftx.Trans203Rsp;
import com.bg.trans.entity.hftx.Trans216Req;
import com.bg.trans.entity.hftx.Trans216Rsp;
import com.bg.trans.entity.hftx.Trans217Req;
import com.bg.trans.entity.hftx.Trans301Req;
import com.bg.trans.entity.hftx.Trans301Rsp;
import com.bg.trans.entity.hftx.Trans303Req;
import com.bg.trans.entity.hftx.Trans303Rsp;
import com.bg.trans.entity.hftx.Trans306Req;
import com.bg.trans.entity.hftx.Trans306Rsp;

public interface ITransHFTXService {
	/**
	 * 处理异步通知
	 * @param param
	 * @return
	 */
	String processNotify(String sign);
	/**
	 * 开户
	 * @param req
	 * @param entity 
	 * @return
	 */
	Trans101Rsp trans101(Trans101Req req, TransactionEntity entity);
	/**
	 * 取现绑卡
	 * @param req
	 * @param entity 
	 * @return
	 */
	Trans104Rsp trans104(Trans104Req req, TransactionEntity entity);
	/**
	 * 交易状态查询
	 * @param req
	 * @return
	 */
	Trans301Rsp trans301(Trans301Req req);
	/**
	 * 快捷支付统合版一阶段
	 * @param req
	 * @return
	 */
	Trans216Rsp trans216(Trans216Req req,TransactionEntity entity);
	/**
	 * 生成统合版二阶段请求参数String
	 * @param req
	 * @return
	 */
	Map<String,String> createTrans217Sign(Trans217Req req);
	/**
	 * 快捷支付一阶段
	 * @param req
	 * @param entity 
	 * @return
	 */
	Trans112Rsp trans112(Trans112Req req, TransactionEntity entity);
	/**
	 * 快捷支付二阶段
	 * @param req
	 * @return
	 */
	Map<String,String> createTrans201Sign(Trans201Req req);
	/**
	 * 余额查询
	 * @param req
	 * @return
	 */
	Trans303Rsp trans303(Trans303Req req);
	/**
	 * 转账
	 * @param req
	 * @return
	 */
	Trans203Rsp trans203(Trans203Req req,TransactionEntity entity);
	/**
	 * 取现
	 * @param req
	 * @return
	 */
	Trans202Rsp trans202(Trans202Req req,TransactionEntity entity);
	/**
	 * 快捷绑卡查询
	 * @param req
	 * @return
	 */
	Trans306Rsp trans306(Trans306Req req);
	
	/**
	 * 银行卡解绑
	 * @param req
	 * @return
	 */
	Trans105Rsp trans105(Trans105Req req);
	/**
	 * 短信发送接口 解绑卡用
	 * @param req
	 * @return
	 */
	Trans110Rsp trans110(Trans110Req req);
	/**
	 * 一阶段绑卡短信
	 * @param req
	 * @return
	 */
	Trans111Rsp trans111(Trans111Req req);
	/**
	 * 快捷绑卡
	 * @param req
	 * @return
	 */
	String createTrans103Sign(Trans103Req req);
	/**
	 * respCode转码
	 * @param status
	 * @return
	 */
	String transStatusToCode(String status);
	/**
	 * 处理回跳请求
	 * @param sign
	 * @return
	 */
	Map<String,String> processRetUrl(String sign);
	
	String getMerCustId();
	String getMerAcctId();
}
