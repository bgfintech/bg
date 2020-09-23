package com.bg.web.dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.bg.util.BaseJDBCDao;
import com.bg.util.UtilData;
import com.bg.web.dao.IMerchantDao;
import com.bg.web.entity.MerchantInfo;
import com.bg.web.service.MerchantServiceI;

import weixin.util.DateUtils;
@Repository("merchantDao")
public class MerchantDaoImpl extends BaseJDBCDao implements IMerchantDao {
    private Logger logger = Logger.getLogger(MerchantDaoImpl.class);
	
//	private static String QUERY_MERLIST_URL = "select * from b_merchant where 1=1";
	private static String QUERY_MERLIST_URL="select * from b_merchant where 1=1";
//	private static String INSERT_MER="insert into b_merchant(addr,area_code,artif_certif_tp,comreg_id" +
//			",contactor,contactor_telephone,enable_mspf_user,group_id" +
//			",install_addr,is_group_mer_shop,legal_id,legal_name,mcc" +
//			",mer_name,mer_short_name,mer_type,mobile_phone" +
//			",net_mchnt_svc_tp,open_bank,pay_fee,real_opt_scope,receive_acc" +
//			",receive_acc_name,receive_acc_type,receive_settlement_period,status,upd_time)" +
//			" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static String INSERT_MER="insert into b_merchant(reqNo,merchantId,secMerchantName,shortName,Province,city,district,address,legalName,contactName,contactPhone,contactEmail,merchantProperty,category,businessScene,remark,certType,certNo,certValidDate,settleBankNo,settleBankAccountNo,settleName,settleBankAcctType,settleAccountCreditOrDebit,upd_time,upd_user,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String INSERT_B_MER_BIND="insert into b_mer_bind(mer_id,acct_id) values(?,?)";
	private static String QUERY_MARCHANTBYID="SELECT * from b_merchant where merchantId=?";
	private static String QUERY_MIDCHANNEL="select * from b_mer_channel where merchantid=? and channel=?";
	private static String QUERY_MERCHANTCHANNELINFO="select c.*,m.publickey from b_mer_channel c,b_merchant m where c.merchantid=m.merchantId and m.merchantId=? and c.status='1'";

	@Override
	public boolean checkMerchant(String merchantId) {
		final List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		this.getJdbcTemplate().query(QUERY_MARCHANTBYID, new Object[]{merchantId},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});		
		if(datas.size()==1){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public Map<String,Object> findMerchantInfoByMerchantId(String merchantId) {
		final List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		this.getJdbcTemplate().query(QUERY_MARCHANTBYID, new Object[]{merchantId},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});		
		
		return datas.size()==0?null:datas.get(0);
	}
	@Override
	public boolean saveBMerBind(String merid, String accid) {
		int rt = this.getJdbcTemplate().update(INSERT_B_MER_BIND,merid,accid);
		return true;
	}
	
	@Override
	public List<Map<String, Object>> getMerchantList() {
		final List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		this.getJdbcTemplate().query(QUERY_MERLIST_URL, new Object[]{},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});		
		return datas;	
	}
	@Override
	public String findChannelMid(String mid, String channel) {
		String obj = this.getJdbcTemplate().query(QUERY_MIDCHANNEL,	new Object[] {mid,channel}, new ResultSetExtractor<String>() {
			public String extractData(ResultSet rs)	throws SQLException, DataAccessException {
				String secmid = null;
				if (rs.next()) {
					secmid = rs.getString("secmid");
				}
				return secmid;
			}
		});
		return obj;
	}
	@Override
	public Map<String, Object> findMerchantChannelInfo(String mid) {
		final List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		this.getJdbcTemplate().query(QUERY_MERCHANTCHANNELINFO, new Object[]{mid},new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				try {
					UtilData.resultSetToList(rs, datas);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
					throw new SQLException(e.toString());
				}
			}
		});			
		return datas.size()==0?null:datas.get(0);
	}
	
	

}
