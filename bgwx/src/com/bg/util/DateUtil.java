package com.bg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 批量日期处理类 Copyright 2010 Bona Co., Ltd. All right reserved.
 * 
 * Date Author Changes 2015/06/30 tbzeng Created
 * 
 */
public class DateUtil {

	public static final String TERM_UNIT_DAY = "010";// 期限天
	public static final String TERM_UNIT_MONTH = "020";// 期限月
	public static final String TERM_UNIT_YEAR = "030";// 期限年

	/**
	 * 获得和给定日期sDate相差Days天的日期yyyyMMdd
	 */
	public static String getRelativeDate(String date, String termUnit, int step)
			throws Exception {
		return DateUtil.getRelativeDate(date, date, termUnit, step);
	}

	/**
	 * 获得和给定日期sDate相差Days天的日期yyyyMMdd
	 */
	public static String getRelativeDate(String baseDate, boolean endOfMonth,
			String date, String termUnit, int step) throws Exception {
		if (step == 0)
			return date;
		if (termUnit == null)
			throw new Exception("未传入期限单位不正确！");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(date));
		if (termUnit.equals(DateUtil.TERM_UNIT_DAY)) {
			cal.add(Calendar.DATE, step);
		} else if (termUnit.equals(DateUtil.TERM_UNIT_MONTH)) {

			cal.add(Calendar.MONTH, step);
			/********
			 * ygwang: 修正超过28号时的情况
			 */
			int baseday = Integer.parseInt(baseDate.substring(6, 8));
			if (cal.get(Calendar.DATE) > 28 && baseday > 28) {
				if (!DateUtil.monthEnd(formatter.format(cal.getTime()))) {// 如果不是月末，则使用baseDate的日期
					cal.set(Calendar.DATE, baseday);
				}
			}
			if (DateUtil.monthEnd(baseDate) && endOfMonth) {
				String s = formatter.format(cal.getTime());
				return DateUtil.getEndDateOfMonth(s);
			}

		} else if (termUnit.equals(DateUtil.TERM_UNIT_YEAR)) {
			cal.add(Calendar.YEAR, step);
		} else
			throw new Exception("期限单位【" + termUnit + "】无法解析！");

