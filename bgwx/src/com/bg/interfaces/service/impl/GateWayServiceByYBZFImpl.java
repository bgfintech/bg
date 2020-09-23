package com.bg.interfaces.service.impl;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.ParseException;
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

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransYBZFService;
import com.bg.util.SignTools;
import com.bg.util.UtilData;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.sqkkseperator.config.Config;

//@Service("gateWayServiceByYBZF")
public class GateWayServiceByYBZFImpl extends AGateWayService {
	private static Logger logger = Logger.getLogger(GateWayServiceByYBZFImpl.class);
	@Autowired
	private ITransBaseService transBaseService;
//	@Autowired
	private ITransYBZFService transYBZFService;
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	

	@Override
	public void processNotify(HttpServletRequest request,HttpServletResponse response) {
		String cmd = (String)request.getAttribute("cmd");
		logger.info("ybzf notify:=============");
		//获取回调数据
		String responseMsg = request.getParameter("response");
		System.out.println("responseMsg==:"+responseMsg);
		DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
		dto.setCipherText(responseMsg);
		if ("1017".equals(cmd)) {
			// 设置商户私钥
			// PrivateKey prik =
			// InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
			String priKey = Config.getInstance().getValue("paymentprikey");
			PrivateKey prik = getPrivateKey(priKey);
			// 设置易宝公钥
			// PublicKey pubk =
			// InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
			PublicKey pubk = getPubKey();
			// 解密验签
			dto = DigitalEnvelopeUtils.decrypt(dto, prik, pubk);
		} else {
			// 设置商户私钥
			// PrivateKey prik =
			// InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
			String priKey = Config.getInstance().getValue("privatekey");
			PrivateKey prik = getPrivateKey(priKey);
			// 设置易宝公钥
			// PublicKey pubk =
			// InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
			PublicKey pubk = getPubKey();
			// 解密验签
			dto = DigitalEnvelopeUtils.decrypt(dto, prik, pubk);
		}	   
	    //打印回调数据
	    System.out.println(dto.getPlainText());
	    Map<String,String> result = JSON.parseObject(dto.getPlainText(),new TypeReference<TreeMap<String,String>>(){});
	    String requestno = result.get("requestno");
	    String orderId = result.get("orderId"); 
		if(requestno!=null&&!"".equals(requestno)){
			processNotify1025(result,response);
		}else if(orderId!=null&&!"".equals(orderId)){
			processNotify1017(result,response);
		}
	}
	
	private void processNotify1025(Map<String,String> result,HttpServletResponse response){
		logger.info("ybzf processNotify1025:=============");
		String orderno = result.get("requestno");
		TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}	
		String reqContent = entity.getSecreqcontent();
		Trans1025Req req1025 = JSONObject.parseObject(reqContent, Trans1025Req.class);
		String bgRetUrl = req1025.getBg_ret_url();				
		String mid = req1025.getMer_id();
		Trans1025Rsp rsp1025 = new Trans1025Rsp();
		String cmd_id = req1025.getCmd_id();
		String status = result.get("status");
		String resp_desc = result.get("errormsg");
		String resp_code = statusToRespCode(cmd_id,status);			
		String trans_status = "102500".equals(resp_code)?"01":"102501".equals(resp_code)?"03":"02";			
		rsp1025.setResp_code(resp_code);
		rsp1025.setResp_desc(resp_desc);
		rsp1025.setMer_id(mid);
		rsp1025.setOrder_id(req1025.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1025);	
		transBaseService.updateTransaction(orderno,trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey,logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//易宝回写
				response.getOutputStream().write("SUCCESS".getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}		
	}
	
