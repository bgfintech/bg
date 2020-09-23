package com.bg.trans.entity.zyht;

import java.util.List;

public class BillFindByPageParams {
	private String accountId;//账户ID	String	32	是	进件成功后返回的唯一账户ID。
	private String channelFlag;//交易渠道标识	String	1	否	A：微信；	F：支付宝；
	private String pageNumber;//查询页号	String	20	否	页号是整数，格式化成字符串传送，如果不填则默认是第1页；
	private String pageSize;//每页行数	String	20	否	1页数据行数，是整数，格式化成字符串传送，如果不填默认是50，最大10000；
	/*
	 * 交易代码	String	4	否	
	 * 	6001：微信刷卡支付(被扫)；
		6003：微信当日撤销；
		6004：微信扫码支付(主扫)；
		6005：微信退货；
		6006：微信台码支付(静态码)；
		7001：支付宝刷卡支付(被扫)；
		7003：支付宝当日撤销；
		7004：支付宝扫码支付(主扫)；
		7005：支付宝退货；
		7006：支付宝台码支付(静态码)；
	 */
	private String trCode;
	private String trId;//服务端交易流水号	String	64	否	服务端交易流水号；
	private String oriTrId;//原服务端交易流水号	String	64	否	原服务端交易流水号
	private String startDate;//开始日期	String	10	否	yyyy-MM-dd 例如:2017-10-01
	private String endDate;//结束日期	String	10	否	yyyy-MM-dd 例如:2017-10-01
	private String agentOrderNo;//商户订单号	String	64	否	
	private String merOrderNo;//收单系统订单号	String	64	否	
	private String channelOrderNo;//支付渠道订单号	String	64	否	
	private String cancelFlag;//原交易撤销标志	String	1	否	返回原交易撤销标志：
	private String toalRows;//总记录数	String	20	总记录数是整数，格式化成字符串传送；
	private String totalPages;//总页数	String	20	总页数是整数，格式化成字符串传送；
	private List<BillResult> resultList;
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTrCode() {
		return trCode;
	}

	public void setTrCode(String trCode) {
		this.trCode = trCode;
	}

	public String getTrId() {
		return trId;
	}

	public void setTrId(String trId) {
		this.trId = trId;
	}

	public String getOriTrId() {
		return oriTrId;
	}

	public void setOriTrId(String oriTrId) {
		this.oriTrId = oriTrId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAgentOrderNo() {
		return agentOrderNo;
	}

	public void setAgentOrderNo(String agentOrderNo) {
		this.agentOrderNo = agentOrderNo;
	}

	public String getMerOrderNo() {
		return merOrderNo;
	}

	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}

	public String getChannelOrderNo() {
		return channelOrderNo;
	}

