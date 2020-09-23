package com.bg.trans.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.minidao.annotation.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.ld.ApplySmsBindReq;
import com.bg.trans.entity.ld.ApplySmsBindRsp;
import com.bg.trans.entity.ld.ConfirmSmsBindReq;
import com.bg.trans.entity.ld.ConfirmSmsBindRsp;
import com.bg.trans.entity.ld.CreateOrderReq;
import com.bg.trans.entity.ld.CreateOrderRsp;
import com.bg.trans.entity.ld.OrderPayReq;
import com.bg.trans.entity.ld.OrderPayRsp;
import com.bg.trans.entity.ld.QueryOrderReq;
import com.bg.trans.entity.ld.QueryOrderRsp;
import com.bg.trans.entity.ld.UnbindReq;
import com.bg.trans.entity.ld.UnbindRsp;
import com.bg.trans.service.ITransLDService;
import com.bg.util.SetSystemProperty;
import com.bg.util.UtilData;
import com.umf.api.service.UmfService;
import com.umf.api.service.UmfServiceImpl;

@Service("transLDService")
public class TransLDServiceImpl implements ITransLDService {
	private static Logger logger = Logger.getLogger(TransLDServiceImpl.class);
	@Autowired
	private ITransDao transDao;
	
	private String mer_id = SetSystemProperty.getKeyValue("treaty.mer_id","ld/ld.properties");
	private String keypath = SetSystemProperty.getKeyValue("treaty.keypath","ld/ld.properties");
	private String notifyUrl = SetSystemProperty.getKeyValue("treaty.notifyUrl","ld/ld.properties");
	private UmfService instance =  new UmfServiceImpl(mer_id,keypath);
//	private static final UmfService instance =  new UmfServiceImpl("52510","E:/52510_.key.p8");
//	private String mer_id = "52510";

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ApplySmsBindRsp applySmsBind(ApplySmsBindReq req,TransactionEntity entity) {
//		String orderid = UtilData.createOnlyData();
		Map reqMap = new HashMap();				
		reqMap.put("mer_id",mer_id);  
		/**
		reqMap.put("card_id","6230580000235397218");
		reqMap.put("media_id","18633669106");
		reqMap.put("identity_type","1");
		reqMap.put("identity_code","130426199004132330");
		reqMap.put("card_holder","李民君");
		reqMap.put("mer_cust_id","130426199004132330");	
		////{"bind_id":"1908021405031736","mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。"}
		*/
//		reqMap.put("card_id","6226732800364690");
//		reqMap.put("media_id","18033797586");
//		reqMap.put("identity_type","1");
//		reqMap.put("identity_code","133023198111280213");
//		reqMap.put("card_holder","李廷");
//		reqMap.put("mer_cust_id","133023198111280213");	
		
		reqMap.put("card_id",req.getCard_id());
		reqMap.put("media_id",req.getMedia_id());
		reqMap.put("identity_type","1");
		reqMap.put("identity_code",req.getIdentity_code());
		reqMap.put("card_holder",req.getCard_holder());
		reqMap.put("mer_cust_id",req.getMer_cust_id());	
		Map respMap = instance.CommercialSignOrderMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);		
		ApplySmsBindRsp rsp = JSONObject.parseObject(json, ApplySmsBindRsp.class);
		entity.setChannel(PARA_CONSTATNTS.Channel_LD);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyApply);
		entity.setReqcontent(JSONObject.toJSONString(reqMap));
		entity.setRespcontent(JSONObject.toJSONString(respMap));
		entity.setIsnotice("2");	
		return rsp;
		/**
		 * //{"bind_id":"1908071727438135","mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。"}
		//{"mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。","usr_busi_agreement_id":"UB201908071728270000000035117208","usr_pay_agreement_id":"P2019080717282700000000043908558"}
		 */
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ConfirmSmsBindRsp confirmSmsBind(ConfirmSmsBindReq req,TransactionEntity entity) {	
		String treatychannel = PARA_CONSTATNTS.Channel_LD;
		Map reqMap = new HashMap();
		reqMap.put("mer_id",mer_id);  
		reqMap.put("bind_id",req.getBind_id());
		reqMap.put("verify_code",req.getVerify_code());
		Map respMap = instance.CommercialSignConfirmMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);
		ConfirmSmsBindRsp rsp = JSONObject.parseObject(json, ConfirmSmsBindRsp.class);
		entity.setChannel(PARA_CONSTATNTS.Channel_LD);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyConfirm);
		entity.setReqcontent(JSONObject.toJSONString(reqMap));
		entity.setRespcontent(JSONObject.toJSONString(respMap));
		entity.setIsnotice("2");
		if("0000".equals(rsp.getRet_code())){
			String treatyid = rsp.getUsr_pay_agreement_id();
			String remark = rsp.getUsr_busi_agreement_id();
			transDao.insertBindBankCard(entity.getName(), entity.getIdcard(), entity.getBankcardno(), entity.getBankphone(), treatychannel, treatyid, remark);			
		}	
		return rsp;
		/** 临时扣款
		//{"mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。","usr_busi_agreement_id":"UB201908021406440000000035036144","usr_pay_agreement_id":"P2019080214064400000000043827802"}		
		//{"mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。","usr_busi_agreement_id":"UB201908071728270000000035117208","usr_pay_agreement_id":"P2019080717282700000000043908558"}
		//req.setMer_id("52510");req.setCard_id("6212260402028101986");req.setMedia_id("15230178444");req.setIdentity_type("1");req.setIdentity_code("130123198604101810");	req.setMer_cust_id("130123198604101810");req.setCard_holder("张建恒");		
		 * {"mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。","usr_busi_agreement_id":"UB201908281553130000000035602025","usr_pay_agreement_id":"P2019082815531200000000044358826"}
		 */
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CreateOrderRsp createOrder(CreateOrderReq req) {
		Map reqMap = new HashMap();
		reqMap.put("mer_id",mer_id);  
		reqMap.put("order_id",req.getOrder_id());
		reqMap.put("mer_date",req.getMer_date());
		reqMap.put("amount",req.getAmount());
		reqMap.put("notify_url",notifyUrl);
		Map respMap = instance.quickOrderMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);	
		return JSONObject.parseObject(json, CreateOrderRsp.class);
		/**
		 * //{"mer_id":"52510","ret_code":"0000","ret_msg":"操作成功","trade_no":"3908021412341135","trade_state":"WAIT_BUYER_PAY"}
		 */
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public OrderPayRsp orderPay(OrderPayReq req,TransactionEntity entity) {
		Map reqMap = new HashMap<String, String>();
		reqMap.put("mer_id",mer_id);  
		reqMap.put("trade_no",req.getTrade_no());
		reqMap.put("mer_cust_id", req.getMer_cust_id());
		reqMap.put("usr_pay_agreement_id", req.getUsr_pay_agreement_id());		
//		reqMap.put("card_id","111111111111111");
//		reqMap.put("identity_code","111111111111111");
//		reqMap.put("media_id","13333333333");
//		reqMap.put("card_holder","张三");	    
		
		Map respMap = instance.agreementPaymentMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);
		entity.setChannel(PARA_CONSTATNTS.Channel_LD);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_TreatyCollect);
		entity.setReqcontent(JSONObject.toJSONString(reqMap));
		entity.setRespcontent(JSONObject.toJSONString(respMap));
		entity.setIsnotice("2");
		return JSONObject.parseObject(json, OrderPayRsp.class);
		//{"amount":"1","amt_type":"RMB","mer_date":"20190802","mer_id":"52510","order_id":"20190802141210794100","pay_date":"20190802","ret_code":"0000","ret_msg":"交易成功","settle_date":"20190802","trade_state":"TRADE_SUCCESS"}
	}
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public String processNofity(String requestParam,HttpServletResponse response) {
		//传入联动异步通知的请求参数
        System.out.println("联动异步通知参数 :" + requestParam);
        /**
    	 * 调用SDK中的异步通知解析方法，传入商户号商户私钥路径
    	 */
        Map respMap = null;           
		try {
			respMap = instance.notifyDataParserMap(requestParam);
			System.out.println(JSONObject.toJSONString(respMap));
			String resmetaString = instance.responseUMFMap(respMap);
			logger.info("返回联动字符串参数 :" + resmetaString);
			response.getWriter().print(resmetaString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}		
		return JSONObject.toJSONString(respMap);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public QueryOrderRsp queryOrder(QueryOrderReq req) {
		Map reqMap = new HashMap();
		reqMap.put("mer_id",mer_id);
//		reqMap.put("mer_date","20190810"); 
//		reqMap.put("trade_no","3908101049658816");
		
		reqMap.put("mer_date",req.getMer_date()); 
		reqMap.put("order_id",req.getOrder_id());
		Map respMap = instance.queryhistoryOrderMap(reqMap);
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);
		return JSONObject.parseObject(json, QueryOrderRsp.class);
		//{"mer_id":"52510","ret_code":"0000","ret_msg":"成功"}
	}
	//{"amount":"1000000","amt_type":"RMB","mer_date":"20190902","mer_id":"52510","order_id":"20190902110357993100","pay_date":"20190902","ret_code":"00090361","ret_msg":"亲，您的交易金额超过发卡行设置的日限额，详情请联系发卡行.,错误码00080545,PSP1104257d663c1","settle_date":"20190902","trade_state":"TRADE_FAIL"}
	public static void main(String[] arg){
		TransLDServiceImpl service = new TransLDServiceImpl();
		TransactionEntity entity = new TransactionEntity();
		/** 
		ApplySmsBindReq req = new ApplySmsBindReq();
		req.setMer_id("52510");
		req.setCard_id("6212260402028101986");
		req.setMedia_id("15230178444");
		req.setIdentity_type("1");
		req.setIdentity_code("130123198604101810");
		req.setMer_cust_id("130123198604101810");
		req.setCard_holder("张建恒");		
		service.applySmsBind(req,entity);
		//{"bind_id":"1908281552180243","mer_id":"52510","ret_code":"0000","ret_msg":"交易成功。"}
		*/
		/**
		ConfirmSmsBindReq req = new ConfirmSmsBindReq();
		req.setBind_id("1908281552180243");
		req.setVerify_code("057267");
		service.confirmSmsBind(req,entity);
		*/
		/**
		CreateOrderReq req = new CreateOrderReq();
		String orderId = UtilData.createOnlyData();
		req.setOrder_id(orderId);
		req.setAmount("1000000");
		req.setMer_date("20190902");
		service.createOrder(req);
		*/
		/**
		OrderPayReq req = new OrderPayReq();
		req.setTrade_no("3909021104998011");
		req.setMer_cust_id("130123198604101810");
		req.setUsr_pay_agreement_id("P2019082815531200000000044358826");
		service.orderPay(req,entity);
		*/
//		service.queryOrder(null);
//		service.unbind();
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public UnbindRsp unbind(UnbindReq req,TransactionEntity entity){
		Map reqMap = new HashMap();
		reqMap.put("mer_id",mer_id);  
//		reqMap.put("mer_cust_id","130123198604101810");
		reqMap.put("mer_cust_id",req.getMer_cust_id());
//		reqMap.put("", "UB201908301258440000000035630524");
//		reqMap.put("usr_pay_agreement_id", "P2019082815531200000000044358826");
		if(req.getUsr_pay_agreement_id()!=null&&!"".equals(req.getUsr_pay_agreement_id())){
			reqMap.put("usr_pay_agreement_id", req.getUsr_pay_agreement_id());
		}		
		Map respMap = instance.unbindMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);	
		entity.setChannel(PARA_CONSTATNTS.Channel_LD);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_UnBindTreaty);
		entity.setReqcontent(JSONObject.toJSONString(reqMap));
		entity.setRespcontent(JSONObject.toJSONString(respMap));
		entity.setIsnotice("2");
		return JSONObject.parseObject(json, UnbindRsp.class);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UnbindRsp unbind2(UnbindReq req,TransactionEntity entity){
		UmfService in =  new UmfServiceImpl("52510","E:/52510_.key.p8");
		Map reqMap = new HashMap();
		reqMap.put("mer_id","52510");  
//		reqMap.put("mer_cust_id","130123198604101810");
		reqMap.put("mer_cust_id",req.getMer_cust_id());
//		reqMap.put("", "UB201908301258440000000035630524");
//		reqMap.put("usr_pay_agreement_id", "P2019082815531200000000044358826");
		if(req.getUsr_pay_agreement_id()!=null&&!"".equals(req.getUsr_pay_agreement_id())){
			reqMap.put("usr_pay_agreement_id", req.getUsr_pay_agreement_id());
		}		
		Map respMap = in.unbindMap(reqMap);		
		String json = JSONObject.toJSONString(respMap);
		System.out.println(json);	
		entity.setChannel(PARA_CONSTATNTS.Channel_LD);
		entity.setTrantype(PARA_CONSTATNTS.TransCode_UnBindTreaty);
		entity.setReqcontent(JSONObject.toJSONString(reqMap));
		entity.setRespcontent(JSONObject.toJSONString(respMap));
		entity.setIsnotice("2");
		return JSONObject.parseObject(json, UnbindRsp.class);
	}
}
