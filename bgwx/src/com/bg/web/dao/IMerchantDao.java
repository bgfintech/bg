package com.bg.web.dao;

import java.util.List;
import java.util.Map;

public interface IMerchantDao {
    /**
     * 商户列表 
     * @return
     */
	List<Map<String,Object>> getMerchantList();
	 /**
     * 快付通商户列表 
     * @return
     */
//	List<Map<String,Object>> getMerchantKftList();
	/**
	 * 保存快付通商户信息
	 * @param entity
	 * @return
	 */
//	boolean insertBMerchantKft(BMerchantEntity entity);
    /**
     * 检查绑定商户是否存在
     * @param marchantId
     * @return
     */
	boolean checkMerchant(String merchantId);   
	
	Map<String, Object> findMerchantInfoByMerchantId(String merchantId);
	/**
	 * 保存商户绑定表
	 * @param merid
	 * @param accid
	 * @return
	 */
	boolean saveBMerBind(String merid,String accid);
	/**
	 * 查询商户在某渠道的商户号
	 * @param mid
	 * @param channel
	 * @return
	 */
	String findChannelMid(String mid, String channel);
	/**
	 * 
	 * @param mid
	 * @return
	 */
	Map<String, Object> findMerchantChannelInfo(String mid);
	
}
