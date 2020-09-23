package com.bg.trans.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.soofa.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.TransactionEntity;
import com.bg.util.UtilData;
import com.bg.web.controller.CreditCardController;
import com.bg.web.dao.impl.CreditCardDaoImpl;
import com.bg.web.service.ICreditCardService;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardPayToBankAccountDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTradeResultDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectDTO;
import com.lycheepay.gateway.client.dto.gbp.sameID.SameIDCreditCardTreatyCollectResultDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryRespDTO;

@Component
public class ScheduleService {
	private static Logger logger = Logger.getLogger(ScheduleService.class);
	@Autowired
	private IWXNoticeService wxNoticeService;	
	@Autowired
	private ITransDao transDao;
	@Autowired
	private ITransKFTService transKFTService;
	@Autowired
	private ICreditCardService creditCardService;
	
	/**
	 * 信用卡代还定时任务
	 */
	//@Scheduled(fixedDelay=60000)
	public void gbpInsteadRepay(){
		double gbpRate = CreditCardController.gbpRate;
		SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date=new Date();
		String strdate = ymdhms.format(date);
		List<Map<String,Object>> rdatas = creditCardService.findRunRepayPlan(strdate);
		List<Map<String,Object>> pdatas = creditCardService.findRunPayPlan(strdate);
		
		for(Map<String,Object> m:rdatas){	
			String amount = (String)m.get("TRANAMT");
			String name = (String)m.get("NAME");
			String bankNo = (String)m.get("BANKNO");
			String bankCardno = (String)m.get("BANKCARDNO");
			String cardvaliddate = (String)m.get("CARDVALIDDATE");
			String cvv = (String)m.get("CVV2");
			String account = (String)m.get("ACCOUNT");
			String idcard = (String)m.get("IDCARD");
			String bankPhone = (String)m.get("BANKPHONE");
			String treatyId = (String)m.get("TREATYID");
			int pid = (int)m.get("PID");
			DecimalFormat decimalFormat = new DecimalFormat("#");
			decimalFormat.setRoundingMode(RoundingMode.DOWN);
			double ta = Double.parseDouble(amount)*gbpRate;
			String rateAmount = decimalFormat.format(ta);
			/*******************协议代扣****************************/
			SameIDCreditCardTreatyCollectDTO tcDto = new SameIDCreditCardTreatyCollectDTO();
			String tcReqNo = UtilData.createOnlyData();				
			tcDto.setReqNo(tcReqNo);
			tcDto.setOrderNo(tcReqNo);// 订单号同一个商户必须保证唯一
			tcDto.setTreatyNo(treatyId);// 协议代扣申请确认返回的协议号	
			tcDto.setAmount(amount);// 此次交易的具体金额,单位:分,不支持小数点		
			tcDto.setHolderName(name);// 持卡人姓名，与申请时一致
			tcDto.setBankType(bankNo);// 客户银行账户行别;快付通定义的行别号,详情请看文档
			tcDto.setBankCardNo(bankCardno);// 银行卡号，与申请时一致，本次交易中,从客户的哪张卡上扣钱
			tcDto.setCustCardValidDate(cardvaliddate);//可空，信用卡扣款时必填
			tcDto.setCustCardCvv2(cvv);//可空，信用卡扣款时必填
			tcDto.setRateAmount(rateAmount);// 手续费			
			TransactionEntity entity = new TransactionEntity();
			entity.setAccountid(account);
			entity.setTranamt(Integer.parseInt(amount));
			entity.setIdcard(idcard);
			entity.setBankphone(bankPhone);
			entity.setRate(CreditCardController.gbpRate);
			entity.setBankno(bankNo);
			String transMerchantId = transKFTService.selectGbpTransMerchantId(bankNo);
			SameIDCreditCardTreatyCollectResultDTO tcresp = transKFTService.sameIDCreditCardTreatyCollect(tcDto, entity,transMerchantId);
			String status = transKFTService.QPayStatusToCode(tcresp.getStatus()+"");
			if("01".equals(status)){
				creditCardService.updateInsteadRepayPlan(pid,"1",new Date());
			}else{
				creditCardService.updateInsteadRepayPlan(pid,"2",new Date());
			}			
		}
		
		/**********************执行还款********************************/
		if(pdatas.size()>0){
			//将List<Map> 转换成 List<List<Map>一天的交易> 		
			List<List<Map<String,Object>>> lp = new ArrayList<List<Map<String,Object>>>();
			String tpid = "";
			List<Map<String,Object>> plist = null;
			for(int i=0;i<pdatas.size();i++){
				Map<String,Object> m = pdatas.get(i);
				String pid = (String)m.get("pid");
				if(tpid.equals(pid)){
					plist.add(m);
				}else{
					plist = new ArrayList<Map<String,Object>>();
					lp.add(plist);
					plist.add(m);
				}
			}
			//List<List<Map>> 按天处理代付
			for(List<Map<String,Object>> oneList:lp){				
				boolean isRun = true;
				int amt = 0;//代扣失败金额
				int zr = 0; //一天就代扣总金额
				//一天内的数据
				for(Map<String,Object> m:oneList){
					String status = (String)m.get("PSTATUS");
					String type = (String)m.get("PTYPE");
					String tranamt = (String)m.get("TRANAMT"); 
					int pid = (int)m.get("PID");
					String bankno = (String)m.get("BANKNO");
					String bankCardNo = (String)m.get("BANKCARDNO");
					String name = (String)m.get("NAME");
					String idcard = (String)m.get("IDCARD");
					String account = (String)m.get("ACCOUNT");
					if("01".equals(type)){
						zr+=Integer.parseInt(tranamt);
					}
					if("01".equals(type)&&"0".equals(status)){
						//有代扣未执行 所以代付也不能执行
						isRun=false;
					}else if("01".equals(type)&&"2".equals(status)){
						//代扣处理失败 
						int ta = Integer.parseInt(tranamt);
						amt+=ta;
					}else if("02".equals(type)&&"0".equals(status)){
						if(isRun){
							/*********************代付********************************/
							String pamt = "";
							if(amt==0){
								int tx = Integer.parseInt(tranamt)+CreditCardController.gbpfee*100;
								pamt = Integer.toString(tx);
							}else{
								int jr = zr-amt;//实际扣成功金额
								DecimalFormat decimalFormat = new DecimalFormat("#");
								pamt = decimalFormat.format(jr*(UtilData.subtract(1, CreditCardController.gbpRate)));
							}							
							SameIDCreditCardPayToBankAccountDTO pDTO = new SameIDCreditCardPayToBankAccountDTO();
							String preqNo = UtilData.createOnlyData();
							pDTO.setReqNo(preqNo);// 商户可以根据此参数来匹配请求和响应信息；快付通将此参数原封不动的返回给商户		
							pDTO.setOrderNo(preqNo);// 订单号同一个商户必须保证唯一
							pDTO.setTradeName("费用支付");// 简要概括此次交易的内容				
							
							pDTO.setAmount(pamt);// 单位:分,不支持小数点				
							pDTO.setCustBankNo(bankno);// 客户银行账户行别;快付通定义的行别号,详情请看文档
							pDTO.setCustBankAccountNo(bankCardNo);// 本次交易中,从客户的哪张卡上扣钱
							pDTO.setCustName(name);// 付款人的真实姓名		
							pDTO.setCustID(idcard);// 证件号码
							pDTO.setRateAmount(CreditCardController.gbpfee*100+"");
							TransactionEntity entity = new TransactionEntity();
							entity.setAccountid(account);
							entity.setFee(CreditCardController.gbpfee*100);
							String transMerchantId = transKFTService.selectGbpTransMerchantId(bankno);
							SameIDCreditCardTradeResultDTO preq = transKFTService.sameIDGbpPay(pDTO, entity,transMerchantId);
							status = transKFTService.QPayStatusToCode(preq.getStatus()+"");
							if("01".equals(status)){
								creditCardService.updateInsteadRepayPlan(pid,"1",new Date());
							}else{
								creditCardService.updateInsteadRepayPlan(pid,"2",new Date());
							}								
						}else{
							logger.error("pid:"+pid+" 交易当天还有未执行的代扣");
						}
					}					
				}				
			}
			
		}
		
	}
	
	
	//@Scheduled(fixedDelay=60000)	
	public void doTimer(){
		List<TransactionEntity> list = transDao.queryNoticeOrder();
		for(TransactionEntity te:list){
			String orderno = te.getOrderno();
			if("03".equals(te.getStatus())){
				String stt = te.getTranstime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmiss");
				try {
					Date tt = sdf.parse(stt);
					Date d = new Date();
					long m = tt.getTime()-d.getTime();
					//TODO 定时查询  增加时间间隔					
				} catch (ParseException e) {
					logger.error(e.getMessage(),e);
					e.printStackTrace();
				}				
				TradeQueryReqDTO reqDTO = new TradeQueryReqDTO();
				reqDTO.setReqNo(System.currentTimeMillis()+"");
				reqDTO.setOriginalOrderNo(orderno);
				TradeQueryRespDTO resp = transKFTService.initiativePayQuery(reqDTO);
				logger.info("queryOrder req:"+JSONObject.toJSON(reqDTO));
				logger.info("queryOrder resp:"+JSONObject.toJSON(resp));
				String status = transKFTService.ZDPayStatusToCode(resp.getStatus());
				if("01".equals(status)){
					transDao.updateNotice(orderno);
					sendNotice(te);
				}
			}else{
				transDao.updateNotice(orderno);
				sendNotice(te);
			}			
			
		}
		
		
		
//		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
//		String amount = decimalFormat.format(Double.parseDouble(settlementAmount)/100);	
//		WXNoticeService.paymentNotice("ovnf9sjLvxT-qg9zdtkJcP_6xQNg", orderNo, amount, "2017-08-10","2017121500092289");
//		WXNoticeService.paymentNotice("ovnf9sjfojNq9_Cf9q3rym0SxAIU",orderNo, amount, "2017-08-10","2017121500092289");
//		System.out.println("dotimer=============="+new Date());
//		try {
//			Thread.sleep(6000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
//		WXNoticeService.paymentNotice("ovnf9sjLvxT-qg9zdtkJcP_6xQNg", orderNo, amount, "2017-08-10","2017121500092289");
	}
	
	private void sendNotice(TransactionEntity te){		
		String openid = te.getDeviceid();
		String orderno = te.getOrderno();
		int amt = te.getTranamt();
		String trantime = te.getTranstime();
		System.out.println("sendNotice:"+orderno+":"+":"+amt+":"+trantime+":"+openid);
		String tt = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date d = sdf.parse(trantime);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tt = sdf1.format(d);			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("0.00");
		String amount = df.format(amt/100);
		wxNoticeService.gbpSwipeNotice(openid, orderno,"", amount, tt,"","");
		System.out.println("sendNotice:"+orderno+":"+":"+amount+":"+tt+":"+openid);
	}
	
	public static void main(String[] arg){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d=null;
		try {
			d = sdf.parse("20171228183143");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf1.format(d));			
	}
}