	private void processNotify1017(Map<String,String> result,HttpServletResponse response){
		logger.info("ybzf processNotify1017:=============");
		String orderno = result.get("orderId");
	    TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}
		String reqContent = entity.getSecreqcontent();
		Trans1017Req req1017 = JSONObject.parseObject(reqContent, Trans1017Req.class);
		String bgRetUrl = req1017.getBg_ret_url();				
		String mid = req1017.getMer_id();
		Trans1017Rsp rsp1017 = new Trans1017Rsp();
		String status = result.get("status");
		System.out.println("notify1017 status:"+status);
		String resp_desc = result.get("errormsg");
		if(resp_desc==null&&"".equals(resp_desc)){
			resp_desc = result.get("errorMsg");
		}
		String tsc = result.get("transferStatusCode");
		String bsc = result.get("bankTrxStatusCode");
		String resp_code = "";
		String trans_status="03";
		if("0025".equals(tsc)){
			resp_code="101703";
			trans_status="02";
		}else if("0028".equals(tsc)){
			resp_code="101704";
			trans_status="02";
		}else if("0029".equals(tsc)){
			resp_code="101705";
		}else if("0030".equals(tsc)){
			resp_code="101706";
		}else if("0027".equals(tsc)){
			resp_code="101707";
			trans_status="02";
		}else if("0026".equals(tsc)&&"S".equals(bsc)){
			resp_code="101708";
			trans_status="01";
		}else if("0026".equals(tsc)&&"I".equals(bsc)){
			resp_code="101709";
		}else if("0026".equals(tsc)&&"F".equals(bsc)){
			resp_code="101710";
			trans_status="02";
		}else if("0026".equals(tsc)&&"W".equals(bsc)){
			resp_code="101711";
		}else if("0026".equals(tsc)&&"U".equals(bsc)){
			resp_code="101712";
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
				//易宝回写
				response.getOutputStream().write("SUCCESS".getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}		
	}
	
//	public void processTransferNotify(HttpServletRequest request,HttpServletResponse response){	
//		logger.info("ybzf 1017notify:=============");
//		//获取回调数据
//		String responseMsg = request.getParameter("response");
//		DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
//		dto.setCipherText(responseMsg);
//		Map<String,String> jsonMap  = new HashMap<>();
//		try {
//		    //设置商户私钥
////		    PrivateKey prik = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
//			PrivateKey prik = getPrivateKey();
//		    //设置易宝公钥
////		    PublicKey pubk = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
//		    PublicKey pubk = getPubKey();
//		    //解密验签
//		    dto = DigitalEnvelopeUtils.decrypt(dto, prik, pubk);
//		    //打印回调数据
//		    System.out.println(dto.getPlainText());
//		    Map<String,String> result = JSON.parseObject(dto.getPlainText(),new TypeReference<TreeMap<String,String>>(){});
//		    
//	}

	@Override
	public String statusToRespCode(String cmd_id, String status) {
		String resp_code = "";
		switch (cmd_id) {
		case "1017":
			resp_code = "101701";
			if("0025".equals(status)){
				resp_code = "101703";
			}else if("0026".equals(status)){
				resp_code = "101704";
			}else if("0027".equals(status)){
				resp_code = "101705";
			}else if("0028".equals(status)){
				resp_code = "101706";
			}else if("0029".equals(status)){
				resp_code = "101707";
			}
			break;
		case "1023":
			resp_code = "102302";
			if("BIND_SUCCESS".equals(status)||"TO_VALIDATE".equals(status)){
				resp_code = "102300";
			}	
			break;
		case "1024":
			resp_code = "102402";
			if("BIND_SUCCESS".equals(status)){
				resp_code = "102400";
			}
			break;
		case "1025":
			resp_code = "102501";
			if("PAY_FAIL".equals(status)){
				resp_code = "102502";
			}else if("TIME_OUT".equals(status)){
				resp_code = "102503";
			}else if("FAIL".equals(status)){
				resp_code = "102599";
			}else if("PAY_SUCCESS".equals(status)){
				resp_code = "102500";
			}
			break;
		default:
			break;
		}
		return resp_code;
	}

	@Override
	public Object process1020(String mid, String cmid, String data,PublicKey publicKey) {
		return errorResponse(publicKey, "999999", "未开通该功能");
	}

