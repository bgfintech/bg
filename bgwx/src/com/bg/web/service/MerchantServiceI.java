package com.bg.web.service;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.bg.trans.entity.TransactionEntity;
import com.bg.web.entity.BMerBindEntity;
import com.bg.web.entity.MerchantInfo;

import weixin.util.PagerPlugin;


public interface MerchantServiceI {
	/**
	 * 商户列表
	 * @param bMerchant
	 * @param pager
	 * @return
	 */
	List<Map<String,Object>> merList();
	
	/**
	 * 绑定商户
	 * @return
	 */
	void bindMer(BMerBindEntity bMerBindEntity);
	/***
	 * 解绑商户
	 * @param bMerBindEntity
	 * @return
	 */
	void unBindMer(String merid);
	/**
	 * 获取商户流水
	 * @param merid
	 * @return
	 */
	List<TransactionEntity> getTransByMer(String merid);
    /***
     * 获取商户信息
     * @param merid
     * @return
     */
	Map<String, Object> getMerchantInfo(String merid);
	/**
	 * 获取交易详情
	 * @param orderid
	 * @return
	 */
	TransactionEntity getTransactionByOrderId(String orderid);
	/**
	 * 查询商户在某渠道的商户号
	 */
	String findChannelMid(String mid,String channel);
	/**
	 * 
	 * @param mid
	 * @return
	 */
	Map<String, Object> getMerchantChannelInfo(String mid);
	
}
