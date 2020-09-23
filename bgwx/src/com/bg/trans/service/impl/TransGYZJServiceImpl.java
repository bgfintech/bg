package com.bg.trans.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;








import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bg.trans.entity.gyzj.EntoutEntity;
import com.bg.trans.entity.gyzj.EntstatEntity;
import com.bg.trans.service.ITransGYZJService;
import com.bg.util.DateUtil;
import com.bg.util.HttpsUtil;

@Service("transGYZJService")
public class TransGYZJServiceImpl implements ITransGYZJService {
	private static Logger logger = Logger.getLogger(TransGYZJServiceImpl.class);
//	private String uid = "test";
//	private String pwd = "12qw90op";
//	private String entstatPeopleUrl = "https://49.4.92.234/shesu/test/api/entstat/portrait/people";
//	private String entstatOrgUrl = "https://49.4.92.234/shesu/test/api/entstat/portrait/org";
//	private String entoutPeopleUrl = "https://49.4.92.234/shesu/test/api/entout/portrait/people";
//	private String entoutOrgUrl = "https://49.4.92.234/shesu/test/api/entout/portrait/org";
	
	
	private String uid = "bjbgxxfw";
	private String pwd = "bgxx.2s4d74hn";
	private String entstatPeopleUrl = "https://49.4.92.234/shesu/api/entstat/portrait/people";
	private String entstatOrgUrl = "https://49.4.92.234/shesu/api/entstat/portrait/org";
	private String entoutPeopleUrl = "https://49.4.92.234/shesu/api/entout/portrait/people";
	private String entoutOrgUrl = "https://49.4.92.234/shesu/api/entout/portrait/org";
	@Override
	public EntstatEntity getEntstatPeople(String name, String id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String sd = sdf.format(new Date());
		String ed = sd;
		try {
			ed = DateUtil.getRelativeDate(sd, DateUtil.TERM_UNIT_DAY, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonData = "{\"name\": \"" + name + "\",\"id\":\"" + id + "\" ,\"inquired_auth\":\"authed:"+sd+"-"+ed+"\"}";
		System.out.println(jsonData);
		String str = post(entstatPeopleUrl, jsonData);
		EntstatEntity entity = JSONObject.parseObject(str, EntstatEntity.class);
		return entity;
	}
	
	@Override
	public EntstatEntity getEntstatOrg(String name) {
		String jsonData = "{\"name\": \"" + name + "\"}";
		System.out.println(jsonData);
		String str = post(entstatOrgUrl, jsonData);   
		EntstatEntity entity = JSONObject.parseObject(str, EntstatEntity.class);
		return entity;
	}
	
	@Override
	public EntoutEntity getEntoutPeople(String name, String id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String sd = sdf.format(new Date());
		String ed = sd;
		try {
			ed = DateUtil.getRelativeDate(sd, DateUtil.TERM_UNIT_DAY, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonData = "{\"name\": \"" + name + "\",\"id\":\"" + id + "\" ,\"inquired_auth\":\"authed:"+sd+"-"+ed+"\"}";
		System.out.println(jsonData);
		String str = post(entoutPeopleUrl, jsonData);
		EntoutEntity entity = JSONObject.parseObject(str, EntoutEntity.class);
		return entity;
	}

	@Override
	public EntoutEntity getEntoutOrg(String name) {
		String jsonData = "{\"name\": \"" + name + "\"}";
		System.out.println(jsonData);
		String str = post(entoutOrgUrl, jsonData);   
		EntoutEntity entity = JSONObject.parseObject(str, EntoutEntity.class);
		return entity;
	}

	private String post(String httpUrl, String jsonData) {
		String authJson = "{\"uid\": \"" + uid + "\",\"pwd\": \"" + pwd	+ "\",\"expire\": 0}";
		String body = null;
		CloseableHttpClient httpclient = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("query", jsonData));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
			httpclient = HttpsUtil.getHttpClient();
//			httpclient = getHttpClient();
			HttpPost post = new HttpPost(httpUrl);
			// post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			post.setHeader("shesu-auth", authJson);
			post.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				body = EntityUtils.toString(response.getEntity(), "utf-8");
				logger.info(body);
			} else {
				logger.info(httpUrl + " " + entity.toString() + " "	+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return body;
	}
	
	

	
	public static void main(String[] arg) {
		TransGYZJServiceImpl service = new TransGYZJServiceImpl();
//		service.getEntstatPeople("测试自然人A新版本", "123456200101011234");
//		service.getEntstatOrg("测试公司A新版本");
//		service.getEntoutPeople("测试自然人A新版本", "123456200101011234");
//		service.getEntoutOrg("测试公司A新版本");
		String a = "1321312;32121";
		String[] rs = a.split(";");
		System.out.println(rs.length);
		for(String m:rs){
			System.out.println(m);
		}
		System.out.println();
//		System.out.println(a.indexOf(":"));
//		System.out.println(service.transformMoney(75));/
	}

	/** 1.3版本
	@Override
	public String transformMoney(int level) {
		String[] ms={"0","1万以下","1-5万","5-10万","10-30万","30-50万","50-80万","80-100万","100-300万","300-500万","500-800万","800-1000万",
				"1000-3000万","3000-5000万","5000-8000万","8000万-1亿","1-3亿","3-5亿","5-8亿","8-10亿","10亿以上"};		
		return ms[level];
	}
	*/
	
	/**
	 * 2.2版本
	 */
	@Override
	public String transformMoney(int level) {
		String ms = "";
		if(level==0){
			ms = "0";
		}else if(level==1){
			ms = "1万以下";
		}else if(1<level && level<=10){
			int n = (level-1);
			ms = n+"万-"+level+"万";
		}else if(10<level && level<=19){
			int n = (level-10)*10;
			int m = n+10;
			ms = n+"万-"+m+"万";
		}else if(19<level && level<=37){
			int n = 100+(level-20)*50;
			int m = n+50;
			ms = n+"万-"+m+"万";
		}else if(37<level && level<=55){
			int n = 1000+(level-38)*500;
			int m = n+500;
			ms = n+"万-"+m+"万";
		}else if(55<level && level<=73){
			double n = 1+(level-56)*0.5;
			double m = n+0.5;
			ms = n+"亿-"+m+"亿";
		}else if(73<level && level<=91){
			int n = 10+(level-74)*5;
			int m = n+5;
			ms = n+"亿-"+m+"亿";
		}else if(91<level && level<=99){
			int n = 100+(level-92)*50;
			int m = n+50;
			ms = n+"亿-"+m+"亿";
		}else if(level==100){
			ms="500亿以上";
		}else{
			ms = level+"级";
		}	
		return ms;
	}
	

	

}
