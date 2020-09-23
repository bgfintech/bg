package com.bg.web.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
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

import org.apache.log4j.Logger;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.DateUtil;
import com.bg.util.UtilData;
import com.bg.web.entity.AccountInfo;
import com.bg.web.service.AccountServiceI;
import com.bg.web.service.ICreditCardService;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.BankCardDetailDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTradeResultDTO;
import com.sun.star.uno.RuntimeException;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.util.WeixinUtil;
import weixin.util.Constants;

/**
 * 信用卡业务controller
 * @author YH
 * @date 2018年7月2日
 * @version 1.0
 */
@Controller
@RequestMapping("/creditCardController")
public class CreditCardController {
	
	private static Logger logger = Logger.getLogger(CreditCardController.class);
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private AccountServiceI accountService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ICreditCardService creditCardService;
	@Autowired
	private ITransKFTService transKFTService;
	
	public static double gbpRate=0.0065;//代扣费率
	
	public static int gbpfee=2; //代付费用
	
	/**
	 * 花呗套现被扫支付进入页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gotxscan")
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
		request.setAttribute("source", "gotxscanpay");
		if (accountService.checkOpenid(openid)) {
			JSONObject jsonObject = JSONObject.fromObject(weixinAccountService.getJsApiInfo(weixinAount,systemService,request.getRequestURL() + "?"+ request.getQueryString()));
			request.setAttribute("nonceStr", jsonObject.getString("nonceStr"));
			request.setAttribute("timestamp", jsonObject.getString("timestamp"));
			request.setAttribute("signature", jsonObject.getString("signature"));
			request.setAttribute("appid", weixinAount.getAccountappid());
			return new ModelAndView("weixin/bgqb/h5/creditcard/pay_scan");
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	/**
	 * 花呗扫码
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gotxqrcode")
	public ModelAndView gotxqrcode(HttpServletRequest request) {	
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
		request.setAttribute("source", "gotxqrcodepay");
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);			
			request.setAttribute("accountinfo", accountinfo);
			return new ModelAndView("weixin/bgqb/h5/creditcard/pay_qrcode");
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	
	
	
	/**
	 * 信用卡代还
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goinsteadrepay")
	public ModelAndView goCardInsteadRepay(HttpServletRequest request) {	
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
		request.setAttribute("source", "goinsteadrepay");
		if (accountService.checkOpenid(openid)) {
			AccountInfo accountinfo = accountService.getAccountByOpenid(openid);
			List<Map<String,Object>> creditCardList = creditCardService.findBindCreditCardList(accountinfo.getAccount());
			for(Map<String,Object> m:creditCardList){
				String bankno = (String)m.get("BANKNO");
				String bankName = transKFTService.banknoToBankName(bankno);
				m.put("BANKNAME", bankName);
				String cardno = (String)m.get("BANKCARDNO");
				String dcardno = cardno.substring(cardno.length()-4);
				m.put("DCARDNO", dcardno);
			}			
			request.setAttribute("cardlist", creditCardList);
			return new ModelAndView("weixin/bgqb/h5/creditcard/creditcardlist");
		}
		return new ModelAndView("weixin/bgqb/h5/login");
	}
	
	/**
	 * 信用卡绑卡页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "gobindcreditcard")
	public ModelAndView goBindCreditCard(HttpServletRequest request) {
		String openid = (String) request.getParameter("openid");
		AccountInfo accountInfo = accountService.getAccountByOpenid(openid);
		String idcard = accountInfo.getIdcard();
		String tidcard = "";
		if(idcard!=null&&!"".equals(idcard)){
			tidcard = idcard.substring(0,5)+"********"+idcard.substring(idcard.length()-3);
		}	
		request.setAttribute("tidcard", tidcard);
		request.setAttribute("account", accountInfo);
		request.setAttribute("openid", openid);
		return new ModelAndView("weixin/bgqb/h5/creditcard/bindcreditcard");
	}
	
	@RequestMapping(params="bindcreditcard")
	@ResponseBody
	public Object bindCreditCard(HttpServletRequest request){
		System.out.println("============bindcreditcard");
		String openid = request.getParameter("openid");
		AccountInfo accountinfo = accountService.getAccountByOpenid(openid);	
		Map<String,Object> bindBandCard = accountService.queryCashCardInfo(accountinfo.getAccount(),"01");
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
		/******************信用卡三要素验证*********************************/
		//没有认证过先认证
		String reqNo1 = UtilData.createOnlyData();	
		BankCardDetailDTO bDTO = new BankCardDetailDTO();
		bDTO.setReqNo(reqNo1);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
		bDTO.setOrderNo(reqNo1);// 订单号同一个商户必须保证唯一
		bDTO.setCustName(name);// 客户姓名		
		bDTO.setCustID(idcard);// 客户证件号码
		bDTO.setCustBankNo(bankNo);// 银行卡行别
		bDTO.setCustBankAccountNo(bankCardNo);// 需要验证的银行卡号
		bDTO.setCustAccountCreditOrDebit("2");//贷记卡
		entity = new TransactionEntity();
		entity.setAccountid(accountinfo.getAccount());
		entity.setDeviceid(openid);
		String transMerchantId = transKFTService.selectGbpTransMerchantId(bankNo);
		SameIDCreditCardTradeResultDTO breq = transKFTService.threeMessageVerify(bDTO,entity,transMerchantId);
		status = transKFTService.QPayStatusToCode(breq.getStatus()+"");
		if(!"01".equals(status)){
			//认证失败
			msg.put(Constants.FAIL_TEXT, breq.getFailureDetails());
			return msg;
		}		
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
	 * 还款计划展示
	 * 
	 * @return
	 */
	@RequestMapping(params = "viewrepayplan")
	public ModelAndView viewRepayPlan(HttpServletRequest request) {
		String planId = (String)request.getParameter("planid");
		List<Map<String,Object>> planList = creditCardService.findRepayPlanByPlanId(planId);
		
		request.setAttribute("planList", planList);
		return new ModelAndView("weixin/bgqb/h5/creditcard/repayplan");
	}
	
