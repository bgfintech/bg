package com.bg.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.soofa.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Administrator
 * 
 */
public class SignTools {
	private static Logger logger = Logger.getLogger(SignTools.class);
	
	private static final String PRIVATEKEY_BG = SetSystemProperty.getKeyValue("bg.privatekey_file","sysConfig.properties");
	public static final String SIGN_TYPE_SHA1RSA = "SHA1withRSA";
	public static final String SIGN_TYPE_MD5RSA = "MD5withRSA";


	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128; 
	
	private static PrivateKey privateKey;
	
	static{
		System.out.println("============================================================================"+PRIVATEKEY_BG);
		try {
			BufferedReader br = new BufferedReader(new FileReader(PRIVATEKEY_BG));
			String readLine= null;  
		    String privatekeystr="";  
		    while((readLine= br.readLine())!=null){  
				if(readLine.charAt(0)!='-'){  
					privatekeystr += readLine;
				}  
		    }  
		    br.close();
		    byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privatekeystr);  
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
	        privateKey = keyFactory.generatePrivate(keySpec); 
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	/**
	 * 读取私钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws DecoderException
	 * @throws InvalidKeySpecException
	*/ 
	public static PrivateKey getBAPrivateKey(){		
		while(privateKey==null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return privateKey;
	}

	/**
	 * 读取公钥
	 */
	public static PublicKey getPublicKey(String publicKeyStr){
		try{
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (PublicKey) keyFactory.generatePublic(keySpec);	
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}
	/**
	 * 
	 * @param privateKeyStr
	 * @return
	 */
	public static PrivateKey getPrivateKey(String privateKeyStr){		
		try {
			byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
	        return keyFactory.generatePrivate(keySpec); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return null;
	}

	/**
	 * 
	 * @param data
	 * @param privateKey
	 * @param algorithm
	 *            MD5withRSA SHA1withRSA (二维码收单用SHA1withRSA)
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, PrivateKey privateKey,String algorithm){
		try{
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(privateKey);
			signature.update(data);
			byte[] bsign64 = Base64.encodeBase64(signature.sign());
			return new String(bsign64);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}		
		return null;
	}
	public static boolean verify(byte[] data, String sign, PublicKey publicKey,	String algorithm){
		try{
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(Base64.decodeBase64(sign.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	/**
	 * 公钥分段加密
	 * @param data
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt2(byte[] data,PublicKey pk){
		byte[] decryptedData = null;
		try{
			Cipher cipher = Cipher.getInstance("RSA"); 
			//初始化Cipher 公钥加密/私钥加密
			cipher.init(Cipher.ENCRYPT_MODE,pk);
			byte[] b = data; 
	        int inputLen = b.length;  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        int offSet = 0;  
	        byte[] cache;  
	        int i = 0; 
	        // 对数据分段解密  
	        while (inputLen - offSet > 0) {  
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
	                cache = cipher.doFinal(b, offSet, MAX_ENCRYPT_BLOCK);  
	            } else {  
	                cache = cipher.doFinal(b, offSet, inputLen - offSet);  
	            }  
	            out.write(cache, 0, cache.length);  
	            i++;  
	            offSet = i * MAX_ENCRYPT_BLOCK;  
	        }  
	        decryptedData = out.toByteArray();  
	        out.close();  
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}		
		return decryptedData;
	}
	/**
	 * 32位私钥加密做签名
	 * @param data
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,PublicKey pk){
		byte[] encdata = null;
		try{
			Cipher cipher = Cipher.getInstance("RSA"); 
			cipher.init(Cipher.ENCRYPT_MODE,pk);		
			encdata = cipher.doFinal(data);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return encdata;
	}
	/**
	 * 公钥解密验签名
	 * @param data
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,PrivateKey pk){
		byte[] decdata = null;
		try{
			Cipher cipher = Cipher.getInstance("RSA"); 
			cipher.init(Cipher.DECRYPT_MODE,pk);
			decdata=cipher.doFinal(data);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}				
		return decdata;
	}
	/**
	 * 私钥分段解密
	 * @param data
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt2(byte[] data,PrivateKey pk) {
		byte[] decryptedData = null;
		try{
			Cipher cipher = Cipher.getInstance("RSA"); 
			//初始化Cipher 公钥加密/私钥加密
			cipher.init(Cipher.DECRYPT_MODE,pk);
			int inputLen = data.length;  
			ByteArrayOutputStream out = new ByteArrayOutputStream();  
			int offSet = 0;  
			byte[] cache;  
			int i = 0;    
			// 对数据分段解密  
			while (inputLen - offSet > 0) {  
			    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
			        cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);  
			    } else {  
			        cache = cipher.doFinal(data, offSet, inputLen - offSet);  
			    }  
			    out.write(cache, 0, cache.length);  
			    i++;  
			    offSet = i * MAX_DECRYPT_BLOCK;  
			}  
			decryptedData = out.toByteArray();  
			out.close();  
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}		
		return decryptedData;
	}
	/**
	 * md5加密32位
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String md5(String data) throws Exception{
		String result = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        result = buf.toString();
        return result;
	}

	
	public static void main(String[] arg) throws Exception {
		String version = "1.0";
		String customerid="6520";
		String sdorderno="t2018123111152986872";
		String total_fee="0.01";
		String paytype="alipay";
		String notifyurl="http://www.baidu.com";
		String returnurl="http://www.baidu.com";
		String remark="onlinepay";
//		String bankcode="ABC";
		String sign = "";
		String get_code="0";
		String key="dd4078e2c0a743e1b874df1965b12dca5c770d8a";
		String url = "http://www.0898shx.com/gateway";
//		version={value}&customerid={value}&total_fee={value}&sdorderno={value}&notifyurl={value}&returnurl={value}&{apikey}
//		String d = "version="+version+"&customerid="+customerid+"&total_fee="+total_fee+"&sdorderno="+sdorderno+"&notifyurl="+notifyurl+"&returnurl="+returnurl+"&";
		String d = "version="+version+"&customerid="+customerid+"&total_fee="+total_fee+"&sdorderno="+sdorderno+"&notifyurl="+notifyurl+"&returnurl="+returnurl+"&"+key;
		System.out.println(d);
		sign = SignTools.md5(d);
		
		System.out.println("==111111:"+sign);
		
		

		String a = url+"?version="+version+"&customerid="+customerid+"&sdordreno="+sdorderno+"&total_fee="+total_fee+"&paytype="+paytype
				+"&notifyurl="+notifyurl+"&returnurl="+returnurl+"&remark="+remark
//				+"&bankcode="+bankcode
				+"&get_code="+get_code
				+"&sign="+sign;
		System.out.println(a);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("version", version));
		params.add(new BasicNameValuePair("customerid", customerid));
		params.add(new BasicNameValuePair("sdorderno", sdorderno));
		params.add(new BasicNameValuePair("total_fee", total_fee));
		params.add(new BasicNameValuePair("paytype", paytype));
		params.add(new BasicNameValuePair("notifyurl", notifyurl));
		params.add(new BasicNameValuePair("returnurl", returnurl));
		params.add(new BasicNameValuePair("remark", remark));
//		params.add(new BasicNameValuePair("bankcode", bankcode));
		params.add(new BasicNameValuePair("sign", sign));
		params.add(new BasicNameValuePair("get_code", get_code));		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
//		post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");	         
		post.setEntity(entity);
		CloseableHttpResponse response = httpclient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String body = EntityUtils.toString(response.getEntity(), "utf-8");
		    logger.info("post body:"+body);		   
		}else{
			logger.info("123123:::::::::"+entity.toString()+" "+response.getStatusLine().getStatusCode());
		}
		
		
		
		
		
		
		
		
		
//		try{
////			JSONObject jo = JSONObject.parseObject("{data:\"212121212\",    sign:\"fdasfsaffda\"}");
////			String data = "{\"mer_id\":\"20181217001\",\"trans_amt\":\"1.00\",\"good_id\":\"230025\",\"buyer_id\":\"654321\",\"order_date\":\"20181221140202\",\"bg_ret_url\":\"47.98.107.166:8080/hhcms\",\"pay_type\":\"01\",\"order_id\":\"201812211402020838\",\"app_id\":\"123456\",\"is_raw\":\"1\",\"cmd_id\":\"1020\",\"ret_url\":\"47.98.107.166:8080/hhcms\",\"good_desc\":\"刚才那看手机电话放款\",\"terminalIp\":\"192.168.1.101\"}";//jo.toJSONString();
////			System.out.println("原数据:"+data);
////			String pst = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3zJILWQI9ps3ngqwFUlEcaJK70Skq1e4pL7aWIU3ltjO1zUkO5eL7xKf30ybBQ2dq5+jZsmC63hruijcDQ7GD3WBoKq2RQ3J6/+K9jae2cxXyxhDRjCVxFfwrYFM9jJhmqQLF/JtB7S19V//Xhjiw07BxZR9QOA/8Xlqh6m+oewIDAQAB";
////			PublicKey pubkeybg = SignTools.getPublicKey(pst);
////			String ed1 = Base64.encodeBase64String(SignTools.encrypt2(data.getBytes(), pubkeybg));
////			System.out.println("ed1:"+ed1);
//			String ed2 = "S2H3lVxyNZ7s+qjLW77St8N1SOZE3nvkj3O05Qi7NIL0XjDLHcKrSD+hDxjNimjpbW5UFc5s1p91gGmI/CvW3NETQCQPr23g0DXqWCAve0N55IkbTc/QMJxU0QJaDuWMafJQ74hMZcyPphR2PEuwMHt88Ga+BZFX9WvqDOZtOfcwsWGlguSp3NK5YkDnJBuRs7nL0O7nbumbYfYH/GedMRQhJCadED1oyiVojw+nOzeXFCCqerFl4Kww9XQ/phKBGjy54mfu2xJE9ahGfKzwFlKTH+HySgyO9KbpABQNpFpMKe6cUGdY/KYoIT8NIriUzNRuYj+jGoL8biWMmQE6yCC8X+RI/tajcqtUZ630cyxid15/EZtfbk8TTHp7AbKCcbm6bEmwatJ5N/7gdBFUhht3PqL9kqswjirNBkh6Pe1Lj5KlfzAGlSsDGvifCSyUROoGOL/UR9AhBVFcEdDmkuJP8fw1XkumJIhz3vA/4Gg/ZZ7OIrMXh1AHFZM16Z7r";
//
////			String ed = "cKb/msYworGWuCEpMM2LxCketlT3Ql/pDiOGtKIwwD29B0ywqbs tJ7utpG88aWLTO NybsXfW4Dm7dMIoMEnKFta7JQ9rU/HkL9Trd9kfYryIQoNoxNJauszmKti9D8yYL9XVXHq3f cDSdz6LZ5tOZ B6X/etO0Ztf7i8HiWiFYGtSAfZJ8ZR0ie88VUju7QRAfKor/cDAmda5LbTnyJZhuleuPB6YKCnVWYZ1fGY8U26PhiJ4dpbRVW0D0X/rbE3JB8SUHUxfZDOfZq h618xzrq kdCbvM0Xs4ZpNSDHVJ4z3RxiT0fuJr6y9B1YF/cvF hwACRoe7yk2CReFyILlIfMceT4FGvvAgwujnkdPw40ZKX0uK7LkRIFEIulgG2DT7 jw17bsR6NSoWKJLFl9/7p7tWK4UpgMhvXlXg2J9fZzFiyQlQgrtkQokx30y/6oAMQwGSmqTKlEmOz6WeR9h3vRMwuwzaYN6wPcpHFfTpfbQ7WFwbgC8S8Y oDfi8w5CBOmuPcxBSu UarYeFciCmSRbf gnln0ba9SuXh5FSiHa37RjtRJSERmcpVOQoy5dHG9lFTuYB5YRWcsMWNkFMffcUrDI6se6e3F1GgNUpUNV8NlCpVTzLVmOs0QMV32H2kd7Y41 iUQYyjN/fpbprEwu1B6SgNqINzlxE=";
////			System.out.println("ed2:"+ed);
//									
//			String privatekeystrbg="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALfMkgtZAj2mzeeCrAVSURxokrvRKSrV7ikvtpYhTeW2M7XNSQ7l4vvEp/fTJsFDZ2rn6NmyYLreGu6KNwNDsYPdYGgqrZFDcnr/4r2Np7ZzFfLGENGMJXEV/CtgUz2MmGapAsX8m0HtLX1X/9eGOLDTsHFlH1A4D/xeWqHqb6h7AgMBAAECgYEAox5gIsgM8BBAvw0+g76Jk8/PIfbANW8FXfIldln6Wzr364pUI2+socrnU09HHtAmUT+ebM4dgNqrRjbOGgyS1jYPUZW6KAVve4Lq/M7LECye9XYKuNwlT5tWOsCSk9/FenMqvGswb/glHhjRzgaxEvH1FdF6mVPsL2+44lSaKykCQQDl3ZnDp3hpfwVzvPYBn13PGe4us/6jxK3GdVcN+nBuILlptaUs69QdoJ1OTLJlMMtJfk1ymoO+dScDhm0x6cYfAkEAzLIr6jWaTa4JJt5eGmJOnZIsEU/hNEH9QKaM0AcwJkYo5eTz5AL9QUYd5nHfYuOPYb8kGVHiSCKMu1HmzN06JQJANPHIs+cD9hCaueDBLPh/C++mC73Lnf70I6ztQzv233bMHgwHooQjFDvlX56MzH/joubjgc0TITAsr0QsLH3y8wJBAJrnPlpoJBQi3uQeJUJ8IJgXtOeI9pjwUzFomGkY93QPZgXLhFGJfZO29wucIvuXz7qdxjivAbmrA6sB6NIhnE0CQDuh7OHoqkZM+62NfScyNjVtqPFDN4fjZfyLM3tYWy7NKolnC20SmfkBmxm3YKz9Gs2mViA5p1NZgsfu5Il2IjI=";
//			String pubkeyshstr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDULZKde+npQL1w7rusO7WF3hUsVHzUw6r1bfEeOXoNLndRz7AMsgd3owh//Kd7D7i8f2Cm3pluSAu0lzH8c0geDKu+RvzYjA8Hnyc4J9QpUJHmUCCfG1+CVbSvm4qLaxuieezMmyg3njDU7FZebWAjje8luQCnoS+sUJ6B07HnxQIDAQAB";				  
//			PublicKey pubkeysh = SignTools.getPublicKey(pubkeyshstr);
//			
//			byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privatekeystrbg);  
//	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
//	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
//	        PrivateKey privateKey = keyFactory.generatePrivate(keySpec); 
////	        String d1 = new String(SignTools.decrypt2(Base64.decodeBase64(ed1), privateKey));
////	        System.out.println("dd1:"+d1);
//	        String d2 = new String(SignTools.decrypt2(Base64.decodeBase64(ed2), privateKey));
//	        System.out.println("dd2:"+d2);
////	        String sign1= SignTools.sign(data.getBytes(), privateKey, SIGN_TYPE_SHA1RSA);
////	        System.out.println("sign1:"+sign1);
////	        String sign2= SignTools.sign(d2.getBytes(), privateKey, SIGN_TYPE_SHA1RSA);
////	        System.out.println("sign2:"+sign2);
//	        String sign4 = "jwy5y0vvqVpw27pCWa2LxoM0q8linMYle855Rw+sl3ItRSQSHfgyla7DX/Gn2eSwGsNoSHy7S6fpxGwwS3N1UiWkuWbjxyxZTiCrrQkbfp88c1tVeDD2YyluffgbwSaZvn/O+x5lT5qvB3Bs+VsudvSns57Gc7jsDDavAhKg5T4=";
//	        
////	        boolean bool = SignTools.verify(d1.getBytes(), sign1, pubkeybg, SignTools.SIGN_TYPE_SHA1RSA);
////	        System.out.println(bool);
//	        boolean bool2 = SignTools.verify(d2.getBytes(), sign4, pubkeysh, SignTools.SIGN_TYPE_SHA1RSA);
//	        System.out.println(bool2);
//
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
	}
	
}
