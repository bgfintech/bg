package com.bg.batch.service.impl;

import org.springframework.stereotype.Service;

import com.bg.batch.entity.TaskConstants;
import com.bg.batch.service.AbstractBaseBatch;

/**
 * 开始批量处理
 * @author YH
 * @date 2017年6月1日
 * @version 1.0
 */
@Service("StartBatch")
public class StartBatch extends AbstractBaseBatch {

	@Override
	protected String process() throws Exception {
		logger.info("批量处理StartBatch开始......");
		//目前只记日志
		logger.info("批量处理StartBatch结束......");
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}

}
