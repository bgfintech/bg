package com.bg.web.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.web.dao.IQRCodeDao;
@Repository("QRCodeDao")
public class QRCodeDaoImpl extends BaseJDBCDao implements IQRCodeDao {
	private static final String UPDATE_ACCOUNT_QRCODE="update b_account set qrcodeurl=? where account=?";
	private static final String UPDATE_ACCOUNT_QRCODE2="update b_account set qrcodeurl2=? where account=?";
	//private static final String INSERT_SUBSCRIBE_LOG="insert into b_subscribe_log (openid,recommender,activatetype,createtime) values (?,?,?,?) ";
	private static final String INSERT_SUBSCRIBE_LOG="insert into b_subscribe_log (openid,recommender,createtime) values (?,?,?) ";
	
	@Override
	public void updateQRCodeInfo(String account, String qrcodeUrl) {
		Object[] obj = new Object[]{qrcodeUrl,account};
		this.getJdbcTemplate().update(UPDATE_ACCOUNT_QRCODE,obj);
	}
	@Override
	public void insertSubscribeLog(String openid, String recommender) {
		Object[] obj = new Object[]{openid,recommender,new Date()};
		this.getJdbcTemplate().update(INSERT_SUBSCRIBE_LOG,obj);		
	}
	
	/*
	@Override
	public void updateQRCodeInfo(String account, String qrcodeUrl,String qrCodeType) {
		Object[] obj = new Object[]{qrcodeUrl,account};
		if("02".equals(qrCodeType)){
			this.getJdbcTemplate().update(UPDATE_ACCOUNT_QRCODE2,obj);
		}else{
			this.getJdbcTemplate().update(UPDATE_ACCOUNT_QRCODE,obj);
		}
		
	}
	@Override
	public void insertSubscribeLog(String openid, String recommender,String activatetype) {
		Object[] obj = new Object[]{openid,recommender,activatetype,new Date()};
		this.getJdbcTemplate().update(INSERT_SUBSCRIBE_LOG,obj);		
	}
	*/
}
