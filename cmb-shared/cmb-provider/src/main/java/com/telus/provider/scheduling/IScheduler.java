package com.telus.provider.scheduling;

public interface IScheduler {

	public void addJob(ITrigger trigger, IJob job);
	
	public void start();
}
