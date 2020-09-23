package com.bg.interfaces.controller;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.codec.binary.Base64;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.service.IGateWayService;
import com.bg.trans.service.ITransKFTService;
import com.bg.util.SignTools;
import com.bg.web.service.MerchantServiceI;

@Controller
public class GateWayController {
	private static Logger logger = Logger.getLogger(GateWayController.class);
	@Autowired
	private ITransKFTService transKFTService;	
	@Autowired
	private MerchantServiceI merchantService;		
	@Autowired
	private IGateWayService gateWayServiceByKFT;
	@Autowired
	private IGateWayService gateWayServiceByYJF;
	@Autowired
	private IGateWayService gateWayServiceByLD;
//	@Autowired
//	private INsposService nsposService;
//	@Autowired
//	private GateWayServiceByYBZFImpl gateWayServiceByYBZF;
//	@Autowired
//	private IGateWayService gateWayServiceByHFTXEloan;
//	@Autowired 该通道已暂停
//	private IGateWayService gateWayServiceByYZF;	
//	@Autowired
//	private ITransSDService transSDService;
	private static Map<String,PublicKey> publicKeyMap = new HashMap<String,PublicKey>();
	private static Map<String,Map<String,Object>> merchantInfoMap = new HashMap<String,Map<String,Object>>();
	private Map<String,String> merchantIdMap = new HashMap<String,String>();	
	private Map<String,String> channelMap = new HashMap<String,String>();
	private static PrivateKey privateKey=SignTools.getBAPrivateKey();
	private static Map<String,Set<String>> orderMap = new HashMap<String,Set<String>>();
	
	public static PublicKey getPublicKey(String mid){
		return publicKeyMap.get(mid);
	}
	
	public static Map<String,Object> getMerchantInfo(String mid){
		return merchantInfoMap.get(mid);
	}
	
