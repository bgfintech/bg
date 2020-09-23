package com.bg.util.wx;

import java.io.InputStream;

import com.bg.util.wx.IWXPayDomain.DomainInfo;

public class WXPayConfigByBG extends WXPayConfig {

	private String appid="wxb39502efc5209b41";
	private String mch_id="1557710421";
	private String apikey="N3GcLOsclXUr3NYAVsUaUAFflCP12OGw";
	private String appsecret="6b795b6a61834d397147f2f8aae0b1b7";
	//private String appsecret="315e912b758e1f40c96b843583c4ce45";
	
	public String getAppsecret() {
		return appsecret;
	}

	@Override
	public String getAppID() {
		return appid;
	}

	@Override
	public String getMchID() {
		return mch_id;
	}

	@Override
	public String getKey() {
		return apikey;
	}

	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	IWXPayDomain getWXPayDomain() {
		return new IWXPayDomain() {
			
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				return new DomainInfo(WXPayConstants.DOMAIN_API,true);
			}
		};
	}

}