	/**
	 * 设置还款计划页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "gosetrepayplan")
	public ModelAndView goSetRepayPlan(HttpServletRequest request) {
		String cardno = request.getParameter("cardno");
		Map<String,Object> cardInfo = creditCardService.findCardInfoByCardno(cardno);
		String cn = (String)cardInfo.get("BANKCARDNO");
		String dcardno = cn.substring(cn.length()-4);
		cardInfo.put("DCARDNO", "******"+dcardno);
		String bankno = (String)cardInfo.get("BANKNO");
		String bankName = transKFTService.banknoToBankName(bankno);
		cardInfo.put("BANKNAME", bankName);		
		request.setAttribute("cardinfo", cardInfo);
		return new ModelAndView("weixin/bgqb/h5/creditcard/setrepayplan");
	}
	
	@RequestMapping(params="setrepayplan")
	@ResponseBody
	public Object setRepayPlan(HttpServletRequest request){			
		String cardno = request.getParameter("cardno");
		String amt = request.getParameter("amt");
		double damt = Double.parseDouble(amt);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String sdate = request.getParameter("sdate");
		String days = request.getParameter("days");
		Date today = new Date();
		String status = null;
		boolean isSet = creditCardService.isSetRepayPlanByCardno(cardno);
		Map<String, Object> msg = new HashMap<String, Object>();	
		if(isSet){
			msg.put(Constants.FAIL_TEXT, "该卡有未执行还款计划,不能重复设置");
			return msg;
		}		
		int d = Integer.parseInt(days);
		/***************计算单日还款金额和单日刷卡总金额**********************/
		double oneRepayAmt = Math.ceil(damt/d); //元
		double limit = Math.ceil((oneRepayAmt+gbpfee)*100/(UtilData.subtract(1, gbpRate))); //分
		
		
		List<Object[]> datas = new ArrayList<Object[]>();		
		String transdate = sdate;
		SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHmmss");
		String planId = UtilData.createOnlyData();
		/**********************生成单日计划数据**************************************/
		for(int i=0;i<d;i++){
			if(i!=0){
				try {
					transdate = DateUtil.getRelativeDate(transdate, DateUtil.TERM_UNIT_DAY, 1);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					e.printStackTrace();
					throw new RuntimeException();
				}
			}			
			/************单日刷卡执行随机次数3-5***************/
			Random random=new Random();
			int num = random.nextInt(2)+2;	
			double olimit = limit-10*100*num; //分  保证最少一次刷卡10元
			/***********金额随机数************/
			double[] swipAmts = new double[num]; 
			double[] rol = randomAll(num);
			double tmpor = limit;
			for(int sr=0;sr<num;sr++){
				if(sr!=num-1){
					double or = Math.floor(olimit*rol[sr]/100)*100+10*100;//单日刷卡金额*单次随机比例-向下取整到元+10元  单位分						
					swipAmts[sr]= or;
					tmpor=tmpor-or;
				}else{
					//最后一笔取单取剩余金额 带分 之前都是取整到元
					swipAmts[sr]=tmpor;
					//每日带分一笔刷卡,随机换到其它位置
					int w = random.nextInt(num);
					double tmpw = swipAmts[w];
					swipAmts[w]=tmpor;
					swipAmts[sr]=tmpw;
				}			
			}
			/************时间随机数***********/
			
			//时间随机数比刷卡数多一笔还款
			String[] swipTimes = new String[num+1]; 
			double[] rts = randomAll(num+2);
			//运行时间在早8点到晚9点之间 分钟数
			int snum = 60*14;
			Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            //当前时间10分钟后第一笔 到当天23点半前结束共40分钟 每天21点后不能设置当天
			int dnum = (int) ((calendar.getTimeInMillis()-40*60*1000-today.getTime())/1000/60);
			if(dnum>snum){
				dnum=snum;
			}
			Date transd = null;
			try {
				transd = ymdhms.parse(transdate+"080000");
			} catch (ParseException e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
			int hour = Integer.parseInt(ymdhms.format(today).substring(8,10));
			Date tmpToday = today;
			for(int tr=0;tr<=num;tr++){							
				//当天设置执行且设置时间大于8点
				if(transdate.equals(sdate)&&hour>=8){
					if(tr==0){
						Calendar c = Calendar.getInstance();
						c.setTime(tmpToday);
						c.add(Calendar.MINUTE, 10);
						tmpToday=c.getTime();
						swipTimes[tr]=ymdhms.format(tmpToday);
					}else{
						Calendar c = Calendar.getInstance();
						c.setTime(tmpToday);
						c.add(Calendar.MINUTE, (int)(dnum*rts[tr]));
						tmpToday=c.getTime();
						swipTimes[tr]=ymdhms.format(tmpToday);
					}					
				}else{		
					Calendar c = Calendar.getInstance();
					c.setTime(transd);
					c.add(Calendar.MINUTE, (int)(snum*rts[tr]));
					transd=c.getTime();
					swipTimes[tr]=ymdhms.format(transd);
				}
			}
			for(int j=0;j<=num;j++){
				if(j!=num){
					double tamt = Math.floor(olimit*rol[j]/100)*100;
					olimit-=tamt;		
					//planid,bankcardno,type,tranamt,transdate,runtime,status
					Object[] obj = new Object[]{planId,cardno,"01",decimalFormat.format(swipAmts[j]),transdate,swipTimes[j],"0"};
					datas.add(obj);					
				}else{
					Object[] obj = new Object[]{planId,cardno,"02",decimalFormat.format(oneRepayAmt*100),transdate,swipTimes[j],"0"};
					datas.add(obj);	
				}							
			}						
		}
		creditCardService.createBatchRepayPlan(datas);				
		msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		msg.put("PLANID", planId);
		return msg;
	}
	
