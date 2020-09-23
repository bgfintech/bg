package com.bg.trans.service;

import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.hftx.eloan.Trans101Req;
import com.bg.trans.entity.hftx.eloan.Trans101Rsp;
import com.bg.trans.entity.hftx.eloan.Trans102Req;
import com.bg.trans.entity.hftx.eloan.Trans102Rsp;
import com.bg.trans.entity.hftx.eloan.Trans103Req;
import com.bg.trans.entity.hftx.eloan.Trans103Rsp;
import com.bg.trans.entity.hftx.eloan.Trans201Req;
import com.bg.trans.entity.hftx.eloan.Trans201Rsp;
import com.bg.trans.entity.hftx.eloan.Trans202Req;
import com.bg.trans.entity.hftx.eloan.Trans202Rsp;
import com.bg.trans.entity.hftx.eloan.Trans204Req;
import com.bg.trans.entity.hftx.eloan.Trans204Rsp;
import com.bg.trans.entity.hftx.eloan.Trans301Req;
import com.bg.trans.entity.hftx.eloan.Trans301Rsp;
import com.bg.trans.entity.hftx.eloan.Trans302Req;
import com.bg.trans.entity.hftx.eloan.Trans302Rsp;
import com.bg.trans.entity.hftx.eloan.Trans303Req;
import com.bg.trans.entity.hftx.eloan.Trans303Rsp;
import com.bg.trans.entity.hftx.eloan.Trans304Req;
import com.bg.trans.entity.hftx.eloan.Trans304Rsp;
import com.bg.trans.entity.hftx.eloan.Trans305Req;
import com.bg.trans.entity.hftx.eloan.Trans305Rsp;
import com.bg.trans.entity.hftx.eloan.Trans306Req;
import com.bg.trans.entity.hftx.eloan.Trans306Rsp;
import com.bg.trans.entity.hftx.eloan.Trans307Req;
import com.bg.trans.entity.hftx.eloan.Trans307Rsp;
import com.bg.trans.entity.hftx.eloan.Trans308Req;
import com.bg.trans.entity.hftx.eloan.Trans308Rsp;
/**
 * 消金版接口
 * @author YH
 * @date 2019年4月22日
 * @version 1.0
 */
public interface ITransHFTXEloanService {
	/**
	 * 个人开户绑卡
	 * @param req
	 * @param entity
	 * @return
	 */
	Trans101Rsp trans101(Trans101Req req,String appkey,TransactionEntity entity);
	/**
	 * 代扣签约绑卡
	 * @param req
	 * @param appkey
	 * @param entity
	 * @return
	 */
	Trans102Rsp trans102(Trans102Req req,String appkey,TransactionEntity entity);
	
	/**
	 * 放款
	 * @param req
	 * @param appkey
	 * @param entity
	 * @return
	 */
	Trans201Rsp trans201(Trans201Req req,String appkey,TransactionEntity entity);
	
	/**
	 * 还款
	 * @param req
	 * @param appkey
	 * @param entity
	 * @return
	 */
	Trans202Rsp trans202(Trans202Req req,String appkey,TransactionEntity entity);
	
	
	/**
	 * 放款取现
	 * @param req
	 * @param appkey
	 * @param entity
	 * @return
	 */
	Trans204Rsp trans204(Trans204Req req,String appkey,TransactionEntity entity);
	
	/**
	 * 代扣卡解绑
	 * @param req
	 * @param appkey
	 * @param entity
	 * @return
	 */
	Trans103Rsp trans103(Trans103Req req,String appkey,TransactionEntity entity);
	
	/**
	 * 查询委托人余额
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans308Rsp trans308(Trans308Req req,String appkey);
	/**
	 * 卡BIN查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans305Rsp trans305(Trans305Req req,String appkey);
	/**
	 * 绑卡信息查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans306Rsp trans306(Trans306Req req,String appkey);
	/**
	 * 查询个人开户绑卡
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans301Rsp trans301(Trans301Req req,String appkey);
	
	/**
	 * 放款查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans302Rsp trans302(Trans302Req req,String appkey);
	
	/**
	 * 放款查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans303Rsp trans303(Trans303Req req,String appkey);
	/**
	 * 还款查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans304Rsp trans304(Trans304Req req,String appkey);
	/**
	 * 放款取现查询
	 * @param req
	 * @param appkey
	 * @return
	 */
	Trans307Rsp trans307(Trans307Req req,String appkey);
	
	/**
	 * 签名
	 * @param plaintext
	 * @param appKey
	 * @return
	 */
	String sign(String plaintext, String appKey);
}
