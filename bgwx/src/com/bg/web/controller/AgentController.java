package com.bg.web.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.util.Constants;
import weixin.util.PagerPlugin;

import com.bg.web.service.IAgentService;

@Controller
@RequestMapping("/agentController")
public class AgentController {
	private Logger logger = Logger.getLogger(AgentController.class);
	@Autowired
	private IAgentService agentService;
	
	
	@RequestMapping(params = "gocusttrans")
	public ModelAndView goCustTrans(HttpServletRequest request) {
		return new ModelAndView("agent/usertranslist");
	}
	
	@RequestMapping(params = "goteam")
	public ModelAndView goteam(HttpServletRequest request) {
		return new ModelAndView("agent/teamlist");
	}
	
	/**
	 * 统计团队交易情况
	 * @param accountId
	 * @param date
	 * @return
	 */
	@RequestMapping(params="countteamtrans")
	@ResponseBody
	public Object countTeamTrans(HttpServletRequest request){
		TSUser user = ResourceUtil.getSessionUserName();		
		Map<String, Object> msg = new HashMap<String, Object>();		
		if(user==null){
			msg.put(Constants.NULL_TEXT, Constants.SUCCESS);
			return msg;
		}
		String username = user.getUserName();
		String accountId = request.getParameter("accountid");
		String date = request.getParameter("date");			
		if(accountId==null||"".equals(accountId)){
			accountId = username;
		}
		if(date==null||"".equals(date)){
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			date = sdf.format(d);
		}
		date = date+"01";
		boolean isAgent = false;
		if(accountId.equals(username)){
			isAgent=true;
		}else{
			isAgent = agentService.isAgentCust(accountId, username);
		}		
		if("admin".equals(username)){
			isAgent=true;
		}
		if(isAgent){
			List<Map<String,Object>> list = agentService.countTeamTrans(accountId,date);
			
			PagerPlugin<Map<String,Object>> pager = new PagerPlugin<Map<String,Object>>();
			pager.setList(list);
			pager.setRowCount(list.size());			
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			msg.put(Constants.RESULT_CONTENT, pager);	
		}else{
			msg.put(Constants.FAIL_TEXT, "无权限查询该用户");
			return msg;
		}	
		return msg;
	}
	
	/**
	 * 查询客户交易列表
	 * @param accountId
	 * @param date
	 * @return
	 */
	@RequestMapping(params="findtranslist")
	@ResponseBody
	public Object findTransList(HttpServletRequest request){
		TSUser user = ResourceUtil.getSessionUserName();
		
		Map<String, Object> msg = new HashMap<String, Object>();		
		if(user==null){
			msg.put(Constants.NULL_TEXT, Constants.SUCCESS);
			return msg;
		}
		String username = user.getUserName();
		String accountId = request.getParameter("accountid");
		String date = request.getParameter("date");			
		boolean isAgent = agentService.isAgentCust(accountId, username);
		System.out.println(username);
		if("admin".equals(username)){
			isAgent=true;
		}
		if(isAgent){
			List<Map<String,Object>> list = agentService.findTransList(accountId,date);
			for(Map<String,Object> row:list){
				String d = (String)row.get("TRANTYPE");
				if(d.equals("1010")||d.equals("1015")){
					row.put("STRANTYPE", "付款");
				}else if("1019".equals(d)){
					row.put("STRANTYPE", "收款");
				}
				int amt =(int)row.get("TRANAMT");
				DecimalFormat df = new DecimalFormat("0.00");
				row.put("STRANAMT", df.format(amt/100d));
				Date c = (Date)row.get("CREATETIME");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				row.put("SCREATETIME", sdf.format(c));
			}
			PagerPlugin<Map<String,Object>> pager = new PagerPlugin<Map<String,Object>>();
			pager.setList(list);
			pager.setRowCount(list.size());			
			msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
			msg.put(Constants.RESULT_CONTENT, pager);	
		}else{
			msg.put(Constants.FAIL_TEXT, "无权限查询该用户");
			return msg;
		}	
		return msg;
	}
}
