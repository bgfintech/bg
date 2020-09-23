package com.bg.trans.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.hftx.eloan.Trans101Req;
import com.bg.trans.entity.hftx.eloan.Trans101Rsp;
import com.bg.trans.entity.hftx.eloan.Trans102Req;
import com.bg.trans.entity.hftx.eloan.Trans102Rsp;
import com.bg.trans.entity.hftx.eloan.Trans103Req;
import com.bg.trans.entity.hftx.eloan.Trans103Rsp;
import com.bg.trans.entity.hftx.eloan.Trans201Req;
import com.bg.trans.entity.hftx.eloan.Trans201Rsp;
import com.bg.trans.entity.hftx.eloan.Trans202Req;
import com.bg.trans.entity.hftx.eloan.Trans202Rsp;
import com.bg.trans.entity.hftx.eloan.Trans204Req;
import com.bg.trans.entity.hftx.eloan.Trans204Rsp;
import com.bg.trans.entity.hftx.eloan.Trans301Req;
import com.bg.trans.entity.hftx.eloan.Trans301Rsp;
import com.bg.trans.entity.hftx.eloan.Trans302Req;
import com.bg.trans.entity.hftx.eloan.Trans302Rsp;
import com.bg.trans.entity.hftx.eloan.Trans303Req;
import com.bg.trans.entity.hftx.eloan.Trans303Rsp;
import com.bg.trans.entity.hftx.eloan.Trans304Req;
import com.bg.trans.entity.hftx.eloan.Trans304Rsp;
import com.bg.trans.entity.hftx.eloan.Trans305Req;
import com.bg.trans.entity.hftx.eloan.Trans305Rsp;
import com.bg.trans.entity.hftx.eloan.Trans306Req;
import com.bg.trans.entity.hftx.eloan.Trans306Rsp;
import com.bg.trans.entity.hftx.eloan.Trans307Req;
import com.bg.trans.entity.hftx.eloan.Trans307Rsp;
import com.bg.trans.entity.hftx.eloan.Trans308Req;
import com.bg.trans.entity.hftx.eloan.Trans308Rsp;
import com.bg.trans.service.ITransHFTXEloanService;
import com.bg.util.HttpsUtil;
import com.bg.util.SetSystemProperty;