	public void setChannelOrderNo(String channelOrderNo) {
		this.channelOrderNo = channelOrderNo;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getToalRows() {
		return toalRows;
	}

	public void setToalRows(String toalRows) {
		this.toalRows = toalRows;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	public List<BillResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<BillResult> resultList) {
		this.resultList = resultList;
	}

	public class BillResult{
		private String trId;//服务端流水号	String	64
		private String transDate;//交易日期	String	8	yyyy-MM-dd
		/*
		 * 交易代码	String	8	
		 * 	00006001：微信刷卡支付(被扫)；
			00006003：微信当日撤销；
			00006004：微信扫码支付(主扫)；
			00006005：微信退货；
			00006006：微信台码支付(静态码)；
			00007001：支付宝刷卡支付(被扫)；
			00007003：支付宝当日撤销；
			00007004：支付宝扫码支付(主扫)；
			00007005：支付宝退货；
			00007006：支付宝台码支付(静态码)；
		 */
		private String trCode;
		private String trName;//交易名称	String	100	交易名称
		private String agentOrderNo;//商户订单号	String	64	商户订单号
		private String merOrderNo;//收单系统订单号	String	64	收单系统订单号
		private String channelOrderNo;//支付渠道订单号	String	64	
		private String revCeFlag;//交易渠道	String	60
		private String rspCode;//服务端响应代码	String	4	00:成功；		NN:失败
		private String rspMsg;//服务端响应信息	String	100	服务端响应信息
		private String accountId;//账户ID	String	32	唯一账户ID。
		private String merName;//商户名称	String	255
		private String qrCode;//台码编号	String	40
		private String cardType;//交易卡类型	String	50	
		private String trAmt;//交易金额	String	20	Decimal(9,2)的字符串格式：#######.##
		private String currType;//货币类型	String	50	
		private String cancelFlag;//撤销标志	String	50	撤销、正常、冲正
		private String oriTrId;//原服务端交易流水号	String	64
		private String oriTrDate;//原服务端交易日期	String	8	yyyyMMdd
		private String riTrAmt;//原交易金额	String	20	Decimal(9,2)的字符串格式：#######.##
		private String customRegion1;//自定义字段	String	2000	自定义域,键值对竖线分割，例：key1=value1|key2=value2
		public String getTrId() {
			return trId;
		}
		public void setTrId(String trId) {
			this.trId = trId;
		}
		public String getTransDate() {
			return transDate;
		}
		public void setTransDate(String transDate) {
			this.transDate = transDate;
		}
		public String getTrCode() {
			return trCode;
		}
		public void setTrCode(String trCode) {
			this.trCode = trCode;
		}
		public String getTrName() {
			return trName;
		}
		public void setTrName(String trName) {
			this.trName = trName;
		}
		public String getAgentOrderNo() {
			return agentOrderNo;
		}
		public void setAgentOrderNo(String agentOrderNo) {
			this.agentOrderNo = agentOrderNo;
		}
		public String getMerOrderNo() {
			return merOrderNo;
		}
		public void setMerOrderNo(String merOrderNo) {
			this.merOrderNo = merOrderNo;
		}
		public String getChannelOrderNo() {
			return channelOrderNo;
		}
		public void setChannelOrderNo(String channelOrderNo) {
			this.channelOrderNo = channelOrderNo;
		}
		public String getRevCeFlag() {
			return revCeFlag;
		}
		public void setRevCeFlag(String revCeFlag) {
			this.revCeFlag = revCeFlag;
		}
		public String getRspCode() {
			return rspCode;
		}
		public void setRspCode(String rspCode) {
			this.rspCode = rspCode;
		}
		public String getRspMsg() {
			return rspMsg;
		}
		public void setRspMsg(String rspMsg) {
			this.rspMsg = rspMsg;
		}
		public String getAccountId() {
			return accountId;
		}
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}
		public String getMerName() {
			return merName;
		}
		public void setMerName(String merName) {
			this.merName = merName;
		}
		public String getQrCode() {
			return qrCode;
		}
		public void setQrCode(String qrCode) {
			this.qrCode = qrCode;
		}
		public String getCardType() {
			return cardType;
		}
		public void setCardType(String cardType) {
			this.cardType = cardType;
		}
		public String getTrAmt() {
			return trAmt;
		}
		public void setTrAmt(String trAmt) {
			this.trAmt = trAmt;
		}
		public String getCurrType() {
			return currType;
		}
		public void setCurrType(String currType) {
			this.currType = currType;
		}
		public String getCancelFlag() {
			return cancelFlag;
		}
		public void setCancelFlag(String cancelFlag) {
			this.cancelFlag = cancelFlag;
		}
		public String getOriTrId() {
			return oriTrId;
		}
		public void setOriTrId(String oriTrId) {
			this.oriTrId = oriTrId;
		}
		public String getOriTrDate() {
			return oriTrDate;
		}
		public void setOriTrDate(String oriTrDate) {
			this.oriTrDate = oriTrDate;
		}
		public String getRiTrAmt() {
			return riTrAmt;
		}
		public void setRiTrAmt(String riTrAmt) {
			this.riTrAmt = riTrAmt;
		}
		public String getCustomRegion1() {
			return customRegion1;
		}
		public void setCustomRegion1(String customRegion1) {
			this.customRegion1 = customRegion1;
		}
		
	}
}
