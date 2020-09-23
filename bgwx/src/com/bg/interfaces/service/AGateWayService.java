package com.bg.interfaces.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSON;
import com.bg.util.SignTools;

public abstract class AGateWayService implements IGateWayService {
	protected static PrivateKey privateKey=SignTools.getBAPrivateKey();
	
	@Override
	public Object process2002(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1003(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1001(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1007(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1008(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1021(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public void processNotify(HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	@Override
	public String statusToRespCode(String cmd_id, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process1020(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1023(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1024(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1025(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1017(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1026(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1027(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1028(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1029(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1030(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process1031(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	@Override
	public Object process1032(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	@Override
	public Object process1100(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	@Override
	public Object process1101(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	
	@Override
	public Object process2003(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}
	protected Object errorResponse(PublicKey publicKey,String resultCode,String resultMessage){
		Map<String,String> dsm = new HashMap<String,String>();
		dsm.put("resp_code", resultCode);
		dsm.put("resp_desc", resultMessage);
		String ds = JSON.toJSONString(dsm);
		String data = Base64.encodeBase64String(SignTools.encrypt2(ds.getBytes(), publicKey));	
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", data);		
		String dsign = SignTools.sign(ds.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);        
		return rm;
	}
	
	protected String post(String url,String data,PublicKey publicKey,Logger logger) {		
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(data.getBytes(), publicKey));
		String dsign = SignTools.sign(data.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        if (dsign == null) {
			logger.error("加签失败:" + data);
			return null;
		}
		String body = null;
		try{
			System.out.println("data:"+data);
			System.out.println("edata:"+enrspdata);
			System.out.println("sign:"+dsign);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("data", enrspdata));
	        params.add(new BasicNameValuePair("sign", dsign));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			System.out.println("post url:"+url);
			if (response.getStatusLine().getStatusCode() == 200) {
				body = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info("post body:"+body);
			}else{
				logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return body;
	}

}
