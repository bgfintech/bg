package com.bg.trans.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.DateUtil;
import com.bg.util.SetSystemProperty;
import com.bg.util.UtilData;
import com.bg.web.entity.AccountInfo;
import com.lycheepay.gateway.client.GBPService;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.InitiativePayService;
import com.lycheepay.gateway.client.KftSecMerchantService;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyApplyResultDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.TreatyConfirmResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.BankCardDetailDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardCollectFromBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardPayToBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardSmsCollectConfirmDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTradeResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectResultDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.DownloadAccountFileReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.DownloadAccountFileRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.RefundReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.RefundRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayApplyRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.SmsQuickPayConfirmRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.BaseSecMerchantRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantResponseDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantResponseDTO;
import com.lycheepay.gateway.client.dto.secmerchant.UpdateSecMerchantRequestDTO;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;

@Service("transKFTService")
public class TransKFTServiceImpl implements ITransKFTService {
	private static Logger logger = Logger.getLogger(TransKFTServiceImpl.class);
	@Autowired
	private ITransDao transDao;	
	private InitiativePayService initiativePayService;
	private KftSecMerchantService secMerchantService;	
	private GBPService gbpService;
	// 商户id
	private String merchantId = SetSystemProperty.getKeyValue("pay.merchantId","kft/kft.properties");
	private String merchantIp = SetSystemProperty.getKeyValue("pay.merchantIp","kft/kft.properties");
	private String keyStorePath = SetSystemProperty.getKeyValue("pay.keyStorePath", "kft/kft.properties");
	private String keyStorePassword = SetSystemProperty.getKeyValue("pay.keyStorePassword", "kft/kft.properties");
	private String keyPassword = SetSystemProperty.getKeyValue("pay.keyPassword", "kft/kft.properties");
	private String gbpKeyStorePath = SetSystemProperty.getKeyValue("pay.gbpKeyStorePath", "kft/kft.properties");
	private String gbpKeyStorePassword = SetSystemProperty.getKeyValue("pay.gbpKeyStorePassword", "kft/kft.properties");
	private String gbpKeyPassword = SetSystemProperty.getKeyValue("pay.gbpKeyPassword", "kft/kft.properties");
	private String httpDomain = SetSystemProperty.getKeyValue("pay.httpDomain","kft/kft.properties");
	private int httpPort = Integer.parseInt(SetSystemProperty.getKeyValue("pay.httpPort", "kft/kft.properties"));
	private String version = SetSystemProperty.getKeyValue("pay.version","kft/kft.properties");
	private String notifyUrl = SetSystemProperty.getKeyValue("pay.notifyUrl","kft/kft.properties");
	
	private String certFilePath = SetSystemProperty.getKeyValue("pay.certFilePath","kft/kft.properties");
	private String gbpTransMerchantId1 = SetSystemProperty.getKeyValue("pay.gbpTransMerchantId1","kft/kft.properties");
	private String gbpTransMerchantId2 = SetSystemProperty.getKeyValue("pay.gbpTransMerchantId2","kft/kft.properties");
	private String gbpTransMerchantId3 = SetSystemProperty.getKeyValue("pay.gbpTransMerchantId3","kft/kft.properties");
	private String gbpSettleMerchantId = SetSystemProperty.getKeyValue("pay.gbpSettleMerchantId","kft/kft.properties");
	private String threeVerifyProductId = SetSystemProperty.getKeyValue("pay.threeVerifyProductId","kft/kft.properties");
	private String sameIdCollectProductId = SetSystemProperty.getKeyValue("pay.sameIdCollectProductId","kft/kft.properties");
	private String sameIdPayProductId = SetSystemProperty.getKeyValue("pay.sameIdPayProductId","kft/kft.properties");
	private String gbpTreatyProductId = SetSystemProperty.getKeyValue("pay.gbpTreatyProductId","kft/kft.properties");
	private static Map<String,String> banknoMap=null;
	private static Map<String,String> banknameMap=null;
	@PostConstruct
	public void init() throws Exception {
		// 证书类型、证书路径、证书密码、别名、证书容器密码
		SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12",keyStorePath, keyStorePassword.toCharArray(), null,keyPassword.toCharArray());
		// 签名提供者、商户服务器IP(callerIp)、下载文件路径(暂时没用)
		initiativePayService = new InitiativePayService(keystoreSignProvider,merchantIp, "zh_CN", "c:/zip");
		initiativePayService.setHttpDomain(httpDomain);// 测试环境ip
		initiativePayService.setHttpPort(httpPort);// 测试环境端口
		// 设置的交易连接超时时间要大于0小于2分钟,单位:秒.0表示不超时,一直等待,默认30秒
		// service.setConnectionTimeoutSeconds(1 * 60);
		// 设置的交易响应超时时间,要大于0小于10分钟,单位:秒.0表示不超时,一直等待,默认5分钟,ps：对应获取对账文件这个应该设长一点时间
		// service.setResponseTimeoutSeconds(10 * 60);
		secMerchantService = new KftSecMerchantService(keystoreSignProvider,merchantIp, "zh_CN", "c:/zip");
		secMerchantService.setHttpDomain(httpDomain);// 测试环境ip
		secMerchantService.setHttpPort(httpPort);// 测试环境端口
		// 设置的交易连接超时时间要大于0小于2分钟,单位:秒.0表示不超时,一直等待,默认30秒
		// secMerchantService.setConnectionTimeoutSeconds(1 * 60);
		// 设置的交易响应超时时间,要大于0小于10分钟,单位:秒.0表示不超时,一直等待,默认5分钟,ps：对应获取对账文件这个应该设长一点时间
		// secMerchantService.setResponseTimeoutSeconds(10 * 60);
		SignProvider gbpkeystoreSignProvider = new KeystoreSignProvider("PKCS12",gbpKeyStorePath, gbpKeyStorePassword.toCharArray(), null,gbpKeyPassword.toCharArray());
		gbpService = new GBPService(gbpkeystoreSignProvider, merchantIp, "zh_CN","c:/zip");
		gbpService.setHttpDomain(httpDomain);// 测试服务器地址
		gbpService.setHttpPort(httpPort);// 测试环境端口6443,生产环境端口8443,不设置默认8443
		
		if(banknoMap==null){
			List<Map<String,Object>> list = transDao.findBanknoByChannel(PARA_CONSTATNTS.Channel_KFT);
			banknoMap = new HashMap<String,String>();
			banknameMap = new HashMap<String,String>();
			for(Map<String,Object> m:list){
				String bankno = (String)m.get("BANKNO");
				String dbankno = (String)m.get("DBANKNO");
				String bankname = (String)m.get("BANKNAME");
				banknoMap.put(bankno, dbankno);
				banknameMap.put(bankno, bankname);
			}
		}
	}
	@Override
	public void updateTransaction(String orderNo,String status,String channelNo,String notifycontent){
		TransactionEntity entity = new TransactionEntity();
		String s = ZDPayStatusToCode(status);
		entity.setStatus(s);
		entity.setOrderno(orderNo);
		entity.setChannelno(channelNo);
		entity.setNotifycontent(notifycontent);
		transDao.updateTransaction(entity);
	}
	@Override
	public TransactionEntity queryTransByOrderNo(String orderNo){
		return transDao.queryTransactionByOrderNo(orderNo);
	}

