package com.telus.api.dealer;

public interface HoursOfOperation {

	/**
	 * <CODE>HoursOfOperation</CODE>
	 *  
	 */

	public final static int HOLIDAY = 0;

	public final static int MONDAY = 1;

	public final static int TUESDAY = 2;

	public final static int WEDNESDAY = 3;

	public final static int THURSDAY = 4;

	public final static int FRIDAY = 5;

	public final static int SATURDAY = 6;

	public final static int SUNDAY = 7;

	int getDay();

	String getOpenTime();

	String getCloseTime();

}