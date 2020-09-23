package com.bg.interfaces.entity.internal;

import java.util.List;

public class BankruptDetail {
	private CountDetail count;
	private List<Bankrupt>	cases;
	public CountDetail getCount() {
		return count;
	}
	public void setCount(CountDetail count) {
		this.count = count;
	}
	public List<Bankrupt> getCases() {
		return cases;
	}
	public void setCases(List<Bankrupt> cases) {
		this.cases = cases;
	}
	
}