	@Override
	public TradeQueryRespDTO initiativePayQuery(TradeQueryReqDTO reqDTO) {
		reqDTO.setService("kpp_trade_record_query");// 接口名称,固定不变
		reqDTO.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);// 替换成快付通提供的商户ID，测试生产不一样
		TradeQueryRespDTO respDTO = null;
		try {
			respDTO = initiativePayService.tradeQuery(reqDTO);
			logger.info("查询响应：" + JSON.toJSONString(respDTO));
			TransactionEntity entity = new TransactionEntity();
			String status = ZDPayStatusToCode(respDTO.getStatus());
			entity.setStatus(status);
			entity.setOrderno(respDTO.getOrderNo());
			entity.setChannelno(respDTO.getChannelNo());
			transDao.updateTransaction(entity);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return respDTO;
	}
	@Override
	public PassiveScanPayRespDTO initiativePayBdsm(PassiveScanPayReqDTO reqDTO,AccountInfo accountinfo){
		reqDTO.setService("kpp_bdsm_pay");///接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		reqDTO.setCurrency("CNY");//币种
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		reqDTO.setTradeTime(sdf.format(d));// 商户方交易时间	// 注意此时间取值一般为商户方系统时间而非快付通生成此时间
											// 20120927185643,此字段建议改为取当前时间
		reqDTO.setNotifyUrl(notifyUrl);//必须可直接访问的url，不能携带参数
		PassiveScanPayRespDTO respDTO=null;
		try {
			respDTO = initiativePayService.passiveScanPay(reqDTO);
			logger.info("被扫支付响应：" + JSON.toJSONString(respDTO));
			TransactionEntity entity = new TransactionEntity();
			entity.setReqno(reqDTO.getReqNo());
			entity.setTranstime(reqDTO.getTradeTime());
			entity.setMerchantid(merchantId);
			entity.setSecmerchantid(reqDTO.getSecMerchantId());
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_BDPay);
			if("0000001".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_WX);
			}else if("0000002".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_ZFB);
			}else if("0000003".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_YL);
			}	
			String status = ZDPayStatusToCode(respDTO.getStatus());
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(respDTO));
			entity.setOrderno(reqDTO.getOrderNo());
			entity.setTranamt(Integer.parseInt(reqDTO.getAmount()));
			entity.setChannelno(respDTO.getChannelNo());
			entity.setDeviceid(accountinfo.getDeviceId());
			entity.setAccountid(accountinfo.getAccount());
			entity.setIsnotice("0");
			transDao.insertTransaction(entity);
			
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		} 
		return respDTO;
	}
	

	@Override
	public ActiveScanPayRespDTO initiativePayZdsm(ActiveScanPayReqDTO reqDTO,TransactionEntity entity) {
		reqDTO.setService("kpp_zdsm_pay");// 接口名称，固定不变
		reqDTO.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);// 替换成快付通提供的商户ID，测试生产不一样
		reqDTO.setTerminalIp(merchantIp);// APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
