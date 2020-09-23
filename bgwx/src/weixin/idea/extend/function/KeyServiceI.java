package weixin.idea.extend.function;

import javax.servlet.http.HttpServletRequest;

import weixin.guanjia.core.entity.message.resp.TextMessageResp;

/**
 * 关键字功能接口
 * @author zhangdaihao
 *
 */
public interface KeyServiceI{

	/**
	 * &#x83b7;&#x53d6;&#x63a5;&#x53e3;&#x5173;&#x952e;&#x5b57;&#xff0c;&#x4f8b;&#x5982;&#xff1a;"&#x7ffb;&#x8bd1;"
	 * @return
	 */
    String getKey();
	
   /**
    * 针对关键字的功能处理方法
    * @param content      请求文本
    * @param textMessage  默认回复此文本消息
    * @param request      请求
    * @return
    */
	String excute(String content,TextMessageResp defaultMessage,HttpServletRequest request);
}
