package com.bg.interfaces.entity.internal;

import java.util.List;

public class PreservationDetail {
	private CountDetail count;
	private List<Preservation> cases;
	public CountDetail getCount() {
		return count;
	}
	public void setCount(CountDetail count) {
		this.count = count;
	}
	public List<Preservation> getCases() {
		return cases;
	}
	public void setCases(List<Preservation> cases) {
		this.cases = cases;
	}
	
}
