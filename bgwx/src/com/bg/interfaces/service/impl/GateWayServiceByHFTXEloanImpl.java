package com.bg.interfaces.service.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.awt.image.PixelConverter.Bgrx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.controller.GateWayController;
import com.bg.interfaces.entity.Trans1003Req;
import com.bg.interfaces.entity.Trans1017Req;
import com.bg.interfaces.entity.Trans1023Req;
import com.bg.interfaces.entity.Trans1023Rsp;
import com.bg.interfaces.entity.Trans1024Req;
import com.bg.interfaces.entity.Trans1024Rsp;
import com.bg.interfaces.entity.Trans1026Req;
import com.bg.interfaces.entity.Trans1026Rsp;
import com.bg.interfaces.entity.Trans1027Req;
import com.bg.interfaces.entity.Trans1027Rsp;
import com.bg.interfaces.entity.Trans1028Req;
import com.bg.interfaces.entity.Trans1028Rsp;
import com.bg.interfaces.entity.Trans1029Req;
import com.bg.interfaces.entity.Trans1029Rsp;
import com.bg.interfaces.entity.Trans1030Req;
import com.bg.interfaces.entity.Trans1030Rsp;
import com.bg.interfaces.entity.Trans1031Req;
import com.bg.interfaces.entity.Trans1031Rsp;
import com.bg.interfaces.entity.Trans2002Req;
import com.bg.interfaces.entity.Trans2002Rsp;
import com.bg.interfaces.entity.Trans2003Req;
import com.bg.interfaces.entity.Trans2003Rsp;
import com.bg.interfaces.service.AGateWayService;
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
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransHFTXEloanService;
import com.bg.util.SignTools;
import com.bg.util.UtilData;

