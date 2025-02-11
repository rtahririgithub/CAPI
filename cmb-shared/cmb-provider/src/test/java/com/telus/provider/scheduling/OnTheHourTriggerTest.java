package com.telus.provider.scheduling;

import java.util.Date;

import com.telus.provider.scheduling.OnTheHourTrigger;

import junit.framework.TestCase;

public class OnTheHourTriggerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCalculateFirstFireTime() throws InterruptedException {
		OnTheHourTrigger onTheHourTrigger = new OnTheHourTrigger(5000);
		
		System.out.println(new Date());
		System.out.println(onTheHourTrigger.getNextFireTime());
		
		Thread.sleep(2000);
		onTheHourTrigger.fired();
		System.out.println(onTheHourTrigger.getNextFireTime());
		
		Thread.sleep(4000);
		onTheHourTrigger.fired();
		System.out.println(onTheHourTrigger.getNextFireTime());
	}
}
