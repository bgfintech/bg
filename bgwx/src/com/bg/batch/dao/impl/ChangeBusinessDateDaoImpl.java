package com.bg.batch.dao.impl;

import org.springframework.stereotype.Repository;

import com.bg.batch.dao.IChangeBusinessDateDao;
import com.bg.util.BaseJDBCDao;



@Repository("changeBusinessDateDao")
public class ChangeBusinessDateDaoImpl extends BaseJDBCDao implements IChangeBusinessDateDao {
	private static String UPDATE_BATECH_BUSINESSDATE="UPDATE batch_system_setup SET businessdate=?";
	@Override
	public boolean changeBusinessDate(String date) {
		this.getJdbcTemplate().update(UPDATE_BATECH_BUSINESSDATE, new Object[]{date});
		return true;
	}

}
