package com.bg.batch.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bg.batch.dao.IBaseBatchDao;
import com.bg.batch.entity.TaskConstants;
import com.bg.util.DateUtil;
import com.bg.util.StringUtil;


/**
 * 批量执行基础实现类，供其他批量业务继承
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date        	Author      Changes
 * 2015/06/30   tbzeng      Created
 *
 */
public abstract class AbstractBaseBatch implements ITaskItem {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	private String executeStatus = TaskConstants.TaskResult.TASK_FAILED;
	private String deductDate = null;// 批处理日期 当日
	private String currentMonth;// 当月
	private String currentYear;// 当年
	private String lastDate;// 上一日
	private String lastMonth;// 上月
	private String lastMonthEndDate;
	private String lastYear;// 上年
	private String nextDate;// 下一日
	private String nextMonth;// 下月
	private String nextYear;// 下年
	private String currentYearEnd;// 本年末

	//SYSTEM_SETUP表中的下次批量执行日期。用这个字段去记录批量单元执行日志，以解决换日问题。
	private String nextBatchExecuteDate;
	private int commitCount;
	
	private String executeFlag;
	
	private String taskName;
	private String taskDescribe;
	private String taskClassName;
	
	@Autowired
	private IBaseBatchDao baseBatchDao;
	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		// 1.根据配置取连接，关闭自动提交
		this.commitCount = 2000;		
		// 2.取到交易日期和下次批量日期
		String[] dateStr=baseBatchDao.findBatchDate();
		if(dateStr!=null){
			deductDate = dateStr[0];
			nextBatchExecuteDate = dateStr[1];
		}
		if(deductDate == null || deductDate.length() != 8){
			throw new Exception("系统SYSTEM_SETUP表中没有配置系统交易日期，请检查！");
		}		
		// 3.计算基础日期、年、月等变量
		lastDate = DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_DAY, -1);
		nextDate=DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_DAY, 1);
		currentMonth=deductDate.substring(0, 6);
		lastMonth=DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_MONTH,-1).substring(0, 6);
		lastMonthEndDate=DateUtil.getEndDateOfMonth(lastMonth+"01");
		nextMonth=DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_MONTH,1).substring(0, 6);
		currentYear=deductDate.substring(0, 4);
		lastYear=DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_YEAR, -1).substring(0, 4);
		nextYear=DateUtil.getRelativeDate(deductDate, DateUtil.TERM_UNIT_YEAR, 1).substring(0, 4);
		currentYearEnd=currentYear + "1231";		
		taskName=this.getTaskName();
		taskClassName=this.getClass().getName();
		// 4.判断是否需要继续执行批量（查询日期为当日或上日的该任务，状态为成功返回跳过）
		String ts = baseBatchDao.findTaskStatus(nextBatchExecuteDate, taskClassName, taskName);		
		String status = "";
		boolean flag = false;//是否存在记录
		if (ts!=null) {
			status = ts;
			flag = true;
		}
		if (TaskConstants.TaskResult.TASK_SUCCESS.equals(status) || TaskConstants.TaskResult.TASK_FLAG_SKIP.equals(status)) {
			executeFlag=TaskConstants.TaskResult.TASK_FLAG_SKIP;// 本次批量跳过
			return;
		}
		
		if(TaskConstants.TaskResult.TASK_EXECUTING.equals(status)) {
			throw new Exception("批量单元处于等待中，请批量监控人员处理！");
		}
		// 5.设置输入日期，供备份使用
		String inputDate = System.getProperty("inputDate");
		if (StringUtil.isEmpty(inputDate)) {
			System.setProperty("inputDate", deductDate);
		}
		if(!flag){
//			Calendar calendar = new GregorianCalendar();
//			DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss:S");
//			String beginTime = df.format(calendar.getTime());
			Date beginTime = new Date();
			Object[] pobj = new Object[]{nextBatchExecuteDate,taskName,taskDescribe,taskClassName,TaskConstants.TaskResult.TASK_EXECUTING,beginTime};
			baseBatchDao.insertTaskStatus(pobj);		
		}
		executeFlag=TaskConstants.TaskResult.TASK_UNEXECUTE;// 需要执行	
		// 设置缓存
	}

	/**
	 * 批量执行逻辑。
	 * 
	 * @throws Exception
	 */
	protected abstract String process() throws Exception;
	
	/**
	 * 完成批量，更新状态和结束时间
	 * 
	 * @throws Exception
	 */
	private void finishBatch() throws Exception {
//		Calendar calendar = new GregorianCalendar();
//		DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss:S");
//		String beginTime = df.format(calendar.getTime());
		Date beginTime = new Date();
//		Date executeDate = DateTools.getDate(nextBatchExecuteDate);
		baseBatchDao.finishBatch(beginTime,taskClassName,nextBatchExecuteDate);		
	}
	
	@Override
	public String execute(String beanId) {
		this.taskName = beanId;
			try {
				init();
				logger.info("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务初始化成功！["+ taskClassName );
			} catch (Exception e) {
				logger.error("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务初始化失败！");				
				logger.error(e.getMessage(), e);
//				sendSms("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务初始化失败！");	
				return TaskConstants.TaskResult.TASK_FAILED;
			}
			try {
				if (TaskConstants.TaskResult.TASK_FLAG_SKIP.equals(executeFlag)) {
					return TaskConstants.TaskResult.TASK_FLAG_SKIP;
				}
				
				executeStatus = process();
			} catch (Exception e) {
				logger.error("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务执行失败！");				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				PrintStream ps = new PrintStream(baos);  
				e.printStackTrace(ps);				
				String msg = baos.toString();
				logger.info(msg);
				
				// 保存业务操作失败信息
				updateBatchErrMsg(msg);
//				sendSms("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务执行失败！");				
				return TaskConstants.TaskResult.TASK_FAILED;
			}
			
			try {
				finishBatch();
			} catch (Exception e) {
				logger.error("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务，置完成标志时失败！");
				e.printStackTrace();
//				sendSms("批量[" + taskName + "]["+this.getTaskDescribe()+"]的任务，置完成标志时失败！");	
				return TaskConstants.TaskResult.TASK_FAILED;
			}
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}
	
