package com.bg.trans.service.impl;


import java.util.HashMap;
import java.util.Map;





import org.apache.log4j.Logger;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weixin.guanjia.core.entity.common.AccessToken;
import weixin.guanjia.core.util.WeixinUtil;
import net.sf.json.JSONObject;

import com.bg.trans.service.IWXNoticeService;
import com.bg.util.SetSystemProperty;

@Service("wxNoticeService")
public class WXNoticeServiceImpl implements IWXNoticeService {
	
	private static Logger logger = Logger.getLogger(WXNoticeServiceImpl.class);
	
	@Autowired
	private SystemService systemService;
	
	private String appid = SetSystemProperty.getKeyValue("wx.appid", "sysConfig.properties");
	private String appsecret = SetSystemProperty.getKeyValue("wx.appsecret", "sysConfig.properties");
	private String swipe_templateid = SetSystemProperty.getKeyValue("wx.swipe_templateid", "sysConfig.properties");
	private String transreward_templateid = SetSystemProperty.getKeyValue("wx.transreward_templateid", "sysConfig.properties");
	private String useIntroduceMediaId = "6jrFak90Do2OGWqxnwmd0Uk5VbrgYFz2xCHxlu62mvs";
	//private String useIntroduceMediaId = "6jrFak90Do2OGWqxnwmd0f3kTHdo47O3Xjhbub6BfQw";
	
	
	@Override
	public String gbpSwipeNotice(String openId,String orderno,String bankCardno,String amt,String date,String status,String remark) {
		Map<String,Object> jm = new HashMap<String,Object>();
		String touser=openId;
		Map<String,Object> data = new HashMap<String,Object>();
		jm.put("touser", touser);
		jm.put("template_id", swipe_templateid);
		//jm.put("url", "http://wx.tianxiacredit.cn/jeewx/acctController.do?showtrans&orderno="+orderno+"&openid="+openId);
		jm.put("data", data); 		
		//first
		String firstStr = "";
		if(status.equals("01")){
			firstStr = "交易成功!\n订单号: "+orderno;
		}else{
			firstStr = "交易失败!\n订单号: "+orderno;
		}
		Map<String,String> first = new HashMap<String,String>();
		first.put("value", firstStr);
		first.put("color", "#173177");
		data.put("first", first);
		//keyword1
		Map<String,String> keyword1 = new HashMap<String,String>();
		keyword1.put("value", bankCardno);
		keyword1.put("color", "#173177");
		data.put("keyword1", keyword1);
		//keyword2
		Map<String,String> keyword2 = new HashMap<String,String>();
		keyword2.put("value", amt+"元");
		keyword2.put("color", "#173177");
		data.put("keyword2", keyword2);
		//remark
		Map<String,String> remarkStr = new HashMap<String,String>();
		remarkStr.put("value", remark);
		remarkStr.put("color", "#173177");
		data.put("remark", remarkStr);
		String json = JSONObject.fromObject(jm).toString();
		AccessToken token=WeixinUtil.getAccessToken(systemService, appid, appsecret);
		JSONObject result = WeixinUtil.httpRequest(WeixinUtil.send_templatemessage_url.replace("ACCESS_TOKEN", token.getToken()), "POST", json.toString());		
		return result.toString();
	}
	public static void main(String[] arg){
		String a = "123456789";
		System.out.println(a.substring(a.length()-4,a.length()));
	}
	@Override
	public String gbpTransRewardNotice(String openId, String orderno,String amt) {
		Map<String,Object> jm = new HashMap<String,Object>();
		String touser=openId;
		Map<String,Object> data = new HashMap<String,Object>();
		jm.put("touser", touser);
		jm.put("template_id", transreward_templateid);
		//jm.put("url", "http://wx.tianxiacredit.cn/jeewx/acctController.do?showtrans&orderno="+orderno+"&openid="+openId);
		jm.put("data", data); 		
		//first
		String firstStr = "您有一笔推广分润已到账,可随时提现!";		
		Map<String,String> first = new HashMap<String,String>();
		first.put("value", firstStr);
		first.put("color", "#173177");
		data.put("first", first);
		//order	
		Map<String,String> order = new HashMap<String,String>();
		order.put("value", orderno);
		order.put("color", "#173177");
		data.put("order", order);
		//money	
		Map<String,String> money = new HashMap<String,String>();
		money.put("value", amt+"元");
		money.put("color", "#173177");
		data.put("money", money);
		//remark
//		Map<String,String> remarkStr = new HashMap<String,String>();
//		remarkStr.put("value", remark);
//		remarkStr.put("color", "#173177");
//		data.put("remark", remarkStr);
		String json = JSONObject.fromObject(jm).toString();
		AccessToken token=WeixinUtil.getAccessToken(systemService, appid, appsecret);
		JSONObject result = WeixinUtil.httpRequest(WeixinUtil.send_templatemessage_url.replace("ACCESS_TOKEN", token.getToken()), "POST", json.toString());
		System.out.println(result);
		return result.toString();
	}
	@Override
	public String useIntroduceNotice(String openId) {
		Map<String,Object> jm = new HashMap<String,Object>();
		String touser=openId;
		jm.put("touser", touser);
		jm.put("msgtype", "image");
		Map<String,Object> img = new HashMap<String,Object>();
		img.put("media_id", useIntroduceMediaId);
		//img.put("media_id", "6jrFak90Do2OGWqxnwmd0f3kTHdo47O3Xjhbub6BfQw");
		jm.put("image", img);
		String json = JSONObject.fromObject(jm).toString();
		AccessToken token=WeixinUtil.getAccessToken(systemService, appid, appsecret);
		JSONObject result = WeixinUtil.httpRequest(WeixinUtil.send_message_url.replace("ACCESS_TOKEN", token.getToken()), "POST", json.toString());
		System.out.println(result);
		return result.toString();
	}
}
