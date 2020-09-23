package com.bg.batch.entity;


/**
 * 批量运行常量参数定义
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date        	Author      Changes
 * 2015/06/30   tbzeng      Created
 *
 */
public class TaskConstants {

	public static final class TaskResult {
		public static final String TASK_UNEXECUTE = "0";//未执行
		public static final String TASK_SUCCESS = "1";//执行成功
		public static final String TASK_FAILED = "2";//失败
		public static final String TASK_FLAG_SKIP = "3";//跳过
		public static final String TASK_EXECUTING = "4";//暂停
		
	}
}