//	private void sendSms(String content){		
//		try {
//			String url = SetSystemProperty.getKeyValue("tpmsUrl");
//			ICommonHesService commService = HessianUtil.create(ICommonHesService.class,url+"/commonHesService");
//			Map<String, String> m = new HashMap<String,String>();
//			//18738197893  15124979872  15011591430
//			m.put("18738197893", content);
//			m.put("15124979872", content);
//			m.put("15011591430", content);
//			m.put("type", "hy");
//			commService.sendSms(m);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(),e);
//		}		
//	}

	public void updateBatchErrMsg(String message) {
		String str = "";
		if(message.length()>=1000){
			str = message.substring(0,999);
		}else{
			str = message;
		}		
		baseBatchDao.updateBatchErrMsg(str,taskClassName,taskName,nextBatchExecuteDate);		
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescribe() {
		return taskDescribe;
	}

	public void setTaskDescribe(String taskDescribe) {
		this.taskDescribe = taskDescribe;
	}	

	public String getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getDeductDate() {
		return deductDate;
	}

	public void setDeductDate(String deductDate) {
		this.deductDate = deductDate;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}

	public String getLastMonthEndDate() {
		return lastMonthEndDate;
	}

	public void setLastMonthEndDate(String lastMonthEndDate) {
		this.lastMonthEndDate = lastMonthEndDate;
	}

	public String getLastYear() {
		return lastYear;
	}

	public void setLastYear(String lastYear) {
		this.lastYear = lastYear;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public String getNextMonth() {
		return nextMonth;
	}

	public void setNextMonth(String nextMonth) {
		this.nextMonth = nextMonth;
	}

	public String getNextYear() {
		return nextYear;
	}

	public void setNextYear(String nextYear) {
		this.nextYear = nextYear;
	}

	public String getCurrentYearEnd() {
		return currentYearEnd;
	}

	public void setCurrentYearEnd(String currentYearEnd) {
		this.currentYearEnd = currentYearEnd;
	}

	public String getNextBatchExecuteDate() {
		return nextBatchExecuteDate;
	}

	public void setNextBatchExecuteDate(String nextBatchExecuteDate) {
		this.nextBatchExecuteDate = nextBatchExecuteDate;
	}

	public int getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public String getExecuteFlag() {
		return executeFlag;
	}

	public void setExecuteFlag(String executeFlag) {
		this.executeFlag = executeFlag;
	}

	
	public String getTaskClassName() {
		return taskClassName;
	}

	public void setTaskClassName(String taskClassName) {
		this.taskClassName = taskClassName;
	}
}
