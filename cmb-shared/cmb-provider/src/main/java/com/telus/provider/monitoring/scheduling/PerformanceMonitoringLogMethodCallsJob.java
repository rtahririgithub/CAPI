package com.telus.provider.monitoring.scheduling;

import com.telus.provider.TMProvider;
import com.telus.provider.scheduling.AJob;
import com.telus.provider.scheduling.IJob;

public class PerformanceMonitoringLogMethodCallsJob extends AJob implements IJob {

	public void execute() {
		TMProvider.logMethodCalls();
	}
}
