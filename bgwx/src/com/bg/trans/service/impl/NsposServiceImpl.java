package com.bg.trans.service.impl;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.nspos.TransE1102Req;
import com.bg.trans.entity.nspos.TransE1102Rsp;
import com.bg.trans.entity.nspos.TransE1103Req;
import com.bg.trans.entity.nspos.TransE1103Rsp;
import com.bg.trans.service.INsposService;
import com.bg.trans.service.IWXNoticeService;
import com.bg.util.DateUtil;
import com.bg.util.HttpsUtil;
import com.bg.util.SetSystemProperty;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;
import com.bg.web.entity.AccountInfo;
import com.huifu.saturn.cfca.CFCASignature;
import com.huifu.saturn.cfca.VerifyResult;

@Service("nsposService")
public class NsposServiceImpl implements INsposService {
	private static Logger logger = Logger.getLogger(NsposServiceImpl.class);
	private static String httpUrl=SetSystemProperty.getKeyValue("httpUrl","hftx/nspos.properties");
	private static String pfx_path=SetSystemProperty.getKeyValue("pfx_path","hftx/nspos.properties");
	private static String pfx_passwd=SetSystemProperty.getKeyValue("pfx_passwd","hftx/nspos.properties");
	private static String cer_path=SetSystemProperty.getKeyValue("cer_path","hftx/nspos.properties");
	private static String notify1103=SetSystemProperty.getKeyValue("notify1103","hftx/nspos.properties");
	private static String merids = SetSystemProperty.getKeyValue("merids","hftx/nspos.properties");
	@Autowired
	private ITransDao transDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IWXNoticeService wxNoticeService;
	private String[] ms;
	private static int i=0;
	private static Map<String,String> notifyOrder = new HashMap<String,String>();
	
