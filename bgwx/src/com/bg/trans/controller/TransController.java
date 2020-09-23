package com.bg.trans.controller;



import java.io.IOException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.util.Constants;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransRsp;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.nspos.TransE1102Req;
import com.bg.trans.entity.nspos.TransE1102Rsp;
import com.bg.trans.entity.nspos.TransE1103Req;
import com.bg.trans.entity.nspos.TransE1103Rsp;
import com.bg.trans.entity.nspos.TransE1103Rsp;
import com.bg.trans.service.INsposService;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransHFTXService;
import com.bg.trans.service.ITransKFTService;
import com.bg.trans.service.ITransLDService;
import com.bg.util.UtilData;
import com.bg.web.entity.AccountInfo;
import com.bg.web.service.AccountServiceI;
import com.bg.web.service.MerchantServiceI;
import com.lycheepay.gateway.client.GBPService;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyResultDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.BankCardDetailDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardCollectFromBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardPayToBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardSmsCollectConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTradeResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectResultDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantResponseDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantRequestDTO;

@Controller
@RequestMapping("/transController")
public class TransController extends BaseController {
	private static Logger logger = Logger.getLogger(TransController.class);
	@Autowired
	private ITransKFTService transKFTService;
	@Autowired
	private ITransHFTXService transHFTXService;
	@Autowired
	private AccountServiceI accountService;
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransLDService transLDService;
	@Autowired
	private INsposService nsposService;
	@Autowired
	private MerchantServiceI merchantService;
	
	@RequestMapping("/kftnotify")
	@ResponseBody
	public void notifyPay(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String orderNo = request.getParameter("orderNo");
		String status = request.getParameter("status");
		String channelNo = request.getParameter("channelNo");	
		String settlementAmount = request.getParameter("settlementAmount");
		logger.info("KFT notify:"+orderNo+":"+status+":"+channelNo+":"+settlementAmount);
		transKFTService.updateTransaction(orderNo,status,channelNo,null);		
	}
	
//	@RequestMapping("/ldnotify")
//	@ResponseBody
//	public void ldNotifyPay(HttpServletRequest request,HttpServletResponse response) throws IOException{
//		String requestParam = request.getQueryString();
//		transLDService.processNofity(requestParam);
//	}
	
	@RequestMapping(params = "hftxreturl")
	public ModelAndView hftxRetPage(HttpServletRequest request){
		String sign =  request.getParameter("check_value");
		Map<String,String> retMap = transHFTXService.processRetUrl(sign);		
		String openid = request.getParameter("openid");
		String accountid = request.getParameter("accountid");
		AccountInfo accountInfo = accountService.getAccountByAccountId(accountid);
		Map<String,Object> personInfo = accountService.queryCashCardInfo(accountInfo.getAccount(),"01");
		Map<String,Object> transInfo = accountService.queryLastGbpTransInfo(accountInfo.getAccount(),PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
		String cbco = (String)personInfo.get("BANKCARDNO");
		String tcbco = "";
		if(cbco!=null&&!"".equals(cbco)){
			tcbco = cbco.substring(0,4)+"********"+cbco.substring(cbco.length()-4);
		}
		if(retMap!=null){
			String retstatus = retMap.get("retstatus");
			String msg = retMap.get("msg");
			request.setAttribute("retstatus", retstatus);
			request.setAttribute("retmsg", msg);
		}		
		request.setAttribute("openid", openid);
		request.setAttribute("accountinfo", accountInfo);
		request.setAttribute("tcbankcardno", tcbco);
		request.setAttribute("person", personInfo);		
		request.setAttribute("trans", transInfo);		
		return new ModelAndView("weixin/bgqb/h5/pay_quick");
	}
	
	@RequestMapping("/hftxnotify")
	@ResponseBody
	public void hftxNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("utf-8");		
		// 取得密文
        String sign =  request.getParameter("check_value");
        System.out.println("hftxnotify:"+sign);
        String extSeqId = transHFTXService.processNotify(sign);
		response.getWriter().print("RECV_ORD_ID_" + extSeqId);
	}
	
	@RequestMapping(params = "txscanPay")
	public ModelAndView txscanPay(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		String amt = request.getParameter("amt");
		String authCode = request.getParameter("authCode");
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		double d = Double.parseDouble(amt);
		String amount = decimalFormat.format(d);	
		if(accountService.checkOpenid(openid)){
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			String orderId = UtilData.createOnlyData();
			TransE1102Req req1102 = new TransE1102Req();			
//			req1103.setMemberId("310000016000762177");
//			jsonDataMap.put("termOrdId" , generateSeq(20));
//			jsonDataMap.put("ordAmt" , ordAmt);
//			jsonDataMap.put("authCode" , "134755413751742262");//二维码			
			req1102.setAuthCode(authCode);
			req1102.setTermOrdId(orderId);
			req1102.setOrdAmt(amount);			
			TransactionEntity entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			entity.setName(accountinfo.getName());
			entity.setIdcard(accountinfo.getIdcard());
			entity.setOrderno(orderId);
			int tranamt = (int)UtilData.multiply(d, 100d);
			entity.setTranamt(tranamt);
			TransE1102Rsp rsp = nsposService.transE1102(req1102,entity);			
			String respCode = rsp.getRespCode();			
			if("000000".equals(respCode)){
				request.setAttribute("status", "03");	
			}else{
				request.setAttribute("status", "02");	
			}			
			request.setAttribute("failureDetails", rsp.getRespDesc());
			request.setAttribute("orderno", orderId);
			return new ModelAndView("weixin/bgqb/h5/pay_result");
		}		
		return new ModelAndView("weixin/bgqb/h5/login");
	}

