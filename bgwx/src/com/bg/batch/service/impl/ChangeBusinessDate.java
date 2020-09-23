package com.bg.batch.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.batch.dao.IChangeBusinessDateDao;
import com.bg.batch.entity.TaskConstants;
import com.bg.batch.service.AbstractBaseBatch;



/**
 * 换日
 * @author YH
 * @date 2017年6月1日
 * @version 1.0
 */
@Service("ChangeBusinessDate")
public class ChangeBusinessDate extends AbstractBaseBatch {
	@Autowired
	private IChangeBusinessDateDao changeBusinessDateDao;
	@Override
	protected String process() throws Exception {
		logger.info("批量处理日切开始......");
		changeBusinessDateDao.changeBusinessDate(this.getNextDate());
		logger.info("批量处理日切结束......");
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}
	
}
