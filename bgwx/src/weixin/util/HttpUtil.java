package weixin.util;
//获取用户信息 的url请求  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.ConnectException;  
import java.net.URL;  


import javax.net.ssl.HttpsURLConnection;  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLSocketFactory;  
import javax.net.ssl.TrustManager;  


import net.sf.json.JSONObject;  


import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.StatusLine;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.HttpResponseException;  
import org.apache.http.client.ResponseHandler;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.util.EntityUtils;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  

import weixin.guanjia.core.util.MyX509TrustManager;


/** 
* 服务器端http请求工具类 
*/  
public class HttpUtil {  
 private static Logger log = LoggerFactory.getLogger(HttpUtil.class);  


 /** 
  * utf-8编码 
  */  
 static class Utf8ResponseHandler implements ResponseHandler<String> {  
     public String handleResponse(final HttpResponse response)  
             throws HttpResponseException, IOException {  
         final StatusLine statusLine = response.getStatusLine();  
         final HttpEntity entity = response.getEntity();  
         if (statusLine.getStatusCode() >= 300) {  
             EntityUtils.consume(entity);  
             throw new HttpResponseException(statusLine.getStatusCode(),  
                     statusLine.getReasonPhrase());  
         }  
         return entity == null ? null : EntityUtils  
                 .toString(entity, "UTF-8");  
     }  


 }  


 /** 
  * 获取用户信息发送 --HTTPS请求 
  *  
  * @param requestUrl 
  *            请求地址 
  * @param requestMethod 
  *            请求方式（GET、POST） 
  * @param outputStr 
  *            提交的数据 
  * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
  */  
 public static JSONObject httpsRequest(String requestUrl,  
         String requestMethod, String outputStr) {  
     JSONObject jsonObject = null;  
     try {  
         // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
         TrustManager[] tm = { new MyX509TrustManager() };  
         SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
         sslContext.init(null, tm, new java.security.SecureRandom());  
         // 从上述SSLContext对象中得到SSLSocketFactory对象  
         SSLSocketFactory ssf = sslContext.getSocketFactory();  
         URL url = new URL(requestUrl);  
         HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
         conn.setSSLSocketFactory(ssf);  
         conn.setDoOutput(true);  
         conn.setDoInput(true);  
         conn.setUseCaches(false);  
         // 设置请求方式（GET/POST）  
         conn.setRequestMethod(requestMethod);  
         // 当outputStr不为null时向输出流写数据  
         if (null != outputStr) {  
             OutputStream outputStream = conn.getOutputStream();  
             // 注意编码格式  
             outputStream.write(outputStr.getBytes("UTF-8"));  
             outputStream.close();  
         }  
         // 从输入流读取返回内容  
         InputStream inputStream = conn.getInputStream();  
         InputStreamReader inputStreamReader = new InputStreamReader(  
                 inputStream, "utf-8");  
         BufferedReader bufferedReader = new BufferedReader(  
                 inputStreamReader);  
         String str = null;  
         StringBuffer buffer = new StringBuffer();  
         while ((str = bufferedReader.readLine()) != null) {  
             buffer.append(str);  
         }  
         // 释放资源  
         bufferedReader.close();  
         inputStreamReader.close();  
         inputStream.close();  
         inputStream = null;  
         conn.disconnect();  
         jsonObject = JSONObject.fromObject(buffer.toString());  
     } catch (ConnectException ce) {  
         log.error("连接超时：{}", ce);  
     } catch (Exception e) {  
         log.error("https请求异常：{}", e);  
     }  
     return jsonObject;  
 }  
} 