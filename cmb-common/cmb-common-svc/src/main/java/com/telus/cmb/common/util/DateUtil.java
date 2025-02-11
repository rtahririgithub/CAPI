package com.telus.cmb.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	private static TimeZone kbTimeZone = TimeZone.getTimeZone("MST7MDT");
	private static TimeZone serverTimeZone = TimeZone.getDefault();
	/**
	 * Compare two dates regardless to the time portion
	 * @param srcDate
	 * @param refDate
	 * @return true if both dates are on the same day
	 */
	public static boolean isSameDay (Date srcDate, Date refDate) {

		if (srcDate == null || refDate == null) {
			return false;
		}
		
		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		date1.setTime(srcDate);
		date2.setTime(refDate);
		return  (date1.get(Calendar.YEAR)  == date2.get(Calendar.YEAR)) &&
			(date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)) &&
			(date1.get(Calendar.DATE)  == date2.get(Calendar.DATE));

	}
	
	/**
	 * Compare two dates regardless of the time portion.
	 * @param srcDate
	 * @param refDate
	 * @return true if both dates are on the same day or srcDate is before refDate
	 */
	public static boolean isBefore(Date srcDate, Date refDate) {
		if (srcDate == null || refDate == null) {
			return false;
		}
		
		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		
		date1.setTime(srcDate);
		date2.setTime(refDate);
		clearTimePortion(date1);
		clearTimePortion(date2);
		
		return date1.getTimeInMillis() < date2.getTimeInMillis();
	}
	
	/**
	 * Compare two dates regardless of the time portion.
	 * @param srcDate
	 * @param refDate
	 * @return true if srcDate is after refDate
	 */
	public static boolean isAfter(Date srcDate, Date refDate) {
		if (srcDate == null || refDate == null) {
			return false;
		}
		
		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		
		date1.setTime(srcDate);
		date2.setTime(refDate);
		clearTimePortion(date1);
		clearTimePortion(date2);
		
		return date1.getTimeInMillis() > date2.getTimeInMillis();
	}
	
	public static void clearTimePortion(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
	}

	public static Date addDay( Date aDate, int days ) {
		if (aDate == null) {
			return aDate;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		clearTimePortion( cal );
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public static Calendar getEODTodayCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set( Calendar.MILLISECOND, 999);
		return cal;
	}
	
	public static String getMonth(Date date) {
		String result = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = "" + cal.get(Calendar.MONTH);


		}
		return result;
	}
	
	public static String getTwoDigitMonth(Date date) {
		String result = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//increasing the month by 1 as January will be 0
			int month = cal.get(Calendar.MONTH)+1;
			//always return two digits months
			result = month<10? "0" + month : "" + month;
		}
		return result;
	}

	public static String getYear(Date date) {
		String result = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = "" + cal.get(Calendar.YEAR);
		}
		return result;
	}
	
	public static String getLastTwoDigitYear(Date date) {
		String result = getYear(date);
		if (StringUtils.isNotBlank(result) && result.length() >= 2) 
			result = result.substring(result.length()-2, result.length());
		return result;
	}
	
	public static String getDayOfMonth(Date date) {
		String result = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = "" + cal.get(Calendar.DAY_OF_MONTH);
		}
		return result;
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		try {
			GregorianCalendar gCalendar = new GregorianCalendar();
			gCalendar.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Date offsetKBSystemDateTimeToServerTimezone(Date date) {
		if (date == null) {
			return null;
		}
		int offset = kbTimeZone.getRawOffset() - serverTimeZone.getRawOffset();
		return new Date(date.getTime() + offset);
	}
}
