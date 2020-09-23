package com.bg.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BaseJDBCDao extends JdbcDaoSupport {
	@Resource(name = "dataSource")
    public void setSuperDataSource(DataSource dataSource) {
       super.setDataSource(dataSource);
    }

	public List<Map<String, Object>> findNoralRepayLoanList(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
}