	@RequestMapping(params = "scanPay")
	public ModelAndView scanPay(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);		
		String payChannel = request.getParameter("payChannel");
		String tradeName = request.getParameter("tradeName");
		String authCode = request.getParameter("authCode");
		if("".equals(tradeName)||tradeName==null){
			tradeName="付款";
		}
		if(accountService.checkOpenid(openid)){
			String secMarchantId = accountService.getSecMerIdByOpenid(openid);	
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			PassiveScanPayReqDTO reqDTO = new PassiveScanPayReqDTO();	
//			setSecMerchantId//二级商户ID 可空
//			 * setAuthCode//扫码支付授权码，通过扫码枪等设备从用户微信/支付宝等APP读取的信息
//			 * setAmount//此次交易的具体金额,单位:分,不支持小数点
//			 * setTradeName//商品描述,简要概括此次交易的内容.可能会在用户App上显示
//			 * setBankNo//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
			String orderNo = UtilData.createOnlyData();			
			reqDTO.setOrderNo(orderNo);
			reqDTO.setReqNo(orderNo);
			reqDTO.setSecMerchantId(secMarchantId);
			reqDTO.setAmount(amount);			
			reqDTO.setBankNo(payChannelToKFTchannel(payChannel));			
			reqDTO.setTradeName(tradeName);
			reqDTO.setAuthCode(authCode);
			PassiveScanPayRespDTO resp = transKFTService.initiativePayBdsm(reqDTO,accountinfo);			
			String kftStatus = resp.getStatus();	
			String failureDetails  = resp.getFailureDetails();
			String status = transKFTService.ZDPayStatusToCode(kftStatus);
			request.setAttribute("status", status);		
			request.setAttribute("failureDetails", failureDetails);
			request.setAttribute("orderno", orderNo);
			return new ModelAndView("weixin/merchant/h5/pay_result");
		}		
		return new ModelAndView("weixin/merchant/h5/login");
	}
	
	@RequestMapping(params = "txqrcodepay")
	@ResponseBody
	public AjaxJson txqrCodePay(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		double d = Double.parseDouble(amt);
		String amount = decimalFormat.format(d);	
		if(accountService.checkOpenid(openid)){
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			String orderId = UtilData.createOnlyData();
			TransE1103Req req1103 = new TransE1103Req();			
//			req1103.setMemberId("310000016000762177");
			req1103.setTermOrdId(orderId);
			req1103.setOrdAmt(amount);			
			TransactionEntity entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			entity.setName(accountinfo.getName());
			entity.setIdcard(accountinfo.getIdcard());
			entity.setOrderno(orderId);
			int tranamt = (int)UtilData.multiply(d, 100d);
			entity.setTranamt(tranamt);
			TransE1103Rsp rsp = nsposService.transE1103(req1103,entity);			
			String respCode = rsp.getRespCode();			
			AjaxJson j = new AjaxJson();
			if("000000".equals(respCode)){
				j.setSuccess(true);
				j.setMsg(rsp.getQrcodeUrl());
			}else{
				j.setSuccess(false);
				j.setMsg(rsp.getRespDesc());
			}			
			Map<String, Object> map = new HashMap<String,Object>();
//			map.put("orderno", orderNo);
			j.setAttributes(map);
			return j;
		}
		return null;
	}
	
	
	@RequestMapping(params = "qrCodePay")
	@ResponseBody
	public AjaxJson qrCodePay(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);		
		String payChannel = request.getParameter("payChannel");
		String tradeName = request.getParameter("tradeName");
		if("".equals(tradeName)||tradeName==null){
			tradeName="付款";
		}
		if(accountService.checkOpenid(openid)){
			String secMarchantId = accountService.getSecMerIdByOpenid(openid);
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			ActiveScanPayReqDTO reqDTO = new ActiveScanPayReqDTO();	
//			setSecMerchantId//二级商户ID 可空
//			 * setAmount//此次交易的具体金额,单位:分,不支持小数点
//			 * setTradeName//商品描述,简要概括此次交易的内容.可能会在用户App上显示
//			 * setBankNo//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
			String orderNo = UtilData.createOnlyData();			
			reqDTO.setOrderNo(orderNo);
			reqDTO.setReqNo(orderNo);
			reqDTO.setSecMerchantId(secMarchantId);
			reqDTO.setAmount(amount);			
			reqDTO.setBankNo(payChannelToKFTchannel(payChannel));			
			reqDTO.setTradeName(tradeName);
			TransactionEntity entity = new TransactionEntity();
			entity.setDeviceid(accountinfo.getDeviceId());
			entity.setAccountid(accountinfo.getAccount());
			ActiveScanPayRespDTO resp = transKFTService.initiativePayZdsm(reqDTO,entity);
			AjaxJson j = new AjaxJson();
			String status = transKFTService.ZDPayStatusToCode(resp.getStatus());
			if("01".equals(status)||"03".equals(status)){
				j.setSuccess(true);
				j.setMsg(resp.getCodeUrl());
			}else{
				j.setSuccess(false);
				j.setMsg(resp.getFailureDetails());
			}			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("orderno", orderNo);
			j.setAttributes(map);
			return j;
		}
		return null;
	}
	@RequestMapping(params="qpapply")
	@ResponseBody
	public Object quickPayApply(HttpServletRequest request){
		String ip = request.getHeader(" x-forwarded-for ");
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader(" Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader(" WL-Proxy-Client-IP ");
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		System.out.println(ip);
		
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
		
		String bankno = request.getParameter("bankno");
		String name = request.getParameter("name");
		String idcard = request.getParameter("idcard");
		String bankcardno = request.getParameter("bankcardno");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String cvv = request.getParameter("cvv");
		String bankphone = request.getParameter("bankphone");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);
		SmsQuickPayApplyReqDTO reqDTO = new SmsQuickPayApplyReqDTO();
		String orderNo = UtilData.createOnlyData();	
		reqDTO.setReqNo(orderNo);//请求编号
		reqDTO.setSecMerchantId("2017121500092289");////////////////////////
		reqDTO.setOrderNo(orderNo);///////////交易编号 
		reqDTO.setTerminalIp(ip); //////////////APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP 
		reqDTO.setAmount(amount);//////////////////////此次交易的具体金额,单位:分,不支持小数点
		reqDTO.setTradeName("购买商品");/////////////商品描述,简要概括此次交易的内容.可能会在用户App上显示
		//reqDTO.setRemark(remark);//商品详情 可空
		reqDTO.setCustBankNo(bankno);//////////////////支付渠道  参考快付通银行类型参数
		reqDTO.setCustBankAccountNo(bankcardno);////////////////客户银行账户号 本次交易中,从客户的哪张卡上扣钱
		reqDTO.setCustBindPhoneNo(bankphone);///////////////持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		reqDTO.setCustName(name);/////////////////收钱钱的客户的真实姓名
//		reqDTO.setCustBankAcctType("1");//客户银行账户类型 1个人 2企业 可空
		reqDTO.setCustAccountCreditOrDebit("2");//////////////客户账户借记贷记类型 1借记 2贷记 3 未知		
		reqDTO.setCustCardValidDate(cardvaliddate);///////////////////如“0119”表示2019年1月份到期  可空
		reqDTO.setCustCardCvv2(cvv);////////////////客户信用卡的cvv2 信用卡的背面的三位数 可空		
		reqDTO.setCustID(idcard);//客户证件号码 与上述所选证件类型相匹配的证件号码
		reqDTO.setCustPhone(bankphone);//客户手机号 如果商户购买的产品中勾选了短信通知功能，则当商户填写了手机号时,快付通会在交易成功后通过短信通知客户
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		SmsQuickPayApplyRespDTO resp = transKFTService.smsQuickPayApply(reqDTO,entity);
		String status = transKFTService.QPayStatusToCode(resp.getStatus());
		Map<String, Object> msg = new HashMap<String, Object>();	
		if("01".equals(status)||"03".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			msg.put("orderno", orderNo);
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}
		return msg;
	}
	
	
	
	@RequestMapping(params="qpconfirm")
	@ResponseBody
	public Object quickPayConfirm(HttpServletRequest request){
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);	
		String bankphone = request.getParameter("bankphone");
		String orderNo = request.getParameter("orderno");
		String smsCode = request.getParameter("smscode");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);
		SmsQuickPayConfirmReqDTO reqDTO = new SmsQuickPayConfirmReqDTO();		
		reqDTO.setSecMerchantId("2017121500092289");
		reqDTO.setOrderNo(orderNo);//交易编号 
		reqDTO.setSmsCode(smsCode);//短信验证码
		reqDTO.setCustBindPhoneNo(bankphone);//持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setTranamt(Integer.parseInt(amount));
		SmsQuickPayConfirmRespDTO resp = transKFTService.smsQuickPayConfirm(reqDTO,entity);
		String status = transKFTService.QPayStatusToCode(resp.getStatus());
		Map<String, Object> msg = new HashMap<String, Object>();	
		if("01".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}
		return msg;
	}
	
	@RequestMapping(params="gbpqpapply")
	@ResponseBody
	public Object gbpQuickPayApply(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String accountId = request.getParameter("accountid");
		AccountInfo accountinfo = null;
		if(openid!=null&&!"".equals(openid)){
			accountinfo = accountService.getAccountByOpenid(openid);
		}else if(accountId!=null&&!"".equals(accountId)){
			accountinfo = accountService.getAccountByAccountId(accountId);
		}		
		String bankno = request.getParameter("bankno");
		String name = request.getParameter("name");
		String idcard = request.getParameter("idcard");
		String bankcardno = request.getParameter("bankcardno");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String cvv = request.getParameter("cvv");
		String bankphone = request.getParameter("bankphone");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setRoundingMode(RoundingMode.UP);
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);
		double ta = Double.parseDouble(amt)*100*accountinfo.getGbprate();
		String rateAmount = decimalFormat.format(ta);
		SameIDCreditCardCollectFromBankAccountDTO reqDTO = new SameIDCreditCardCollectFromBankAccountDTO();
		String orderNo = UtilData.createOnlyData();	
		reqDTO.setReqNo(orderNo);//请求编号
		reqDTO.setOrderNo(orderNo);///////////交易编号 
		reqDTO.setAmount(amount);//////////////////////此次交易的具体金额,单位:分,不支持小数点
		reqDTO.setCustBankNo(bankno);//////////////////支付渠道  参考快付通银行类型参数
		reqDTO.setCustBankAccountNo(bankcardno);////////////////客户银行账户号 本次交易中,从客户的哪张卡上扣钱
		reqDTO.setCustBindPhoneNo(bankphone);///////////////持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		reqDTO.setCustName(name);/////////////////收钱钱的客户的真实姓名
		reqDTO.setCustBankAcctType("1");//客户银行账户类型 1个人 2企业 可空
		reqDTO.setCustAccountCreditOrDebit("2");//////////////客户账户借记贷记类型 1借记 2贷记 3 未知
		reqDTO.setCustCardValidDate(cardvaliddate);///////////////////如“0119”表示2019年1月份到期  可空
		reqDTO.setCustCardCvv2(cvv);////////////////客户信用卡的cvv2 信用卡的背面的三位数 可空		
		reqDTO.setCustID(idcard);//客户证件号码 与上述所选证件类型相匹配的证件号码
		reqDTO.setCustPhone(bankphone);//客户手机号 如果商户购买的产品中勾选了短信通知功能，则当商户填写了手机号时,快付通会在交易成功后通过短信通知客户		
		reqDTO.setRateAmount(rateAmount);
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setRate(accountinfo.getGbprate());
		entity.setFee(accountinfo.getGbpfee());
		String transMerchantId = transKFTService.selectGbpTransMerchantId(bankno);
		SameIDCreditCardTradeResultDTO resp = transKFTService.sameIDSmsCollect(reqDTO,entity,transMerchantId);
		String status = transKFTService.QPayStatusToCode(resp.getStatus()+"");
		Map<String, Object> msg = new HashMap<String, Object>();	
		if("01".equals(status)||"03".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			msg.put("orderno", orderNo);
			msg.put("ramt", rateAmount);
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}
		return msg;
	}
	
	@RequestMapping(params="gbptreatyapply")
	@ResponseBody
	public Object gbpTreatyApply(HttpServletRequest request){
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
		String bankno = request.getParameter("bankno");
		String name = request.getParameter("name");
		String idcard = request.getParameter("idcard");
		String bankcardno = request.getParameter("bankcardno");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String cvv = request.getParameter("cvv");
		String bankphone = request.getParameter("bankphone");
		System.out.println(bankcardno);
		String treatyId = accountService.findBankCardTreatyId(bankcardno);
		System.out.println("treatyId："+treatyId);
		Map<String, Object> msg = new HashMap<String, Object>();	
		if(treatyId!=null&&"".equals(treatyId)){
			return msg.put(Constants.FAIL_TEXT, "该卡已添加,请关闭页面重试");
		}
		TreatyApplyDTO reqDTO = new TreatyApplyDTO();				
		String orderNo = UtilData.createOnlyData();	
		reqDTO.setReqNo(orderNo);//请求编号
		reqDTO.setOrderNo(orderNo);///////////交易编号 
		reqDTO.setBankType(bankno);//////////////////支付渠道  参考快付通银行类型参数
		reqDTO.setBankCardNo(bankcardno);////////////////客户银行账户号 本次交易中,从客户的哪张卡上扣钱
		reqDTO.setMobileNo(bankphone);///////////////持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		reqDTO.setHolderName(name);/////////////////收钱钱的客户的真实姓名
		reqDTO.setCertificateNo(idcard);
		reqDTO.setCustCardValidDate(cardvaliddate);///////////////////如“0119”表示2019年1月份到期  可空
		reqDTO.setCustCardCvv2(cvv);////////////////客户信用卡的cvv2 信用卡的背面的三位数 可空
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		String transMerchantId = transKFTService.selectGbpTransMerchantId(bankno);
		TreatyApplyResultDTO resp = transKFTService.treatyCollectApply(reqDTO,entity,transMerchantId);
		String status = transKFTService.QPayStatusToCode(resp.getStatus()+"");
		
		if("01".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			msg.put("orderno", orderNo);
			msg.put("smsseq", resp.getSmsSeq());
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}
		return msg;
	}
	/**
	 * 信用卡代还回信用卡
	 * @param request
	 * @return
	 */
	@RequestMapping(params="gbptreatyconfirm")
	@ResponseBody
	public Object gbpTreatyConfirm(HttpServletRequest request){
		System.out.println("============gbptreatyconfirm");
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);	
		Map<String,Object> bindBandCard = accountService.queryCashCardInfo(accountinfo.getAccount(),"02");
		String orderNo = request.getParameter("orderno");
		String smsCode = request.getParameter("smscode");		
		String smsSeq = request.getParameter("smsseq");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String cvv = request.getParameter("cvv");
		String bankNo = request.getParameter("bankno");
		String bankCardNo = request.getParameter("bankcardno");	
		String bankPhone = request.getParameter("bankphone");
		String name = (String)bindBandCard.get("NAME");
		String idcard = (String)bindBandCard.get("IDCARD");			
		Map<String, Object> msg = new HashMap<String, Object>();			
		TransactionEntity entity  = null;
		String status = null;
		/*******************快捷协议确认***********************************/
		//借记卡认证成功后代扣
		TreatyConfirmDTO dto = new TreatyConfirmDTO();		
		String reqNo = UtilData.createOnlyData();			
		dto.setReqNo(reqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户
		dto.setOrderNo(orderNo);// 同快捷代扣申请订单号一致
		dto.setSmsSeq(smsSeq);// 协议代扣申请返回的短信流水号
		dto.setAuthCode(smsCode);// 短信验证码
		dto.setHolderName(name);// 持卡人姓名，与申请时一致
		dto.setBankCardNo(bankCardNo);// 银行卡号，与申请时一致	
		dto.setCustCardValidDate(cardvaliddate);
		dto.setCustCardCvv2(cvv);
		entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setIdcard(idcard);
		entity.setBankno(bankNo);
		entity.setBankphone(bankPhone);
		String transMerchantId = transKFTService.selectGbpTransMerchantId(bankNo);
		TreatyConfirmResultDTO resp = transKFTService.confirmTreatyCollectApply(dto,entity,transMerchantId);
		status = transKFTService.QPayStatusToCode(resp.getStatus()+"");			
		if("01".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			String treatyId = resp.getTreatyId();
			accountService.insertTreatyCardInfo(accountinfo.getAccount(), name, idcard, bankNo, bankCardNo, bankPhone,cvv,cardvaliddate, treatyId);
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}	
		return msg;
	}
	
	/**
	 * 套现快捷确认
	 * @param request
	 * @return
	 */
	@RequestMapping(params="gbpqpconfirm")
	@ResponseBody
	public Object gbpQuickPayConfirm(HttpServletRequest request){		
		String openid = request.getParameter("openid");		
		String accountId = request.getParameter("accountid");
		AccountInfo accountinfo = null;
		if(openid!=null&&!"".equals(openid)){
			accountinfo = accountService.getAccountByOpenid(openid);
		}else if(accountId!=null&&!"".equals(accountId)){
			accountinfo = accountService.getAccountByAccountId(accountId);
		}	
		Map<String,Object> bindBandCard = accountService.queryCashCardInfo(accountinfo.getAccount(), "01");
		String vertify = (String)bindBandCard.get("VERTIFY");
		String bankno = (String)bindBandCard.get("BANKNO");
		String name = (String)bindBandCard.get("NAME");
		String idcard = (String)bindBandCard.get("IDCARD");
		String bankCardNo = (String)bindBandCard.get("BANKCARDNO");
		String bankCardType = (String)bindBandCard.get("BANKCARDTYPE");
       	String pbankno = request.getParameter("pbankno");
		String pbankcardno = request.getParameter("pbankcardno");
		String cvv = request.getParameter("cvv");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String bankphone = request.getParameter("bankphone");
		String orderNo = request.getParameter("orderno");
		String smsCode = request.getParameter("smscode");
		String amt = request.getParameter("amt");
		String ramt = request.getParameter("ramt");
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);
		Map<String, Object> msg = new HashMap<String, Object>();	
		String transMerchantId = transKFTService.selectGbpTransMerchantId(pbankno);
		/*****************借记卡三要素认证*************************/
		if("0".equals(vertify)){
			//没有认证过先认证
			String reqNo1 = UtilData.createOnlyData();	
			BankCardDetailDTO bDTO = new BankCardDetailDTO();
			bDTO.setReqNo(reqNo1);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
			bDTO.setOrderNo(reqNo1);// 订单号同一个商户必须保证唯一
			bDTO.setCustName(name);// 客户姓名		
			bDTO.setCustID(idcard);// 客户证件号码
			bDTO.setCustBankNo(bankno);// 银行卡行别
			bDTO.setCustBankAccountNo(bankCardNo);// 需要验证的银行卡号
			bDTO.setCustAccountCreditOrDebit("1");// 客户账户借记贷记类型,0存折 1借记 2贷记
			TransactionEntity entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			SameIDCreditCardTradeResultDTO breq = transKFTService.threeMessageVerify(bDTO,entity,transMerchantId);
			String status = transKFTService.QPayStatusToCode(breq.getStatus()+"");
			if("01".equals(status)){
				//三要素认证成功
				transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "1","01");
			}else{
				//认证失败
				transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "2","01");
				msg.put(Constants.FAIL_TEXT, "绑定储蓄卡信息有误请检查后,再操作");
				return msg;
			}
		}else if("2".equals(vertify)){
			msg.put(Constants.FAIL_TEXT, "绑定储蓄卡信息有误请检查后,再操作");
			return msg;
		}
		/*******************信用卡代扣***********************************/
		//借记卡认证成功后代扣
		SameIDCreditCardSmsCollectConfirmDTO dto = new SameIDCreditCardSmsCollectConfirmDTO();		
		String reqNo = UtilData.createOnlyData();	
		dto.setReqNo(reqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户
		dto.setOrderNo(orderNo);// 同快捷代扣申请订单号一致
		dto.setSmsCode(smsCode);// 短信验证码
		dto.setCustBindPhoneNo(bankphone);// 手机号	
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setTranamt(Integer.parseInt(amount));
		entity.setName(name);
		entity.setIdcard(idcard);
		entity.setBankno(pbankno);
		entity.setBankcardno(pbankcardno);
		entity.setRate(accountinfo.getGbprate());
		entity.setFee(accountinfo.getGbpfee());
		SameIDCreditCardTradeResultDTO resp = transKFTService.sameIDSmsCollectConfirm(dto,entity,transMerchantId);
		String status = transKFTService.QPayStatusToCode(resp.getStatus()+"");
		if("01".equals(status)){
			/*********************代付********************************/
			SameIDCreditCardPayToBankAccountDTO pDTO = new SameIDCreditCardPayToBankAccountDTO();
			String preqNo = UtilData.createOnlyData();
			pDTO.setReqNo(preqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
			pDTO.setOrderNo(preqNo);// 订单号同一个商户必须保证唯一
			pDTO.setTradeName("费用支付");// 简要概括此次交易的内容					
			String pamt = Integer.parseInt(amount)-Integer.parseInt(ramt)+"";
			pDTO.setAmount(pamt);// 单位:分,不支持小数点				
			pDTO.setCustBankNo(bankno);// 客户银行账户行别;快付通定义的行别号,详情请看文档
			pDTO.setCustBankAccountNo(bankCardNo);// 本次交易中,从客户的哪张卡上扣钱
			pDTO.setCustName(name);// 付款人的真实姓名		
			pDTO.setCustID(idcard);// 证件号码
			pDTO.setRateAmount(accountinfo.getGbpfee()+"");
			if("02".equals(bankCardType)){
				pDTO.setCustAccountCreditOrDebit("2");
				pDTO.setCustCardValidDate(cardvaliddate);
				pDTO.setCustCardCvv2(cvv);
			}else{
				pDTO.setCustAccountCreditOrDebit("1");
			}			
			entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			SameIDCreditCardTradeResultDTO preq = transKFTService.sameIDGbpPay(pDTO, entity,transMerchantId);
			status = transKFTService.QPayStatusToCode(preq.getStatus()+"");
			if("01".equals(status)){
				msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			}else{
				msg.put(Constants.FAIL_TEXT, preq.getFailureDetails());
			}
		}else{
			msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
		}		
		return msg;
	}
	/**
	 * 协议快捷套现回借记卡
	 * @param request
	 * @return
	 */
	@RequestMapping(params="gbptreatyconfirmpay")
	@ResponseBody
	public Object gbpTreatyConfirmAndPay(HttpServletRequest request){		
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);	
		Map<String,Object> bindBandCard = accountService.queryCashCardInfo(accountinfo.getAccount(),"01");
		String orderNo = request.getParameter("orderno");
		String smsCode = request.getParameter("smscode");
		String amt = request.getParameter("amt");		
		String smsSeq = request.getParameter("smsseq");
		String cardvaliddate = request.getParameter("cardvaliddate");
		String cvv = request.getParameter("cvv");
		String pBankNo = request.getParameter("pbankno");
		String pBankCardNo = request.getParameter("pbankcardno");	
		String pBankPhone = request.getParameter("pbankphone");
		String vertify = (String)bindBandCard.get("VERTIFY");
		String bankno = (String)bindBandCard.get("BANKNO");
		String name = (String)bindBandCard.get("NAME");
		String idcard = (String)bindBandCard.get("IDCARD");
		String bankCardNo = (String)bindBandCard.get("BANKCARDNO");		
		String transMerchantId = transKFTService.selectGbpTransMerchantId(pBankNo);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setRoundingMode(RoundingMode.UP);
		String amount = decimalFormat.format(Double.parseDouble(amt)*100);
		double ta = Double.parseDouble(amt)*100*accountinfo.getGbprate();
		String rateAmount = decimalFormat.format(ta);
		Map<String, Object> msg = new HashMap<String, Object>();	
		/*****************借记卡三要素认证*************************/
		if("0".equals(vertify)){
			//没有认证过先认证
			String reqNo1 = UtilData.createOnlyData();	
			BankCardDetailDTO bDTO = new BankCardDetailDTO();
			bDTO.setReqNo(reqNo1);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
			bDTO.setOrderNo(reqNo1);// 订单号同一个商户必须保证唯一
			bDTO.setCustName(name);// 客户姓名		
			bDTO.setCustID(idcard);// 客户证件号码
			bDTO.setCustBankNo(bankno);// 银行卡行别
			bDTO.setCustBankAccountNo(bankCardNo);// 需要验证的银行卡号
			bDTO.setCustAccountCreditOrDebit("1");// 客户账户借记贷记类型,0存折 1借记 2贷记
			TransactionEntity entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			SameIDCreditCardTradeResultDTO breq = transKFTService.threeMessageVerify(bDTO,entity,transMerchantId);
			String status = transKFTService.QPayStatusToCode(breq.getStatus()+"");
			if("01".equals(status)){
				//三要素认证成功
				transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "1","01");
			}else{
				//认证失败
				transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "2","01");
				msg.put(Constants.FAIL_TEXT, "绑定储蓄卡信息有误请检查后,再操作");
				return msg;
			}
		}else if("2".equals(vertify)){
			msg.put(Constants.FAIL_TEXT, "绑定储蓄卡信息有误请检查后,再操作");
			return msg;
		}
		//查看该卡是否有协议ID
		String treatyId = accountService.findBankCardTreatyId(pBankCardNo);
		TransactionEntity entity  = null;
		String status = null;
		if(treatyId==null||"".equals(treatyId)){
			/*******************快捷协议确认***********************************/
			//借记卡认证成功后代扣
			TreatyConfirmDTO dto = new TreatyConfirmDTO();		
			String reqNo = UtilData.createOnlyData();			
			dto.setReqNo(reqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户
			dto.setOrderNo(orderNo);// 同快捷代扣申请订单号一致
			dto.setSmsSeq(smsSeq);// 协议代扣申请返回的短信流水号
			dto.setAuthCode(smsCode);// 短信验证码
			dto.setHolderName(name);// 持卡人姓名，与申请时一致
			dto.setBankCardNo(pBankCardNo);// 银行卡号，与申请时一致	
			dto.setCustCardValidDate(cardvaliddate);
			dto.setCustCardCvv2(cvv);
			entity = new TransactionEntity();
			entity.setAccountid(accountinfo.getAccount());
			entity.setDeviceid(openid);
			entity.setIdcard(idcard);
			entity.setBankno(pBankNo);
			entity.setBankphone(pBankPhone);
			TreatyConfirmResultDTO resp = transKFTService.confirmTreatyCollectApply(dto,entity,transMerchantId);
			status = transKFTService.QPayStatusToCode(resp.getStatus()+"");
			if(!"01".equals(status)){
				msg.put(Constants.FAIL_TEXT, resp.getFailureDetails());
				return msg;
			}	
			treatyId = resp.getTreatyId();
			accountService.insertTreatyCardInfo(accountinfo.getAccount(), name, idcard, pBankNo, pBankCardNo, pBankPhone,cvv,cardvaliddate, treatyId);
		}					
		/*****************快捷协议代扣***********************/
		SameIDCreditCardTreatyCollectDTO tcDto = new SameIDCreditCardTreatyCollectDTO();
		String tcReqNo = UtilData.createOnlyData();				
		tcDto.setReqNo(tcReqNo);
		tcDto.setOrderNo(tcReqNo);// 订单号同一个商户必须保证唯一
		tcDto.setTreatyNo(treatyId);// 协议代扣申请确认返回的协议号	
		tcDto.setAmount(amount);// 此次交易的具体金额,单位:分,不支持小数点		
		tcDto.setHolderName(name);// 持卡人姓名，与申请时一致
		tcDto.setBankType(pBankNo);// 客户银行账户行别;快付通定义的行别号,详情请看文档
		tcDto.setBankCardNo(pBankCardNo);// 银行卡号，与申请时一致，本次交易中,从客户的哪张卡上扣钱
		tcDto.setCustCardValidDate(cardvaliddate);//可空，信用卡扣款时必填
		tcDto.setCustCardCvv2(cvv);//可空，信用卡扣款时必填
		tcDto.setRateAmount(rateAmount);// 手续费			
		entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setTranamt(Integer.parseInt(amount));
		entity.setIdcard(idcard);
		entity.setBankphone(pBankPhone);
		SameIDCreditCardTreatyCollectResultDTO tcresp = transKFTService.sameIDCreditCardTreatyCollect(tcDto, entity,transMerchantId);
		status = transKFTService.QPayStatusToCode(tcresp.getStatus()+"");
		if(!"01".equals(status)){
			msg.put(Constants.FAIL_TEXT, tcresp.getFailureDetails());
			return msg;
		}			
		/*********************代付********************************/
		SameIDCreditCardPayToBankAccountDTO pDTO = new SameIDCreditCardPayToBankAccountDTO();
		String preqNo = UtilData.createOnlyData();
		pDTO.setReqNo(preqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
		pDTO.setOrderNo(preqNo);// 订单号同一个商户必须保证唯一
		pDTO.setTradeName("费用支付");// 简要概括此次交易的内容				
		String pamt = Integer.parseInt(amount)-Integer.parseInt(rateAmount)+"";
		pDTO.setAmount(pamt);// 单位:分,不支持小数点				
		pDTO.setCustBankNo(bankno);// 客户银行账户行别;快付通定义的行别号,详情请看文档
		pDTO.setCustBankAccountNo(bankCardNo);// 本次交易中,从客户的哪张卡上扣钱
		pDTO.setCustName(name);// 付款人的真实姓名		
		pDTO.setCustID(idcard);// 证件号码
		pDTO.setRateAmount(accountinfo.getGbpfee()+"");
		entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		SameIDCreditCardTradeResultDTO preq = transKFTService.sameIDGbpPay(pDTO, entity,transMerchantId);
		status = transKFTService.QPayStatusToCode(preq.getStatus()+"");
		if("01".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}else{
			msg.put(Constants.FAIL_TEXT, preq.getFailureDetails());
		}	
		return msg;
	}	
	@RequestMapping(params="queryOrder")
	@ResponseBody
	public AjaxJson queryOrder(@RequestParam String orderno){		
		TransactionEntity en = transBaseService.queryOrder(orderno);
		String status = en.getStatus();		
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setStatus(status);
		if(en.getNotifycontent()!=null&&!"".equals(en.getNotifycontent())){
			String errorMsg = JSONObject.parseObject(en.getNotifycontent()).getString("respMsg");	
			j.setMsg(errorMsg);			
		}		
		return j;
	}
	/**
	 * 
	 @RequestMapping(params="queryOrder")
	@ResponseBody
	public AjaxJson queryOrder(@RequestParam String orderno){		
		TransactionEntity en = transBaseService.queryOrder(orderno);
		String status = en.getStatus();
		String errorMsg = JSONObject.parseObject(en.getRespcontent()).getString("failureDetails");
		if("03".equals(status)){
			TradeQueryReqDTO reqDTO = new TradeQueryReqDTO();
			reqDTO.setReqNo(System.currentTimeMillis()+"");
			reqDTO.setOriginalOrderNo(orderno);
			TradeQueryRespDTO resp = transKFTService.initiativePayQuery(reqDTO);
			logger.info("queryOrder req:"+JSONObject.toJSON(reqDTO));
			logger.info("queryOrder resp:"+JSONObject.toJSON(resp));
			status = transKFTService.ZDPayStatusToCode(resp.getStatus());
			if("02".equals(status)){
				errorMsg="原因："+resp.getFailureDetails();
			}
		}
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setStatus(status);
		j.setMsg(errorMsg);			
		return j;
	}
	 */
	
	@RequestMapping(params = "goResult")
	public ModelAndView goResult(@RequestParam String orderno,@RequestParam String status,HttpServletRequest request) {
		request.setAttribute("status", status);			
		request.setAttribute("orderno", orderno);
		return new ModelAndView("weixin/merchant/h5/pay_result");
	}
	
	@RequestMapping(params = "addMerchant")
	@ResponseBody
	public AjaxJson settledSecMerchant(HttpServletRequest request) {
		SettledSecMerchantRequestDTO reqDTO = new SettledSecMerchantRequestDTO();
		String merid = request.getParameter("merid");
		Map<String,Object> m = merchantService.getMerchantInfo(merid);	
		System.out.println(m);
		// 1. 将商户资料压缩为一个zip文件
		// 2. 上传文件到sftp
		// 3. 调用商户入驻接口
		//商户全称
		String secMerchantName = (String)m.get("SECMERCHANTNAME");
		//商户简称
		String shortMerchName = (String)m.get("SHORTNAME");
		//所在省
		//String province = "0058";
		//所有市
		//String city = "5840";
		//所有地
		String district = (String)m.get("DISTRICT");
		//地址
		String address = (String)m.get("ADDRESS");
		//法人姓名
		String legalName = (String)m.get("LEGALNAME");
		//联系人姓名
		String contactName = (String)m.get("CONTACTNAME");
		//联系电话
		String contactPhone = (String)m.get("CONTACTPHONE");
		//联系邮箱
		String contactEmail = (String)m.get("CONTACTEMAIL");
		//商户类型：1：个人、2：企业、3：个体工商户、4：事业单位
		String merchantProperty = (String)m.get("MERCHANTPROPERTY");
		//商户属性 1实体特约商户 2 网络特约商户 3 实体兼网络特约商户    个人商户时必须是2或3
		String merchantAttribute = (String)m.get("MERCHANTARRTIBUTE");
		if("2".equals(merchantAttribute)||"3".equals(merchantAttribute)){
			//ICP备 个人商户可以添平台商的
			String icpRecord = (String)m.get("ICPRECORD");
			//服务器IP
			String serviceIp = (String)m.get("SERVICEIP");
			//网址
			String companyWebUrl = (String)m.get("COMPANYURL");
			reqDTO.setIcpRecord(icpRecord);
			reqDTO.setServiceIp(serviceIp);
			reqDTO.setCompanyWebUrl(companyWebUrl);
		}		
		//经营类目
		String category = (String)m.get("CATEGORY");
		//业务场景说明
		String businessScene = (String)m.get("BUSINESSSCENE");
		//ftp文件地址
		String certPath = (String)m.get("FILEPATH");
		//文件签名
		//String certDigest = md5AndBase64("D:\\20171215123027156.zip");
		//商户账户信息
		String settleBankAccount = "{\"settleAccountCreditOrDebit\":\"1\","//账户借记贷记类型 1借记
			+ "\"settleBankAccountNo\":\""+(String)m.get("SETTLEBANKACCOUNTNO")+"\","//账户账号
			+ "\"settleBankAcctType\":\""+(String)m.get("SETTLEBANKACCTTYPE")+"\","//银行账户类型1个人2企业
			+ "\"settleBankNo\":\""+(String)m.get("SETTLEBANKNO")+"\","//账户所属银行
			+ "\"settleName\":\""+(String)m.get("SETTLENAME")+"\"}";//银行账户户名
		
		// 二级商户开通的产品只能为一级商户已开通的产品的全部或部分，不能包含一级商户未开通的产品
		String productFees = "[";
		int index = 0;
		if(m.get("WXRATE")!=null){
			index++;
			productFees+="{\"feeOfAttach\":\"0\",\"feeOfRate\":\""+(String)m.get("WXRATE")+"\",\"feeType\":\"3\",\"productId\":\"010101\"}";//微信扫码
		}
		if(m.get("WXGZHRATE")!=null){
			if(index>0){
				productFees+=",";
			}
			index++;
			productFees+= "{\"feeOfAttach\":\"0\",\"feeOfRate\":\""+(String)m.get("WXGZHRATE")+"\",\"feeType\":\"3\",\"productId\":\"010102\"}";//微信公众号
		}
		if(m.get("ZFBRATE")!=null){
			if(index>0){
				productFees+=",";
			}
			index++;
			productFees+="{\"feeOfAttach\":\"0\",\"feeOfRate\":\""+(String)m.get("ZFBRATE")+"\",\"feeType\":\"3\",\"productId\":\"010201\"}";//支付宝扫码
		}
		if((String)m.get("YLRATE")!=null){
			if(index>0){
				productFees+=",";
			}
			index++;
			productFees+= "{\"feeOfAttach\":\"0\",\"feeOfRate\":\""+(String)m.get("YLRATE")+"\",\"feeType\":\"3\",\"productId\":\"010301\"}";//银联扫码
		}
		if(m.get("QPRATE")!=null){
			if(index>0){
				productFees+=",";
			}
			index++;
			productFees+= "{\"feeOfAttach\":\"0\",\"feeOfRate\":\""+(String)m.get("QPRATE")+"\",\"feeType\":\"3\",\"productId\":\"010501\"}";//快捷支付
		}
		productFees+= "]";
		if(merchantProperty.equals("1")){
			//个人
			String personCertInfo = "[{certType:0,"//证件类型：0身份证
				+ "certNo:"+(String)m.get("CERTNO")+","//证件号
				+ "certValidDate:"+(String)m.get("CERTVALIDDATE")+"}]";//证件有效期
			reqDTO.setPersonCertInfo(personCertInfo);
		}else{
			//个体户+企业 
			String corpCertInfo = "[{\"certNo\":\""+(String)m.get("CERTNO")+"\",\"certType\":\"0\",\"certValidDate\":\""+(String)m.get("CERTVALIDDATE")+"\"},"//身份证
				+ "{\"certNo\":\""+(String)m.get("LICENSENO")+"\",\"certType\":\"Y\",\"certValidDate\":\""+(String)m.get("LICENSEVALIDDATE")+"\"}]";//营业执照
			reqDTO.setCorpCertInfo(corpCertInfo);
		}
		
		
		reqDTO.setSecMerchantName(secMerchantName);
		reqDTO.setShortName(shortMerchName);
		//reqDTO.setProvince(province);
		//reqDTO.setCity(city);
		reqDTO.setDistrict(district);
		reqDTO.setAddress(address);
		reqDTO.setLegalName(legalName);
		reqDTO.setContactName(contactName);
		reqDTO.setContactPhone(contactPhone);
		reqDTO.setContactEmail(contactEmail);
		reqDTO.setMerchantProperty(merchantProperty);
		reqDTO.setMerchantAttribute(merchantAttribute);		
		reqDTO.setCategory(category);
		reqDTO.setBusinessScene(businessScene);
		reqDTO.setCertPath(certPath);
		reqDTO.setSettleBankAccount(settleBankAccount);
		reqDTO.setProductFees(productFees);
		transKFTService.settledSecMerchant(reqDTO);	
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);		
		return j;
	}
	@RequestMapping(params = "queryMer")
	@ResponseBody
	public Object querySecMerchant(HttpServletRequest request){		
		QuerySecMerchantRequestDTO reqDTO = new QuerySecMerchantRequestDTO();
		reqDTO.setCertNo(request.getParameter("certNo"));
		reqDTO.setMerchantProperty(request.getParameter("merchantProperty"));
		
		QuerySecMerchantResponseDTO resp = transKFTService.querySecMerchant(reqDTO);		
		
		Map<String, Object> msg = new HashMap<String, Object>();
		
		msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		msg.put(Constants.RESULT_CONTENT, JSONObject.toJSONString(resp));	
		return msg;
	}
	
	private String payChannelToKFTchannel(String payChannel){
		String kftcode = null;
		if(PARA_CONSTATNTS.PayChannel_WX.equals(payChannel)){
			kftcode = "0000001";
		}else if(PARA_CONSTATNTS.PayChannel_ZFB.equals(payChannel)){
			kftcode = "0000002";
		}else if(PARA_CONSTATNTS.PayChannel_YL.equals(payChannel)){
			kftcode = "0000003";
		}
		return kftcode;
	}
	/**
	 * 汇付天下同名卡支付申请
	 * @param request
	 * @return
	 */
	@RequestMapping(params="gbphftxpayapply")
	@ResponseBody
	public Object gbphftxPayApply(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String accountId = request.getParameter("accountid");
		AccountInfo accountinfo = null;
		if(openid!=null&&!"".equals(openid)){
			accountinfo = accountService.getAccountByOpenid(openid);
		}else if(accountId!=null&&!"".equals(accountId)){
			accountinfo = accountService.getAccountByAccountId(accountId);
		}
		String bankno = request.getParameter("bankcode");
		String bankcardno = request.getParameter("bankcardno");
		String bankphone = request.getParameter("bankphone");
		String amt = request.getParameter("amt");
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		amt = decimalFormat.format(Double.parseDouble(amt));
		Map<String,Object> row = accountService.findChannelCustIdAndAcctId(accountinfo.getAccount(),PARA_CONSTATNTS.Channel_HFTX);
		String custId = (String)row.get("CUSTID");
		String custAcctId = (String)row.get("ACCTID");
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setRate(accountinfo.getGbprate());
		entity.setFee(accountinfo.getGbpfee());
		entity.setName(accountinfo.getName());
		entity.setIdcard(accountinfo.getIdcard());
		entity.setBankcardno(bankcardno);
		entity.setBankphone(bankphone);
		TransRsp rsp = transBaseService.gbpPayApply(accountinfo,custId,custAcctId, bankcardno, bankphone, amt,entity);
		Map<String, Object> msg = new HashMap<String, Object>();
		if(!rsp.getCode().equals("02")){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			Map<String,Object> params = rsp.getParams();
			msg.put("orderno", params.get("orderno"));
			msg.put("orderdate", params.get("orderdate"));
			msg.put("bindcardid", params.get("bindcardid"));
		}else{
			msg.put(Constants.FAIL_TEXT, rsp.getMsg());
		}
		return msg;
	}
	
	/**
	 * 汇付天下同名卡支付确认
	 * @param request
	 * @return
	 */
	@RequestMapping(params="gbphftxpayconfirm")
	@ResponseBody
	public Object gbphftxPayConfirm(HttpServletRequest request){		
		String openid = request.getParameter("openid");
		String accountId = request.getParameter("accountid");
		AccountInfo accountinfo = null;
		if(openid!=null&&!"".equals(openid)){
			accountinfo = accountService.getAccountByOpenid(openid);
		}else if(accountId!=null&&!"".equals(accountId)){
			accountinfo = accountService.getAccountByAccountId(accountId);
		}
		Map<String,Object> row = accountService.findChannelCustIdAndAcctId(accountinfo.getAccount(),PARA_CONSTATNTS.Channel_HFTX);
		String custId = (String)row.get("CUSTID");
		String orderno = request.getParameter("orderno");
		String orderDate = request.getParameter("orderdate");
		String smscode = request.getParameter("smscode");
		String bindcardid = request.getParameter("bindcardid");
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		entity.setRate(accountinfo.getGbprate());
		entity.setFee(accountinfo.getGbpfee());
		entity.setName(accountinfo.getName());
		entity.setIdcard(accountinfo.getIdcard());
		TransRsp rsp = transBaseService.gbpPayConfirm(bindcardid,custId, orderno,orderDate,smscode,openid,accountId);
		Map<String, Object> msg = new HashMap<String, Object>();
		if(!rsp.getCode().equals("02")){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			Map<String,Object> params = rsp.getParams();
			System.out.println("url===================="+params.get("url"));
			msg.put("url", params.get("url"));
			msg.put("cmd_id", params.get("cmd_id"));
			msg.put("version", params.get("version"));
			msg.put("mer_cust_id", params.get("mer_cust_id"));
			msg.put("check_value", params.get("check_value"));
			
		}else{
			msg.put(Constants.FAIL_TEXT, rsp.getMsg());
		}
		return msg;
	}
	
	/**
	 * 余额提现
	 * @param request
	 * @return
	 */
	@RequestMapping(params="balancetocash")
	@ResponseBody
	public Object balanceToCash(HttpServletRequest request){		
		String openid = request.getParameter("openid");
		String accountId = request.getParameter("accountid");
		if(accountId==null||"".equals(accountId)){
			AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
			accountId = accountInfo.getAccount();
		}
		double amt = transBaseService.balanceToCash(accountId,openid);
		Map<String, Object> msg = new HashMap<String, Object>();
		if(amt>0){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}else{
			msg.put(Constants.FAIL_TEXT, "请查询余额再取现");
		}
		return msg;
	}
	
	
	/**
	 * kft手动操作
	 * @param request
	 * @return
	 */
	@RequestMapping(params="kft")
	@ResponseBody
	public Object kft(HttpServletRequest request){		
		String accountId = request.getParameter("accountid");
		AccountInfo accountinfo = null;
		accountinfo = accountService.getAccountByAccountId(accountId);
		Map<String,Object> bindBandCard = accountService.queryCashCardInfo(accountinfo.getAccount(), "01");
		String bankno = (String)bindBandCard.get("BANKNO");
		String name = (String)bindBandCard.get("NAME");
		String idcard = (String)bindBandCard.get("IDCARD");
		String bankCardNo = (String)bindBandCard.get("BANKCARDNO");
		String bankCardType = (String)bindBandCard.get("BANKCARDTYPE");
       	String pbankno = request.getParameter("pbankno");
		String pamt = request.getParameter("pamt");
		String cd = request.getParameter("cd");
		Map<String, Object> msg = new HashMap<String, Object>();	
		String transMerchantId = transKFTService.selectGbpTransMerchantId(pbankno);
		
		
		String reqNo1 = UtilData.createOnlyData();	
		BankCardDetailDTO bDTO = new BankCardDetailDTO();
		bDTO.setReqNo(reqNo1);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
		bDTO.setOrderNo(reqNo1);// 订单号同一个商户必须保证唯一
		bDTO.setCustName(name);// 客户姓名		
		bDTO.setCustID(idcard);// 客户证件号码
		bDTO.setCustBankNo(bankno);// 银行卡行别
		bDTO.setCustBankAccountNo(bankCardNo);// 需要验证的银行卡号
		bDTO.setCustAccountCreditOrDebit(cd);// 客户账户借记贷记类型,0存折 1借记 2贷记
		TransactionEntity entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
//		entity.setDeviceid(openid);
		SameIDCreditCardTradeResultDTO breq = transKFTService.threeMessageVerify(bDTO,entity,transMerchantId);
		String status = transKFTService.QPayStatusToCode(breq.getStatus()+"");
		if("01".equals(status)){
			//三要素认证成功
			transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "1","01");
		}else{
			//认证失败
			transBaseService.updateThreeVertify(name, idcard, bankno, bankCardNo, "2","01");
			msg.put(Constants.FAIL_TEXT, "绑定储蓄卡信息有误请检查后,再操作");
			return msg;
		}
		SameIDCreditCardPayToBankAccountDTO pDTO = new SameIDCreditCardPayToBankAccountDTO();
		String preqNo = UtilData.createOnlyData();
		pDTO.setReqNo(preqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
		pDTO.setOrderNo(preqNo);// 订单号同一个商户必须保证唯一
		pDTO.setTradeName("费用支付");// 简要概括此次交易的内容	
		pDTO.setAmount(pamt);// 单位:分,不支持小数点				
		pDTO.setCustBankNo(bankno);// 客户银行账户行别;快付通定义的行别号,详情请看文档
		pDTO.setCustBankAccountNo(bankCardNo);// 本次交易中,从客户的哪张卡上扣钱
		pDTO.setCustName(name);// 付款人的真实姓名		
		pDTO.setCustID(idcard);// 证件号码
		pDTO.setRateAmount(accountinfo.getGbpfee()+"");
		
		pDTO.setCustAccountCreditOrDebit(cd);
				
		entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
//		entity.setDeviceid(openid);
		SameIDCreditCardTradeResultDTO preq = transKFTService.sameIDGbpPay(pDTO, entity,transMerchantId);
		status = transKFTService.QPayStatusToCode(preq.getStatus()+"");
		if("01".equals(status)){
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}else{
			msg.put(Constants.FAIL_TEXT, preq.getFailureDetails());
		}
		return msg;
	}

	public static void main(String[] arg){
		String t = "115.252";
		double a = Double.parseDouble(t);
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
//		decimalFormat.setRoundingMode(RoundingMode.UP);
		String b = decimalFormat.format(a);
		System.out.println(b);
	}
}
