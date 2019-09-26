package com.gainetdb.autocheck.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DateUtils {
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String MONTH_PATTERN = "yyyy-MM";
	public static final String YEAR_PATTERN = "yyyy";
	public static final String MINUTE_ONLY_PATTERN = "mm";
	public static final String HOUR_ONLY_PATTERN = "HH";
	
	/**
	 * 日期相加减天数
	 * @param date 如果为Null，则为当前时间
	 * @param days 加减天数
	 * @param includeTime 是否包括时分秒,true表示包含
	 * @return
	 * @throws ParseException 
	 */
	public static Date dateAdd(Date date, int days, boolean includeTime) throws ParseException{
		if(date == null){
			date = new Date();
		}
		if(!includeTime){
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
			date = sdf.parse(sdf.format(date));
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	/**
	 * 时间格式化成字符串
	 * @param date Date
	 * @param pattern StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String dateFormat(Date date, String pattern) throws ParseException{
		if(StringUtils.isBlank(pattern)){
			pattern = DateUtils.DATE_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 字符串解析成时间对象
	 * @param dateTimeString String
	 * @param pattern StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date dateParse(String dateTimeString, String pattern) throws ParseException{
		if(StringUtils.isBlank(pattern)){
			pattern = DateUtils.DATE_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateTimeString);
	}
	
	/**
	 * 将日期时间格式成只有日期的字符串（可以直接使用dateFormat，Pattern为Null进行格式化）
	 * @param dateTime Date
	 * @return
	 * @throws ParseException
	 */
	public static String dateTimeToDateString(Date dateTime) throws ParseException{
		String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);  
	    return dateTimeString.substring(0, 10); 
	}
	
	/**
	 * 当时、分、秒为00:00:00时，将日期时间格式成只有日期的字符串，
	 * 当时、分、秒不为00:00:00时，直接返回
	 * @param dateTime Date
	 * @return
	 * @throws ParseException
	 */
	public static String dateTimeToDateStringIfTimeEndZero(Date dateTime) throws ParseException{
		String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
		if(dateTimeString.endsWith("00:00:00")){
			return dateTimeString.substring(0, 10);
		}else{
			return dateTimeString;
		}
	}
	
	/**
	 * 将日期时间格式成日期对象，和dateParse互用
	 * @param dateTime Date
	 * @return Date
	 * @throws ParseException
	 */
	public static Date dateTimeToDate(Date dateTime) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/** 
     * 时间加减小时
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param hours 加减的小时 
     * @return Date 
     */  
    public static Date dateAddHours(Date startDate, int hours) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);  
        return c.getTime();  
    }
    
    /**
     * 时间加减分钟
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param minutes 加减的分钟
     * @return
     */
    public static Date dateAddMinutes(Date startDate, int minutes) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);  
        return c.getTime();  
    }
    

    public static Date dateAddSeconds(Date startDate, int seconds) {  
    	if (startDate == null) {  
    		startDate = new Date();  
    	}  
    	Calendar c = Calendar.getInstance();  
    	c.setTime(startDate);  
    	c.set(Calendar.SECOND, c.get(Calendar.SECOND) + seconds);  
    	return c.getTime();  
    }

	/** 
     * 时间加减天数 
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param days 加减的天数 
     * @return Date 
     */  
    public static Date dateAddDays(Date startDate, int days) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);  
        return c.getTime();  
    }
    
    /** 
     * 时间加减月数
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param months 加减的月数 
     * @return Date 
     */  
    public static Date dateAddMonths(Date startDate, int months) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);  
        return c.getTime();  
    }
    
    /** 
     * 时间加减年数
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param years 加减的年数 
     * @return Date 
     */  
    public static Date dateAddYears(Date startDate, int years) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);  
        return c.getTime();  
    }  
    
    /** 
     * 时间比较（如果myDate>compareDate返回1，<返回-1，相等返回0） 
     * @param myDate 时间 
     * @param compareDate 要比较的时间 
     * @return int 
     */  
    public static int dateCompare(Date myDate, Date compareDate) {  
        Calendar myCal = Calendar.getInstance();  
        Calendar compareCal = Calendar.getInstance();  
        myCal.setTime(myDate);  
        compareCal.setTime(compareDate);  
        return myCal.compareTo(compareCal);  
    }

