package com.bg.interfaces.entity.internal;

import java.util.List;

public class CaseDetail {
	private CountDetail count;
	private List<Case> cases;
	public CountDetail getCount() {
		return count;
	}
	public void setCount(CountDetail count) {
		this.count = count;
	}
	public List<Case> getCases() {
		return cases;
	}
	public void setCases(List<Case> cases) {
		this.cases = cases;
	}
	
	
}
