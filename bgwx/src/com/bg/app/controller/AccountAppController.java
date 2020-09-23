package com.bg.app.controller;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.DataUtils;
import org.jeecgframework.core.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.UtilData;
import com.bg.web.entity.AccountInfo;
import com.bg.web.entity.TransInfo;
import com.bg.web.service.AccountServiceI;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardCollectFromBankAccountDTO;

@Controller
@RequestMapping("acctApp")
public class AccountAppController {
	private static Logger logger = Logger.getLogger(AccountAppController.class);
	@Autowired
	private AccountServiceI accountService;
	@Autowired
	private ITransKFTService transKFTService;
	@Autowired
	private ITransBaseService transBaseService;
	/**
	 * 用户登陆
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "login")
	@ResponseBody
	public Object login(HttpServletRequest req) {
		String user = req.getParameter("user");
		String passwd = req.getParameter("passwd");
		Map<String, Object> msg = new HashMap<String, Object>();
		String pwd = PasswordUtil.encrypt(user, passwd, PasswordUtil.getStaticSalt());
		System.out.println("user:"+user);
		System.out.println("passwd:"+pwd);
	    if (accountService.checkAccount(user, pwd)) {
	    	msg.put("ajaxCode", "00");
		}else{
			msg.put("msg", "账号或密码错");
		}
	    return msg;
	}
	/**
	 * 账号注册
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "regist")
	@ResponseBody
	public Object regist(HttpServletRequest req) {
		String account = req.getParameter("user");
		String passwd = req.getParameter("passwd");
		Map<String, Object> msg = new HashMap<String, Object>();
		//检查账号是否存在
		if(accountService.checkAccount(account)){//前端提示账号已存在
			msg.put("msg", "账号已存在");
			return msg;
		}
		try{			
			String password = PasswordUtil.encrypt(account, passwd, PasswordUtil.getStaticSalt());
			accountService.createAccount(account,password);
			msg.put("ajaxCode", "00");
			msg.put("msg", "注册成功");	
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			System.out.println(e);
			msg.put("msg", "注册失败");
		}		
		return msg;
	}
	/**
	 * 进入快捷支付获取首次数据
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goquickpay")
	@ResponseBody
	public Object goQuickPay(HttpServletRequest req) {
		String accountId = req.getParameter("accountId");
		Map<String,Object> personInfo = accountService.queryCashCardInfo(accountId,"01");
		Map<String,Object> transInfo = accountService.queryLastGbpTransInfo(accountId,PARA_CONSTATNTS.TransCode_GbpQuickPayApply);
		String bankno = "";
		String bankname = "---";
		String bankcardno = "";
		String bankphone = "";
		if(transInfo!=null){
			bankno = (String)transInfo.get("BANKNO");
			bankname = transKFTService.banknoToBankName(bankno);
			bankcardno = (String)transInfo.get("BANKCARDNO");
			bankphone = (String)transInfo.get("BANKPHONE");
		}			
		String name = (String)personInfo.get("NAME");
		String idcard = (String)personInfo.get("IDCARD");		
		
		String hvertify = (String)personInfo.get("VERTIFY");
		String hbankno = (String)personInfo.get("BANKNO");
		String hbankcardno = (String)personInfo.get("BANKCARDNO");
		String thbankcardno = "";
		if(hbankcardno!=null&&!"".equals(hbankcardno)){
			thbankcardno = hbankcardno.substring(0,3)+"**********"+hbankcardno.substring(hbankcardno.length()-3);
		}			
		String tidcard = "";
		if(idcard!=null&&!"".equals(idcard)){
			tidcard = idcard.substring(0,5)+"********"+idcard.substring(idcard.length()-3);
		}	
		Map<String,String> msg = new HashMap<String,String>();
		msg.put("ajaxCode", "00");
		msg.put("name", name);
		msg.put("idcard", idcard);
		msg.put("tidcard", tidcard);
		msg.put("bankcardno", bankcardno);
		msg.put("bankno", bankno);
		msg.put("bankname", bankname);
		msg.put("bankphone", bankphone);
		msg.put("hvertify", hvertify);
		msg.put("hbankno", hbankno);
		msg.put("hbankcardno", hbankcardno);
		msg.put("thbankcardno", thbankcardno);
	    return msg;
	}	
	
	/**
	 * 获取用户账单列表
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "gettranslist")
	@ResponseBody
	public Object getTransList(HttpServletRequest req) {
		String accountid = req.getParameter("accountid");
		List<TransactionEntity> trans = transBaseService.queryPayOrder(accountid, null);
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
					transinfo.setStatus("扫码收款");
				}else if (tran.getTrantype().equals("1002")) {
					transinfo.setStatus("扫码收款");
				}else if(tran.getTrantype().equals("1010")||"1015".equals(tran.getTrantype())) {
					transinfo.setStatus("快捷收款");
				}
				transinfos.add(transinfo);
			}
		}
		return transinfos;
	}
	/**
	 * 进入我的团队页获取数据
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goteam")
	@ResponseBody
	public Object goTeam(HttpServletRequest req) {
		String accountId = req.getParameter("accountid");		
		AccountInfo accountinfo = accountService.getAccountByAccountId(accountId);
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
			if(m.get("ACCOUNT")==null||"".equals((String)m.get("ACCOUNT"))){
				continue;
			}
			num+=1;
			num+=(long)m.get("NUM");
			String account = (String)m.get("ACCOUNT");
			String tmp = "********";
			String ta = tmp+account.substring(account.length()-4, account.length());
			m.put("TACCOUNT", ta);
		}
		String ta = "********"+accountId.substring(accountId.length()-3);	
		Map<String,String> pd = new HashMap<String,String>();
		pd.put("taccount", ta);
		pd.put("tnum", num+"");
		pd.put("transamt", amount);
		pd.put("name", accountinfo.getName());
		Map<String,Object> msg = new HashMap<String,Object>();
		msg.put("tlist", tlist);
		msg.put("pobj", pd);
		return msg;
	}
	/**
	 * 进入个人中心页获取数据
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "gopersonal")
	@ResponseBody
	public Object goPersonal(HttpServletRequest req) {
		String accountId = req.getParameter("accountid");			
		Map<String,Object> personInfo = accountService.queryCashCardInfo(accountId,"01");
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
		if(personInfo.get("BANKNO")!=null){
			String bankno = (String)personInfo.get("BANKNO");
			String bankname = transKFTService.banknoToBankName(bankno);
			personInfo.put("BANKNAME", bankname);
		}	
		return personInfo;
	}
	/**
	 * 进入绑卡页面获取数据
	 * 
	 * @param merchant
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "gobindbankcard")
	@ResponseBody
	public Object goBindBankcard(HttpServletRequest req) {
		String accountId = req.getParameter("accountid");			
		AccountInfo accountInfo = accountService.getAccountByAccountId(accountId);
		boolean bool = accountService.isIdentity(accountInfo.getAccount());
		Map<String,Object> msg = new HashMap<String,Object>();
		msg.put("isvalid", bool);
		msg.put("accountinfo", accountInfo);
		return msg;
	}
}
