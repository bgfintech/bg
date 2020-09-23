package com.bg.trans.service;

public interface IWXNoticeService {
	/**
	 * 同名卡刷卡通知
	 * @param openId
	 * @param amt
	 * @param date
	 * @return
	 */
	String gbpSwipeNotice(String openId,String orderno,String bankCardno,String amt,String date,String status,String remark);
	/**
	 * 分润到账通知
	 * @param openId
	 * @param orderno
	 * @param amt
	 * @param date
	 * @return
	 */
	String gbpTransRewardNotice(String openId,String orderno,String amt);
	/**
	 * 使用说明通知
	 * @param openId
	 * @return
	 */
	String useIntroduceNotice(String openId);
}
