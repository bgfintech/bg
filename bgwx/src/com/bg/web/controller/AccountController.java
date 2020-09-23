package com.bg.web.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DataUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.util.WeixinUtil;
import weixin.util.Constants;

import com.alibaba.fastjson.JSON;
import com.bg.interfaces.entity.SendSmsReq;
import com.bg.interfaces.entity.SendSmsRsp;
import com.bg.interfaces.service.ISmsService;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.UtilData;
import com.bg.web.entity.AccountInfo;
import com.bg.web.entity.MerchantInfo;
import com.bg.web.entity.TransInfo;
import com.bg.web.service.AccountServiceI;
import com.bg.web.service.IAgentService;
import com.bg.web.service.IQRCodeService;
import com.bg.web.service.MerchantServiceI;

@Controller
@RequestMapping("/acctController")
public class AccountController {
	private static Logger logger = Logger.getLogger(AccountController.class);
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AccountServiceI accountService;
	@Autowired
	private ITransKFTService transKFTService;
	@Autowired
	private MerchantServiceI merService;
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private IQRCodeService QRCodeService;
	@Autowired
	private ISmsService aliyunSmsService;
	@Autowired
	private IAgentService agentService;
	
	
	@RequestMapping(params = "merlist")
	public ModelAndView merlist(HttpServletRequest request) {
		return new ModelAndView("weixin/bgqb/merchantList");
	}
	
	
	
	
	/**
	 * 扫码支付进入页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goscanpay")
	public ModelAndView goScanPay(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		String payChannel = request.getParameter("payChannel");
		request.setAttribute("payChannel", payChannel);
		WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
		if (openid == null || "".equals(openid)) {
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");	
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);
		request.setAttribute("state", weixin_accountid);
		request.setAttribute("source", "goscanpay");
		if (accountService.checkOpenid(openid)) {
			// 根据openid取得merid
			String merid = accountService.getMerIdByOpenid(openid);
			LogUtil.info("------------------商户ID------------" + merid);
			if (merid == null) {// 跳转绑定商户页面
				return new ModelAndView("weixin/bgqb/h5/nopermission");
			}			
			JSONObject jsonObject = JSONObject.fromObject(weixinAccountService.getJsApiInfo(weixinAount,systemService,request.getRequestURL() + "?"+ request.getQueryString()));
			request.setAttribute("nonceStr", jsonObject.getString("nonceStr"));
			request.setAttribute("timestamp", jsonObject.getString("timestamp"));
			request.setAttribute("signature", jsonObject.getString("signature"));
			request.setAttribute("appid", weixinAount.getAccountappid());
			return new ModelAndView("weixin/bgqb/h5/pay_scan");
		}
		
		return new ModelAndView("weixin/bgqb/h5/login");
	}

	/**
	 * 二维码支付请求
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goqrcodepay")
	public ModelAndView goQRCodePay(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		String payChannel = request.getParameter("payChannel");
		request.setAttribute("payChannel", payChannel);
		WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
		if (openid == null || "".equals(openid)) {
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");	
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);
		request.setAttribute("source", "goqrcodepay");
		if (accountService.checkOpenid(openid)) {
			// 根据openid取得merid
			String merid = accountService.getMerIdByOpenid(openid);
			LogUtil.info("------------------商户ID------------" + merid);
			if (merid == null) {// 跳转绑定商户页面
				return new ModelAndView("weixin/bgqb/h5/nopermission");
			}
			return new ModelAndView("weixin/bgqb/h5/pay_qrcode");
		}		
		
		return new ModelAndView("weixin/bgqb/h5/login");
	}

	/**
	 * 个人中心页面跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gopersonal")
	public ModelAndView goPersonal(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		String accountId = request.getParameter("accountid");		
		AccountInfo accountInfo = null;		
		if ((openid == null || "".equals(openid))&&code!=null&&!"".equals(code)) {
			WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");		
				accountInfo = accountService.getAccountByOpenid(openid);	
			}else{
				request.setAttribute("wclose", "1");
			}					
		}else if(accountId!=null){
			accountInfo = accountService.getAccountByAccountId(accountId);
		}else if(openid!=null&&!"".equals(openid)){
			accountInfo = accountService.getAccountByOpenid(openid);
		}
		request.setAttribute("source", "gopersonal");	
		request.setAttribute("openid", openid);
		if (accountInfo!=null) {
			double balance = accountService.findAccountBalance(accountInfo.getAccount());
			DecimalFormat df = new DecimalFormat("0.00");
			request.setAttribute("balance", df.format(balance==-1?0:balance));
			Map<String,Object> personInfo = accountService.queryCashCardInfo(accountInfo.getAccount(),"01");
			String bankcardno = (String)personInfo.get("BANKCARDNO");
			String tbankcardno = "";
			if(bankcardno!=null&&!"".equals(bankcardno)){
				tbankcardno = bankcardno.substring(0,3)+"*********"+bankcardno.substring(bankcardno.length()-3);
			}			
			personInfo.put("TBANKCARDNO", tbankcardno);			
			String bankphone = (String)personInfo.get("BANKPHONE");
			String tbankphone = "";
			if(bankphone!=null&&!"".equals(bankphone)){
				tbankphone = "********"+bankphone.substring(bankphone.length()-3);
			}			
			personInfo.put("TBANKPHONE", tbankphone);			
			request.setAttribute("accountinfo", accountInfo);
			request.setAttribute("personinfo", personInfo);
			if(personInfo.get("BANKNO")!=null){
				String bankno = (String)personInfo.get("BANKNO");
				String bankname = transBaseService.bankno2name(bankno);
				personInfo.put("BANKNAME", bankname);
			}	
			double rate = accountInfo.getGbprate();
			if(rate!=0){
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String amount = decimalFormat.format(accountInfo.getGbpfee()*0.01);		
				String sr = UtilData.multiply(rate, 100)+"%+"+amount;
				personInfo.put("GBPSTR", sr);
			}
			return new ModelAndView("weixin/bgqb/h5/persional");
		}		
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			System.out.println("recommend========"+recMap.get("ACCOUNT"));
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	/**
	 * 商户交易明细列表页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gotrans")
	public ModelAndView goTrans(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
//		String payChannel = request.getParameter("payChannel");
//		request.setAttribute("payChannel", payChannel);		
		if (openid == null || "".equals(openid)) {
			WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");			
			}else{
				request.setAttribute("wclose", "1");
			}	
		}
		request.setAttribute("openid", openid);
		request.setAttribute("source", "gotrans");
		if (accountService.checkOpenid(openid)) {
			String secmerid = accountService.getSecMerIdByOpenid(openid);
			if(secmerid==null){
				return new ModelAndView("weixin/bgqb/h5/merchant_bind");
			}			
			List<TransactionEntity> trans = transBaseService.queryMerchantOrder(secmerid, null);
			List<TransInfo> transinfos = new ArrayList<TransInfo>();
			if (trans != null && trans.size() > 0) {
				SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat date_sdf_wz2 = new SimpleDateFormat("HH点mm分");
				for (TransactionEntity tran : trans) {
					TransInfo transinfo = new TransInfo();
					transinfo.setOrderNo(tran.getOrderno());
					Date output = DataUtils.str2Date(tran.getTranstime(),yyyymmddhhmmss);
					int d = tran.getTranamt();
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String amount = decimalFormat.format(d/100);	
					transinfo.setAmt(amount);
					transinfo.setYymmdd(DataUtils.date2Str(output, date_sdf_wz));
					transinfo.setMmss(DataUtils.date2Str(output, date_sdf_wz2));
					if (tran.getTrantype().equals("1001")) {
						transinfo.setStatus("客户主扫");
					}
					if (tran.getTrantype().equals("1002")) {
						transinfo.setStatus("客户被扫");
					}
					if (tran.getTrantype().equals("1003")) {
						transinfo.setStatus("支付查询");
					}
					if (tran.getTrantype().equals("1004")) {
						transinfo.setStatus("退款");
					}
					if (tran.getTrantype().equals("1005")) {
						transinfo.setStatus("商户入驻");
					}
					if (tran.getTrantype().equals("1006")) {
						transinfo.setStatus("商户查询");
					}
					transinfos.add(transinfo);
				}
			}
			request.setAttribute("trans", transinfos);
			return new ModelAndView("weixin/bgqb/h5/receiving_list");
		}
		request.setAttribute("gopage", "gotrans");
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	/**
	 * 个人快捷交易明细列表页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gopaytrans")
	public ModelAndView goPayTrans(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		if (openid == null || "".equals(openid)) {
			WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");			
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);
		request.setAttribute("source", "goqpaytrans");
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			List<TransactionEntity> trans = transBaseService.queryPayOrder(accountinfo.getAccount(), null);
			List<TransInfo> transinfos = new ArrayList<TransInfo>();
			if (trans != null && trans.size() > 0) {
				SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat date_sdf_wz2 = new SimpleDateFormat("HH点mm分");
				for (TransactionEntity tran : trans) {
					TransInfo transinfo = new TransInfo();
					transinfo.setOrderNo(tran.getOrderno());
					Date output = DataUtils.str2Date(tran.getTranstime(),yyyymmddhhmmss);
					int d = tran.getTranamt();
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String amount = decimalFormat.format(d/100d);	
					transinfo.setAmt(amount);
					transinfo.setYymmdd(DataUtils.date2Str(output, date_sdf_wz));
					transinfo.setMmss(DataUtils.date2Str(output, date_sdf_wz2));
					if (tran.getTrantype().equals("1001")) {
						transinfo.setStatus("客户主扫");
					}else if (tran.getTrantype().equals("1002")) {
						transinfo.setStatus("客户被扫");
					}else if(tran.getTrantype().equals("1010")||"1015".equals(tran.getTrantype())) {
						transinfo.setStatus("快捷支付");
					}
					transinfos.add(transinfo);
				}
			}
			request.setAttribute("trans", transinfos);
			return new ModelAndView("weixin/bgqb/h5/qpaytrans_list");
		}
		request.setAttribute("gopage", "goqpaytrans");
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	/**
	 * Ajax快捷交易列表时间查询
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "querypaytrans")
	@ResponseBody
	public Object queryPayTrans(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String qDate = request.getParameter("qdate");
		System.out.println(qDate);
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
		Map<String, Object> msg = new HashMap<String, Object>();
		List<TransactionEntity> trans = transBaseService.queryPayOrder(accountinfo.getAccount(), qDate);
		List<TransInfo> transinfos = new ArrayList<TransInfo>();
		if (trans != null && trans.size() > 0) {
			SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat date_sdf_wz2 = new SimpleDateFormat("HH点mm分");
			for (TransactionEntity tran : trans) {
				TransInfo transinfo = new TransInfo();
				transinfo.setOrderNo(tran.getOrderno());
				Date output = DataUtils.str2Date(tran.getTranstime(),yyyymmddhhmmss);
				int d = tran.getTranamt();
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String amount = decimalFormat.format(d/100d);	
				transinfo.setAmt(amount);
				transinfo.setYymmdd(DataUtils.date2Str(output, date_sdf_wz));
				transinfo.setMmss(DataUtils.date2Str(output, date_sdf_wz2));
				
				if (tran.getTrantype().equals("1001")) {
					transinfo.setStatus("客户主扫");
				}else if (tran.getTrantype().equals("1002")) {
					transinfo.setStatus("客户被扫");
				}else if(tran.getTrantype().equals("1010")) {
					transinfo.setStatus("快捷支付");
				}
				
				transinfos.add(transinfo);
			}			
		}
		msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		msg.put(Constants.RESULT_CONTENT, transinfos);		
		return msg;
	}
	

	
	/**
	 * 交易详情页
	 * 
	 * @param request
	 * @return
	*/
	@RequestMapping(params = "showtrans")
	public ModelAndView showTrans(HttpServletRequest request) {
		String orderno = request.getParameter("orderno");
		String openid = request.getParameter("openid");
		if (accountService.checkOpenid(openid)) {
			TransactionEntity transEntity = transKFTService.queryTransByOrderNo(orderno);
			TransInfo transInfo = new TransInfo();
			transInfo.setOrderNo(transEntity.getOrderno());
			int d = transEntity.getTranamt();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			String amount = decimalFormat.format(d/100);	
			transInfo.setAmt(amount);
			SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
			Date output = DataUtils.str2Date(transEntity.getTranstime(),yyyymmddhhmmss);
			SimpleDateFormat yyyy_mm_dd_hh_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			transInfo.setTransTime(DataUtils.date2Str(output,yyyy_mm_dd_hh_mm_ss));
			transInfo.setPayChannel(transEntity.getPaychannel());
			if(transEntity.getPaychannel().equals("01")) {				
				transInfo.setPayChannelName("微信支付");
			}else if(transEntity.getPaychannel().equals("02")) {
				transInfo.setPayChannelName("支付宝支付");
			}else if(transEntity.getPaychannel().equals("03")) {
				transInfo.setPayChannelName("银联支付");
			}else if(transEntity.getPaychannel().equals("04")) {
				transInfo.setPayChannelName("快捷支付");
			}
			if(transEntity.getTrantype().equals("1001")) {
				transInfo.setTrantype("客户主扫");
			}else if(transEntity.getTrantype().equals("1002")) {
				transInfo.setTrantype("客户被扫");
			}else if(transEntity.getTrantype().equals("1010")) {
				transInfo.setTrantype("快捷支付");
			}
			if(transEntity.getStatus().equals("01")) {
				transInfo.setStatus("支付成功");
			}else if(transEntity.getStatus().equals("02")) {
				transInfo.setStatus("支付失败");
			}else if(transEntity.getStatus().equals("03")) {
				transInfo.setStatus("支付中");
			}
			request.setAttribute("tran", transInfo);
			return new ModelAndView("weixin/bgqb/h5/trans_detail");
		}
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");

	} 
	/**
	 * 我要刷卡 收款
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goquickpay")
	public ModelAndView quickPay(HttpServletRequest request){
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");		
		if (openid == null || "".equals(openid)) {
			WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");			
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);		
		request.setAttribute("source", "goquickpay");
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
			List<Map<String,Object>> cards = accountService.queryGbpBankCardInfo(accountInfo.getAccount(),PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
			Map<String,Object> personInfo = accountService.queryCashCardInfo(accountInfo.getAccount(),"01");
			Map<String,Object> transInfo = accountService.queryLastGbpTransInfo(accountInfo.getAccount(),PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
//			Map<String,Object> trans = new HashMap<String,Object>();
//			if(cards!=null&&cards.size()>0){
//				trans=cards.get(0);
//				for(Map<String,Object> m: cards){
//					String bco = (String)m.get("BANKCARDNO");
//					m.put("TBCO", bco.substring(bco.length()-4));
//				}
//			}
			if(transInfo!=null){
				String bankno = (String)transInfo.get("BANKNO");
				String bankname = transBaseService.bankno2name(bankno);
				transInfo.put("BANKNAME", bankname);
			}else{
				transInfo = new HashMap<String,Object>();
				transInfo.put("BANKNAME", "---");
			}			
			String cbco = (String)personInfo.get("BANKCARDNO");
			String tcbco = "";
			if(cbco!=null&&!"".equals(cbco)){
				tcbco = cbco.substring(0,3)+"**********"+cbco.substring(cbco.length()-3);
			}			
			String idcard = accountInfo.getIdcard();
			String tidcard = "";
			if(idcard!=null&&!"".equals(idcard)){
				tidcard = idcard.substring(0,5)+"********"+idcard.substring(idcard.length()-3);
			}
			/**
			String isDeposit = "0";
			String isActivate = accountInfo.getIsactivate();
			String activateType = accountInfo.getActivatetype();
			if("0".equals(isActivate)&&"01".equals(activateType)){
				isDeposit = "1";//需要首刷押金
			}
			request.setAttribute("isDeposit", isDeposit);*/
			request.setAttribute("accountinfo", accountInfo);
			request.setAttribute("tidcard", tidcard);
			request.setAttribute("tcbankcardno", tcbco);
			request.setAttribute("person", personInfo);		
			request.setAttribute("cards", JSON.toJSONString(cards));
			request.setAttribute("trans", transInfo);
			if (accountService.checkOpenid(openid)) {
				return new ModelAndView("weixin/bgqb/h5/pay_quick");
			}
		}
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	@RequestMapping(params = "gotreatyquickpay")
	public ModelAndView goTreatyQuickPay(HttpServletRequest request){
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");		
		if (openid == null || "".equals(openid)) {
			WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");			
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);		
		request.setAttribute("source", "goquickpay");
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
			Map<String,Object> personInfo = accountService.queryCashCardInfo(accountInfo.getAccount(),"01");
			Map<String,Object> transInfo = accountService.queryLastGbpTransInfo(accountInfo.getAccount(),PARA_CONSTATNTS.TransCode_GbpTreatyConfirm);
			String treatyId = null;
			if(transInfo!=null){
				String bankno = (String)transInfo.get("BANKNO");
				String bankname = transBaseService.bankno2name(bankno);
				transInfo.put("BANKNAME", bankname);
				treatyId=accountService.findBankCardTreatyId((String)transInfo.get("BANKCARDNO"));
			}else{
				transInfo = new HashMap<String,Object>();
				transInfo.put("BANKNAME", "---");
			}			
			String cbco = (String)personInfo.get("BANKCARDNO");
			String tcbco = "";
			if(cbco!=null&&!"".equals(cbco)){
				tcbco = cbco.substring(0,3)+"**********"+cbco.substring(cbco.length()-3);
			}			
			String idcard = accountInfo.getIdcard();
			String tidcard = "";
			if(idcard!=null&&!"".equals(idcard)){
				tidcard = idcard.substring(0,5)+"********"+idcard.substring(idcard.length()-3);
			}	
			request.setAttribute("tidcard", tidcard);
			request.setAttribute("tcbankcardno", tcbco);
			request.setAttribute("person", personInfo);		
			request.setAttribute("trans", transInfo);	
			request.setAttribute("treatyid", treatyId);
			if (accountService.checkOpenid(openid)) {
				return new ModelAndView("weixin/bgqb/h5/pay_treatyquick");
			}
		}
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	

	/**
	 * 商户登录
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "login")
	@ResponseBody
	public AjaxJson login(HttpServletRequest req) {
		String openid = req.getParameter("openid");
		String user = req.getParameter("user");
		String passwd = req.getParameter("passwd");
		AjaxJson j = new AjaxJson();
		String pwd = PasswordUtil.encrypt(user, passwd, PasswordUtil.getStaticSalt());
	    if (accountService.checkAccount(user, pwd)) {
	    	boolean bool = accountService.isAccountByDevice(user,openid);
	    	if(!bool){
	    		accountService.createAccountDevice(user,openid);
	    	}			
			j.setSuccess(true);
			j.setMsg("登录成功");
		}else{
			j.setSuccess(false);
			j.setMsg("账号或密码错");
		}
	    return j;
	}
	/**
	 * 商户注册 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		String openid = (String) request.getParameter("openid");
		String source = (String) request.getParameter("source");
		LogUtil.info("---------openid:" + openid);
		AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
		accountService.logout(accountInfo.getAccount(),openid);
		request.setAttribute("openid", openid);
		request.setAttribute("source", source);
		request.setAttribute("recommend", accountInfo.getRecommender());
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	/**
	 * 商户注册 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "gologin")
	public ModelAndView gologin(HttpServletRequest request) {
		String openid = (String) request.getParameter("openid");
		String source = (String) request.getParameter("source");
		LogUtil.info("---------openid:" + openid);
		LogUtil.info("---------source:" + source);
		request.setAttribute("openid", openid);
		request.setAttribute("source", source);
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	/**
	 * 修改密码页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "goeditpasswd")
	public ModelAndView goEditPassword(HttpServletRequest request) {
		String openid = (String) request.getParameter("openid");		
		request.setAttribute("openid", openid);
		return new ModelAndView("weixin/bgqb/h5/editpasswd");
	}
	/**
	 * 修改密码
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "editpasswd")
	@ResponseBody
	public Object editPassword(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		String password = PasswordUtil.encrypt(accountinfo.getAccount(), oldpwd, PasswordUtil.getStaticSalt());
		System.out.println(password);
		Map<String, Object> msg = new HashMap<String, Object>();
	    if (accountService.checkAccount(accountinfo.getAccount(), password)) {
	    	String npwd = PasswordUtil.encrypt(accountinfo.getAccount(), newpwd, PasswordUtil.getStaticSalt());
	    	accountService.updatePassword(accountinfo.getAccount(), npwd);    	
	    	msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}else{
			msg.put(Constants.FAIL_TEXT,"原密码不正确");
		}
	    
		return msg;
	}
	/**
	 * 账号注册信息
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "regist")
	@ResponseBody
	public AjaxJson regist(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String account = req.getParameter("user");
		String passwd = req.getParameter("passwd");
		String openid = req.getParameter("openid");
		String recommend = req.getParameter("recommend");
		String smscode = req.getParameter("smscode");
		if(openid==null){
			j.setSuccess(false);
			j.setMsg("设备标识取得失败");
			return j;
		}
		String ycode = (String)req.getSession().getAttribute(account);
		//校码验证码
		if(smscode==null||!smscode.equals(ycode)){
			j.setSuccess(false);
			j.setMsg("验证码错误");
			return j;
		}
		//检查账号是否存在
		if(accountService.checkAccount(account)){//前端提示账号已存在
			j.setSuccess(false);
			j.setMsg("该账号已存在");
			return j;
		}
		//检查推荐人是否存在
		if(!accountService.checkAccount(recommend)){
			j.setSuccess(false);
			j.setMsg("推荐人手机号错误");
			return j;
		}
		try{			
			String password = PasswordUtil.encrypt(account, passwd, PasswordUtil.getStaticSalt());
			accountService.createAccountAndDevice(account,password,openid,recommend);
			j.setSuccess(true);
			j.setMsg("注册成功");		
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			System.out.println(e);
			j.setSuccess(false);
			j.setMsg("注册失败");
		}		
		return j;
	}
	/**
	 * 绑卡页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "gobindbankcard")
	public ModelAndView goBindBankCard(HttpServletRequest request) {
		String openid = (String) request.getParameter("openid");	
		AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
		request.setAttribute("isvalid", accountInfo.getIscertified());
		request.setAttribute("accountinfo", accountInfo);
		request.setAttribute("openid", openid);
		return new ModelAndView("weixin/bgqb/h5/bindbankcard");
	}
	
	
	
	/**
	 * 
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "bindbankcard")
	@ResponseBody
	public Object bindBankCard(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		String accountid = request.getParameter("accountid");
		String name = request.getParameter("name");
		String bankno = request.getParameter("bankno");
		String idcard = request.getParameter("idcard");
		String bankCardno = request.getParameter("bankcardno");
		String bankPhone = request.getParameter("bankphone");
		AccountInfo accountinfo = null;
		if(accountid!=null&&!"".equals(accountid)){
			accountinfo = accountService.getAccountByAccountId(accountid);
		}else{
			accountinfo = accountService.getAccountByOpenid(openid);
		}		
		String accountId = accountinfo.getAccount();
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			accountService.addCustomerAndBankCard(accountId,name,idcard,bankno,bankCardno,bankPhone);
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		}catch(Exception e){
			System.out.println(e);
			logger.error(e.getMessage(),e);
			msg.put(Constants.FAIL_TEXT,"绑卡失败");
		}		
		return msg;
	}
	
	
	
	/**
	 * 绑卡hftx
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
//	@RequestMapping(params = "bindbankcard")
//	@ResponseBody
//	public Object bindBankCard(HttpServletRequest request) {
//		String openid = request.getParameter("openid");
//		String accountid = request.getParameter("accountid");
//		String name = request.getParameter("name");
//		String bankno = request.getParameter("bankno");
//		String idcard = request.getParameter("idcard");
//		String bankCardno = request.getParameter("bankcardno");
//		String bankPhone = request.getParameter("bankphone");
//		String prov = request.getParameter("prov");
//		String area = request.getParameter("area");
//		String iscertified = request.getParameter("iscertified");
//		Map<String, Object> msg = new HashMap<String, Object>();
//		try{
//			String custId;
//			String acctId;
//			TransactionEntity entity = new TransactionEntity();
//			entity.setAccountid(accountid);
//			entity.setDeviceid(openid);
//			idcard = idcard.toUpperCase();
//			/****如果第三方没有开户先开户************************************/
//			if("0".equals(iscertified)){
//				//判断该身份证号是否已绑定
//				Map<String,Object> tmpm = accountService.findAccountByIdcard(idcard);
//				if(tmpm!=null){
//					msg.put(Constants.FAIL_TEXT,"该身份证已绑定,请重新核对或联系客服");
//					return msg;
//				}				
//				//第三方开户
//				TransRsp rsp = transBaseService.createTransAccount(name, idcard, bankPhone, prov, area, entity);
//				if(!rsp.getCode().equals("01")){
//					msg.put(Constants.FAIL_TEXT,rsp.getMsg());
//					return msg;
//				}
//				Map<String,Object> params = rsp.getParams();
//				custId = (String)params.get("custid");
//				acctId = (String)params.get("acctid");
//				accountService.updateAccountInfo(accountid, name, idcard, "1",custId,acctId,PARA_CONSTATNTS.Channel_HFTX);
//			}else{
//				Map<String,Object> row = accountService.findChannelCustIdAndAcctId(accountid,PARA_CONSTATNTS.Channel_HFTX);
//				custId =(String) row.get("CUSTID");
//				acctId =(String) row.get("ACCTID");
//			}
//			/****判断绑定的卡之前是否做过绑定及是否已解绑************************************/
//			Map<String,Object> cardMap = accountService.findBankCardInfo(accountid, bankCardno, bankno, "01", name, idcard);
//			if(cardMap==null){
//				entity = new TransactionEntity();
//				entity.setName(name);
//				entity.setIdcard(idcard);
//				entity.setBankphone(bankPhone);
//				entity.setAccountid(accountid);
//				entity.setDeviceid(openid);
//				TransRsp rsp = transBaseService.bindCashCard(custId, bankCardno, bankno, prov, area, entity);
//				if(rsp.getCode().equals("01")){
//					String bankcardtype="01";
//					Map<String,Object> ps = rsp.getParams();
//					String bindCardId=(String)ps.get("bindCardId");
//					accountService.addCashCard(accountid,name,idcard,bankno,bankCardno,bankPhone,bankcardtype,bindCardId);
//					msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
//				}else{
//					msg.put(Constants.FAIL_TEXT, rsp.getMsg());
//				}
//			}else{
//				//之前该卡已绑卡且没有解绑
//				accountService.changeCashCard(accountid, name, idcard, bankno, bankCardno);
//				msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
//			}			
//		}catch(Exception e){
//			System.out.println(e);
//			logger.error(e.getMessage(),e);
//			msg.put(Constants.FAIL_TEXT,"绑卡失败");
//		}		
//		return msg;
//	}
	/**
	 * 朋友圈分享
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goshare")
	public ModelAndView goshare(HttpServletRequest request){
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		//String qrCodeType = request.getParameter("qrcodetype");
		WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);
		if (openid == null || "".equals(openid)) {			
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");			
			}else{
				request.setAttribute("wclose", "1");
			}
		}
		request.setAttribute("openid", openid);			
		if (accountService.checkOpenid(openid)) {
			if(weixin_accountid!=null&&!"".equals(weixin_accountid)){
				JSONObject jsonObject = JSONObject.fromObject(weixinAccountService.getJsApiInfo(weixinAount,systemService,request.getRequestURL() + "?"+ request.getQueryString()));
				request.setAttribute("nonceStr", jsonObject.getString("nonceStr"));
				request.setAttribute("timestamp", jsonObject.getString("timestamp"));
				request.setAttribute("signature", jsonObject.getString("signature"));
				request.setAttribute("appid", weixinAount.getAccountappid());			
			}			
			AccountInfo account = accountService.getAccountByOpenid(openid);
			request.setAttribute("accountinfo", account);
			//String qrurl = QRCodeService.getQRCodeUrl(account,qrCodeType);
			String qrurl = QRCodeService.getQRCodeUrl(account);
			request.setAttribute("qrurl", qrurl);			
			return new ModelAndView("weixin/bgqb/h5/share");
		}
		request.setAttribute("state", weixin_accountid);
		request.setAttribute("source", "goshare");
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	
	
	/**
	 * 分享页面
	 * @param req
	 * @return
	 
	@RequestMapping(params = "share")
	public ModelAndView share(HttpServletRequest request) {
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);		
		if(accountinfo!=null){
			try{
				accountService.createShare(accountinfo);				
			}catch(Exception e){
				System.out.println(e);
				logger.error(e.getMessage(),e);
			}	
		}			
		return new ModelAndView("weixin/bgqb/h5/share");
	}*/
	/**
	 * 我的团队
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goteam")
	public ModelAndView goTeam(HttpServletRequest request) {
		String code = request.getParameter("code");
		String weixin_accountid = request.getParameter("state");
		String openid = request.getParameter("openid");
		String headimgurl = request.getParameter("headimgurl");
		WeixinAccountEntity weixinAount = weixinAccountService.findByToUsername(weixin_accountid);	
		if (openid == null || "".equals(openid)) {
			String code2token_url = WeixinUtil.code2accesstoken_url.replace("APPID", weixinAount.getAccountappid()).replace("SECRET", weixinAount.getAccountappsecret()).replace("CODE", code);
			JSONObject json = WeixinUtil.httpRequest(code2token_url, "GET", "");
			if(json.has("openid")){
				openid = json.getString("openid");	
				String token = json.getString("access_token");
				String getuserinfo_url = WeixinUtil.getuserinfo_url.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
				json = WeixinUtil.httpRequest(getuserinfo_url, "GET", "");
				headimgurl = json.getString("headimgurl");
				request.setAttribute("headimgurl", headimgurl);
			}else{
				request.setAttribute("wclose", "1");
			}			
		}		
		request.setAttribute("openid", openid);		
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			Calendar c = Calendar.getInstance();  
			c.add(Calendar.MONTH, 0);  
			c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天  			
			c.set(Calendar.HOUR_OF_DAY, 0);  //将小时至0  			
			c.set(Calendar.MINUTE, 0);  //将分钟至0  			
			c.set(Calendar.SECOND,0);  //将秒至0  			
			c.set(Calendar.MILLISECOND, 0);  //将毫秒至0  
			Date date = c.getTime();
			int transAmt = accountService.countTransAmtByAccountTeam(accountinfo.getAccount(), date);
			List<Map<String,Object>> tlist = accountService.findTeamByAccount(accountinfo.getAccount());
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			String amount = decimalFormat.format(transAmt/100);	
			int num = 0;
			for(Map<String,Object> m:tlist){
				num+=1;
				num+=(long)m.get("CNUM");
				String account = (String)m.get("SUBACCOUNT");
				String tmp = "********";
				String ta = tmp+account.substring(account.length()-4, account.length());
				m.put("TACCOUNT", ta);
			}
			String accountId = accountinfo.getAccount();
			String ta = "********"+accountId.substring(accountId.length()-3);			
			request.setAttribute("taccount", ta);			
			request.setAttribute("accountinfo", accountinfo);
			request.setAttribute("tlist", tlist);
			request.setAttribute("tnum", num);
			request.setAttribute("transamt", amount);
			return new ModelAndView("weixin/bgqb/h5/goteam");
		}
		request.setAttribute("source", "goteam");
		Map<String,Object> recMap = accountService.findSubscribeRecommender(openid);
		if(recMap!=null){
			request.setAttribute("recommend",(String)recMap.get("ACCOUNT"));
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	/**
	 * 我要办卡
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "applicationcard")
	public ModelAndView applicationCard(HttpServletRequest request) {		
		return new ModelAndView("weixin/bgqb/h5/applicationcard");
	}
	/**
	 * 找回密码
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goretrievepasswd")
	public ModelAndView retrievePasswd(HttpServletRequest request) {		
		return new ModelAndView("weixin/bgqb/h5/retrievepasswd");
	}
	/**
	 * 找回密码发短信验证码
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "getpwdsmscode")
	@ResponseBody
	public Object getPasswdSmsCode(HttpServletRequest request) {		
		String account = request.getParameter("account");		
		SendSmsReq req = new SendSmsReq();
		req.setPhone(account);
		req.setSignName("布广钱包");
		req.setTemplateId("SMS_149390237");
		String code = String.valueOf((new Random().nextInt(8999) + 1000));
		Map<String,String> param = new HashMap<String,String>();
		param.put("code", code);
		req.setTemplateParam(param);		
		SendSmsRsp rsp=null;
		Map<String, Object> msg = new HashMap<String, Object>();
		//检查账号是否存在
		if(!accountService.checkAccount(account)){//前端提示账号已存在
			msg.put(Constants.FAIL_TEXT, "该账号不存在");
			return msg;
		}		
		try {
			System.out.println("phone=========:"+account+":"+code);
			rsp = aliyunSmsService.sendSms(req);	
			System.out.println("rspcode:"+rsp.getRespCode()+"::::"+rsp.getMsg()+"::::"+rsp.getOrderId());
			if(rsp.getRespCode().equals("00")){
				msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
				request.getSession().setAttribute(account, code);
			}else{
				msg.put(Constants.FAIL_TEXT, rsp.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			msg.put(Constants.FAIL_TEXT, rsp.getMsg());
		}
		return msg;
	}
	/**
	 * 找回密码更换密码
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "changepwdbysmscode")
	@ResponseBody
	public Object changePwdBySmscode(HttpServletRequest request) {		
		String account = request.getParameter("account");		
		String pwd = request.getParameter("passwd");
		String smscode = request.getParameter("smscode");		
		Map<String, Object> msg = new HashMap<String, Object>();
		//检查账号是否存在
		if(!accountService.checkAccount(account)){//前端提示账号已存在
			msg.put(Constants.FAIL_TEXT, "该账号不存在");
			return msg;
		}
		String ycode = (String)request.getSession().getAttribute(account);
		System.out.println("sc:"+smscode);
		System.out.println("yc:"+ycode);
		//校码验证码
		if(smscode==null||!smscode.equals(ycode)){
			msg.put(Constants.FAIL_TEXT, "手机或验证码不正确");
			return msg;
		}
		String npwd = PasswordUtil.encrypt(account, pwd, PasswordUtil.getStaticSalt());
    	accountService.updatePassword(account, npwd);    	
		msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);		
		return msg;
	}
	
	/**
	 * 获取验证码
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "getsmscode")
	@ResponseBody
	public Object getSmsCode(HttpServletRequest request) {		
		String user = request.getParameter("user");		
		String recommend = request.getParameter("recommend");	
		SendSmsReq req = new SendSmsReq();
		req.setPhone(user);
		req.setSignName("布广钱包");
		req.setTemplateId("SMS_143700959");
		String code = String.valueOf((new Random().nextInt(8999) + 1000));
		Map<String,String> param = new HashMap<String,String>();
		param.put("code", code);
		req.setTemplateParam(param);		
		SendSmsRsp rsp=null;
		Map<String, Object> msg = new HashMap<String, Object>();
		//检查账号是否存在
		if(accountService.checkAccount(user)){//前端提示账号已存在
			msg.put(Constants.FAIL_TEXT, "该账号已存在");
			return msg;
		}
		//检查是否有推荐人
		if(recommend==null||"".equals(recommend)||!accountService.checkAccount(recommend)){
			msg.put(Constants.FAIL_TEXT, "请输入正确的推荐人手机号");
			return msg;
		}
		try {
			System.out.println("phone=========:"+user+":"+code);
			rsp = aliyunSmsService.sendSms(req);			
			if(rsp.getRespCode().equals("00")){
				msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
				request.getSession().setAttribute(user, code);
			}else{
				msg.put(Constants.FAIL_TEXT, rsp.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			msg.put(Constants.FAIL_TEXT, rsp.getMsg());
		}
		return msg;
	}
	
	@RequestMapping(params = "golegalaccount")
	public ModelAndView goLegalAccount(HttpServletRequest request) {		
		return new ModelAndView("shesu/golegalaccount");
	}
	
	@RequestMapping(params = "createaccount")
	@ResponseBody
	public Object createAccount(HttpServletRequest request) {		
		String accountid = request.getParameter("accountid");		
		String entstat = request.getParameter("entstat");
		String entout = request.getParameter("entout");
		double es = Double.parseDouble(entstat);
		double eo = Double.parseDouble(entout);
		accountService.createLegalAccount(accountid, es, eo);		
		Map<String, Object> msg = new HashMap<String, Object>();		
		msg.put("resp_code", "00");		
		return msg;
	}
	@RequestMapping(params = "recharge")
	@ResponseBody
	public Object manualRecharge(HttpServletRequest request) {		
		String accountid = request.getParameter("accountid");		
		String type = request.getParameter("type");
		String amt = request.getParameter("amt");
		double amount = Double.parseDouble(amt);
		accountService.manualRecharge(accountid,type,amount);
		Map<String, Object> msg = new HashMap<String, Object>();		
		msg.put("resp_code", "00");		
		return msg;
	}
}
