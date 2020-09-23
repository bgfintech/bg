package com.bg.web.service;

import com.bg.web.entity.AccountInfo;

public interface IQRCodeService {
	String getQRCodeUrl(AccountInfo account);
	/**
	 * 获取推广二维码
	 * @param account
	 * @param qrCodeType
	 * @return
	 */
	//String getQRCodeUrl(AccountInfo account,String qrCodeType);
	/**
	 * 新增关注记录
	 * @param fromUserName
	 * @param eventKey
	 */
	void insertSubscribeLog(String fromUserName, String eventKey);
}
