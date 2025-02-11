package com.telus.provider.scheduling;

import java.util.Calendar;
import java.util.Date;

public class OnTheHourTrigger extends ATrigger implements ITrigger {

	public OnTheHourTrigger(long repeatInterval) {
		super(repeatInterval);
	}

	protected Date calculateFirstFireTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.setLenient(true);

		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		long startMillis = c.getTimeInMillis();
		long afterMillis = new Date().getTime();

		long numberOfTimesExecuted = ((afterMillis - startMillis) / super.repeatInterval) + 1;

		Date time = new Date(startMillis + (numberOfTimesExecuted * super.repeatInterval));

		return time;
	}



}
