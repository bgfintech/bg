package com.bg.interfaces.service.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.controller.GateWayController;
import com.bg.interfaces.entity.Trans1003Req;
import com.bg.interfaces.entity.Trans1017Req;
import com.bg.interfaces.entity.Trans1017Rsp;
import com.bg.interfaces.entity.Trans1023Req;
import com.bg.interfaces.entity.Trans1023Rsp;
import com.bg.interfaces.entity.Trans1024Req;
import com.bg.interfaces.entity.Trans1024Rsp;
import com.bg.interfaces.entity.Trans1025Req;
import com.bg.interfaces.entity.Trans1025Rsp;
import com.bg.interfaces.service.AGateWayService;
import com.bg.interfaces.service.IGateWayService;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.yjf.AddBankCardReqParams;
import com.bg.trans.entity.yjf.AddBankCardRespParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardReqParams;
import com.bg.trans.entity.yjf.ConfirmAddBankCardRespParams;
import com.bg.trans.entity.yjf.PayReqParams;
import com.bg.trans.entity.yjf.PayRespParams;
import com.bg.trans.entity.yjf.ResponseEntity;
import com.bg.trans.entity.yjf.TreatyPayReqParams;
import com.bg.trans.entity.yjf.TreatyPayRespParams;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransYJFService;
import com.bg.util.SignTools;
import com.bg.util.UtilData;


@Service("gateWayServiceByYJF")
public class GateWayServiceByYJFImpl extends AGateWayService {
	private static Logger logger = Logger.getLogger(GateWayServiceByYJFImpl.class);
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransYJFService transYJFService;
	
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	

