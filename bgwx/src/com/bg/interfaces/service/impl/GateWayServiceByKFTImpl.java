package com.bg.interfaces.service.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.bg.interfaces.entity.Trans1001Req;
import com.bg.interfaces.entity.Trans1001Rsp;
import com.bg.interfaces.entity.Trans1007Req;
import com.bg.interfaces.entity.Trans1007Rsp;
import com.bg.interfaces.entity.Trans1008Req;
import com.bg.interfaces.entity.Trans1008Rsp;
import com.bg.interfaces.entity.Trans1021Req;
import com.bg.interfaces.entity.Trans1021Rsp;
import com.bg.interfaces.service.AGateWayService;
import com.bg.interfaces.service.IGateWayService;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.SetSystemProperty;
import com.bg.util.SignTools;
import com.bg.util.UtilData;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmRespDTO;

@Service("gateWayServiceByKFT")
public class GateWayServiceByKFTImpl extends AGateWayService {
	private static Logger logger = Logger.getLogger(GateWayServiceByKFTImpl.class);
	
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransKFTService transKFTService;	
	private static String notifyUrl_ba = SetSystemProperty.getKeyValue("pay.notifyUrl_ba","kft/kft.properties");
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	@Override
	public Object process1001(String mid,String cmid,String data, PublicKey publicKey) {
		Trans1001Req req1001 = JSONObject.parseObject(data, Trans1001Req.class);
		String orderId = req1001.getOrder_id();
		String mer_id = req1001.getMer_id();
		String cmd_id = req1001.getCmd_id();
		String pay_type = req1001.getPay_type();
		String good_desc = req1001.getGood_desc();
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
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1001.getTrans_amt())*100);
		ActiveScanPayReqDTO reqDTO = new ActiveScanPayReqDTO();
		String oid = UtilData.createOnlyData();
		reqDTO.setOrderNo(oid);
		reqDTO.setReqNo(oid);
		reqDTO.setSecMerchantId(cmid);
		reqDTO.setAmount(amount);		
		String bankno = transKFTService.payChannelToKFTchannel(pay_type);
		reqDTO.setBankNo(bankno);			
		reqDTO.setTradeName(good_desc);
		System.out.println("1001================:"+notifyUrl_ba);
		reqDTO.setNotifyUrl(notifyUrl_ba);
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);
		ActiveScanPayRespDTO rspDTO = transKFTService.initiativePayZdsm(reqDTO,entity);
		String status = rspDTO.getStatus();
		String code_url = rspDTO.getCodeUrl();
		String resp_desc = rspDTO.getFailureDetails();	
		String resp_code = statusToRespCode(cmd_id,status);			
		Trans1001Rsp rsp1001 = new Trans1001Rsp();
		rsp1001.setResp_code(resp_code);
		rsp1001.setResp_desc(resp_desc);
		rsp1001.setMer_id(mid);
		rsp1001.setOrder_id(orderId);		
		rsp1001.setCode_url(code_url);
		rsp1001.setTrans_amt(req1001.getTrans_amt());	
		String rspdata = JSONObject.toJSONString(rsp1001);
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
	public Object process1007(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1007Req req1007 = JSONObject.parseObject(data, Trans1007Req.class);
		String orderId = req1007.getOrder_id();
		String mer_id = req1007.getMer_id();
		String cmd_id = req1007.getCmd_id();
		String good_desc = req1007.getGood_desc();
		String ip = req1007.getTerminalip();
		String bank_no = req1007.getBank_no();
		String card_no = req1007.getCard_no();
		String cust_name = req1007.getCust_name();
		String card_type = req1007.getCard_type();
		String card_validdate = req1007.getCard_validdate();
		String cvv = req1007.getCvv();
		String idcard = req1007.getIdcard();
		String bank_phone = req1007.getBank_phone();		
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}		
		Set<String> mm = orderMap.get(mid);
		boolean ishas = mm.contains(orderId);
		if(ishas==true){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		mm.add(orderId);
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te!=null){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1007.getTrans_amt())*100);
		SmsQuickPayApplyReqDTO reqDTO = new SmsQuickPayApplyReqDTO();
		String oid = UtilData.createOnlyData();	
		reqDTO.setReqNo(oid);//请求编号
		reqDTO.setSecMerchantId(cmid);////////////////////////
		reqDTO.setOrderNo(oid);///////////交易编号 
		reqDTO.setTerminalIp(ip); //////////////APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP 
		reqDTO.setAmount(amount);//////////////////////此次交易的具体金额,单位:分,不支持小数点
		reqDTO.setTradeName(good_desc);/////////////商品描述,简要概括此次交易的内容.可能会在用户App上显示
		reqDTO.setCustBankNo(bank_no);//////////////////支付渠道  参考快付通银行类型参数
		reqDTO.setCustBankAccountNo(card_no);////////////////客户银行账户号 本次交易中,从客户的哪张卡上扣钱
		reqDTO.setCustBindPhoneNo(bank_phone);///////////////持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		reqDTO.setCustName(cust_name);/////////////////收钱钱的客户的真实姓名
