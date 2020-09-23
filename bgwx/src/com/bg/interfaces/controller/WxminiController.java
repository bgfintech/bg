package com.bg.interfaces.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weixin.guanjia.core.util.WeixinUtil;

import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.entity.Trans1101Rsp;
import com.bg.interfaces.entity.internal.Case;
import com.bg.interfaces.entity.internal.CaseT;
import com.bg.interfaces.entity.internal.CaseTree;
import com.bg.interfaces.entity.internal.Implement;
import com.bg.interfaces.service.IBPEntService;
import com.bg.interfaces.service.IWxminiService;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.util.HttpsUtil;
import com.bg.util.UtilData;
import com.bg.util.wx.WXPay;
import com.bg.util.wx.WXPayConfigByBG;
import com.bg.util.wx.WXPayConstants.SignType;
import com.bg.util.wx.WXPayUtil;

@Controller
@RequestMapping("/mini") 
public class WxminiController {
	private static Logger logger = Logger.getLogger(WxminiController.class);
	
	private String notifyUrl = "http://www.widefinance.cn/gateway/wxpaynotify";
	//private String notifyUrl = "http://test.widefinance.cn/gateway/wxpaynotify";
	private WXPayConfigByBG config_bg = new WXPayConfigByBG();
	
	private static String appid = "wx18784a6a625f190e";	
	private static String appsecret = "315e912b758e1f40c96b843583c4ce45";
	
	@Autowired
	private UserService userService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private IBPEntService bpEntService;
	@Autowired
	private IWxminiService wxminiService;
	@Autowired
	private ITransBaseService transBaseService;
	
	private static Map<String,String> accountidMap = new HashMap<String,String>();
	
