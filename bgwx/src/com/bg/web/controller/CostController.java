package com.bg.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransBaseService;
import com.bg.util.UtilData;
import com.bg.util.wx.WXPay;
import com.bg.util.wx.WXPayConfigByBG;
import com.bg.util.wx.WXPayUtil;
import com.bg.web.service.ICostService;

/**
 * 费用管理
 * @author YH
 * @date 2019年11月15日
 * @version 1.0
 */
@Controller
public class CostController {
	private static Logger logger = Logger.getLogger(CostController.class);
	
	private String notifyUrl = "http://www.widefinance.cn/gateway/wxpaynotify";
	
	@Autowired
	private ICostService costService;
	@Autowired
	private ITransBaseService transBaseService;
	private WXPayConfigByBG config_bg = new WXPayConfigByBG();
	
	/**
	@RequestMapping("/gateway/wxpayorder")
	@ResponseBody
	public void wxpayOrder(HttpServletRequest request,HttpServletResponse response){
		InputStream inputStream=null;
		BufferedReader in=null;
		StringBuffer sb = new StringBuffer();
		try {
			inputStream = request.getInputStream();
			String s;
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}
			Map<String,String> rm = WXPayUtil.xmlToMap(sb.toString());
			boolean bool = WXPayUtil.isSignatureValid(rm, config_bg);
			if(bool){
				//调用微信统一下单/			
				String rappid = rm.get("appid");
				String ropenid = rm.get("openid");
				String rmch_id = rm.get("mch_id");
				String ris = rm.get("is_subscribe");
				String rnonce_str = rm.get("nonce_str");
				String rproductid=rm.get("product_id");
				Map<String,String> data = new HashMap<String,String>();
				data.put("body", "布广信息-账户充值");
				String orderno = UtilData.createOnlyData();
				data.put("out_trade_no",orderno);
				String amtstr = rproductid.substring(11,19);
				String amt = Integer.parseInt(amtstr)+"";
				data.put("total_fee", amt);
				String ip = getIpAddress(request);
				System.out.println("wxpayorder ip:"+ip);
				data.put("spbill_create_id", ip);
				data.put("notify_url", notifyUrl);
				data.put("trade_type","NATIVE");				
				
				Map<String,String> rdata = WXPayUtil.generateSignedXml(data,config_bg);
				WXPay pay = new WXPay(config_bg);
				pay.unifiedOrder(data);
	
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			try {
				in.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
	}	
	**/
	@RequestMapping("/gateway/wxpaynotify")
	@ResponseBody
	public void wxpayNotify(HttpServletRequest request,HttpServletResponse response){
		InputStream inputStream=null;
		BufferedReader in=null;
		StringBuffer sb = new StringBuffer();
		try {
			inputStream = request.getInputStream();
			String s;
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}
			Map<String,String> rm = WXPayUtil.xmlToMap(sb.toString());
			boolean bool = WXPayUtil.isSignatureValid(rm, config_bg);
			if(bool){
				String result_code = rm.get("result_code");
				String total_fee=rm.get("total_fee");
				int fee = Integer.parseInt(total_fee);
				String out_trade_no = rm.get("out_trade_no");
				String channelNo = rm.get("transaction_id");
				TransactionEntity entity = transBaseService.queryOrder(out_trade_no);
				
				if(entity==null){
					return;
				}
				int tranamt = entity.getTranamt();
				String account = entity.getAccountid();
				int id = entity.getId();
				if(fee!=tranamt){
					return;
				}
				Map<String,String> resp = new HashMap<String,String>();
				resp.put("return_code", "SUCCESS");
				resp.put("return_msg", "OK");
				String rspStr = WXPayUtil.mapToXml(resp);
				double df = UtilData.div(fee, 100, 2);
				if("SUCCESS".equals(result_code)&&!"01".equals(entity.getStatus())){
					transBaseService.updateTransaction(out_trade_no, "01", channelNo, JSONObject.toJSONString(rm));					
					costService.recharge(account, PARA_CONSTATNTS.TransCode_Recharge, id+"", df);
					response.getOutputStream().write(rspStr.getBytes());
					response.flushBuffer();		
				}else if("FAIL".equals(result_code)){
					transBaseService.updateTransaction(out_trade_no, "02", channelNo, JSONObject.toJSONString(rm));
					response.getOutputStream().write(rspStr.getBytes());
					response.flushBuffer();		
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			try {
				in.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		
	}
	
	
	@RequestMapping("/cost/getqrcode")
	@ResponseBody
	public Object getqrcode(HttpServletRequest request){
		TSUser user = ResourceUtil.getSessionUserName();
		String account = user.getUserName();
		String name = user.getRealName();
		String amt = request.getParameter("amt");
		/*************调用微信统一下单**********************/			
		Map<String,String> data = new HashMap<String,String>();
		data.put("body", "布广信息-账户充值");
		String orderno = UtilData.createOnlyData();
		data.put("out_trade_no",orderno);
		data.put("total_fee", amt);
		String ip = getIpAddress(request);
		data.put("spbill_create_ip", ip);
		data.put("notify_url", notifyUrl);
		data.put("trade_type","NATIVE");
		Map<String,String> msg = new HashMap<String,String>();
		try {
			WXPay pay = new WXPay(config_bg);
			Map<String,String> orsp = pay.unifiedOrder(data);
			String result_code = orsp.get("result_code");
			if("SUCCESS".equals(result_code)){
				String code_url = orsp.get("code_url");			
				msg.put("resp_code", "00");
				msg.put("qrcode", code_url);
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				TransactionEntity entity = new TransactionEntity();	
				entity.setReqno(orderno);
				entity.setOrderno(orderno);			
				entity.setTranstime(sdf.format(d));
				entity.setTrantype(PARA_CONSTATNTS.TransCode_Recharge);	
				entity.setAccountid(account);
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
	
	
	/**
	 * 费用账单
	 * @param request
	 * @return
	 */
	@RequestMapping("/cost/getlist")
	@ResponseBody
	public Object getCostList(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		String account = user.getUserName();
		String sl = request.getParameter("limit");
		String so = request.getParameter("offset");
		
		int rowCount = costService.countCostList(account);
		int limit = (Integer.parseInt(sl));
		int offset = (Integer.parseInt(so)-1)*limit;
		
		List<Map<String,Object>> list = costService.findCostList(account, offset,limit);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("0.00");
		for(Map<String,Object> m:list){
			Date d = (Date)m.get("INPUTTIME");
			String sd = sdf.format(d);
			m.put("INPUTTIME", sd);
			String t = (String)m.get("OCCURTYPE");
			if("1101".equals(t)){
				m.put("OCCURTYPE", "风险查询(详版)");
			}else if("1100".equals(t)){
				m.put("OCCURTYPE", "风险查询(简版)"); 
			}else if("1033".equals(t)){
				Object lt = m.get("LINKTYPE");
				if(lt!=null&&"b_transaction".equals((String)lt)){
					m.put("OCCURTYPE", "充值");
				}else{
					m.put("OCCURTYPE", "赠送");
				}
				
			}
			m.put("BALANCEAMT", df.format(m.get("BALANCEAMT")));
			m.put("AFTERBALANCE", df.format(m.get("AFTERBALANCE")));			
		}		
		Map<String, Object> msg = new HashMap<>();
		msg.put("total", rowCount);
		msg.put("rows",list);
		return msg;
	}
	
	@RequestMapping("/cost/golist")
	public ModelAndView goCostList(){
		return new ModelAndView("cost/list"); 
	}
	
	@RequestMapping("/cost/gorecharge")
	public ModelAndView goRecharge(){
		return new ModelAndView("cost/gorecharge"); 
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
	
	public static void main(String[] arg){
		String s = "13011112222000200001218194911";
		System.out.println(s.length());
		System.out.println(s.substring(11,19));
		System.out.println(Integer.parseInt(s.substring(11,19)));
	}
}