//@Service("gateWayServiceByHFTXEloan")
public class GateWayServiceByHFTXEloanImpl extends AGateWayService {
	private static Logger logger = Logger.getLogger(GateWayServiceByHFTXEloanImpl.class);
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransHFTXEloanService transHFTXEloanService;
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	
	@Override
	public Object process2002(String mid, String cmid,String data,PublicKey publicKey) {
		Trans2002Req req2002 = JSONObject.parseObject(data, Trans2002Req.class);
		String orderId = req2002.getOrder_id();
		String mer_id = req2002.getMer_id();
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");	
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		Trans101Req req101 = new Trans101Req();	
		req101.setApp_token(appToken);
		req101.setClient_name(clientName);
		req101.setCompany_name(company);
		req101.setRequest_seq(oid);
		req101.setRequest_date(req2002.getOrder_date().substring(0,8));
		req101.setCert_type("00");
		req101.setCert_id(req2002.getIdcard());
		req101.setUser_name(req2002.getCust_name());
		req101.setCard_no(req2002.getCard_no());
		req101.setCard_type("D");		
		req101.setBank_id(req2002.getBank_no());
		req101.setBank_mobile(req2002.getPhone());
		req101.setBank_province_id(req2002.getProvince());
		req101.setBank_area_id(req2002.getCity());
		req101.setBg_return_url(req2002.getBg_ret_url());		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setBankno(req2002.getBank_no());
		entity.setBankno(req2002.getBank_no());
		entity.setBankcardno(req2002.getCard_no());
		entity.setBankphone(req2002.getPhone());
		entity.setName(req2002.getCust_name());
		entity.setTranstime(req2002.getOrder_date());
		entity.setMerchantid(cmid);			
		Trans101Rsp rsp101 = transHFTXEloanService.trans101(req101, appKey, entity);
		String bind_code = rsp101.getResponse_code();
		String bind_error_message = rsp101.getResponse_message();
		
		String resp_code = "";
		String trans_status = "";
		if("10100".equals(bind_code)||"10102".equals(bind_code)){
			resp_code="200200";
			trans_status="01";
		}else if("10101".equals(bind_code)){
			resp_code="200201";
			trans_status="03";
		}else{
			resp_code="200202";
			trans_status="02";
		}
		String resp_desc=bind_error_message;			
		entity.setStatus(trans_status);
		Trans2002Rsp rsp2002 = new Trans2002Rsp();
		rsp2002.setResp_code(resp_code);
		rsp2002.setResp_desc(resp_desc);
		rsp2002.setMer_id(mid);
		rsp2002.setOrder_id(orderId);	
		String rspdata = JSONObject.toJSONString(rsp2002);
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
	@Override
	public Object process2003(String mid, String cmid,String data,PublicKey publicKey) {
		Trans2003Req req2003 = JSONObject.parseObject(data, Trans2003Req.class);
		String orderId = req2003.getOrder_id();
		String mer_id = req2003.getMer_id();
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");	
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		Trans103Req req103 = new Trans103Req();	
		req103.setApp_token(appToken);
		req103.setClient_name(clientName);
		req103.setCompany_name(company);
		req103.setRequest_seq(oid);
		req103.setRequest_date(req2003.getOrder_date().substring(0,8));
		req103.setCert_type("00");
		req103.setCert_id(req2003.getIdcard());
		req103.setUser_name(req2003.getCust_name());
		req103.setCard_no(req2003.getCard_no());
			
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setBankcardno(req2003.getCard_no());
		entity.setName(req2003.getCust_name());
		entity.setTranstime(req2003.getOrder_date());
		entity.setMerchantid(cmid);			
		Trans103Rsp rsp103 = transHFTXEloanService.trans103(req103, appKey, entity);
		String bind_code = rsp103.getResponse_code();
		String bind_error_message = rsp103.getResponse_message();
		
		String resp_code = "";
		String trans_status = "";
		if("10300".equals(bind_code)){
			resp_code="200300";
			trans_status="01";
		}else{
			resp_code="200302";
			trans_status="02";
		}
		String resp_desc=bind_error_message;			
		entity.setStatus(trans_status);
		Trans2003Rsp rsp2003 = new Trans2003Rsp();
		rsp2003.setResp_code(resp_code);
		rsp2003.setResp_desc(resp_desc);
		rsp2003.setMer_id(mid);
		rsp2003.setOrder_id(orderId);	
		String rspdata = JSONObject.toJSONString(rsp2003);
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
	@Override
	public Object process1026(String mid, String cmid,String data,PublicKey publicKey) {
		System.out.println("p1026========================================");
		Trans1026Req req1026 = JSONObject.parseObject(data, Trans1026Req.class);
		String orderId = req1026.getOrder_id();
		String mer_id = req1026.getMer_id();
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
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te!=null){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}	
		mm.add(orderId);
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		Trans201Req req201 = new Trans201Req();	
		req201.setApp_token(appToken);
		req201.setClient_name(clientName);
		req201.setCompany_name(company);
		req201.setRequest_seq(oid);
		req201.setRequest_date(req1026.getOrder_date().substring(0,8));
		req201.setBusiness_type("00");
		req201.setProduct_type(req1026.getProduct_type());
		req201.setLoan_purpose(req1026.getLoan_purpose());
		req201.setLoan_start_date(req1026.getLoan_start_date());
		req201.setLoan_end_date(req1026.getLoan_end_date());
		req201.setLoan_period(req1026.getLoan_period());
		req201.setLoan_period_type(req1026.getLoan_period_type());
//		req201.setContract_no(req1026.getContract_no());
		req201.setLoan_amount(req1026.getLoan_amount());
		req201.setEntrusted_flag(req1026.getEntrusted_flag());
		req201.setUser_cert_type("00");
		req201.setUser_cert_id(req1026.getUser_cert_id());
		req201.setUser_name(req1026.getUser_name());
		req201.setUser_card_no(req1026.getUser_card_no());
		req201.setUser_amount(req1026.getUser_amount());
		req201.setUser_cash_method(req1026.getUser_cash_method());
		req201.setMerchant_cert_type(req1026.getMerchant_cert_type());
		req201.setMerchant_cert_id(req1026.getMerchant_cert_id());
		req201.setMerchant_amount(req1026.getMerchant_amount());
		req201.setInstallment_number(req1026.getInstallment_number());
		req201.setInstallment_rate(req1026.getInstallment_rate());		
		req201.setPayment_method(req1026.getPayment_method());
		req201.setBg_return_url(req1026.getBg_ret_url());		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setBankcardno(req1026.getUser_card_no());
		entity.setName(req1026.getUser_name());
		entity.setTranstime(req1026.getOrder_date());
		entity.setMerchantid(cmid);		
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1026.getLoan_amount())*100);
		entity.setTranamt(Integer.parseInt(amount));
		Trans201Rsp rsp201 = transHFTXEloanService.trans201(req201, appKey, entity);		
		String response_code = rsp201.getResponse_code();
		String response_message = rsp201.getResponse_message();		
		String resp_code = "";
		String trans_status = "";
		if("20100".equals(response_code)){
			resp_code="102600";
			trans_status="01";
		}else if("20116".equals(response_code)){
			resp_code="102603";
			trans_status="01";
		}else if("20101".equals(response_code)){
			resp_code="102601";
			trans_status="03";
		}else{
			resp_code="102602";
			trans_status="02";
		}
		String resp_desc=response_message;			
		entity.setStatus(trans_status);
		Trans1026Rsp rsp1026 = new Trans1026Rsp();
		rsp1026.setResp_code(resp_code);
		rsp1026.setResp_desc(resp_desc);
		rsp1026.setMer_id(mid);
		rsp1026.setOrder_id(orderId);
		rsp1026.setLoan_amount(rsp201.getLoan_amount());
		rsp1026.setLoan_fee_amount(rsp201.getLoan_fee_amt());
		rsp1026.setCompany_fee_amt(rsp201.getCompany_fee_amt());
		String rspdata = JSONObject.toJSONString(rsp1026);
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
	
