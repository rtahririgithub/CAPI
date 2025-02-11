package com.telus.provider.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * Compare two dates regardless to the time portion
	 * @param srcDate
	 * @param refDate
	 * @return true if both dates are on the same day
	 */
	public static boolean isSameDay (Date srcDate, Date refDate) {
		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		
		date1.setTime(srcDate);
		date2.setTime(refDate);
		clearTimePortion(date1);
		clearTimePortion(date2);
		return date1.getTimeInMillis() == date2.getTimeInMillis();
	}
	
	/**
	 * Compare two dates regardless of the time portion.
	 * @param srcDate
	 * @param refDate
	 * @return true if srcDate is before refDate
	 */
	public static boolean isBefore(Date srcDate, Date refDate) {
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
	
	public static Date clearTimePortion(Date date) {
		if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        clearTimePortion(c);
        return c.getTime();
	}
	
	public static long calendarToTimezoneDate(Calendar srcDateTime, String timeZoneId) {
		return com.telus.api.util.DateUtil.calendarToTimezoneDate(srcDateTime, timeZoneId);
	}
	
	public static Calendar calendarToTimezone(Calendar srcDateTime, String timeZoneId) {
		return com.telus.api.util.DateUtil.calendarToTimezone(srcDateTime, timeZoneId);
	}
	
	public static Calendar dateToTimezoneCalendar(Date srcDate, String timeZoneId) {
		return com.telus.api.util.DateUtil.dateToTimezoneCalendar(srcDate, timeZoneId);
	}
	
}
