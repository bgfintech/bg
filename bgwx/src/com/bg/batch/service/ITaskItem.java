package com.bg.batch.service;

/**
 * 批量接口执行必须实现接口类
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date        	Author      Changes
 * 2015/06/30   tbzeng      Created
 * 
 */
public interface ITaskItem {

	public String execute(String beanId);

}
