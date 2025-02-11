package com.telus.cmb.reference.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;




public class ServiceTermDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String serviceCode="";
	private int termDuration=0;
	private String termUnit="";


	public ServiceTermDto(
			String serviceCode,
		    int termDuration,
		    String termUnit
	) {
		this.serviceCode = serviceCode;
	    this.termDuration = termDuration;
	    this.termUnit = termUnit;
	    
	}

	
	public String toString()
	  {
	      StringBuffer s = new StringBuffer(128);

	      s.append("ServiceTermDto:[\n");
	      s.append("    serviceCode=[").append(serviceCode.trim()).append("]\n");
	      s.append("    termDuration=[").append(termDuration).append("]\n");
	      s.append("    termUnit=[").append(termUnit.trim()).append("]\n");
	      s.append("]");

	      return s.toString();
	  }

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}


	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	/**
	 * @return the termDuration
	 */
	public int getTermDuration() {
		return termDuration;
	}


	/**
	 * @param termDuration the termDuration to set
	 */
	public void setTermDuration(int termDuration) {
		this.termDuration = termDuration;
	}


	/**
	 * @return the termUnit
	 */
	public String getTermUnit() {
		return termUnit;
	}


	/**
	 * @param termUnit the termUnit to set
	 */
	public void setTermUnit(String termUnit) {
		this.termUnit = termUnit;
	}
	
	
	public static Calendar truncateDate( Date aDate ) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis( aDate.getTime() );
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static Date advanceDateByMonth( Date aDate, int termMonths ) {
		Calendar cal = truncateDate( aDate );
		cal.add(Calendar.MONTH, termMonths);
		return cal.getTime();
	}
	
	public static Date advanceDateByDay( Date aDate, int days ) {
		Calendar cal = truncateDate( aDate );
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public Date calculateExpirationDate( Date effectiveDate ) {
		
		if (getTermUnit().equals("M")) { //month
			return advanceDateByMonth( effectiveDate, getTermDuration() );
		} else if (getTermUnit().equals("D")) { //days
			return advanceDateByDay( effectiveDate, getTermDuration() );
		} else {
			//this shall never happen
			throw new RuntimeException("Unkown termUnit[" + getTermUnit() +"] : data issue.");
		}
	}
	
}
