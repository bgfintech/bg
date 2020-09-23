package com.bg.web.dao;

import java.util.Date;

public interface IQRCodeDao {
	
	/**
	 * 更新二维码信息
	 * @param account
	 * @param qrcodeUrl
	 */
	void updateQRCodeInfo(String account, String qrcodeUrl);
	/**
	 * 记录订阅关注日志
	 * @param openid
	 * @param recommender
	 */
	void insertSubscribeLog(String openid, String recommender);
	
	/**
	 * 更新二维码信息
	 * @param account
	 * @param qrcodeUrl
	 */
	//void updateQRCodeInfo(String account, String qrcodeUrl,String qrCodeType);
	/**
	 * 记录订阅关注日志
	 * @param openid
	 * @param recommender
	 * @param activatetype 
	 */
	//void insertSubscribeLog(String openid, String recommender, String activatetype);
	
}