	@RequestMapping("/gateway")
	@ResponseBody
	public Object gateway(HttpServletRequest request){
		System.out.println("gateway=====================================================");
		String mid = request.getParameter("mer_id");
		System.out.println("1111:"+request.getParameter("cmd_id"));
		System.out.println("2222:"+request.getParameter("mer_id"));
		System.out.println("3333:"+request.getParameter("data"));
		System.out.println("4444:"+request.getParameter("sign"));
		if(publicKeyMap.get(mid)==null){
			Map<String,Object> mrow = merchantService.getMerchantChannelInfo(mid);	
			if(mrow==null){
				return "商户号有误";
			}
			String keystr = (String)mrow.get("PUBLICKEY");
			String cmid = (String)mrow.get("CMID");
			String channel = (String)mrow.get("CHANNEL");
			System.out.println("PUBLICEKEY:"+keystr);
			System.out.println("cmid:"+cmid);
			if(keystr==null||"".equals(keystr)){
				return "该商户未配置密钥";
			}
			merchantInfoMap.put(mid, mrow);
			publicKeyMap.put(mid, SignTools.getPublicKey(keystr));
			merchantIdMap.put(mid, cmid);
			channelMap.put(mid, channel);
			orderMap.put(mid, new HashSet<String>());
		}
		PublicKey publicKey = publicKeyMap.get(mid);		
		String edata = request.getParameter("data");
		System.out.println("edata:"+edata);
		String sign = request.getParameter("sign");		
		String data = new String(SignTools.decrypt2(Base64.decodeBase64(edata), privateKey));
		System.out.println("data:"+data);
		boolean vb = SignTools.verify(data.getBytes(), sign, publicKey, SignTools.SIGN_TYPE_SHA1RSA);
		System.out.println("vb:"+vb);
		if(!vb){
			return errorResponse(publicKey,"999999","验签失败");
		}		
		Object respStr = null;
		String cmdId = request.getParameter("cmd_id");
		String channel = channelMap.get(mid);
		System.out.println("channel:"+channel);
		IGateWayService service;
		switch(channel){
			case "01":
				service = gateWayServiceByKFT;
				break;	
//			case "03":
//				service = gateWayServiceByYZF;
//				break;
//			case "04":
//				service = gateWayServiceByYBZF;
//				break;
			case "05":
				service = gateWayServiceByYJF;
				break;
//			case "06":
//				service = gateWayServiceByHFTXEloan;
//				break;
			case "08":
				service = gateWayServiceByLD;
				break;				
			default:
				return errorResponse(publicKey,"999999","无有效通道");
		}		
		switch (cmdId) {		
		case "1001":
			respStr = service.process1001(mid, merchantIdMap.get(mid), data, publicKey);
			break;			
		case "1003":
			respStr = service.process1003(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1007":
			respStr = service.process1007(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1008":
			respStr = service.process1008(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1017":
			respStr = service.process1017(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1020":
			respStr = service.process1020(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1021":
			respStr = service.process1021(mid, merchantIdMap.get(mid), data, publicKey);
			break;	
		case "1023":
			respStr = service.process1023(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1024":
			respStr = service.process1024(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1025":
			respStr = service.process1025(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1026":
			respStr = service.process1026(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1027":
			respStr = service.process1027(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1028":
			respStr = service.process1028(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1029":
			respStr = service.process1029(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1030":
			respStr = service.process1030(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1031":
			respStr = service.process1031(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "1032":
			respStr = service.process1032(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "2002":
			respStr = service.process2002(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		case "2003":
			respStr = service.process2003(mid, merchantIdMap.get(mid), data, publicKey);
			break;
		default:
			return errorResponse(publicKey,"999999","无效的cmd_id:"+cmdId);
		}
		return respStr;
	}	
	
//	@RequestMapping("/gateway/yzfnotify")
//	@ResponseBody
//	public void yzfNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{		
//		gateWayServiceByYZF.processNotify(request,response);
//	}	
	
//	@RequestMapping("/gateway/hftxeloannotify")
//	@ResponseBody
//	public void hftxEloanNotify(HttpServletRequest request,HttpServletResponse response) {		
//		gateWayServiceByHFTXEloan.processNotify(request,response);
//	}
	
	@RequestMapping("/gateway/ldnofity")
	@ResponseBody
	public void ldNotify(HttpServletRequest request,HttpServletResponse response) {		
		gateWayServiceByLD.processNotify(request,response);
	}
	
//	@RequestMapping("/gateway/nsposnofity")
//	@ResponseBody
//	public void nsposNotify(HttpServletRequest request,HttpServletResponse response) {		
//		nsposService.processNotify(request,response);
//	}
	
	@RequestMapping("/gateway/kftnotify")
	@ResponseBody
	public void kftnotify(HttpServletRequest request,HttpServletResponse response) {		
		gateWayServiceByKFT.processNotify(request,response);
	}
	
	@RequestMapping("/gateway/yjfnotify")
	@ResponseBody
	public void yjfnotify(HttpServletRequest request,HttpServletResponse response) {		
		request.setAttribute("cmd", "1017");
		gateWayServiceByYJF.processNotify(request,response);
	}
	@RequestMapping("/gateway/yjftreatynotify")
	@ResponseBody
	public void yjfTreatyNotify(HttpServletRequest request,HttpServletResponse response) {		
		request.setAttribute("cmd", "1025");
		gateWayServiceByYJF.processNotify(request,response);
	}	
	
	private Object errorResponse(PublicKey publicKey,String resultCode,String resultMessage){
		Map<String,String> dsm = new HashMap<String,String>();
		dsm.put("resp_code", resultCode);
		dsm.put("resp_desc", resultMessage);
		String ds = JSON.toJSONString(dsm);
		String data = Base64.encodeBase64String(SignTools.encrypt2(ds.getBytes(), publicKey));	
		Map<String,String> rm = new HashMap<String,String>();
		rm.put("data", data);		
		String dsign = SignTools.sign(ds.getBytes(), privateKey, SignTools.SIGN_TYPE_SHA1RSA);
        rm.put("sign", dsign);        
		return rm;
	}
	
	public static String respCoceByKFT(String cmd_id,String status){
		String resp_code = "";
		switch (cmd_id) {
		case "1008":
			resp_code = "100801";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100800";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100802";
			}	
			break;
		case "1007":
			resp_code = "100701";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100700";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100702";
			}	
			break;
		case "1001":
			resp_code = "100101";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "100100";
			}else if("2".equals(status)||"5".equals(status)){
				resp_code = "100102";
			}	
			break;
		case "1021":
			resp_code = "102102";
			if("1".equals(status)||"4".equals(status)){
				resp_code = "102100";
			}else if("3".equals(status)){
				resp_code = "102101";
			}else if("7".equals(status)){
				resp_code = "102103";
			}	
			break;
		default:
			break;
		}
		return resp_code;
	}
	
	public static void main(String[] arg){

		String url = "http://test.widefinance.cn/gateway.do";
//		String url ="http://api.widefinance.cn/gateway";
		Map<String,String> m = new HashMap<String,String>();
		
		
		
		String cmd = "2003";
		String merid = "20190428001";
		m.put("cmd_id", cmd);
		m.put("mer_id", merid);
		m.put("order_id", "2019050820000224823729");
		m.put("order_date", "20190508111529");
		
		m.put("card_no", "6232510000000006");
		m.put("cust_name", "张三");
		m.put("idcard", "110101198506020011");
		
		//m.put("idcard", "110101198506020011");
		/**
		m.put("card_no", "6232510000000006");
		*/
		/**
		m.put("trans_type", "1028");
		*/
		/**
		m.put("product_type", "00");
		m.put("user_name", "张三");
		m.put("cert_id", "110101198506020011");
		m.put("card_no", "6232510000000006");
		m.put("repay_mode", "0");
		m.put("trans_amt", "11.00");
		m.put("loan_request_seq", "2019050820000224823703");
		m.put("back_cert_type", "2");
		m.put("back_cert_id", "91110108MA00B71C04");
		*/
		
		/**
		m.put("smscode", "111111");
		*/
		
		/**
		m.put("card_no", "6232510000000006");
		m.put("bank_id", "01050000");
		m.put("cust_name", "张三");
		m.put("idcard", "110101198506020011");
		m.put("bank_phone", "13801110303");
		*/
		
		
		/**
		m.put("loan_request_seq", "2019022020000224823719");
		m.put("card_no", "6232510000001003");
		*/
		
		/**
		m.put("product_type", "00");
		m.put("loan_purpose", "00");
		m.put("loan_start_date", "20190428");
		m.put("loan_end_date", "20190528");
		m.put("loan_period", "1");
		m.put("loan_period_type", "01");
		m.put("loan_amount", "1100.00");
		m.put("entrusted_flag", "N");
		m.put("user_name", "张三");
		m.put("user_cert_id", "110101198506020011");
		m.put("user_card_no", "6232510000001003");
		m.put("user_amount", "1100.00");
		m.put("user_cash_method", "T0");
		m.put("installment_number", "1");
		m.put("installment_rate", "23.00");
		m.put("payment_method", "00");
		*/
		/**
		m.put("bank_no", "01050000");
		m.put("card_no", "6232510000001004");
		m.put("idcard", "110101198506020011");
		m.put("cust_name","张三");
		m.put("phone", "13021110302");
		*/
//		m.put("trans_type", "1025");
//		m.put("trans_amt", "0.01");
//		m.put("termianlip", "117.61.130.87");
//		m.put("bg_ret_url", "http://www.baidu.com/pay/wide/notify_url.php");
//		m.put("good_desc", "onlinepay");
//		m.put("pay_type", "02");
		String data = JSONObject.toJSONString(m);
		System.out.println("d1:"+data);
		System.out.println("d2:"+JSONObject.toJSONString(m));
		System.out.println("d3:"+JSON.toJSONString(m));								
//		String privatekeystrsh="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIeIVg+EbveE+qjub9Y7/sQ9JmrBK2eJKhl+L3/cUZfv24GjGGds9ATopMQmnGTFaT4p2HFc0r73imz7cm9KdrOfNSdlAhbj96fdaVy9CmVjALQM4GeM5fPetBJndMQxDxciF0/SJUU3i7qzz/PUU4SeeIehOjvNTIpkfeWajcfNAgMBAAECgYBQXaKSbvgR45Nyyngcc9Tc7lmyYbroGOiS10U5Lx5e1sa1d02IPo3vvze8bBy+kGSYjN2gUudQf55ggzVXErORcCUAPCL+IwPqhNBTC7u7pudrJLdc0UfhWYTlBNWYsz/8EAT+LyaMeyJ7xASFex2cVMZ3LlqraVfYa+ihXpRVQQJBAMC2CQhprHN8yfoqBKp6waVvvRGhOhTeHPfUxW08iNCcm+3FplmvZQoaF/zZN2jj0agi3IMkPd2dJsIye9j/Sp0CQQC0CxUa8SlwjVQGSa5NtmKeONsF2XY7JnKi36cjCEH1cMr0H2W8cICt71V6hVc3kKq+t49nnhK6Af+/gpoe55LxAkAwnjxNKQve3v695FflAq9UI4qZpglXNmoshDuCwaDqsHgOVq0PN1bQY6Dlo499IOzF9HWZiE0rWfhrSf1A07dpAkB4awUlrPCzd8MgJX2wOnFBM/PBuM2sCC4aI45NDwPWnicQGkPYQkK+ktnQBYjtABRDVNBUVFRgFmAwfbCJRQdxAkANx2JJmDcsxA+VrEyOVON0/ijJb8hWdWOcPlTRXGsQzEPOYzsJVIGivybR0PP7CdYf9mEC1z/yuk+Dz4qcF+6c";
		String privatekeystrsh="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANQtkp176elAvXDuu6w7tYXeFSxUfNTDqvVt8R45eg0ud1HPsAyyB3ejCH/8p3sPuLx/YKbemW5IC7SXMfxzSB4Mq75G/NiMDwefJzgn1ClQkeZQIJ8bX4JVtK+biotrG6J57MybKDeeMNTsVl5tYCON7yW5AKehL6xQnoHTsefFAgMBAAECgYEAhfqM9SK0zpQRy4kEOrqtYe2dQxPKi3NOtZGJysMSfdZUg/V4PlwAeRwalu7MNtnzlht8xYIUMl4N/ifm6XlGFIUnvjPnjrx7RSyB/b0hRzbnFTQFe2ykhB9jhcSXWB1ER9jvSsfpDsfLfnPboJM3GRn43LQvELAv4aASyrGenLUCQQDpmVuDN7bLUM7NkNBR9BRXVKCaCNAE+ukm6Nz3Qklc1INiuvl72AxPvhbSvgqilju4awP1uMIU8I8oDzcJYuBDAkEA6IZZNy4AR5NZHibskfbLkv/knQdgOBn6ChVy8Rtflzeny9WigR9xa7geSrmDXUWQZ75moe7UbFEEho4f2ZL7VwJAP7hyiw+mkD1hvBdVjBVtewj9qibfP4yGDvQUWmo9gtIBaOwh49NiQFpU9XWbhxA+CCdA9EVKw9V+52mHFMtg6wJAdJyj83NjeoHgCKoWrGEr2Q3yNfoz/A6zAgmdumMy/mBQC36ZX85IEHCm6Gy+/7DaadzoFb/z0lqTPXPbbz8yQwJBALKzCPhyqmVkplVSpnlFNlBVJzxVRm46AM5+N7lrmV+AJi3AkIr2tKuu774sqzS44m8VHdl4MjlKowteajFeNpw=";							  
		String pubkeybgstr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3zJILWQI9ps3ngqwFUlEcaJK70Skq1e4pL7aWIU3ltjO1zUkO5eL7xKf30ybBQ2dq5+jZsmC63hruijcDQ7GD3WBoKq2RQ3J6/+K9jae2cxXyxhDRjCVxFfwrYFM9jJhmqQLF/JtB7S19V//Xhjiw07BxZR9QOA/8Xlqh6m+oewIDAQAB";		  
//		String pubkeybgstr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZ+5ZmstjNfGhQb+/kwo2KbolOfFB2tkc833vPUmp9xywwksVLYwlWqwyAN4wW30Z5q7+/aNjpXB0/S8EV9S3vUiVOUmCQNlZEJl3IiIXULydORGdEZVbuVlUIeQsM6hX0wShoiFkb11UYBB9kgy/j0GQdD1xF2GQGGXRH/rC+zQIDAQAB";
						
		PublicKey pubkeybg = SignTools.getPublicKey(pubkeybgstr);	
		try{
			byte[] keyBytes = Base64.decodeBase64(privatekeystrsh.getBytes());  
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
	        PrivateKey privateKey1 = keyFactory.generatePrivate(keySpec); 
	        
	        String ed1 = Base64.encodeBase64String(SignTools.encrypt2(data.getBytes(), pubkeybg));
	        String sign1= SignTools.sign(data.getBytes(), privateKey1, SignTools.SIGN_TYPE_SHA1RSA);
	        System.out.println(ed1);
	        System.out.println(sign1);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("cmd_id", cmd));
			params.add(new BasicNameValuePair("mer_id", merid));
	        params.add(new BasicNameValuePair("data", ed1));
	        params.add(new BasicNameValuePair("sign", sign1));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	        
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			System.out.println("post url:"+url);
			if (response.getStatusLine().getStatusCode() == 200) {
				String body = EntityUtils.toString(response.getEntity(), "utf-8");
			    logger.info("post body:"+body);
			    
			    JSONObject jb = JSONObject.parseObject(body);
			    String d = jb.getString("data");
			    String sign = jb.getString("sign");
			    String edata = new String(SignTools.decrypt2(Base64.decodeBase64(d), privateKey1));		    
			    boolean vb = SignTools.verify(edata.getBytes(), sign, pubkeybg, SignTools.SIGN_TYPE_SHA1RSA);
			    System.out.println("vb:"+vb);
			    System.out.println(edata);
			    
			}else{
				logger.info(url+" "+entity.toString()+" "+response.getStatusLine().getStatusCode());
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
	}
	
}
