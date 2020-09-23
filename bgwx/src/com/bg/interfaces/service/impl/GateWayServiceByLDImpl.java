package com.bg.interfaces.service.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.controller.GateWayController;
import com.bg.interfaces.entity.Trans1003Req;
import com.bg.interfaces.entity.Trans1023Req;
import com.bg.interfaces.entity.Trans1023Rsp;
import com.bg.interfaces.entity.Trans1024Req;
import com.bg.interfaces.entity.Trans1024Rsp;
import com.bg.interfaces.entity.Trans1025Req;
import com.bg.interfaces.entity.Trans1025Rsp;
import com.bg.interfaces.entity.Trans1032Req;
import com.bg.interfaces.entity.Trans1032Rsp;
import com.bg.interfaces.service.AGateWayService;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.ld.ApplySmsBindReq;
import com.bg.trans.entity.ld.ApplySmsBindRsp;
import com.bg.trans.entity.ld.ConfirmSmsBindReq;
import com.bg.trans.entity.ld.ConfirmSmsBindRsp;
import com.bg.trans.entity.ld.CreateOrderReq;
import com.bg.trans.entity.ld.CreateOrderRsp;
import com.bg.trans.entity.ld.OrderPayReq;
import com.bg.trans.entity.ld.OrderPayRsp;
import com.bg.trans.entity.ld.PayNotifyReq;
import com.bg.trans.entity.ld.QueryOrderReq;
import com.bg.trans.entity.ld.QueryOrderRsp;
import com.bg.trans.entity.ld.UnbindReq;
import com.bg.trans.entity.ld.UnbindRsp;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransLDService;
import com.bg.util.SignTools;
import com.bg.util.UtilData;

