package com.bg.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder {
	private static ApplicationContext applicationContext;

	public static Object getBean(String key) {
		try {
			if (applicationContext == null) {
				applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return applicationContext.getBean(key);

	}

	public static ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		return applicationContext;
	}

	
}