//		reqDTO.setNotifyUrl(notifyUrl);// 必须可直接访问的url，不能携带参数
		reqDTO.setCurrency("CNY");// 币种
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		reqDTO.setTradeTime(sdf.format(d));// 商户方交易时间	// 注意此时间取值一般为商户方系统时间而非快付通生成此时间	// 20120927185643,此字段建议改为取当前时间
		ActiveScanPayRespDTO respDTO = null;
		try {
			respDTO = initiativePayService.activeScanPay(reqDTO);
			logger.info("主扫支付响应：" + JSON.toJSONString(respDTO));
//			entity.setReqno(reqDTO.getReqNo());
			entity.setTranstime(reqDTO.getTradeTime());
			entity.setMerchantid(merchantId);
//			entity.setSecmerchantid(reqDTO.getSecMerchantId());
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_ZDPay);
			if("0000001".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_WX);
			}else if("0000002".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_ZFB);
			}else if("0000003".equals(reqDTO.getBankNo())){
				entity.setPaychannel(PARA_CONSTATNTS.PayChannel_YL);
			}			
			String status = ZDPayStatusToCode(respDTO.getStatus());
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(respDTO));
			entity.setTranamt(Integer.parseInt(reqDTO.getAmount()));				
			entity.setIsnotice("0");
//			transDao.insertTransaction(entity);	
			
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return respDTO;
	}
	
	@Override
	public RefundRespDTO initiativePayRefund(RefundReqDTO reqDTO,AccountInfo accountinfo){
		reqDTO.setService("kpp_refund");//接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		reqDTO.setTradeTime(sdf.format(d));// 商户方交易时间	// 注意此时间取值一般为商户方系统时间而非快付通生成此时间	// 20120927185643,此字段建议改为取当前时间
		reqDTO.setCurrency("CNY");//币种
		RefundRespDTO respDTO = null;
		try {
			respDTO = initiativePayService.refund(reqDTO);
			logger.info("退款响应：" + JSON.toJSONString(respDTO));
			TransactionEntity entity = new TransactionEntity();
			entity.setReqno(reqDTO.getReqNo());			
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_PayRefund);
			String status = ZDPayStatusToCode(respDTO.getStatus());
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(respDTO));
			entity.setOrderno(reqDTO.getOrderNo());
			entity.setOriginalorderno(reqDTO.getOriginalOrderNo());
			entity.setTranamt(Integer.parseInt(reqDTO.getAmount()));			
			entity.setDeviceid(accountinfo.getDeviceId());
			entity.setAccountid(accountinfo.getAccount());
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);				
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		} 		
		return respDTO;
	}
	
	
	public DownloadAccountFileRespDTO initiativeDownloadReconfile(DownloadAccountFileReqDTO reqDTO) {
		reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
		reqDTO.setService("kpp_recon_result_query");//接口名称,固定不变
		reqDTO.setVersion("1.0.0-IEST");//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		reqDTO.setCheckDate("20160804");//账务日期，格式YYYYMMDD
		DownloadAccountFileRespDTO resp = new DownloadAccountFileRespDTO();
		try {
			resp = initiativePayService.downloadAccountFile(reqDTO);
			logger.info("下载对账文件响应：" + JSON.toJSONString(resp));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return resp;
	}
	

	@Override
	public SettledSecMerchantResponseDTO settledSecMerchant(SettledSecMerchantRequestDTO reqDTO) {
		SettledSecMerchantResponseDTO resDTO = null;
		try {			
			reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));// 请求编号
			reqDTO.setService("amp_secmerchant_add");// 接口名称
			reqDTO.setVersion(version);// 接口版本号
			reqDTO.setMerchantId(merchantId);
			String certPath = reqDTO.getCertPath();
			
			reqDTO.setCertDigest(md5AndBase64(certFilePath+certPath));
//			reqDTO.setSecMerchantName(secMerchantName);
//			reqDTO.setShortName(shortMerchName);
//			reqDTO.setProvince(province);
//			reqDTO.setCity(city);
//			reqDTO.setDistrict(district);
//			reqDTO.setAddress(address);
//			reqDTO.setLegalName(legalName);
//			reqDTO.setContactName(contactName);
//			reqDTO.setContactPhone(contactPhone);
//			reqDTO.setContactEmail(contactEmail);
//			reqDTO.setMerchantProperty(merchantProperty);
//			reqDTO.setCategory(category);
//			reqDTO.setBusinessScene(businessScene);
//			reqDTO.setRemark(remark);
//			reqDTO.setCertPath(certPath);
//			reqDTO.setCertDigest(certDigest);
//			reqDTO.setProductFees(productFees);
//			reqDTO.setSettleBankAccount(settleAccout);
//			// reqDTO.setPersonCertInfo(personCertInfo);
//			reqDTO.setCorpCertInfo(comCertInfo);
			System.out.println(JSONObject.toJSONString(reqDTO));
			logger.info("addMerchant request:"+JSON.toJSONString(reqDTO));			
			resDTO = secMerchantService.settledSecMerchant(reqDTO);
			logger.info("addMerchant response:"+JSON.toJSONString(resDTO));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}

		System.out.println(JSONObject.toJSONString(resDTO));
		return resDTO;
	}
	
	
	
	
	@Override
	public QuerySecMerchantResponseDTO querySecMerchant(QuerySecMerchantRequestDTO reqDTO){
		reqDTO.setService("amp_secmerchant_query");
		reqDTO.setVersion(version);//接口版本号

		reqDTO.setMerchantId(merchantId);
//		reqDTO.setMerchantProperty("3"); // 商户类型
		// 商户入驻时的证件号码，个人用户使用身份证查询，个体商户企业使用营业执照查询
//		reqDTO.setCertNo("1020170808113211D");
		QuerySecMerchantResponseDTO resDTO = null;
		try {
			resDTO = secMerchantService.querySecMerchant(reqDTO);
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		System.out.println("resDTO:"+resDTO.getMerchantDetail());
		return resDTO;
	}
	@Override
	public SmsQuickPayApplyRespDTO smsQuickPayApply(SmsQuickPayApplyReqDTO reqDTO,TransactionEntity entity) {
		
		reqDTO.setService("kpp_sms_collect");//接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
//		Date d = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		reqDTO.setTradeTime(sdf.format(d));//商户方交易时间 注意此时间取值一般为商户方系统时间而非快付通生成此时间 20120927185643
		reqDTO.setCustBankNo(banknoMap.get(reqDTO.getCustBankNo()));
		reqDTO.setCurrency("CNY");//币种
		reqDTO.setCustCertificationType("0");//收钱客户的身份证件类型 ，参考接口文档		
//		reqDTO.setIsS0(isS0);//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务。如果无此业务权限，此参数为1，则返回失败。 可空 
//		reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
//		reqDTO.setIsSplit("1");//是否分账交易,1:是，0：否 ，
		//分账详情，如果是否分账交易为是，该字段为必填，格式如下:
//		reqDTO.setSplitInfo("[{\"merchantId\":\"2017072600081986\",\"amount\":\"1\",\"remark\":\"有线电视费\"},{\"merchantId\":\"2017073100082105\",\"amount\":\"1\",\"remark\":\"宽带费\"}]");
		SmsQuickPayApplyRespDTO resp=null;
		try {
			resp = initiativePayService.smsQuickPayApply(reqDTO);
			System.out.println("短信快捷申请响应：" + JSON.toJSONString(resp));
			entity.setTranstime(reqDTO.getTradeTime());
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_QuickPayApply);
			String status = QPayStatusToCode(resp.getStatus());			
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(resp));
			entity.setOrderno(reqDTO.getOrderNo());
			entity.setTranamt(Integer.parseInt(reqDTO.getAmount()));	
			entity.setMerchantid(merchantId);
			entity.setIsnotice("2");
//			transDao.insertTransaction(entity);	
			
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}		
		return resp;
	}
	@Override
	public SmsQuickPayConfirmRespDTO smsQuickPayConfirm(SmsQuickPayConfirmReqDTO reqDTO,TransactionEntity entity) {
		reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
		reqDTO.setService("kpp_sms_pay");//接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		reqDTO.setConfirmFlag("1");//确认标识 1确认支付2取消支付
		
//		reqDTO.setSecMerchantId("2017121500092289");
//		reqDTO.setOrderNo("20180307010");//交易编号 
//		reqDTO.setSmsCode("12356");//短信验证码
//		reqDTO.setCustBindPhoneNo("18649196843");//持卡人开户时绑定手机号，须与相应短信快捷支付申请时一致
		
		SmsQuickPayConfirmRespDTO resp = null;
		try {
			resp = initiativePayService.smsQuickPayConfirm(reqDTO);
			System.out.println("短信快捷确认响应：" + JSON.toJSONString(resp));
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			entity.setReqno(reqDTO.getReqNo());	
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_QuickPayConfirm);
			String status = QPayStatusToCode(resp.getStatus());
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(resp));
			entity.setOrderno(reqDTO.getOrderNo());
			entity.setMerchantid(merchantId);
			entity.setIsnotice("2");
