package com.bg.trans.service.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.sandpay.cashier.sdk.util.CertUtil;
import cn.com.sandpay.cashier.sdk.util.CryptoUtil;
import cn.com.sandpay.cashier.sdk.util.HttpClient;
import cn.com.sandpay.cashier.sdk.util.SDKUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransSDService;
import com.bg.util.SetSystemProperty;
import com.bg.util.UtilData;
import com.bg.web.service.MerchantServiceI;
@Service("transSDService")
public class TransSDServiceImpl implements ITransSDService {
	private static Logger logger = Logger.getLogger(TransSDServiceImpl.class);
	@Autowired
	private ITransDao transDao;	
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private ITransBaseService transBaseService;
	
	private static String gateWayUrl = SetSystemProperty.getKeyValue("pay.getWayUrl","sd/sd.properties");
	private static String payAgentUrl = SetSystemProperty.getKeyValue("pay.payAgentUrl","sd/sd.properties");
	private static String plMid=SetSystemProperty.getKeyValue("pay.plMid","sd/sd.properties");
	private static String publicKeyPath = SetSystemProperty.getKeyValue("pay.publicKeyPath","sd/sd.properties");
	private static String privateKeyPath  = SetSystemProperty.getKeyValue("pay.privateKeyPath","sd/sd.properties");
	private static String keyPassword= SetSystemProperty.getKeyValue("pay.keyPassword","sd/sd.properties");
	private static String notifyUrl = SetSystemProperty.getKeyValue("pay.notifyUrl","sd/sd.properties");
	static{
		try {
			//CertUtil.init(publicKeyPath,privateKeyPath,keyPassword);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public JSONObject createOrder(HttpServletRequest request) {
		String service = request.getParameter("service");
		String mid = request.getParameter("mid");
		String channelType=request.getParameter("channelType");
		String reqTime = request.getParameter("reqTime");
		String amtStr = "000000000000"+request.getParameter("amount");
		String subject = request.getParameter("transName");
		String sbody = request.getParameter("remark");
		String clientIp = request.getParameter("clientIp");
		String frontUrl = request.getParameter("frontUrl");
		String oNotifyUrl = request.getParameter("notifyUrl");
		String reqNo = request.getParameter("orderNo");
		String amount = amtStr.substring(amtStr.length()-12);
		String secmid = merchantService.findChannelMid(mid, PARA_CONSTATNTS.Channel_SD);
		String orderCode = UtilData.createOnlyData();
		String payMode = serviceToPayMode(service);
		TransactionEntity te = new TransactionEntity();
		JSONObject requestJson = new JSONObject();
		JSONObject head = new JSONObject();
		JSONObject body = new JSONObject();
		JSONObject payExtra = new JSONObject();
		requestJson.put("head", head);
		requestJson.put("body", body);
		/************head**************/
		head.put("version", "1.0");
		head.put("method", "sandpay.trade.pay");
		head.put("productId", serviceToProductId(service));
		head.put("accessType", "2");
		head.put("mid", secmid);
		head.put("plMid", plMid);
		head.put("channelType", channelType);
		head.put("reqTime",reqTime);
		/***********body****************/
		body.put("orderCode", orderCode);
		body.put("totalAmount", amount);
		body.put("subject", subject);
		body.put("body", sbody);
		body.put("payMode", payMode);
		body.put("payExtra", payExtra);			
		body.put("clientIp", clientIp);
		body.put("notifyUrl", notifyUrl);
		body.put("frontUrl", frontUrl);		
		/**********payExtra**********************/
		if("sand_wxh5".equals(payMode)){
			payExtra.put("ip", clientIp);
			payExtra.put("sceneInfo", subject);
			te.setPaychannel(PARA_CONSTATNTS.PayChannel_WX);//		
		}else if("sand_h5".equals(payMode)){
//			String cardno = request.getParameter("cardNo");
			te.setPaychannel(PARA_CONSTATNTS.PayChannel_YL);//
		}		
		/*************TransactionEntity*******************/
		te.setTrantype(PARA_CONSTATNTS.TransCode_H5);
		te.setReqno(reqNo);		
		te.setMerchantid(mid);		
		te.setTranstime(reqTime);
		te.setChannel(PARA_CONSTATNTS.Channel_SD);
		te.setOrderno(orderCode);
		te.setTranamt(Integer.parseInt(amount));	
		te.setSecmerchantid(secmid);
		te.setNotifyurl(oNotifyUrl);
		te.setIsnotice("2");
		/***************dopost***************************/
		String reqAddr = "/order/pay";
		String data = requestJson.toJSONString();
		JSONObject resp =null;
		try {
			logger.info("统一下单请求：" + JSON.toJSONString(data));
			resp = doPost(gateWayUrl+reqAddr,data);			
			String status = "000000".equals(resp.getJSONObject("head").getString("respCode"))?"03":"02";	
			te.setStatus(status);
			te.setReqcontent(data);
			te.setRespcontent(JSON.toJSONString(resp));			
			transDao.insertTransaction(te);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return resp;
	}
	

	@Override
	public JSONObject queryOrder(HttpServletRequest request,TransactionEntity transEntity) {
		String oriService = request.getParameter("originalService");
		String channelType=request.getParameter("channelType");
		String reqTime = request.getParameter("reqTime");
		String secmid = transEntity.getMerchantid();
		String oriOrderNo = transEntity.getOrderno();
		JSONObject requestJson = new JSONObject();
		JSONObject head = new JSONObject();
		JSONObject body = new JSONObject();
		requestJson.put("head", head);
		requestJson.put("body", body);
		/************head**************/
		head.put("version", "1.0");
		head.put("method", "sandpay.trade.query");
		head.put("productId", serviceToProductId(oriService));
		head.put("accessType", "2");
		head.put("mid", secmid);
		head.put("plMid", plMid);
		head.put("channelType", channelType);
		head.put("reqTime",reqTime);
		/***********body****************/
		body.put("orderCode", oriOrderNo);		
		/***************dopost***************************/
		String reqAddr = "/order/query";
		String data = requestJson.toJSONString();
		JSONObject resp =null;
		try {
			logger.info("查询订单请求：" + JSON.toJSONString(data));
			resp = doPost(gateWayUrl+reqAddr,data);			
			String status = "000000".equals(resp.getJSONObject("head").getString("respCode"))?"01":"02";	
			logger.info("查询订单响答：" + JSON.toJSONString(resp));
//			transDao.insertTransaction(te);
			if("01".equals(status)){
				TransactionEntity te = new TransactionEntity();
				te.setOrderno(oriOrderNo);
				String orderStatus = resp.getJSONObject("body").getString("orderStatus");
				String s = null;
				if("00".equals(orderStatus)){
					s="01";
				}else if("02".equals(orderStatus)){
					s="02";
				}else{
					s="03";
				}
				te.setStatus(s);
				transDao.updateTransaction(te);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return resp;
	}
	
	@Override
	public JSONObject agentPay(HttpServletRequest request) {
		String transCode = "RTCO";
		String mid = request.getParameter("mid");
		String reqTime = request.getParameter("reqTime");
		String amtStr = "000000000000"+request.getParameter("amount");
		String reqNo = request.getParameter("orderNo");
		String custBankNo = request.getParameter("custBankNo");
		String custName = request.getParameter("custName");
		String amount = amtStr.substring(amtStr.length()-12);
		String secmid = merchantService.findChannelMid(mid, PARA_CONSTATNTS.Channel_SD);
		String orderCode = UtilData.createOnlyData();
		TransactionEntity te = new TransactionEntity();
		JSONObject requestJson = new JSONObject();
		/************requestJson**************/
		requestJson.put("version", "01");								//版本号      
		requestJson.put("productId", "00000004");              //产品ID     
		requestJson.put("tranTime", reqTime);                     //交易时间     
		requestJson.put("orderCode",orderCode);                      //订单号      
		//requestJson.put("timeOut", DemoBase.getNextDayTime());                      //订单超时时间   
		requestJson.put("tranAmt", amtStr);                                 //金额       
		requestJson.put("currencyCode", "156");                    //币种       
		requestJson.put("accAttr", "0");               //账户属性     0-对私   1-对公
		requestJson.put("accType", "4");             //账号类型      3-公司账户  4-银行卡
		requestJson.put("accNo", custBankNo);     //收款人账户号   
		requestJson.put("accName", custName);               	//收款人账户名   
		//requestJson.put("provNo", "");            //收款人开户省份编码
		//requestJson.put("cityNo", "");            //收款人开会城市编码
		//requestJson.put("bankName", "");                                            //收款账户开户行名称
		//requestJson.put("bankType", "123456123456");                                //收款人账户联行号 
		requestJson.put("remark", "代付");                                          	//摘要       
		//requestJson.put("channelType", "");                                         //渠道类型   
		//requestJson.put("reqReserved", "");                                         //请求方保留域  
		//requestJson.put("extend", "");                                              //扩展域
		/*************TransactionEntity*******************/
		te.setTrantype(PARA_CONSTATNTS.TransCode_AgentPay);
		te.setReqno(reqNo);		
		te.setMerchantid(mid);		
		te.setTranstime(reqTime);
		te.setChannel(PARA_CONSTATNTS.Channel_SD);
		te.setOrderno(orderCode);
		te.setTranamt(Integer.parseInt(amount));	
		te.setSecmerchantid(secmid);
		te.setBankcardno(custBankNo);
		te.setName(custName);
		te.setIsnotice("2");
		/***************doAgentPost***************************/
		String reqAddr = "/openapi/agentpay";
		String data = requestJson.toJSONString();
		JSONObject resp =null;
		try {
			logger.info("杉德代付请求：" + JSON.toJSONString(data));
			resp = doAgentPost(payAgentUrl+reqAddr,data,transCode,secmid);
			String respCode =  resp.getString("respCode");
			String status = "0".equals(respCode)?"01":"1".equals(respCode)?"02":"03";	
			te.setStatus(status);
			te.setReqcontent(data);
			te.setRespcontent(JSON.toJSONString(resp));			
			transDao.insertTransaction(te);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return resp;
	}
	
	@Override
	public JSONObject queryAgentPay(HttpServletRequest request,TransactionEntity transEntity) {
		String transCode = "ODQU";
		String secmid = transEntity.getMerchantid();
		String oriOrderNo = transEntity.getOrderno();
		String reqTime = request.getParameter("reqTime");
		TransactionEntity te = new TransactionEntity();
		JSONObject requestJson = new JSONObject();
		/************requestJson**************/
		requestJson.put("version", "01");                     // 版本号  
		requestJson.put("productId", "00000004");    // 产品ID  
		requestJson.put("tranTime", reqTime);           	      // 查询订单的交易时间
		requestJson.put("orderCode", oriOrderNo);                 // 要查询的订单号  
		requestJson.put("extend", "");    
		/***************doAgentPost***************************/
		String reqAddr = "/openapi/queryOrder";
		String data = requestJson.toJSONString();
		JSONObject resp =null;
		try {
			logger.info("杉德代付查询请求：" + JSON.toJSONString(data));
			resp = doAgentPost(payAgentUrl+reqAddr,data,transCode,secmid);
			String respCode =  resp.getString("respCode");
			String status = "0".equals(respCode)?"01":"1".equals(respCode)?"02":"03";	
			te.setOrderno(oriOrderNo);
			te.setStatus(status);	
			transDao.updateTransaction(te);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return resp;
	}
	
	private String serviceToProductId(String service){
		String productId=null;
		switch(service) {
		case "bg_h5_pay":    	//银行卡H5支付
			productId="00000008";
		case "bg_wxh5_pay":	//微信H5支付
			productId="00000025";
			break;
		}		
		return productId;
	}
	
	private String serviceToPayMode(String service){
		String payMode=null;
		switch(service) {
		case  "bg_h5_pay":    	//银行卡H5支付
			payMode="sand_h5";
		case  "bg_wxh5_pay":	//微信H5支付
			payMode="sand_wxh5";
			break;
		}		
		return payMode;
	}
	
	private JSONObject doPost(String url,String data) throws Exception{
		int connectTimeout = 300000;
		//http响应超时时间
		int readTimeout = 600000;
		Map<String, String> reqMap = new HashMap<String,String>();
		String reqSign;
		// 签名
		reqSign = new String(Base64.encodeBase64(CryptoUtil.digitalSign(data.getBytes("UTF-8"), CertUtil.getPrivateKey(), "SHA1WithRSA")));		
		//整体报文格式
		reqMap.put("charset", "UTF-8");
		reqMap.put("data", data);
		reqMap.put("signType", "01");
		reqMap.put("sign", reqSign);
		reqMap.put("extend", "");	
		String result = HttpClient.doPost(url, reqMap, connectTimeout, readTimeout);
		Map<String, String> respMap = SDKUtil.convertResultStringToMap(result);		
		String respData = respMap.get("data");
		String respSign = respMap.get("sign");		
		// 验证签名
		boolean valid;
		valid = CryptoUtil.verifyDigitalSign(respData.getBytes("UTF-8"), Base64.decodeBase64(respSign), CertUtil.getPublicKey(),"SHA1WithRSA");
		if(!valid) {
			throw new Exception("verify sign fail");
		}			
		logger.info("verify sign success");
		JSONObject respJson=JSONObject.parseObject(respData);
		if(respJson!=null) {
			logger.info("响应码：["+respJson.getJSONObject("head").getString("respCode")+"]");	
			logger.info("响应描述：["+respJson.getJSONObject("head").getString("respMsg")+"]");	
			logger.info("响应报文：\n"+JSONObject.toJSONString(respJson,true));	
		}else {
			logger.error("服务器请求异常！！！");	
			throw new Exception("服务器请求异常！！！");
		}			
		return respJson;
	}
	
	

	private JSONObject doAgentPost(String url,String data,String transCode,String merId) throws Exception{
		int connectTimeout = 300000;
		//http响应超时时间
		int readTimeout = 600000;
		/**************生成加密KEY，加密数据和签名******************/
		String aesKey = getRandomStringByLength(16);
		byte[] aesKeyBytes = aesKey.getBytes("UTF-8");		
		byte[] plainBytes = data.getBytes("UTF-8");
		String encryptData = new String(Base64.encodeBase64(CryptoUtil.AESEncrypt(plainBytes, aesKeyBytes,"AES","AES/ECB/PKCS5Padding", null)),"UTF-8");
		String sign = new String(Base64.encodeBase64(CryptoUtil.digitalSign(plainBytes, CertUtil.getPrivateKey(), "SHA1WithRSA")), "UTF-8");		
		String encryptKey = new String(Base64.encodeBase64(CryptoUtil.RSAEncrypt(aesKeyBytes,CertUtil.getPublicKey(),2048,11,"RSA/ECB/PKCS1Padding")),"UTF-8");
		
		Map<String, String> reqMap = new HashMap<String, String>();
		//整体报文格式
		reqMap.put("transCode", transCode); // 交易码
		reqMap.put("accessType", "01"); // 接入类型
		reqMap.put("merId", merId); // 合作商户ID	杉德系统分配，唯一标识
		reqMap.put("plId", plMid);  // 平台商户ID	平台接入必填，商户接入为空
		reqMap.put("encryptKey", encryptKey); // 加密后的AES秘钥
		reqMap.put("encryptData", encryptData); // 加密后的请求/应答报文
		reqMap.put("sign", sign); // 签名
		reqMap.put("extend", ""); // 扩展域	
		String result = HttpClient.doPost(url, reqMap, connectTimeout, readTimeout);
		result = URLDecoder.decode(result, "UTF-8");
		logger.info("响应报文：\n"+result);
		/***********************响应报文验签***************************/
		Map<String, String> responseMap = SDKUtil.convertResultStringToMap(result);		
	    String retEncryptKey = (String)responseMap.get("encryptKey");
	    String retEncryptData = (String)responseMap.get("encryptData");
	    String retSign = (String)responseMap.get("sign");
	    logger.debug("retEncryptKey:[" + retEncryptKey + "]");
	    logger.debug("retEncryptData:[" + retEncryptData + "]");
	    logger.debug("retSign:[" + retSign + "]");
	    byte[] decodeBase64KeyBytes = Base64.decodeBase64(retEncryptKey.getBytes("UTF-8"));
	    byte[] merchantAESKeyBytes = CryptoUtil.RSADecrypt(decodeBase64KeyBytes, CertUtil.getPrivateKey(),2048,11,"RSA/ECB/PKCS1Padding");
	    byte[] decodeBase64DataBytes = Base64.decodeBase64(retEncryptData.getBytes("UTF-8"));
	    byte[] respDataBytes = CryptoUtil.AESDecrypt(decodeBase64DataBytes,merchantAESKeyBytes, "AES", "AES/ECB/PKCS5Padding", null);
	    String respData = new String(respDataBytes, "UTF-8");
	    logger.info("retData:[" + respData + "]");
	    byte[] signBytes = Base64.decodeBase64(retSign.getBytes("UTF-8"));
	    boolean isValid = CryptoUtil.verifyDigitalSign(respDataBytes,signBytes,CertUtil.getPublicKey(),"SHA1WithRSA");
		if(!isValid) {
			logger.error("verify sign fail.");
			return null;
		}			
		logger.info("verify sign success");	
		JSONObject respJson=JSONObject.parseObject(respData);
		if(respJson!=null) {
			logger.info("响应码：["+respJson.getString("respCode")+"]");	
			logger.info("响应描述：["+respJson.getString("respMsg")+"]");	
			logger.info("响应报文：\n"+JSONObject.toJSONString(respJson,true));	
		}else {
			logger.error("服务器请求异常！！！");	
			throw new Exception("服务器请求异常！！！");
		}			
		return respJson;
	}
	private String getRandomStringByLength(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


	
}
