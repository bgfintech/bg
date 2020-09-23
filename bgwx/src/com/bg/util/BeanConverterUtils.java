package com.bg.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * javabean转换工具类
 * @author create by liucs on 2017年5月24日 下午2:52:33  
 * @version V1.0
 */
public class BeanConverterUtils {
	/**
	 * javaBean 转 Map
	 * 
	 * @param object
	 *            需要转换的javabean
	 * @return 转换结果map
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> beanToMap(Object object) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Class cls = object.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(object));
		}
		return map;
	}

	/**
	 *
	 * @param map
	 *            需要转换的map
	 * @param cls
	 *            目标javaBean的类对象
	 * @return 目标类object
	 * @throws Exception
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> cls)
			throws Exception {
		T object = cls.newInstance();
		for (String key : map.keySet()) {
			Field temFiels = cls.getDeclaredField(key);
			temFiels.setAccessible(true);
			temFiels.set(object, map.get(key));
		}
		return object;
	}
}
