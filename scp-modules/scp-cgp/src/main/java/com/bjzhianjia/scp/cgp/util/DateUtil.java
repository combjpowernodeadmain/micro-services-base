package com.bjzhianjia.scp.cgp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

import com.bjzhianjia.scp.cgp.constances.CommonConstances;

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
}
