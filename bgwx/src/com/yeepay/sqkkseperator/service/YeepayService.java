package com.yeepay.sqkkseperator.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.sqkkseperator.config.Config;







public class YeepayService {
	
	static String merchantno=		Config.getInstance().getValue("merchantno");
	static String appKey= 			Config.getInstance().getValue("appKey");
	static String publickey= 			Config.getInstance().getValue("publickey");
	static String privatekey= 			Config.getInstance().getValue("privatekey");
	
	
	 public static Map<String,String> yeepayYOP(Map<String,String> map,String Uri) throws IOException{

          	YopRequest yoprequest  =  new YopRequest("SQKK"+merchantno,privatekey);
        	Map<String,String> result  =  new HashMap<String,String>();
        
        Set<Entry<String,String> > entry		=	map.entrySet();
        	for(Entry<String,String> s:entry){
        		yoprequest.addParam(s.getKey(), s.getValue());
        	}
        	System.out.println("yoprequest:"+yoprequest.getParams());
    
       	//向YOP发请求
        	YopResponse yopresponse = YopClient3.postRsa(Uri, yoprequest);
        	System.out.println("请求YOP之后结果："+yopresponse.toString());
        	System.out.println("请求YOP之后结果："+yopresponse.getStringResult());
	
        	
        	
//        	对结果进行处理
        		if("FAILURE".equals(yopresponse.getState())){
        			if(yopresponse.getError() != null)
        			result.put("errorcode",yopresponse.getError().getCode());
        			result.put("errormsg",yopresponse.getError().getMessage());
        			System.out.println("错误明细："+yopresponse.getError());
        			System.out.println("系统处理异常结果："+result);
       			return result;
        		}
        		//成功则进行相关处理
        		if (yopresponse.getStringResult() != null) {
        			result = parseResponse(yopresponse.getStringResult());
        		
       			System.out.println("yopresponse.getStringResult: "+result);

       		}
        		
      		return result;
        	}
        
        

        	//将获取到的yopresponse转换成json格式
        	public static Map<String, String> parseResponse(String yopresponse){
        		
        		Map<String,String> jsonMap  = new HashMap<>();
        		jsonMap	= JSON.parseObject(yopresponse, 
        				new TypeReference<TreeMap<String,String>>() {});
        		System.out.println("将结果yopresponse转化为map格式之后: "+jsonMap);
        		return jsonMap;
        	}
       
        }
        

