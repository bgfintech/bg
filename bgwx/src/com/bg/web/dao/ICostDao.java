package com.bg.web.dao;

import java.util.List;
import java.util.Map;

public interface ICostDao {
	
	int countCostList(String account);
	
	List<Map<String,Object>> queryCostList(String account,int offset,int limit);
	
}