//		reqDTO.setCustBankAcctType("1");//客户银行账户类型 1个人 2企业 可空		
		reqDTO.setCustAccountCreditOrDebit(card_type);//////////////客户账户借记贷记类型 1借记 2贷记 3 未知		
		if("2".equals(card_type)){
			reqDTO.setCustCardValidDate(card_validdate);///////////////////如“0119”表示2019年1月份到期  可空
			reqDTO.setCustCardCvv2(cvv);////////////////客户信用卡的cvv2 信用卡的背面的三位数 可空		
		}		
		reqDTO.setCustID(idcard);//客户证件号码 与上述所选证件类型相匹配的证件号码
		reqDTO.setCustPhone(bank_phone);
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(orderId);
		entity.setSecmerchantid(mid);
		SmsQuickPayApplyRespDTO rspDTO = transKFTService.smsQuickPayApply(reqDTO, entity);
		String status = rspDTO.getStatus();
		String resp_desc = rspDTO.getFailureDetails();	
		String resp_code = statusToRespCode(cmd_id,status);
			
		Trans1007Rsp rsp1007 = new Trans1007Rsp();
		rsp1007.setResp_code(resp_code);
		rsp1007.setResp_desc(resp_desc);
		rsp1007.setMer_id(mid);
		rsp1007.setOrder_id(orderId);		
		rsp1007.setTrans_amt(req1007.getTrans_amt());	
		String rspdata = JSONObject.toJSONString(rsp1007);		
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
	public String statusToRespCode(String cmd_id,String status){
		String resp_code = "";
		switch (cmd_id) {
		case "1008":
			resp_code = "100801";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100800";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100802";
			}	
			break;
		case "1007":
			resp_code = "100701";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100700";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100702";
			}	
			break;
		case "1001":
			resp_code = "100101";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100100";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100102";
			}	
			break;
		case "1021":
			resp_code = "102102";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "102100";
			}else if("3".equals(status)){
				resp_code = "102101";
			}else if("7".equals(status)){
				resp_code = "102103";
			}	
			break;
		default:
			break;
		}
		return resp_code;
	}

	@Override
	public Object process1008(String mid, String cmid, String data,	PublicKey publicKey) {
		Trans1008Req req1008 = JSONObject.parseObject(data, Trans1008Req.class);
		String orderId = req1008.getOrder_id();
		String mer_id = req1008.getMer_id();	
		String cmd_id = req1008.getCmd_id();
		String smscode = req1008.getSmscode();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}		
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te==null){
			return errorResponse(publicKey,"999999","无对应一阶段订单");
		}
		SmsQuickPayConfirmReqDTO reqDTO = new SmsQuickPayConfirmReqDTO();
		reqDTO.setSecMerchantId(cmid);
		reqDTO.setOrderNo(te.getOrderno());//交易编号 
		reqDTO.setSmsCode(smscode);//短信验证码
		reqDTO.setCustBindPhoneNo(te.getBankphone());//持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		TransactionEntity entity = new TransactionEntity();
		entity.setReqno(orderId);
		entity.setSecmerchantid(mid);
		entity.setTranamt(te.getTranamt());
		entity.setName(te.getName());
		entity.setIdcard(te.getIdcard());
		entity.setBankno(te.getBankno());
		entity.setBankcardno(te.getBankcardno());
		entity.setBankphone(te.getBankphone());
		SmsQuickPayConfirmRespDTO rspDTO = transKFTService.smsQuickPayConfirm(reqDTO,entity);		
		String status = rspDTO.getStatus();
		String resp_desc = rspDTO.getFailureDetails();
		String resp_code = statusToRespCode(cmd_id,status);			
		Trans1008Rsp rsp1008 = new Trans1008Rsp();
		rsp1008.setResp_code(resp_code);
		rsp1008.setResp_desc(resp_desc);
		rsp1008.setMer_id(mid);
		rsp1008.setOrder_id(orderId);			
		String rspdata = JSONObject.toJSONString(rsp1008);			
		String enrspdata = Base64.encodeBase64String(SignTools.encrypt2(rspdata.getBytes(), publicKey));
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", enrspdata);		
		String dsign = SignTools.sign(rspdata.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);     
        entity.setSecreqcontent(data);
		entity.setSecrespcontent(rspdata);
		transBaseService.insertTransaction(entity);
		return rm;
	}
	
	@Override
	public Object process1021(String mid, String cmid, String data,PublicKey publicKey) {
		Trans1021Req req1021 = JSONObject.parseObject(data, Trans1021Req.class);
		String orderId = req1021.getOrder_id();
		String mer_id = req1021.getMer_id();	
		String cmd_id = req1021.getCmd_id();
		if(!mid.equals(mer_id)){
			return errorResponse(publicKey, "999999", "商户ID与报文内不一致");
		}		
		Set<String> mm = orderMap.get(mid);
		boolean ishas = mm.contains(orderId);
		if(ishas==true){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		mm.add(orderId);
		TransactionEntity te = transBaseService.queryOrderByReqNo(orderId, mid);
		if(te!=null){
			return errorResponse(publicKey,"999999","商户订单号重复");
		}
		DecimalFormat df = new DecimalFormat("#");
		String amount = df.format(Double.parseDouble(req1021.getTrans_amt())*100);
		String oid = UtilData.createOnlyData();
		PublicNoPayReqDTO reqDTO = new PublicNoPayReqDTO();
		reqDTO.setReqNo(oid);//请求编号
		reqDTO.setSecMerchantId(cmid);
		reqDTO.setOrderNo(oid);//交易编号 
		reqDTO.setTerminalIp(req1021.getTerminalip()); //APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
		reqDTO.setProductId(req1021.getGood_id());//此id为二维码中包含的商品ID，商户自行定义
		reqDTO.setAmount(amount);//此次交易的具体金额,单位:分,不支持小数点
		reqDTO.setTradeName(req1021.getGood_desc());//商品描述,简要概括此次交易的内容.可能会在用户App上显示
		reqDTO.setTradeTime(req1021.getOrder_date());//商户方交易时间 注意此时间取值一般为商户方系统时间而非快付通生成此时间 20120927185643
		reqDTO.setSubAppId(req1021.getApp_id());//商户公众号账号ID
		reqDTO.setUserOpenId(req1021.getBuyer_id());//用户在支付渠道的ID,如微信的openId，可以通过微信、支付宝SDK获取。 微信可参考： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_4
		reqDTO.setNotifyUrl(notifyUrl_ba);
		String payType =  req1021.getPay_type();
		String bankno = transKFTService.payChannelToKFTchannel(payType);
		TransactionEntity entity = new TransactionEntity();
		if("01".equals(payType)){
			reqDTO.setBankNo(bankno);//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003	
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_WX);
		}else if("02".equals(payType)){
			reqDTO.setBankNo(bankno);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_ZFB);
		}		
		entity.setReqno(orderId);
		entity.setOrderno(oid);
		entity.setSecmerchantid(mid);		
		entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_GZHPay);		
		PublicNoPayRespDTO rspDTO = transKFTService.publicNoPay(reqDTO,entity);
		String status = rspDTO.getStatus();
		String pay_info = rspDTO.getApiStr();
		String resp_desc = rspDTO.getFailureDetails();		
		String resp_code = statusToRespCode(cmd_id,status);	
		Trans1021Rsp rsp1021 = new Trans1021Rsp();
		rsp1021.setResp_code(resp_code);
		rsp1021.setResp_desc(resp_desc);
		rsp1021.setMer_id(mid);
		rsp1021.setOrder_id(orderId);		
		rsp1021.setPay_info(pay_info);
		rsp1021.setTrans_amt(req1021.getTrans_amt());	
		String rspdata = JSONObject.toJSONString(rsp1021);		
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
	public void processNotify(HttpServletRequest request,HttpServletResponse response) {
		String orderno = request.getParameter("orderNo");		
		String status = request.getParameter("status");
		String resp_desc = request.getParameter("failureDetails");
		String channelNo = request.getParameter("channelNo");
		logger.info("BA kft notity:"+ orderno +" status:"+status);		
		TransactionEntity entity = transBaseService.queryOrder(orderno);
		if (entity == null) {
			return;
		}
		String resp_code = statusToRespCode("1001", status);	
		String transStatus = transKFTService.ZDPayStatusToCode(status);
		String reqContent = entity.getSecreqcontent();		
		String transType = entity.getTrantype();
		switch (transType) {
		case "1001":
			notify1001(reqContent,orderno,transStatus,resp_code,resp_desc,channelNo);
			break;
		case "1021":
			notify1021(reqContent,orderno,transStatus,resp_code,resp_desc,channelNo);
			break;
		default:
			break;
		}
	}	
	
	private void notify1001(String reqContent,String orderNo,String transStatus,String resp_code,String resp_desc,String channelNo) {	
		logger.info("BA notify1001:"+orderNo + "failDetails:"+resp_desc);
		Trans1001Req req1001 = JSONObject.parseObject(reqContent, Trans1001Req.class);
		String bgRetUrl = req1001.getBg_ret_url();					
		String mid = req1001.getMer_id();
		Trans1001Rsp rsp1001 = new Trans1001Rsp();
		rsp1001.setResp_code(resp_code);
		rsp1001.setResp_desc(resp_desc);
		rsp1001.setMer_id(mid);
		rsp1001.setOrder_id(req1001.getOrder_id());		
		rsp1001.setTrans_amt(req1001.getTrans_amt());	
		rsp1001.setOut_trans_id(channelNo);
		String rspdata = JSONObject.toJSONString(rsp1001);	
		transBaseService.updateTransaction(orderNo,transStatus,channelNo,rspdata);		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		post(bgRetUrl,rspdata,publicKey,logger);		
	}

	private void notify1021(String reqContent,String orderNo,String transStatus,String resp_code,String resp_desc,String channelNo){		
		logger.info("BA KFT notify1021:"+orderNo+ "failDetails:"+resp_desc);
		Trans1021Req req1021 = JSONObject.parseObject(reqContent, Trans1021Req.class);
		String bgRetUrl = req1021.getBg_ret_url();		
		String mid = req1021.getMer_id();
		Trans1021Rsp rsp1021 = new Trans1021Rsp();
		rsp1021.setResp_code(resp_code);
		rsp1021.setResp_desc(resp_desc);
		rsp1021.setMer_id(mid);
		rsp1021.setOrder_id(req1021.getOrder_id());		
		rsp1021.setTrans_amt(req1021.getTrans_amt());	
		rsp1021.setOut_trans_id(channelNo);
		String rspdata = JSONObject.toJSONString(rsp1021);	
		transBaseService.updateTransaction(orderNo,transStatus,channelNo,rspdata);
		
		PublicKey publicKey = GateWayController.getPublicKey(mid);	
		post(bgRetUrl,rspdata,publicKey,logger);        
	}	
	
	

	
}
