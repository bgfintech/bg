package com.bg.trans.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.entity.zyht.AccessToken;
import com.bg.trans.entity.zyht.AccessTokenRsp;
import com.bg.trans.entity.zyht.BillFindByPageParams;
import com.bg.trans.entity.zyht.CancelTransParams;
import com.bg.trans.entity.zyht.CycleQueryParams;
import com.bg.trans.entity.zyht.GetDynCodeParams;
import com.bg.trans.entity.zyht.PayBackParams;
import com.bg.trans.entity.zyht.QueryTransParams;
import com.bg.trans.entity.zyht.ResponseEntity;
import com.bg.trans.entity.zyht.ScannedParams;
import com.bg.trans.service.ITransZYHTService;
import com.bg.util.SignTools;

//@Service("transZYHTService")
public class TransZYHTServiceImpl {
//public class TransZYHTServiceImpl implements ITransZYHTService {	
	private static Logger logger = Logger.getLogger(TransZYHTServiceImpl.class);
	
	private static String accessTokenUrl="http://apitest.cylcs.com/auth/applyToken";
	private static String getDynCodeUrl="http://apitest.cylcs.com/dyncode/getDynCode";
	private static String scannedUrl="http://apitest.cylcs.com/dyncode/scanned";
	private static String cancelUrl="http://apitest.cylcs.com/dyncode/cancel";
	private static String paybackUrl="http://apitest.cylcs.com/dyncode/payback";
	private static String cycleQueryUrl="http://apitest.cylcs.com/dyncode/cycleQuery";
	private static String queryUrl="http://apitest.cylcs.com/dyncode/query";
	private static String findByPage="http://apitest.cylcs.com/bill/findByPage";
	