/**
     * 获取两个时间中最小的一个时间
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMin(Date date, Date compareDate) {
    	if(date == null){
    		return compareDate;
    	}
    	if(compareDate == null){
    		return date;
    	}
    	if(1 == dateCompare(date, compareDate)){
    		return compareDate;
    	}else if(-1 == dateCompare(date, compareDate)){
    		return date;
    	}
    	return date;  
    }
    
    /**
     * 获取两个时间中最大的一个时间
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMax(Date date, Date compareDate) {
    	if(date == null){
    		return compareDate;
    	}
    	if(compareDate == null){
    		return date;
    	}
    	if(1 == dateCompare(date, compareDate)){
    		return date;
    	}else if(-1 == dateCompare(date, compareDate)){
    		return compareDate;
    	}
    	return date;  
    }
    
    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException 
     */
    public static int dateBetween(Date startDate, Date endDate) throws ParseException {
        Date dateStart = dateParse(dateFormat(startDate, DATE_PATTERN), DATE_PATTERN);
        Date dateEnd = dateParse(dateFormat(endDate, DATE_PATTERN), DATE_PATTERN);
        return (int) ((dateEnd.getTime() - dateStart.getTime())/1000/60/60/24); 
    }
    
    /**
     * 获取两个日期（不含时分秒）相差的天数，包含今天
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException 
     */
    public static int dateBetweenIncludeToday(Date startDate, Date endDate) throws ParseException {  
        return dateBetween(startDate, endDate) + 1;
    }
    
    /**
     * 获取日期时间的年份，如2017-02-13，返回2017
     * @param date
     * @return
     */
    public static int getYear(Date date) {  
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * 获取日期时间的月份，如2017年2月13日，返回2
     * @param date
     * @return
     */
    public static int getMonth(Date date) {  
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    /**
     * 获取日期时间的第几天（即返回日期的dd），如2017-02-13，返回13
     * @param date
     * @return
     */
    public static int getDate(Date date) {  
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }
    
    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {  
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }
    
    /**
     * 获取日期时间当年的总天数，如2017-02-13，返回2017年的总天数
     * @param date
     * @return
     */
    public static int getDaysOfYear(Date date) {  
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 根据时间获取当月最大的日期
     * <li>2017-02-13，返回2017-02-28</li>
     * <li>2016-02-13，返回2016-02-29</li>
     * <li>2016-01-11，返回2016-01-31</li>
     * @param date Date
     * @return
     * @throws Exception 
     */
    public static Date maxDateOfMonth(Date date) throws Exception {
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
    	int value = cal.getActualMaximum(Calendar.DATE);
    	return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }
    
    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     * @param date Date
     * @return
     * @throws Exception 
     */
    public static Date minDateOfMonth(Date date) throws Exception {
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
    	int value = cal.getActualMinimum(Calendar.DATE);
    	return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }

	/**
	 * 生成随机时间
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}

	/**
	 * 比较日期大小
	 * <p>
	 * 2017年9月7日 16:15:53
	 * xj
	 *
	 * @param DATE1      第一个时间
	 * @param DATE2      第二个时间
	 * @param dateFormat 日期格式
	 * @return Integer null日期格式有误，1：第一个日期大，0：两个日期一样，-1：第二个日期大
	 */
	public static Integer compareDate(String DATE1, String DATE2, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public static int  getHoliday(){
        //http://api.goseek.cn/,正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2，休息日对应结果为 3
		//默认选一个正常工作日
		String currentDate="20190929";
		try {
			currentDate=DateUtils.dateFormat(new Date(),"yyyyMMdd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<JSONObject> exchange= RestTemplateUtils.get("http://api.goseek.cn/Tools/holiday?date="+currentDate,JSONObject.class,"");
		System.out.println("exchange："+exchange.getBody());
		return exchange.getBody().getInteger("data") ;
	}
	public static void main(String[] args) throws Exception {
		/*System.out.println(dateTimeToDate(new Date()));
		System.out.println(dateParse("2017-02-04 14:58:20", null));
		System.out.println(dateTimeToDateStringIfTimeEndZero(new Date()));
		System.out.println(dateTimeToDateStringIfTimeEndZero(dateTimeToDate(new Date())));*/
		//System.out.println(dateBetween(dateParse("2017-01-30", null), dateParse("2017-02-01", null)));
		//System.out.println(dateBetweenIncludeToday(dateParse("2017-01-30", null), dateParse("2017-02-01", null)));
		//System.out.println(getDate(dateParse("2017-01-17", null)));
		/*
		System.out.println(getDaysOfMonth(dateParse("2017-02-01", null)));
		System.out.println(getDaysOfYear(dateParse("2017-01-30", null)));*/
		//System.out.println(dateFormat(dateAddMonths(dateParse("2017-02-07", StrUtils.MONTH_PATTERN), -12), StrUtils.MONTH_PATTERN));
		/*System.out.println(dateFormat(maxDateOfMonth(dateParse("2016-02", "yyyy-MM")), null));
		System.out.println(dateFormat(minDateOfMonth(dateParse("2016-03-31", null)), null));*/

		Date randomDate = randomDate("2019-09-24 09:00:00", "2019-09-24 10:00:00");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String result = format.format(randomDate);
		System.out.println(result);
		System.out.println(compareDate("09","10","HH"));

		DateUtils.compareDate("10",String.valueOf(new Date().getHours()),"HH");
		System.out.println(dateFormat(new Date(),null));

		String currentDate=DateUtils.dateFormat(new Date(),null);
		if (DateUtils.compareDate("10",String.valueOf(new Date().getHours()),"HH")<0){
			//上班逻辑
		String	clockTime=DateUtils.dateFormat(DateUtils.randomDate(currentDate+" 08:10:00", currentDate+" 08:50:00"),"yyyy-MM-dd HH:mm:ss");
			System.out.println(clockTime);
		}
		System.out.println(getHoliday());
	}
	
	
}
