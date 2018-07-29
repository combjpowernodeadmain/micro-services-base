package com.bjzhianjia.scp.cgp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author 尚
 *
 */
public class DateUtil {
	
	/**
	 * 将字符串类型日期转化为Date类型，默认格式为"yyyy-MM-dd"
	 * @author 尚
	 * @param dateStr
	 * @return
	 */
	public static Date dateFromStrToDate(String dateStr) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date date =null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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
	
}
