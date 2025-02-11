package com.telus.cmb.subscriber.utilities;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AsyncSubscriberCommitUtilities {
	private static final Log logger = LogFactory.getLog(AsyncSubscriberCommitUtilities.class);
	
	public static Date attachTimeComponent(Date logicalDate, double delaySecs) {
		Calendar calendar = Calendar.getInstance();
		Calendar logicalCalndar = Calendar.getInstance();
		logicalCalndar.setTime(logicalDate);
		
		logicalCalndar.set(logicalCalndar.get(Calendar.YEAR),
				logicalCalndar.get(Calendar.MONTH),
				logicalCalndar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));

		// Delay to deliver message
		logicalCalndar.setTimeInMillis(logicalCalndar.getTimeInMillis() + Double.valueOf(delaySecs).longValue()*1000);
		
		return logicalCalndar.getTime();
	}
}
