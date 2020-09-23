package com.bg.batch.dao;

import java.util.Date;


public interface IBaseBatchDao {
	boolean endBatch(String date);
	String[] findBatchDate();
	String findTaskStatus(String executdate,String taskClassName,String taskName);
	boolean insertTaskStatus(Object[] objs);
	boolean finishBatch(Date begintime,String taskClassName,String executetime);
	void updateBatchErrMsg(String message,String taskClassName, String taskName,String nextBatchExecuteDate);
}
