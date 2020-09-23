package com.bg.interfaces.service.impl;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.entity.Trans1100Req;
import com.bg.interfaces.entity.Trans1100Rsp;
import com.bg.interfaces.service.AGateWayService;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.gyzj.CountDetail;
import com.bg.trans.entity.gyzj.EntstatEntity;
import com.bg.trans.entity.gyzj.StatusData;
import com.bg.trans.entity.gyzj.Type;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransGYZJService;
import com.bg.util.SignTools;
import com.bg.util.UtilData;
@Service("gateWayServiceByGYZJ")
public class GateWayServiceByGYZJImpl extends AGateWayService {
	private static Logger logger = Logger.getLogger(GateWayServiceByGYZJImpl.class);
	
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransGYZJService transGYZJService;
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	@Override
	public Object process1100(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1100Req req1100 = JSONObject.parseObject(data, Trans1100Req.class);
		String orderId = req1100.getOrder_id();
		String mer_id = req1100.getMer_id();
		String cmd_id = req1100.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}
		Set<String> mm = orderMap.get(mid);
		if(mm==null){
			mm = new HashSet<String>();
			orderMap.put(mid, mm);
		}
		boolean ishas = mm.contains(orderId);
		if(ishas==true){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		mm.add(orderId);
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te!=null){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		String oid = UtilData.createOnlyData();		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setName(req1100.getName());
		entity.setIdcard(req1100.getIdcard());
		entity.setTranstime(req1100.getOrder_date());
		entity.setMerchantid(cmid);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_CourtDataCount);
		String type = req1100.getType();
		String name = req1100.getName();
		String idcard = req1100.getIdcard();
		String account = req1100.getAccount();
		EntstatEntity rsp = null;
		if("1".equals(type)){		
			rsp = transGYZJService.getEntstatPeople(name, idcard);
		}else{
			rsp = transGYZJService.getEntstatOrg(name);
		}
		StatusData status = rsp.getStatus();
		String resp_code = "";
		String resp_desc = "";
		String trans_status = "";
		boolean isSuccess = false;
		if(status!=null){		
			int response_code = status.getError_code();
			if(response_code==0||response_code==-5||response_code==-7){
				resp_code="110000";
				trans_status="01";
				consume(account,"1");
				isSuccess = true;
			}else{
				resp_code="110002";
				trans_status="02";
			}			
			resp_desc = status.getError_message();
		}else{
			resp_code="110000";
			trans_status="01";
			consume(account,"1");
			isSuccess = true;
		}		
		Trans1100Rsp rsp1100 = new Trans1100Rsp();
		rsp1100.setResp_code(resp_code);
		rsp1100.setResp_desc(resp_desc);
		rsp1100.setMer_id(mid);
		rsp1100.setOrder_id(orderId);	
		if(isSuccess){
			Type civil = rsp.getData().getCivil();
			if(civil!=null){
				CountDetail cd = civil.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transform(cd);
				rsp1100.setCivil(cdr);
			}
			Type implement = rsp.getData().getImplement();
			if(implement!=null){
				CountDetail cd = implement.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transform(cd);
				rsp1100.setImplement(cdr);
			}
			Type criminal = rsp.getData().getCriminal();
			if(criminal!=null){
				CountDetail cd = criminal.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transform(cd);
				rsp1100.setCriminal(cdr);
			}
			Type administrative = rsp.getData().getAdministrative();
			if(administrative!=null){
				CountDetail cd = administrative.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transform(cd);
				rsp1100.setAdministrative(cdr);
			}
			Type bankrupt = rsp.getData().getBankrupt();
			if(bankrupt!=null){
				CountDetail cd = bankrupt.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transform(cd);
				rsp1100.setBankrupt(cdr);
			}
		}		
		String rspdata = JSONObject.toJSONString(rsp1100);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
        entity.setStatus(trans_status);
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata.substring(0,4999));
		transBaseService.insertTransaction(entity);
		mm.remove(orderId);
		return rm;
	}
	
	@Override
	public Object process1101(String mid, String cmid, String data,PublicKey publicKey) {
		
		
		return null;
	}
	
	private void consume(String account,String type){
		
		//TODO type=1 统计模板 2为明细模板  10元 30元 根据用户情况打折
	}
	
	private com.bg.interfaces.entity.internal.CountDetail transform(CountDetail cd){
		com.bg.interfaces.entity.internal.CountDetail cdr = new com.bg.interfaces.entity.internal.CountDetail();
		cdr.setCount_yuangao(cd.getCount_yuangao()==null?null:cd.getCount_yuangao().toString());
		cdr.setCount_wei_yuangao(cd.getCount_wei_yuangao()==null?null:cd.getCount_wei_yuangao().toString());
		cdr.setCount_beigao(cd.getCount_beigao()==null?null:cd.getCount_beigao().toString());
		cdr.setCount_wei_beigao(cd.getCount_wei_beigao()==null?null:cd.getCount_wei_beigao().toString());
		cdr.setCount_other(cd.getCount_other()==null?null:cd.getCount_other().toString());
		cdr.setCount_wei_other(cd.getCount_wei_other()==null?null:cd.getCount_wei_other().toString());
		cdr.setMoney_yuangao(cd.getMoney_yuangao()==null?null:cd.getMoney_yuangao().toString());
		cdr.setMoney_wei_yuangao(cd.getMoney_wei_yuangao()==null?null:cd.getMoney_wei_yuangao().toString());
		cdr.setMoney_beigao(cd.getMoney_beigao()==null?null:cd.getMoney_beigao().toString());
		cdr.setMoney_wei_beigao(cd.getMoney_wei_beigao()==null?null:cd.getMoney_wei_beigao().toString());
		cdr.setMoney_other(cd.getMoney_other()==null?null:cd.getMoney_other().toString());
		cdr.setMoney_wei_other(cd.getMoney_wei_other()==null?null:cd.getMoney_wei_other().toString());
		cdr.setArea_stat(cd.getArea_stat());
		cdr.setAy_stat(cd.getAy_stat());
		cdr.setLarq_stat(cd.getLarq_stat());
		cdr.setJafs_stat(cd.getJafs_stat());	
		return cdr;
	}
	
	public static void main(String[] arg){
		try{
//			String url="http://test.widefinance.cn/bp/expensesrecord";
//			String url="http://test.widefinance.cn/bp/cachelist";
//			String url="http://test.widefinance.cn/bp/entstat";
			String url="http://test.widefinance.cn/bp/entout";
			String t = "20191024101921";
			String key = "123456";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", "测试自然人B"));
	        params.add(new BasicNameValuePair("account", "13811113333"));
	        params.add(new BasicNameValuePair("idcard", "123456200201011234"));
	        params.add(new BasicNameValuePair("type", "1"));
	        params.add(new BasicNameValuePair("t", t));
	        String s = SignTools.md5("13811113333"+t+key);
	        System.out.println(s);
	        params.add(new BasicNameValuePair("s", s));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			System.out.println("post url:"+url);
			if (response.getStatusLine().getStatusCode() == 200) {
				String body = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info("post body:"+body);
			}else{
				logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
	}
}
