package com.bg.interfaces.service;

import java.security.PublicKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IGateWayService {
	/**
	 * 开户
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public Object process2002(String mid, String cmid,String data,PublicKey publicKey);
	/**
	 * 解绑代扣卡
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public Object process2003(String mid, String cmid,String data,PublicKey publicKey);
	/**
	 * 查询订单
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1003(String mid,String cmid,String data,PublicKey publicKey);
	/**
	 * 扫码
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1001(String mid,String cmid,String data,PublicKey publicKey);
	/**
	 * 
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1007(String mid,String cmid,String data,PublicKey publicKey);
	/**
	 * 
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1008(String mid,String cmid,String data,PublicKey publicKey);
	/**
	 * 
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1021(String mid,String cmid,String data,PublicKey publicKey);
	/**
	 * 
	 * @param request
	 * @return
	 */
	void processNotify(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 
	 * @param cmd_id
	 * @param status
	 * @return
	 */
	String statusToRespCode(String cmd_id, String status);
	/**
	 * 余额提现
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1020(String mid, String cmid, String data,PublicKey publicKey);
	
	/**
	 * 协议支付绑卡一阶段
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1023(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 协议支付绑卡二阶段
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1024(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 协议支付
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1025(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 代付
	 * @param mid     布广商户编号
	 * @param cmid	      渠道商户编号
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1017(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 放款
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1026(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 协议还款
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1027(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 放款补单取现
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1028(String mid, String cmid, String data,PublicKey publicKey);
	
	/**
	 * 账户余额查询
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1029(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 卡BIN查询
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1030(String mid, String cmid, String data,PublicKey publicKey);
	
	/**
	 * 绑卡信息查询
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1031(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 协议支付解约
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1032(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 司法涉诉统计查询
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1100(String mid, String cmid, String data,PublicKey publicKey);
	/**
	 * 司法涉诉详情查询
	 * @param mid
	 * @param cmid
	 * @param data
	 * @param publicKey
	 * @return
	 */
	Object process1101(String mid, String cmid, String data,PublicKey publicKey);
	
}
