package com.bg.batch.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bg.batch.dao.IShareGBPRateProcessDao;
import com.bg.batch.entity.TaskConstants;
import com.bg.batch.service.AbstractBaseBatch;
import com.bg.util.UtilData;

@Service("ShareGBPRateProcess")
public class ShareGBPRateProcess extends AbstractBaseBatch {
	@Autowired
	private IShareGBPRateProcessDao shareGBPRateProcessDao;

	@Override
	protected String process() throws Exception {
		logger.info("批量处理ShareGBPRateProcess开始......");
		String date = this.getDeductDate();
		List<Map<String, Object>> list = shareGBPRateProcessDao.findNoShareAccount(date);
		List<Object[]> params = new ArrayList<Object[]>();
		for (Map<String, Object> m : list) {
			String account = (String) m.get("ACCOUNT");
			Object ob = m.get("RATE");
			if (ob != null) {
				double rate = (double) ob;
				if (rate != 0.0065) {
					double tmp = 0.0001;
					if (rate <= 0.0055) {
						tmp = 0.0003;
					} else if (rate <= 0.0058) {
						tmp = 0.0002;
					}
					double r = UtilData.add(rate, tmp);
					if (r > 0.0065) {
						r = 0.0065;
					}
					Object[] obj = new Object[] { r, account };
					params.add(obj);
				}
			}
		}
		shareGBPRateProcessDao.updateBatchAccountGbpRate(params);

		logger.info("批量处理ShareGBPRateProcess结束......");
		return TaskConstants.TaskResult.TASK_SUCCESS;
	}

}
