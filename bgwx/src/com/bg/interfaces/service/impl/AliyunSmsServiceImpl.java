package com.bg.interfaces.service.impl;

import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bg.interfaces.entity.SendSmsReq;
import com.bg.interfaces.entity.SendSmsRsp;
import com.bg.interfaces.service.ISmsService;

@Service("aliyunSmsService")
public class AliyunSmsServiceImpl implements ISmsService {
	//产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";
    //此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    private static final String accessKeyId = "LTAIIvAiW6U4TSKu";
    private static final String accessKeySecret = "F8zxOx8YbTD4mDz3WQpxEqZkKaJeO3";

	@Override
	public SendSmsRsp sendSms(SendSmsReq req) throws Exception {
		System.out.println("aliyunSendSms:start========================");
		//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(req.getPhone());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(req.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(req.getTemplateId());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        Map<String,String> param = req.getTemplateParam();
        String paramStr = JSONObject.toJSONString(param);  
        request.setTemplateParam(paramStr);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        System.out.println("aliyunSendSms:get========================");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        SendSmsRsp rsp = new SendSmsRsp();
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")){
        	rsp.setRespCode("00");//成功
        	rsp.setOrderId(sendSmsResponse.getRequestId());
        }else{
        	rsp.setRespCode("01");//失败
        	rsp.setMsg(sendSmsResponse.getMessage());
        }
        System.out.println("aliyunSendSms:end========================");
		return rsp;
	}
	
	public static void main(String[] arg){
		System.out.println(String.valueOf((new Random().nextInt(8999) + 1000)));
		
//		ISmsService service = new AliyunSmsServiceImpl();
//		SendSmsReq req = new SendSmsReq();
//		req.setPhone("15631114199");
//		req.setSignName("布广钱包");
//		req.setTemplateId("SMS_143700959");
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("code", "1234");
//		req.setTemplateParam(param);
//		try {
//			SendSmsRsp rsp = service.sendSms(req);
//			System.out.println(rsp.getRespCode()+":"+rsp.getMsg());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}

}
