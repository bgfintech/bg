package com.bg.trans.service.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransYBZFService;
import com.bg.util.SetSystemProperty;
import com.yeepay.sqkkseperator.config.Config;
import com.yeepay.sqkkseperator.service.YeepayService;

//@Service("transYBZFService")
public class TransYBZFServiceImpl implements ITransYBZFService {
	private static Logger logger = Logger.getLogger(TransYBZFServiceImpl.class);
	private final String merId = Config.getInstance().getValue("merchantno");
	private final String callbackUrl = SetSystemProperty.getKeyValue("pay.callbackUrl","ybzf/ybzf.properties");
	//鉴权协议绑卡申请请求地址
	private final String Url_1023 = Config.getInstance().getValue("authbindcardreqUri");
	//鉴权协议绑卡确认请求地址
	private final String Url_1024 = Config.getInstance().getValue("authbindcardconfirmUri");
	//协议支付请求地址
	private final String Url_1025 = Config.getInstance().getValue("unibindcardpayUri");
	
	@Autowired
	private ITransDao transDao;
	private static Map<String,String> banknoMap=null;
	@PostConstruct
	public void init() throws Exception {
		if(banknoMap==null){
			List<Map<String,Object>> list = transDao.findBanknoByChannel(PARA_CONSTATNTS.Channel_YBZF);
			banknoMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String bankno = (String)m.get("BANKNO");
				String dbankno = (String)m.get("DBANKNO");
				banknoMap.put(bankno, dbankno);
			}
		}
	}

	
	@Override
	public Map<String,Object> transferSingle(Map<String,Object> params,TransactionEntity entity) {
		String customerNumber = Config.getInstance().getValue("customerNumber");
		String groupNumber = Config.getInstance().getValue("groupNumber");
		params.put("customerNumber", customerNumber);
		params.put("groupNumber", groupNumber);
		String bankCode = (String)params.get("bankCode");		
		params.put("bankCode", banknoMap.get(bankCode));		
//		params.put("batchNo", batchNo);
//		params.put("orderId", orderId);
//		params.put("amount", amount);
//		params.put("product", product);
//		params.put("urgency", urgency);
//		params.put("accountName", accountName);
//		params.put("accountNumber", accountNumber);
//		params.put("bankCode", bankCode);
//		params.put("bankName", bankName);
//		params.put("bankBranchName", bankBranchName);
//		params.put("provinceCode", provinceCode);
//		params.put("cityCode", cityCode);
//		params.put("feeType", feeType);
//		params.put("desc", desc);
//		params.put("leaveWord", leaveWord);
//		params.put("abstractInfo", abstractInfo);

		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); String
		 * merchantNo = format(Config.getInstance().getValue("merchantNo"));
		 * String parentMerchantNo =
		 * format(Config.getInstance().getValue("parentMerchantNo")); String
		 * privateKey = Config.getInstance().getValue("privatekey"); YopRequest
		 * yoprequest = new YopRequest("OPR:" + merchantNo, privateKey,
		 * Config.getInstance().getValue("baseURL"));
		 */
		String uri = com.yeepay.payment.YeepayService.getUrl(com.yeepay.payment.YeepayService.PAYMENT_URL);
		Map<String, Object> rspMap = new HashMap<String,Object>();
		try {
			rspMap = com.yeepay.payment.YeepayService.yeepayYOP(params, uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setMerchantid(merId);
		entity.setChannel(PARA_CONSTATNTS.Channel_YBZF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_AgentPay);
		entity.setReqcontent(JSON.toJSONString(params));
		entity.setRespcontent(JSON.toJSONString(rspMap));
		entity.setIsnotice("2");
		return rspMap;
	}
	
	@Override
	public Map<String, String> authBindCardRequest(Map<String, String> map,TransactionEntity entity) {
		map.put("merchantno", merId);
//		map.put("requestno", requestno);
//		map.put("identityid", identityid);
		map.put("identitytype", "AGREEMENT_NO");
//		map.put("cardno", cardno);
//		map.put("idcardno", idcardno);
		map.put("idcardtype", "ID");
//		map.put("username", username);
//		map.put("phone", phone);
//		map.put("issms", issms);
//		map.put("advicesmstype", "MESSAGE");
//		map.put("smstemplateid", "");
//		map.put("smstempldatemsg", "");
//		map.put("avaliabletime", "");
//		map.put("callbackurl", "");
//		map.put("requesttime", requesttime);
		map.put("authtype", "COMMON_FOUR");
//		map.put("remark", remark);
//		map.put("extinfos", extinfos);	   
		Map<String, String> rspMap=null;
		try {
			rspMap = YeepayService.yeepayYOP(map,Url_1023);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setMerchantid(merId);
		entity.setChannel(PARA_CONSTATNTS.Channel_YBZF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyApply);
		entity.setReqcontent(JSON.toJSONString(map));
		entity.setRespcontent(JSON.toJSONString(rspMap));
		entity.setIsnotice("2");		
		return rspMap;
	}
	@Override
	public Map<String, String> authBindCardConfirm(Map<String, String> map,TransactionEntity entity) {
		map.put("merchantno", merId);
		Map<String, String> rspMap=null;
		try {
			rspMap = YeepayService.yeepayYOP(map,Url_1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setMerchantid(merId);
		entity.setChannel(PARA_CONSTATNTS.Channel_YBZF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyConfirm);
		entity.setReqcontent(JSON.toJSONString(map));
		entity.setRespcontent(JSON.toJSONString(rspMap));
		entity.setIsnotice("2");		
		return rspMap;
	}
	@Override
	public Map<String, String> treatyPay(Map<String, String> map,TransactionEntity entity) {
		map.put("merchantno", merId);
//		map.put("requestno", requestno);
		map.put("issms", "false");
//		map.put("identityid", identityid);
		map.put("identitytype", "AGREEMENT_NO");
//		map.put("cardtop", cardtop);
//		map.put("cardlast", cardlast);
//		map.put("amount", amount);
//		map.put("advicesmstype", advicesmstype);
//		map.put("avaliabletime", avaliabletime);
//		map.put("productname", productname);
		map.put("callbackurl", callbackUrl);
//		map.put("requesttime", requesttime);
		map.put("terminalno", "SQKKSCENEKJ010");
//		map.put("remark", remark);
//		map.put("extinfos", extinfos);
//		map.put("dividecallbackurl", dividecallbackurl);
//		map.put("dividejstr", dividejstr);		
		Map<String, String> rspMap=new HashMap<String,String>();
		try {
			rspMap = YeepayService.yeepayYOP(map,Url_1025);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setMerchantid(merId);
		entity.setChannel(PARA_CONSTATNTS.Channel_YBZF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyCollect);
		entity.setReqcontent(JSON.toJSONString(map));
		entity.setRespcontent(JSON.toJSONString(rspMap));
		entity.setIsnotice("2");		
		return rspMap;
	}
	


	@Override
	public Map<String, Object> paymentQuery(Map<String, Object> params) {
		params.put("customerNumber",Config.getInstance().getValue("customerNumber"));
		params.put("pageNo", "1");
		params.put("pageSize", "1");
		String uri = com.yeepay.payment.YeepayService.getUrl(com.yeepay.payment.YeepayService.PAYMENTQUERY_URL);
		Map<String, Object> yopresponsemap=null;
		try {
			yopresponsemap = com.yeepay.payment.YeepayService.yeepayYOP(params,uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return yopresponsemap;
	}


	@Override
	public Map<String, String> treatPayQuery(Map<String, String> map) {
		String merchantno = Config.getInstance().getValue("merchantno");
		map.put("merchantno", merchantno);
//		map.put("requestno", requestno);
//		map.put("yborderid", yborderid);
		String uri = Config.getInstance().getValue("bindcardpayqueryUri");
		Map<String, String> yopresponsemap=null;
		try {
			yopresponsemap = YeepayService.yeepayYOP(map,uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return yopresponsemap;
	}
	
	
	
	
}
