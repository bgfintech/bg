package com.bg.web.entity;

import java.util.Date;

public class AccountInfo {
	private int id;
	private String account;
	private String deviceId;
	private String passwd;
	private String name;
	private String idcard;
	private double gbprate;
	private int	gbpfee;
	private String qrcodeurl;
	private Date qrcodeexpiredate;
	private String qrcodeurl2;
	private String activatetype;
	private String isactivate;
	private String recommender;
	private int recommenderid;
	private String iscertified;
	private Date createtime;
	
	public String getQrcodeurl2() {
		return qrcodeurl2;
	}
	public void setQrcodeurl2(String qrcodeurl2) {
		this.qrcodeurl2 = qrcodeurl2;
	}
	public String getActivatetype() {
		return activatetype;
	}
	public void setActivatetype(String activatetype) {
		this.activatetype = activatetype;
	}
	public String getIsactivate() {
		return isactivate;
	}
	public void setIsactivate(String isactivate) {
		this.isactivate = isactivate;
	}
	public int getRecommenderid() {
		return recommenderid;
	}
	public void setRecommenderid(int recommenderid) {
		this.recommenderid = recommenderid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getIscertified() {
		return iscertified;
	}
	public void setIscertified(String iscertified) {
		this.iscertified = iscertified;
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
	public String getRecommender() {
		return recommender;
	}
	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}
	public String getQrcodeurl() {
		return qrcodeurl;
	}
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}
	public Date getQrcodeexpiredate() {
		return qrcodeexpiredate;
	}
	public void setQrcodeexpiredate(Date qrcodeexpiredate) {
		this.qrcodeexpiredate = qrcodeexpiredate;
	}
	public double getGbprate() {
		return gbprate;
	}
	public void setGbprate(double gbprate) {
		this.gbprate = gbprate;
	}
	public int getGbpfee() {
		return gbpfee;
	}
	public void setGbpfee(int gbpfee) {
		this.gbpfee = gbpfee;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
