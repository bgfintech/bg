package com.bg.batch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.batch.dao.IBaseBatchDao;
import com.bg.batch.entity.TaskConstants;
import com.bg.batch.service.AbstractBaseBatch;


/**
 * 批量处理结束
 * @author YH
 * @date 2017年6月1日
 * @version 1.0
 */
@Service("EndBatch")
public class EndBatch extends AbstractBaseBatch {
	@Autowired
	private IBaseBatchDao baseBatchDao;
	@Override
	protected String process() throws Exception {
		logger.info("批量处理EndBatch开始......");
		baseBatchDao.endBatch(this.getDeductDate());
		logger.info("批量处理EndBatch结束......");
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}
	
}