	@RequestMapping("autologin")
	@ResponseBody
	public Object autoLogin(HttpServletRequest request){	
		String openid = request.getParameter("openid");
		String accountid = request.getParameter("accountid");
		String code = request.getParameter("code");
		if(openid==null||"".equals(openid)){
			String requestUrl = WeixinUtil.mini_code2session.replace("APPID",config_bg.getAppID()).replace("SECRET", config_bg.getAppsecret()).replace("JSCODE", code);
			//String requestUrl = WeixinUtil.mini_code2session.replace("APPID",appid).replace("SECRET", appsecret).replace("JSCODE", code);
			String result="";
			try {
				result = HttpsUtil.get(requestUrl, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONObject jsonObject = JSONObject.parseObject(result);
			System.out.println(result);
			return jsonObject;
		}else{
			Map<String,String> m = new HashMap<String,String>();
			String to = accountidMap.get(accountid);
			System.out.println("openid====="+to+" : "+accountid);
			if(openid.equals(to)){
				m.put("code", "00");
			}else{
				m.put("code", "01");
			}
			return m;
		}
	}
	
	
	@RequestMapping("login")
	@ResponseBody
	public Object login(HttpServletRequest request){		
		String name = request.getParameter("name");
		String pwd = request.getParameter("passwd");
		String openid = request.getParameter("openid");
		String code = request.getParameter("code");
		TSUser user = new TSUser();
		user.setUserName(name);
		user.setPassword(pwd);
		TSUser u = userService.checkUserExits(user);
		Map<String,String> m = new HashMap<String,String>();
		if (u == null) {
			m.put("code", "01");
			m.put("msg", "用户名或密码错误!");
		}else if(u != null && u.getStatus() != 0) {			
			String message = "用户: " + user.getUserName() +  "登录成功";		
			System.out.println("登录成功"+openid);
			m.put("code", "00");
			if(openid!=null&&!"".equals(openid)){
				m.put("openid", openid);
				accountidMap.put(name, openid);
			}else{
				String requestUrl = WeixinUtil.mini_code2session.replace("APPID",config_bg.getAppID()).replace("SECRET", config_bg.getAppsecret()).replace("JSCODE", code);
				//String requestUrl = WeixinUtil.mini_code2session.replace("APPID",appid).replace("SECRET", appsecret).replace("JSCODE", code);
				String result="";
				try {
					result = HttpsUtil.get(requestUrl, null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				JSONObject jsonObject = JSONObject.parseObject(result);
				String op = jsonObject.getString("openid");
				m.put("openid", op);
				accountidMap.put(name, op);				
			}		
			// 添加登陆日志
			systemService.addLog(message, Globals.Log_Type_LOGIN,Globals.Log_Leavel_INFO);			

		}else{
			System.out.println("111");
			m.put("code", "01");
			m.put("msg", "该用户已锁定，请联系管理员！");
		}
		return m;
	}
	
	@RequestMapping("riskquery")
	@ResponseBody
	public Object riskquery(HttpServletRequest request){	
		String accountid = request.getParameter("accountid");
		String openid = request.getParameter("openid");		
		String name = request.getParameter("name");
		String idcard = request.getParameter("idcard");
		String type = request.getParameter("type");
		String org = request.getParameter("org");
		String state = request.getParameter("state");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String t = sdf.format(new Date());
		String n = "1".equals(type)?name:org;
		String id = "1".equals(type)?idcard:null;
		/********判断登录是否有效*****************/
		String topenid = accountidMap.get(accountid);
		if(openid==null||!openid.equals(topenid)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("resp_code", "02");
			m.put("resp_desc", "登录失效,请重新登录");
			return m;
		}		
		Object obj = null;
		if("1".equals(state)){
			obj=bpEntService.entstatService(n,id,type,t,accountid);		
		}else{
			Trans1101Rsp rsp=(Trans1101Rsp)bpEntService.entoutService(n,id,type,t,accountid);
			transform(rsp);
			obj = rsp;
		}
		return obj;		
	}
	
	@RequestMapping("getquerylist")
	@ResponseBody
	public Object getQueryList(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String accountid = request.getParameter("accountid");
		String type= request.getParameter("type");
		int pageno = Integer.parseInt(request.getParameter("pageno"));
		int pagesize = Integer.parseInt(request.getParameter("pagesize"));
		
		/********判断登录是否有效*****************/
		String topenid = accountidMap.get(accountid);
		if(openid==null||!openid.equals(topenid)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("resp_code", "02");
			m.put("resp_desc", "登录失效,请重新登录");
			return m;
		}				
		Map<String,Object> m = new HashMap<String,Object>();			
		int offset = (pageno-1)*pagesize;			
		List<Map<String,Object>> list = wxminiService.getQueryList(accountid,type,offset, pagesize);
		if(list.size()==0){
			m.put("resp_code", "01");
			m.put("resp_desc", "没有数据了");
			return m;
		}
		m.put("resp_code", "00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String,Object> row:list){
			String idcard = (String)row.get("IDCARD");
			if(idcard!=null&&idcard.length()==18){
				idcard=idcard.substring(0,4)+"**********"+idcard.substring(idcard.length()-3);
			}else{
				idcard = idcard==null?"":idcard;
			}			
			row.put("idcard", idcard);
			Date time = (Date)row.get("CREATETIME");
			row.put("time", sdf.format(time)); 
		}			
		m.put("list", list);	
		return m;
		
	}
	@RequestMapping("getbilllist")
	@ResponseBody
	public Object getBillList(HttpServletRequest request){
		String openid = request.getParameter("openid");
		String accountid = request.getParameter("accountid");
		int pageno = Integer.parseInt(request.getParameter("pageno"));
		int pagesize = Integer.parseInt(request.getParameter("pagesize"));
		
		/********判断登录是否有效*****************/
		String topenid = accountidMap.get(accountid);
		if(openid==null||!openid.equals(topenid)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("resp_code", "02");
			m.put("resp_desc", "登录失效,请重新登录");
			return m;
		}				
		Map<String,Object> m = new HashMap<String,Object>();			
		int offset = (pageno-1)*pagesize;			
		List<Map<String,Object>> list = wxminiService.getBillList(accountid, offset, pagesize);
		if(list.size()==0){
			m.put("resp_code", "01");
			m.put("resp_desc", "没有数据了");
			return m;
		}
		m.put("resp_code", "00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("0.00");
		for(Map<String,Object> row:list){
			String type = (String)row.get("OCCURTYPE");
			if("1100".equals(type)){
				row.put("type", "查询简版");
			}else if("1101".equals(type)){
				row.put("type", "查询详版");
			}else if("1033".equals(type)){
				if("b_transaction".equals(row.get("LINKTYPE"))){
					row.put("type", "充值");
				}else{
					row.put("type", "赠送");
				}
			}
			double amt = (Double)row.get("BALANCEAMT");
			row.put("amt", df.format(amt));
			Date time = (Date)row.get("INPUTTIME");
			row.put("time", sdf.format(time));
		}			
		m.put("list", list);	
		if(pageno==1){
			Map<String,Object> row = list.get(0);
			double balance = (Double)row.get("AFTERBALANCE");
			m.put("balance", df.format(balance));
		}					
		return m;
		
	}
	
	@RequestMapping("gopayment")
	@ResponseBody
	public Object goPayment(HttpServletRequest request){
		String accountid = request.getParameter("accountid");
		String openid = request.getParameter("openid");
		String amt = request.getParameter("amt");
		/**判断登录是否失效 */
		String topenid = accountidMap.get(accountid);
		if(openid==null||!openid.equals(topenid)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("resp_code", "02");
			m.put("resp_desc", "登录失效,请重新登录");
			return m;
		}				
		/*************调用微信统一下单**********************/		
		TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName",accountid);
		String name = users.getRealName();
		Map<String,String> data = new HashMap<String,String>();
		data.put("body", "布广信息-账户充值");
		String orderno = UtilData.createOnlyData();
		data.put("out_trade_no",orderno);
		data.put("total_fee", amt);
		String ip = getIpAddress(request);
		data.put("spbill_create_ip", ip);
		data.put("notify_url", notifyUrl);
		data.put("trade_type","JSAPI");
		data.put("openid", openid);
		Map<String,String> msg = new HashMap<String,String>();
		try {
			WXPay pay = new WXPay(config_bg);
			Map<String,String> orsp = pay.unifiedOrder(data);
			String result_code = orsp.get("result_code");
			if("SUCCESS".equals(result_code)){
				String prepay_id = orsp.get("prepay_id");			
				msg.put("appId", config_bg.getAppID());
				msg.put("timeStamp", Long.toString(WXPayUtil.getCurrentTimestamp()));
				msg.put("nonceStr", WXPayUtil.generateNonceStr());
				msg.put("package", "prepay_id="+prepay_id);
				msg.put("signType","HMAC-SHA256");
				String sign = WXPayUtil.generateSignature(msg, config_bg.getKey(), SignType.HMACSHA256);
				msg.put("sign", sign);				
				msg.put("resp_code", "00");
				
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				TransactionEntity entity = new TransactionEntity();	
				entity.setReqno(orderno);
				entity.setOrderno(orderno);			
				entity.setTranstime(sdf.format(d));
				entity.setTrantype(PARA_CONSTATNTS.TransCode_Recharge);	
				entity.setAccountid(accountid);
				entity.setName(name);
				entity.setTranamt(Integer.parseInt(amt));
				entity.setStatus("03");
				entity.setRespcontent(JSONObject.toJSONString(orsp));
				transBaseService.insertTransaction(entity);				
			}else{
				String resp_desc=orsp.get("err_code_des");
				msg.put("resp_code", "01");
				msg.put("resp_desc", resp_desc);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			msg.put("resp_code", "01");
			msg.put("resp_desc", "系统错误,稍后再试");
		}		
		return msg;
	}
	
	
	private void transform(Trans1101Rsp rsp){
		List<Case> civils = null;
		if(rsp.getCivil()!=null){
			civils = rsp.getCivil().getCases();
		}
		List<Case> criminals = null;
		if(rsp.getCriminal()!=null){
			criminals= rsp.getCriminal().getCases();
		}
		List<Case> admins = null;
		if(rsp.getAdministrative()!=null){
			admins = rsp.getAdministrative().getCases();
		}
		List<Implement> imps = null;
		if(rsp.getImplement()!=null){
			imps = rsp.getImplement().getCases();
		}		
		CaseTree ctree = rsp.getCases_tree();	
		if(ctree!=null){
			List<CaseT> civs = ctree.getCivil();
			transformTree(civs,civils,imps);
			List<CaseT> cris = ctree.getCriminal();
			transformTree(cris,criminals,imps);
			List<CaseT> adms = ctree.getAdministrative();
			transformTree(adms,admins,imps);
		}
		
	}
	
	private void transformTree(List<CaseT> lct,List<Case> lc,List<Implement> li){
		if(lct==null){
			return;
		}		
		int offsize = 0;
		int size = lct.size();
		for(int i=0;i<size;i++){
			CaseT ct = lct.get(offsize);
			transforCaseT(ct,lc,li,lct,offsize);			
			offsize=lct.size()-(size-i-1);
		}		
		
	}
	
	private void transforCaseT(CaseT ct,List<Case> lc,List<Implement> li,List<CaseT> lct,Integer offset){
		String ajbs = ct.getN_ajbs();
		String lx = ct.getCase_type();
		if("执行".equals(lx)){
			if(li!=null){
				Iterator<Implement> iterator = li.iterator();  
		        while (iterator.hasNext()) {  
		        	Implement imp = iterator.next();  
		        	String n_ajbs = imp.getN_ajbs();
					if(ajbs.equals(n_ajbs)){
						ct.setCaseobj(imp);
						iterator.remove();
					}
		        }  		
			}							
		}else{
			if(lc!=null){
				Iterator<Case> iterator = lc.iterator();  
		        while (iterator.hasNext()) {  
		        	Case c = iterator.next();  
		        	String n_ajbs = c.getN_ajbs();
					if(ajbs.equals(n_ajbs)){
						ct.setCaseobj(c);
						iterator.remove();
					}
		        }  		
			}						
		}
		CaseT next = ct.getNext();
		if(next!=null){
			offset++;
			next.setIsfrist(false);
			lct.add(offset,next);
			transforCaseT(next,lc,li,lct,offset);
		}
	}
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}	
}