	@Override
	public TransE1102Rsp transE1102(TransE1102Req req, TransactionEntity entity) {
		double rate = 0.018;
		if(ms==null){
			ms = merids.split("\\|");
			i = (int)(Math.random()*ms.length);
		}
		i++;
		if(i>=ms.length){
			i=0;
		}
		System.out.println("merids"+merids);
		System.out.println("i"+i);
		String merid = ms[i];
		System.out.println("merid"+merid);
		String url = httpUrl+"E1102";
		TransE1102Rsp rsp1102 = null;
		try{
//			String orderId = UtilData.createOnlyData();
//			TransE1103Req req1103 = new TransE1103Req();
			
			
//			jsonDataMap.put("termOrdId" , generateSeq(20));
//			jsonDataMap.put("ordAmt" , ordAmt);
//			jsonDataMap.put("authCode" , "134755413751742262");//二维码
					
			
			req.setMemberId(merid);
			req.setApiVersion("3.0.0.2");
			req.setGoodsDesc(URLEncoder.encode("商品","UTF-8"));
			JSONObject merPriv = new JSONObject();
	        merPriv.put("callType" , "04");
	        merPriv.put("merNoticeUrl" , notify1103);
	        req.setMerPriv(merPriv.toJSONString());
//			req1103.setTimeExpire("");
			
			String jsonData =  JSON.toJSONString(req);			
			String checkValue = sign(jsonData);			
			String result = post(url,jsonData,checkValue);		
			
			JSONObject jsonObject = JSONObject.parseObject(result);
			String respSign = jsonObject.getString("checkValue");
			String respCode = jsonObject.getString("respCode");
			String respDesc = jsonObject.getString("respDesc");
			String respData = jsonObject.getString("jsonData");
			
			rsp1102 = JSONObject.parseObject(respData, TransE1102Rsp.class);
			if(rsp1102==null){
				rsp1102=new TransE1102Rsp();
			}
			rsp1102.setRespCode(respCode);
			rsp1102.setRespDesc(respDesc);
			Date d = new Date();
			entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
			entity.setChannel(PARA_CONSTATNTS.Channel_HFTXNSPOS);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_ZFB);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_BDPay);
			String status = "02";
			if("000000".equals(respCode)){
				status="03";
			}			
			entity.setRate(rate);
			entity.setStatus(status);
			entity.setReqcontent(jsonData);
			entity.setRespcontent(JSON.toJSONString(rsp1102));
			entity.setMerchantid(req.getMemberId());
			entity.setIsnotice("2");		
			transDao.insertTransaction(entity);					
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rsp1102;
	}
	
	
	@Override
	public TransE1103Rsp transE1103(TransE1103Req req,TransactionEntity entity) {
		double rate = 0.018;
		if(ms==null){
			ms = merids.split("\\|");
			i = (int)(Math.random()*ms.length);
		}
		i++;
		if(i>=ms.length){
			i=0;
		}
		System.out.println("merids"+merids);
		System.out.println("i"+i);
		String merid = ms[i];
		System.out.println("merid"+merid);
		String url = httpUrl+"E1103";
		TransE1103Rsp rsp1103 = null;
		try{
//			String orderId = UtilData.createOnlyData();
//			TransE1103Req req1103 = new TransE1103Req();
			/*** 分账
			Map m = new HashMap();
			m.put("divCusCounts", "2");
			m.put("feeMemberId", "310000016000762177");
			
			List l = new ArrayList();
			
			
			Map d = new HashMap();
			d.put("divRate","0.01");
			d.put("memberId", "310000016000762177");
			Map d2 = new HashMap();
			d2.put("divRate","0.99");
			d2.put("memberId", "310000016000714060");
			l.add(d);
			l.add(d2);
			m.put("accInfos", l);
			
			String accSplitBunch = JSONObject.toJSONString(m);
			
			req1103.setAccSplitBunch(accSplitBunch);
			*/
//			req1103.setMemberId("310000016000762177");
//			req1103.setTermOrdId(orderId);				
//			req1103.setOrdAmt("2000.00");
			req.setMemberId(merid);
			req.setApiVersion("3.0.0.2");
			req.setPayChannelType("A1");
			req.setGoodsDesc(URLEncoder.encode("商品","UTF-8"));
			JSONObject merPriv = new JSONObject();
	        merPriv.put("callType" , "04");
	        merPriv.put("merNoticeUrl" , notify1103);
	        req.setMerPriv(merPriv.toJSONString());
//			req1103.setTimeExpire("");
			
			String jsonData =  JSON.toJSONString(req);			
			String checkValue = sign(jsonData);			
			String result = post(url,jsonData,checkValue);		
			
			JSONObject jsonObject = JSONObject.parseObject(result);
			String respSign = jsonObject.getString("checkValue");
			String respCode = jsonObject.getString("respCode");
			String respDesc = jsonObject.getString("respDesc");
			String respData = jsonObject.getString("jsonData");
			
			rsp1103 = JSONObject.parseObject(respData, TransE1103Rsp.class);
			if(rsp1103==null){
				rsp1103=new TransE1103Rsp();
			}
			rsp1103.setRespCode(respCode);
			rsp1103.setRespDesc(respDesc);
			Date d = new Date();
			entity.setTranstime(DateUtil.getDateString(d, "yyyyMMddHHmmss"));
			entity.setChannel(PARA_CONSTATNTS.Channel_HFTXNSPOS);
			entity.setPaychannel(PARA_CONSTATNTS.PayChannel_ZFB);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_ZDPay);
			String status = "02";
			if("000000".equals(respCode)){
				status="03";
			}			
			entity.setRate(rate);
			entity.setStatus(status);
			entity.setReqcontent(jsonData);
			entity.setRespcontent(result);
			entity.setMerchantid(req.getMemberId());
			entity.setIsnotice("2");		
			transDao.insertTransaction(entity);				
			System.out.println(rsp1103.getQrcodeUrl());			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return rsp1103;
	}

	@Override
	public void processNotify(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("process NOPOS notify=============");		
		String jsonData = request.getParameter("jsonData");
		String checkValue = request.getParameter("checkValue");		
		JSONObject jo = JSONObject.parseObject(jsonData);		
		String respSign;
		try {
			
			respSign = URLDecoder.decode(checkValue , "UTF-8");
//			System.out.println("checkValue:\r\n" + respSign);
			System.out.println(jsonData);
			VerifyResult verifyResult = verify("310000015000707678".substring(0,16),checkValue);
			System.out.println(verifyResult.getCode() + "\t" + verifyResult.getMessage());
			VerifyResult verifyResult2 = verify("310000016000762177".substring(0,16),checkValue);
			System.out.println(verifyResult2.getCode() + "\t22222222222222:" + verifyResult2.getMessage());			
			String orderId=jo.getString("termOrdId");
			String oid = jo.getString("ordId");
			String amt = jo.getString("settlementAmt");
			System.out.println(orderId+":"+amt);	
			TransactionEntity entity = new TransactionEntity();
			entity.setOrderno(orderId);
			entity.setNotifycontent(jsonData);		
			response.getWriter().print("RECV_ORD_ID_" + oid);
			response.flushBuffer();			
			TransactionEntity qte = transDao.queryTransactionByOrderNo(orderId);
			System.out.println("transStat:"+jo.getString("transStat"));
			//如果状态是成功,则不能修改
			if("S".equals(jo.getString("transStat"))&&qte!=null){	
				System.out.println("qqqqqq00000");
				String o = notifyOrder.get(orderId);						
//				System.out.println("SSSSSSSSSSSSSSSSSSs");
				entity.setStatus("01");
				transDao.updateTransaction(entity);				
				String openId = qte.getDeviceid();
				String accountId = qte.getAccountid();
				String date = jo.getString("transDate");
				String zfb = jo.getString("openId");	
				if(o==null){
					notifyOrder.put(orderId, orderId);
					System.out.println("qqqqqq");
				}else{
					System.out.println("qqqqqq11111");
					return;
				}		
				wxNoticeService.gbpSwipeNotice(openId, orderId, zfb, amt, date, "01", "感谢您的使用");
				/************分润奖励*************************/				
				List<Map<String,Object>> recommenders = accountDao.findRelationshopBySubAccount(accountId);
				for(Map<String,Object> m:recommenders){
					int level = (Integer)m.get("LEVEL");
					double r = 0;
					if(level==1){
						r=0.002;
					}else if(level==2){
						r=0.0005;
					}else if(level==3){
						r=0.0005;
					}else{
						continue;
					}
					double settleAmt = Double.parseDouble(amt);
					double rewardAmt = UtilData.multiply(settleAmt, r);
					BigDecimal b = new BigDecimal(rewardAmt);
					rewardAmt = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
					if(rewardAmt<0.01){
						continue;
					}
					String recommenderId = (String)m.get("ACCOUNT");
					//int rowId = transDao.insertRewardLog(accountId,recommenderId,rewardAmt,PARA_CONSTATNTS.Reward_Trans,qte.getId()+"","b_transaction",0,null,null);
					updateAccountBalance(recommenderId,rewardAmt,PARA_CONSTATNTS.Reward_Trans,String.valueOf(qte.getId()),"b_transaction");
					AccountInfo recommenderInfo = accountDao.findAccountByAccountId(recommenderId);
					if(recommenderInfo!=null){
						DecimalFormat df = new DecimalFormat("0.00");					
						wxNoticeService.gbpTransRewardNotice(recommenderInfo.getDeviceId(), orderId, df.format(rewardAmt));		
					}							
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public static String post(String httpUrl,String jsonData,String checkValue) {		
		String body = null;
		CloseableHttpClient httpclient = null;
		try{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("jsonData", jsonData));
	        params.add(new BasicNameValuePair("checkValue", checkValue));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
//			httpclient = HttpClients.createDefault();
			httpclient = HttpsUtil.getHttpClient();
			HttpPost post = new HttpPost(httpUrl);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);		
			System.out.println(jsonData);
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
		return body;
	}
	
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
	
	public static String sign(String context){
		return CFCASignature.signature(pfx_path, pfx_passwd, context, "UTF-8").getSign();
	}
	
	private VerifyResult verify(String mer_id,String sign){
		return CFCASignature.verifyChinaPnRSign(mer_id, sign ,"UTF-8", cer_path);
	}
	
	public static void main(String[] arg){
		try{		
			String url = httpUrl+"E1103";
			TransE1103Req req = new TransE1103Req();
			String orderId = UtilData.createOnlyData();
			req.setTermOrdId(orderId);				
			req.setOrdAmt("0.01");			
			req.setMemberId("310000016002364644");
			req.setApiVersion("3.0.0.2");
			req.setPayChannelType("A1");
			req.setGoodsDesc(URLEncoder.encode("装修款","UTF-8"));
			JSONObject merPriv = new JSONObject();
	        merPriv.put("callType" , "04");
	        merPriv.put("merNoticeUrl" , "http://test.widefinance.cn/gateway/nsposnofity");
	        req.setMerPriv(merPriv.toJSONString());
	//		req1103.setTimeExpire("");
			//https://qr.alipay.com/bax03580cbit9ua0f9i060fc
			String jsonData =  JSON.toJSONString(req);		
			
			String checkValue = CFCASignature.signature("D:/shanzhi.pfx", "123456", jsonData, "UTF-8").getSign();
//			String checkValue = sign(jsonData);			
			String result = post(url,jsonData,checkValue);		
//			
			JSONObject jsonObject = JSONObject.parseObject(result);
			String respSign = jsonObject.getString("checkValue");
			String respCode = jsonObject.getString("respCode");
			String respDesc = jsonObject.getString("respDesc");
			String respData = jsonObject.getString("jsonData");
			System.out.println(respData);
		}catch(Exception e){
					
		}
			
//		System.out.println("310000015000707678".substring(0,16));
//		Map m = new HashMap();
//		m.put("divCusCounts", "2");
//		m.put("feeMemberId", "310000016000762177");
//		
//		List l = new ArrayList();
//		
//		
//		Map d = new HashMap();
//		d.put("divRate","0.01");
//		d.put("memberId", "310000016000762177");
//		Map d2 = new HashMap();
//		d2.put("divRate","0.99");
//		d2.put("memberId", "310000016000714060");
//		l.add(d);
//		l.add(d2);
//		m.put("accInfos", l);
		
//		String accSplitBunch = JSONObject.toJSONString(m);
//		System.out.println(accSplitBunch);
//		String amt="10.1";
//		DecimalFormat decimalFormat = new DecimalFormat("0.00");
//		String amount = decimalFormat.format(Double.parseDouble(amt));		
//		System.out.println(amount);
//		String m ="310000016000778603|310000016000778596|310000016000776075|310000016000776043|310000016000762177|310000016000732799|310000016000714060";
//		String[] ms = m.split("\\|");
//		int i = 0;
//		double d = Math.random();
//		i = (int)(d*ms.length);
//		System.out.println(d);
//		System.out.println("merids"+merids);
//		System.out.println("i"+i);
//		String merid = ms[i];
//		System.out.println(merid);
//		NsposServiceImpl service = new NsposServiceImpl();
//		service.transE1103(null);
	}

	



	
}
