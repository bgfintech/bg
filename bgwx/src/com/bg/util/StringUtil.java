package com.bg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * 字符串操作类
 * Copyright 2010 Bona Co., Ltd.
 * All right reserved.
 * 
 * Date        	Author      Changes
 * 2015/06/30   tbzeng      Created
 *
 */
public class StringUtil {
	public static Date parseDate(String str) {
		if (str == null)
			return null;
		Date d = null;
		String format = null;
		char[] split = { '/', '-', '.' };

		if (str.length() < 8)
			return d;
		if (str.length() > 10)
			str = str.substring(0, 10);
		for (int i = 0; i < split.length; ++i) {
			int k = str.indexOf(split[i]);
			if (k < 0)
				continue;
			if (k == 4) {
				format = "yyyy" + split[i] + 'M' + split[i] + 'd';
				break;
			}
			if (k < 4) {
				format = ('M' + split[i] + 100 + split[i]) + "yyyy";
				break;
			}
			return null;
		}

		if (format == null) {
			if (str.substring(0, 2).compareTo("12") > 0)
				format = "yyyyMMdd";
			else {
				format = "MMddyyyy";
			}
		}

		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			d = df.parse(str);
		} catch (ParseException e) {
			d = null;
		}
		return d;
	}

	public static boolean parseBoolean(String str) {
		return (str != null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] parseArray(String str) {
		if (str == null)
			return null;
		ArrayList l = new ArrayList();
		int sc = 0;
		int ec = 0;
		int p = 0;
		int sp = -1;
		while (p < str.length()) {
			if (str.charAt(p) == '{') {
				++sc;
				if (sc == 1)
					sp = p + 1;
			} else if (str.charAt(p) == '}') {
				++ec;
				if (ec == sc) {
					l.add(str.substring(sp, p));

					sp = -1;
					sc = 0;
					ec = 0;
				}
			}
			++p;
		}
		return ((String[]) l.toArray(new String[0]));
	}

	public static Properties parseProperties(String str) {
		Properties p = new Properties();
		String[] s = parseArray(str);
		if ((s != null) && (s.length > 0))
			for (int i = 0; i < s.length; ++i) {
				int p0 = s[i].indexOf(61);
				if (p0 < 1)
					continue;
				if (p0 != s[i].length()) {
					String n = trimAll(s[i].substring(0, p0));
					if (n != "") {
						String v = s[i].substring(p0 + 1);
						p.setProperty(n, v);
					}
				}
			}
		return p;
	}

	public static String bytesToHexString(byte[] bytes, boolean toUpperCase) {
		if (bytes == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			int t = bytes[i];
			sb.append(Integer.toString(t >> 4 & 0xF, 16)).append(
					Integer.toString(t & 0xF, 16));
		}

		String ret = sb.toString();
		return ((toUpperCase) ? ret.toUpperCase() : ret.toLowerCase());
	}

	public static String trimStart(String str) {
		String s = null;
		int p = 0;
		if (str != null) {
			for (p = 0; p < str.length(); ++p) {
				if (!(Character.isSpaceChar(str.charAt(p)))) {
					break;
				}
			}
			s = (p < str.length()) ? str.substring(p) : "";
		}
		return s;
	}

	public static String trimEnd(String str) {
		String s = null;
		int p = 0;
		if (str != null) {
			for (p = str.length() - 1; p >= 0; --p) {
				if (!(Character.isSpaceChar(str.charAt(p)))) {
					break;
				}
			}
			s = (p >= 0) ? str.substring(0, p + 1) : "";
		}
		return s;
	}

	public static String trimAll(String str) {
		String s = null;
		if (str != null) {
			s = trimEnd(trimStart(str));
		}
		return s;
	}

	public static boolean isEmpty(String str) {
		return ((str == null) || (str.equals("")));
	}

	public static boolean isSpace(String str) {
		if (str == null)
			return true;
		boolean r = true;
		for (int i = 0; i < str.length(); ++i) {
			if (!(Character.isSpaceChar(str.charAt(i)))) {
				r = false;
				break;
			}
		}
		return r;
	}

}