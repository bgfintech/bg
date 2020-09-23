package com.bg.trans.entity.gyzj;

import java.util.List;

/**
 * 非诉财产保全审查
 * @author YH
 * @date 2020年4月29日
 * @version 1.0
 */
public class PreservationData {
	private CountDetail count;
	private List<Preservation>	cases;
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
