package com.bg.batch.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bg.batch.dao.IBaseBatchDao;
import com.bg.batch.entity.TaskConstants;
import com.bg.util.BaseJDBCDao;


@Repository("baseBatchDao")
public class BaseBatchDaoImpl extends BaseJDBCDao implements IBaseBatchDao {
	private Logger logger = Logger.getLogger(BaseBatchDaoImpl.class);

	@Override
	public String[] findBatchDate() {
		String sSql = "SELECT businessdate,nextbatchexecuteDate FROM batch_system_setup where 1=1";
		logger.debug("[取到交易日期和下次批量日期]:"+sSql);
		Object object = this.getJdbcTemplate().query(sSql, new Object[] {},
				new ResultSetExtractor<Object>() {
					public Object extractData(ResultSet rs)	throws SQLException, DataAccessException {
						if (rs.next()) {
							String[] strs = new String[2];
							strs[0] = rs.getString("BUSINESSDATE");
							strs[1] = rs.getString("NEXTBATCHEXECUTEDATE");
							return strs;
						}
						return null;
					}
				});
		return object == null ? null : (String[]) object;
	}

	@Override
	public String findTaskStatus(String executedate, String taskClassName,
			String taskName) {
		String sSql = "SELECT taskstatus FROM batch_taskstatus WHERE inputdate = '"
				+ executedate
				+ "'"
				+ " and taskclassname='"
				+ taskClassName
				+ "' and taskname='" + taskName + "' ";
		logger.debug("[查询日期为当日或上日的该任务]:"+sSql);

		Object object = this.getJdbcTemplate().query(sSql, new Object[] {},
				new ResultSetExtractor<Object>() {
					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							return rs.getString("TaskStatus");
						}
						return null;
					}
				});
		return object==null?null:(String)object;
	}

	@Override
	public boolean insertTaskStatus(Object[] objs) {
		boolean bool = false;
		String sSql = "INSERT INTO batch_taskstatus (inputdate,taskname,taskdescribe,taskclassname,taskstatus,begintime)"
				+ "VALUES(?,?,?,?,?,?) ";
		logger.debug("[插入执行开始记录]："+sSql);
		
		this.getJdbcTemplate().update(sSql,objs);
		logger.debug("[插入执行开始记录，参数]：nextBatchExecuteDate:"+objs[0]
				+ " taskClassName: "+objs[3]
				+ " TaskStatus: "+TaskConstants.TaskResult.TASK_EXECUTING
				+ " BeginTime: "+objs[5]);
		bool = true;
		return bool;
	}

	@Override
	public boolean finishBatch(Date begintime, String taskClassName,
			String executetime) {
		boolean bool = false;
		String sSql = "update batch_taskstatus set taskstatus=?,endtime=? where taskclassname=? AND inputdate=?";
		logger.debug("[任务单元"+taskClassName+"]执行完成更新状态："+sSql);
		
		Object[] objs = new Object[]{TaskConstants.TaskResult.TASK_SUCCESS,begintime,taskClassName,executetime};
		this.getJdbcTemplate().update(sSql,objs);
		bool = true;
		return bool;
	}

	@Override
	public void updateBatchErrMsg(String message, String taskClassName,String taskName,String nextBatchExecuteDate) {
		String sSql = "update batch_taskstatus set taskstatus=?,errmsg=? where taskclassname=? and taskname=? AND inputdate=?";
		Object[] objs = new Object[]{TaskConstants.TaskResult.TASK_FAILED,message,taskClassName,taskName,nextBatchExecuteDate};
		this.getJdbcTemplate().update(sSql,objs);
		logger.debug("[任务执行失败保存异常信息]："+ sSql);
		logger.debug("[任务执行失败保存异常信息]，参数："
			+ " TaskStatus: "+ TaskConstants.TaskResult.TASK_FAILED
			+ " ErrMsg: "+ message
			+ " TaskClassName: "+ taskClassName
			+ " TaskName: "+ taskName
			+ " InputDate: " + nextBatchExecuteDate);				
	}

	@Override
	public boolean endBatch(String date) {
		String sql = "UPDATE batch_system_setup SET nextbatchexecutedate=?";	// 批量执行完成后， 设置可登录
		this.getJdbcTemplate().update(sql,new Object[]{date});
		return true;
	}

}
