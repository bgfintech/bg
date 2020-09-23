package com.bg.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bg.batch.entity.RouteItem;




/**
 * 通过配置路由构建批量执行顺序
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date       	Author      Changes
 * 2015/06/30   tbzeng      Created
 *
 */
public class BuildTaskFromXML {

	private static final String filePath = "task.xml";
	
	@SuppressWarnings("unchecked")
	public List<String> buildTaskItemFromXML(String filePath) throws Exception {
		
		List<String> executeTaskList = new ArrayList<String>(); 
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("task.xml");
		SAXReader saxReader = new SAXReader();
		Document saxDoc = saxReader.read(is);
		
		Element rootEle = saxDoc.getRootElement();
		
		List<Element> routeList = rootEle.selectNodes("//routeTable/route");
		RouteItem routeItem = null;
		
		for (Element routeEle: routeList) {
			String unit = routeEle.attributeValue("unit");
			String executeStatus = routeEle.attributeValue("executeStatus");
			String nextUnit = routeEle.attributeValue("nextUnit");
			if (StringUtil.isEmpty(unit)) {
				throw new RuntimeException("节点"+(StringUtil.isEmpty(unit)?"":"("+unit+")")+"值(unit)不能为空！");
			}
			if (routeItem != null) {
				if (!unit.equals(routeItem.getNextUnit())) {
					throw new RuntimeException("当前节点("+unit+")与上一节点("+routeItem.getNextUnit()+")不一致，请核查！");
				}
			}
			
			routeItem = new RouteItem(unit, executeStatus, nextUnit);
			executeTaskList.add(unit);
			if (StringUtil.isEmpty(nextUnit)) {	// nextUnit 为空， 结束路由配置
				break;
			}
		}
		
		return executeTaskList;
	}
	
	public static List<String> initTaskRoute() {
		
		List<String> beanIdList = null;
		try {
			beanIdList = new BuildTaskFromXML().buildTaskItemFromXML(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return beanIdList;
	}
}
