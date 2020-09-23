package com.bg.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据工具类
 * 
 * @author yh
 * @version 1.0
 * @date 2010-9-5 下午10:23:03
 */
public class UtilData {
	private static String domainId = SetSystemProperty.getKeyValue("domainId","sysConfig.properties");
	private static int n = 0;

	public static void resultSetToList(ResultSet rs,List<Map<String, Object>> list) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		// while (rs.next()) {
		HashMap<String, Object> row = new HashMap<String, Object>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			row.put(rsmd.getColumnName(i).toUpperCase(), rs.getObject(i));
		}
		list.add(row);
		// }
	}

	public static <T> void resultSetToObject(ResultSet rs, List<T> list,Class<T> cls) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		T object = cls.newInstance();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			Field temFiels = cls.getDeclaredField(rsmd.getColumnName(i));
			temFiels.setAccessible(true);
			temFiels.set(object, rs.getObject(i));
		}
		list.add(object);
	}

	/**
	 * 根据请求生成系统唯一值
	 * 
	 * @param request
	 * @return
	 */
	public static synchronized String createOnlyData() {
		StringBuffer sb = new StringBuffer();
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sb.append(sdf.format(d)).append(domainId);
		if (n < 10) {
			sb.append("0" + n);
			n++;
		} else {
			sb.append(n);
			n = n == 99 ? 0 : n + 1;
		}
		return sb.toString();
	}
	/**
	 * 加
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal a2 = new BigDecimal(Double.toString(b));
		return a1.add(a2).doubleValue();
	}
	/**
	 * 减
	 */
	public static double subtract(double a, double b) {
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal a2 = new BigDecimal(Double.toString(b));
		return a1.subtract(a2).doubleValue();
	}
	/**
	 * 乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static double multiply(double a, double b) {
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal a2 = new BigDecimal(Double.toString(b));
		return a1.multiply(a2).doubleValue();
	}
	/**
	 * 除
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] arg) {
		for (int i = 0; i < 20; i++) {
			System.out.println(createOnlyData());
		}

	}
}
