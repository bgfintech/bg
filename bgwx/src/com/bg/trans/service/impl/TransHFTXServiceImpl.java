package com.bg.trans.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.hftx.DivDetailEntity;
import com.bg.trans.entity.hftx.Trans101Req;
import com.bg.trans.entity.hftx.Trans101Rsp;
import com.bg.trans.entity.hftx.Trans103Req;
import com.bg.trans.entity.hftx.Trans104Req;
import com.bg.trans.entity.hftx.Trans104Rsp;
import com.bg.trans.entity.hftx.Trans105Req;
import com.bg.trans.entity.hftx.Trans105Rsp;
import com.bg.trans.entity.hftx.Trans110Req;
import com.bg.trans.entity.hftx.Trans110Rsp;
import com.bg.trans.entity.hftx.Trans111Req;
import com.bg.trans.entity.hftx.Trans111Rsp;
import com.bg.trans.entity.hftx.Trans112Req;
import com.bg.trans.entity.hftx.Trans112Rsp;
import com.bg.trans.entity.hftx.Trans201Req;
import com.bg.trans.entity.hftx.Trans201Rsp;
import com.bg.trans.entity.hftx.Trans202Req;
import com.bg.trans.entity.hftx.Trans202Rsp;
import com.bg.trans.entity.hftx.Trans203Req;
import com.bg.trans.entity.hftx.Trans203Rsp;
import com.bg.trans.entity.hftx.Trans216Req;
import com.bg.trans.entity.hftx.Trans216Rsp;
import com.bg.trans.entity.hftx.Trans217Req;
import com.bg.trans.entity.hftx.Trans217Rsp;
import com.bg.trans.entity.hftx.Trans301Req;
import com.bg.trans.entity.hftx.Trans301Rsp;
import com.bg.trans.entity.hftx.Trans303Req;
import com.bg.trans.entity.hftx.Trans303Rsp;
import com.bg.trans.entity.hftx.Trans306Req;
import com.bg.trans.entity.hftx.Trans306Rsp;
import com.bg.trans.service.ITransHFTXService;
import com.bg.trans.service.IWXNoticeService;
import com.bg.util.DateUtil;
import com.bg.util.SetSystemProperty;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.entity.AccountInfo;
import com.huifu.saturn.cfca.CFCASignature;
import com.huifu.saturn.cfca.SignResult;
import com.huifu.saturn.cfca.VerifyResult;
@Service("transHFTXService")
public class TransHFTXServiceImpl implements ITransHFTXService {
	private static Logger logger = Logger.getLogger(TransHFTXServiceImpl.class);
	@Autowired
	private ITransDao transDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IWXNoticeService wxNoticeService;
	// 商户调用Url
	private String httpUrl = SetSystemProperty.getKeyValue("pay.httpUrl","hftx/hftx.properties");
	private String merCustId = SetSystemProperty.getKeyValue("pay.merCustId","hftx/hftx.properties");
	private String merAcctId = SetSystemProperty.getKeyValue("pay.merAcctId","hftx/hftx.properties");
	private String syFile = SetSystemProperty.getKeyValue("pay.syFile","hftx/hftx.properties");
	private String gyFile = SetSystemProperty.getKeyValue("pay.gyFile","hftx/hftx.properties");
	private String syPassword = SetSystemProperty.getKeyValue("pay.syPassword","hftx/hftx.properties");
	private String version = "10";
	private String notifyUrl = SetSystemProperty.getKeyValue("pay.notifyUrl","hftx/hftx.properties");
	private String retUrl = SetSystemProperty.getKeyValue("pay.retUrl","hftx/hftx.properties");
	private static Map<String,String> areaMap=null;
	private static Map<String,String> banknoMap=null;
	
