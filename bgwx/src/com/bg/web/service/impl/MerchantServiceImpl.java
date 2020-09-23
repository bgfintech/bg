package com.bg.web.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bg.trans.entity.TransactionEntity;
import com.bg.web.dao.IMerchantDao;
import com.bg.web.entity.BMerBindEntity;
import com.bg.web.service.MerchantServiceI;

@Service("merService")
@Transactional
public class MerchantServiceImpl extends CommonServiceImpl implements MerchantServiceI{
	@Autowired
	private IMerchantDao merchantDao;
	

	@Override
	public List<Map<String,Object>> merList() {
		return merchantDao.getMerchantList();		
	}

	

	@Override
	public void bindMer(BMerBindEntity bMerBindEntity) {
		merchantDao.saveBMerBind(bMerBindEntity.getMerId(), bMerBindEntity.getAcctId());
	}

	@Override
	public List<TransactionEntity> getTransByMer(String merid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void unBindMer(String merid) {
        String sql="delete from b_mer_bind where mer_id=?";
        this.executeSql(sql, merid);
	}
	@Override
	public TransactionEntity getTransactionByOrderId(String orderid) {
		List<TransactionEntity> rs =this.findByProperty(TransactionEntity.class, "orderno", orderid);
		if(rs!=null&&rs.size()>0){
			return rs.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> getMerchantInfo(String merid) {
		return merchantDao.findMerchantInfoByMerchantId(merid);
	}
	
	@Override
	public String findChannelMid(String mid, String channel) {
		return merchantDao.findChannelMid(mid,channel);
	}



	@Override
	public Map<String, Object> getMerchantChannelInfo(String mid) {
		return merchantDao.findMerchantChannelInfo(mid);
	}

	

}