@Service("gateWayServiceByLD")
public class GateWayServiceByLDImpl extends AGateWayService{
	private static Logger logger = Logger.getLogger(GateWayServiceByLDImpl.class);
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransLDService transLDService;
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	
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
//		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
//		String appToken = (String)mi.get("CHANNELPRIKEY");
//		String appKey = (String)mi.get("CHANNELSECKEY");
//		String clientName = (String)mi.get("CMID");
//		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();		
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
		ApplySmsBindReq req = new ApplySmsBindReq();
		req.setCard_id(req1023.getCard_no());
		req.setMedia_id(req1023.getBank_phone());
		req.setIdentity_code(req1023.getIdcard());
		req.setCard_holder(req1023.getCust_name());
		req.setMer_cust_id(req1023.getIdcard());
//		reqMap.put("card_id","6226732800364690");
//		reqMap.put("media_id","18033797586");
//		reqMap.put("identity_type","1");
//		reqMap.put("identity_code","133023198111280213");
//		reqMap.put("card_holder","李廷");
//		reqMap.put("mer_cust_id","133023198111280213");		
		ApplySmsBindRsp rsp = transLDService.applySmsBind(req,entity);
		String response_code = rsp.getRet_code();
		String response_message = rsp.getRet_msg();
		String resp_code = "";
		String trans_status = "";
		if("0000".equals(response_code)){
			resp_code="102300";
			trans_status="01";
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
		ApplySmsBindRsp bindRsp = JSONObject.parseObject(te.getRespcontent(), ApplySmsBindRsp.class);
		String bind_id = bindRsp.getBind_id();
		
		ConfirmSmsBindReq req = new ConfirmSmsBindReq();
		req.setBind_id(bind_id);
		req.setVerify_code(req1024.getSmscode());
//		reqMap.put("mer_id",mer_id);  
//		reqMap.put("bind_id","1908071727438135");
//		reqMap.put("verify_code","923982");
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
		ConfirmSmsBindRsp rsp = transLDService.confirmSmsBind(req,entity);
		String response_code = rsp.getRet_code();
		String response_message = rsp.getRet_msg();	
		String resp_code = "";
		String trans_status = "";
		if("0000".equals(response_code)){
			resp_code="102400";
			trans_status="01";
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
		rsp1024.setTreaty_id(rsp.getUsr_pay_agreement_id());
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
		CreateOrderReq coreq = new CreateOrderReq();		
		String time = req1025.getOrder_date();
		String date = time.substring(0,8);
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1025.getTrans_amt())*100);
//		reqMap.put("order_id",orderid);
//		reqMap.put("mer_date",date);
//		reqMap.put("amount","189000");
		coreq.setOrder_id(oid);
		coreq.setMer_date(date);
		coreq.setAmount(amount);
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
//		entity.setIdcard(req1025.getIdcard());
		entity.setBankcardno(req1025.getCard_no());
//		entity.setBankphone(req1025.getBank_phone());
//		entity.setName(req1025.getCust_name());
		entity.setTranstime(req1025.getOrder_date());		
		entity.setTranamt(Integer.parseInt(amount));	
		CreateOrderRsp corsp = transLDService.createOrder(coreq);	
		String trans_status = "";
		String resp_desc = "";
		String resp_code = "";
		//创建订单成功
		if("0000".equals(corsp.getRet_code())){
			OrderPayReq opreq = new OrderPayReq();
			String trade_no = corsp.getTrade_no();
			opreq.setTrade_no(trade_no);
			opreq.setMer_cust_id(req1025.getIdentity_id());
			opreq.setUsr_pay_agreement_id(req1025.getTreaty_id());
			OrderPayRsp rsp = transLDService.orderPay(opreq,entity);
			resp_desc = rsp.getRet_msg();
			if("0000".equals(rsp.getRet_code())||"00060780".equals(rsp.getRet_code())){
				resp_code="102500";
				trans_status="01";
			}else if("00200014".equals(rsp.getRet_code())||"00060761".equals(rsp.getRet_code())||"00080730".equals(rsp.getRet_code())){
				resp_code="102501";
				trans_status="03";
			}else{
				resp_code="102502";
				trans_status="02";
			}			
		}else{
			//创建订单失败
			logger.info("协议支付创建订单失败："+JSONObject.toJSONString(corsp));
			logger.info("协议支付创建订单失败请求参数："+JSONObject.toJSONString(coreq));
			resp_code = "102502";
			resp_desc = corsp.getRet_msg();
			trans_status="02";
			entity.setChannel(PARA_CONSTATNTS.Channel_LD);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyCollect);
			entity.setReqcontent(JSONObject.toJSONString(coreq));
			entity.setRespcontent(JSONObject.toJSONString(corsp));
		}	
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
	public Object process1032(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1032Req req1032 = JSONObject.parseObject(data, Trans1032Req.class);
		String orderId = req1032.getOrder_id();
		String mer_id = req1032.getMer_id();
		String cmd_id = req1032.getCmd_id();
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
//		Map<String,Object> mi = GateWayController.getMerchantInfo(mid);
//		String appToken = (String)mi.get("CHANNELPRIKEY");
//		String appKey = (String)mi.get("CHANNELSECKEY");
//		String clientName = (String)mi.get("CMID");
//		String company = (String)mi.get("CHANNELCOMPANY");
		String oid = UtilData.createOnlyData();		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		
		entity.setBankcardno(req1032.getCard_no());
		entity.setTranstime(req1032.getOrder_date());
		entity.setMerchantid(cmid);
		UnbindReq req = new UnbindReq();
		req.setMer_cust_id(req1032.getIdentity_id());
		req.setUsr_pay_agreement_id(req1032.getTreaty_id());
		UnbindRsp rsp = transLDService.unbind(req,entity);
		String response_code = rsp.getRet_code();
		String response_message = rsp.getRet_msg();
		String resp_code = "";
		String trans_status = "";
		if("0000".equals(response_code)){
			resp_code="103200";
			trans_status="01";
		}else{
			resp_code="103202";
			trans_status="02";
		}
		String resp_desc=response_message;			
		Trans1032Rsp rsp1032 = new Trans1032Rsp();
		rsp1032.setResp_code(resp_code);
		rsp1032.setResp_desc(resp_desc);
		rsp1032.setMer_id(mid);
		rsp1032.setOrder_id(orderId);		
		String rspdata = JSONObject.toJSONString(rsp1032);
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
	public void processNotify(HttpServletRequest request,HttpServletResponse response){
		String requestParam = request.getQueryString();
		String reqStr = transLDService.processNofity(requestParam,response);
		PayNotifyReq notifyParam = JSONObject.parseObject(reqStr,PayNotifyReq.class);
		String orderno = notifyParam.getOrder_id();
		TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			logger.info("requestno is null:"+orderno);
			return;
		}
		String reqContent = entity.getSecreqcontent();
		Trans1025Req req1025 = JSONObject.parseObject(reqContent, Trans1025Req.class);
		String bgRetUrl = req1025.getBg_ret_url();	
		String mid = req1025.getMer_id();
		String trade_state = notifyParam.getTrade_state();
		String resp_code = "";
		String trans_status = "03";
		if("TRADE_SUCCESS".equals(trade_state)){
			resp_code = "102500";
			trans_status = "01";
		}else if("WAIT_BUYER_PAY".equals(trade_state)){
			resp_code = "102501";
			trans_status = "03";
		}else{
			resp_code = "102502";
			trans_status = "02";
		}		
		String resp_desc = notifyParam.getError_code();		
		Trans1025Rsp rsp1025 = new Trans1025Rsp();
		rsp1025.setResp_code(resp_code);
		rsp1025.setResp_desc(resp_desc);
		rsp1025.setMer_id(mid);
		rsp1025.setOrder_id(req1025.getOrder_id());		
		String rspdata = JSONObject.toJSONString(rsp1025);			
		transBaseService.updateTransaction(entity.getOrderno(),trans_status,entity.getChannelno(),reqStr);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);			
		//异步通知商户
		post(bgRetUrl,rspdata,publicKey, logger);		
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
		String reqno = te.getReqno();
		Trans1025Req req1025 = null;
		try{
			req1025 = JSONObject.parseObject(te.getSecreqcontent(), Trans1025Req.class);
		}catch(Exception e){
			e.printStackTrace();
		}		
		String resp_code = "";
		String resp_desc = "";
		if(req1025==null){
			resp_code="100399";
			resp_desc="原交易与查询类型不符";
		}else{
			String date = req1025.getOrder_date().substring(0,8);
			QueryOrderReq req = new QueryOrderReq();
			req.setMer_date(date);
			req.setOrder_id(req.getOrder_id());			
			QueryOrderRsp rsp = transLDService.queryOrder(req);
			String trade_state = rsp.getTrade_state();
			String trans_status = "";
			/*******
			 *  ret_code=0000判断trade_state值如下：
				WAIT_BUYER_PAY 等待支付（已发起支付的情况需查询，如微信支付宝被扫交易需要输入密码情况）
				TRADE_SUCCESS 支付成功
				TRADE_FAIL 支付失败
				TRADE_CANCEL 订单已撤销
				TRADE_CLOSE 订单过期关闭				
				ret_code!=0000时判断ret_code值如下：
				00200078 支付订单不存在
				00200080 订单已关闭（因订单已过有效期）
				00200014 支付结果未明 需重新查询
				00060761 订单支付中 需重新查询
				其他 查询失败 需重新查询 
			 */
			if("0000".equals(rsp.getRet_code())){
				if("TRADE_SUCCESS".equals(trade_state)){
					resp_code = "102500";
					trans_status = "01";				
				}else if("WAIT_BUYER_PAY".equals(trade_state)){
					resp_code = "102501";
					trans_status = "03";
				}else{
					resp_code = "102502";
					trans_status = "02";
				}
			}else{
				if("00200014".equals(rsp.getRet_code())||"00060761".equals(rsp.getRet_code())){
					resp_code = "102501";
					trans_status = "03";
				}else{
					resp_code = "102502";
					trans_status = "02";
				}
			}			
			if(!trans_status.equals(te.getStatus())){
				transBaseService.updateTransaction(te.getOrderno(),trans_status,te.getChannelno(),te.getNotifycontent());
			}
			resp_desc=rsp.getOrig_retmsg();				
		}	
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
	
}
