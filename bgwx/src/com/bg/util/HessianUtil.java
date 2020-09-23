package com.bg.util;


import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

public class HessianUtil {
	private static HessianProxyFactory factory = new HessianProxyFactory(); 
	
	@SuppressWarnings("unchecked")
	public static <T> T create(Class<?> api,String url) throws MalformedURLException{		
		Object obj = factory.create(api, url);
		return (T)obj;
	}
}