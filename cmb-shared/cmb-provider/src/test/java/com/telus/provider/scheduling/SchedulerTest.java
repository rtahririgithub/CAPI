package com.telus.provider.scheduling;

import java.util.Date;

import com.telus.provider.scheduling.AJob;
import com.telus.provider.scheduling.IJob;
import com.telus.provider.scheduling.NowTrigger;
import com.telus.provider.scheduling.OnTheHourTrigger;
import com.telus.provider.scheduling.Scheduler;

import junit.framework.TestCase;

public class SchedulerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testScheduler() throws InterruptedException {
		Scheduler sched = Scheduler.getInstance();
		sched.addJob(new OnTheHourTrigger(5000l), new ConsolePrintJob(" - OnTheHourTrigger"));
		sched.addJob(new NowTrigger(5000l), new ConsolePrintJob(" - NowTrigger"));
		sched.start();
		Thread.sleep(15000);
		sched.addJob(new OnTheHourTrigger(3000l), new ConsolePrintJob(" - OnTheHourTrigger2"));
		Thread.sleep(100000);
	}
	
	
	private class ConsolePrintJob extends AJob implements IJob {

		private String append;

		public ConsolePrintJob(String append) {
			this.append = append;
		}
		
		public void execute() {
			System.out.println (new Date() + append);
		}		
	}

}
