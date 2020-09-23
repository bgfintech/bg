package com.bg.trans.entity.zyht;

public class AccessToken {
	private static String accessToken;//访问令牌	String	获取accessToken的值
	private static long expireDate;//expireDate	有效期	long	令牌的有效截止日期，数值为时间戳
	public static String getAccessToken() {
		return accessToken;
	}
	public static void setAccessToken(String accessToken) {
		AccessToken.accessToken = accessToken;
	}
	public static long getExpireDate() {
		return expireDate;
	}
	public static void setExpireDate(long expireDate) {
		AccessToken.expireDate = expireDate;
	}
}
