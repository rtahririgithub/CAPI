package com.telus.provider.scheduling;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.telus.provider.util.Logger;

public class Scheduler implements IScheduler{
	
	private SchedulerThread thread = new SchedulerThread(this.toString());
	
	private Scheduler() {
		
	}
	
	private static class SchedulerSingletonHolder {
		private static final Scheduler INSTANCE = new Scheduler();
	}
	
	public static Scheduler getInstance() {
		return SchedulerSingletonHolder.INSTANCE;
	}

	
	
	public void addJob(ITrigger trigger, IJob job) {
		thread.addJob(trigger, job);
		if (thread != null) {
			thread.interrupt();
		}
	}
	
	public void start() {
		try {
			if (!thread.isAlive()) {
				thread.setDaemon(true);
				thread.start();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	private class SchedulerThread extends Thread {
		private Map triggerJobMap = Collections.synchronizedMap(new HashMap());
		
		public SchedulerThread(String name) {
			super(name);			
		}
		
		public void addJob(ITrigger trigger, IJob job) {
			synchronized(triggerJobMap) {
				triggerJobMap.put(trigger, job);
			}
		}
		
		public void run() {
			while (true) {
				long sleepTime = Long.MAX_VALUE;
				
				synchronized(triggerJobMap) {
					Set triggers = triggerJobMap.keySet();			
					Iterator trigIter = triggers.iterator();
					Date now = new Date();					

					while (trigIter.hasNext()) {								
						ITrigger trig = (ITrigger) trigIter.next();

						if (trig.getNextFireTime() != null ) {
							if (now.getTime() > trig.getNextFireTime().getTime()) {
								IJob job = ((IJob)triggerJobMap.get(trig));
								StringBuffer sb = new StringBuffer();
								sb.append("Executing " + job.getName());
								Logger.debug(sb);
								job.execute();
								trig.fired();
							}

							long diffTime = trig.getNextFireTime().getTime() - now.getTime();
							if (diffTime < sleepTime) {
								sleepTime = diffTime;
							}		
						}
					}				
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					
				}
			}
		}		
	}
}
