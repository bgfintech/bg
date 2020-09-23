package weixin.guanjia.core.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.jeecgframework.web.system.service.SystemService;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.core.entity.common.AccessToken;
import weixin.guanjia.core.entity.model.AccessTokenYw;

/**
 * 公众平台通用接口工具类
* 
 * @author liuyq
 * @date 2013-08-09
 */
public class WeixinUtil {
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    //客服接口地址
    public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    //发送模板消息地址
    public static String send_templatemessage_url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    //获取临时二维码
    public static String qrcode_tmp_url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    //通过code换取网页授权access_token
    public static String code2accesstoken_url = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
  //获取用户基本信息
    public static String getuserinfo_url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //jsapi ticket
    public static String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    //上传临时素材
    public static String uploadFile_tmp_url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
  ///上传永久素材
    public static String uploadFile_url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
    
    public static String mini_code2session = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
     
    /**
     * 发起https请求并获取结果
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new MyX509TrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();

                URL url = new URL(requestUrl);
                HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn.setSSLSocketFactory(ssf);

                httpUrlConn.setDoOutput(true);
                httpUrlConn.setDoInput(true);
                httpUrlConn.setUseCaches(false);
                // 设置请求方式（GET/POST）
                httpUrlConn.setRequestMethod(requestMethod);

                if ("GET".equalsIgnoreCase(requestMethod))
                        httpUrlConn.connect();

                // 当有数据需要提交时
                if (null != outputStr) {
                        OutputStream outputStream = httpUrlConn.getOutputStream();
                        // 注意编码格式，防止中文乱码
                        outputStream.write(outputStr.getBytes("UTF-8"));
                        outputStream.close();
                }

                // 将返回的输入流转换成字符串
                InputStream inputStream = httpUrlConn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                inputStream.close();
                inputStream = null;
                httpUrlConn.disconnect();
                jsonObject = JSONObject.fromObject(buffer.toString());
                //jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
        	org.jeecgframework.core.util.LogUtil.info("Weixin server connection timed out.");
        } catch (Exception e) {
        	org.jeecgframework.core.util.LogUtil.info("https request error:{}"+e.getMessage());
        }
        return jsonObject;
    }
    
    public static String getApiTicket(SystemService systemService,String appid,String appsecret,WeixinAccountEntity weixinAount){
    	/**---------------------判断jsapi_ticket是否过期,过期重新请求并保存本地----------------------------------**/       
        java.util.Date end = new java.util.Date();
        java.util.Date start = weixinAount.getApiticketttime();
        String ticket = weixinAount.getApiticket();
        String jsapi_ticket = null;
        if(ticket != null && !"".equals(ticket) && start != null&& ((end.getTime() - start.getTime()) < (7199 * 1000))){
        	jsapi_ticket = ticket;
        }else{
        	AccessToken accessToken = WeixinUtil.getAccessToken(systemService, appid, appsecret);
            // 获取jsapi_ticket
            JSONObject jsonObject = WeixinUtil.httpRequest(WeixinUtil.jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken.getToken()), "GET", null);        
            jsapi_ticket = jsonObject.getString("ticket");
            weixinAount.setApiticket(jsapi_ticket);
            weixinAount.setApiticketttime(new Date());
            systemService.saveOrUpdate(weixinAount);      
        }
        return ticket;
    }
    
    /**
     * 获取access_token
     * @param appid 凭证
    * @param appsecret 密钥
    * @return
     */
    public static AccessToken getAccessToken(SystemService systemService,String appid,String appsecret) {
    	// 第三方用户唯一凭证
//        String appid = bundle.getString("appId");
//        // 第三方用户唯一凭证密钥
//        String appsecret = bundle.getString("appSecret");
        
    	AccessTokenYw accessTocken = getRealAccessToken(systemService);
    	
    	if(accessTocken!=null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		java.util.Date end = new java.util.Date();
    		java.util.Date start = new java.util.Date(accessTocken.getAddTime().getTime());
        	if(end.getTime()-accessTocken.getAddTime().getTime()>accessTocken.getExpires_in()*1000){
        		 AccessToken accessToken = null;
                 String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
                 JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
                 // 如果请求成功
                 if (null != jsonObject) {
                     try {
                         accessToken = new AccessToken();
                         accessToken.setToken(jsonObject.getString("access_token"));
                         accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                         //凭证过期更新凭证
                         AccessTokenYw atyw = new AccessTokenYw();
                         atyw.setId(accessTocken.getId());
                         atyw.setExpires_in(jsonObject.getInt("expires_in"));
                         atyw.setAccess_token(jsonObject.getString("access_token"));
                         updateAccessToken(atyw,systemService);
                     } catch (Exception e) {
                         accessToken = null;
                         // 获取token失败
                         String wrongMessage = "获取token失败 errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+jsonObject.getString("errmsg");
                         org.jeecgframework.core.util.LogUtil.info(wrongMessage);
                     }
                 }
                 return accessToken;
        	}else{
        		
        		 AccessToken  accessToken = new AccessToken();
                 accessToken.setToken(accessTocken.getAccess_token());
                 accessToken.setExpiresIn(accessTocken.getExpires_in());
        		return accessToken;
        	}
    	}else{
    		
    		 AccessToken accessToken = null;
             String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
             JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
             // 如果请求成功
             if (null != jsonObject) {
                 try {
                     accessToken = new AccessToken();
                     accessToken.setToken(jsonObject.getString("access_token"));
                     accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                     
                     AccessTokenYw atyw = new AccessTokenYw();
                     atyw.setExpires_in(jsonObject.getInt("expires_in"));
                     atyw.setAccess_token(jsonObject.getString("access_token"));
                     saveAccessToken(atyw,systemService);
                     
                 } catch (Exception e) {
                     accessToken = null;
                     // 获取token失败
                     String wrongMessage = "获取token失败 errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+jsonObject.getString("errmsg");
                     org.jeecgframework.core.util.LogUtil.info(wrongMessage);
                 }
             }
             return accessToken;
    	}
    }
    public static JSONObject uploadFile(SystemService systemService,byte[] file,int filelength,String filename,String type,String appId,String appsecret) {
    	
    	AccessToken token=getAccessToken(systemService,appId,appsecret);   
    	String requestUrl=uploadFile_url.replace("ACCESS_TOKEN", token.getToken()).replace("TYPE", type);
    	System.out.println("requestUrl::"+requestUrl);
    	String requestMethod="POST";    	
    	byte[] buffer = file;
//        try {
//        	FileInputStream fis = new FileInputStream(file);  
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024*1024*2];  
//            int n;  
//            while ((n = fis.read(b)) != -1) {  
//                bos.write(b, 0, n);  
//            }
//			fis.close();
//			bos.close();  			
//	        buffer = bos.toByteArray(); 
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}     	
        JSONObject jsonObject = null;
        StringBuffer bufferStr = new StringBuffer();
        try {
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new MyX509TrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                URL url = new URL(requestUrl);
                HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn.setSSLSocketFactory(ssf);
                httpUrlConn.setDoOutput(true);
                httpUrlConn.setDoInput(true);
                httpUrlConn.setUseCaches(false);               
                httpUrlConn.setRequestProperty("Connection", "Keep-Alive");                 
                httpUrlConn.setRequestProperty("Charset", "UTF-8");                 
                // 设置边界                    
                String BOUNDARY = "----------" + System.currentTimeMillis();                  
                httpUrlConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);                 
                StringBuilder sb = new StringBuilder();                  
                sb.append("--"); // 必须多两道线
                sb.append(BOUNDARY);                    
                sb.append("\r\n");                    
                sb.append("Content-Disposition: form-data;name=\"media\";filelength=\""+filelength+"\";filename=\""+ filename + "\"\r\n");                   
                sb.append("Content-Type:application/octet-stream\r\n\r\n");                    
                byte[] head = sb.toString().getBytes("utf-8");                  
                // 设置请求方式（GET/POST）
                httpUrlConn.setRequestMethod(requestMethod);
                if (null != buffer) {
					OutputStream outputStream = httpUrlConn.getOutputStream();
					outputStream.write(head);  
					// 注意编码格式，防止中文乱码
					outputStream.write(buffer);                        
					byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
					outputStream.write(foot);
					outputStream.close();
                }
                // 将返回的输入流转换成字符串
                InputStream inputStream = httpUrlConn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                	bufferStr.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                inputStream.close();
                inputStream = null;
                httpUrlConn.disconnect();
                jsonObject = JSONObject.fromObject(bufferStr.toString());
        } catch (ConnectException ce) {
        	ce.printStackTrace();
        	org.jeecgframework.core.util.LogUtil.info("Weixin server connection timed out.");
        } catch (Exception e) {
        	e.printStackTrace();
        	org.jeecgframework.core.util.LogUtil.info("https request error:{}"+e.getMessage());
        }
        return jsonObject;
    }
  
    /**
     * 从数据库中读取凭证
     * @return
     */
    public static AccessTokenYw getRealAccessToken(SystemService systemService){
        List<AccessTokenYw> accessTockenList = systemService.findByQueryString("from AccessTokenYw");
 		return accessTockenList.get(0);
    }
    
    /**
     * 保存凭证
     * @return
     */
    public static void saveAccessToken( AccessTokenYw accessTocken,SystemService systemService){
    	systemService.save(accessTocken);
    }
    
    /**
     * 更新凭证
     * @return
     */
    public static void updateAccessToken( AccessTokenYw accessTocken,SystemService systemService){
    	String sql = "update weixin_accesstoken set access_token='"+accessTocken.getAccess_token()+"',expires_ib="+accessTocken.getExpires_in()+",addtime=now() where id='"+accessTocken.getId()+"'";
    	systemService.updateBySqlString(sql);
    }
    
  
    /** 
     * 编码 
     * @param bstr 
     * @return String 
     */  
    public static String encode(byte[] bstr){  
    	return new sun.misc.BASE64Encoder().encode(bstr);  
    }  
  
    /** 
     * 解码 
     * @param str 
     * @return string 
     */  
    public static byte[] decode(String str){ 
    	
	    byte[] bt = null;  
	    try {  
	        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
	        bt = decoder.decodeBuffer( str );  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
        return bt;  
        
    }  
    
}