	@Override
	public Object process1003(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1003Req req1003 = JSONObject.parseObject(data, Trans1003Req.class);
		String reqno = req1003.getOrder_id();
		String mer_id = req1003.getMer_id();
		String trans_type = req1003.getTrans_type();
		logger.info("queryOrder======================reqno:"+reqno+" mer_id:"+mer_id);
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}	
		TransactionEntity te = transBaseService.queryOrderByReqNo(reqno, mid);
		if(te==null){
			return errorResponse(publicKey,"999999","无该订单号");
		}
		Map<String,String> rm = new HashMap<String,String>();
		switch (trans_type) {
		case "1017":
			rm = query1017(te,publicKey);
			break;
		case "1025":
			rm = query1025(te,publicKey);
			break;
		default:
			return errorResponse(publicKey,"999999","查询原交易类型不正确:"+trans_type);
		}
		return rm;
	}

	private Map<String, String> query1025(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String cmid = te.getMerchantid();
		String reqno = te.getReqno();
		String orderno = te.getOrderno();	
		String oid = UtilData.createOnlyData();
		TreatyPayReqParams params = new TreatyPayReqParams();
		params.setOrderNo(oid);
		params.setOrigOrderNo(orderno);
		params.setPartnerId(cmid);
		TreatyPayRespParams rspDTO = transYJFService.treatyPayQuery(params,te.getSecmerchantid());		
		String resp_desc =rspDTO.getResultMessage();
		String tradeStatus = rspDTO.getTradeStatus();
		String resultCode = rspDTO.getResultCode();
		String success = rspDTO.getSuccess();
		String resp_code = "";
		String trans_status="";
		if("PAY_PROCESSING".equals(tradeStatus)||"WAIT_PAY".equals(tradeStatus)){
			resp_code="102501";
			trans_status="03";
		}else if("FINISHED".equals(tradeStatus)||"SUCCESS".equals(tradeStatus)){
			resp_code="102500";
			trans_status="01";
		}else if("CLOSED".equals(tradeStatus)){
			resp_code="102502";
			trans_status="02";
		}else if("EXECUTE_FAIL".equals(resultCode)||"false".equals(success)){
			resp_code="100302";
		}
		Trans1025Rsp rsp1025 = new Trans1025Rsp();
		rsp1025.setResp_code(resp_code);
		rsp1025.setResp_desc(resp_desc);
		rsp1025.setMer_id(mid);
		rsp1025.setOrder_id(reqno);	
		rsp1025.setOut_trans_id(orderno);
		String rspdata = JSONObject.toJSONString(rsp1025);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
		return rm;
	}

	private Map<String, String> query1017(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String cmid = te.getMerchantid();
		String reqno = te.getReqno();
		String orderno = te.getOrderno();	
		String oid = UtilData.createOnlyData();
		PayReqParams params = new PayReqParams();
		params.setOrderNo(oid);
		params.setMerchOrderNo(orderno);		
		params.setPartnerId(cmid);
		PayRespParams rspDTO = transYJFService.payQuery(params,te.getSecmerchantid());		
		String serviceStatus = rspDTO.getServiceStatus();
		String resp_desc =rspDTO.getResultMessage();
		String resp_code = "101701";
		String trans_status="03";
		String success = rspDTO.getSuccess();
		if("REMITTANCE_FAIL".equals(serviceStatus)){
			resp_code="101702";
			trans_status="02";
		}else if("REMITTANCE_SUCCESS".equals(serviceStatus)){
			resp_code="101700";
			trans_status="01";
		}else if("false".equals(success)){
			resp_code="101702";
			trans_status="02";
		}
		Trans1017Rsp rsp1017 = new Trans1017Rsp();
		rsp1017.setResp_code(resp_code);
		rsp1017.setResp_desc(resp_desc);
		rsp1017.setMer_id(mid);
		rsp1017.setOrder_id(reqno);	
		rsp1017.setOut_trans_id(orderno);
		String rspdata = JSONObject.toJSONString(rsp1017);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
		return rm;
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
		String cmd = (String)request.getAttribute("cmd");
		logger.info("yjf notify:=============");
		if ("1017".equals(cmd)) {
			processNotify1017(request,response);
		} else {
			processNotify1025(request,response);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processNotify1025(HttpServletRequest request,HttpServletResponse response) {
		Map map = request.getParameterMap();		
		TreeMap<String,String> sortMap = new TreeMap<String,String>();		
		Set<Map.Entry<String, Object>> entries = map.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        String sign = "";
        for (Map.Entry<String, Object> entry : entries) {
        	String value = request.getParameter(entry.getKey());
        	if("sign".equals(entry.getKey())){
        		sign=value;
        		continue;
        	}
        	sortMap.put(entry.getKey(), value);
        }
        Set<Map.Entry<String, String>> ent = sortMap.entrySet();
        for (Map.Entry<String, String> entry : ent) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }        
        System.out.println("stringbuff========"+stringBuffer.toString());
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        String orderno = request.getParameter("merchOrderNo");
        TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}
        logger.info("签名字符串为：" + singString);
        System.out.println("secmid:"+entity.getSecmerchantid());
        Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String pubkeystr = (String)mi.get("CHANNELPUBKEY");     
        System.out.println("pubsky:"+pubkeystr);
        PublicKey pubkey = SignTools.getPublicKey(pubkeystr);
        boolean bool = SignTools.verify(singString.getBytes(), sign, pubkey, SignTools.SIGN_TYPE_SHA1RSA);
        System.out.println("验签 :"+bool);
        if(bool){
        	System.out.println("notify 验签成功");        	
        	String tradeStatus = sortMap.get("tradeStatus");        	
    		String reqContent = entity.getSecreqcontent();
    		Trans1025Req req1025 = JSONObject.parseObject(reqContent, Trans1025Req.class);
    		String bgRetUrl = req1025.getBg_ret_url();				
    		String mid = req1025.getMer_id();
    		Trans1025Rsp rsp1025 = new Trans1025Rsp();
    		System.out.println("notify1025 status:"+tradeStatus);
    		String resp_desc =(String)sortMap.get("resultMessage");
    		String resp_code = "";
    		String trans_status="";
    		if("PAY_PROCESSING".equals(tradeStatus)||"WAIT_PAY".equals(tradeStatus)){
    			resp_code="102501";
    			trans_status="03";
    		}else if("FINISHED".equals(tradeStatus)||"SUCCESS".equals(tradeStatus)){
    			resp_code="102500";
    			trans_status="01";
    		}else if("CLOSED".equals(tradeStatus)){
    			resp_code="102502";
    			trans_status="02";
    		}
    		rsp1025.setResp_code(resp_code);
    		rsp1025.setResp_desc(resp_desc);
    		rsp1025.setMer_id(mid);
    		rsp1025.setOrder_id(req1025.getOrder_id());		
    		String rspdata = JSONObject.toJSONString(rsp1025);	
    		transBaseService.updateTransaction(orderno,trans_status,entity.getChannelno(),rspdata);		
    		PublicKey publicKey = GateWayController.getPublicKey(mid);	
    		String body = post(bgRetUrl,rspdata,publicKey, logger);
    		if(body.startsWith("RECV_ORD_ID_")){
    			try{
    				//易极付回写
    				response.getOutputStream().write("SUCCESS".getBytes());
    				response.flushBuffer();		    
    			} catch (Exception e) {
    			    e.printStackTrace();
    			    logger.error(e.getMessage(),e);
    			}
    		}        	
        }		
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processNotify1017(HttpServletRequest request,HttpServletResponse response) {		
		Map map = request.getParameterMap();		
		TreeMap<String,String> sortMap = new TreeMap<String,String>();
		Set<Map.Entry<String, Object>> entries = map.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        String sign = "";
        for (Map.Entry<String, Object> entry : entries) {
        	String value = request.getParameter(entry.getKey());
        	if("sign".equals(entry.getKey())){
        		sign=value;
        		continue;
        	}
        	sortMap.put(entry.getKey(), value);
        }        
        Set<Map.Entry<String, String>> ent = sortMap.entrySet();
        for (Map.Entry<String, String> entry : ent) {        	
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }        
        String orderno = (String)sortMap.get("merchOrderNo");
    	String serviceStatus = (String)sortMap.get("serviceStatus");
    	TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}
		Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String privateKeystr = (String)mi.get("CHANNELSECKEY"); 
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1) + privateKeystr;
        logger.info("签名字符串为：" + singString);
        String vsign = transYJFService.sign(singString);
        logger.info("生成的签名为：" + vsign);
        System.out.println("sign:"+sign);
        if(sign.equals(vsign)){
        	System.out.println("notify 验签成功");        	
    		String reqContent = entity.getSecreqcontent();
    		Trans1017Req req1017 = JSONObject.parseObject(reqContent, Trans1017Req.class);
    		String bgRetUrl = req1017.getBg_ret_url();				
    		String mid = req1017.getMer_id();
    		Trans1017Rsp rsp1017 = new Trans1017Rsp();
    		System.out.println("notify1017 status:"+serviceStatus);
    		String resp_desc =(String)sortMap.get("resultMessage");
    		String resp_code = "";
    		String trans_status="03";
    		if("REMITTANCE_FAIL".equals(serviceStatus)){
    			resp_code="101702";
    			trans_status="02";
    		}else if("REMITTANCE_SUCCESS".equals(serviceStatus)){
    			resp_code="101700";
    			trans_status="01";
    		}    		
    		rsp1017.setResp_code(resp_code);
    		rsp1017.setResp_desc(resp_desc);
    		rsp1017.setMer_id(mid);
    		rsp1017.setOrder_id(req1017.getOrder_id());		
    		String rspdata = JSONObject.toJSONString(rsp1017);	
    		transBaseService.updateTransaction(orderno,trans_status,entity.getChannelno(),rspdata);		
    		PublicKey publicKey = GateWayController.getPublicKey(mid);	
    		String body = post(bgRetUrl,rspdata,publicKey,logger);
    		if(body.startsWith("RECV_ORD_ID_")){
    			try{
    				//易极付回写
    				response.getOutputStream().write("SUCCESS".getBytes());
    				response.flushBuffer();		    
    			} catch (Exception e) {
    			    e.printStackTrace();
    			    logger.error(e.getMessage(),e);
    			}
    		}        	
        }		
	}

	@Override
	public Object process1023(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1023Req req1023 = JSONObject.parseObject(data, Trans1023Req.class);
		String orderId = req1023.getOrder_id();
		String mer_id = req1023.getMer_id();
		String cmd_id = req1023.getCmd_id();
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
		AddBankCardReqParams params = new AddBankCardReqParams();
		params.setPartnerId(cmid);
		params.setOrderNo(oid);
		params.setMerchOrderNo(oid);
		params.setSignAccId(req1023.getCard_no());
		params.setSignName(req1023.getCust_name());
		params.setSignID(req1023.getIdcard());
		params.setSignMobile(req1023.getBank_phone());
		params.setPartnerId(cmid);
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setIdcard(req1023.getIdcard());
		entity.setBankcardno(req1023.getCard_no());
		entity.setBankphone(req1023.getBank_phone());
		entity.setName(req1023.getCust_name());
		entity.setTranstime(req1023.getOrder_date());
		entity.setMerchantid(cmid);
		AddBankCardRespParams rspDTO = transYJFService.addBankCard(params, entity);
		String status = rspDTO.getResultCode();
		String resp_desc = rspDTO.getResultMessage();	
		String resp_code = "102302";
		if("EXECUTE_SUCCESS".equals(status)){
			if(rspDTO.getSignNo()!=null&&!"".equals(rspDTO.getSignNo())){
				resp_code = "102300";
			}
		}else if("EXECUTE_PROCESSING".equals(status)){
			resp_code = "102301";
		}		
		String trans_status = "102300".equals(resp_code)?"01":"02";
		Trans1023Rsp rsp1023 = new Trans1023Rsp();
		rsp1023.setResp_code(resp_code);
		rsp1023.setResp_desc(resp_desc);
		rsp1023.setMer_id(mid);
		rsp1023.setOrder_id(orderId);		
		String rspdata = JSONObject.toJSONString(rsp1023);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
        entity.setStatus(trans_status);
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata);
		transBaseService.insertTransaction(entity);
		mm.remove(orderId);
		return rm;
	}

	@Override
	public Object process1024(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1024Req req1024 = JSONObject.parseObject(data, Trans1024Req.class);
		String orderId = req1024.getOrder_id();
		String mer_id = req1024.getMer_id();
		String cmd_id = req1024.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te==null){
			return errorResponse(publicKey,"999999","无对应一阶段订单");
		}
		String oid = UtilData.createOnlyData();			
		ConfirmAddBankCardReqParams params = new ConfirmAddBankCardReqParams();
		params.setPartnerId(cmid);
		params.setOrderNo(oid);
		params.setMerchOrderNo(oid);
		String respcontent = te.getRespcontent();
		AddBankCardRespParams rp = JSON.parseObject(respcontent, AddBankCardRespParams.class);
		String signNo = rp.getSignNo();		
		params.setSignNo(signNo);
		params.setAuthMsg(req1024.getSmscode());		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setIdcard(te.getIdcard());
		entity.setBankcardno(te.getBankcardno());
		entity.setBankphone(te.getBankphone());
		entity.setName(te.getName());
		entity.setTranstime(req1024.getOrder_date());
		entity.setMerchantid(cmid);
		ConfirmAddBankCardRespParams rspDTO = transYJFService.confirmAddBankCard(params, entity);
		String status = rspDTO.getStatus();
		String resp_code = "";
		if("PACT_SUCCESS".equals(status)){
			resp_code = "102400";
		}else{
			resp_code = "102402";
		}	
		String resp_desc = rspDTO.getResultMessage();		
		String trans_status = "102400".equals(resp_code)?"01":"02";
		Trans1024Rsp rsp1024 = new Trans1024Rsp();
		rsp1024.setResp_code(resp_code);
		rsp1024.setResp_desc(resp_desc);
		rsp1024.setMer_id(mid);
		rsp1024.setOrder_id(orderId);
		rsp1024.setTreaty_id(rspDTO.getSignNo());
		String rspdata = JSONObject.toJSONString(rsp1024);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
        entity.setStatus(trans_status);
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata);
		transBaseService.insertTransaction(entity);
		return rm;
	}

	@Override
	public Object process1025(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1025Req req1025 = JSONObject.parseObject(data, Trans1025Req.class);
		String orderId = req1025.getOrder_id();
		String mer_id = req1025.getMer_id();
		String cmd_id = req1025.getCmd_id();
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
		TreatyPayReqParams params = new TreatyPayReqParams();
		params.setOrderNo(oid);
		params.setMerchOrderNo(oid);
		params.setPartnerId(cmid);
		params.setPayeeUserId(cmid);
		params.setOrderDesc(req1025.getGood_desc());
		params.setTradeAmount(req1025.getTrans_amt());
		params.setSignNo(req1025.getTreaty_id());		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
//		entity.setIdcard(req1025.getIdcard());
		entity.setBankcardno(req1025.getCard_no());
//		entity.setBankphone(req1025.getBank_phone());
//		entity.setName(req1025.getCust_name());
		entity.setTranstime(req1025.getOrder_date());
		entity.setMerchantid(cmid);
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1025.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(amount));	
		TreatyPayRespParams rspDTO = transYJFService.treatyPay(params, entity);
		String status = rspDTO.getResultCode();	
		String success = rspDTO.getSuccess();
		String tradeStatus = rspDTO.getTradeStatus();
		String resp_code = "";
		String trans_status="";
		if("PAY_PROCESSING".equals(tradeStatus)||"WAIT_PAY".equals(tradeStatus)){
			resp_code="102501";
			trans_status="03";
		}else if("FINISHED".equals(tradeStatus)||"SUCCESS".equals(tradeStatus)){
			resp_code="102500";
			trans_status="01";
		}else if("CLOSED".equals(tradeStatus)){
			resp_code="102502";
			trans_status="02";
		}else if("false".equals(success)){
			resp_code="102599";
			trans_status="02";
		} 
		String resp_desc = rspDTO.getResultMessage();
		Trans1025Rsp rsp1025 = new Trans1025Rsp();
		rsp1025.setResp_code(resp_code);
		rsp1025.setResp_desc(resp_desc);
		rsp1025.setMer_id(mid);
		rsp1025.setOrder_id(orderId);		
		String rspdata = JSONObject.toJSONString(rsp1025);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
        entity.setStatus(trans_status);
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata);
		transBaseService.insertTransaction(entity);
		mm.remove(orderId);
		return rm;
	}

	@Override
	public Object process1017(String mid, String cmid, String data,PublicKey publicKey) {
		System.out.println("p1017========================================");
		Trans1017Req req1017 = JSONObject.parseObject(data, Trans1017Req.class);
		String orderId = req1017.getOrder_id();
		String mer_id = req1017.getMer_id();
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
			return errorResponse(publicKey,"999999","商户该订单号正在处理中");
		}
		mm.add(orderId);
		String oid = UtilData.createOnlyData();
		PayReqParams params = new PayReqParams();
		params.setPartnerId(cmid);
		params.setOrderNo(oid);
		params.setMerchOrderNo(oid);
		params.setTransAmount(req1017.getTrans_amt());
		params.setAccountName(req1017.getCust_name());
		params.setCertNo(req1017.getIdcard());
		params.setAccountNo(req1017.getCard_no());
		params.setPurpose("线上交易");
		if("01".equals(req1017.getAccount_type())){
			params.setAccountType("PRIVATE");
		}else if("02".equals(req1017.getAccount_type())){
			params.setAccountType("PUBLIC");
		}else if("03".equals(req1017.getAccount_type())){
			params.setAccountType("YJF");
		}
		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setBankno(req1017.getBank_no());
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1017.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(amount));
		entity.setBankno(req1017.getBank_no());
		entity.setBankcardno(req1017.getCard_no());
		entity.setBankphone(req1017.getPhone());
		entity.setName(req1017.getCust_name());
		entity.setTranstime(req1017.getOrder_date());
		entity.setMerchantid(cmid);
		PayRespParams rspDTO = transYJFService.pay(params, entity);	
		String success = rspDTO.getSuccess();
		String resultCode = rspDTO.getResultCode();
		String resultMessage = rspDTO.getResultMessage();
				
		String resp_desc = resultMessage;
		String resp_code = "101701";
		String trans_status="03";
		if("false".equals(success)){
			resp_code="101702";
			trans_status="02";
		}else if("EXECUTE_FAIL".equals(resultCode)){
			resp_code="101702";
			trans_status="02";
		}
		entity.setStatus(trans_status);
		Trans1017Rsp rsp1017 = new Trans1017Rsp();
		rsp1017.setResp_code(resp_code);
		rsp1017.setResp_desc(resp_desc);
		rsp1017.setMer_id(mid);
		rsp1017.setOrder_id(orderId);	
		rsp1017.setOut_trans_id(oid);
		String rspdata = JSONObject.toJSONString(rsp1017);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign); 
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata);
		transBaseService.insertTransaction(entity);
		mm.remove(orderId);
		return rm;
	}	
	
}
