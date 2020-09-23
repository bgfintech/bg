package com.bg.trans.service;

import com.bg.trans.entity.zyht.BillFindByPageParams;
import com.bg.trans.entity.zyht.CancelTransParams;
import com.bg.trans.entity.zyht.CycleQueryParams;
import com.bg.trans.entity.zyht.GetDynCodeParams;
import com.bg.trans.entity.zyht.PayBackParams;
import com.bg.trans.entity.zyht.QueryTransParams;
import com.bg.trans.entity.zyht.ResponseEntity;
import com.bg.trans.entity.zyht.ScannedParams;

public interface ITransZYHTService {
	/**
	 * 获取token
	 * @return
	 * @throws Exception
	 */
	String getAccessToken() throws Exception;
	/**
	 * 动态码主扫
	 * @param para
	 * @return
	 */
	GetDynCodeParams getDynCode(GetDynCodeParams para) throws Exception;
	/**
	 * 动态码被扫
	 * @param para
	 * @return
	 */
	ScannedParams scanned(ScannedParams para) throws Exception;
	/**
	 * 撤销交易
	 * @param para
	 * @return
	 */
	CancelTransParams cancelTrans(CancelTransParams para) throws Exception;
	/**
	 * 交易退款
	 * @param para
	 * @return
	 */
	PayBackParams payBack(PayBackParams para) throws Exception;
	/**
	 * 轮询查询
	 * @param para
	 * @return
	 */
	CycleQueryParams cycleQuery(CycleQueryParams para) throws Exception;
	/**
	 * 交易查询
	 * @param para
	 * @return
	 */
	QueryTransParams queryTrans(QueryTransParams para) throws Exception;
	/**
	 * 对账
	 * @param para
	 * @return
	 * @throws Exception
	 */
	BillFindByPageParams findByPage(BillFindByPageParams para) throws Exception;
}
