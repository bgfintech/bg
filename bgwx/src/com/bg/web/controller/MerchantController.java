package com.bg.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import weixin.util.Constants;
import weixin.util.PagerPlugin;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/merController")
public class MerchantController {
	private static final Logger logger = Logger.getLogger(MerchantController.class);
	
	
	@RequestMapping(params = "goautopay")
	public ModelAndView goAutoPay(HttpServletRequest request) {	
		return new ModelAndView("merchant/autopay");
	}
	
	
	
	/**
	 * 保存商户信息
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "getmerList")
	@ResponseBody
	public Object getmerList(HttpServletRequest req){		
		System.out.println("getmerList============================");
		
		Map<String, Object> msg = new HashMap<String, Object>();
		int currentPageNum = Integer.parseInt(req.getParameter("currentPageNumb"));
		PagerPlugin<Map<String,Object>> pager = new PagerPlugin<Map<String,Object>>();
		List<Map<String,Object>> list = null;		
		pager.setList(list);
		pager.setRowCount(list.size());
		msg.put(Constants.SUCCESS_TEXT, Constants.SUCCESS);
		msg.put(Constants.RESULT_CONTENT, pager);		
		return msg;
	}

	
	
}
