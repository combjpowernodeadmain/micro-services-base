package com.bjzhianjia.scp.cgp.util;

import com.bjzhianjia.scp.cgp.constances.CommonConstances;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author 尚
 *
 */
public class DateUtil {

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static final String DATE_FORMAT_DF = "yyyy-MM-dd";

	/**
	 * 将字符串类型日期转化为Date类型，默认格式为"yyyy-MM-dd"
	 * @author 尚
	 * @param dateStr
	 * @return
	 */
	public static Date dateFromStrToDate(String dateStr) {
		SimpleDateFormat format=new SimpleDateFormat(CommonConstances.DATE_FORMAT);
		Date date =null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串类型日期转化为Date类型，默认格式为
	 * @author 尚
	 * @param dateStr
	 * @return
	 */
	public static Date dateFromStrToDate(String dateStr,boolean isFullDateTime) {
	    if(isFullDateTime) {
	        return dateFromStrToDate(dateStr, CommonConstances.DATE_FORMAT_FULL);
	    }
	    return dateFromStrToDate(dateStr);
	}

	/**
	 * 将指定格式的字符串日期转化为Date类型
	 * @author 尚
	 * @param dateStr 字符串日期源
	 * @param formater 日期格式
	 * @return
	 */
	public static Date dateFromStrToDate(String dateStr,String formater) {
		SimpleDateFormat format=new SimpleDateFormat(formater);
		Date date =null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 以formater为模板，将日期类型转化为字符串表示 的日期
	 * @author 尚
	 * @param date
	 * @param formater
	 * @return
	 */
	public static String dateFromDateToStr(Date date,String formater) {
		SimpleDateFormat format=new SimpleDateFormat(formater);
		return format.format(date);
	}

	/**
     * 获取date所在月和最后一天
     *
     * @param date
     * @return
     */
    public static Date theLastDatOfMonth(Date date) {
        DateTime dateTime = new DateTime(date.getTime());// date
        DateTime plusMonths = dateTime.plusMonths(1);// date的后一个月
        DateTime withDayOfMonth = plusMonths.withDayOfMonth(1);// date后一个月的第一天
        DateTime minusDays = withDayOfMonth.minusDays(1);// date所在月的最后一天

        return new Date(minusDays.getMillis());
    }

	/**
	 * 获取sourceDate明天的日期
	 * @param sourceDate
	 * @return
	 */
	public static Date theDayOfTommorrow(Date sourceDate) {
		return theDayOfPlus(sourceDate,1);
    }

	/**
	 * 获取sourceDate之后days天的日期
	 * @param sourceDate
	 * @param days
	 * @return
	 */
	public static Date theDayOfPlus(Date sourceDate,int days){
		DateTime dateTime = new DateTime(sourceDate.getTime());
		return new Date(dateTime.plusDays(days).getMillis());
	}

	/**
	 * 给指定日期添加指定的分钟数
	 * @param date 日期
	 * @param minute 需要添加的分钟数
	 * @return
	 */
	public static Date addMinute(Date date, int minute){
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		time.add(Calendar.MINUTE, minute);
		return time.getTime();
	}
	/**
	 * 给指定日期添加指定的秒数
	 * @param date 日期
	 * @param second 需要添加的秒数
	 * @return
	 */
	public static Date addSecond(Date date, int second){
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		time.add(Calendar.SECOND, second);
		return time.getTime();
	}
	/**
	 * 将时间戳转化为Date类型，默认格式为"yyyyy-MM-dd HH:mm:ss""
	 * @param time
	 * @return
	 */
	public static Date dateFromLongToDate(long time) {
		SimpleDateFormat format=new SimpleDateFormat(CommonConstances.DATE_FORMAT_FULL);
		Date date =null;
		try {
			String dateStr = format.format(time);
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 获取当年开始日期
	 * @return
	 */
	public static Date getYearStart(){
		Calendar calendar =  Calendar.getInstance();
		//月份
		calendar.set(Calendar.MONTH,0);
		//天
		calendar.set(Calendar.DAY_OF_MONTH,0);
		//时
		calendar.set(Calendar.HOUR_OF_DAY,0);
		//分
		calendar.set(Calendar.MINUTE,0);
		//秒
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	/**
	 * 获取当年结束日期
	 * @return
	 */
	public static Date getYearEnd(){
		Calendar calendar =  Calendar.getInstance();
		//月份
		calendar.set(Calendar.MONTH,12);
		//天
		calendar.set(Calendar.DAY_OF_MONTH,0);
		//时
		calendar.set(Calendar.HOUR_OF_DAY,0);
		//分
		calendar.set(Calendar.MINUTE,0);
		//秒
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	/**
	 * java 获取获取某年某月
	 *
	 * @param year  年份
	 * @param month 月份
	 * @return
	 */
	public static Date getMonthFullDay(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		// 1月从0开始
		cal.set(Calendar.MONTH, month - 1);
		// 当月1号
		cal.set(Calendar.DAY_OF_MONTH, 1);
		//天
		cal.set(Calendar.DAY_OF_MONTH, 0);
		//时
		cal.set(Calendar.HOUR_OF_DAY, 0);
		//分
		cal.set(Calendar.MINUTE, 0);
		//秒
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取本周的开始时间
	 */
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	/**
	 * 获取本周的结束时间
	 *
	 * @return
	 */
	public static Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	/**
	 * 获取某个日期的开始时间
	 *
	 * @param d
	 * @return
	 */
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 获取某个日期的结束时间
	 *
	 * @param d
	 * @return
	 */
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d){
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}
}