//			transDao.insertTransaction(entity);				
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		} 	
		return resp;
	}
	
	@Override
	public BaseSecMerchantRespDTO updateSecMerchant(UpdateSecMerchantRequestDTO reqDTO) {
		reqDTO.setService("amp_secmerchant_update");//接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		String productFees = "[{\"feeOfAttach\":\"0\",\"feeOfRate\":\"45\",\"feeType\":\"3\",\"productId\":\"010501\"}]";//微信扫码
//				+ "{\"feeOfAttach\":\"0\",\"feeOfRate\":\"35\",\"feeType\":\"3\",\"productId\":\"010201\"},"//支付宝扫码
//				+ "{\"feeOfAttach\":\"0\",\"feeOfRate\":\"45\",\"feeType\":\"3\",\"productId\":\"010501\"}]";//支付宝扫码
//				+ "{\"feeOfAttach\":\"0\",\"feeOfRate\":\"35\",\"feeType\":\"3\",\"productId\":\"010301\"}]";//银联扫码
		
		reqDTO.setProductFees(productFees);
		BaseSecMerchantRespDTO resp=null;
		try {
			resp = secMerchantService.updateSecMerchant(reqDTO);
			System.out.println("更新二级商户信息响应：" + JSON.toJSONString(resp));
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}		
		return resp;
	}
	
	@Override
	public String ZDPayStatusToCode(String status){
		String code=null;
		//成功
		if("1".equals(status)||"4".equals(status)){
			code="01";
		//失败
		}else if("2".equals(status)||"5".equals(status)){
			code="02";
		//处理中或付款中
		}else{
			code="03";
		}
		return code;
	}
	
	
	@Override
	public String QPayStatusToCode(String status){
		String code=null;
		//成功
		if("1".equals(status)||"4".equals(status)||"7".equals(status)){
			code="01";
		//失败
		}else if("2".equals(status)||"5".equals(status)){
			code="02";
		//处理中或付款中
		}else{
			code="03";
		}
		return code;
	}	

	private String md5AndBase64(String filePath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(filePath);
		byte[] md5Bytes = DigestUtils.md5(fileInputStream);
		String result = new String(Base64.encodeBase64(md5Bytes), "UTF-8");
		IOUtils.closeQuietly(fileInputStream);
		return result;
	}

	@Override
	public SameIDCreditCardTradeResultDTO sameIDSmsCollect(SameIDCreditCardCollectFromBankAccountDTO dto,TransactionEntity entity,String gbpTransMerchantId) {
		String bankno = dto.getCustBankNo();
		dto.setCustBankNo(banknoMap.get(dto.getCustBankNo()));
		dto.setService("gbp_same_id_credit_card_sms_collect");// 接口名称，固定不变
//		dto.setProductNo("GBPTM003");// 替换成快付通提供的产品编号，测试生产不一样	
		dto.setProductNo(sameIdCollectProductId);		
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setCurrency("CNY");// 快付通定义的扣费币种,详情请看文档
		dto.setCustBankAcctType("1");// 可空，指客户的银行账户是个人账户还是企业账户 1个人 2企业
		dto.setCustAccountCreditOrDebit("2");// 客户账户借记贷记类型,0存折 1借记 2贷记
		dto.setCustCertificationType("0");// 客户证件类型,目前只支持身份证,详情请看文档
		dto.setTradeName("线上支付");// 简要概括此次交易的内容
		dto.setCityCode("9233");
		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dto.setTradeTime(sdf.format(d));		
		System.out.println("请求参数:" + dto);
		SameIDCreditCardTradeResultDTO result=null;
		try {
			result = gbpService.sameIDCreditCardSmsCollect(dto);
			System.out.println("响应结果:" + result);
			entity.setReqno(dto.getReqNo());	
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayApply);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setTranamt(Integer.parseInt(dto.getAmount()));
			entity.setName(dto.getCustName());
			entity.setIdcard(dto.getCustID());
			entity.setBankno(bankno);
			entity.setBankcardno(dto.getCustBankAccountNo());
			entity.setBankphone(dto.getCustBindPhoneNo());
			entity.setCvv2(dto.getCustCardCvv2());
			entity.setCardvaliddate(dto.getCustCardValidDate());
			entity.setMerchantid(dto.getMerchantId());
			entity.setBankphone(dto.getCustBindPhoneNo());
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);	
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public SameIDCreditCardTradeResultDTO sameIDSmsCollectConfirm(SameIDCreditCardSmsCollectConfirmDTO dto,TransactionEntity entity,String gbpTransMerchantId) {
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setService("gbp_same_id_credit_card_collect_confirm");// 接口名称，固定不变
//		dto.setProductNo("GBPTM003");// 替换成快付通提供的产品编号，测试生产不一样
		dto.setProductNo(sameIdCollectProductId);
		dto.setConfirmFlag("1");// 1确认，2取消,确认支付时，短信验证码不能为空		
		System.out.println("请求参数:" + dto);
		SameIDCreditCardTradeResultDTO result=null;
		try {
			result = gbpService.sameIDCreditCardSmsCollectConfirm(dto);
			System.out.println("响应结果:" + result);
			entity.setReqno(dto.getReqNo());
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpQuickPayConfirm);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);	
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public SameIDCreditCardTradeResultDTO threeMessageVerify(BankCardDetailDTO dto,TransactionEntity entity,String gbpTransMerchantId) {
		String bankno = dto.getCustBankNo();
		dto.setCustBankNo(banknoMap.get(dto.getCustBankNo()));
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setService("gbp_threeMessage_verification");// 接口名称，固定不变
//		dto.setProductNo("GBPTM001");// 替换成快付通提供的产品编号，测试生产不一样
		dto.setProductNo(threeVerifyProductId);
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setCustCertificationType("0");// 客户证件类型,目前只支持身份证,详情请看文档		
		System.out.println("请求参数:" + dto);
		SameIDCreditCardTradeResultDTO result=null;
		try {
			result = gbpService.threeMessageVerify(dto);
			System.out.println("响应结果:" + result);
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_ThreeMessageVerify);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setName(dto.getCustName());
			entity.setIdcard(dto.getCustID());
			entity.setBankno(bankno);
			entity.setBankcardno(dto.getCustBankAccountNo());
			entity.setIsnotice("2");	
			transDao.insertTransaction(entity);	
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}		
		return result;
	}
	@Override
	public SameIDCreditCardTradeResultDTO sameIDGbpPay(SameIDCreditCardPayToBankAccountDTO dto,TransactionEntity entity,String gbpTransMerchantId) {
		String bankno = dto.getCustBankNo();
		dto.setCustBankNo(banknoMap.get(bankno));
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setService("gbp_same_id_credit_card_pay");// 接口名称，固定不变
//		dto.setProductNo("GBPTM002");// 替换成快付通提供的产品编号，测试生产不一样
		dto.setProductNo(sameIdPayProductId);
		dto.setCurrency("CNY");// 快付通定义的扣费币种,详情请看文档
		dto.setCustBankAcctType("1");// 可空，指客户的银行账户是个人账户还是企业账户 1个人 2企业
//		dto.setCustAccountCreditOrDebit("1");// 客户账户借记贷记类型,0存折 1借记 2贷记
		dto.setCustCertificationType("0");// 客户证件类型,目前只支持身份证,详情请看文档
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dto.setTradeTime(sdf.format(d));// 交易时间,注意此时间取值一般为商户方系统时间而非快付通生成此时间
		
		System.out.println("请求参数:" + dto);
		SameIDCreditCardTradeResultDTO result=null;
		try {
			result = gbpService.sameIDCreditCardGbpPay(dto);
			System.out.println("响应结果:" + result);
			entity.setTranstime(dto.getTradeTime());
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpPay);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setTranamt(Integer.parseInt(dto.getAmount()));
			entity.setName(dto.getCustName());
			entity.setIdcard(dto.getCustID());
			entity.setBankno(bankno);
			entity.setBankcardno(dto.getCustBankAccountNo());
			entity.setIsnotice("2");	
			transDao.insertTransaction(entity);	
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return result;
	}
	

	public static void main(String[] arg) throws Exception {
		System.out.println("1111");
		String orderno = "20181217134145443104";
		String orderno1 = "20180102134145443101";
		String secMerchantId="2017121500092289";
		String amount = "100";
		String tradeName = "测试交易";
		String bankNo = "0000001";
		String authCode = "134683431007944532";
		String version = "1.0.0-IEST";
		DecimalFormat decimalFormat = new DecimalFormat("#");
		decimalFormat.setRoundingMode(RoundingMode.UP);
		String a = decimalFormat.format(332.4);		
		System.out.println(a);
		TransKFTServiceImpl service = new TransKFTServiceImpl();
		service.init();
//		PublicNoPayReqDTO reqDTO = new PublicNoPayReqDTO();
//		reqDTO.setOrderNo(orderno);
//		reqDTO.setSecMerchantId(secMerchantId);
//		reqDTO.setVersion(version);
//		service.publicNoPay(reqDTO,null);
//		SmsQuickPayApplyReqDTO reqDTO = new SmsQuickPayApplyReqDTO();
//		service.smsQuickPayApply(reqDTO);
//		SmsQuickPayConfirmReqDTO reqDTO = new SmsQuickPayConfirmReqDTO();
//		AccountInfo accountinfo = null;
//		service.smsQuickPayConfirm(reqDTO,accountinfo);
//		SameIDCreditCardSmsCollectConfirmDTO dto = new SameIDCreditCardSmsCollectConfirmDTO();
//		
//		service.sameIDSmsCollectConfirm(dto,null);
		
//		ActiveScanPayReqDTO reqDTO = new ActiveScanPayReqDTO();
//		reqDTO.setReqNo(orderno);
//		reqDTO.setSecMerchantId(secMerchantId);
//		reqDTO.setOrderNo(orderno);
//		reqDTO.setAmount(amount);
//		reqDTO.setTradeName(tradeName);
//		reqDTO.setBankNo(bankNo);
//		service.initiativePayZdsm(reqDTO,null);
		
		
		//4100000815528151359492400000083 查询响应：{"channelNo":"4100000815528151359492400000083","errorCode":"CUST_CHANNEL_00000000","failureDetails":"交易成功","orderNo":"20171218002","reqNo":"20171218003","settlementAmount":"1","status":"1"}
//		PassiveScanPayReqDTO reqDTO = new PassiveScanPayReqDTO();
//		reqDTO.setReqNo(orderno);
//		reqDTO.setSecMerchantId(secMerchantId);
//		reqDTO.setOrderNo(orderno);
//		reqDTO.setAmount(amount);
//		reqDTO.setBankNo(bankNo);
//		reqDTO.setTradeName(tradeName);
//		reqDTO.setAuthCode(authCode);
//		service.initiativePayBdsm(reqDTO);
		
		
//		TradeQueryReqDTO reqDTO = new TradeQueryReqDTO();
//		reqDTO.setReqNo(System.currentTimeMillis()+"");
//		reqDTO.setOriginalOrderNo(orderno1);
//		service.initiativePayQuery(reqDTO);
		
		
//		SettledSecMerchantRequestDTO reqDTO = new SettledSecMerchantRequestDTO();
//		service.settledSecMerchant(reqDTO);
		
//		QuerySecMerchantRequestDTO reqDTO = new QuerySecMerchantRequestDTO();
//		service.querySecMerchant(reqDTO);
		
//		RefundReqDTO reqDTO = new RefundReqDTO();
//		reqDTO.setReqNo(orderno);
//		reqDTO.setOrderNo(orderno);
//		reqDTO.setAmount(amount);
//		reqDTO.setSecMerchantId(secMerchantId);
//		reqDTO.setOriginalOrderNo(orderno1);
//		reqDTO.setTradeName(tradeName);		
//		service.initiativePayRefund(reqDTO);
		
		 String orderNo = UtilData.createOnlyData();
		 ActiveScanPayReqDTO reqDTO = new ActiveScanPayReqDTO();
		 reqDTO.setOrderNo(orderNo);
		 reqDTO.setAmount(amount);
		 reqDTO.setSecMerchantId(secMerchantId);
		 reqDTO.setTradeName(tradeName);
		 reqDTO.setBankNo(bankNo);
		 ActiveScanPayRespDTO respDTO = service.initiativePayZdsm(reqDTO,new TransactionEntity());
		 System.out.println(JSONObject.toJSONString(respDTO));
//		UpdateSecMerchantRequestDTO reqDTO = new UpdateSecMerchantRequestDTO();
//		reqDTO.setSecMerchantId(secMerchantId);
//		service.updateSecMerchant(reqDTO);
//		
		
	}
	@Override
	public TreatyApplyResultDTO treatyCollectApply(TreatyApplyDTO dto,TransactionEntity entity,String gbpTransMerchantId) {
		String bankno = dto.getBankType();
		dto.setBankType(banknoMap.get(dto.getBankType()));
		dto.setService("gbp_same_id_treaty_collect_apply");// 接口名称，固定不变		
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setProductNo(gbpTreatyProductId);
		dto.setTreatyType("12");// 11借计卡扣款 12信用卡扣款
		dto.setBankCardType("2");// 0存折 1借记 2贷记
		dto.setCertificateType("0");// 持卡人证件类型，0身份证
//		dto.setNote("协议说明12");// 协议简要说明，可空
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//////有效期一年
		TreatyApplyResultDTO result=null;
		try {
			Date d = new Date();
			String vcd = dto.getCustCardValidDate();
			String y = vcd.substring(2,4);
			String m = vcd.substring(0,2);			
			String ed = DateUtil.getEndDateOfMonth("20"+y+m+"01");
			dto.setStartDate(sdf.format(d));// 生效日期,日期格式yyyyMMdd
			dto.setEndDate(ed);// 协议失效日期,日期格式yyyyMMdd			
			System.out.println("请求信息为：" + dto.toString());					
			result = gbpService.treatyCollectApply(dto);
			System.out.println("响应信息为:" + result.toString());
			entity.setReqno(dto.getReqNo());
			d = new Date();
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpTreatyApply);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setName(dto.getHolderName());
			entity.setIdcard(dto.getCertificateNo());
			entity.setBankno(bankno);
			entity.setBankcardno(dto.getBankCardNo());
			entity.setBankphone(dto.getMobileNo());
			entity.setCvv2(dto.getCustCardCvv2());
			entity.setCardvaliddate(dto.getCustCardValidDate());
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);				
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();		
		}		
		return result;
	}
	@Override
	public TreatyConfirmResultDTO confirmTreatyCollectApply(TreatyConfirmDTO dto, TransactionEntity entity,String gbpTransMerchantId) {
		dto.setService("gbp_same_id_confirm_treaty_collect_apply");// 接口名称，固定不变
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setProductNo(gbpTreatyProductId);		
		System.out.println("请求信息为：" + dto.toString());
		TreatyConfirmResultDTO result=null;
		try {
			result = gbpService.confirmTreatyCollectApply(dto);
			System.out.println("响应信息为:" + result.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date d = new Date();
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpTreatyConfirm);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setName(dto.getHolderName());
			entity.setCvv2(dto.getCustCardCvv2());
			entity.setCardvaliddate(dto.getCustCardValidDate());
			entity.setBankcardno(dto.getBankCardNo());			
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return result;
	}
	
	
	@Override
	public SameIDCreditCardTreatyCollectResultDTO sameIDCreditCardTreatyCollect(SameIDCreditCardTreatyCollectDTO dto, TransactionEntity entity,String gbpTransMerchantId) {
		String bankno = dto.getBankType();
		dto.setBankType(banknoMap.get(bankno));
		dto.setService("gbp_same_id_credit_card_treaty_collect");// 接口名称，固定不变
		dto.setMerchantId(gbpTransMerchantId);// 替换成快付通提供的商户ID，测试生产不一样
		dto.setVersion(version);// 接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		dto.setProductNo(gbpTreatyProductId);	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = new Date();
		dto.setTradeTime(sdf.format(d));// 交易时间,注意此时间取值一般为商户方系统时间而非快付通生成此时间
		dto.setCurrency("CNY");// 快付通定义的扣费币种,详情请看文档		
		System.out.println("请求参数:" + dto);
		SameIDCreditCardTreatyCollectResultDTO result=null;
		try {
			result = gbpService.sameIDCreditCardTreatyCollect(dto);
			System.out.println("响应结果:" + result);			
			entity.setTranstime(sdf.format(d));
			entity.setChannel(PARA_CONSTATNTS.Channel_KFT);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_QJ);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_GbpTreatyCollect);
			String status = QPayStatusToCode(result.getStatus()+"");
			entity.setStatus(status);
			entity.setReqcontent(JSON.toJSONString(dto));
			entity.setRespcontent(JSON.toJSONString(result));
			entity.setOrderno(dto.getOrderNo());
			entity.setMerchantid(dto.getMerchantId());
			entity.setName(dto.getHolderName());
			entity.setCvv2(dto.getCustCardCvv2());
			entity.setCardvaliddate(dto.getCustCardValidDate());
			entity.setBankcardno(dto.getBankCardNo());
			entity.setTranamt(Integer.parseInt(dto.getAmount()));
			entity.setIsnotice("2");
			transDao.insertTransaction(entity);
		} catch (GatewayClientException e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public String selectGbpTransMerchantId(String bankno){		
		String dbankno = banknoMap.get(bankno);
		String merchantId="";
		switch (dbankno) {
		//工,农,建,北京,30家地方角行
		case "1021000"://工
			merchantId=gbpTransMerchantId1;
			break;
		case "1031000"://农
			merchantId=gbpTransMerchantId1;
			break;
		case "1051000"://建
			merchantId=gbpTransMerchantId1;
			break;
		case "3131000"://北京银行
			merchantId=gbpTransMerchantId1;
			break;
		//中信,华夏,上海银行,广发,平安,民生
		case "3021000"://中信
			merchantId=gbpTransMerchantId2;
			break;
		case "3041000"://华夏
			merchantId=gbpTransMerchantId2;
			break;
		case "3135841"://上海银行
			merchantId=gbpTransMerchantId2;
			break;
		case "3065810"://广发
			merchantId=gbpTransMerchantId2;
			break;
		case "3135840"://平安
			merchantId=gbpTransMerchantId2;
			break;
		case "3051000"://民生
			merchantId=gbpTransMerchantId2;
			break;
		//浦发,光大,交通,招商,兴业
		case "3011000"://交通
			merchantId=gbpTransMerchantId3;
			break;
		case "3102900"://浦发
			merchantId=gbpTransMerchantId3;
			break;
		case "3031000"://光大
			merchantId=gbpTransMerchantId3;
			break;
		case "3085840"://招商
			merchantId=gbpTransMerchantId3;
			break;
		case "3091000"://兴业
			merchantId=gbpTransMerchantId3;
			break;
		default:
			merchantId=gbpTransMerchantId1;
			break;
		}
		return merchantId;
	}
	@Override
	public String banknoToKFTbankno(String bankno){
		return banknoMap.get(bankno);
	}
	
	@Override
	public String banknoToBankName(String bankno){
		return banknameMap.get(bankno);	
//		switch (bankno) {
//		case "3154560":
//			bankname="恒丰银行";
//			break;
//		case "3131210":
//			bankname="河北银行";
//			break;
//		case "3135841":
//			bankname="上海银行";
//			break;
//		case "1051000":
//			bankname="建设银行";
//			break;
//		case "1021000":
//			bankname="工商银行";
//			break;
////		case "1031000":
////			bankname="农业银行";
////			break;
//		case "3091000":
//			bankname="兴业银行";
//			break;
//		case "3031000":
//			bankname="光大银行";
//			break;
//		case "3065810":
//			bankname="广发银行";
//			break;
//		case "3135840":
//			bankname="平安银行";
//			break;
//		case "3051000":
//			bankname="民生银行";
//			break;
//		case "1041000":
//			bankname="中国银行";
//			break;
////		case "0025840":
////			bankname="邮储银行";
////			break;
//		case "3085840":
//			bankname="招商银行";
//			break;
//		case "3041000":
//			bankname="华夏银行";
//			break;
//		case "3021000":
//			bankname="中信银行";
//			break;
//		case "3011000":
//			bankname="交通银行";
//			break;
//		case "3102900":
//			bankname="浦发银行";t.
//			break;
////		case "5315840":
////			bankname="花旗银行";
////			break;
//		default:
//			break;
//		}
//		return bankname;		
	}
	@Override
	public PublicNoPayRespDTO publicNoPay(PublicNoPayReqDTO reqDTO,TransactionEntity entity) {
		reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
		reqDTO.setService("kpp_pa_pay");//接口名称，固定不变
		reqDTO.setVersion(version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
		reqDTO.setMerchantId(merchantId);//替换成快付通提供的商户ID，测试生产不一样
		reqDTO.setCurrency("CNY");//币种
//		reqDTO.setSecMerchantId(secMerchantId);
//		reqDTO.setOrderNo(orderNo);//交易编号 
		/*
		reqDTO.setTerminalIp("192.168.1.105"); //APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
		reqDTO.setNotifyUrl(notifyUrl_ba);//必须可直接访问的url，不能携带参数
		reqDTO.setProductId("102");//此id为二维码中包含的商品ID，商户自行定义
		reqDTO.setAmount("10000");//此次交易的具体金额,单位:分,不支持小数点		
		reqDTO.setTradeName("测试");//商品描述,简要概括此次交易的内容.可能会在用户App上显示
		reqDTO.setRemark("测试");//商品详情 可空
		reqDTO.setTradeTime("20181214160643");//商户方交易时间 注意此时间取值一般为商户方系统时间而非快付通生成此时间 20120927185643
		reqDTO.setSubAppId("gh_786273a4aad6");//商户公众号账号ID
		reqDTO.setUserOpenId("oZ29IwcKXzcRquJDimXwpgHasek0");//用户在支付渠道的ID,如微信的openId，可以通过微信、支付宝SDK获取。 微信可参考： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_4
		reqDTO.setBankNo("0000001");//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003 
		*/
//		reqDTO.setIsS0(0);//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务。如果无此业务权限，此参数为1，则返回失败。 可空 
//		reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
//		reqDTO.setIsSplit("1");//是否分账交易,1:是，0：否 ，
		PublicNoPayRespDTO resp = null;
		try {
			logger.info("请求参数:" + JSON.toJSONString(reqDTO));
			resp = initiativePayService.publicNoPay(reqDTO);
			logger.info("公众号支付响应：" + JSON.toJSONString(resp));			
			entity.setReqcontent(JSON.toJSONString(reqDTO));
			entity.setRespcontent(JSON.toJSONString(resp));
			String status = QPayStatusToCode(resp.getStatus());
			entity.setMerchantid(merchantId);
			entity.setStatus(status);
			entity.setTranamt(Integer.parseInt(reqDTO.getAmount()));
			entity.setIsnotice("2");
//			transDao.insertTransaction(entity);			
		} catch (GatewayClientException e) {
			e.printStackTrace();
		} 		
		return resp;
	}
	@Override
	public String payChannelToKFTchannel(String payChannel){
		String kftcode = null;
		if(PARA_CONSTATNTS.PayChannel_WX.equals(payChannel)){
			kftcode = "0000001";
		}else if(PARA_CONSTATNTS.PayChannel_ZFB.equals(payChannel)){
			kftcode = "0000002";
		}else if(PARA_CONSTATNTS.PayChannel_YL.equals(payChannel)){
			kftcode = "0000003";
		}
		return kftcode;
	}
}
