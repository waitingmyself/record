/*
 * Created on 2005-7-13
 * 
 * 时间工具类.
 */
package com.waitingmyself.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 * 
 * @author 马劼
 */
public class TimeUtil {

	/**
	 * 得到当前时间
	 * 
	 * @param timeFormat
	 * @return nowstr
	 */
	public static Date getNowTime() {
		Date now = new Date();
		return now;
	}

	/**
	 * SimpleDateFormat到格式的日期
	 * 
	 * @param formatString
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getDateFormatter(String f) {
		SimpleDateFormat formatter = new SimpleDateFormat(f);
		// workaround for bug
		formatter.setTimeZone(java.util.TimeZone.getDefault());
		// don't alllow things like 1/35/99
		formatter.setLenient(false);
		return formatter;
	}

	/**
	 * 获得当前时间Calendar格式
	 * 
	 * @param d
	 * @return now date
	 */
	public static Calendar getCalendar(Date d) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		return now;
	}

	/**
	 * 得到指定格式的日期字符串
	 * 
	 * @param timeFormat
	 * @return nowDate String.
	 */
	public static String getNowTime(String f) {
		return getDateFormatter(f).format(getNowTime());
	}

	/**
	 * 得到当前时间的毫秒部分
	 * 
	 * @return str
	 */
	public static String getSecond() {
		return getNowTime("sss");
	}

	/**
	 * String到Date格式的转换
	 * 
	 * @param dateString
	 * @param formatString
	 * @return date Date
	 */
	public static Date stringToDate(String d, String f) {
		// will throw exception if can't parse
		try {
			return getDateFormatter(f).parse(d);
		} catch (ParseException e) {
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * string 到 date 的转换
	 * 
	 * @param d
	 * @param f
	 * @return
	 */
	public static Long stringToLong(String d, String f) {
		if (d == null || "".equals(d)) {
			return null;
		}
		Date date = stringToDate(d, f);
		if (date == null) {
			return null;
		}
		return date.getTime();
	}

	/**
	 * date到String格式的转换
	 * 
	 * @param date
	 * @param format
	 * @return date String
	 */
	public static String dateToString(Date d, String f) {
		return getDateFormatter(f).format(d);
	}

	/**
	 * Long到String格式的转换
	 * 
	 * @param d
	 * @param f
	 * @return date String
	 */
	public static String longToString(Long d, String f) {
		if (d == null)
			return "";
		return dateToString(longToDate(d), f);
	}

	/**
	 * 当前时间的long格式,除以1000以Integer表示. 主要用于与c++交换日期,缺点是没有毫秒值.
	 * 
	 * @return longDate Integer
	 */
	public static Integer nowDateToInteger() {
		return Integer.valueOf(String.valueOf(getNowTime().getTime() / 1000));
	}

	/**
	 * 转换指定日期到Integer格式
	 * 
	 * @param date
	 * @return date Integer
	 */
	public static Integer dateToInteger(Date date) {
		if (date == null) {
			return new Integer(0);
		} else {
			return Integer.valueOf(String.valueOf(date.getTime() / 1000));
		}
	}

	/**
	 * integer to Date 主要用于从c++中传入的日期类型转换为Date类型.
	 * 
	 * @param date
	 * @return date Date
	 */
	public static Date integerToDate(Integer date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(((long) date.intValue()) * 1000);
		return cal.getTime();
	}

	/**
	 * 转换现在的时间为Long格式 以毫秒为单位计算,从1970年1月1日0时0分0秒0000毫秒算起.
	 * 
	 * @return longDate long
	 */
	public static long dateTolong() {
		return getNowTime().getTime();
	}

	/**
	 * @see this.dateTolong
	 * 
	 * @return longDate Long
	 */
	public static Long dateToLong() {
		return new Long(getNowTime().getTime());
	}

	/**
	 * this.dateTolong
	 * 
	 * @return longDate string
	 */
	public static String dateToLongString() {
		return String.valueOf(getNowTime().getTime());
	}

	/**
	 * long转换为日期
	 * 
	 * @param date
	 * @return date Date
	 */
	public static Date longToDate(long d) {
		return new Date(d);
	}

	/**
	 * long转换为日期
	 * 
	 * @param date
	 * @return date Date
	 */
	public static Date longToDate(Long d) {
		return new Date(d.longValue());
	}

	/**
	 * lang or integer型转换成date string
	 * 
	 * @param str
	 * @param format
	 * @return string date
	 */
	public static String getDate(String str, String format) {
		if (str.length() > 11) {
			return dateToString(longToDate(Long.valueOf(str)), format);
		} else {
			try {
				return dateToString(integerToDate(Integer.valueOf(str)), format);				
			} catch(Exception e) {
				return dateToString(longToDate(Long.valueOf(str)), format);								
			}
		}
	}

	/**
	 * 取得某月最后一天的日期整数值
	 * 
	 * @param year
	 *            年(yyyy)
	 * @param month
	 *            月(mm或m)
	 * @return int 日期整数值
	 */
	public static int getMaxDayOfMonth(String year, String month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(year));
		c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算两个long型的时间之间的差（**天 ** 小时 ** 分钟）
	 * 
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return
	 */
	public static String getTimeDiff(long starttime, long endtime) {
		StringBuffer time = new StringBuffer();
		if (endtime > starttime) {
			long left = endtime - starttime;
			long daynum = left / (24 * 60 * 60 * 1000);
			left = left - daynum * (24 * 60 * 60 * 1000);
			long hournum = left / (3600 * 1000);
			left = left - hournum * (3600 * 1000);
			long minnum = left / (60 * 1000);
			if (daynum > 0) {
				time.append(daynum + " 天 ");
			}
			if (hournum > 0) {
				time.append(hournum + " 小时 ");
			}
			if (minnum > 0) {
				time.append(minnum + " 分钟 ");
			}
		}
		return time.toString();
	}

}