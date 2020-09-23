package com.bg.batch.service;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bg.batch.entity.TaskConstants;
import com.bg.util.BuildTaskFromXML;
import com.bg.util.SpringContextUtil;

/**
 * 批量运行操作类
 *
 */
@Component
public class RunTask {
	private static Logger log = Logger.getLogger(RunTask.class);
	
	public static void main(String[] args) {
		
		List<String> beansIds = BuildTaskFromXML.initTaskRoute();
		for (String beanId: beansIds) {
			ITaskItem taskItem = SpringContextUtil.getBean(beanId,ITaskItem.class);
			String executeStatus = taskItem.execute(beanId);
			System.out.println("批量节点："+beanId+"  execute status: " + executeStatus);
			
			if (TaskConstants.TaskResult.TASK_FAILED.equals(executeStatus)) {
				break;
			}
			if (TaskConstants.TaskResult.TASK_FLAG_SKIP.equals(executeStatus)) {
				log.info("单元【"+beanId+"】已成功执行过，此次批量跳过！！！！！！！！！！！！！！！！！！");
				continue;
			  }
		}
	}
	/**
	 * 每日批量
	 */
	//@Scheduled(cron="0 0 0 * * ?")
	public void doTimer(){
		System.out.println("time time time ========================="+new Date());
		List<String> beansIds = BuildTaskFromXML.initTaskRoute();
		for (String beanId: beansIds) {
			ITaskItem taskItem = SpringContextUtil.getBean(beanId,ITaskItem.class);
			String executeStatus = taskItem.execute(beanId);
			System.out.println("批量节点："+beanId+"  execute status: " + executeStatus);
			
			if (TaskConstants.TaskResult.TASK_FAILED.equals(executeStatus)) {
				break;
			}
			if (TaskConstants.TaskResult.TASK_FLAG_SKIP.equals(executeStatus)) {
				log.info("单元【"+beanId+"】已成功执行过，此次批量跳过！！！！！！！！！！！！！！！！！！");
				continue;
			  }
		}		
	}
}