	public double[] randomAll(int num){
		double[] rd = new double[num];
		double rdsum=0;
		Random random=new Random();		
		//如果出现0重新随机
		boolean bool = true;
		while(bool){
			rdsum=0;
			boolean bb = false;
			for(int i=0;i<num;i++){
				int x = random.nextInt(100/(num-1));
				if(x==0){
					bb=true;
				}
				rdsum+=x;
				rd[i]=x;
			}
			if(bb==true){
				bool=true;
			}else{
				bool=false;
			}
		}		
		for(int i=0;i<num;i++){
			rd[i]=Math.round(rd[i]/rdsum*100)/100d;
		}		
		return rd;
	}
	
	public static void main(String[] arg){
		
		int d = 1;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		System.out.println(d/100d);
		String amount = decimalFormat.format(d/100d);	
		System.out.println(amount);
//		
//		DecimalFormat decimalFormat = new DecimalFormat("#");
//		//System.out.println(decimalFormat.format(11.49));
//		
//		Random random=new Random();	
//		double a = 153;
//		//System.out.println(Math.floor(a/100)*100);
//		SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHmmss");
//		Date today = new Date();
//		System.out.println(ymdhms.format(today));
//		System.out.println(ymdhms.format(today).substring(8,10));
//		int hour = Integer.parseInt(ymdhms.format(today).substring(8,10));
//		System.out.println(hour);
	}
	
}
