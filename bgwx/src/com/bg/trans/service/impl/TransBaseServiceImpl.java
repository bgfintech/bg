package com.bg.trans.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransRsp;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.hftx.DivDetailEntity;
import com.bg.trans.entity.hftx.Trans101Req;
import com.bg.trans.entity.hftx.Trans101Rsp;
import com.bg.trans.entity.hftx.Trans104Req;
import com.bg.trans.entity.hftx.Trans104Rsp;
import com.bg.trans.entity.hftx.Trans112Req;
import com.bg.trans.entity.hftx.Trans112Rsp;
import com.bg.trans.entity.hftx.Trans201Req;
import com.bg.trans.entity.hftx.Trans216Req;
import com.bg.trans.entity.hftx.Trans216Rsp;
import com.bg.trans.entity.hftx.Trans217Req;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransHFTXService;
import com.bg.util.DateUtil;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.entity.AccountInfo;

@Service("transBaseService")
public class TransBaseServiceImpl implements ITransBaseService {
	private static Logger logger = Logger.getLogger(TransBaseServiceImpl.class);
	@Autowired
	private ITransDao transDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private ITransHFTXService transHFTXService;
	private static Map<String,String> banknameMap=null;
	@Override
	public String bankno2name(String bankno) {
		if(banknameMap==null){
			List<Map<String,Object>> list = transDao.findBanknoByChannel(PARA_CONSTATNTS.Channel_HFTX);
			banknameMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String bno = (String)m.get("BANKNO");
				String bankname = (String)m.get("BANKNAME");
				banknameMap.put(bno, bankname);
			}
		}
		return banknameMap.get(bankno);
	}
	
	@Override
	public TransactionEntity queryOrder(String orderno) {
		return transDao.queryTransactionByOrderNo(orderno);
	}

	@Override
	public List<TransactionEntity> queryMerchantOrder(String merchantId,String date) {
		return transDao.queryMerchantOrder(merchantId, date);
	}

	@Override
	public List<TransactionEntity> queryPayOrder(String accountId, String date) {
		return transDao.queryPayOrder(accountId, date);
	}

	@Override
	public void updateThreeVertify(String name, String idcard, String bankno,String bankcardno, String vertify, String bankcardtype) {
		transDao.updateThreeVertify(name, idcard, bankno, bankcardno, vertify,bankcardtype);
	}

	@Override
	public TransactionEntity queryOrderByReqNo(String reqno, String mid) {
		return transDao.queryTransactionByReqNo(reqno, mid);
	}

	@Override
	public TransRsp createTransAccount(String name,String idcard, String mobile, String prov,String area,TransactionEntity entity) {		
		String orderId = UtilData.createOnlyData();	
		String date = DateUtil.getDateString(new Date(), "yyyyMMdd");
		Trans101Req req = new Trans101Req();
		req.setOrder_id(orderId);
		req.setOrder_date(date);
		req.setSolo_flg("00000100");
		req.setUser_name(name);
		req.setCert_id(idcard);
		req.setUser_mobile(mobile);
		req.setCust_prov(prov);
		req.setCust_area(area);
		Trans101Rsp rsp = transHFTXService.trans101(req,entity);
		TransRsp result = new TransRsp();
		result.setCode(transHFTXService.transStatusToCode(rsp.getResp_code()));
		result.setMsg(rsp.getResp_desc());
		Map<String,Object> params = result.getParams();
		params.put("custid", rsp.getUser_cust_id());
		params.put("acctid", rsp.getAcct_id());
		return result;
	}

	@Override
	public TransRsp bindCashCard(String custId, String cardno, String bankId,String prov, String area, TransactionEntity entity) {
		String orderId = UtilData.createOnlyData();	
		String date = DateUtil.getDateString(new Date(), "yyyyMMdd");
		Trans104Req req = new Trans104Req(); // 03080000
		req.setUser_cust_id(custId);
		req.setOrder_id(orderId);
		req.setOrder_date(date);
		req.setBank_id(bankId);
		req.setCard_no(cardno);
		req.setCard_prov(prov);
		req.setCard_area(area);
		Trans104Rsp rsp = transHFTXService.trans104(req,entity);
		TransRsp result = new TransRsp();
		result.setCode(transHFTXService.transStatusToCode(rsp.getResp_code()));
		result.setMsg(rsp.getResp_desc());
		Map<String,Object> params = result.getParams();
		params.put("bindCardId", rsp.getCash_bind_card_id());
		return result;
	}

	@Override
	public TransRsp gbpPayApply(AccountInfo accountInfo,String custId,String custAcctId, String bankCardno,String bankphone, String transamt,TransactionEntity entity) {
		String accountid = entity.getAccountid();
		Map<String,Object> cardRow = accountDao.findBindCardId(accountid, bankCardno);
		TransRsp result = new TransRsp();
		String orderId = UtilData.createOnlyData();	
		String date = DateUtil.getDateString(new Date(), "yyyyMMdd");	
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		double dTransamt = Double.parseDouble(transamt);
		/********计算分账金额*******************/
		//目前只计算TX客户到账和平台费用
		decimalFormat.setRoundingMode(RoundingMode.UP);			
		double gbpfee = dTransamt*accountInfo.getGbprate()+(accountInfo.getGbpfee()/100);			
		String sgbpfee = decimalFormat.format(gbpfee);//手续费
		double gbpamt = dTransamt-Double.parseDouble(sgbpfee);		
		String sgbpamt = decimalFormat.format(gbpamt);//TX后到账金额		
		
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId(custId);
		divEntity.setDivAcctId(custAcctId);
		divEntity.setDivAmt(sgbpamt);
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		divEntity = new DivDetailEntity();
		divEntity.setDivCustId(transHFTXService.getMerCustId());
		divEntity.setDivAcctId(transHFTXService.getMerAcctId());
		divEntity.setDivAmt(sgbpfee);
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		/*******计算分账金额end***************************/	
		//该卡如果没有绑卡,走统合版216 否则走112
		if(cardRow==null||cardRow.get("HFTXBINDID")==null){					
			Trans216Req req = new Trans216Req();		
			req.setUser_cust_id(custId);
			req.setOrder_id(orderId);
			req.setOrder_date(date);
			req.setTrans_amt(transamt);
			req.setCard_no(bankCardno);
			req.setCard_mobile(bankphone);
//			req.setPayer_term_type("01");		
//			req.setCard_prov("");
//			req.setCard_area("");
			
			req.setDiv_detail(list);				
			/**计算扣除费用后的转账金额end**/
			Trans216Rsp rsp = transHFTXService.trans216(req,entity);			
			result.setCode(transHFTXService.transStatusToCode(rsp.getResp_code()));
			result.setMsg(rsp.getResp_desc());
			Map<String,Object> params = result.getParams();
			params.put("orderno", rsp.getOrder_id());
			params.put("orderdate", date);
		}else{
		//走112
			String bindCardId=(String)cardRow.get("HFTXBINDID");
			Trans112Req req = new Trans112Req();
			req.setUser_cust_id(custId);
			req.setOrder_id(orderId);
			req.setOrder_date(date);
			req.setTrans_amt(transamt);
			req.setBind_card_id(bindCardId);			
			req.setPayer_term_type("01");		
			req.setDiv_detail(list);
			Trans112Rsp rsp = transHFTXService.trans112(req,entity);	
			result.setCode(transHFTXService.transStatusToCode(rsp.getResp_code()));
			result.setMsg(rsp.getResp_desc());
			Map<String,Object> params = result.getParams();
			params.put("orderno", rsp.getOrder_id());
			params.put("orderdate", date);
			params.put("bindcardid", bindCardId);
		}		
		return result;
	}

	@Override
	public TransRsp gbpPayConfirm(String bindcardid,String custId, String orderno,String orderDate,String smscode,String openid,String accountid) {
		TransRsp result = new TransRsp();
		//如果有绑卡ID说明申请走的是112快捷否是216统合版
		if(bindcardid==null||"".equals(bindcardid)){
			Trans217Req rq = new Trans217Req();
			rq.setUser_cust_id(custId);
			rq.setOrder_id(orderno);
			rq.setOrder_date(orderDate);
			rq.setSms_code(smscode);
			rq.setRequest_type("01");
			rq.setRet_url("openid="+openid+"&accountid="+accountid);
			Map<String,String> map =  transHFTXService.createTrans217Sign(rq);			
			result.setCode("01");
			Map<String,Object> params = result.getParams();
			params.put("url", map.get("url"));
			params.put("cmd_id",  map.get("cmd_id"));
			params.put("version",  map.get("version"));
			params.put("mer_cust_id",  map.get("mer_cust_id"));
			params.put("check_value",  map.get("check_value"));
		}else{
			TransactionEntity trow112 = transDao.queryTransactionByOrderNo(orderno);
			if(trow112==null){
				result.setCode("02");
				return result;
			}
			Trans112Req rq112 = JSONObject.parseObject(trow112.getReqcontent(),Trans112Req.class);
			Trans201Req rq = new Trans201Req();		
			rq.setUser_cust_id(custId);	
			rq.setOrder_id(orderno);
			rq.setOrder_date(orderDate);
			rq.setTrans_amt(rq112.getTrans_amt());
			rq.setBind_card_id(bindcardid);
			rq.setSms_code(smscode);
			rq.setRet_url("openid="+openid+"&accountid="+accountid);
			Map<String,String> map = transHFTXService.createTrans201Sign(rq);
			result.setCode("01");
			Map<String,Object> params = result.getParams();
			params.put("url", map.get("url"));
			params.put("cmd_id",  map.get("cmd_id"));
			params.put("version",  map.get("version"));
			params.put("mer_cust_id",  map.get("mer_cust_id"));
			params.put("check_value",  map.get("check_value"));
		}		
		return result;		
	}
	
	@Override
	@Transactional
	public double merchantBalanceToCash(String mid,double amt,String linkno) {
		double balance = accountDao.findAccountBalance(mid);
		if(balance>0&&balance>=amt&&amt>0){
			Date date =  new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			DecimalFormat df = new DecimalFormat("#");
			transDao.insertAAccountDetail(mid, -amt, balance, balance-amt, PARA_CONSTATNTS.TransCode_BalanceCash, linkno, "b_transaction");
			transDao.updateAccountBalance(mid, balance-amt, date);
		}	
		return balance;
	}
	
	
	@Override
	@Transactional
	public double balanceToCash(String accountId,String deviceId) {
		double balance = accountDao.findAccountBalance(accountId);		
		if(balance>0){
			Date date =  new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			DecimalFormat df = new DecimalFormat("#");
			Map<String,Object> cashCardInfo = accountDao.queryCashCardInfo(accountId, "01");
			String orderno = UtilData.createOnlyData();
			TransactionEntity entity = new TransactionEntity();			
			entity.setAccountid(accountId);
			entity.setDeviceid(deviceId);
			entity.setOrderno(orderno);
			entity.setName((String)cashCardInfo.get("NAME"));
			entity.setIdcard((String)cashCardInfo.get("IDCARD"));
			entity.setBankno((String)cashCardInfo.get("BANKNO"));
			entity.setBankcardno((String)cashCardInfo.get("BANKCARDNO"));
			entity.setTrantype(PARA_CONSTATNTS.TransCode_BalanceCash);
			entity.setTranstime(sdf.format(date));
			entity.setTranamt((new Double(balance*100)).intValue());
			entity.setStatus("01");
			entity.setIsnotice("2");
			int rowId = transDao.insertTransaction(entity);
			transDao.insertAAccountDetail(accountId, -balance, balance, 0d, PARA_CONSTATNTS.TransCode_BalanceCash, String.valueOf(rowId), "b_transaction");
			transDao.updateAccountBalance(accountId, 0d, date);
		}	
		return balance;
	}
	public static void main(String[] arg){
		double a = 1.6;
		System.out.println((new Double(a)).intValue());
	}

	@Override
	public int insertTransaction(TransactionEntity entity) {
		return transDao.insertTransaction(entity);		
	}

	@Override
	public void updateTransaction(String orderNo, String transStatus,String channelNo, String rspdata) {
		TransactionEntity entity = new TransactionEntity();
		entity.setStatus(transStatus);
		entity.setOrderno(orderNo);
		entity.setChannelno(channelNo);
		entity.setNotifycontent(rspdata);
		transDao.updateTransaction(entity);
	}
	
	@Transactional
	@Override
	public void updateAccountBalance(String accountId,double amount,String occurType,String linkno,String linkType){
		double balance = accountDao.findAccountBalance(accountId);
		//accountid 余额账户不存在
		if(balance==-1){
			return;
		}
		double after = UtilData.add(balance, amount);
		transDao.insertAAccountDetail(accountId,amount,balance,after,occurType,linkno,linkType);
		transDao.updateAccountBalance(accountId,after,new Date());
	}
	
}
