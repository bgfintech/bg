package com.bg.web.service.impl;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bg.util.SetSystemProperty;
import com.bg.web.dao.IQRCodeDao;
import com.bg.web.entity.AccountInfo;
import com.bg.web.service.AccountServiceI;
import com.bg.web.service.IQRCodeService;
import weixin.guanjia.core.entity.common.AccessToken;
import weixin.guanjia.core.util.WeixinUtil;

@Service("QRCodeService")
public class QRCodeServiceImpl implements IQRCodeService{
	private static Logger log = Logger.getLogger(QRCodeServiceImpl.class);	
	@Autowired
	private SystemService systemService;
	@Autowired
	private IQRCodeDao QRCodeDao;
	@Autowired
	private AccountServiceI accountService;
	private String appid = SetSystemProperty.getKeyValue("wx.appid", "sysConfig.properties");
	private String appsecret = SetSystemProperty.getKeyValue("wx.appsecret", "sysConfig.properties");

	public String getQRCodeUrl(AccountInfo account) {
		String url = account.getQrcodeurl();
//		Date edate = account.getQrcodeexpiredate();
//		Date now = new Date();
//		int days = (int)((edate.getTime()-now.getTime())/1000*60*60*24L);
		if(url==null||"".equals(url)){
			Map<String, Object> m = new HashMap<String, Object>();
			//m.put("expire_seconds", 60*60*24*30);//过期30天
			m.put("action_name", "QR_LIMIT_STR_SCENE");
			Map<String, Map<String, String>> m1 = new HashMap<String, Map<String, String>>();
			Map<String, String> m2 = new HashMap<String, String>();
			m2.put("scene_str", account.getAccount());
			m1.put("scene", m2);
			m.put("action_info", m1);
			JSONObject json = JSONObject.fromObject(m);
			/*------------------拼请求JSON结束-------------------------*/
			AccessToken token=WeixinUtil.getAccessToken(systemService,appid,appsecret);   
			JSONObject result = WeixinUtil.httpRequest(WeixinUtil.qrcode_tmp_url.replace("ACCESS_TOKEN", token.getToken()), "POST", json.toString());
			url = result.getString("url");
//			String exSeconds = result.getString("expire_seconds");
			QRCodeDao.updateQRCodeInfo(account.getAccount(),url);
		}	
		
		return url;
	}
	/**
	@Override
	public String getQRCodeUrl(AccountInfo account,String qrCodeType) {		
		if(qrCodeType==null||"".equals(qrCodeType)){
			qrCodeType="01";//押金激活的二维码
		}
		String url = null;
		String scenesstr = account.getAccount();
		if("01".equals(qrCodeType)){
			url = account.getQrcodeurl();
			scenesstr = account.getAccount();
		}else{
			qrCodeType="02";
			url = account.getQrcodeurl2();
			scenesstr = "S"+account.getAccount();//免押金刷卡
		}
//		Date edate = account.getQrcodeexpiredate();
//		Date now = new Date();
//		int days = (int)((edate.getTime()-now.getTime())/1000*60*60*24L);
		if(url==null||"".equals(url)){
			Map<String, Object> m = new HashMap<String, Object>();
			//m.put("expire_seconds", 60*60*24*30);//过期30天
			m.put("action_name", "QR_LIMIT_STR_SCENE");
			Map<String, Map<String, String>> m1 = new HashMap<String, Map<String, String>>();
			Map<String, String> m2 = new HashMap<String, String>();			
			m2.put("scene_str",scenesstr);
			m1.put("scene", m2);
			m.put("action_info", m1);
			JSONObject json = JSONObject.fromObject(m);
			//------------------拼请求JSON结束-------------------------
			AccessToken token=WeixinUtil.getAccessToken(systemService,appid,appsecret);   
			JSONObject result = WeixinUtil.httpRequest(WeixinUtil.qrcode_tmp_url.replace("ACCESS_TOKEN", token.getToken()), "POST", json.toString());
			url = result.getString("url");
//			String exSeconds = result.getString("expire_seconds");
			QRCodeDao.updateQRCodeInfo(account.getAccount(),url,qrCodeType);
		}	
		
		return url;
	}
*/
	@Override
	public void insertSubscribeLog(String openid, String eventKey) {
		if(eventKey != null && !"".equals(eventKey)){
			String[] eks = eventKey.split("_");			
			String recommender = eventKey.split("_")[1];
			String activatetype="01";
			if(recommender.startsWith("S")){
				activatetype="02";
			}
			QRCodeDao.insertSubscribeLog(openid,recommender);
			//QRCodeDao.insertSubscribeLog(openid,recommender,activatetype);
		}
	}	
}
