package com.bg.batch.entity;

import java.util.List;

public class BrokerageEntity {
	/*
	 * 推荐人集合从本人向上  1级，2级，3级
	 */
	private List<BrokerageEntity> recommenders;
	/*
	 * 账户ID
	 */
	private String accountId;
	/*
	 * 分销团队交易额
	 */
	private int teamTranAmt;
	/*
	 * 本人刷卡交易额
	 */
	private int tranAmt;
	/*
	 * 本月返佣达到的比例
	 */
	private double rate;
	public List<BrokerageEntity> getRecommenders() {
		return recommenders;
	}
	public void setRecommenders(List<BrokerageEntity> recommenders) {
		this.recommenders = recommenders;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public int getTeamTranAmt() {
		return teamTranAmt;
	}
	public void setTeamTranAmt(int teamTranAmt) {
		this.teamTranAmt = teamTranAmt;
	}
	public int getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(int tranAmt) {
		this.tranAmt = tranAmt;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
