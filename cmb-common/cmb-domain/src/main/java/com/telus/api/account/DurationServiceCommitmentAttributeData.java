/**
 * 
 */
package com.telus.api.account;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.telus.api.util.DateUtil;

/**
 * @author x168277
 *
 */
public class DurationServiceCommitmentAttributeData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1962384446310943609L;

	public static final String TIME_ZONE_CANADA_MOUNTAIN = "Canada/Mountain";
	
	public static final String SWITCH_CODE_XHOUR = "XHOUR";
	public static final String SWITCH_CODE_3XHOUR = "3XHOUR";
	
	private static final Map provinceToUtc;
	static {
		provinceToUtc = new HashMap();
		provinceToUtc.put("AB", "UTC-0700");
		provinceToUtc.put("BC", "UTC-0800");
		provinceToUtc.put("MB", "UTC-0600");
		provinceToUtc.put("NB", "UTC-0400");
		provinceToUtc.put("NF", "UTC-0330");
		provinceToUtc.put("NS", "UTC-0400");
		provinceToUtc.put("NT", "UTC-0700");
		provinceToUtc.put("NU", "UTC-0500");
		provinceToUtc.put("ON", "UTC-0500");
		provinceToUtc.put("PE", "UTC-0400");
		provinceToUtc.put("PQ", "UTC-0500");
		provinceToUtc.put("SK", "UTC-0600");
		provinceToUtc.put("YT", "UTC-0800");
	};
	
	private static final Map utcToProvince;
	static {
		utcToProvince = new HashMap();
		utcToProvince.put("UTC-0700", "AB");
		utcToProvince.put("UTC-0800", "BC");
		utcToProvince.put("UTC-0600", "MB");
		utcToProvince.put("UTC-0400", "NB");
		utcToProvince.put("UTC-0330", "NF");
		utcToProvince.put("UTC-0400", "NS");
		utcToProvince.put("UTC-0700", "NT");
		utcToProvince.put("UTC-0500", "NU");
		utcToProvince.put("UTC-0500", "ON");
		utcToProvince.put("UTC-0400", "PE");
		utcToProvince.put("UTC-0500", "PQ");
		utcToProvince.put("UTC-0600", "SK");
		utcToProvince.put("UTC-0800", "YT");
	};
	
	private static final Map provinceToTimezone;
	static {
		provinceToTimezone = new HashMap();
		provinceToTimezone.put("AB", "Canada/Mountain");
		provinceToTimezone.put("BC", "Canada/Pacific");
		provinceToTimezone.put("MB", "Canada/Central");
		provinceToTimezone.put("NB", "Canada/Atlantic");
		provinceToTimezone.put("NF", "Canada/Newfoundland");
		provinceToTimezone.put("NS", "Canada/Atlantic");
		provinceToTimezone.put("NT", "Canada/Mountain");
		provinceToTimezone.put("NU", "Canada/Eastern");
		provinceToTimezone.put("ON", "Canada/Eastern");
		provinceToTimezone.put("PE", "Canada/Atlantic");
		provinceToTimezone.put("PQ", "Canada/Eastern");
		provinceToTimezone.put("SK", "Canada/Central");
		provinceToTimezone.put("YT", "Canada/Pacific");
	};
	
	public static final Map timezoneToUtc;
	static {
		timezoneToUtc = new HashMap();
		timezoneToUtc.put("Canada/Mountain", "UTC-0700");
		timezoneToUtc.put("Canada/Pacific", "UTC-0800");
		timezoneToUtc.put("Canada/Central", "UTC-0600");
		timezoneToUtc.put("Canada/Atlantic", "UTC-0400");
		timezoneToUtc.put("Canada/Newfoundland", "UTC-0330");
		timezoneToUtc.put("Canada/Eastern", "UTC-0500");
	};
	
	
	/**
	* The start time when the x-hour SOC is effective.  Only the hour, minute, second component of this attribute
	* are relevant.  This attribute is to be used in conjunction with ContractService.getEffectiveDate().
	* It is always in Canada/Mountain time zone.
	*/
	Calendar xhrServiceStartTime;
	
	/**
	 * Same as systemServiceStartTime time but in user's timezone representation (subscriberTimeZone)
	 */
	Calendar bpServiceStartTime;

	/**
	* The end time when the activated x-hour SOC is effective.  Only the hour, minute, second component 
	* of this attribute are relevant.  This attribute is to be used in conjunction with ContractService.getExpiryDate().
	* It is always in Canada/Mountain time zone.
	*/
	Calendar xhrServiceEndTime;
	
	/**
	 * Same as systemServiceEndTime but in user's timezone representation (subscriberTimeZone)
	 */
	Calendar bpServiceEndTime;

	/**
	* The timezone of the durationServiceStartTime and durationServiceEndTime.  If this is not explicitly set,
	* the default would be America/Toronto
	*/
	TimeZone subscriberTimeZone;
	
	/**
	 * The internal UTC-xxxx native subscriber timezone. Calculated based on the table below:
	 * Province	UTC Offset in Standard Time
		AB		UTC-0700
		BC		UTC-0800
		MB		UTC-0600
		NB		UTC-0400
		NF		UTC-0330
		NS		UTC-0400
		NT		UTC-0700
		NU		UTC-0500
		ON		UTC-0500
		PE		UTC-0400
		PQ		UTC-0500
		SK		UTC-0600
		YT		UTC-0800
	 */
	String utcSubscriberTimeZone;
	
	/**
	 * The timezone where subscriber travels - destination.
	 */
	TimeZone displayTimeZone;
	
	//private SimpleDateFormat switchFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public DurationServiceCommitmentAttributeData() {
		xhrServiceStartTime = null;
		bpServiceStartTime = null;
		xhrServiceEndTime = null;
		bpServiceEndTime = null;
		subscriberTimeZone = null;
		displayTimeZone = null;
		utcSubscriberTimeZone = null;
	}
	
	/**
	 * Parses the input string to extract range dates and time zones.
	 * The input string is expected in the following format:
	 * XHR-PARAM=XHR-START|YYYYMMDDHHMISS;XHR-END|YYYYMMDDHHMISS;XHR-TZ_ID|{java.util.TimeZone.getID()};BP_START|YYYYMMDDHHMISS;BP_END|YYYYMMDDHHMISS;BP_TZ|UTC-xxxx
	 * @param serializedSource
	 */
	public DurationServiceCommitmentAttributeData(String serializedSource) {
		// parse string and populate fields
		String systemStartString = null;
		String systemEndString = null;
		String displayStartString = null;
		String displayEndString = null;
		
		String[] splitted = serializedSource.substring(0, serializedSource.length()-1).split(";"); // chopping the last character which is '@'
		for(int i = 0; i < splitted.length; i++) {
			// parse startDate
			if(splitted[i].indexOf("XHR-START") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				systemStartString = subSplit[1];
			} 
			// parse endDate
			if(splitted[i].indexOf("XHR-END") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				systemEndString = subSplit[1];
			}
			// parse XHR-TZ_ID
			if(splitted[i].indexOf("XHR-TZ_ID") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				displayTimeZone = TimeZone.getTimeZone(subSplit[1]);
			}
			// parse BP_START
			if(splitted[i].indexOf("BP_START") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				displayStartString = subSplit[1];
			}
			// parse BP_END
			if(splitted[i].indexOf("BP_END") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				displayEndString = subSplit[1];
			}
			// parse utcSubscriberTimeZone
			if(splitted[i].indexOf("BP_TZ") >= 0) {
				String[] subSplit = splitted[i].split("\\|");
				utcSubscriberTimeZone = subSplit[1];
				if(utcToProvince.get(utcSubscriberTimeZone) != null && provinceToTimezone.get(utcToProvince.get(utcSubscriberTimeZone)) != null) {
					subscriberTimeZone = TimeZone.getTimeZone((String)provinceToTimezone.get(utcToProvince.get(utcSubscriberTimeZone)));
				}
			}
		}
		if(systemStartString != null && systemEndString != null && displayStartString != null && displayEndString != null) {
			xhrServiceStartTime = DateUtil.calendarToTimezone(null, TIME_ZONE_CANADA_MOUNTAIN);
			parseDateAndUpdateCalendar(xhrServiceStartTime, systemStartString);
			
			xhrServiceEndTime = DateUtil.calendarToTimezone(null, TIME_ZONE_CANADA_MOUNTAIN);
			parseDateAndUpdateCalendar(xhrServiceEndTime, systemEndString);
			
			bpServiceStartTime = DateUtil.calendarToTimezone(null, subscriberTimeZone.getID());
			parseDateAndUpdateCalendar(bpServiceStartTime, displayStartString);
			
			bpServiceEndTime = DateUtil.calendarToTimezone(null, subscriberTimeZone.getID());
			parseDateAndUpdateCalendar(bpServiceEndTime, displayEndString);
		} else {
			// TODO: throw runtime or continue
		}
	}

	public TimeZone getSubscriberTimeZone() {
		return subscriberTimeZone;
	}

	public void setSubscriberTimeZone(TimeZone subscriberTimeZone) {
		this.subscriberTimeZone = subscriberTimeZone;
		this.utcSubscriberTimeZone = (String)timezoneToUtc.get(subscriberTimeZone.getID());
	}

	public String getUtcSubscriberTimeZone() {
		return utcSubscriberTimeZone;
	}

	/**
	 * Represent the object in the following format:
	 * XHR-PARAM=XHR-START|YYYYMMDDHHMISS;XHR-END|YYYYMMDDHHMISS;XHR-TZ_ID|{java.util.TimeZone.getID()};BP_START|YYYYMMDDHHMISS;BP_END|YYYYMMDDHHMISS;BP_TZ|UTC-xxxx
	 */
	public String toString() {
		return serialize();
	}
	
	public static final String getTimezoneByProvince(String prvovinceCode) {
		return (String)provinceToTimezone.get(prvovinceCode);
	}
	
	public Calendar getXhrServiceStartTime() {
		return xhrServiceStartTime;
	}

	public void setXhrServiceStartTime(Calendar xhrServiceStartTime) {
		this.xhrServiceStartTime = xhrServiceStartTime;
	}

	public Calendar getXhrServiceEndTime() {
		return xhrServiceEndTime;
	}

	public void setXhrServiceEndTime(Calendar xhrServiceEndTime) {
		this.xhrServiceEndTime = xhrServiceEndTime;
	}
	
	public Calendar getBpServiceStartTime() {
		return bpServiceStartTime;
	}

	public void setBpServiceStartTime(Calendar bpServiceStartTime) {
		this.bpServiceStartTime = bpServiceStartTime;
	}
	
	public Calendar getBpServiceEndTime() {
		return bpServiceEndTime;
	}

	public void setBpServiceEndTime(Calendar bpServiceEndTime) {
		this.bpServiceEndTime = bpServiceEndTime;
	}

	public TimeZone getDisplayTimeZone() {
		return displayTimeZone;
	}

	public void setDisplayTimeZone(TimeZone displayTimeZone) {
		this.displayTimeZone = displayTimeZone;
	}

	public String serialize() {
		if(displayTimeZone != null && xhrServiceStartTime != null && xhrServiceEndTime != null && 
				bpServiceStartTime != null && bpServiceEndTime != null && utcSubscriberTimeZone != null) {
			StringBuffer sb = new StringBuffer("XHR-PARAM=XHR-START|")
				.append(formatFromCalendar(xhrServiceStartTime))
				.append(";XHR-END|")
				.append(formatFromCalendar(xhrServiceEndTime))
				.append(";XHR-TZ_ID|")
				.append(displayTimeZone.getID())
				.append(";XHR-UTC|").append(getUtcOffsetInHours(displayTimeZone.getOffset(xhrServiceStartTime.getTimeInMillis())))
				.append(";BP_START|")
				.append(formatFromCalendar(bpServiceStartTime)).append(";BP_END|")
				.append(formatFromCalendar(bpServiceEndTime))
				.append(";BP_TZ|")
				.append(utcSubscriberTimeZone)
				.append("@"); // indicates the end of the parameter value XHR-PARAM=<PARAM VALUE>@
			return sb.toString();
		} else {
			return "not initialized";
		}
	}
	
	private int getUtcOffsetInHours(int offsetInMillis) {
		return offsetInMillis/3600000;
	}
	
	/**
	 * Expected format of the dateString is yyyyMMddHHmmss.
	 * 
	 * @param cal
	 * @param dateString - expected in the 'yyyyMMddHHmmss' format
	 */
	private void parseDateAndUpdateCalendar(Calendar cal, String dateString) {
		cal.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(dateString.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(6, 8)));
		int hourOfTheDay = Integer.parseInt(dateString.substring(8, 10));
		cal.set(Calendar.AM_PM, hourOfTheDay < 12?0:1);
		cal.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
		cal.set(Calendar.HOUR, (hourOfTheDay % 12));
		cal.set(Calendar.MINUTE, Integer.parseInt(dateString.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(dateString.substring(12)));
		cal.set(Calendar.MILLISECOND, 0);
	}
	
	private String formatFromCalendar(Calendar cal) {
		StringBuffer sb = new StringBuffer(Integer.toString(cal.get(Calendar.YEAR)))
			.append(formatDateInteger(cal.get(Calendar.MONTH) + 1)) // JANUARY is 0
			.append(formatDateInteger(cal.get(Calendar.DAY_OF_MONTH)))
			.append(formatDateInteger(cal.get(Calendar.HOUR_OF_DAY)))
			.append(formatDateInteger(cal.get(Calendar.MINUTE)))
			.append(formatDateInteger(cal.get(Calendar.SECOND)));
		return sb.toString();
	}
	
	private String formatDateInteger(int i) {
		return i > 9?Integer.toString(i):"0" + Integer.toString(i);
	}
}
