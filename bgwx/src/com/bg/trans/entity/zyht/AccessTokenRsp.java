package com.bg.trans.entity.zyht;

public class AccessTokenRsp {
	private boolean success;//标识符	boolean	获取accessToken是否成功
	private Data data;
	private String message;//消息	String	提示的消息，如果成功，则可能没有，失败则一定会有
	private String code;//消息码	String	提示的消息码，如果成功，则可能没有，失败则一定会有
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public class Data {
		private String status;//状态	String	请求接口状态
		private String accessToken;//访问令牌	String	获取accessToken的值
		private long expireDate;//有效期	long	令牌的有效截止日期，数值为时间戳
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public long getExpireDate() {
			return expireDate;
		}
		public void setExpireDate(long expireDate) {
			this.expireDate = expireDate;
		}
	}
}
