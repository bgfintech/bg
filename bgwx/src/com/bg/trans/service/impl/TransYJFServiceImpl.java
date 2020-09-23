package com.bg.trans.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.soofa.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.controller.GateWayController;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.yjf.AddBankCardReqParams;
import com.bg.trans.entity.yjf.AddBankCardRespParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardReqParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardRespParams;
import com.bg.trans.entity.yjf.PayReqParams;
import com.bg.trans.entity.yjf.PayRespParams;
import com.bg.trans.entity.yjf.QueryPayReqParams;
import com.bg.trans.entity.yjf.QueryPayRespParams;
import com.bg.trans.entity.yjf.ResponseEntity;
import com.bg.trans.entity.yjf.TreatyPayReqParams;
import com.bg.trans.entity.yjf.TreatyPayRespParams;
import com.bg.trans.service.ITransYJFService;
import com.bg.util.HttpsUtil;
import com.bg.util.SetSystemProperty;
import com.bg.util.SignTools;
import com.bg.util.UtilData;
@Service("transYJFService")
public class TransYJFServiceImpl implements ITransYJFService {
	private static Logger logger = Logger.getLogger(TransYJFServiceImpl.class);
	
//	private String privateKey = SetSystemProperty.getKeyValue("pay.privatekey","yjf/yjf.properties");
//	private String payUrl = SetSystemProperty.getKeyValue("pay.payUrl","yjf/yjf.properties");
//	private String partnerId = SetSystemProperty.getKeyValue("pay.partnerId","yjf/yjf.properties");
//	private String privateKey = SetSystemProperty.getKeyValue("pay.privateKey","yjf/yjf.properties");
//	private String privateKey = "6d4108e062023f8a38bd37a58738cf3d";
	private String payUrl = SetSystemProperty.getKeyValue("pay.payUrl","yjf/yjf.properties");
	private String treatyUrl = SetSystemProperty.getKeyValue("pay.treatyUrl","yjf/yjf.properties");
//	private String payUrl = "http://merchantapi.yijifu.net/gateway.html";
//	private String partnerId = "20170821020000793831";
	private String notifyUrl = SetSystemProperty.getKeyValue("pay.notifyUrl","yjf/yjf.properties");
	private String notifyTreatyUrl = SetSystemProperty.getKeyValue("pay.notifyTreatyUrl","yjf/yjf.properties");
	@Override
	public PayRespParams pay(PayReqParams params,TransactionEntity entity) {	
		System.out.println("pay============================");
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "loan");
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "MD5");
		sortMap.put("notifyUrl", notifyUrl);
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("transAmount", params.getTransAmount());
		sortMap.put("accountName", params.getAccountName());		
		sortMap.put("certNo", params.getCertNo());
		sortMap.put("accountNo", params.getAccountNo());
		sortMap.put("accountType", params.getAccountType());	
		sortMap.put("partnerId", params.getPartnerId());
		sortMap.put("purpose", params.getPurpose());
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}  
        }
        Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String privateKey = (String)mi.get("CHANNELSECKEY");        
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1) + privateKey;
        logger.info("签名字符串为：" + singString);
        String sign = sign(singString);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        PayRespParams resp = new PayRespParams();
        System.out.println(JSON.toJSONString(params));
		try {
			resp = post(payUrl,nameValuePairs,PayRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_YJF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_AgentPay);
		entity.setReqcontent(JSON.toJSONString(params));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}
	
		
	@Override
	public String sign(String paramSrc){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] inptut = paramSrc.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(inptut);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
			    System.out.println("11111"+json);
			    logger.info("post:"+json);
			}else{
				System.out.println(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
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


	public static void main(String[] arg){
		TransYJFServiceImpl trans = new TransYJFServiceImpl();
//		PayReqParams params = new PayReqParams();
		String o = UtilData.createOnlyData();	
//		params.setOrderNo(o);
//		params.setMerchOrderNo(o);
//		params.setTransAmount("30.01");
//		params.setAccountName("李四");
//		params.setCertNo("130102198802010338");
//		params.setAccountNo("6210984910004641239");
//		params.setAccountType("PRIVATE");
//		trans.pay(params);
		QueryPayReqParams params = new QueryPayReqParams();
		params.setMerchOrderNo("20180503102923267100");
		params.setOrderNo(o);
//		trans.queryPay(params);
//		String[] a = new String[]{"a1","B2","c1","C2"};
//		TreeMap tm = new TreeMap();
//		tm.put("a1", "a1");
//		tm.put("B2", "B2");
//		tm.put("c1", "c1");
//		tm.put("C2", "C2");
//		Arrays.sort(a);
//		for(String s :a){
//			System.out.println(s);
//		}
//		Set<Map.Entry<String, Object>> entries = tm.entrySet();
//        for (Map.Entry<String, Object> entry : entries) {
//            System.out.println(entry.getKey() + "=" + entry.getValue() + "&");
//        }
		
	}

	@Override
	public AddBankCardRespParams addBankCard(AddBankCardReqParams params,TransactionEntity entity) {
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "card.add.apply.unionpay");
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "RSA");
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("signAccId", params.getSignAccId());
		sortMap.put("signName", params.getSignName());
		sortMap.put("signID", params.getSignID());
		sortMap.put("signMobile", params.getSignMobile());	
		sortMap.put("partnerId", params.getPartnerId());
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}
        }
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        logger.info("签名字符串为：" + singString);
        Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String privatekeyStr = (String)mi.get("CHANNELPRIKEY");        
        String sign = SignTools.sign(singString.getBytes(), SignTools.getPrivateKey(privatekeyStr), SignTools.SIGN_TYPE_SHA1RSA);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        System.out.println(JSON.toJSONString(sortMap));
        AddBankCardRespParams resp = null;
		try {
			resp = post(treatyUrl,nameValuePairs,AddBankCardRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("addpesp:"+ JSON.toJSONString(resp));
		entity.setChannel(PARA_CONSTATNTS.Channel_YJF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyApply);
		entity.setReqcontent(JSON.toJSONString(sortMap));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;      
	}

	@Override
	public ConfirmAddBankCardRespParams confirmAddBankCard(ConfirmAddBankCardReqParams params, TransactionEntity entity) {
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "card.add.confirm");
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "RSA");
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("signNo", params.getSignNo());
		sortMap.put("authMsg", params.getAuthMsg());
		sortMap.put("partnerId", params.getPartnerId());
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}  
        }
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        logger.info("签名字符串为：" + singString);
        Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String privatekeyStr = (String)mi.get("CHANNELPRIKEY");        
        String sign = SignTools.sign(singString.getBytes(), SignTools.getPrivateKey(privatekeyStr), SignTools.SIGN_TYPE_SHA1RSA);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        ConfirmAddBankCardRespParams resp = null;
		try {
			resp = post(treatyUrl,nameValuePairs,ConfirmAddBankCardRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_YJF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyConfirm);
		entity.setReqcontent(JSON.toJSONString(params));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public TreatyPayRespParams treatyPay(TreatyPayReqParams params,TransactionEntity entity) {
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "pay.entrustpay");
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "RSA");
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("orderDesc", params.getOrderDesc());
		sortMap.put("bizTp", "120004");
		sortMap.put("tradeAmount", params.getTradeAmount());
		sortMap.put("signNo", params.getSignNo());
		sortMap.put("notifyUrl", notifyTreatyUrl);
		sortMap.put("partnerId", params.getPartnerId());
		sortMap.put("payeeUserId", params.getPayeeUserId());
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}            
        }
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        logger.info("签名字符串为：" + singString);
        Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String privatekeyStr = (String)mi.get("CHANNELPRIKEY");        
        String sign = SignTools.sign(singString.getBytes(), SignTools.getPrivateKey(privatekeyStr), SignTools.SIGN_TYPE_SHA1RSA);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        TreatyPayRespParams resp = new TreatyPayRespParams();
		try {
			resp = post(treatyUrl,nameValuePairs,TreatyPayRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setChannel(PARA_CONSTATNTS.Channel_YJF);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyCollect);
		entity.setReqcontent(JSON.toJSONString(params));
		entity.setRespcontent(JSON.toJSONString(resp));
		entity.setIsnotice("2");
		return resp;
	}

	@Override
	public PayRespParams payQuery(PayReqParams params,String mid) {
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "loanQuery");
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "MD5");
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("partnerId", params.getPartnerId());
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}  
        }
        Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
        String privateKey = (String)mi.get("CHANNELSECKEY"); 
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1) + privateKey;
        logger.info("签名字符串为：" + singString);
        String sign = sign(singString);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        PayRespParams resp = null;
		try {
			resp = post(payUrl,nameValuePairs,PayRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}


	@Override
	public TreatyPayRespParams treatyPayQuery(TreatyPayReqParams params,String mid) {
		TreeMap<String, Object> sortMap = new TreeMap<>();
		sortMap.put("service", "order.query.entrustpay");
		sortMap.put("partnerId", params.getPartnerId());
		sortMap.put("orderNo", params.getOrderNo());
		sortMap.put("signType", "RSA");
		sortMap.put("merchOrderNo", params.getMerchOrderNo());
		sortMap.put("origOrderNo", params.getOrigOrderNo());	
		Set<Map.Entry<String, Object>> entries = sortMap.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : entries) {
        	if(entry.getValue()!=null&&!"".equals(entry.getValue())){
        		stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                nameValuePairs.add(new BasicNameValuePair((String) entry.getKey(),(String) entry.getValue()));
        	}  
        }
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        logger.info("签名字符串为：" + singString);
        Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
        String privatekeyStr = (String)mi.get("CHANNELPRIKEY");        
        String sign = SignTools.sign(singString.getBytes(), SignTools.getPrivateKey(privatekeyStr), SignTools.SIGN_TYPE_SHA1RSA);
        logger.info("生成的签名为：" + sign);
        nameValuePairs.add(new BasicNameValuePair("sign",sign));
        TreatyPayRespParams resp = null;
		try {
			resp = post(treatyUrl,nameValuePairs,TreatyPayRespParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
}