	@Override
	public Object process1027(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1027Req req1027 = JSONObject.parseObject(data, Trans1027Req.class);
		String orderId = req1027.getOrder_id();
		String mer_id = req1027.getMer_id();
		String cmd_id = req1027.getCmd_id();
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		entity.setIdcard(req1027.getCert_id());
		entity.setBankcardno(req1027.getCard_no());
		entity.setName(req1027.getUser_name());
		entity.setTranstime(req1027.getOrder_date().substring(0,8));
		String reqno = req1027.getLoan_request_seq();
		TransactionEntity lt = transBaseService.queryOrderByReqNo(reqno, mid);
		String resp_code = "";
		String trans_status="";
		String resp_desc = "";
		Trans1027Rsp rsp1027 = new Trans1027Rsp();
		if(lt==null){
			resp_code = "102702";
			resp_desc = "放款流水号不存在";
		}else{			
			Trans202Req req202 = new Trans202Req();	
			req202.setApp_token(appToken);
			req202.setClient_name(clientName);
			req202.setCompany_name(company);
			req202.setRequest_seq(oid);
			req202.setRequest_date(req1027.getOrder_date().substring(0,8));
			req202.setBusiness_type("00");
			req202.setProduct_type(req1027.getProduct_type());
			req202.setCert_type("00");
			req202.setCert_id(req1027.getCert_id());
			req202.setUser_name(req1027.getUser_name());
			req202.setCard_no(req1027.getCard_no());
			req202.setRepay_mode(req1027.getRepay_mode());
			req202.setTrans_amt(req1027.getTrans_amt());
			req202.setLoan_request_seq(lt.getOrderno());			
			req202.setBack_cert_type(req1027.getBack_cert_type());
			req202.setBack_cer_id(req1027.getBack_cert_id());
			if(req1027.getBack_div_details()!=null&&!"".equals(req1027.getBack_div_details())){
				req202.setBack_div_details(req1027.getBack_div_details());
			}			
			entity.setMerchantid(cmid);
			DecimalFormat df = new DecimalFormat("#");
			String amount = df.format(Double.parseDouble(req1027.getTrans_amt())*100);
			entity.setTranamt(Integer.parseInt(amount));			
			Trans202Rsp rsp202 = transHFTXEloanService.trans202(req202, appKey, entity);
			String response_code = rsp202.getResponse_code();
			String response_message = rsp202.getResponse_message();		
			if("20200".equals(response_code)){
				resp_code="102700";
				trans_status="01";
			}else if("20201".equals(response_code)){
				resp_code="102701";
				trans_status="03";
			}else{
				resp_code="102702";
				trans_status="02";
			}
			resp_desc=response_message;		
			rsp1027.setTrans_amt(rsp202.getTrans_amt());
			rsp1027.setFee_amt(rsp202.getFee_amt());
		}		
		rsp1027.setResp_code(resp_code);
		rsp1027.setResp_desc(resp_desc);
		rsp1027.setMer_id(mid);
		rsp1027.setOrder_id(orderId);		
		String rspdata = JSONObject.toJSONString(rsp1027);
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
	public Object process1028(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1028Req req1028 = JSONObject.parseObject(data, Trans1028Req.class);
		String orderId = req1028.getOrder_id();
		String mer_id = req1028.getMer_id();
		String cmd_id = req1028.getCmd_id();
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		
		String reqno = req1028.getLoan_request_seq();
		TransactionEntity lt = transBaseService.queryOrderByReqNo(reqno, mid);
		String resp_code = "";
		String trans_status="";
		String resp_desc = "";
		Trans1028Rsp rsp1028 = new Trans1028Rsp();
		if(lt==null){
			resp_code = "102802";
			resp_desc = "放款流水号不存在";
		}else{			
			Trans204Req req204 = new Trans204Req();	
			req204.setApp_token(appToken);
			req204.setClient_name(clientName);
			req204.setCompany_name(company);
			req204.setRequest_seq(oid);
			req204.setRequest_date(req1028.getOrder_date().substring(0,8));		
			req204.setLoan_request_seq(lt.getOrderno());	
			req204.setCard_no(req1028.getCard_no());
			entity.setMerchantid(cmid);
			entity.setOriginalorderno(lt.getOrderno());	
			entity.setIdcard(lt.getIdcard());
			entity.setBankcardno(req1028.getCard_no());
			entity.setName(lt.getName());
			entity.setTranstime(req1028.getOrder_date());
			Trans204Rsp rsp204 = transHFTXEloanService.trans204(req204, appKey, entity);
			String response_code = rsp204.getResponse_code();
			String response_message = rsp204.getResponse_message();		
			if("20400".equals(response_code)){
				resp_code="102800";
				trans_status="01";
			}else if("20401".equals(response_code)){
				resp_code="102801";
				trans_status="03";
			}else{
				resp_code="102802";
				trans_status="02";
			}
			resp_desc=response_message;		
			rsp1028.setCash_amt(rsp204.getCash_amt());
			rsp1028.setCash_fee_amt(rsp204.getCash_fee_amt());
		}		
		
		rsp1028.setResp_code(resp_code);
		rsp1028.setResp_desc(resp_desc);
		rsp1028.setMer_id(mid);
		rsp1028.setOrder_id(orderId);		
		String rspdata = JSONObject.toJSONString(rsp1028);
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();
		Trans102Req req102 = new Trans102Req();	
		req102.setApp_token(appToken);
		req102.setClient_name(clientName);
		req102.setCompany_name(company);
		req102.setRequest_seq(oid);
		req102.setRequest_date(req1023.getOrder_date().substring(0,8));
		req102.setCert_type("00");
		req102.setCert_id(req1023.getIdcard());
		req102.setUser_name(req1023.getCust_name());
		req102.setCard_type("D");
		req102.setCard_no(req1023.getCard_no());
		req102.setBank_mobile(req1023.getBank_phone());
		req102.setBank_id(req1023.getBank_id());
//		String transid = "D"+oid.substring(oid.length()-21);
		req102.setBind_trans_id(oid);
		req102.setStep_flag("01");
		req102.setBank_province_id(req1023.getCard_prov());
		req102.setBank_area_id(req1023.getCard_city());
			
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
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyApply);
		Trans102Rsp rsp102 = transHFTXEloanService.trans102(req102, appKey, entity);
		String response_code = rsp102.getResponse_code();
		String response_message = rsp102.getResponse_message();		
		String resp_code = "";
		String trans_status = "";
		if("10200".equals(response_code)){
			resp_code="102300";
			trans_status="01";
		}else if("10202".equals(response_code)){
			resp_code="102303";
			trans_status="02";
		}else if("10201".equals(response_code)){
			resp_code="102301";
			trans_status="03";
		}else{
			resp_code="102302";
			trans_status="02";
		}
		String resp_desc=response_message;			
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
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();			
		Trans102Req req102 = new Trans102Req();
		req102.setApp_token(appToken);
		req102.setClient_name(clientName);
		req102.setCompany_name(company);
		req102.setRequest_seq(oid);
		req102.setRequest_date(req1024.getOrder_date().substring(0,8));	
		String reqocontent = te.getReqcontent();
		Trans102Req rq = JSON.parseObject(reqocontent, Trans102Req.class);
		req102.setCert_type(rq.getCert_type());
		req102.setCert_id(rq.getCert_id());
		req102.setUser_name(rq.getUser_name());
		req102.setCard_type(rq.getCard_type());
		req102.setCard_no(rq.getCard_no());
		req102.setBank_mobile(rq.getBank_mobile());
		req102.setBank_id(rq.getBank_id());
		req102.setBind_trans_id(rq.getBind_trans_id());
		req102.setStep_flag("02");
		req102.setBank_province_id(rq.getBank_province_id());
		req102.setBank_area_id(rq.getBank_area_id());
		req102.setSms_code(req1024.getSmscode());		
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
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyConfirm);
		Trans102Rsp rsp102 = transHFTXEloanService.trans102(req102, appKey, entity);
		String response_code = rsp102.getResponse_code();
		String response_message = rsp102.getResponse_message();		
		String resp_code = "";
		String trans_status = "";
		if("10200".equals(response_code)){
			resp_code="102400";
			trans_status="01";
		}else if("10202".equals(response_code)){
			resp_code="102403";
			trans_status="02";
		}else if("10201".equals(response_code)){
			resp_code="102401";
			trans_status="03";
		}else{
			resp_code="102402";
			trans_status="02";
		}
		String resp_desc=response_message;			
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
	public Object process1029(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1029Req req1029 = JSONObject.parseObject(data, Trans1029Req.class);
		String orderId = req1029.getOrder_id();
		String mer_id = req1029.getMer_id();
		String cmd_id = req1029.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();			
		Trans308Req req308 = new Trans308Req();
		req308.setApp_token(appToken);
		req308.setClient_name(clientName);
		req308.setCompany_name(company);
		req308.setRequest_seq(oid);
		req308.setRequest_date(req1029.getOrder_date().substring(0,8));		
		Trans308Rsp rsp308 = transHFTXEloanService.trans308(req308, appKey);
		String response_code = rsp308.getResponse_code();
		String response_message = rsp308.getResponse_message();		
		String resp_code = "";
		if("30800".equals(response_code)){
			resp_code="102900";
		}else{
			resp_code="102902";
		}
		String resp_desc=response_message;			
		Trans1029Rsp rsp1029 = new Trans1029Rsp();
		rsp1029.setResp_code(resp_code);
		rsp1029.setResp_desc(resp_desc);
		rsp1029.setMer_id(mid);
		rsp1029.setOrder_id(orderId);
		rsp1029.setBalance(rsp308.getBalance());
		rsp1029.setAcct_balance(rsp308.getAcct_balance());
		rsp1029.setFreeze_balance(rsp308.getFreeze_balance());
		rsp1029.setAcct_state(rsp308.getAcct_state());
		String rspdata = JSONObject.toJSONString(rsp1029);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	
	@Override
	public Object process1030(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1030Req req1030 = JSONObject.parseObject(data, Trans1030Req.class);
		String orderId = req1030.getOrder_id();
		String mer_id = req1030.getMer_id();
		String cmd_id = req1030.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();			
		Trans305Req req305 = new Trans305Req();
		req305.setApp_token(appToken);
		req305.setClient_name(clientName);
		req305.setCompany_name(company);
		req305.setRequest_seq(oid);
		req305.setRequest_date(req1030.getOrder_date().substring(0,8));	
		
		req305.setCard_no(req1030.getCard_no());
		
		Trans305Rsp rsp305 = transHFTXEloanService.trans305(req305, appKey);
		String response_code = rsp305.getResponse_code();
		String response_message = rsp305.getResponse_message();		
		String resp_code = "";
		if("30500".equals(response_code)){
			resp_code="103000";
		}else{
			resp_code="103002";
		}
		String resp_desc=response_message;			
		Trans1030Rsp rsp1030 = new Trans1030Rsp();
		rsp1030.setResp_code(resp_code);
		rsp1030.setResp_desc(resp_desc);
		rsp1030.setMer_id(mid);
		rsp1030.setOrder_id(orderId);

		rsp1030.setCard_no(rsp305.getCard_no());
		rsp1030.setCard_bin(rsp305.getCard_bin());
		rsp1030.setBank_name(rsp305.getBank_name());
		rsp1030.setCard_type(rsp305.getCard_type());
		rsp1030.setBank_no(rsp305.getBank_no());		
		
		String rspdata = JSONObject.toJSONString(rsp1030);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	
	@Override
	public Object process1031(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1031Req req1031 = JSONObject.parseObject(data, Trans1031Req.class);
		String orderId = req1031.getOrder_id();
		String mer_id = req1031.getMer_id();
		String cmd_id = req1031.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}
		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
		String appToken = (String)mi.get("CHANNELPRIKEY");
		String appKey = (String)mi.get("CHANNELSECKEY");
		String clientName = (String)mi.get("CMID");
		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();			
		Trans306Req req306 = new Trans306Req();
		req306.setApp_token(appToken);
		req306.setClient_name(clientName);
		req306.setCompany_name(company);
		req306.setRequest_seq(oid);
		req306.setRequest_date(req1031.getOrder_date().substring(0,8));	
		
		req306.setCert_type("00");
		req306.setCert_id(req1031.getIdcard());
		
		Trans306Rsp rsp306 = transHFTXEloanService.trans306(req306, appKey);
		String response_code = rsp306.getResponse_code();
		String response_message = rsp306.getResponse_message();		
		String resp_code = "";
		if("30600".equals(response_code)){
			resp_code="103100";
		}else{
			resp_code="103102";
		}
		String resp_desc=response_message;			
		Trans1031Rsp rsp1031 = new Trans1031Rsp();
		rsp1031.setResp_code(resp_code);
		rsp1031.setResp_desc(resp_desc);
		rsp1031.setMer_id(mid);
		rsp1031.setOrder_id(orderId);

		rsp1031.setIdcard(rsp306.getCert_id());
		rsp1031.setCard_list(rsp306.getCard_list());
		
		String rspdata = JSONObject.toJSONString(rsp1031);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
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
		case "2002":
			rm = query2002(te,publicKey);
			break;
		case "1026":
			rm = query1026(te,publicKey);
			break;
		case "1024":
			rm = query1024(te,publicKey);
			break;
		case "1027":
			rm = query1027(te,publicKey);
			break;
		case "1028":
			rm = query1028(te,publicKey);
			break;
		default:
			return errorResponse(publicKey,"999999","查询原交易类型不正确:"+trans_type);
		}
		return rm;
	}
	
