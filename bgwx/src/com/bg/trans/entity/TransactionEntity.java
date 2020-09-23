package com.bg.trans.entity;

import java.sql.Timestamp;

public class TransactionEntity {
	
	private Integer id;
	private String orderno;
	private String reqcontent;
	private String respcontent;
	private String notifycontent;
	private String merchantid;
	private String secmerchantid;
	private int tranamt;
	private String name;
	private String idcard;
	private String bankno;
	private String bankcardno;
	private String bankphone;
	private String cvv2;
	private String cardvaliddate;
	private String channel;	
	private String paychannel;
	private String trantype;
	private String reqno;
	private String status;
	private String originalorderno;
	private String channelno;
	private String transtime;
	private String isnotice;
	private String accountid;
	private String deviceid;
	private String notifyurl;
    private Timestamp createtime;
    private double rate;
    private int fee;
    private String secreqcontent;
    private String secrespcontent;
    
	public String getSecreqcontent() {
		return secreqcontent;
	}

	public void setSecreqcontent(String secreqcontent) {
		this.secreqcontent = secreqcontent;
	}

	public String getSecrespcontent() {
		return secrespcontent;
	}

	public void setSecrespcontent(String secrespcontent) {
		this.secrespcontent = secrespcontent;
	}

	public String getNotifycontent() {
		return notifycontent;
	}

	public void setNotifycontent(String notifycontent) {
		this.notifycontent = notifycontent;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getReqcontent() {
		return reqcontent;
	}

	public void setReqcontent(String reqcontent) {
		this.reqcontent = reqcontent;
	}

	public String getRespcontent() {
		return respcontent;
	}

	public void setRespcontent(String respcontent) {
		this.respcontent = respcontent;
	}


	public String getSecmerchantid() {
		return secmerchantid;
	}

	public void setSecmerchantid(String secmerchantid) {
		this.secmerchantid = secmerchantid;
	}

	public int getTranamt() {
		return tranamt;
	}

	public void setTranamt(int tranamt) {
		this.tranamt = tranamt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public String getBankphone() {
		return bankphone;
	}

	public void setBankphone(String bankphone) {
		this.bankphone = bankphone;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getCardvaliddate() {
		return cardvaliddate;
	}

	public void setCardvaliddate(String cardvaliddate) {
		this.cardvaliddate = cardvaliddate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPaychannel() {
		return paychannel;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public String getTrantype() {
		return trantype;
	}

	public void setTrantype(String trantype) {
		this.trantype = trantype;
	}

	

	public String getReqno() {
		return reqno;
	}

	public void setReqno(String reqno) {
		this.reqno = reqno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriginalorderno() {
		return originalorderno;
	}

	public void setOriginalorderno(String originalorderno) {
		this.originalorderno = originalorderno;
	}

	public String getChannelno() {
		return channelno;
	}

	public void setChannelno(String channelno) {
		this.channelno = channelno;
	}

	public String getTranstime() {
		return transtime;
	}

	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}

	public String getIsnotice() {
		return isnotice;
	}

	public void setIsnotice(String isnotice) {
		this.isnotice = isnotice;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	
	
	
}
