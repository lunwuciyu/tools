package datetime;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateTimeUtil extends DateUtils{

	private static String arrWeekdays[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	public static final String DATA_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_FORMAT_AMERICAN = "MMM dd, yyyy hh:mm:ss a";

	/**
	 * For a instance : getDatePart(new Date(),"y"); Parameters list
	 * 
	 * <pre>
	 * Parameter illuminate
	 * y	year
	 * m	month
	 * d	day
	 * dm	day of the month
	 * dy	day of the year
	 * h	hour
	 * M	minute
	 * s	second
	 * S	millisecond
	 * w	week
	 * wim	week in a month
	 * wm	week of the month
	 * wy	week of the year
	 * </pre>
	 * 
	 * @param dateValue
	 *            -
	 * @param interval
	 *            -
	 * @return get the value corresponds on the date to be given
	 */
	public static int getDatePart(Date dateValue, String interval) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateValue);

		if (interval.equalsIgnoreCase("y"))
			return c.get(Calendar.YEAR);
		if (interval.equals("M"))
			return c.get(Calendar.MONTH);
		if (interval.equalsIgnoreCase("d"))
			return c.get(Calendar.DATE);
		if (interval.equalsIgnoreCase("dm"))
			return c.get(Calendar.DAY_OF_MONTH);
		if (interval.equalsIgnoreCase("dy"))
			return c.get(Calendar.DAY_OF_YEAR);
		if (interval.equals("h"))
			return c.get(Calendar.HOUR); // 12hours
		if (interval.equals("H"))
			return c.get(Calendar.HOUR_OF_DAY); // 24hours
		if (interval.equals("m"))
			return c.get(Calendar.MINUTE);
		if (interval.equals("s"))
			return c.get(Calendar.SECOND);
		if (interval.equals("S"))
			return c.get(Calendar.MILLISECOND);
		if (interval.equalsIgnoreCase("w"))
			return c.get(Calendar.DAY_OF_WEEK);
		if (interval.equalsIgnoreCase("wim"))
			return c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		if (interval.equalsIgnoreCase("wm"))
			return c.get(Calendar.WEEK_OF_MONTH);
		if (interval.equalsIgnoreCase("wy"))
			return c.get(Calendar.WEEK_OF_YEAR);

		return -1;
	}

	/**
	 * For a instance : getDatePart("2003-10-11", "yyyy-MM-dd", "M");
	 * 
	 * @param strDateValue
	 *            -
	 * @param pattern
	 *            -
	 * @param interval
	 *            -
	 * @return date correspond the paramater
	 * @throws ParseException
	 *             -
	 */
	public static int getDatePart(String strDateValue, String pattern, String interval) throws ParseException {
		SimpleDateFormat sdfFormat = new SimpleDateFormat(pattern);
		Date dateValue = sdfFormat.parse(strDateValue);
		return getDatePart(dateValue, interval);
	}

	/**
	 * For a instance : getDateAdd(new Date(),1,"y");
	 * 
	 * @param dateValue
	 *            -
	 * @param intDiff
	 *            -
	 * @param interval
	 *            -
	 * @return difference between the current date and the given
	 */
	public static Date getDateAdd(Date dateValue, int intDiff, String interval) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateValue);

		if (interval.equalsIgnoreCase("y"))
			c.add(Calendar.YEAR, intDiff);
		if (interval.equals("M"))
			c.add(Calendar.MONTH, intDiff);
		if (interval.equalsIgnoreCase("d"))
			c.add(Calendar.DATE, intDiff);
		if (interval.equalsIgnoreCase("h"))
			c.add(Calendar.HOUR, intDiff);
		if (interval.equals("m"))
			c.add(Calendar.MINUTE, intDiff);
		if (interval.equals("s"))
			c.add(Calendar.SECOND, intDiff);
		if (interval.equals("S"))
			c.add(Calendar.MILLISECOND, intDiff);

		return c.getTime();
	}

	/**
	 * @return last month
	 */
	public static String getLastMonth() {
		return getLastMonth(new Date());
	}

	/**
	 * For a instance : getLastMonth(new Date());
	 * 
	 * @param dateValue
	 *            -
	 * @return the last month of the month to be given
	 */
	public static String getLastMonth(Date dateValue) {
		Calendar c = Calendar.getInstance();

		c.setTime(dateValue);
		c.add(Calendar.MONTH, 0);

		return new SimpleDateFormat("yyyyMM").format(c.getTime());
	}

	/*
	 * For a instance : getCurrentDate("yyyy-MM-dd") or getCurrentDate("yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param pattern yyyy-mm-dd or yyyy-mm-dd HH:MM:ss
	 * 
	 * @return current date or datetime
	 */
	public static String getCurrentDate(String pattern) {
		return getFormatDateTime(new Date(), pattern);
	}

	public static String getCurrentDate() {
		return getCurrentDate(DATA_FORMAT_yyyy_MM_dd);
	}

	public static String getCurrentDateTime() {
		return getCurrentDate(DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
	}

	public static String getSpecifyDateTime(long value) {
		return getFormatDateTime(new Date(value), DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
	}

	public static Date getDateTime(long value) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value);
		return c.getTime();
	}

	public static String getSpecifyDate(long value) {
		return getFormatDateTime(new Date(value), DATA_FORMAT_yyyy_MM_dd);
	}

	/*
	 * For a instance : getCurrentWeekDay();
	 * 
	 * @return today
	 */
	public static String getCurrentWeekDay() {
		return arrWeekdays[getDatePart(new Date(), "w") - 1];
	}

	/*
	 * For a instance : getCurrentWeekDay(new Date());
	 * 
	 * @param Date dateValue -
	 * 
	 * @return the day correspond the given date
	 */
	public static String getWeekDay(Date dateValue) {
		return arrWeekdays[getDatePart(dateValue, "w") - 1];
	}

	/*
	 * For a instance : getCurrentWeekDay();
	 * 
	 * @param String dateValue -
	 * 
	 * @param String pattern -
	 * 
	 * @return the day correspond the given datetime
	 */
	public static String getWeekDay(String dateValue, String pattern) throws ParseException {
		return arrWeekdays[getDatePart(dateValue, pattern, "w") - 1];
	}

	/**
	 * For a instance : getFormatDateTime(new Date(), "yyyy-MM-dd") or getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param dateValue
	 *            -
	 * @param strFormat
	 *            -
	 * @return specification date depends on the given data pattern
	 */
	public static String getFormatDateTime(Date dateValue, String strFormat) {
		return new SimpleDateFormat(strFormat).format(dateValue);
	}

	/**
	 * For a instance : getFormatDateTime("20021011", "yyyyMMdd", "yyyy-MM-dd");
	 * 
	 * @param strDateValue
	 *            -
	 * @param strFormat1
	 *            -
	 * @param strFormat2
	 *            -
	 * @return change date into another pattern
	 * @throws ParseException
	 *             -
	 */
	public static String getFormatDateTime(String strDateValue, String strFormat1, String strFormat2) throws ParseException {
		return getFormatDateTime(new SimpleDateFormat(strFormat1).parse(strDateValue), strFormat2);
	}

	/**
	 * @param strDate
	 *            -
	 * @return change string to date
	 * @throws java.text.ParseException
	 *             -
	 */
	public static Date changeStringToDate(String strDate) throws ParseException {
		if (strDate == null || strDate.equals("")) {
			return null;
		}

		return DateFormat.getDateInstance(DateFormat.MEDIUM).parse(strDate);
	}

	/**
	 * For a instance : getDateDiff(date1, date2);
	 * 
	 * @param date1
	 *            -
	 * @param date2
	 *            -
	 * @return get the difference between two dates
	 */
	public static int getDateDiff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		Long lngDiff = (c1.getTime().getTime() - c2.getTime().getTime()) / (24 * 3600 * 1000);
		return (lngDiff.intValue());
	}

	/**
	 * For a instance : getDateDiff(new Date(),"2003-10-1","yyyy-MM-dd");
	 * 
	 * @param date1
	 *            -
	 * @param date2
	 *            -
	 * @param pattern
	 *            -
	 * @return get the number of day between two dates
	 * @throws ParseException
	 *             -
	 */
	public static int getDateDiff(Date date1, String date2, String pattern) throws ParseException {
		SimpleDateFormat sdfFormat = new SimpleDateFormat(pattern);
		return getDateDiff(date1, sdfFormat.parse(date2));
	}

	/**
	 * For a instance : getDateDiff("2003-10-1", "2003-9-1", "yyyy-MM-dd");
	 * 
	 * @param date1
	 *            -
	 * @param date2
	 *            -
	 * @param pattern
	 *            -
	 * @return get the number of day between two dates
	 * @throws ParseException
	 *             -
	 */
	public static int getDateDiff(String date1, String date2, String pattern) throws ParseException {
		SimpleDateFormat sdfFormat = new SimpleDateFormat(pattern);
		return getDateDiff(sdfFormat.parse(date1), sdfFormat.parse(date2));
	}

	/**
	 * For a instance : getDateDiff("2003/10/1", "yyyy/MM/dd", "2003-09-01", "yyyy-MM-dd");
	 * 
	 * @param date1
	 *            -
	 * @param pattern1
	 *            -
	 * @param date2
	 *            -
	 * @param pattern2
	 *            -
	 * @return get the number of day between two dates
	 * @throws ParseException
	 *             -
	 */
	public static int getDateDiff(String date1, String pattern1, String date2, String pattern2) throws ParseException {
		SimpleDateFormat sdfFormat1 = new SimpleDateFormat(pattern1);
		SimpleDateFormat sdfFormat2 = new SimpleDateFormat(pattern2);
		return getDateDiff(sdfFormat1.parse(date1), sdfFormat2.parse(date2));
	}

	/**
	 * For a instance : getDateAfer(10,"yyyy-MM-dd")
	 * 
	 * @param inter
	 *            -
	 * @param format
	 *            -
	 * @return get the day before or after today
	 */
	public static String getDateAfter(long inter, String format) {
		Date d1 = new Date();
		d1.setTime(d1.getTime() + inter * 24 * 3600 * 1000);
		return getFormatDateTime(d1, format);
	}

	public static String getDateAfter(String date1, String date1format, long inter, String format) throws ParseException {
		SimpleDateFormat sdfFormat = new SimpleDateFormat(date1format);
		Date dateValue = sdfFormat.parse(date1);
		dateValue.setTime(dateValue.getTime() + inter * 24 * 3600 * 1000);
		return getFormatDateTime(dateValue, format);
	}

	/**
	 * formate date pattern
	 * 
	 * @param data
	 *            -
	 * @return date pattern to be formatted
	 */
	public static String getDateNoSpace(String data) {
		if (data == null || data.length() < 10)
			return "";
		data = data.substring(0, 4) + data.substring(5, 7) + data.substring(8, 10);
		return data;
	}

	/**
	 * get the hour of this day
	 * 
	 * @return Date
	 */
	public static Date getSpecifyHourOfDayByDate(int hour) {
		Calendar c = Calendar.getInstance();
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.MILLISECOND);
		c.clear(Calendar.HOUR_OF_DAY);
		c.set(Calendar.HOUR_OF_DAY, hour);
		return c.getTime();
	}

	public static long changeDateStringToLong(String s_date) {
		DateFormat format = new SimpleDateFormat(DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
		Date date;
		long l = System.currentTimeMillis();
		try {
			date = format.parse(s_date);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			l = c.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
	
	

	/**
	 * get the hour of this day
	 * 
	 * @return Date
	 */
	public static Date getDateOfFormat(long l_date, String s_format) {
		DateFormat format = new SimpleDateFormat(s_format);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(l_date);
		String dt = format.format(c.getTime());
		Date date = null;
		try {
			date = format.parse(dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * get the hour of this day
	 * 
	 * @return Date
	 */
	public static Date getDateOfFormat(String s_date, String s_format) {
		DateFormat format = new SimpleDateFormat(s_format);
		// Calendar c= Calendar.getInstance();
		// c.setTimeInMillis(l_date);
		// String dt=format.format(c.getTime());
		Date date = null;
		try {
			date = format.parse(s_date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * get the hour of this day
	 * 
	 * @return Date
	 */
	public static Date getDateOfFormat(Date d_date, String s_format) {
		DateFormat format = new SimpleDateFormat(s_format);
		// Calendar c= Calendar.getInstance();
		// c.setTimeInMillis(l_date);
		String dt = format.format(d_date);
		Date date = null;
		try {
			date = format.parse(dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * get the hour of this day
	 * 
	 * @return long
	 */
	public static long getSpecifyHourOfDayByLong(int hour) {
		Calendar c = Calendar.getInstance();
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.MILLISECOND);
		c.clear(Calendar.HOUR_OF_DAY);
		c.set(Calendar.HOUR_OF_DAY, hour);
		return c.getTimeInMillis();
	}

	/**
	 * @param dt1
	 *            :date 1;dt2:date 2;format:date format,such as "yyyy-MM-dd"
	 * @return result 0:equal;1:dt1>dt2;-1:dt1<dt2;other:exception
	 */
	public static int compareDatetime(String dt1, String dt2, String format) {
		int result = -100;
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date d1 = df.parse(dt1);
			Date d2 = df.parse(dt2);
			if (d1.equals(d2))
				result = 0;
			if (d1.after(d2))
				result = 1;
			if (d1.before(d2))
				result = -1;
		} catch (ParseException pe) {
			// return -100;
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(DateTimeUtil.getDateOfFormat(System.currentTimeMillis(), "MMM dd,yyyy HH:mm:ss aaa"));
	}
	
	/**
	 * 获取当前时间字符串，格式："yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	 * 
	 * @return 时间字符串
	 */
	public static String getCurrentDatetimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date now = new Date(System.currentTimeMillis()); // 获取当前时间
		return formatter.format(now);
	}

	/**
	 * 转换时间为时间字符串，格式："yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	 * 
	 * @return 时间字符串
	 */
	public static String convertCalendarToString(Calendar cal) {
		if (cal != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			return formatter.format(cal.getTime());
		}
		return "";
	}

	/**
	 * 转换时间为时间字符串，格式：自定义
	 * 
	 * @return 时间字符串
	 */
	public static String convertCalendarToString(Calendar cal, String dataFormat) {
		if (cal != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(dataFormat);
			return formatter.format(cal.getTime());
		}
		return "";
	}

	
	/**
	 * 转换时间为日历格式
	 * 
	 * @param dateString
	 *            格式:"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	 * @return
	 */
	public static Calendar convertStringToCalendar(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static Calendar stringToCalendar(String dateString, String format) {
		Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			Date d = df.parse(dateString);

			cal.setTime(d);

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return cal;
	}
	public static Calendar stringToCalendar(String dateString) {
		Calendar cal = stringToCalendar(dateString, "yyyy-MM-dd");		
		return cal;
	}
	/**
	 * 把8859-1编码转换成UTF-8编码
	 * 
	 * @param str
	 * @return
	 */
	public static String convertStringEncoding(String str) {
		try {
			return new String(str.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Error";
		}
	}
	public static String longToString(String time) {
		if (StringUtils.isEmpty(time))
			return "";
		Long l = Long.valueOf(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(l);
	}
	
	public static String longToDateString(String time) {
		if (StringUtils.isEmpty(time))
			return "";
		String s = time;
		try {
			Long l = Long.valueOf(time);
			Date d = new Date(l);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			s = sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static String dateToString(Date date){
		if(date == null){
			return "";
		}
		String s = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			s = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static String dateToString(Date date,int i){
		if(date == null){
			return "";
		}
		String s = "";
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.SECOND, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			s = sdf.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static Date longToDate(String time) {
		if (StringUtils.isEmpty(time))
			return null;
		Long l = Long.valueOf(time);
		return new Date(l);
	}
	public static Date stringToDate(String strDate) throws ParseException {
		if (strDate == null || strDate.equals("")) {
			return null;
		}

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
	}
	
	/**
	 * 
	 * <p>时间向前或向后推n秒</p>
	 * @date 2015-9-11 下午4:28:25
	 * @param date
	 * @param seconds: 正数为延迟n秒,负数为提前n秒
	 * @return String
	 * @throws
	 * @version v1.0
	 */
	public static String addSecond(Date date,Integer seconds){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date);
		startCal.add(Calendar.SECOND, seconds);
		return sdf.format(startCal.getTime());
	}
	
	
	
}