	private Map<String, String> query1028(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		Trans204Req req204 = null;
		try{
			req204 = JSONObject.parseObject(te.getReqcontent(), Trans204Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req204==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
			String appToken = (String)mi.get("CHANNELPRIKEY");
			String appKey = (String)mi.get("CHANNELSECKEY");
			String clientName = (String)mi.get("CMID");
			String company = (String)mi.get("CHANNELCOMPANY");
			String oid = UtilData.createOnlyData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			Trans307Req req307 = new Trans307Req();
			req307.setApp_token(appToken);
			req307.setClient_name(clientName);
			req307.setCompany_name(company);
			req307.setRequest_seq(oid);
			req307.setRequest_date(date);
			
			req307.setCash_request_seq(req204.getRequest_seq());
			
			Trans307Rsp rsp307 = transHFTXEloanService.trans307(req307, appKey);
			String code = rsp307.getResponse_code();
			String response_code = rsp307.getCash_return_code();
			String response_message = rsp307.getCash_return_message();
			if("30700".equals(code)){
				if("20400".equals(response_code)){
					resp_code="102800";
				}else if("20401".equals(response_code)){
					resp_code="102801";
				}else{
					resp_code="102802";
				}
				resp_desc=response_message;		
			}else{
				resp_code="10399";
				resp_desc = rsp307.getResponse_message();
			}			
		}	
		Trans1028Rsp rsp1028 = new Trans1028Rsp();
		rsp1028.setResp_code(resp_code);
		rsp1028.setResp_desc(resp_desc);
		rsp1028.setMer_id(mid);
		rsp1028.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp1028);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	private Map<String, String> query1027(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		Trans202Req req202 = null;
		try{
			req202 = JSONObject.parseObject(te.getReqcontent(), Trans202Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req202==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
			String appToken = (String)mi.get("CHANNELPRIKEY");
			String appKey = (String)mi.get("CHANNELSECKEY");
			String clientName = (String)mi.get("CMID");
			String company = (String)mi.get("CHANNELCOMPANY");
			String oid = UtilData.createOnlyData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			Trans304Req req304 = new Trans304Req();
			req304.setApp_token(appToken);
			req304.setClient_name(clientName);
			req304.setCompany_name(company);
			req304.setRequest_seq(oid);
			req304.setRequest_date(date);
			
			req304.setRepay_request_seq(req202.getRequest_seq());
			
			Trans304Rsp rsp304 = transHFTXEloanService.trans304(req304, appKey);
			String code = rsp304.getResponse_code();
			String response_code = rsp304.getRepay_return_code();
			String response_message = rsp304.getRepay_return_message();
			if("30400".equals(code)){
				if("20200".equals(response_code)){
					resp_code="102700";
				}else if("20201".equals(response_code)){
					resp_code="102701";
				}else{
					resp_code="102702";
				}
				resp_desc=response_message;		
			}else{
				resp_code="10399";
				resp_desc = rsp304.getResponse_message();
			}			
		}	
		Trans1027Rsp rsp1027 = new Trans1027Rsp();
		rsp1027.setResp_code(resp_code);
		rsp1027.setResp_desc(resp_desc);
		rsp1027.setMer_id(mid);
		rsp1027.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp1027);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	private Map<String, String> query1024(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		Trans102Req req102 = null;
		try{
			req102 = JSONObject.parseObject(te.getReqcontent(), Trans102Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req102==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
			String appToken = (String)mi.get("CHANNELPRIKEY");
			String appKey = (String)mi.get("CHANNELSECKEY");
			String clientName = (String)mi.get("CMID");
			String company = (String)mi.get("CHANNELCOMPANY");
			String oid = UtilData.createOnlyData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			Trans303Req req303 = new Trans303Req();
			req303.setApp_token(appToken);
			req303.setClient_name(clientName);
			req303.setCompany_name(company);
			req303.setRequest_seq(oid);
			req303.setRequest_date(date);
			
			req303.setCert_type("00");
			req303.setCert_id(req102.getCert_id());
			req303.setCard_no(req102.getCard_no());
			
			Trans303Rsp rsp303 = transHFTXEloanService.trans303(req303, appKey);
			String code = rsp303.getResponse_code();
			String response_code = rsp303.getBind_return_code();
			String response_message = rsp303.getBind_return_message();
			if("30300".equals(code)){
				if("10200".equals(response_code)){
					resp_code="102400";
				}else if("10202".equals(response_code)){
					resp_code="102403";
				}else if("10201".equals(response_code)){
					resp_code="102401";
				}else{
					resp_code="102402";
				}
				resp_desc=response_message;		
			}else{
				resp_code="10399";
				resp_desc = rsp303.getResponse_message();
			}
			
		}	
		Trans1026Rsp rsp1026 = new Trans1026Rsp();
		rsp1026.setResp_code(resp_code);
		rsp1026.setResp_desc(resp_desc);
		rsp1026.setMer_id(mid);
		rsp1026.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp1026);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	private Map<String, String> query1026(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		Trans201Req req201 = null;
		try{
			req201 = JSONObject.parseObject(te.getReqcontent(), Trans201Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req201==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
			String appToken = (String)mi.get("CHANNELPRIKEY");
			String appKey = (String)mi.get("CHANNELSECKEY");
			String clientName = (String)mi.get("CMID");
			String company = (String)mi.get("CHANNELCOMPANY");
			String oid = UtilData.createOnlyData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			Trans302Req req302 = new Trans302Req();
			req302.setApp_token(appToken);
			req302.setClient_name(clientName);
			req302.setCompany_name(company);
			req302.setRequest_seq(oid);
			req302.setRequest_date(date);
			
			req302.setLoan_request_seq(req201.getRequest_seq());
			
			Trans302Rsp rsp302 = transHFTXEloanService.trans302(req302, appKey);
			String response_code = rsp302.getLoan_return_code();
			String response_message = rsp302.getLoan_return_message();
			if("20100".equals(response_code)){
				resp_code="102600";
			}else if("20116".equals(response_code)){
				resp_code="102603";
			}else if("20101".equals(response_code)){
				resp_code="102601";
			}else{
				resp_code="102602";
			}
			resp_desc=response_message;		
		}	
		Trans1026Rsp rsp1026 = new Trans1026Rsp();
		rsp1026.setResp_code(resp_code);
		rsp1026.setResp_desc(resp_desc);
		rsp1026.setMer_id(mid);
		rsp1026.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp1026);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	private Map<String, String> query2002(TransactionEntity te,PublicKey publicKey) {
		String mid = te.getSecmerchantid();
		String reqno = te.getReqno();
		Trans2002Req req2002 = null;
		try{
			req2002 = JSONObject.parseObject(te.getSecreqcontent(), Trans2002Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req2002==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
			String appToken = (String)mi.get("CHANNELPRIKEY");
			String appKey = (String)mi.get("CHANNELSECKEY");
			String clientName = (String)mi.get("CMID");
			String company = (String)mi.get("CHANNELCOMPANY");
			String oid = UtilData.createOnlyData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			Trans301Req req301 = new Trans301Req();
			req301.setApp_token(appToken);
			req301.setClient_name(clientName);
			req301.setCompany_name(company);
			req301.setRequest_seq(oid);
			req301.setRequest_date(date);
			
			req301.setCard_no(req2002.getCard_no());
			req301.setCert_type("00");
			req301.setCert_id(req2002.getIdcard());
			
			Trans301Rsp rsp301 = transHFTXEloanService.trans301(req301, appKey);
			String code = rsp301.getResponse_code();
			String response_code = rsp301.getBind_return_code();
			String response_message = rsp301.getBind_return_message();
			if("30100".equals(code)){
				if("10100".equals(response_code)||"10102".equals(response_code)){
					resp_code="200200";
				}else if("10101".equals(response_code)){
					resp_code="200201";
				}else{
					resp_code="200202";
				}
				resp_desc=response_message;		
			}else{
				resp_code="100399";
				resp_desc=rsp301.getResponse_message();
			}
			
		}	
		Trans2002Rsp rsp2002 = new Trans2002Rsp();
		rsp2002.setResp_code(resp_code);
		rsp2002.setResp_desc(resp_desc);
		rsp2002.setMer_id(mid);
		rsp2002.setOrder_id(reqno);		
		String rspdata = JSONObject.toJSONString(rsp2002);
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);
		return rm;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void processNotify(HttpServletRequest request,HttpServletResponse response){
		Map map = request.getParameterMap();		
		TreeMap<String,String> sortMap = new TreeMap<String,String>();
		Set<Map.Entry<String, Object>> entries = map.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        String sign = "";
        for (Map.Entry<String, Object> entry : entries) {
        	String value = request.getParameter(entry.getKey());
        	if("signature".equals(entry.getKey())){
        		sign=value;
        		continue;
        	}
        	sortMap.put(entry.getKey(), value);
        }        
        Set<Map.Entry<String, String>> ent = sortMap.entrySet();
        for (Map.Entry<String, String> entry : ent) {        	
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }        
        String orderno = (String)sortMap.get("request_seq");
    	TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}
		Map<String,Object> mi = GateWayController.getMerchantInfo(entity.getSecmerchantid());
        String appKey = (String)mi.get("CHANNELSECKEY"); 
        String singString = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        logger.info("签名字符串为：" + singString);
        String vsign = transHFTXEloanService.sign(singString,appKey);
        logger.info("生成的签名为：" + vsign);
        System.out.println("sign:"+sign);
        //验签成功
        if(sign.equals(vsign)){
        	String cmd = entity.getTrantype();
        	switch (cmd) {
			case "2002":
				processNotify2002(sortMap,entity,response);
				break;
			case "1023":
				processNotify1023(sortMap,entity,response);
				break;
			case "1024":
				processNotify1024(sortMap,entity,response);
				break;
			case "1026":
				processNotify1026(sortMap,entity,response);
				break;
			case "1027":
				processNotify1027(sortMap,entity,response);
				break;
			case "1028":
				processNotify1028(sortMap,entity,response);
				break;
			default:
				break;
			}
        }       
	}
	
	private void processNotify1028(TreeMap<String, String> map,TransactionEntity entity, HttpServletResponse response) {
		String response_code = map.get("response_code");      
		String reponse_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans1028Req req1028 = JSONObject.parseObject(reqContent, Trans1028Req.class);
		String bgRetUrl = req1028.getBg_ret_url();	
		String mid = req1028.getMer_id();
		Trans1028Rsp rsp1028 = new Trans1028Rsp();
		System.out.println("notify1028 status:"+response_code);
		String resp_desc =reponse_message;
		String resp_code = "";
		String trans_status="";
		if("20400".equals(response_code)){
			resp_code="102800";
			trans_status="01";
		}else if("20401".equals(response_code)){
			resp_code="102801";
			trans_status="03";
		}else{
			resp_code="102802";
			trans_status="02";
		}
		rsp1028.setResp_code(resp_code);
		rsp1028.setResp_desc(resp_desc);
		rsp1028.setMer_id(mid);
		rsp1028.setOrder_id(req1028.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1028);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}
	}

	private void processNotify1024(TreeMap<String, String> map,TransactionEntity entity, HttpServletResponse response) {
		String response_code = map.get("response_code");      
		String reponse_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans1024Req req1024 = JSONObject.parseObject(reqContent, Trans1024Req.class);
		String bgRetUrl = req1024.getBg_ret_url();	
		String mid = req1024.getMer_id();
		Trans1024Rsp rsp1024 = new Trans1024Rsp();
		System.out.println("notify1024 status:"+response_code);
		String resp_desc =reponse_message;
		String resp_code = "";
		String trans_status="";
		if("10200".equals(response_code)){
			resp_code="102400";
			trans_status="01";
		}else if("10202".equals(response_code)){
			resp_code="102403";
			trans_status="02";
		}else if("10201".equals(response_code)){
			resp_code="102401";
			trans_status="03";
		}else{
			resp_code="102402";
			trans_status="02";
		}
		rsp1024.setResp_code(resp_code);
		rsp1024.setResp_desc(resp_desc);
		rsp1024.setMer_id(mid);
		rsp1024.setOrder_id(req1024.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1024);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}
	}

	private void processNotify1023(TreeMap<String, String> map,TransactionEntity entity, HttpServletResponse response) {
		String response_code = map.get("response_code");      
		String reponse_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans1023Req req1023 = JSONObject.parseObject(reqContent, Trans1023Req.class);
		String bgRetUrl = req1023.getBg_ret_url();	
		String mid = req1023.getMer_id();
		Trans1023Rsp rsp1023 = new Trans1023Rsp();
		System.out.println("notify1023 status:"+response_code);
		String resp_desc =reponse_message;
		String resp_code = "";
		String trans_status="";
		if("10200".equals(response_code)){
			resp_code="102300";
			trans_status="01";
		}else if("10202".equals(response_code)){
			resp_code="102303";
			trans_status="02";
		}else if("10201".equals(response_code)){
			resp_code="102301";
			trans_status="03";
		}else{
			resp_code="102302";
			trans_status="02";
		}
		rsp1023.setResp_code(resp_code);
		rsp1023.setResp_desc(resp_desc);
		rsp1023.setMer_id(mid);
		rsp1023.setOrder_id(req1023.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1023);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}      		
	}

	private void processNotify1027(TreeMap<String, String> map,TransactionEntity entity, HttpServletResponse response) {
		String response_code = map.get("response_code");      
		String reponse_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans1027Req req1027 = JSONObject.parseObject(reqContent, Trans1027Req.class);
		String bgRetUrl = req1027.getBg_ret_url();	
		String mid = req1027.getMer_id();
		Trans1027Rsp rsp1027 = new Trans1027Rsp();
		System.out.println("notify1027 status:"+response_code);
		String resp_desc =reponse_message;
		String resp_code = "";
		String trans_status="";
		if("20200".equals(response_code)){
			resp_code="102700";
			trans_status="01";
		}else if("20201".equals(response_code)){
			resp_code="102701";
			trans_status="03";
		}else{
			resp_code="102702";
			trans_status="02";
		}
		rsp1027.setResp_code(resp_code);
		rsp1027.setResp_desc(resp_desc);
		rsp1027.setMer_id(mid);
		rsp1027.setFee_amt(map.get("fee_amt"));
		rsp1027.setTrans_amt(map.get("trans_amt"));
		rsp1027.setOrder_id(req1027.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1027);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}      
		
	}