	private static String accountId="5e18044c9c854bc28fa38adc402822be";
//
//	@Override
//	public String getAccessToken() {		
//		try{
//			long exprieDate = AccessToken.getExpireDate();
//			long now = new Date().getTime();
//			if(exprieDate>now){
//				return AccessToken.getAccessToken();
//			}		
//			String url = accessTokenUrl;
////			String corpId = SetSystemProperty.getKeyValue("zyht.corpId");
////			String secret = SetSystemProperty.getKeyValue("zyht.secret");
////			String appKey = SetSystemProperty.getKeyValue("zyht.appKey");
//			
//			String corpId = "zyht609a4c79d99ddb0a";
//			String secret = "a5b9d970c21d14dd40ae";
//			String appKey = "fb075bd57d191c15057a054b5360507c";		
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//	        params.add(new BasicNameValuePair("corpId", corpId));
//	        params.add(new BasicNameValuePair("secret", secret));
//	        params.add(new BasicNameValuePair("appKey", appKey));
//	        
//	        AccessTokenRsp rsp  = post(url,params,AccessTokenRsp.class);	        
//	        boolean success = rsp.isSuccess();
//	        if(success){
//	        	String tokenStr = rsp.getData().getAccessToken();
//	        	long expireDate = rsp.getData().getExpireDate();
//	        	AccessToken.setAccessToken(tokenStr);
//	        	AccessToken.setExpireDate(expireDate);
//	        	return tokenStr;
//	        }else{
//	        	logger.info("accessToken message:"+rsp.getMessage()+", code:"+rsp.getCode());
//	        }
//		}catch(Exception e){
//			logger.error(e.getMessage(),e);
//		}
//		return null;
//	}
//	
//	@Override
//	public GetDynCodeParams getDynCode(GetDynCodeParams para) throws Exception {		
//		String js = JSONObject.toJSONString(para);
//		ResponseEntity rsp = null;
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = Base64.encodeBase64String(SignTools.encrypt(bs, SignTools.getPrivateKey()));
////			String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = getDynCodeUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        rsp = post(url,params,ResponseEntity.class);
//        
//        GetDynCodeParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		logger.info("getDynCode return datastr:"+datastr);
//    		String tmd5 = SignTools.md5(datastr);
//    		System.out.println("tmd5:"+tmd5);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));
//    		logger.info("getDynCode return datastr:"+datastr+",signdecrypt:"+ts);
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, GetDynCodeParams.class);
//    		}
//        }
//		return rPara;
//	}
//	
//	@Override
//	public ScannedParams scanned(ScannedParams para) throws Exception{
//		String js = JSONObject.toJSONString(para);
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = scannedUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        ScannedParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		logger.info("scanned return datastr:"+datastr);
//    		String tmd5 = SignTools.md5(rspdata);
//    		System.out.println("tmd5:"+tmd5);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));
//    		logger.info("scanned return datastr:"+datastr+",signdecrypt:"+ts);
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, ScannedParams.class);
//    		}
//        }
//		return rPara;
//	}	
//
//	@Override
//	public CancelTransParams cancelTrans(CancelTransParams para)  throws Exception{
//		String js = JSONObject.toJSONString(para);
//		
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = cancelUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        CancelTransParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		String tmd5 = SignTools.md5(rspdata);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));    		
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, CancelTransParams.class);
//    		}else{
//    			logger.info("cancelTrans 验签失败 return datamd5:"+tmd5+",signdecrypt:"+ts);
//    		}
//        }
//		return rPara;
//	}
//
//	@Override
//	public PayBackParams payBack(PayBackParams para) throws Exception{
//		String js = JSONObject.toJSONString(para);
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = paybackUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        PayBackParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		String tmd5 = SignTools.md5(rspdata);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));    		
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, PayBackParams.class);
//    		}else{
//    			logger.info("payBack 验签失败 return datamd5:"+tmd5+",signdecrypt:"+ts);
//    		}
//        }
//		return rPara;
//	}
//
//	@Override
//	public CycleQueryParams cycleQuery(CycleQueryParams para) throws Exception{
//		String js = JSONObject.toJSONString(para);
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = cycleQueryUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        CycleQueryParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		String tmd5 = SignTools.md5(rspdata);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));    		
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, CycleQueryParams.class);
//    		}else{
//    			logger.info("cycleQuery 验签失败 return datamd5:"+tmd5+",signdecrypt:"+ts);
//    		}
//        }
//		return rPara;
//	}
//
//	@Override
//	public QueryTransParams queryTrans(QueryTransParams para) throws Exception{
//		String js = JSONObject.toJSONString(para);
//	
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = queryUrl;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        QueryTransParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		String tmd5 = SignTools.md5(rspdata);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));    		
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, QueryTransParams.class);
//    		}else{
//    			logger.info("queryTrans 验签失败 return datamd5:"+tmd5+",signdecrypt:"+ts);
//    		}
//        }
//
//		return rPara;
//		
//	}
//	
//	private <T> T post(String url,List<NameValuePair> params,Class<T> classz) throws Exception{
//		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpPost post = new HttpPost(url);
//		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
//		post.setEntity(entity);
//		CloseableHttpResponse response = httpclient.execute(post);
//		String json = null;
//		if (response.getStatusLine().getStatusCode() == 200) {
//		    json = EntityUtils.toString(response.getEntity(), "utf-8");
//		    logger.info(json);
//		}else{
//			logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
//		}
//		return JSONObject.parseObject(json, classz);
//	}
//
//	@Override
//	public BillFindByPageParams findByPage(BillFindByPageParams para) throws Exception {
//		String js = JSONObject.toJSONString(para);
//		
//		byte[] bd = SignTools.encrypt(js.getBytes(), SignTools.getPublicKey());
//		String data =Base64.encodeBase64String(bd);
//		byte[] bs = SignTools.md5(js).getBytes();
//		String sign = SignTools.sign(bs, SignTools.getPrivateKey(), SignTools.SIGN_TYPE_SHA1RSA);
//		String token = getAccessToken();
//		String url = findByPage;
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("data", data));
//        params.add(new BasicNameValuePair("sign", sign));
//        params.add(new BasicNameValuePair("accessToken", token));
//        ResponseEntity rsp = post(url,params,ResponseEntity.class);        
//        BillFindByPageParams rPara = null; 
//        if("true".equalsIgnoreCase(rsp.getSuccess())){
//        	String rspdata = rsp.getData();        	
//    		byte[] rspbd = Base64.decodeBase64(rspdata);
//    		String rspsign = rsp.getSign();
//    		String datastr = new String(SignTools.decrypt(rspbd, SignTools.getPrivateKey()));
//    		String tmd5 = SignTools.md5(rspdata);
//    		byte[] rspbs = Base64.decodeBase64(rspsign);
//    		String ts = new String(SignTools.decrypt(rspbs, SignTools.getPublicKey()));    		
//    		if(tmd5.equals(ts)){
//    			rPara = JSONObject.parseObject(datastr, BillFindByPageParams.class);
//    		}else{
//    			logger.info("findByPage 验签失败 return datamd5:"+tmd5+",signdecrypt:"+ts);
//    		}
//        }
//
//		return rPara;
//	}
//
//	public static void main(String[] arg){
//		String json = null;
//		ITransZYHTService service = new TransZYHTServiceImpl();
//		
//		
//		Object rsp=null;
//		try {
////			BillFindByPageParams para = new BillFindByPageParams();
////			para.setAccountId(accountId);
////			rsp = service.findByPage(para);
//			GetDynCodeParams para = new GetDynCodeParams();
//			para.setTransCode("6004");
//			para.setAccountId("5e18044c9c854bc28fa38adc402822be");
//			para.setTransTime("091811");
//			para.setTransDate("20171123");
//			para.setTranAmt("0.01");
//			para.setCurrency("CNY");
//			para.setAgentOrderNo("201711220001");
//			para.setChannelFlag("A");
//			rsp = service.getDynCode(para);		
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 		
//		json = JSONObject.toJSONString(rsp);
//		System.out.println(json);
//	}

}
