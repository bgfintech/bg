package com.bg.interfaces.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weixin.guanjia.core.util.WeixinUtil;

import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.service.IBPEntService;
import com.bg.util.SignTools;

/**
 * B+ HTTP Controller
 * @author YH
 * @date 2019年10月24日
 * @version 1.0
 */
@Controller
@RequestMapping("/bp")
public class BPController {
	private static Logger logger = Logger.getLogger(BPController.class);
	@Autowired
	private IBPEntService bpEntService;
	
	private static String key = "123456";
	
	
	
	//每次请求的时间戳缓存
	private static Map<String,String> cache = new HashMap<String,String>();
	private static String cacheDate = "";
	
	/**
	 * 涉诉统计模板查询
	 * @param request
	 * @return
	 */
	@RequestMapping("entstat")
	@ResponseBody
	public Object entstat(HttpServletRequest request){
		String name = request.getParameter("name");
		String id = request.getParameter("idcard");
		String type = request.getParameter("type");
		String account = request.getParameter("account");
		String t = request.getParameter("t");
		String s = request.getParameter("s");
		boolean bool = verifyToken(account+t,s);
		if(!bool){
			return "token error!";
		}	
		Object rspdata = bpEntService.entstatService(name,id,type,t,account);
		return JSONObject.toJSONString(rspdata);
	}
	
	/**
	 * 涉诉明细模板查询
	 * @param request
	 * @return
	 */
	@RequestMapping("entout")
	@ResponseBody
	public Object entout(HttpServletRequest request){
		String name = request.getParameter("name");
		String id = request.getParameter("idcard");
		String type = request.getParameter("type");
		String account = request.getParameter("account");
		String t = request.getParameter("t");
		String s = request.getParameter("s");
		boolean bool = verifyToken(account+t,s);
		if(!bool){
			return "token error!";
		}	
		Object rspdata = bpEntService.entoutService(name, id, type, t, account);
		return JSONObject.toJSONString(rspdata);
	}
	/**
	 * 查询用户消费记录
	 * @param request
	 * @return
	 */
	@RequestMapping("expensesrecord")
	@ResponseBody
	public Object expensesRecord(HttpServletRequest request){
		String account = request.getParameter("account");
		String t = request.getParameter("t");
		String s = request.getParameter("s");
		String sdate = request.getParameter("start_date");
		String edate = request.getParameter("end_date");
		boolean bool = verifyToken(account+t,s);
		if(!bool){
			return "token error!";
		}	
		return bpEntService.expensesRecord(account, sdate, edate);		
	}
	/**
	 * 查询用户涉诉缓存信息
	 * @param request
	 * @return
	 */
	@RequestMapping("cachelist")
	@ResponseBody
	public Object entCache(HttpServletRequest request){
		String account = request.getParameter("account");
		String t = request.getParameter("t");
		String s = request.getParameter("s");
		boolean bool = verifyToken(account+t,s);
		if(!bool){
			return "token error!";
		}	
		List<Map<String,Object>> list = bpEntService.findTransListDesc(account);
		Map<String,Object> cm = new HashMap<String,Object>();
		if(list.size()>0){
			List<Object> cl = new ArrayList<Object>();
			cm.put("caselist", cl);
			for(Map<String,Object> m:list){
				Map<String,Object> row = new HashMap<String,Object>();
				String entType = (String)m.get("OCCURTYPE");
				String name = (String)m.get("NAME");
				String id = (String)m.get("IDCARD");
				row.put("ent_type", entType);
				row.put("name", name);
				if("1100".equals(entType)){
					Object o = bpEntService.entstatCache(account, name, id);
					row.put("case_data", o);
				}else{
					Object o = bpEntService.entoutCache(account, name, id);
					row.put("case_data", o);
				}				
				cl.add(row);
			}
			System.out.println(JSONObject.toJSONString(cm));
		}		
		return JSONObject.toJSONString(cm);		
	}
	
	/**
	 * 验证token是否合法
	 * @param token
	 * @param s
	 * @return
	 */
	private boolean verifyToken(String token,String s){
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String cd = sdf.format(d);
		if(!cd.equals(cacheDate)){
			cacheDate = cd;
			cache.clear();
		}		
		String sign = null;
		try {
			sign = SignTools.md5(token+key);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sign==null||!sign.equals(s)||cache.get(token)!=null){
			return false;
		}else{
			//缓存时间戳 防止恶意重复请求
			cache.put(token, "");
			return true;
		}
	}
	
	
	
}
