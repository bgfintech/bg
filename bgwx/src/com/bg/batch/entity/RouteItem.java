package com.bg.batch.entity;

/**
 * 路由节点信息
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date        	Author      Changes
 * 2015/06/30   tbzeng      Created
 *
 */
public class RouteItem { 
		
		private String unit;
		private String executeStatus;
		private String nextUnit;
		
		public RouteItem(String unit, String executeStatus, String nextUnit) {
			this.unit = unit;
			this.executeStatus = executeStatus;
			this.nextUnit = nextUnit;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public String getExecuteStatus() {
			return executeStatus;
		}

		public void setExecuteStatus(String executeStatus) {
			this.executeStatus = executeStatus;
		}

		public String getNextUnit() {
			return nextUnit;
		}

		public void setNextUnit(String nextUnit) {
			this.nextUnit = nextUnit;
		}
		
		@Override
		public String toString() {
			return "unit:"+unit + ",executeStatus:"+executeStatus+",nextUnit:"+nextUnit;
		}
		
	}