	@PostConstruct
	public void init(){
		if(areaMap==null){
			List<Map<String,Object>> list = transDao.findAreaByChannel(PARA_CONSTATNTS.Channel_HFTX);
			areaMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String code = (String)m.get("CODE");
				String dcode = (String)m.get("DCODE");
				areaMap.put(code, dcode);
			}
		}
		if(banknoMap==null){
			List<Map<String,Object>> list = transDao.findBanknoByChannel(PARA_CONSTATNTS.Channel_HFTX);
			banknoMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String bankno = (String)m.get("BANKNO");
				String dbankno = (String)m.get("DBANKNO");
				banknoMap.put(bankno, dbankno);
			}
		}
	}
	
	@Override
	public Trans101Rsp trans101(Trans101Req req,TransactionEntity entity) {
		/**
		 * 54557 测试返回客户ID 6666000000046231  54580
		 * req.setOrder_id("20180823501004022019");
		 * req.setOrder_date("20180823"); 
		 * req.setSolo_flg("00000100");
		 * req.setUser_name("姓名"); 
		 * req.setCert_id("140101201612122775");
		 * req.setUser_mobile("15126311458"); 
		 * req.setCust_prov("0031");
		 * req.setCust_area("3100");
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "101";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		String prov = req.getCust_prov();
		String area = req.getCust_area();
		req.setCust_prov(areaMap.get(prov));
		req.setCust_area(areaMap.get(area));
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);
		String content = post(cmdId,paramsStr);
		Trans101Rsp rsp = JSONObject.parseObject(content, Trans101Rsp.class);
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_CreateAccount);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		entity.setName(req.getUser_name());
		entity.setIdcard(req.getCert_id());
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);	
		if (content == null) {
			return null;
		}		
		return rsp;
	}

	@Override
	public Trans104Rsp trans104(Trans104Req req,TransactionEntity entity) {
		/**
		 * // 1129的卡是75345 1130的是75347
		 * 
		 * req.setUser_cust_id("6666000000046231");
		 * req.setOrder_id("20180823501004022028");
		 * req.setOrder_date("20180823"); 
		 * req.setBank_id("01050000");
		 * req.setCard_no("6232510000001129"); 
		 * req.setCard_prov("0011");
		 * req.setCard_area("1100");
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "104";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		req.setDc_flag("0");// 只能绑定借记卡		
		String prov = req.getCard_prov();
		String area = req.getCard_area();
		String bankno = req.getBank_id();
		req.setBank_id(banknoMap.get(bankno));
		req.setCard_prov(areaMap.get(prov));
		req.setCard_area(areaMap.get(area));
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);
		String content = post(cmdId,paramsStr);	
		Trans104Rsp rsp = JSONObject.parseObject(content, Trans104Rsp.class);
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_BindCaseCard);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		entity.setBankno(bankno);
		entity.setBankcardno(req.getCard_no());
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);	
		if (content == null) {
			return null;
		}
		return rsp;
	}

	@Override
	public Trans301Rsp trans301(Trans301Req req) {
		/**
		 * req.setOrder_id("20180823501004022028");
		 * req.setOrder_date("20180823"); req.setTrans_type("08");
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "301";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		return JSONObject.parseObject(content, Trans301Rsp.class);
	}

	@Override
	public Trans216Rsp trans216(Trans216Req req,TransactionEntity entity) {
		
		/**
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180824501004022001");
		req.setOrder_date("20180824");
		req.setTrans_amt("10000.00");
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000045782");
		divEntity.setDivAcctId("53993");
		divEntity.setDivAmt("58.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000046231");
		divEntity.setDivAcctId("54580");
		divEntity.setDivAmt("9942.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		req.setDiv_detail(list);
		req.setCard_no("4367480104350827");
		req.setCard_mobile("13621113262");
		req.setPayer_term_type("01");
		*/
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "216";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
//		req.setIn_cust_id(merCustId);
//		req.setIn_acct_id(merAcctId);		
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		Trans216Rsp rsp = JSONObject.parseObject(content, Trans216Rsp.class);	
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayApply);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String samt = decimalFormat.format(Double.parseDouble(req.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(samt));
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);	
		if (content == null) {
			return null;
		}
		return rsp;
	}

	@Override
	public Map<String,String> createTrans217Sign(Trans217Req req) {
		/**
		 rq.setUser_cust_id("6666000000046231");
		rq.setOrder_id("20180827501004022004");
		rq.setOrder_date("20180827");
		rq.setSms_code("111111");
		rq.setRet_url("http://test.widefinance.cn:8082/transController/hftxnotify.do");
		rq.setRequest_type("00");
		*/
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "217";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		String urltmp = req.getRet_url();
		if(urltmp!=null&&!"".equals(urltmp)){
			req.setRet_url(retUrl+urltmp);
		}else{
			req.setRet_url(retUrl);
		}
		System.out.println(retUrl+urltmp);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		//paramsStr="{\"bg_ret_url\":\"http://192.168.0.74:8001/npayCallBack/asyncHandle.do\",\"card_mobile\":\"13621113262\",\"card_no\":\"4367480104351127\",\"cmd_id\":\"216\",\"div_detail\":[{'divAcctId':'53993','divAmt':'58.00','divCustId':'6666000000045782','divFreezeFg':'00'},{'divAcctId':'54557','divAmt':'9942.00','divCustId':'6666000000046231','divFreezeFg':'00'}],\"mer_cust_id\":\"6666000000045782\",\"order_date\":\"20180824\",\"order_id\":\"20180824501004022002\",\"payer_term_type\":\"01\",\"trans_amt\":\"10000.00\",\"user_cust_id\":\"6666000000046231\",\"version\":\"10\"}";
		
		// 签名密文
		String sign = sign(paramsStr);
		if (sign == null) {
			logger.error("加签失败:" + paramsStr);
			return null;
		}
		// 组织post数据
		String postStr = httpUrl+"?cmd_id=" + cmdId + "&version=" + version+ "&mer_cust_id=" + merCustId + "&check_value=" + sign;
		System.out.println("postStr::::::::::"+postStr);
		Map<String,String> post = new HashMap<String,String>();
		post.put("url", httpUrl);
		post.put("cmd_id", cmdId);
		post.put("version", version);
		post.put("mer_cust_id", merCustId);
		post.put("check_value", sign);		
		return post;
	}
	
	@Override
	public Trans112Rsp trans112(Trans112Req req,TransactionEntity entity) {
		/**   4367480104350832  1000002600 
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180827501004022004");
		req.setOrder_date("20180827");
		req.setTrans_amt("10000.00");
		req.setBind_card_id("1000002600");
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000045782");
		divEntity.setDivAcctId("53993");
		divEntity.setDivAmt("58.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000046231");
		divEntity.setDivAcctId("54580");
		divEntity.setDivAmt("9942.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
		req.setDiv_detail(list);
		req.setPayer_term_type("02"); 
		 * 
		 * 
		 * */			
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "112";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
//		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
//		DivDetailEntity divEntity = new DivDetailEntity();
//		divEntity.setDivCustId(merCustId);
//		divEntity.setDivAcctId(merAcctId);
//		divEntity.setDivAmt(req.getTrans_amt());
//		divEntity.setDivFreezeFg("00");
//		list.add(divEntity);
//		req.setDiv_detail(list);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		Trans112Rsp rsp = JSONObject.parseObject(content, Trans112Rsp.class);	
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayApply);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String samt = decimalFormat.format(Double.parseDouble(req.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(samt));
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);	
		if (content == null) {
			return null;
		}
		return rsp;
	}
	
	@Override
	public Map<String,String> createTrans201Sign(Trans201Req req) {
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "201";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		String urltmp = req.getRet_url();
		if(urltmp!=null&&!"".equals(urltmp)){
			req.setRet_url(retUrl+urltmp);
		}else{
			req.setRet_url(retUrl);
		}
		//分账串取申请时的112的
		TransactionEntity trow112 = transDao.queryTransactionByOrderNo(req.getOrder_id());
		Trans112Req req112 = JSONObject.parseObject(trow112.getReqcontent(), Trans112Req.class);
		List<DivDetailEntity> list = req112.getDiv_detail();		
		req.setDiv_detail(list);		
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		//paramsStr="{\"bg_ret_url\":\"http://192.168.0.74:8001/npayCallBack/asyncHandle.do\",\"card_mobile\":\"13621113262\",\"card_no\":\"4367480104351127\",\"cmd_id\":\"216\",\"div_detail\":[{'divAcctId':'53993','divAmt':'58.00','divCustId':'6666000000045782','divFreezeFg':'00'},{'divAcctId':'54557','divAmt':'9942.00','divCustId':'6666000000046231','divFreezeFg':'00'}],\"mer_cust_id\":\"6666000000045782\",\"order_date\":\"20180824\",\"order_id\":\"20180824501004022002\",\"payer_term_type\":\"01\",\"trans_amt\":\"10000.00\",\"user_cust_id\":\"6666000000046231\",\"version\":\"10\"}";
		
		// 签名密文
		String sign = sign(paramsStr);
		if (sign == null) {
			logger.error("加签失败:" + paramsStr);
			return null;
		}
		// 组织post数据
		String postStr = httpUrl+"?cmd_id=" + cmdId + "&version=" + version+ "&mer_cust_id=" + merCustId + "&check_value=" + sign;
		System.out.println("url========"+postStr);
		Map<String,String> post = new HashMap<String,String>();
		post.put("url", httpUrl);
		post.put("cmd_id", cmdId);
		post.put("version", version);
		post.put("mer_cust_id", merCustId);
		post.put("check_value", sign);		
		return post;
	}
	@Override
	public Trans303Rsp trans303(Trans303Req req) {
		/**
		 req.setUser_cust_id("6666000000045782");
		 req.setAcct_id("53993");		
		 */	
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "303";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		return JSONObject.parseObject(content, Trans303Rsp.class);
	}
	@Override
	public Trans203Rsp trans203(Trans203Req req, TransactionEntity entity) {
		/**
		req.setOrder_id("20180829501004022001");
		req.setOrder_date("20180829");
		req.setTransfer_type("02040003");
		req.setOut_cust_id("6666000000045782");
		req.setOut_acct_id("53993");
		req.setIn_cust_id("6666000000046231");
		req.setIn_acct_id("54580");
		req.setTranfer_amt("9942.00"); 
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "203";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		Trans203Rsp rsp = JSONObject.parseObject(content, Trans203Rsp.class);
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_Transfer);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String samt = decimalFormat.format(Double.parseDouble(rsp.getTransfer_amt())*100);
		entity.setTranamt(Integer.parseInt(samt));
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);		
		if (content == null) {
			return null;
		}
		return rsp;
	}
	
	@Override
	public Trans202Rsp trans202(Trans202Req req, TransactionEntity entity) {
		/**
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180829501004022001");
		req.setOrder_date("20180829");
		req.setTrans_amt("9942.00");
		req.setCash_bind_card_id("75345");
		req.setFee_acct_id("53993");
		req.setCash_type("02030000");		
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "202";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		Trans202Rsp rsp = JSONObject.parseObject(content, Trans202Rsp.class);
		Date d = new Date();		
		entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_Cash);
		String status = transStatusToCode(rsp.getResp_code());
		entity.setStatus(status);
		entity.setReqcontent(paramsStr);
		entity.setRespcontent(content);
		entity.setOrderno(req.getOrder_id());
		entity.setMerchantid(req.getMer_cust_id());
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String samt = decimalFormat.format(Double.parseDouble(rsp.getTrans_amt())*100);
		entity.setTranamt(Integer.parseInt(samt));
		entity.setIsnotice("2");		
		transDao.insertTransaction(entity);	
		if (content == null) {
			return null;
		}
		return rsp;
	}
	
	@Override
	public Trans306Rsp trans306(Trans306Req req) {
		/**
		req.setUser_cust_id("6666000000046231");
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "306";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		content = content.replace("\"[", "[");
		content = content.replace("]\"", "]");
		content = content.replace("\\\"", "'");
		return JSONObject.parseObject(content, Trans306Rsp.class);
	}
	@Override
	public Trans105Rsp trans105(Trans105Req req) {
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "105";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		logger.info(paramsStr);
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		return JSONObject.parseObject(content, Trans105Rsp.class);
	}
	@Override
	public Trans110Rsp trans110(Trans110Req req) {
		/**
		
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "110";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		return JSONObject.parseObject(content, Trans110Rsp.class);
	}
	
	@Override
	public Trans111Rsp trans111(Trans111Req req) {
		/**
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022012");
		req.setOrder_date("20180926");
		req.setCard_verify_type("02");		
		req.setBank_id("01050000");
		req.setDc_flag("1");
		req.setCard_no("4367480104350838");
		req.setCard_mobile("13621113262");
		 */
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "111";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		String content = post(cmdId,paramsStr);
		if (content == null) {
			return null;
		}
		return JSONObject.parseObject(content, Trans111Rsp.class);
	}
	@Override
	public String createTrans103Sign(Trans103Req req) {
		/**
		 rq.setUser_cust_id("6666000000046231");
		rq.setOrder_id("20180827501004022004");
		rq.setOrder_date("20180827");
		rq.setSms_code("111111");
		rq.setRet_url("http://test.widefinance.cn:8082/transController/hftxnotify.do");
		rq.setRequest_type("00");
		*/
		// 消息类型 需要在签名密文外额外定义
		String cmdId = "103";
		req.setVersion(version);
		req.setCmd_id(cmdId);
		req.setMer_cust_id(merCustId);
		req.setBg_ret_url(notifyUrl);
		req.setRet_url(retUrl);
		// 请求数据json串
		String paramsStr = JSONObject.toJSONString(req);	
		//paramsStr="{\"bg_ret_url\":\"http://192.168.0.74:8001/npayCallBack/asyncHandle.do\",\"card_mobile\":\"13621113262\",\"card_no\":\"4367480104351127\",\"cmd_id\":\"216\",\"div_detail\":[{'divAcctId':'53993','divAmt':'58.00','divCustId':'6666000000045782','divFreezeFg':'00'},{'divAcctId':'54557','divAmt':'9942.00','divCustId':'6666000000046231','divFreezeFg':'00'}],\"mer_cust_id\":\"6666000000045782\",\"order_date\":\"20180824\",\"order_id\":\"20180824501004022002\",\"payer_term_type\":\"01\",\"trans_amt\":\"10000.00\",\"user_cust_id\":\"6666000000046231\",\"version\":\"10\"}";
		
		// 签名密文
		String sign = sign(paramsStr);
		if (sign == null) {
			logger.error("加签失败:" + paramsStr);
			return null;
		}
		// 组织post数据
		String postStr = "cmd_id=" + cmdId + "&version=" + version+ "&mer_cust_id=" + merCustId + "&check_value=" + sign;
		return postStr;
	}
	public static void main(String[] args) {		
		double a = 8908/100d;
		System.out.println(a);
		
		
		TransHFTXServiceImpl service = new TransHFTXServiceImpl();
		/**
		Trans103Req rq = new Trans103Req();
		rq.setUser_cust_id("6666000000046231");
		rq.setOrder_id("20180926501004022012");
		rq.setOrder_date("20180926");
		rq.setDc_flag("1");
		rq.setCard_no("4367480104350838");
		rq.setCard_mobile("13621113262");	
		rq.setSms_code("111111");
		rq.setRet_url("http://test.widefinance.cn");
		String paramsStr = service.createTrans103Sign(rq);
		System.out.println(paramsStr);
		*/
		/**
		Trans111Req req = new Trans111Req();
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022012");
		req.setOrder_date("20180926");
		req.setCard_verify_type("02");		
		req.setBank_id("01050000");
		req.setDc_flag("1");
		req.setCard_no("4367480104350838");
		req.setCard_mobile("13621113262");
		Trans111Rsp rsp = service.trans111(req);	
		*/
		/**
		
		Trans105Req req = new Trans105Req();
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022011");
		req.setOrder_date("20180926");
		req.setBank_id("01050000");
		req.setCard_id("1000003479");
		req.setCard_buss_type("2");
		req.setSms_order_date("20180926");
		req.setSms_order_id("20180926501004022010");
		req.setSms_code("111111");
		req.setCard_mobile("13621113262");
		Trans105Rsp rsp = service.trans105(req);	
		 */
		/**	
		Trans110Req req = new Trans110Req();
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022010");
		req.setOrder_date("20180926");
		req.setBusiness_type("105");
		req.setUser_mobile("13621113262");
		Trans110Rsp rsp = service.trans110(req);	
		*/
		/**
		Trans306Req req = new Trans306Req();
		req.setUser_cust_id("6666000000046231");
		Trans306Rsp rsp = service.trans306(req);		
		*/
		
		/**
		Trans202Req req = new Trans202Req();
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022009");
		req.setOrder_date("20180926");
		req.setTrans_amt("9942.00");
		req.setCash_bind_card_id("75345");
		req.setFee_acct_id("53993");
		req.setCash_type("02030000");		
		Trans202Rsp rsp = service.trans202(req);
		*/
		
		
		/**
		Trans203Req req = new Trans203Req();
		req.setOrder_id("20180926501004022008");
		req.setOrder_date("20180926");
		req.setTransfer_type("02040003");
		req.setOut_cust_id("6666000000045782");
		req.setOut_acct_id("53993");
		req.setIn_cust_id("6666000000046231");
		req.setIn_acct_id("54580");
		req.setTransfer_amt("9942.00");
		Trans203Rsp rsp = service.trans203(req);
		*/
		/**
		Trans303Req req = new Trans303Req();
		req.setUser_cust_id("6666000000045782");
		req.setAcct_id("53993");		
		Trans303Rsp rsp = service.trans303(req);
		*/
		
		/**   4367480104350832  1000002600 	
		Trans112Req req = new Trans112Req();
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022007");
		req.setOrder_date("20180926");
		req.setTrans_amt("10000.00");
		req.setBind_card_id("1000003479");
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000045782");
		divEntity.setDivAcctId("53993");
		divEntity.setDivAmt("10000.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
//		divEntity = new DivDetailEntity();
//		divEntity.setDivCustId("6666000000046231");
//		divEntity.setDivAcctId("54580");
//		divEntity.setDivAmt("9942.00");
//		divEntity.setDivFreezeFg("00");
//		list.add(divEntity);
		req.setDiv_detail(list);
		req.setPayer_term_type("01");
		Trans112Rsp rsp = service.trans112(req);	
		*/
		/**
		Trans201Req rq = new Trans201Req();		
		rq.setUser_cust_id("6666000000046231");	
		rq.setOrder_id("20180926501004022007");
		rq.setOrder_date("20180926");
		rq.setTrans_amt("10000.00");
		rq.setBind_card_id("1000003479");
		rq.setSms_code("111111");
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000045782");
		divEntity.setDivAcctId("53993");
		divEntity.setDivAmt("10000.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
//		divEntity = new DivDetailEntity();
//		divEntity.setDivCustId("6666000000046231");
//		divEntity.setDivAcctId("54580");
//		divEntity.setDivAmt("9942.00");
//		divEntity.setDivFreezeFg("00");
//		list.add(divEntity);
		rq.setDiv_detail(list);
		rq.setPayer_term_type("01");
		String paramsStr = service.createTrans201Sign(rq);
		System.out.println(paramsStr);
		*/
		/**
		Trans216Req req = new Trans216Req();		
		req.setUser_cust_id("6666000000046231");
		req.setOrder_id("20180926501004022006");
		req.setOrder_date("20180926");
		req.setTrans_amt("10000.00");
		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
		DivDetailEntity divEntity = new DivDetailEntity();
		divEntity.setDivCustId("6666000000045782");
		divEntity.setDivAcctId("53993");
		divEntity.setDivAmt("10000.00");
		divEntity.setDivFreezeFg("00");
		list.add(divEntity);
//		List<DivDetailEntity> list = new ArrayList<DivDetailEntity>();
//		DivDetailEntity divEntity = new DivDetailEntity();
//		divEntity.setDivCustId("6666000000045782");
//		divEntity.setDivAcctId("53993");
//		divEntity.setDivAmt("58.00");
//		divEntity.setDivFreezeFg("00");
//		list.add(divEntity);
//		divEntity = new DivDetailEntity();
//		divEntity.setDivCustId("6666000000046231");
//		divEntity.setDivAcctId("54580");
//		divEntity.setDivAmt("9942.00");
//		divEntity.setDivFreezeFg("00");
//		list.add(divEntity);
		req.setDiv_detail(list);
		req.setCard_no("4367480104350837");
		req.setCard_mobile("13621113262");
		req.setPayer_term_type("01");		
//		req.setCard_prov("");
//		req.setCard_area("");
		Trans216Rsp rsp = service.trans216(req);	
		*/
		/**	
		Trans217Req rq = new Trans217Req();
		rq.setUser_cust_id("6666000000046231");
		rq.setOrder_id("20180926501004022006");
		rq.setOrder_date("20180926");
		rq.setSms_code("111111");
		rq.setRet_url("http://wx.widefinance.cn");
		rq.setRequest_type("00");
		String paramsStr = service.createTrans217Sign(rq);
		System.out.println(paramsStr);
		*/
		
		
		/** 
		Trans301Req req = new Trans301Req();
		req.setOrder_id("20180926501004022030");
		req.setOrder_date("20180926");
		req.setTrans_type("08");
		Trans301Rsp rsp = service.trans301(req);
		*/
		/**
		 Trans104Req req = new Trans104Req(); // 03080000
		 req.setUser_cust_id("6666000000054825");
		 req.setOrder_id("20180926501004022030");
		 req.setOrder_date("20180926"); 
		 req.setBank_id("01050000");
		 req.setCard_no("6232510000001131"); 
		 req.setCard_prov("0011");
		 req.setCard_area("1100"); 
		 Trans104Rsp rsp = service.trans104(req);
		*/ 
		/**
		 Trans101Req req = new Trans101Req();
		 req.setOrder_id("20180930501004022016");
		 req.setOrder_date("20180930"); 
		 req.setSolo_flg("00000100");
		 req.setUser_name("李五"); 
		 req.setCert_id("110101199003079411");
		 req.setUser_mobile("15126311458"); 
		 req.setCust_prov("0031");
		 req.setCust_area("3100"); 
		 Trans101Rsp rsp = service.trans101(req,new TransactionEntity());
		*/ 

//		System.out.println(JSONObject.toJSONString(rsp));
	}

	private String sign(String params) {
		logger.info("请求参数:" + params);
		// 加签用pfx文件
		String pfxFile = syFile;
		// 加签用密码
		String pfxFilePwd = syPassword;
		// 进行base64转换
		String base64RequestParams = Base64.encodeBase64String(params.getBytes(Charset.forName("utf-8")));
		// 加签
		SignResult signResult = CFCASignature.signature(pfxFile, pfxFilePwd,base64RequestParams, "utf-8");
		if ("000".equals(signResult.getCode())) {
			return signResult.getSign();
		} else {
			logger.error("加签失败:" + params);
			return null;
		}
	}
	
	private String post(String cmdId,String paramsStr) {
		// 签名密文
		String sign = sign(paramsStr);
		if (sign == null) {
			logger.error("加签失败:" + paramsStr);
			return null;
		}
		String body = null;
		try{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("cmd_id", cmdId));
	        params.add(new BasicNameValuePair("version", version));
	        params.add(new BasicNameValuePair("mer_cust_id", merCustId));
	        params.add(new BasicNameValuePair("check_value", sign));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(httpUrl);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);			
			if (response.getStatusLine().getStatusCode() == 200) {
				body = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info(body);
			}else{
				logger.info(httpUrl+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}	
		/************
		HttpRequest httpRequest = HttpRequest.post(httpUrl).charset("UTF-8");
		System.out.println("1111");
		// 组织post数据
		String postStr = "cmd_id=" + cmdId + "&version=" + version+ "&mer_cust_id=" + merCustId + "&check_value=" + sign;
		// 发送请求给汇付
		HttpResponse httpResponse = httpRequest.contentType("application/x-www-form-urlencoded").body(postStr).send();
		System.out.println("222");
		// 取得同步返回数据
		String body = httpResponse.bodyText();
		*/
		String content = parseResult(body);
		return content;
	}

	private String parseResult(String responseJson) {
		// 将json格式字符串转换为json对象
		JSONObject jsonObject = JSON.parseObject(responseJson);
		// 取得返回数据密文
		String sign = jsonObject.getString("check_value");
		// 验签cer文件名
		String cerName = gyFile;
		// 进行验签，参数1为汇付商户号，固定为100001
		VerifyResult verifyResult = CFCASignature.verifyMerSign("100001", sign,	"utf-8", cerName);
		if ("000".equals(verifyResult.getCode())) {
			// 取得base64格式内容
			String content = new String(verifyResult.getContent(),Charset.forName("utf-8"));
			// base64格式解码
			String decrptyContent = new String(Base64.decodeBase64(content),Charset.forName("utf-8"));
			System.out.println("decrptyContent = " + decrptyContent);
			return decrptyContent;
		} else {
			logger.error("验签失败:" + responseJson);
			return null;
		}
	}
	
	@Override
	public Map<String,String> processRetUrl(String sign) {
		// 验签cer文件名
		String cerName = gyFile;
		Map<String,String> map = null;
		String status = null;
		String msg = null;
		// 进行验签，参数1为汇付商户号，固定为100001
		VerifyResult verifyResult = CFCASignature.verifyMerSign("100001",sign,"utf-8", cerName);
		if (verifyResult!=null&&"000".equals(verifyResult.getCode())) {
			// 取得base64格式内容
			String content = new String(verifyResult.getContent(),Charset.forName("utf-8"));
			// base64格式解码
			String decrptyContent = new String(Base64.decodeBase64(content),Charset.forName("utf-8"));
			logger.info("processRetUrl decrptyContent = " + decrptyContent);
			// JSON字符串转JSON对象
			JSONObject jsonObject = JSON.parseObject(decrptyContent);
			// 取得JSON对象内的项目 订单号
			String cmd_id = (String)jsonObject.get("cmd_id");
			status = transStatusToCode(jsonObject.getString("resp_code"));
			msg = jsonObject.getString("resp_desc");
			switch (cmd_id) {
			case "217":
			case "201":
				map = new HashMap<String,String>();
				map.put("retstatus", status);
				map.put("msg", msg);
				break;
			default:
				logger.error("无法解析处理 decrptyContent = " + decrptyContent);
				break;
			}
		} else {
			logger.error("retUrl 验签失败:" + sign);
		}
		return map;
	}	
	

	@Override
	public String processNotify(String sign) {
		//异步通知处理等2秒,主线程的交易流水还没有入库
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 验签cer文件名
		String cerName = gyFile;
		// 进行验签，参数1为汇付商户号，固定为100001
		VerifyResult verifyResult = CFCASignature.verifyMerSign("100001",sign,"utf-8", cerName);
		String extSeqId = null;
		if ("000".equals(verifyResult.getCode())) {
			// 取得base64格式内容
			String content = new String(verifyResult.getContent(),Charset.forName("utf-8"));
			// base64格式解码
			String decrptyContent = new String(Base64.decodeBase64(content),Charset.forName("utf-8"));
			logger.info("proessNotify decrptyContent = " + decrptyContent);
			// JSON字符串转JSON对象
			JSONObject jsonObject = JSON.parseObject(decrptyContent);
			// 取得JSON对象内的项目 订单号
			extSeqId = (String) jsonObject.get("order_id");
			String cmd_id = (String)jsonObject.get("cmd_id");
			switch (cmd_id) {
			case "104":
				process104(decrptyContent);
				break;
			case "217":
				process217(decrptyContent);
				break;
			case "202":
				process202(decrptyContent);
				break;
			case "201":
				process201(decrptyContent);
				break;
			default:
				break;
			}
			// 将订单号连同前缀RECV_ORD_ID_打印出来，汇付可以知道发送给商户发送成功，可避免重复发送
		} else {
			logger.error("验签失败:" + sign);
		}
		return extSeqId;
	}
	
	private void process201(String content) {
		String jsoncontent = content;
		jsoncontent = jsoncontent.replace("\"[", "[");
		jsoncontent = jsoncontent.replace("]\"", "]");
		jsoncontent = jsoncontent.replace("\\\"", "'");
		/**********记录交易流水b_transaction********************************************/
		Trans201Rsp rsp201 = JSONObject.parseObject(jsoncontent, Trans201Rsp.class);
		Date d = new Date();		
		String dstr = DateUtil.getDateString(d, "yyyyMMddHHmmss");
		String respCode = rsp201.getResp_code();
		String orderno = rsp201.getOrder_id();
		TransactionEntity trow112 = transDao.queryTransactionByOrderNo(orderno);
		String transType = trow112.getTrantype();
		String transStatus = trow112.getStatus();
		//判断如果已通知过一次且已是成功的说明已转账及提现
		if(PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm.equals(transType)&&"01".equals(transStatus)){
			return;
		}
		String accountid = trow112.getAccountid();
		int amt112 = trow112.getTranamt();
		TransactionEntity entity201 = new TransactionEntity();
		entity201.setOrderno(orderno);
		entity201.setNotifycontent(content);		
		entity201.setTranstime(dstr);
		entity201.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity201.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity201.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity201.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
		String status201 = transStatusToCode(respCode);
		entity201.setStatus(status201);
		entity201.setTranamt(amt112);
		entity201.setMerchantid(rsp201.getMer_cust_id());
		entity201.setName(trow112.getName());
		entity201.setIdcard(trow112.getIdcard());
		entity201.setBankcardno(trow112.getBankcardno());
		entity201.setBankphone(trow112.getBankphone());
		entity201.setAccountid(accountid);
		entity201.setIsnotice("2");		
		transDao.insertTransaction(entity201);
		String date = DateUtil.getDateString(new Date(), "yyyyMMdd");
		String custId = null;
		String custAcctId = null;
		Map<String,Object> custRow = accountDao.findChannelCustIdAndAcctId(accountid, PARA_CONSTATNTS.Channel_HFTX);	
		custId = (String)custRow.get("CUSTID");
		custAcctId = (String)custRow.get("ACCTID");
//		/*************如果交易成功 转账 ***************************************/
//		if("01".equals(status201)){
//			AccountInfo accountInfo = accountDao.findAccountByAccountId(accountid);
//			/**计算扣除费用后的转账金额**/
//			DecimalFormat decimalFormat = new DecimalFormat("#.00");
//			decimalFormat.setRoundingMode(RoundingMode.UP);
//			double amt203 = (Double.parseDouble(amt112)*(1-accountInfo.getGbprate())-accountInfo.getGbpfee())/100;
//			String samt203 = decimalFormat.format(amt203);			
//			/**计算扣除费用后的转账金额end**/			
//			String orderId203= UtilData.createOnlyData();	
//			Trans203Req req = new Trans203Req();
//			req.setOrder_id(orderId203);
//			req.setOrder_date(date);
//			req.setTransfer_type("02040003");
//			req.setOut_cust_id(merCustId);
//			req.setOut_acct_id(merAcctId);
//			req.setIn_cust_id(custId);
//			req.setIn_acct_id(custAcctId);
//			req.setTransfer_amt(samt203);
//			TransactionEntity entity203 = new TransactionEntity();
//			entity203.setAccountid(accountid);			
//			Trans203Rsp rsp203 = trans203(req,entity203);
//			String status203 = transStatusToCode(rsp203.getResp_code());
//			if("01".equals(status203)){
		if("01".equals(status201)){
			/*************如果交易成功  提现 **********************************/
			Map<String,Object> cashRow = accountDao.queryCashCardInfo(accountid, "01");
			String bindCardId = (String)cashRow.get("HFTXBINDID");				
			String orderId202 = UtilData.createOnlyData();
			Trans202Req req202 = new Trans202Req();
			req202.setUser_cust_id(custId);
			req202.setOrder_id(orderId202);
			req202.setOrder_date(date);
			/********计算提现金********/
			String txamt = "";
			List<DivDetailEntity> dl = rsp201.getDiv_detail();
			for(DivDetailEntity dde:dl){
				if(custId.equals(dde.getDivCustId())){
					txamt = dde.getDivAmt();
				}
			}
			/***********************/
			req202.setTrans_amt(txamt);
			req202.setCash_bind_card_id(bindCardId);
			req202.setFee_acct_id(merAcctId);
			req202.setCash_type("02030000");	//T+0取现	
			TransactionEntity entity202 = new TransactionEntity();
			entity202.setAccountid(accountid);
			entity202.setBankcardno((String)cashRow.get("BANKCARDNO"));
			entity202.setBankno((String)cashRow.get("BANKNO"));
			entity202.setName((String)cashRow.get("NAME"));
			entity202.setIdcard((String)cashRow.get("IDCARD"));
			trans202(req202,entity202);			
		}				
	}

	private void process202(String content) {		
		Trans202Rsp rsp = JSONObject.parseObject(content, Trans202Rsp.class);
		String respCode = rsp.getResp_code();
		TransactionEntity entity = new TransactionEntity();
		entity.setOrderno(rsp.getOrder_id());
		entity.setNotifycontent(content);		
		TransactionEntity qte = transDao.queryTransactionByOrderNo(rsp.getOrder_id());
		String rspStatus = transStatusToCode(respCode);
		entity.setStatus(rspStatus);
		String status  = qte.getStatus();
		double amt = qte.getTranamt()/100;
		//如果状态是成功,则不能修改
		if(!"01".equals(status)){
			transDao.updateTransaction(entity);
		}
		/***********第一笔套现后给推荐人奖励金**************
		System.out.println("rspStatus:"+rspStatus+" :status:"+status);
		if("01".equals(rspStatus)&&!"01".equals(status)){
			String accountId = qte.getAccountid();
			reward3001(accountId,qte.getId()+"","b_transaction");
		}	
		************/	
	}
	
	
	/**
	 * 新用户推广奖劢20元活动(暂定)
	 * @param accountId
	 * @param linkno
	 * @param linkType
	 
	private void reward3001(String accountId,String linkno,String linkType){
		System.out.println(accountId+":"+linkno+":"+linkType);
		DecimalFormat df = new DecimalFormat("0.00");
		double np = 10d;
		double prize = 20d;
		AccountInfo accountInfo = accountDao.findAccountByAccountId(accountId);	
		String recommender = accountInfo.getRecommender();
		if(recommender!=null&&!"".equals(recommender)){
			Map<String,Object> rewardRow = transDao.findRewardSpread(accountId);			
			if(rewardRow==null){								
				try {
					//活动开始时间
					Date activityDate = DateUtil.getDate("20181019","yyyyMMdd");
					//活动期内的进行奖励
					if(accountInfo.getCreatetime().after(activityDate)){	
						Map<String,Object> rowTrans = transDao.findAllTransAmtByAccount(accountId);
						int amt = ((BigDecimal)rowTrans.get("AMT")).intValue();
						System.out.println("amt=============="+amt);
						//首刷2W以上,直接奖励20元
						if(amt>=2000000){
							np=20d;
						}					
						int recommenderId = accountInfo.getRecommenderid();	
						System.out.println("recommenderId:"+recommenderId);
						String channelAccount = null;
	//					AccountInfo recommenderInfo = accountDao.findAccountByAccountId(recommender);						
						//判断是否是渠道推广,更新奖励总金额
						List<Map<String,Object>> parList = accountDao.findParList(accountInfo.getId());						
						for(Map<String,Object> m: parList){						
							String recom = (String)m.get("RECOMMENDER");
							if("15833917568".equals(recom)){
								prize=100d;
								channelAccount="15833917568";
								break;
							}
						}
						int rowId = transDao.insertRewardLog(accountInfo.getAccount(),accountInfo.getRecommender(),np,PARA_CONSTATNTS.Reward_Spread,linkno,linkType,prize,channelAccount,df.format(amt/100d));
						updateAccountBalance(recommender,np,PARA_CONSTATNTS.Reward_Spread,String.valueOf(rowId),"b_reward_log");
					}					
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage(),e);
				}					
			}else{				
				double ramount = (double)rewardRow.get("AMOUNT");
				double rramt = (double)rewardRow.get("REWARDAMT");
				String remark = (String)rewardRow.get("REMARK");
				String channelAccount = (String)rewardRow.get("CHANNELACCOUNT");
				double hisamt = Double.parseDouble(remark);
				int id = (int)rewardRow.get("ID");
				//奖励金小于总奖励金时
				if(ramount<rramt){
					Map<String,Object> rowTrans = transDao.findAllTransAmtByAccount(accountId);
					int amt = ((BigDecimal)rowTrans.get("AMT")).intValue();
					double tamt = amt/100d;
					//用数据库查询的最新累计交易额
					double nowamt = tamt;
					//用单笔通知计算累计交易额
//					double nowamt = hisamt+tranamt;
					int anum = (int)hisamt/10000;
					int lnum = (int)nowamt/10000;
					double qnp=0d;
					double nr=ramount;
					if(hisamt<20000&&nowamt>=20000){
						//个人推广奖励
						np=10d;
						nr+=np;
						if(nr>rramt){
							nr = rramt;
							np = rramt-ramount;
						}
						int rowId = transDao.insertRewardLog(accountInfo.getAccount(),accountInfo.getRecommender(),nr,PARA_CONSTATNTS.Reward_Spread,linkno,linkType,rramt,channelAccount,df.format(nowamt));
						updateAccountBalance(recommender,np,PARA_CONSTATNTS.Reward_Spread,String.valueOf(rowId),"b_reward_log");
						//渠道奖励金	
						int qnum = (int)((nowamt-20000)/10000);
						qnp=qnum*10;
					}else if(hisamt>=20000&&nowamt>=20000){
						qnp = (lnum-anum)*10;
					}			
					double qnr=nr;
					if(qnp>0&&qnr<rramt){
						qnr+=qnp;						
						if(qnr>rramt){
							qnr=rramt;
							qnp=rramt-nr;
						}
						int rowId = transDao.insertRewardLog(accountInfo.getAccount(),channelAccount,qnr,PARA_CONSTATNTS.Reward_ChannelSpread,linkno,linkType,rramt,channelAccount,df.format(nowamt));
						updateAccountBalance(channelAccount,qnp,PARA_CONSTATNTS.Reward_ChannelSpread,String.valueOf(rowId),"b_reward_log");
					}else{
						transDao.updateRewardLogRemark(id,df.format(nowamt));
					}
					
				}
			}
		}			
	}
	*/
	@Transactional
	private void updateAccountBalance(String accountId,double amount,String occurType,String linkno,String linkType){
		double balance = accountDao.findAccountBalance(accountId);
		//accountid 余额账户不存在
		if(balance==-1){
			return;
		}
		double after = UtilData.add(balance, amount);
		transDao.insertAAccountDetail(accountId,amount,balance,after,occurType,linkno,linkType);
		transDao.updateAccountBalance(accountId,after,new Date());
	}
	

	private void process217(String content) {
		String jsoncontent = content;
		jsoncontent = jsoncontent.replace("\"[", "[");
		jsoncontent = jsoncontent.replace("]\"", "]");
		jsoncontent = jsoncontent.replace("\\\"", "'");
		/**********记录交易流水b_transaction********************************************/
		Trans217Rsp rsp217 = JSONObject.parseObject(jsoncontent, Trans217Rsp.class);
		Date d = new Date();		
		String dstr = DateUtil.getDateString(d, "yyyyMMddHHmmss");
		String respCode = rsp217.getResp_code();
		String orderno = rsp217.getOrder_id();
		TransactionEntity trow216 = transDao.queryTransactionByOrderNo(orderno);
		String transType = trow216.getTrantype();
		String transStatus = trow216.getStatus();
		//判断如果支付公司已通知过一次且已是成功的说明已转账及提现
		if(PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm.equals(transType)&&"01".equals(transStatus)){
			return;
		}
		String accountid = trow216.getAccountid();
		int amt216 = trow216.getTranamt();
		TransactionEntity entity217 = new TransactionEntity();
		entity217.setOrderno(orderno);
		entity217.setNotifycontent(content);		
		entity217.setTranstime(dstr);
		entity217.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
		entity217.setChannel(PARA_CONSTATNTS.Channel_HFTX);
		entity217.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
		entity217.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
		String status217 = transStatusToCode(respCode);
		entity217.setStatus(status217);
		entity217.setTranamt(amt216);
		entity217.setMerchantid(rsp217.getMer_cust_id());
		entity217.setName(trow216.getName());
		entity217.setIdcard(trow216.getIdcard());
		entity217.setBankcardno(trow216.getBankcardno());
		entity217.setBankphone(trow216.getBankphone());
		entity217.setAccountid(accountid);
		AccountInfo accountInfo = null;
		DecimalFormat df = new DecimalFormat("0.00");
		double amount216 = UtilData.div(amt216,100,2);
		String amount216Str = df.format(amount216);
		String strTime = DateUtil.getDateString(d, "yyyy-MM-dd HH:mm:ss");
		if(!"03".equals(status217)){
			accountInfo = accountDao.findAccountByAccountId(accountid);				
			String remark = "";
			if("01".equals(status217)){
				remark="感谢您的使用";
			}else if("02".equals(status217)){
				remark=rsp217.getResp_desc();
			}			
			wxNoticeService.gbpSwipeNotice(accountInfo.getDeviceId(),trow216.getOrderno(),trow216.getBankcardno(),amount216Str,strTime,status217, remark);			
			entity217.setIsnotice("1");
		}else{
			entity217.setIsnotice("0");		
		}	
		int rowid = transDao.insertTransaction(entity217);		
		String custId = null;
		String custAcctId = null;
		Map<String,Object> custRow = accountDao.findChannelCustIdAndAcctId(accountid, PARA_CONSTATNTS.Channel_HFTX);
		custId = (String)custRow.get("CUSTID");
		custAcctId = (String)custRow.get("ACCTID");
		String date = DateUtil.getDateString(new Date(), "yyyyMMdd");
//		/*************如果交易成功 转账 ***************************************/		
//		if("01".equals(status217)){
//			//217成功后保存卡信息及绑卡ID			
//			accountDao.insertAccBankCard(accountid, trow216.getName(), trow216.getIdcard(), trow216.getBankno(), trow216.getBankcardno(), trow216.getBankphone(), "02", "02", "0",rsp217.getBind_card_id());
//			AccountInfo accountInfo = accountDao.findAccountByAccountId(accountid);
//			/**计算扣除费用后的转账金额**/
//			DecimalFormat decimalFormat = new DecimalFormat("#.00");
//			decimalFormat.setRoundingMode(RoundingMode.UP);
//			double amt203 = (Double.parseDouble(amt216)*(1-accountInfo.getGbprate())-accountInfo.getGbpfee())/100;
//			String samt203 = decimalFormat.format(amt203);			
//			/**计算扣除费用后的转账金额end**/
//			String orderId217 = UtilData.createOnlyData();	
//			Trans203Req req = new Trans203Req();
//			req.setOrder_id(orderId217);
//			req.setOrder_date(date);
//			req.setTransfer_type("02040003");
//			req.setOut_cust_id(merCustId);
//			req.setOut_acct_id(merAcctId);
//			req.setIn_cust_id(custId);
//			req.setIn_acct_id(custAcctId);
//			req.setTransfer_amt(samt203);
//			TransactionEntity entity203 = new TransactionEntity();
//			entity203.setAccountid(accountid);			
//			entity203.setName(accountInfo.getName());
//			entity203.setIdcard(accountInfo.getIdcard());
//			Trans203Rsp rsp203 = trans203(req,entity203);
//			String status203 = transStatusToCode(rsp203.getResp_code());
//		
//			if("01".equals(status203)){
		if("01".equals(status217)){
//			accountDao.insertAccBankCard(accountid, trow216.getName(), trow216.getIdcard(), trow216.getBankno(), trow216.getBankcardno(), trow216.getBankphone(), "02", "02", "0",rsp217.getBind_card_id());
			/*************如果交易成功 提现 **********************************/
			Map<String,Object> cashRow = accountDao.queryCashCardInfo(accountid, "01");
			String bindCardId = (String)cashRow.get("HFTXBINDID");				
			String orderId202 = UtilData.createOnlyData();
			Trans202Req req202 = new Trans202Req();
			req202.setUser_cust_id(custId);
			req202.setOrder_id(orderId202);
			req202.setOrder_date(date);			
			/********计算提现金********/
			String txamt = "";
			List<DivDetailEntity> dl = rsp217.getDiv_detail();
			for(DivDetailEntity dde:dl){
				if(custId.equals(dde.getDivCustId())){
					txamt = dde.getDivAmt();
				}
			}
			/***********************/
			req202.setTrans_amt(txamt);
			req202.setCash_bind_card_id(bindCardId);
			req202.setFee_acct_id(merAcctId);
			req202.setCash_type("02030000");	//T+0取现	
			TransactionEntity entity202 = new TransactionEntity();
			entity202.setAccountid(accountid);
			entity202.setBankcardno((String)cashRow.get("BANKCARDNO"));
			entity202.setBankno((String)cashRow.get("BANKNO"));
			entity202.setName((String)cashRow.get("NAME"));
			entity202.setIdcard((String)cashRow.get("IDCARD"));
			trans202(req202,entity202);
			/*********分润到上级钱包余额中*******************/
			String recommender = accountInfo.getRecommender();
			if(recommender!=null){
				double prize = UtilData.multiply(amount216, 0.0005);
				BigDecimal b = new BigDecimal(prize); 
				prize = b.setScale(2, RoundingMode.DOWN).doubleValue();
				String prizeStr = df.format(prize);
				if(prize>=0.01){
					updateAccountBalance(recommender,prize,PARA_CONSTATNTS.Reward_Trans,String.valueOf(rowid),"b_transaction");
					AccountInfo recommenderInfo = accountDao.findAccountByAccountId(recommender);
					wxNoticeService.gbpTransRewardNotice(recommenderInfo.getDeviceId(), orderno, prizeStr);
				}				
			}			
			/*****************************************/
		}		
		
	}

	@Transactional
	private void process104(String content) {
		Trans104Rsp rsp = JSONObject.parseObject(content, Trans104Rsp.class);
		String respCode = rsp.getResp_code();
		TransactionEntity entity = new TransactionEntity();
		entity.setOrderno(rsp.getOrder_id());
		entity.setNotifycontent(content);		
		TransactionEntity qte = transDao.queryTransactionByOrderNo(rsp.getOrder_id());
		switch (respCode) {
		case "104000":			
			entity.setStatus("01");
			accountDao.updateBankCardInvalidByAccount(entity.getAccountid(),"01");
			accountDao.insertAccBankCard(qte.getAccountid(), qte.getName(), qte.getIdcard(), qte.getBankno(), qte.getBankcardno(), qte.getBankphone(), "01", "01", "1",rsp.getCash_bind_card_id());		
			break;
		case "104002":
			entity.setStatus("03");
			break;
		default:			
			entity.setStatus("02");
			break;
		}		
		String status  = qte.getStatus();
		//如果状态是成功,则不能修改
		if(!"01".equals(status)){
			transDao.updateTransaction(entity);
		}
	}

	@Override
	public String transStatusToCode(String status){
		String code=null;
		switch (status) {
		case "101000":// 101开户
		case "104000":// 104取现绑卡成功	
		case "216000":// 216统合一阶段
		case "216002"://216一阶段
		case "216318":// 216统合一阶段
		case "216319"://216统合一阶段 验证码发送中
		case "217000":// 217统合二阶段
		case "203000":// 203转账
		case "202000":// 202取现
		case "112000":// 112快捷一阶段
		case "112002":// 112快捷一阶段
		case "201000":// 201快捷二阶段
			code="01";
			break;
		case "104002"://104取现绑卡已受理		
		case "202002"://202取现	
		case "201002"://201快捷二阶段
			code="03";
			break;
		default:
			code="02";
		}
		return code;
	}

	@Override
	public String getMerCustId() {
		return merCustId;
	}

	@Override
	public String getMerAcctId() {
		return merAcctId;
	}

	

	

}
