package com.telus.provider.scheduling;

import java.util.Date;

public abstract class ATrigger implements ITrigger{
	
	protected long repeatInterval;
	private Date nextFireTime = null;

	public ATrigger(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}


	public Date getNextFireTime() {
		if (nextFireTime == null) {
			this.nextFireTime = calculateFirstFireTime();
		}
		return this.nextFireTime;
	}
	
	protected abstract Date calculateFirstFireTime();

	public void fired() {
		Date nowDate = new Date();
		while (this.nextFireTime.getTime() < nowDate.getTime()) {		
			this.nextFireTime = new Date(this.nextFireTime.getTime() + repeatInterval);
		}
	}

}
