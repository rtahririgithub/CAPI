package com.telus.eas.framework.info;

import java.util.Calendar;
import java.util.Date;


/**
 * This class calculate the start/end date for the bill cycle to which the given date falls in
 *
 */
public class BillingCycle {
	
	private int closeDay;
	private Date startDate;
	private Date endDate;
	private Date nextCycleStartDate;
	
	private static final int MIILISECONDS_IN_DAY= (24 * 60 * 60 * 1000);
	
	private int days; 

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getNextCycleStartDate() {
		return nextCycleStartDate;
	}

	public int getCloseDay() {
		return closeDay;
	}
	
	//roll over to next billing cycle
	public BillingCycle rollover() {
		return new BillingCycle( nextCycleStartDate, closeDay );
	}
	
	//return how many days in this cycle
	public int getDays() {
		return days;
	}
	
	/**
	 * calculate the start/end date for the bill cycle to which the given date falls in
	 * 
	 * @param aDate
	 * @param cycleCloseDay
	 */
	public BillingCycle( Date aDate, int cycleCloseDay ) {
		
		this.closeDay = cycleCloseDay;
		
		Calendar cal = calculateEndDate( aDate, cycleCloseDay ); 
		endDate = cal.getTime();
		
		//move one day forward become next cycle start date
		cal.add( Calendar.DATE,1 );
		nextCycleStartDate = cal.getTime();
		
		//move one month backward become current cycle start date
		cal.add( Calendar.MONTH,-1 );
		startDate = cal.getTime();
		
		int day1 = (int) (startDate.getTime() / MIILISECONDS_IN_DAY );
		int day2 = (int) (nextCycleStartDate.getTime() / MIILISECONDS_IN_DAY);
		days = day2-day1;
	}
	
	/**
	 * Calculate bill cycle end date for the bill cycle that the given date falls in
	 *  
	 * @param aDate - any date
	 * @param cycleCloseDay
	 * @return
	 */
	private static Calendar calculateEndDate( Date aDate, int cycleCloseDay ) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime( aDate );
		
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int day = cal.get(Calendar.DATE );
		
		int maxDay = cal.getActualMaximum( Calendar.DATE );
		if ( cycleCloseDay>maxDay ) {
			cycleCloseDay= maxDay;
		}
		cal.set( Calendar.DATE, cycleCloseDay );
		
		if ( day > cycleCloseDay ) {
			cal.add( Calendar.MONTH, 1);
		}
		return cal;
	}
}
