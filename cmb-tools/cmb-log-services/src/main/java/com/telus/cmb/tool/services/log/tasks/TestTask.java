package com.telus.cmb.tool.services.log.tasks;

public class TestTask extends BaseTask implements TaskExecutor {

	@Override
	public void run() {		
		System.out.println("Testing the task scheduler...it works!");
	}

	@Override
	public String getDescription() {
		return "For testing purposes";
	}

}
