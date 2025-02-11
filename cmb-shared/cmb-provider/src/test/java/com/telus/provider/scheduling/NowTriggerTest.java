package com.telus.provider.scheduling;

import java.util.Date;

import com.telus.provider.scheduling.NowTrigger;

import junit.framework.TestCase;

public class NowTriggerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testNowTrigger() throws InterruptedException {
		NowTrigger nt = new NowTrigger(5000L);
		
		System.out.println (new Date());
		System.out.println(nt.getNextFireTime());
		Thread.sleep(2000);
		nt.fired();
		System.out.println(nt.getNextFireTime());
	}
}
