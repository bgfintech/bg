package com.bg.batch.dao;

import java.util.List;
import java.util.Map;

public interface IShareGBPRateProcessDao {
	List<Map<String,Object>> findNoShareAccount(String date);
	/**
	 * 批量更新GBP费率
	 * @param params
	 */
	void updateBatchAccountGbpRate(List<Object[]> params);
}