	private void processNotify1026(TreeMap<String, String> map,TransactionEntity entity, HttpServletResponse response) {
		String response_code = map.get("response_code");      
		String reponse_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans1026Req req1026 = JSONObject.parseObject(reqContent, Trans1026Req.class);
		String bgRetUrl = req1026.getBg_ret_url();	
		String mid = req1026.getMer_id();
		Trans1026Rsp rsp1026 = new Trans1026Rsp();
		System.out.println("notify1026 status:"+response_code);
		String resp_desc =reponse_message;
		String resp_code = "";
		String trans_status="";
		if("20100".equals(response_code)){
			resp_code="102600";
			trans_status="01";
		}else if("20116".equals(response_code)){
			resp_code="102603";
			trans_status="01";
		}else if("20101".equals(response_code)){
			resp_code="102601";
			trans_status="03";
		}else{
			resp_code="102602";
			trans_status="02";
		}
		rsp1026.setResp_code(resp_code);
		rsp1026.setResp_desc(resp_desc);
		rsp1026.setMer_id(mid);
		rsp1026.setOrder_id(req1026.getOrder_id());
		rsp1026.setLoan_amount(map.get("loan_amount"));
		rsp1026.setLoan_fee_amount(map.get("loan_fee_amt"));
		rsp1026.setCompany_fee_amt(map.get("company_fee_amt"));
		String rspdata = JSONObject.toJSONString(rsp1026);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}      
		
	}

	private void processNotify2002(Map<String,String> map,TransactionEntity entity,HttpServletResponse response) {
		String bind_code = map.get("response_code");      
		String bind_message = map.get("response_message");
		String reqContent = entity.getSecreqcontent();
		Trans2002Req req2002 = JSONObject.parseObject(reqContent, Trans2002Req.class);
		String bgRetUrl = req2002.getBg_ret_url();				
		String mid = req2002.getMer_id();
		Trans2002Rsp rsp2002 = new Trans2002Rsp();
		System.out.println("notify2002 status:"+bind_code);
		String resp_desc =bind_message;
		String resp_code = "";
		String trans_status="";
		if("10100".equals(bind_code)||"10102".equals(bind_code)){
			resp_code="200200";
			trans_status="01";
		}else if("10101".equals(bind_code)){
			resp_code="200201";
			trans_status="03";
		}else{
			resp_code="200202";
			trans_status="02";
		}
		rsp2002.setResp_code(resp_code);
		rsp2002.setResp_desc(resp_desc);
		rsp2002.setMer_id(mid);
		rsp2002.setOrder_id(req2002.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp2002);	
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		String body = post(bgRetUrl,rspdata,publicKey, logger);
		if(body.startsWith("RECV_ORD_ID_")){
			try{
				//汇付回写
				response.getOutputStream().write(("RECV_ORD_ID_"+entity.getOrderno()).getBytes());
				response.flushBuffer();		    
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.error(e.getMessage(),e);
			}
		}      
	}
	
}