@Service("transHFTXEloanService")
public class TransHFTXEloanServiceImpl implements ITransHFTXEloanService {
	private static Logger logger = Logger.getLogger(TransHFTXEloanServiceImpl.class);
	@Autowired
	private ITransDao transDao;
	private String httpUrl = SetSystemProperty.getKeyValue("pay.httpUrl","hftx/eloan.properties");
	private String returnUrl = 	SetSystemProperty.getKeyValue("pay.returnUrl","hftx/eloan.properties");
	private static Map<String,String> banknoMap=null;
	@PostConstruct
	public void init() throws Exception {
		if(banknoMap==null){
			List<Map<String,Object>> list = transDao.findBanknoByChannel(PARA_CONSTATNTS.Channel_HFTX);
			banknoMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String bankno = (String)m.get("BANKNO");
				String dbankno = (String)m.get("DBANKNO");
				banknoMap.put(bankno, dbankno);
			}
		}
	}
	
	@Override
	public Trans101Rsp trans101(Trans101Req req,String appkey,TransactionEntity entity) {
		String url = httpUrl+"/v1/users/add-cash-card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("user_name", req.getUser_name());
		sortMap.put("card_no", req.getCard_no());
		sortMap.put("card_type", req.getCard_type());
		String bank_id = banknoMap.get(req.getBank_id());
		sortMap.put("bank_id", bank_id==null?req.getBank_id():bank_id);
		sortMap.put("bank_mobile", req.getBank_mobile());
		sortMap.put("bg_return_url", returnUrl);
		if(req.getBank_province_id()!=null&&!"".equals(req.getBank_province_id())){
			sortMap.put("bank_province_id", req.getBank_province_id());
		}
		if(req.getBank_area_id()!=null&&!"".equals(req.getBank_area_id())){
			sortMap.put("bank_area_id", req.getBank_area_id());
		}
		System.out.println("101req:"+JSONObject.toJSONString(sortMap));
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        		nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String)entry.getValue()));
//        		try {
//					String encodeParam = URLEncoder.encode((String)entry.getValue(),"UTF-8");
//					nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),encodeParam));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//                
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        System.out.println("HFTX signString:"+signString);
//        String pss = "";
//        try {
//        	pss= URLEncoder.encode(signString,"UTF-8");
//			System.out.println("encode:"+pss);
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		String sign = sign(signString,appkey);	
		System.out.println("HFTX sign:"+sign);
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans101Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans101Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_BindCaseCard);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}
	
	@Override
	public Trans201Rsp trans201(Trans201Req req, String appkey, TransactionEntity entity) {
		String url = httpUrl+"/v1/trade/loan";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		sortMap.put("business_type", req.getBusiness_type());
		sortMap.put("product_type", req.getProduct_type());
		sortMap.put("loan_purpose", req.getLoan_purpose());
		sortMap.put("loan_start_date", req.getLoan_start_date());
		sortMap.put("loan_end_date", req.getLoan_end_date());
		sortMap.put("loan_period", req.getLoan_period());
		sortMap.put("loan_period_type", req.getLoan_period_type());
		//sortMap.put("contract_no", req.getContract_no());
		sortMap.put("loan_amount", req.getLoan_amount());
		sortMap.put("entrusted_flag", req.getEntrusted_flag());
		sortMap.put("user_cert_type", req.getUser_cert_type());
		sortMap.put("user_cert_id", req.getUser_cert_id());
		sortMap.put("user_name", req.getUser_name());
		sortMap.put("user_card_no", req.getUser_card_no());
		sortMap.put("user_amount", req.getUser_amount());
		sortMap.put("user_cash_method", req.getUser_cash_method());
		if(req.getMerchant_cert_type()!=null&&!"".equals(req.getMerchant_cert_type())){
			sortMap.put("merchant_cert_type", req.getMerchant_cert_type());
		}
		if(req.getMerchant_cert_id()!=null&&!"".equals(req.getMerchant_cert_id())){
			sortMap.put("merchant_cert_id", req.getMerchant_cert_id());
		}
		if(req.getMerchant_amount()!=null&&!"".equals(req.getMerchant_amount())){
			sortMap.put("merchant_amount", req.getMerchant_amount());
		}
			
		sortMap.put("installment_number", req.getInstallment_number());
		sortMap.put("installment_rate", req.getInstallment_rate());
		sortMap.put("payment_method", req.getPayment_method());
//		sortMap.put("loan_comment", req.getLoan_comment());
//		sortMap.put("auditor", req.getAuditor());
//		sortMap.put("audit_time", req.getAudit_time());
		sortMap.put("bg_return_url", returnUrl);		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans201Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans201Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_Loan);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}
	
	
	
	@Override
	public String sign(String plaintext, String appKey) {
		SecretKeySpec signingKey = new SecretKeySpec(appKey.getBytes(),"HmacSHA256");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			byte[] bt = mac.doFinal(plaintext.getBytes());
			return byte2hex(bt);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}

	private String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				hs.append('0');
			}
			hs.append(stmp);
		}
		return hs.toString();
	}
	
	private <T> T post(String url,List<NameValuePair> params,Class<T> classz){
		String json = null;
		CloseableHttpClient httpclient = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
//			httpclient = HttpClients.createDefault();
			httpclient = HttpsUtil.getHttpClient();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);			
			if (response.getStatusLine().getStatusCode() == 200) {
			    json = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info("post:"+json);
			}else{
				System.out.println(JSONObject.toJSONString(response));
				logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
			}
			System.out.println("json:::::::::::::"+json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(httpclient!=null){
					httpclient.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return JSONObject.parseObject(json, classz);
	}

	private <T> T get(String url,List<NameValuePair> params,Class<T> classz){
		String json = null;
		CloseableHttpClient httpclient = null;
		try {
//			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
//			httpclient = HttpClients.createDefault();
			httpclient = HttpsUtil.getHttpClient();
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
			String getUrl = url + "?" + paramsStr;
			HttpGet get = new HttpGet(getUrl);
			get.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	       
			CloseableHttpResponse response = httpclient.execute(get);			
			if (response.getStatusLine().getStatusCode() == 200) {
			    json = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info("post:"+json);
			}else{
				System.out.println(JSONObject.toJSONString(response));
//				logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
			}
			System.out.println("json:::::::::::::"+json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(httpclient!=null){
					httpclient.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return JSONObject.parseObject(json, classz);
	}
	
	@Override
	public Trans102Rsp trans102(Trans102Req req, String appkey,TransactionEntity entity) {
		String url = httpUrl+"/v1/users/add-withhold-card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("user_name", req.getUser_name());
		sortMap.put("card_no", req.getCard_no());
		sortMap.put("card_type", req.getCard_type());
		String bank_id = banknoMap.get(req.getBank_id());
		sortMap.put("bank_id", bank_id==null?req.getBank_id():bank_id);
		sortMap.put("bank_mobile", req.getBank_mobile());
		sortMap.put("bind_trans_id", req.getBind_trans_id());
		if(entity.getTrantype().equals(PARA_CONSTATNTS.TransCode_TreatyApply)){
			sortMap.put("step_flag", "01");
		}else if(entity.getTrantype().equals(PARA_CONSTATNTS.TransCode_TreatyConfirm)){
			sortMap.put("step_flag", "02");
			sortMap.put("sms_code", req.getSms_code());
		}	
		sortMap.put("bg_return_url", returnUrl);
		if(req.getBank_province_id()!=null&&!"".equals(req.getBank_province_id())){
			sortMap.put("bank_province_id", req.getBank_province_id());
		}
		if(req.getBank_area_id()!=null&&!"".equals(req.getBank_area_id())){
			sortMap.put("bank_area_id", req.getBank_area_id());
		}
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));	
		System.out.println(JSON.toJSONString(sortMap));
		Trans102Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans102Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public Trans202Rsp trans202(Trans202Req req, String appkey,TransactionEntity entity) {
		String url = httpUrl+"/v1/trade/repay";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		sortMap.put("business_type", req.getBusiness_type());
		sortMap.put("product_type", req.getProduct_type());
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("user_name", req.getUser_name());
		sortMap.put("card_no", req.getCard_no());
		sortMap.put("repay_mode", req.getRepay_mode());
		sortMap.put("trans_amt", req.getTrans_amt());
		sortMap.put("loan_request_seq", req.getLoan_request_seq());
		sortMap.put("back_cert_type", req.getBack_cert_type());
		sortMap.put("back_cert_id", req.getBack_cer_id());
		if(req.getBack_div_details()!=null&&!"".equals(req.getBack_div_details())){
			sortMap.put("back_div_details", req.getBack_div_details());
		}		
		sortMap.put("bg_return_url", returnUrl);		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans202Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans202Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_Repay);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public Trans204Rsp trans204(Trans204Req req, String appkey,TransactionEntity entity) {
		String url = httpUrl+"/v1/trade/loan/cash";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		sortMap.put("loan_request_seq", req.getLoan_request_seq());
		sortMap.put("card_no", req.getCard_no());
		sortMap.put("bg_return_url", returnUrl);		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans204Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans204Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_LoanCash);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public Trans308Rsp trans308(Trans308Req req, String appkey) {
		String url = httpUrl+"/v1/query/clients/balance";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans308Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans308Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans103Rsp trans103(Trans103Req req, String appkey,TransactionEntity entity) {
		String url = httpUrl+"/v1/users/relieve-withhold-card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("user_name", req.getUser_name());
		sortMap.put("card_no", req.getCard_no());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans103Rsp resp = null;
		try {
			resp = post(url,nameValuePairs,Trans103Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTXELOAN);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_UNBindCard);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public Trans301Rsp trans301(Trans301Req req, String appkey) {
		String url = httpUrl+"/v1/users/query/add-cash-card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());

		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("card_no", req.getCard_no());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));	
		System.out.println(JSON.toJSONString(sortMap));
		Trans301Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans301Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans302Rsp trans302(Trans302Req req, String appkey) {
		String url = httpUrl+"/v1/query/trade/loan";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("loan_request_seq", req.getLoan_request_seq());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans302Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans302Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans303Rsp trans303(Trans303Req req, String appkey) {
		String url = httpUrl+"/v1/query/users/add-withhold-card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		sortMap.put("card_no", req.getCard_no());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans303Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans303Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans304Rsp trans304(Trans304Req req, String appkey) {
		String url = httpUrl+"/v1/query/trade/repay";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("repay_request_seq", req.getRepay_request_seq());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans304Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans304Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans307Rsp trans307(Trans307Req req, String appkey) {
		String url = httpUrl+"/v1/query/loan/cash";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("cash_request_seq", req.getCash_request_seq());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans307Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans307Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans305Rsp trans305(Trans305Req req, String appkey) {
		String url = httpUrl+"/v1/query/card/bin";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("card_no", req.getCard_no());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans305Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans305Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public Trans306Rsp trans306(Trans306Req req, String appkey) {
		String url = httpUrl+"/v1/query/bind/card";
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("app_token", req.getApp_token());
		sortMap.put("client_name", req.getClient_name());
		sortMap.put("company_name", req.getCompany_name());
		sortMap.put("request_seq", req.getRequest_seq());
		sortMap.put("request_date", req.getRequest_date());
		
		sortMap.put("cert_type", req.getCert_type());
		sortMap.put("cert_id", req.getCert_id());
		
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("GET"+url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String signString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
		String sign = sign(signString,appkey);		
		nameValuePairs.add(new BasicNameValuePair("signature",sign));		
		Trans306Rsp resp = null;
		try {
			resp = get(url,nameValuePairs,Trans306Rsp.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	
}
