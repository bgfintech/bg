package com.bg.web.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.soofa.log4j.Logger;

import weixin.guanjia.core.entity.message.resp.Image;
import weixin.guanjia.core.entity.message.resp.ImageMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;
import weixin.idea.extend.function.KeyServiceI;
/**
 * 使用说明
 * @author YH
 * @date 2018年8月30日
 * @version 1.0
 */
public class UseIntroduceServiceImpl implements KeyServiceI {
	private static Logger log = Logger.getLogger(UseIntroduceServiceImpl.class);
	//{"media_id":"6jrFak90Do2OGWqxnwmd0f3kTHdo47O3Xjhbub6BfQw","url":"http://mmbiz.qpic.cn/mmbiz_jpg/CIiauLrl5kwqGovKCOR82Qxs7h5vjlGQRaibaduExuERf8lDYItT5rZkicCpC8aib7eXMkzmJZl1S0NVDXShGpGIyw/0?wx_fmt=jpeg"}	6jrFak90Do2OGWqxnwmd0f3kTHdo47O3Xjhbub6BfQw
	//private static final String tmpMediaId="6jrFak90Do2OGWqxnwmd0f3kTHdo47O3Xjhbub6BfQw";
	private static final String tmpMediaId="6jrFak90Do2OGWqxnwmd0Uk5VbrgYFz2xCHxlu62mvs";
	
	@Override
	public String getKey() {
		return "使用说明";
	}
	@SuppressWarnings("unchecked")
	@Override
	public String excute(String content, TextMessageResp defaultMessage,HttpServletRequest request) {		
		// xml请求解析
		Map<String, String> requestMap;
		String respMessage=null;
		try {
			requestMap = (Map<String, String>)request.getAttribute("requestMap");
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");			
			//返回的imageMessageResp
			ImageMessageResp  imr = new ImageMessageResp();
			imr.setFromUserName(toUserName);
			imr.setToUserName(fromUserName);
			imr.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_IMAGE);	
			imr.setCreateTime(new Date().getTime());
			Image image = new Image();
			imr.setImage(image);
			image.setMediaId(tmpMediaId);			
			respMessage = MessageUtil.imageMessageToXml(imr);	
			System.out.println("respMessage::"+respMessage);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}		
		return respMessage;
	}

}