		return formatter.format(cal.getTime());
	}

	/**
	 * 获得和给定日期sDate相差Days天的日期yyyyMMdd
	 */
	public static String getRelativeDate(String baseDate, String date,
			String termUnit, int step) throws Exception {
		return DateUtil.getRelativeDate(baseDate, false, date, termUnit, step);
	}

	/**
	 * 获得和给定日期BeginDate和EndDate相差的期数（取整，直接抛掉小数位）
	 */
	public static int getTermPeriod(String BeginDate, String EndDate,
			String termUnit, int step) throws Exception {
		if (step <= 0)
			throw new Exception("期限周期不能小于零！");
		;
		if (termUnit == null)
			throw new Exception("未传入期限单位不正确！");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(BeginDate));
		if (termUnit.equals(DateUtil.TERM_UNIT_DAY)) {
			return (int) (getDays(BeginDate, EndDate) / step);
		} else if (termUnit.equals(DateUtil.TERM_UNIT_MONTH)) {

			return (int) (getMonths(BeginDate, EndDate) / step);
		} else if (termUnit.equals(DateUtil.TERM_UNIT_YEAR)) {
			return (int) (getYears(BeginDate, EndDate) / step);
		} else
			throw new Exception("未传入期限单位不正确！");
	}

	public static boolean monthEnd(String date) throws ParseException {
		if (date.equals(getEndDateOfMonth(date)))
			return true;
		return false;
	}

	/**
	 * 得到月底
	 */
	public static String getEndDateOfMonth(String curDate)
			throws ParseException {
		if (curDate == null || curDate.length() !=8)
			return null;
		curDate = curDate.substring(0, 6) + "01";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(curDate));
		int maxDays = cal.getActualMaximum(Calendar.DATE);// 得到该月的最大天数
		cal.set(Calendar.DATE, maxDays);
		return formatter.format(cal.getTime());
	}

	public static Date getDate(String curDate,String format) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		cal.setTime(formatter.parse(curDate));

		return cal.getTime();
	}

	/**
	 * 得到当前的时间 返回值
	 */
	public static String getDateString(Date date, String format) {
		SimpleDateFormat sdfTempDate = new SimpleDateFormat(format);
		String prev = sdfTempDate.format(date);
		return prev;
	}

	/**
	 * @param date
	 *            yyyyMMdd
	 * @param type
	 *            1 返回数字 2 返回中文 3 返回英文 返回星期 1 星期一 、2 星期二 、3星期三、4 星期四、5 星期五、6
	 *            星期六、7 星期日
	 */
	public static String getWeekDay(String date, String type)
			throws ParseException {
		String[] sWeekDates = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String[] sWeekDatesE = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(date));
		if (type.equals("2"))
			return sWeekDates[cal.get(Calendar.DAY_OF_WEEK) - 1];
		else if (type.equals("3"))
			return sWeekDatesE[cal.get(Calendar.DAY_OF_WEEK) - 1];
		else
			return String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
	}

	/**
	 * @param date
	 *            yyyyMMdd
	 * @return yyyyMMdd 返回某个日期对应周的所有日期
	 */
	public static String[] getWeekDates(String date) throws ParseException {
		String[] sWeekDates = { "", "", "", "", "", "", "" };
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(date));
		// 星期日
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 6);
		sWeekDates[0] = formatter.format(cal.getTime());
		// 星期一
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 5);
		sWeekDates[1] = formatter.format(cal.getTime());
		// 星期二
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 4);
		sWeekDates[2] = formatter.format(cal.getTime());
		// 星期三
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 3);
		sWeekDates[3] = formatter.format(cal.getTime());
		// 星期四
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 2);
		sWeekDates[4] = formatter.format(cal.getTime());
		// 星期五
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 1);
		sWeekDates[5] = formatter.format(cal.getTime());
		// 星期六
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
		sWeekDates[6] = formatter.format(cal.getTime());
		return sWeekDates;
	}

	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 *             获取两个日期之间的天数
	 */
	public static int getDays(String sBeginDate, String sEndDate) {
		Date startDate = java.sql.Date.valueOf(sBeginDate.replace('/', '-'));
		Date endDate = java.sql.Date.valueOf(sEndDate.replace('/', '-'));

		int iDays = (int) ((endDate.getTime() - startDate.getTime()) / 86400000L);
		return iDays;
	}

	/**
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 *             获取两个日期之间的时间
	 */
	public static String getTime(String beginTime, String endTime)
			throws Exception {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date begin = dfs.parse(beginTime);
		Date end = dfs.parse(endTime);

		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		long day = between / (24 * 3600);
		long hour = (between / 3600 - day * 24);
		long minute = (between / 60 - day * 24 * 60 - hour * 60);
		long second = (between - day * 24 * 3600 - hour * 3600 - minute * 60);
		String returnMessage = "" + day + "天" + hour + "小时" + minute + "分"
				+ second + "秒";
		// System.out.println(""+day+"天"+hour+"小时"+minute+"分"+second+"秒");
		return returnMessage;
	}

	/**
	 * 获取两个日期之间的月数，向下取整
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 * @throws ParseException
	 * 
	 */
	public static int getMonths(String beginDate1, String endDate1)
			throws ParseException {
		Date beginDate = getDate(beginDate1,"yyyyMMdd");
		Date endDate = getDate(endDate1,"yyyyMMdd");
		Calendar former = Calendar.getInstance();
		Calendar latter = Calendar.getInstance();
		former.clear();
		latter.clear();
		boolean positive = true;
		if (beginDate.after(endDate)) {
			former.setTime(endDate);
			latter.setTime(beginDate);
			positive = false;
		} else {
			former.setTime(beginDate);
			latter.setTime(endDate);
		}

		int monthCounter = 0;
		while (former.get(Calendar.YEAR) != latter.get(Calendar.YEAR)
				|| former.get(Calendar.MONTH) != latter.get(Calendar.MONTH)) {
			former.add(Calendar.MONTH, 1);
			monthCounter++;
		}

		if (positive)
			return monthCounter;
		else
			return -monthCounter;
	}

	/**
	 * 获取2个日期相隔的月数（向上取整） 传入的2个日期没有顺序要求
	 * 
	 * @param date1
	 *            yyyyMMdd
	 * @param date2
	 *            yyyyMMdd
	 * @return
	 * @throws Exception
	 */
	public static int getUpMonths(String date1, String date2) throws Exception {
		String beginDate = "";
		String endDate = "";
		int months_Between = 0;
		if (date1.equals(date2)) {
			return months_Between;
		} else if (date1.compareTo(date2) > 0) {
			endDate = date1;
			beginDate = date2;
		} else {
			beginDate = date1;
			endDate = date2;
		}
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(getDate(beginDate,"yyyyMMdd"));

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(getDate(endDate,"yyyyMMdd"));

		while (beginCalendar.get(Calendar.YEAR) != endCalendar
				.get(Calendar.YEAR)
				|| beginCalendar.get(Calendar.MONTH) != endCalendar
						.get(Calendar.MONTH)) {
			beginCalendar.add(Calendar.MONTH, 1);
			months_Between++;
		}
		if (!monthEnd(beginDate)) {
			String beginDays = beginDate.substring(8);
			String endDays = endDate.substring(8);
			if (endDays.compareTo(beginDays) > 0) {
				months_Between++;
			}
		}
		return months_Between;
	}

	/**
	 * 获取两个日期之间的年数 add by xjzhao 2011/04/06
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 * @throws ParseException
	 * 
	 */
	public static int getYears(String beginDate1, String endDate1)
			throws ParseException {
		Date beginDate = getDate(beginDate1,"yyyyMMdd");
		Date endDate = getDate(endDate1,"yyyyMMdd");
		Calendar former = Calendar.getInstance();
		Calendar latter = Calendar.getInstance();
		former.clear();
		latter.clear();
		boolean positive = true;
		if (beginDate.after(endDate)) {
			former.setTime(endDate);
			latter.setTime(beginDate);
			positive = false;
		} else {
			former.setTime(beginDate);
			latter.setTime(endDate);
		}

		int monthCounter = 0;
		while (former.get(Calendar.YEAR) != latter.get(Calendar.YEAR)) {
			former.add(Calendar.YEAR, 1);
			monthCounter++;
		}

		if (positive)
			return monthCounter;
		else
			return -monthCounter;
	}

	/**
	 * 获取两个日期之间的年数，向上取整 add by xjzhao 2011/04/06
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 * @throws ParseException
	 * 
	 */
	public static int getUpYears(String beginDate1, String endDate1)
			throws ParseException {
		Date beginDate = getDate(beginDate1,"yyyyMMdd");
		Date endDate = getDate(endDate1,"yyyyMMdd");
		Calendar former = Calendar.getInstance();
		Calendar latter = Calendar.getInstance();
		former.clear();
		latter.clear();
		boolean positive = true;
		if (beginDate.after(endDate)) {
			former.setTime(endDate);
			latter.setTime(beginDate);
			positive = false;
		} else {
			former.setTime(beginDate);
			latter.setTime(endDate);
		}

		int monthCounter = 0;
		while (former.get(Calendar.YEAR) != latter.get(Calendar.YEAR)) {
			former.add(Calendar.YEAR, 1);
			monthCounter++;
		}

		// modify by xjzhao 当天数前者小于后者时才加一，防止少计算一年
		if (positive
				&& beginDate1.substring(5).compareTo(endDate1.substring(5)) < 0) {
			monthCounter++;
		} else if (!positive
				&& beginDate1.substring(5).compareTo(endDate1.substring(5)) > 0) {
			monthCounter++;
		}

		if (positive)
			return monthCounter;
		else
			return -monthCounter;
	}

	public static int compareDate(String date1, String date2)
			throws ParseException {
		Date date1d = DateUtil.getDate(date1,"yyyyMMdd");
		Date date2d = DateUtil.getDate(date2,"yyyyMMdd");
		if (date1d.after(date2d))
			return 1;
		if (date1d.before(date2d))
			return -1;
		else
			return 0;
	}

	/*
	 * 传入日期和 区域标志 返回是否工作日
	 */
	public static boolean isWorkingDate(String date, String calendarType)
			throws Exception {
		// ASValuePool t=(ASValuePool)workDays.getAttribute(calendarType);
		// if(t==null) return true;
		// ASValuePool workPool= (ASValuePool)t.getAttribute(date);
		// if(workPool==null) return true;
		// String flag = workPool.getString("WorkFlag");
		// //工作日
		// if("1".equals(flag)){
		// return true;
		// }
		// //节假日
		// else if("2".equals(flag)){
		// return false;
		// }
		// //法定假
		// else if("3".equals(flag)){
		// return false;
		// }
		// //其他
		// else throw new Exception("节假日标志 WorkFlag【"+flag+"】定义错误！");
		return true;
	}

	/*
	 * 传入日期和日历脚本，如A||B或者A&&B， 返回是否工作日 ,
	 */
	public static boolean isWorkingDate2(String date, String calendarTypeScript)
			throws Exception {
		String operator;
		String[] calendarType;
		if (calendarTypeScript.indexOf("&&") >= 0) {
			operator = "&&";
			calendarType = calendarTypeScript.split("&&");
		} else if (calendarTypeScript.indexOf("||") >= 0) {
			operator = "||";
			calendarType = calendarTypeScript.split("||");
		} else {
			return isWorkingDate(date, calendarTypeScript);
		}

		boolean b = true;
		for (int i = 0; i < calendarType.length; i++) {
			if (operator.equals("&&"))
				b = b && isWorkingDate(date, calendarType[i]);
			else if (operator.equals("||"))
				b = b || isWorkingDate(date, calendarType[i]);
			else
				throw new Exception("无效的标示");
		}
		return b;
	}

	/**
	 * 得到距离当前日期最近的未来工作日
	 * 
	 * @param date
	 * @return 得到工作日
	 * @throws Exception
	 */
	public static String getNextWorkDate(String date, String calendarTypeScript)
			throws Exception {
		String nextworkdate = date.trim();
		while (true) {
			nextworkdate = DateUtil.getRelativeDate(nextworkdate,
					DateUtil.TERM_UNIT_DAY, 1);
			if (DateUtil.isWorkingDate2(nextworkdate, calendarTypeScript))
				break;
		}
		return nextworkdate;
	}

	/**
	 * 得到距离当前日期最近的过去工作日
	 * 
	 * @param date
	 * @return 得到工作日
	 * @throws Exception
	 */
	public static String getLastWorkDate(String date, String calendarTypeScript)
			throws Exception {
		String nextworkdate = date.trim();
		while (true) {
			nextworkdate = DateUtil.getRelativeDate(nextworkdate,
					DateUtil.TERM_UNIT_DAY, -1);
			if (DateUtil.isWorkingDate(nextworkdate, calendarTypeScript))
				break;
		}
		return nextworkdate;
	}

	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 *            (int)
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为闰年
	 * 
	 * @param date
	 *            （String）yyyyMMdd
	 * @return
	 */
	public static boolean isLeapYear(String date) throws Exception {
		return isLeapYear(Integer.parseInt(date.substring(0, 4)));
	}

	/*
	 * 清空缓存对象
	 * 
	 * @see com.amarsoft.dict.als.cache.AbstractCache#clear()
	 */
	public void clear() throws Exception {

	}


	public static int getUpTermPeriod(String BeginDate, String EndDate,
			String termUnit, int step) throws Exception {
		if (step <= 0)
			throw new Exception("期限周期不能小于零！");
		;
		if (termUnit == null)
			throw new Exception("未传入期限单位不正确！");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(formatter.parse(BeginDate));
		if (termUnit.equals(DateUtil.TERM_UNIT_DAY)) {
			return (int) (getDays(BeginDate, EndDate) / step);
		} else if (termUnit.equals(DateUtil.TERM_UNIT_MONTH)) {

			return (int) (getUpMonths(BeginDate, EndDate) / step);
		} else if (termUnit.equals(DateUtil.TERM_UNIT_YEAR)) {
			return (int) (getUpYears(BeginDate, EndDate) / step);
		} else
			throw new Exception("未传入期限单位不正确！");
	}
	public static void main(String[] arg) throws ParseException{
//		Date activityDate = DateUtil.getDate("20181010","yyyyMMdd");
//		Date d = new Date();
//		if(d.after(activityDate)){
//			System.out.println(1);
//		}
		
		
		String vcd = "0619";
		String y = vcd.substring(2,4);
		System.out.println(y);
		String m = vcd.substring(0,2);	
		System.out.println(m);
		System.out.println(DateUtil.getEndDateOfMonth("20"+y+m+"01"));
	}
}