	@Override
	public Object process1023(String mid, String cmid, String data,	PublicKey publicKey) {
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
		Map<String,String> map = new HashMap<String,String>();
		map.put("requestno", oid);
		map.put("identityid", format(req1023.getIdentity_id()));
//		map.put("identitytype", "AGREEMENT_NO");
		map.put("cardno", format(req1023.getCard_no()));
		map.put("idcardno", format(req1023.getIdcard()));
//		map.put("idcardtype", "ID");
		map.put("username",format(req1023.getCust_name()));
		map.put("phone", format(req1023.getBank_phone()));
		map.put("issms", "true");
		map.put("advicesmstype", "MESSAGE");
		map.put("smstemplateid", "");
		map.put("smstempldatemsg", "");
		map.put("avaliabletime", "");
		map.put("callbackurl", "");			
//		map.put("authtype", "COMMON_FOUR");
		map.put("remark", "");
		map.put("extinfos", "");	   
		String time = format(req1023.getOrder_date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		try {
			Date tmp = sdf.parse(time);
			map.put("requesttime", sdf1.format(tmp));
		} catch (ParseException e) {
			e.printStackTrace();
		}

			
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setIdcard(req1023.getIdcard());
		entity.setBankcardno(req1023.getCard_no());
		entity.setBankphone(req1023.getBank_phone());
		entity.setName(req1023.getCust_name());
		entity.setTranstime(req1023.getOrder_date());
		Map<String,String> rspDTO = transYBZFService.authBindCardRequest(map, entity);
		String status = rspDTO.get("status");
		String resp_desc = rspDTO.get("errormsg");
		String resp_code = statusToRespCode(cmd_id,status);			
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
		Map<String,String> map = new HashMap<String,String>();
		map.put("requestno", te.getOrderno());
		map.put("validatecode", req1024.getSmscode());
		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setIdcard(te.getIdcard());
		entity.setBankcardno(te.getBankcardno());
		entity.setBankphone(te.getBankphone());
		entity.setName(te.getName());
		entity.setTranstime(req1024.getOrder_date());
		Map<String,String> rspDTO = transYBZFService.authBindCardConfirm(map, entity);
		String status = rspDTO.get("status");
		String resp_desc = rspDTO.get("errormsg");
		String resp_code = statusToRespCode(cmd_id,status);			
		String trans_status = "102400".equals(resp_code)?"01":"02";
		Trans1024Rsp rsp1024 = new Trans1024Rsp();
		rsp1024.setResp_code(resp_code);
		rsp1024.setResp_desc(resp_desc);
		rsp1024.setMer_id(mid);
		rsp1024.setOrder_id(orderId);		
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
		Map<String,String> map = new HashMap<String,String>();
//		map.put("merchantno", merId);
		map.put("requestno", oid);
//		map.put("issms", "false");
		map.put("identityid", req1025.getIdentity_id());
//		map.put("identitytype", "AGREEMENT_NO");
		String cardno = req1025.getCard_no();	
		map.put("cardtop", cardno.substring(0,6));
		map.put("cardlast", cardno.substring(cardno.length()-4));		
		map.put("amount", req1025.getTrans_amt());
//		map.put("advicesmstype", advicesmstype);
//		map.put("avaliabletime", avaliabletime);
		map.put("productname", req1025.getGood_desc());
//		map.put("callbackurl", callbackUrl);
		String time = format(req1025.getOrder_date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		try {
			Date tmp = sdf.parse(time);
			map.put("requesttime", sdf1.format(tmp));
		} catch (ParseException e) {
			e.printStackTrace();
		}	
//		map.put("terminalno", "SQKKSCENEKJ010");
//		map.put("remark", remark);
//		map.put("extinfos", extinfos);
//		map.put("dividecallbackurl", dividecallbackurl);
//		map.put("dividejstr", dividejstr);			
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
//		entity.setIdcard(req1025.getIdcard());
		entity.setBankcardno(req1025.getCard_no());
//		entity.setBankphone(req1025.getBank_phone());
//		entity.setName(req1025.getCust_name());
		entity.setTranstime(req1025.getOrder_date());
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1025.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(amount));	
		Map<String,String> rspDTO = transYBZFService.treatyPay(map, entity);
		String status = rspDTO.get("status");
		String resp_code = statusToRespCode(cmd_id,status);			
		String trans_status = "102501".equals(resp_code)?"03":"02";
		if(status==null||"".equals(status)){
			String state = rspDTO.get("state");
			if("FAILURE".equals(state)){
				resp_code="102599";
				trans_status="02";
			}
		}		
		String resp_desc = rspDTO.get("errormsg");	
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
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		boolean isbf = false;
		if(te!=null){
			oid = te.getOrderno();
			isbf=true;
			logger.info("1017补发订单:"+oid+":"+data);
		}		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("batchNo", oid);
		params.put("orderId", oid);
		params.put("amount", req1017.getTrans_amt());
//		params.put("product", product);
//		params.put("urgency", urgency);
		params.put("accountName", req1017.getCust_name());
		params.put("accountNumber", req1017.getCard_no());
		params.put("bankCode", req1017.getBank_no());
//		params.put("bankName", bankName);
		params.put("bankBranchName", req1017.getBranch_name());
		params.put("provinceCode", req1017.getProvince());
		params.put("cityCode", req1017.getCity());
//		params.put("feeType", feeType);
//		params.put("desc", desc);
//		params.put("leaveWord", leaveWord);
//		params.put("abstractInfo", abstractInfo);		
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
		
		Map<String,Object> rspDTO = transYBZFService.transferSingle(params, entity);		
		String status = (String)rspDTO.get("status");
		String errorCode = (String)rspDTO.get("errorCode");
		String resp_desc = (String)rspDTO.get("errormsg");
		if(resp_desc==null&&"".equals(resp_desc)){
			resp_desc = (String)rspDTO.get("errorMsg");
		}
		String tsc = (String)rspDTO.get("transferStatusCode");
		String bsc = (String)rspDTO.get("bankTrxStatusCode");
		String resp_code = "101701";
		String trans_status="03";
		if("BAC009999".equals(errorCode)){
			resp_code="101701";
		}else if("0025".equals(tsc)){
			resp_code="101703";
			trans_status="03";
		}else if("0028".equals(tsc)){
			resp_code="101704";
			trans_status="03";
		}else if("0029".equals(tsc)){
			resp_code="101705";
		}else if("0030".equals(tsc)){
			resp_code="101706";
		}else if("0027".equals(tsc)){
			resp_code="101707";
			trans_status="02";
		}else if("0026".equals(tsc)&&"S".equals(bsc)){
			resp_code="101708";
			trans_status="01";
		}else if("0026".equals(tsc)&&"I".equals(bsc)){
			resp_code="101709";
		}else if("0026".equals(tsc)&&"F".equals(bsc)){
			resp_code="101710";
			trans_status="02";
		}else if("0026".equals(tsc)&&"W".equals(bsc)){
			resp_code="101711";
		}else if("0026".equals(tsc)&&"U".equals(bsc)){
			resp_code="101712";
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
		if(!isbf){
			transBaseService.insertTransaction(entity);
		}
		mm.remove(orderId);
		return rm;
	}
	
	@Override
	public Object process1003(String mid, String cmd, String data,PublicKey publicKey) {
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
	
	private Map<String,String> query1025(TransactionEntity te,PublicKey publicKey){
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		String orderno = te.getOrderno();	
		Map<String, String> params = new HashMap<>();
		params.put("requestno", orderno);
//		params.put("yborderid", yborderid);
		Map<String,String> rspDTO = transYBZFService.treatPayQuery(params);		
		System.out.println(rspDTO);
		String resp_desc = (String)rspDTO.get("errormsg");
		String status = rspDTO.get("status");
		String resp_code = statusToRespCode("1025",status);
		Trans1025Rsp rsp1025 = new Trans1025Rsp();
		rsp1025.setResp_code(resp_code);
		rsp1025.setResp_desc(resp_desc);
		rsp1025.setMer_id(mid);
		rsp1025.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp1025);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,String> query1017(TransactionEntity te,PublicKey publicKey){
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		String orderno = te.getOrderno();	
		Map<String, Object> params = new HashMap<>();
		params.put("batchNo", orderno);
		params.put("orderId", orderno);		
		Map<String,Object> rspDTO = transYBZFService.paymentQuery(params);		
		String tsc = "";
		String bsc = "";
		List<JSONObject> li = new ArrayList<JSONObject>();
		if (rspDTO.get("list") != null) {
			li = (List<JSONObject>) rspDTO.get("list");
			System.out.println("list----" + li);
			if(li.size()>0){
				JSONObject js = li.get(0);
				bsc = (String)js.get("bankTrxStatusCode");
				tsc = (String)js.get("transferStatusCode");
			}				
		}
		String errorCode = (String)rspDTO.get("errorCode");
		String resp_desc = (String)rspDTO.get("errormsg");
		if(resp_desc==null&&"".equals(resp_desc)){
			resp_desc = (String)rspDTO.get("errorMsg");
		}
		String resp_code = "100302";
		if("BAC009999".equals(errorCode)){
			resp_code="101701";
		}else if("0025".equals(tsc)){
			resp_code="101703";
		}else if("0028".equals(tsc)){
			resp_code="101704";
		}else if("0029".equals(tsc)){
			resp_code="101705";
		}else if("0030".equals(tsc)){
			resp_code="101706";
		}else if("0027".equals(tsc)){
			resp_code="101707";
		}else if("0026".equals(tsc)&&"S".equals(bsc)){
			resp_code="101708";
		}else if("0026".equals(tsc)&&"I".equals(bsc)){
			resp_code="101709";
		}else if("0026".equals(tsc)&&"F".equals(bsc)){
			resp_code="101710";
		}else if("0026".equals(tsc)&&"W".equals(bsc)){
			resp_code="101711";
		}else if("0026".equals(tsc)&&"U".equals(bsc)){
			resp_code="101712";
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
	
	
	
	
	
	private String format(String text) {
		return text == null ? "" : text.trim();
	}
	
	/**
	 * 实例化公钥
	 * 
	 * @return
	 */
	private PublicKey getPubKey() {
		PublicKey publicKey = null;
		try {
			// 自己的公钥(测试)
			String publickey = Config.getInstance().getValue("publickey");
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(publickey));
			// RSA对称加密算法
			java.security.KeyFactory keyFactory;
			keyFactory = java.security.KeyFactory.getInstance("RSA");
			// 取公钥匙对象
			publicKey = keyFactory.generatePublic(bobPubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	private PrivateKey getPrivateKey(String priKey) {
		PrivateKey privateKey = null;
		
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(priKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	
	
}
